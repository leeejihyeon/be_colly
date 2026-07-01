package lab.coder.colly.domain.stay.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lab.coder.colly.domain.stay.application.port.in.CreateUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.in.ListStayOverlapsUseCase;
import lab.coder.colly.domain.stay.application.port.in.UpdateUserStayUseCase;
import lab.coder.colly.domain.stay.application.port.out.AccommodationPort;
import lab.coder.colly.domain.stay.application.port.out.StayUserPort;
import lab.coder.colly.domain.stay.application.port.out.UserStayPort;
import lab.coder.colly.domain.stay.domain.model.Accommodation;
import lab.coder.colly.domain.stay.domain.model.UserStay;
import lab.coder.colly.shared.error.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StayServiceTest {

    @Mock
    private AccommodationPort accommodationPort;

    @Mock
    private UserStayPort userStayPort;

    @Mock
    private StayUserPort stayUserPort;

    @InjectMocks
    private StayService stayService;

    @Test
    void create_rejectsInvalidDateRange() {
        assertThatThrownBy(() -> stayService.create(
                new CreateUserStayUseCase.CreateUserStayCommand(
                        1L,
                        10L,
                        LocalDate.of(2026, 7, 10),
                        LocalDate.of(2026, 7, 9)
                )
        )).isInstanceOf(DomainException.class);
    }

    @Test
    void create_rejectsDuplicateStay() {
        when(accommodationPort.findAccommodationById(10L)).thenReturn(Optional.of(
                Accommodation.restore(10L, "AU", "SYD", "Wake Up", null, null)
        ));
        when(userStayPort.existsDuplicate(1L, 10L, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5), null))
                .thenReturn(true);

        assertThatThrownBy(() -> stayService.create(
                new CreateUserStayUseCase.CreateUserStayCommand(
                        1L,
                        10L,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 5)
                )
        )).isInstanceOf(DomainException.class);
    }

    @Test
    void update_rejectsWhenStayOwnerDoesNotMatch() {
        when(userStayPort.findById(5L)).thenReturn(Optional.of(
                UserStay.restore(5L, 2L, 10L, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5), LocalDateTime.now())
        ));

        assertThatThrownBy(() -> stayService.update(
                new UpdateUserStayUseCase.UpdateUserStayCommand(
                        5L,
                        1L,
                        10L,
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 6)
                )
        )).isInstanceOf(DomainException.class);
    }

    @Test
    void listOverlaps_returnsOverlapWindowAndNickname() {
        when(userStayPort.findById(1L)).thenReturn(Optional.of(
                UserStay.restore(1L, 7L, 10L, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 20), LocalDateTime.now())
        ));
        when(userStayPort.findOverlaps(10L, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 20), 7L)).thenReturn(List.of(
                UserStay.restore(2L, 11L, 10L, LocalDate.of(2026, 7, 15), LocalDate.of(2026, 7, 25), LocalDateTime.now())
        ));
        when(stayUserPort.findNamesByIds(java.util.Set.of(11L))).thenReturn(Map.of(11L, "Mina"));

        var result = stayService.listOverlaps(new ListStayOverlapsUseCase.ListStayOverlapsQuery(1L, 7L));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nickname()).isEqualTo("Mina");
        assertThat(result.get(0).overlapStartDate()).isEqualTo(LocalDate.of(2026, 7, 15));
        assertThat(result.get(0).overlapEndDate()).isEqualTo(LocalDate.of(2026, 7, 20));
    }
}
