package finalmission.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import finalmission.domain.entity.Manager;
import finalmission.domain.entity.Member;
import finalmission.domain.entity.Reservation;
import finalmission.domain.entity.Tour;
import finalmission.domain.vo.MemberRole;
import finalmission.dto.ReservationDetailResponse;
import finalmission.dto.ReservationResponse;
import finalmission.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;
    @InjectMocks
    ReservationService reservationService;

    @Test
    void findAllMemberReservationsTest() {
        // given
        Reservation reservation1 = createReservationByReservationIdAndMemberId(1L, 1L);
        Reservation reservation2 = createReservationByReservationIdAndMemberId(2L, 1L);
        when(reservationRepository.findByMemberId(1L)).thenReturn(List.of(reservation1, reservation2));

        // when
        List<ReservationResponse> responses = reservationService.findAllMemberReservations(1L);

        // then
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void findMemberReservationDetailTest() {
        // given
        Reservation reservation = createReservationByReservationIdAndMemberId(1L, 1L);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // when
        ReservationDetailResponse response = reservationService.findMemberReservationDetail(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(ReservationDetailResponse.class);
    }

    @DisplayName("조회된 예약이 없을 경우 예외를 발생한다.")
    @Test
    void throwErrorWhenReservationNotFoundTest() {
        // given
        when(reservationRepository.findById(2L)).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> reservationService.findMemberReservationDetail(2L))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("Reservation not found");
    }

    private Reservation createReservationByReservationIdAndMemberId(Long reservationId, Long memberId) {
        return new Reservation(
                reservationId,
                new Member(memberId, "member1@email.com", "Password123!@#", MemberRole.USER),
                new Manager(1L, "Peter", "010-1234-5678"),
                new Tour(1L, "title", "description"),
                LocalDate.of(2025, 6, 10),
                LocalTime.of(10, 30)
        );
    }
}
