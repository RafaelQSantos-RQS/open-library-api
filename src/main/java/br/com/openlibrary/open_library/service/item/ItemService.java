package br.com.openlibrary.open_library.service.item;

import br.com.openlibrary.open_library.dto.item.ItemCreateDto;
import br.com.openlibrary.open_library.dto.item.ItemPatchDto;
import br.com.openlibrary.open_library.dto.item.ItemPutDto;
import br.com.openlibrary.open_library.dto.item.ItemResponseDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemService {
    /**
     * Creates a new item.
     *
     * @param itemCreateDTO the data transfer object containing information about the item to be created
     * @return the created item
     */
    ItemResponseDto createItem(ItemCreateDto itemCreateDTO);
    /**
     * Finds all items in the database.
     *
     * @param pageable the pagination object
     * @return a page of all items
     */
    PageDto<ItemResponseDto> findAllItems(Pageable pageable);
    /**
     * Finds an item by its ID.
     *
     * @param id the ID of the item to find
     * @return an optional containing the item if found, otherwise an empty optional
     */
    Optional<ItemResponseDto> findItemById(Long id);
    /**
     * Updates an existing item with new information.
     *
     * @param id the ID of the item to be updated
     * @param itemPutDTO the data transfer object containing updated item information
     * @return an optional containing the updated item if the item exists, otherwise an empty optional
     */
    Optional<ItemResponseDto> updateItem(Long id, ItemPutDto itemPutDTO);
    /**
     * Partially updates an existing item with new information. Only the fields that are
     * present in the provided ItemUpdateDTO are updated. If the item does not exist,
     * an empty optional is returned.
     *
     * @param id           the ID of the item to be updated
     * @param itemPatchDTO the data transfer object containing updated item information
     * @return an optional containing the updated item if the item exists, otherwise an empty optional
     */
    Optional<ItemResponseDto> partialUpdateItem(Long id, ItemPatchDto itemPatchDTO);
    /**
     * Deletes an item by its ID.
     *
     * @param id the ID of the item to be deleted
     * @return true if the item was successfully deleted, false if the item was not found
     */
    boolean deleteItem(Long id);
    /**
     * Finds items that match the given search criteria.
     * <p>
     * The search criteria can be combined, and only items that match all of the
     * specified criteria will be returned. If no criteria are specified, all items
     * will be returned.
     * <p>
     * The search criteria are case-insensitive and support partial matches.
     * <p>
     * The returned items are sorted by their title.
     *
     * @param title         the title to search for
     * @param authorName    the author name to search for
     * @param subjectAreaId the subject area ID to search for
     * @param pageable      the pagination object
     * @return a page of items that match the search criteria
     */
    PageDto<ItemResponseDto> searchItems(String title, String authorName, Long subjectAreaId, Pageable pageable);
}
