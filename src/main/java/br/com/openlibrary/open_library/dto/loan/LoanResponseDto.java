package br.com.openlibrary.open_library.dto.loan;

import br.com.openlibrary.open_library.dto.item.ItemResponseDto;
import br.com.openlibrary.open_library.dto.user.UserDto;
import br.com.openlibrary.open_library.model.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanResponseDto(
        Long id,
        UserDto user,
        ItemResponseDto item,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        LoanStatus status,
        Long originatingReservationId,
        BigDecimal fineAmount
) {
}
