package br.com.openlibrary.open_library.dto.reservation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservationCreateDTO(
        @NotNull(message = "User ID cannot be null")
        @Positive(message = "User ID must be a positive number")
        Long userId,

        @NotNull(message = "Item ID cannot be null")
        @Positive(message = "Item ID must be a positive number")
        Long itemId
) {
}