package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.loan.LoanHistoryItemDTO;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDTO;
import br.com.openlibrary.open_library.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface LoanMapper {

    @Mapping(source = "item", target = "item")
    @Mapping(source = "user", target = "user")
    LoanResponseDTO toResponseDto(Loan loan);

    LoanHistoryItemDTO toHistoryItemDto(Loan loan);
}
