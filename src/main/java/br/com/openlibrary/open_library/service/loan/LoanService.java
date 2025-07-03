package br.com.openlibrary.open_library.service.loan;

import br.com.openlibrary.open_library.dto.loan.LoanCreateDto;
import br.com.openlibrary.open_library.dto.loan.LoanHistoryItemDto;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.model.LoanStatus;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService {
    /**
     * Create a new loan. This method will validate the given loanCreateDTO,
     * create a new Loan entity and update the item quantity.
     *
     * @param loanCreateDTO the LoanCreateDto with the data to create a new loan.
     * @return a LoanResponseDto with the created Loan data.
     */
    LoanResponseDto createLoan(LoanCreateDto loanCreateDTO);
    /**
     * Updates the status of overdue loans to {@link LoanStatus#OVERDUE}. This
     * method is meant to be used by a scheduler to update the status of all
     * overdue loans periodically.
     */
    void updateOverdueLoansStatus();
    /**
     * Retrieves a loan by its unique identifier. This method will return a
     * LoanResponseDto with the details of the loan if found. If the loan with the
     * specified id is not found, an IllegalArgumentException will be thrown.
     *
     * @param id the unique identifier of the loan to be retrieved.
     * @return a LoanResponseDto containing the details of the loan if found.
     * @throws IllegalArgumentException if the loan with the specified id is not found.
     */
    LoanResponseDto returnLoan(Long id);
    /**
     * Retrieves a page of {@link LoanResponseDto} containing the details of all
     * loans of the user with the specified id.
     *
     * @param userId the unique identifier of the user whose loans will be
     *               retrieved.
     * @param pageable the pagination details.
     * @return a PageDto containing the requested page of LoanResponseDTOs.
     */
    PageDto<LoanHistoryItemDto> findLoansByUserId(Long userId, Pageable pageable);
    /**
     * Renews a loan, given its unique identifier. This method will update the
     * Loan entity and return a LoanResponseDto with the renewed Loan data.
     *
     * @param loanId the unique identifier of the loan to be renewed.
     * @return an Optional containing a LoanResponseDto with the renewed Loan
     * data if the loan was found and renewed, or an empty Optional if the loan
     * with the specified id is not found.
     */
    Optional<LoanResponseDto> renewLoan(Long loanId);
}
