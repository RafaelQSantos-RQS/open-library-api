package br.com.openlibrary.open_library.dto.reservation;

import br.com.openlibrary.open_library.model.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public record ReservationUpdateDTO(
        @NotNull(message = "Status cannot be null")
        ReservationStatus status
) {
}
