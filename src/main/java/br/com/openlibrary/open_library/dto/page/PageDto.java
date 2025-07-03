package br.com.openlibrary.open_library.dto.page;

import java.util.List;

public record PageDto<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
