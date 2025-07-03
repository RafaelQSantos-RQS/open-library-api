package br.com.openlibrary.open_library.dto.reservation;

import br.com.openlibrary.open_library.dto.item.ItemResponseDTO;
import br.com.openlibrary.open_library.dto.user.UserDTO;
import br.com.openlibrary.open_library.model.ReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponseDTO(
        Long id,
        UserDTO user,
        ItemResponseDTO item,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
