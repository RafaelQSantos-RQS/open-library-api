package br.com.openlibrary.open_library.dto.item;

import br.com.openlibrary.open_library.model.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record ItemPutDto(
        @NotBlank String title,
        @NotEmpty(message = "Item must have at least one author")
        Set<Long> authorIds,
        @NotNull ItemType itemType,
        @NotNull @Positive Long subjectAreaId
) {
}
