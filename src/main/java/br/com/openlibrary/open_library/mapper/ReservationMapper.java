package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDto;
import br.com.openlibrary.open_library.model.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface ReservationMapper {

    /**
     * Converts a Reservation entity to a ReservationResponseDto.
     *
     * @param reservation the Reservation entity to be converted
     * @return the converted ReservationResponseDto
     */
    ReservationResponseDto toResponseDto(Reservation reservation);
}
