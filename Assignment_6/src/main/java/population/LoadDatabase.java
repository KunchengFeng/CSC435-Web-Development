package population;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PersonRepository repository) {
        return args -> {
            repository.save(new Person(67, "Fredrick", "von Oldenburg", "King"));
            repository.save(new Person(72, "Karl", "Johan Bernadotte", "King"));
            repository.save(new Person(70, "William", "of Hanover", "King"));
            repository.save(new Person(39, "Nikolas", "Romanov", "Tsar"));
            repository.save(new Person(50, "Mahmut", "Osmanoglu", "Sultan"));
            repository.save(new Person(42, "Ferdinand", "von Habsburg", "Kaiser"));

            repository.findAll().forEach(person -> log.info("Preloaded " + person.toString()));
        };
    }
}
