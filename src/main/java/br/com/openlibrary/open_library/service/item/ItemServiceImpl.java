package br.com.openlibrary.open_library.service.item;

import br.com.openlibrary.open_library.dto.item.ItemCreateDTO;
import br.com.openlibrary.open_library.dto.item.ItemPatchDTO;
import br.com.openlibrary.open_library.dto.item.ItemPutDTO;
import br.com.openlibrary.open_library.dto.item.ItemResponseDTO;
import br.com.openlibrary.open_library.dto.page.PageDTO;
import br.com.openlibrary.open_library.mapper.ItemMapper;
import br.com.openlibrary.open_library.model.Item;
import br.com.openlibrary.open_library.model.SubjectArea;
import br.com.openlibrary.open_library.repository.ItemRepository;
import br.com.openlibrary.open_library.repository.SubjectAreaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final SubjectAreaRepository subjectAreaRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, SubjectAreaRepository subjectAreaRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.subjectAreaRepository = subjectAreaRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public ItemResponseDTO createItem(ItemCreateDTO itemCreateDTO) {
        SubjectArea subjectArea = subjectAreaRepository.findById(itemCreateDTO.subjectAreaId())
                .orElseThrow(() -> new IllegalArgumentException("Subject area with id " + itemCreateDTO.subjectAreaId() + " not found."));

        Item item = new Item();
        item.setTitle(itemCreateDTO.title());
        item.setAuthor(itemCreateDTO.author());
        item.setItemType(itemCreateDTO.itemType());
        item.setSubjectArea(subjectArea);
        item.setTotalQuantity(0);
        item.setAvailableQuantity(0);

        Item savedItem = itemRepository.save(item);

        return itemMapper.toResponseDto(savedItem);
    }

    @Override
    public PageDTO<ItemResponseDTO> findAllItems(Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findAll(pageable);

        List<ItemResponseDTO> items = itemsPage.getContent().stream()
                .map(itemMapper::toResponseDto)
                .toList();

        return new PageDTO<>(
                items,
                itemsPage.getNumber(),
                itemsPage.getSize(),
                itemsPage.getTotalElements(),
                itemsPage.getTotalPages()
        );
    }

    @Override
    public Optional<ItemResponseDTO> findItemById(Long id) {
        return itemRepository.findById(id)
                .map(itemMapper::toResponseDto);
    }

    @Override
    @Transactional
    public Optional<ItemResponseDTO> updateItem(Long id, ItemPutDTO itemPutDTO) {
        return itemRepository.findById(id)
                .map(item -> {
                    SubjectArea subjectArea = subjectAreaRepository.findById(itemPutDTO.subjectAreaId())
                            .orElseThrow(() -> new EntityNotFoundException("Subject area with id " + itemPutDTO.subjectAreaId() + " not found."));

                    item.setTitle(itemPutDTO.title());
                    item.setAuthor(itemPutDTO.author());
                    item.setItemType(itemPutDTO.itemType());
                    item.setSubjectArea(subjectArea);

                    Item updatedItem = itemRepository.save(item);

                    return itemMapper.toResponseDto(updatedItem);
                });
    }

    @Override
    @Transactional
    public Optional<ItemResponseDTO> partialUpdateItem(Long id, ItemPatchDTO itemPatchDTO) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    itemMapper.partialUpdate(existingItem, itemPatchDTO);

                    if (itemPatchDTO.subjectAreaId() != null) {
                        SubjectArea subjectArea = subjectAreaRepository.findById(itemPatchDTO.subjectAreaId())
                                .orElseThrow(() -> new EntityNotFoundException("Subject area with id " + itemPatchDTO.subjectAreaId() + " not found."));

                        existingItem.setSubjectArea(subjectArea);
                    }

                    Item updatedItem = itemRepository.save(existingItem);

                    return itemMapper.toResponseDto(updatedItem);
                });
    }

    @Override
    @Transactional
    public boolean deleteItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
