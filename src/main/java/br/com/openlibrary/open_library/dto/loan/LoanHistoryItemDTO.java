package br.com.openlibrary.open_library.dto.loan;

import br.com.openlibrary.open_library.dto.item.ItemResponseDTO;
import br.com.openlibrary.open_library.model.LoanStatus;

import java.time.LocalDate;

public record LoanHistoryItemDTO(
        Long id,
        ItemResponseDTO item, // Mantemos os detalhes do item
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        LoanStatus status
) {
}
