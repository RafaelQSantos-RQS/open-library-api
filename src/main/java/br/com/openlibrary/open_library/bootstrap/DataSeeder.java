package br.com.openlibrary.open_library.bootstrap;

import br.com.openlibrary.open_library.model.*;
import br.com.openlibrary.open_library.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@Profile("dev") // 1. Este seeder só vai rodar quando o perfil "dev" estiver ativo
public class DataSeeder implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final SubjectAreaRepository subjectAreaRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final FinePolicyRepository finePolicyRepository;

    public DataSeeder(AuthorRepository authorRepository,
                      SubjectAreaRepository subjectAreaRepository,
                      ItemRepository itemRepository,
                      UserRepository userRepository,
                      FinePolicyRepository finePolicyRepository) {
        this.authorRepository = authorRepository;
        this.subjectAreaRepository = subjectAreaRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.finePolicyRepository = finePolicyRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (authorRepository.count() > 0) {
            return;
        }

        SubjectArea sa1 = subjectAreaRepository.save(new SubjectArea("Software Engineering"));
        SubjectArea sa2 = subjectAreaRepository.save(new SubjectArea("Agile Development"));
        SubjectArea sa3 = subjectAreaRepository.save(new SubjectArea("Database Management"));

        Author a1 = authorRepository.save(new Author("Robert C. Martin"));
        Author a2 = authorRepository.save(new Author("Martin Fowler"));
        Author a3 = authorRepository.save(new Author("Erich Gamma"));
        Author a4 = authorRepository.save(new Author("Richard Helm"));
        Author a5 = authorRepository.save(new Author("Ralph Johnson"));

        userRepository.save(new User("Ana Clara - Aluna", "AC202501", "Ciência da Computação", null, "71911110001", UserType.STUDENT));
        userRepository.save(new User("Dr. Carlos Silva - Professor", "CSPROF01", null, "Departamento de Computação", "71922220001", UserType.TEACHER));

        Item i1 = new Item("Clean Code", Set.of(a1), ItemType.BOOK, sa1, 3, 3);
        Item i2 = new Item("Refactoring", Set.of(a2), ItemType.BOOK, sa2, 2, 2);
        Item i3 = new Item("Design Patterns", Set.of(a3, a4, a5), ItemType.BOOK, sa1, 5, 5);
        itemRepository.saveAll(List.of(i1, i2, i3));

        FinePolicy fp1 = new FinePolicy(BigDecimal.valueOf(1), LocalDate.now());
        finePolicyRepository.save(fp1);

        System.out.println(">>> Sample Data Loaded Successfully <<<");
    }
}
