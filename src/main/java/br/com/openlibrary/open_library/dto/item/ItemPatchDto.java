package br.com.openlibrary.open_library.dto.item;

import br.com.openlibrary.open_library.model.ItemType;

import java.util.Set;

public record ItemPatchDto(
        String title,
        Set<Long> authorIds,
        ItemType itemType,
        Long subjectAreaId
) {
}
