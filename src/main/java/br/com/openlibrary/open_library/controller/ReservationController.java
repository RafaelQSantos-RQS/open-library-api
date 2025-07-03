package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.reservation.ReservationCreateDTO;
import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDTO;
import br.com.openlibrary.open_library.service.reservation.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationCreateDTO reservationCreateDTO) {
        ReservationResponseDTO reservationResponseDTO = reservationService.createReservation(reservationCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(reservationResponseDTO);
    }
}
