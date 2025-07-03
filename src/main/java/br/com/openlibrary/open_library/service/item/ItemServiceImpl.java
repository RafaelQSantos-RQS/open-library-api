package br.com.openlibrary.open_library.service.item;

import br.com.openlibrary.open_library.dto.item.ItemCreateDto;
import br.com.openlibrary.open_library.dto.item.ItemPatchDto;
import br.com.openlibrary.open_library.dto.item.ItemPutDto;
import br.com.openlibrary.open_library.dto.item.ItemResponseDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.mapper.ItemMapper;
import br.com.openlibrary.open_library.model.Author;
import br.com.openlibrary.open_library.model.Item;
import br.com.openlibrary.open_library.model.SubjectArea;
import br.com.openlibrary.open_library.repository.AuthorRepository;
import br.com.openlibrary.open_library.repository.ItemRepository;
import br.com.openlibrary.open_library.repository.ItemSpecification;
import br.com.openlibrary.open_library.repository.SubjectAreaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final SubjectAreaRepository subjectAreaRepository;
    private final AuthorRepository authorRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, SubjectAreaRepository subjectAreaRepository, ItemMapper itemMapper, AuthorRepository authorRepository) {
        this.itemRepository = itemRepository;
        this.subjectAreaRepository = subjectAreaRepository;
        this.authorRepository = authorRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional
    public ItemResponseDto createItem(ItemCreateDto itemCreateDTO) {
        SubjectArea subjectArea = subjectAreaRepository.findById(itemCreateDTO.subjectAreaId())
                .orElseThrow(() -> new IllegalArgumentException("Subject area with id " + itemCreateDTO.subjectAreaId() + " not found."));

        List<Author> authors = authorRepository.findAllById(itemCreateDTO.authorIds());

        if (authors.size() != itemCreateDTO.authorIds().size())
            throw new IllegalArgumentException("One or more authors not found.");

        Item item = new Item();
        item.setTitle(itemCreateDTO.title());
        item.setAuthors(new HashSet<>(authors));
        item.setItemType(itemCreateDTO.itemType());
        item.setSubjectArea(subjectArea);
        item.setTotalQuantity(0);
        item.setAvailableQuantity(0);

        Item savedItem = itemRepository.save(item);

        return itemMapper.toResponseDto(savedItem);
    }

    @Override
    public PageDto<ItemResponseDto> findAllItems(Pageable pageable) {
        Page<Item> itemsPage = itemRepository.findAll(pageable);

        List<ItemResponseDto> items = itemsPage.getContent().stream()
                .map(itemMapper::toResponseDto)
                .toList();

        return new PageDto<>(
                items,
                itemsPage.getNumber(),
                itemsPage.getSize(),
                itemsPage.getTotalElements(),
                itemsPage.getTotalPages()
        );
    }

    @Override
    public Optional<ItemResponseDto> findItemById(Long id) {
        return itemRepository.findById(id)
                .map(itemMapper::toResponseDto);
    }

    @Override
    @Transactional
    public Optional<ItemResponseDto> updateItem(Long id, ItemPutDto itemPutDto) {
        return itemRepository.findById(id)
                .map(item -> {
                    // Check if the subject area exists
                    SubjectArea subjectArea = subjectAreaRepository.findById(itemPutDto.subjectAreaId())
                            .orElseThrow(() -> new EntityNotFoundException("Subject area with id " + itemPutDto.subjectAreaId() + " not found."));

                    // Check if the authors exist
                    List<Author> authors = authorRepository.findAllById(itemPutDto.authorIds());
                    if (authors.size() != itemPutDto.authorIds().size()) throw new EntityNotFoundException("One or more authors not found for update.");

                    item.setTitle(itemPutDto.title());
                    item.setAuthors(new HashSet<>(authors));
                    item.setItemType(itemPutDto.itemType());
                    item.setSubjectArea(subjectArea);

                    Item updatedItem = itemRepository.save(item);

                    return itemMapper.toResponseDto(updatedItem);
                });
    }

    @Override
    @Transactional
    public Optional<ItemResponseDto> partialUpdateItem(Long id, ItemPatchDto itemPatchDto) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    // Perform partial update
                    itemMapper.partialUpdate(existingItem, itemPatchDto);

                    // Check if the subject area exists
                    if (itemPatchDto.subjectAreaId() != null) {
                        SubjectArea subjectArea = subjectAreaRepository.findById(itemPatchDto.subjectAreaId())
                                .orElseThrow(() -> new EntityNotFoundException("SubjectArea not found with id: " + itemPatchDto.subjectAreaId()));
                        existingItem.setSubjectArea(subjectArea);
                    }

                    // Check if the authors exist
                    if (itemPatchDto.authorIds() != null) {
                        List<Author> authors = authorRepository.findAllById(itemPatchDto.authorIds());
                        if (authors.size() != itemPatchDto.authorIds().size()) {
                            throw new EntityNotFoundException("One or more authors not found for patch.");
                        }
                        existingItem.setAuthors(new HashSet<>(authors));
                    }

                    // Save the updated item
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

    @Override
    public PageDto<ItemResponseDto> searchItems(String title, String authorName, Long subjectAreaId, Pageable pageable) {
        Specification<Item> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (title != null && !title.isEmpty()) specification = specification.and(ItemSpecification.titleContains(title));
        if (authorName != null && !authorName.isEmpty()) specification = specification.and(ItemSpecification.authorNameContains(authorName));
        if (subjectAreaId != null) specification = specification.and(ItemSpecification.hasSubjectArea(subjectAreaId));

        Page<Item> itemsPage = itemRepository.findAll(specification, pageable);

        List<ItemResponseDto> items = itemsPage.getContent().stream()
                .map(itemMapper::toResponseDto)
                .toList();

        return new PageDto<>(
                items,
                itemsPage.getNumber(),
                itemsPage.getSize(),
                itemsPage.getTotalElements(),
                itemsPage.getTotalPages()
        );
    }
}
