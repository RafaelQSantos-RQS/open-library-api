package br.com.openlibrary.open_library.dto.item;

import br.com.openlibrary.open_library.dto.author.AuthorDto;
import br.com.openlibrary.open_library.dto.subject_area.SubjectAreaDto;
import br.com.openlibrary.open_library.model.ItemType;

import java.time.LocalDateTime;
import java.util.Set;

public record ItemResponseDto(
        Long id,
        String title,
        Set<AuthorDto> authors,
        ItemType itemType,
        SubjectAreaDto subjectArea, // Retornamos um DTO aninhado para a área temática
        int totalQuantity,
        int availableQuantity,
        LocalDateTime createdAt
) {
}
