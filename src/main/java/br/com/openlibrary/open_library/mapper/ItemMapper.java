package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.item.ItemPatchDto;
import br.com.openlibrary.open_library.dto.item.ItemResponseDto;
import br.com.openlibrary.open_library.model.Item;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    /**
     * Converts an Item entity to an ItemDTO.
     *
     * @param item the Item entity to be converted
     * @return the converted ItemDTO
     */
    ItemResponseDto toResponseDto(Item item);

    /**
     * Partially updates the fields of an Item entity with values from an ItemUpdateDTO.
     * Only the fields present in the ItemUpdateDTO are updated; null values are ignored.
     *
     * @param item         the Item entity to be updated
     * @param itemPatchDto the data transfer object containing updated item information
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authors", ignore = true)
    void partialUpdate(@MappingTarget Item item, ItemPatchDto itemPatchDto);
}
