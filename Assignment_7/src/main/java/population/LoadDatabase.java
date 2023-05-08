package population;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger LOGGER = LogManager.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PersonRepository repository) {
        return args -> {
            repository.save(new Person(67, "Fredrick", "von Oldenburg", "King"));
            repository.save(new Person(72, "Karl", "Johan Bernadotte", "King"));
            repository.save(new Person(70, "William", "of Hanover", "King"));
            repository.save(new Person(39, "Nikolas", "Romanov", "Tsar"));
            repository.save(new Person(50, "Mahmut", "Osmanoglu", "Sultan"));
            repository.save(new Person(42, "Ferdinand", "von Habsburg", "Kaiser"));

            repository.findAll().forEach(person -> LOGGER.info("Preloaded " + person.toString()));
        };
    }
}
