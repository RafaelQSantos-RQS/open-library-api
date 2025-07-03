package br.com.openlibrary.open_library.dto.item;

import br.com.openlibrary.open_library.dto.subject_area.SubjectAreaDTO;
import br.com.openlibrary.open_library.model.ItemType;

import java.time.LocalDateTime;

public record ItemResponseDTO(
        Long id,
        String title,
        String author,
        ItemType itemType,
        SubjectAreaDTO subjectArea, // Retornamos um DTO aninhado para a área temática
        int totalQuantity,
        int availableQuantity,
        LocalDateTime createdAt
) {
}
