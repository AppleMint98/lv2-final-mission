package finalmission.service;

import finalmission.domain.entity.Reservation;
import finalmission.dto.ReservationDetailResponse;
import finalmission.dto.ReservationResponse;
import finalmission.repository.ReservationRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllMemberReservations(Long memberId) {
        List<Reservation> byMemberId = reservationRepository.findByMemberId(memberId);
        List<ReservationResponse> responses = new ArrayList<>();
        for (Reservation reservation : byMemberId) {
            responses.add(ReservationResponse.from(reservation));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public ReservationDetailResponse findMemberReservationDetail(Long reservationId) {
        Reservation byId = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        // TODO: 본인만 예약 상세내용 확인가능하도록 변경
        return ReservationDetailResponse.from(byId);
    }
}
