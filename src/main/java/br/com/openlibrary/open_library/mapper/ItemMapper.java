package br.com.openlibrary.open_library.mapper;

import br.com.openlibrary.open_library.dto.item.ItemPatchDTO;
import br.com.openlibrary.open_library.dto.item.ItemResponseDTO;
import br.com.openlibrary.open_library.dto.item.ItemPutDTO;
import br.com.openlibrary.open_library.model.Item;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    /**
     * Converts an Item entity to an ItemDTO.
     *
     * @param item the Item entity to be converted
     * @return the converted ItemDTO
     */
    ItemResponseDTO toResponseDto(Item item);

    /**
     * Partially updates the fields of an Item entity with values from an ItemUpdateDTO.
     * Only the fields present in the ItemUpdateDTO are updated; null values are ignored.
     *
     * @param item         the Item entity to be updated
     * @param itemPatchDTO the data transfer object containing updated item information
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Item item, ItemPatchDTO itemPatchDTO);
}
