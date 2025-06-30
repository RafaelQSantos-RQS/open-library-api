package br.com.openlibrary.open_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OpenLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenLibraryApplication.class, args);
	}

}
