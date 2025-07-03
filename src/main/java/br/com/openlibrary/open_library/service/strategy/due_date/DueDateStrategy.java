package br.com.openlibrary.open_library.service.strategy.due_date;

import br.com.openlibrary.open_library.model.Item;

import java.time.LocalDate;

public interface DueDateStrategy {
    LocalDate calculateDueDate(LocalDate loanDate, Item item);
}
