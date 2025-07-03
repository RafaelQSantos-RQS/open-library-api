package br.com.openlibrary.open_library.dto.loan;

import br.com.openlibrary.open_library.dto.item.ItemResponseDto;
import br.com.openlibrary.open_library.model.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanHistoryItemDto(
        Long id,
        ItemResponseDto item, // Mantemos os detalhes do item
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        LoanStatus status,
        BigDecimal fineAmount
) {
}
