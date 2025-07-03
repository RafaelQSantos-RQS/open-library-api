package br.com.openlibrary.open_library.dto.item;

import br.com.openlibrary.open_library.model.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPutDTO(
        @NotBlank String title,
        @NotBlank String author,
        @NotNull ItemType itemType,
        @NotNull @Positive Long subjectAreaId
) {
}
