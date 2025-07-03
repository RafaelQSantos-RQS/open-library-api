package br.com.openlibrary.open_library.dto.reservation;

import br.com.openlibrary.open_library.dto.item.ItemResponseDto;
import br.com.openlibrary.open_library.dto.user.UserDto;
import br.com.openlibrary.open_library.model.ReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponseDto(
        Long id,
        UserDto user,
        ItemResponseDto item,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
