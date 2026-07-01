package lab.coder.colly.domain.stay.application.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lab.coder.colly.domain.stay.application.port.in.AccommodationView;
import lab.coder.colly.domain.stay.application.port.in.CreateUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.in.DeleteUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.in.ListAccommodationsUseCase;
import lab.coder.colly.domain.stay.application.port.in.ListMyUserStaysUseCase;
import lab.coder.colly.domain.stay.application.port.in.ListStayOverlapsUseCase;
import lab.coder.colly.domain.stay.application.port.in.StayOverlapView;
import lab.coder.colly.domain.stay.application.port.in.UpdateUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.in.UserStayView;
import lab.coder.colly.domain.stay.application.port.out.AccommodationPort;
import lab.coder.colly.domain.stay.application.port.out.StayUserPort;
import lab.coder.colly.domain.stay.application.port.out.UserStayPort;
import lab.coder.colly.domain.stay.domain.model.Accommodation;
import lab.coder.colly.domain.stay.domain.model.UserStay;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StayService implements
        ListAccommodationsUseCase,
        CreateUserStayUseCase,
        ListMyUserStaysUseCase,
        UpdateUserStayUseCase,
        DeleteUserStayUseCase,
        ListStayOverlapsUseCase {

    private final AccommodationPort accommodationPort;
    private final UserStayPort userStayPort;
    private final StayUserPort stayUserPort;

    @Override
    public List<AccommodationView> list(ListAccommodationsQuery query) {
        return accommodationPort.findByCountryCodeAndCityCode(query.countryCode(), query.cityCode()).stream()
                .map(this::toAccommodationView)
                .toList();
    }

    @Override
    @Transactional
    public UserStayView create(CreateUserStayCommand command) {
        validateDateRange(command.checkIn(), command.checkOut());
        ensureAccommodationExists(command.accommodationId());
        ensureNoDuplicate(command.userId(), command.accommodationId(), command.checkIn(), command.checkOut(), null);

        UserStay saved = userStayPort.save(
                UserStay.create(
                        command.userId(),
                        command.accommodationId(),
                        command.checkIn(),
                        command.checkOut()
                )
        );

        return toUserStayView(saved);
    }

    @Override
    public List<UserStayView> listMyStays(Long userId) {
        return userStayPort.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toUserStayView)
                .toList();
    }

    @Override
    @Transactional
    public UserStayView update(UpdateUserStayCommand command) {
        validateDateRange(command.checkIn(), command.checkOut());
        ensureAccommodationExists(command.accommodationId());

        UserStay existing = getOwnedStay(command.stayId(), command.userId());
        ensureNoDuplicate(command.userId(), command.accommodationId(), command.checkIn(), command.checkOut(), existing.getId());

        UserStay updated = userStayPort.save(
                existing.change(
                        command.accommodationId(),
                        command.checkIn(),
                        command.checkOut()
                )
        );

        return toUserStayView(updated);
    }

    @Override
    @Transactional
    public void delete(DeleteUserStayCommand command) {
        UserStay existing = getOwnedStay(command.stayId(), command.userId());
        userStayPort.delete(existing);
    }

    @Override
    public List<StayOverlapView> listOverlaps(ListStayOverlapsQuery query) {
        UserStay stay = getOwnedStay(query.stayId(), query.userId());

        List<UserStay> overlaps = userStayPort.findOverlaps(
                stay.getAccommodationId(),
                stay.getCheckIn(),
                stay.getCheckOut(),
                stay.getUserId()
        );

        Map<Long, String> namesByUserId = stayUserPort.findNamesByIds(
                overlaps.stream().map(UserStay::getUserId).collect(java.util.stream.Collectors.toSet())
        );

        return overlaps.stream()
                .map(overlap -> new StayOverlapView(
                        overlap.getUserId(),
                        namesByUserId.getOrDefault(overlap.getUserId(), "Unknown"),
                        max(stay.getCheckIn(), overlap.getCheckIn()),
                        min(stay.getCheckOut(), overlap.getCheckOut())
                ))
                .sorted(Comparator.comparing(StayOverlapView::overlapStartDate)
                        .thenComparing(StayOverlapView::userId))
                .toList();
    }

    private UserStay getOwnedStay(Long stayId, Long userId) {
        UserStay stay = userStayPort.findById(stayId)
                .orElseThrow(() -> new DomainException(ErrorCode.STAY_NOT_FOUND, "Stay not found: " + stayId));

        if (!stay.getUserId().equals(userId)) {
            throw new DomainException(ErrorCode.FORBIDDEN_ACTION, "Only owner can access stay");
        }
        return stay;
    }

    private void ensureAccommodationExists(Long accommodationId) {
        accommodationPort.findAccommodationById(accommodationId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.ACCOMMODATION_NOT_FOUND,
                        "Accommodation not found: " + accommodationId
                ));
    }

    private void ensureNoDuplicate(Long userId, Long accommodationId, LocalDate checkIn, LocalDate checkOut, Long excludeStayId) {
        if (userStayPort.existsDuplicate(userId, accommodationId, checkIn, checkOut, excludeStayId)) {
            throw new DomainException(ErrorCode.DUPLICATE_STAY, "Duplicate stay already exists");
        }
    }

    private void validateDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || checkIn.isAfter(checkOut)) {
            throw new DomainException(ErrorCode.INVALID_STAY_DATE_RANGE, "checkIn must be before or equal to checkOut");
        }
    }

    private LocalDate max(LocalDate left, LocalDate right) {
        return left.isAfter(right) ? left : right;
    }

    private LocalDate min(LocalDate left, LocalDate right) {
        return left.isBefore(right) ? left : right;
    }

    private AccommodationView toAccommodationView(Accommodation accommodation) {
        return new AccommodationView(
                accommodation.getId(),
                accommodation.getCountryCode(),
                accommodation.getCityCode(),
                accommodation.getName(),
                accommodation.getAddressLine1(),
                accommodation.getAddressLine2()
        );
    }

    private UserStayView toUserStayView(UserStay userStay) {
        return new UserStayView(
                userStay.getId(),
                userStay.getAccommodationId(),
                userStay.getCheckIn(),
                userStay.getCheckOut(),
                userStay.getCreatedAt()
        );
    }
}
