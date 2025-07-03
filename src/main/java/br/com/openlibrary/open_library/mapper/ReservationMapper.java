package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.reservation.ReservationResponseDTO;
import br.com.openlibrary.open_library.model.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface ReservationMapper {

    /**
     * Converts a Reservation entity to a ReservationResponseDTO.
     *
     * @param reservation the Reservation entity to be converted
     * @return the converted ReservationResponseDTO
     */
    ReservationResponseDTO toResponseDto(Reservation reservation);
}
