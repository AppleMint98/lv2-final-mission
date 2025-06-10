package finalmission.controller;

import finalmission.dto.ReservationDetailResponse;
import finalmission.dto.ReservationResponse;
import finalmission.service.ReservationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{memberId}")
    public ResponseEntity<List<ReservationResponse>> findAllByMemberId(@PathVariable("memberId") Long memberId) {
        List<ReservationResponse> response = reservationService.findAllMemberReservations(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDetailResponse> findReservationDetail(@PathVariable("reservationId") Long reservationId) {
        ReservationDetailResponse response = reservationService.findMemberReservationDetail(reservationId);
        return ResponseEntity.ok(response);
    }
}
