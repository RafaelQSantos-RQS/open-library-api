package br.com.openlibrary.open_library.service.item;

import br.com.openlibrary.open_library.dto.item.ItemCreateDTO;
import br.com.openlibrary.open_library.dto.item.ItemPatchDTO;
import br.com.openlibrary.open_library.dto.item.ItemPutDTO;
import br.com.openlibrary.open_library.dto.item.ItemResponseDTO;
import br.com.openlibrary.open_library.dto.page.PageDTO;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemService {
    /**
     * Creates a new item.
     *
     * @param itemCreateDTO the data transfer object containing information about the item to be created
     * @return the created item
     */
    ItemResponseDTO createItem(ItemCreateDTO itemCreateDTO);
    /**
     * Finds all items in the database.
     *
     * @param pageable the pagination object
     * @return a page of all items
     */
    PageDTO<ItemResponseDTO> findAllItems(Pageable pageable);
    /**
     * Finds an item by its ID.
     *
     * @param id the ID of the item to find
     * @return an optional containing the item if found, otherwise an empty optional
     */
    Optional<ItemResponseDTO> findItemById(Long id);
    /**
     * Updates an existing item with new information.
     *
     * @param id the ID of the item to be updated
     * @param itemPutDTO the data transfer object containing updated item information
     * @return an optional containing the updated item if the item exists, otherwise an empty optional
     */
    Optional<ItemResponseDTO> updateItem(Long id, ItemPutDTO itemPutDTO);
    /**
     * Partially updates an existing item with new information. Only the fields that are
     * present in the provided ItemUpdateDTO are updated. If the item does not exist,
     * an empty optional is returned.
     *
     * @param id           the ID of the item to be updated
     * @param itemPatchDTO the data transfer object containing updated item information
     * @return an optional containing the updated item if the item exists, otherwise an empty optional
     */
    Optional<ItemResponseDTO> partialUpdateItem(Long id, ItemPatchDTO itemPatchDTO);
    /**
     * Deletes an item by its ID.
     *
     * @param id the ID of the item to be deleted
     * @return true if the item was successfully deleted, false if the item was not found
     */
    boolean deleteItem(Long id);
}
