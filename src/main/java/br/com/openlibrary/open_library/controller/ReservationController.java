package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.loan.LoanResponseDto;
import br.com.openlibrary.open_library.dto.reservation.ReservationCreateDto;
import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDto;
import br.com.openlibrary.open_library.service.reservation.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationCreateDto reservationCreateDTO) {
        ReservationResponseDto reservationResponseDTO = reservationService.createReservation(reservationCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(reservationResponseDTO);
    }

    @PostMapping("/{id}/fullfill")
    public ResponseEntity<LoanResponseDto> fullfillReservation(@PathVariable Long id) {
        LoanResponseDto loanResponseDTO = reservationService.fullfillReservation(id);
        return ResponseEntity.ok(loanResponseDTO);
    }
}
