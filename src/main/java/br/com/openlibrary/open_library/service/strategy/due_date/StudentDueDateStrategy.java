package br.com.openlibrary.open_library.service.strategy.due_date;

import br.com.openlibrary.open_library.model.Item;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("studentStrategy")
public class StudentDueDateStrategy implements DueDateStrategy{

    private static final int DUE_DAYS = 7;

    @Override
    public LocalDate calculateDueDate(LocalDate loanDate, Item item) {
        return switch (item.getItemType()) {
            case BOOK -> loanDate.plusDays(7);
            default -> loanDate.plusDays(3);
        };
    }
}
