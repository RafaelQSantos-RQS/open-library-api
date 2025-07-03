package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.loan.LoanHistoryItemDto;
import br.com.openlibrary.open_library.dto.loan.LoanResponseDto;
import br.com.openlibrary.open_library.model.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface LoanMapper {

    @Mapping(source = "item", target = "item")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "reservation.id", target = "originatingReservationId")
    LoanResponseDto toResponseDto(Loan loan);

    LoanHistoryItemDto toHistoryItemDto(Loan loan);
}
