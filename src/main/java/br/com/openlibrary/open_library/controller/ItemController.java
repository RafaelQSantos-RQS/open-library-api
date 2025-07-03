package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.item.ItemCreateDto;
import br.com.openlibrary.open_library.dto.item.ItemPatchDto;
import br.com.openlibrary.open_library.dto.item.ItemResponseDto;
import br.com.openlibrary.open_library.dto.item.ItemPutDto;
import br.com.openlibrary.open_library.dto.page.PageDto;
import br.com.openlibrary.open_library.service.item.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemCreateDto itemCreateDTO) {
        ItemResponseDto createdItem = itemService.createItem(itemCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdItem.id())
                .toUri();

        return ResponseEntity.created(location).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<PageDto<ItemResponseDto>> searchItems(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) Long subjectAreaId,
            Pageable pageable) {

        return ResponseEntity.ok(itemService.searchItems(title, authorName, subjectAreaId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
        return itemService.findItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable Long id, @Valid @RequestBody ItemPutDto itemPutDTO) {
        return itemService.updateItem(id, itemPutDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDto> partialUpdateItem(@PathVariable Long id, @Valid @RequestBody ItemPatchDto itemPatchDTO) {
        return itemService.partialUpdateItem(id, itemPatchDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        boolean deleted = itemService.deleteItem(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}
