package br.com.openlibrary.open_library.dto.loan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoanCreateDTO(

        @NotNull(message = "Item id cannot be null")
        @Positive(message = "Item id must be positive")
        Long userId,

        @NotNull(message = "Item ID cannot be null")
        @Positive(message = "Item ID must be a positive number")
        Long itemId
) {
}
