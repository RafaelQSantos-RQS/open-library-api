package br.com.openlibrary.open_library.dto.item;

import br.com.openlibrary.open_library.model.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record ItemCreateDto(

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotEmpty(message = "Item must have at least one author")
        Set<Long> authorIds,

        @NotNull(message = "Item type cannot be blank")
        ItemType itemType,

        @NotNull(message = "Subject area cannot be blank")
        @Positive(message = "Subject area id must be positive")
        Long subjectAreaId
) {
}
