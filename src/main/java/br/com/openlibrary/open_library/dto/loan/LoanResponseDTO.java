package br.com.openlibrary.open_library.dto.loan;

import br.com.openlibrary.open_library.dto.item.ItemResponseDTO;
import br.com.openlibrary.open_library.dto.user.UserDTO;
import br.com.openlibrary.open_library.model.LoanStatus;

import java.time.LocalDate;

public record LoanResponseDTO(
        Long id,
        UserDTO user,
        ItemResponseDTO item,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        LoanStatus status
) {
}
