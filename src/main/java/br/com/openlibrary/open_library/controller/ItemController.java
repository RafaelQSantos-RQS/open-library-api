package br.com.openlibrary.open_library.controller;

import br.com.openlibrary.open_library.dto.item.ItemCreateDTO;
import br.com.openlibrary.open_library.dto.item.ItemPatchDTO;
import br.com.openlibrary.open_library.dto.item.ItemResponseDTO;
import br.com.openlibrary.open_library.dto.item.ItemPutDTO;
import br.com.openlibrary.open_library.dto.page.PageDTO;
import br.com.openlibrary.open_library.service.item.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ItemResponseDTO> createItem(@Valid @RequestBody ItemCreateDTO itemCreateDTO) {
        ItemResponseDTO createdItem = itemService.createItem(itemCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdItem.id())
                .toUri();

        return ResponseEntity.created(location).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<PageDTO<ItemResponseDTO>> getAllItems(Pageable pageable) {
        return ResponseEntity.ok(itemService.findAllItems(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        return itemService.findItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ItemPutDTO itemPutDTO) {
        return itemService.updateItem(id, itemPutDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> partialUpdateItem(@PathVariable Long id, @Valid @RequestBody ItemPatchDTO itemPatchDTO) {
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
