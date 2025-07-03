package br.com.openlibrary.open_library.repository;

import br.com.openlibrary.open_library.model.Author;
import br.com.openlibrary.open_library.model.Item;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {
    public static Specification<Item> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Item> authorNameContains(String authorName) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Item, Author> authorJoin = root.join("authors");
            return criteriaBuilder.like(criteriaBuilder.lower(authorJoin.get("name")), "%" + authorName.toLowerCase() + "%");
        };
    }

    public static Specification<Item> hasSubjectArea(Long subjectAreaId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("subjectArea").get("id"), subjectAreaId);
    }

}
