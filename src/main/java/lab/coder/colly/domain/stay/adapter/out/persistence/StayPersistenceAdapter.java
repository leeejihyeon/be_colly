package lab.coder.colly.domain.stay.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.AccommodationEntity;
import lab.coder.colly.domain.community.adapter.out.persistence.entity.UserStayEntity;
import lab.coder.colly.domain.stay.adapter.out.persistence.repository.AccommodationJpaRepository;
import lab.coder.colly.domain.stay.adapter.out.persistence.repository.StayUserJpaRepository;
import lab.coder.colly.domain.stay.adapter.out.persistence.repository.UserStayJpaRepository;
import lab.coder.colly.domain.stay.application.port.out.AccommodationPort;
import lab.coder.colly.domain.stay.application.port.out.StayUserPort;
import lab.coder.colly.domain.stay.application.port.out.UserStayPort;
import lab.coder.colly.domain.stay.domain.model.Accommodation;
import lab.coder.colly.domain.stay.domain.model.UserStay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StayPersistenceAdapter implements AccommodationPort, UserStayPort, StayUserPort {

    private final AccommodationJpaRepository accommodationJpaRepository;
    private final UserStayJpaRepository userStayJpaRepository;
    private final StayUserJpaRepository stayUserJpaRepository;

    @Override
    public List<Accommodation> findByCountryCodeAndCityCode(String countryCode, String cityCode) {
        return accommodationJpaRepository.findByCountryCodeAndCityCodeOrderByNameAsc(countryCode, cityCode).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Accommodation> findAccommodationById(Long accommodationId) {
        return accommodationJpaRepository.findById(accommodationId)
                .map(this::toDomain);
    }

    @Override
    public UserStay save(UserStay userStay) {
        UserStayEntity saved = userStayJpaRepository.save(
                new UserStayEntity(
                        userStay.getId(),
                        userStay.getUserId(),
                        userStay.getAccommodationId(),
                        userStay.getCheckIn(),
                        userStay.getCheckOut(),
                        null
                )
        );
        return toDomain(saved);
    }

    @Override
    public Optional<UserStay> findById(Long stayId) {
        return userStayJpaRepository.findById(stayId)
                .map(this::toDomain);
    }

    @Override
    public List<UserStay> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return userStayJpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsDuplicate(Long userId, Long accommodationId, LocalDate checkIn, LocalDate checkOut, Long excludeStayId) {
        if (excludeStayId == null) {
            return userStayJpaRepository.existsByUserIdAndAccommodationIdAndCheckInAndCheckOut(
                    userId,
                    accommodationId,
                    checkIn,
                    checkOut
            );
        }
        return userStayJpaRepository.existsByUserIdAndAccommodationIdAndCheckInAndCheckOutAndIdNot(
                userId,
                accommodationId,
                checkIn,
                checkOut,
                excludeStayId
        );
    }

    @Override
    public void delete(UserStay userStay) {
        userStayJpaRepository.deleteById(userStay.getId());
    }

    @Override
    public List<UserStay> findOverlaps(Long accommodationId, LocalDate checkIn, LocalDate checkOut, Long excludeUserId) {
        return userStayJpaRepository
                .findByAccommodationIdAndCheckInLessThanEqualAndCheckOutGreaterThanEqualAndUserIdNot(
                        accommodationId,
                        checkOut,
                        checkIn,
                        excludeUserId
                )
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Map<Long, String> findNamesByIds(Set<Long> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }
        return stayUserJpaRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(
                        lab.coder.colly.domain.user.adapter.out.persistence.entity.UserEntity::getId,
                        lab.coder.colly.domain.user.adapter.out.persistence.entity.UserEntity::getName,
                        (left, right) -> left
                ));
    }

    private Accommodation toDomain(AccommodationEntity entity) {
        return Accommodation.restore(
                entity.getId(),
                entity.getCountryCode(),
                entity.getCityCode(),
                entity.getName(),
                entity.getAddressLine1(),
                entity.getAddressLine2()
        );
    }

    private UserStay toDomain(UserStayEntity entity) {
        return UserStay.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getAccommodationId(),
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getCreatedAt()
        );
    }
}
