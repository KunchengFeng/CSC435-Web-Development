package population;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class PersonController {
    private final PersonRepository repository;
    private final PersonModelAssembler assembler;
    private static final Logger LOGGER = LogManager.getLogger(PersonController.class);

    public PersonController(PersonRepository repository, PersonModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    // Create ---------------------------------------------------------- //
    @PostMapping("/population")
    ResponseEntity<?> newPerson(@RequestBody Person person) {
        Person newPerson = repository.save(person);
        EntityModel<Person> personModel = assembler.toModel(newPerson);
        URI selfLink = personModel.getRequiredLink(IanaLinkRelations.SELF).toUri();

        String endpoint = "Endpoint: /population, ";
        String method = "Method: " + HttpMethod.POST + ", ";
        String status = "Status: " + HttpStatus.CREATED;
        LOGGER.info(endpoint + method + status);

        return ResponseEntity.created(selfLink).body(personModel);
    }
    // ----------------------------------------------------------------- //


    // Review ---------------------------------------------------------- //
    // Review one //
    @GetMapping("/population/{id}")
    EntityModel<Person> getOne(@PathVariable Long id) {
        String endpoint = "Endpoint: /population/" + id + ", ";
        String method = "Method: " + HttpMethod.GET + ", ";

        Person person = repository
                .findById(id)
                .orElseThrow(() -> {
                    String status = "Status: " + HttpStatus.NOT_FOUND;
                    LOGGER.error(endpoint + method + status);
                    return new PersonNotFoundException(id);
                });

        String status = "Status: " + HttpStatus.OK;
        LOGGER.info(endpoint + method + status);

        return assembler.toModel(person);
    }

    // Review all //
    @GetMapping("/population")
    CollectionModel<EntityModel<Person>> getAll() {
        List<EntityModel<Person>> population = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(PersonController.class).getAll()).withSelfRel();

        String endpoint = "Endpoint: /population, ";
        String method = "Method: " + HttpMethod.GET + ", ";
        String status = "Status: " + HttpStatus.OK;
        LOGGER.info(endpoint + method + status);

        return CollectionModel.of(population, selfLink);
    }
    // ----------------------------------------------------------------- //


    // Update ---------------------------------------------------------- //
    @PutMapping("/population/{id}")
    ResponseEntity<?> replacePerson(@RequestBody Person newPerson, @PathVariable Long id) {
        Person updatedPerson = repository.findById(id).map(person -> {
            person.setAge(newPerson.getAge());
            person.setFirstName(newPerson.getFirstName());
            person.setLastName(newPerson.getLastName());
            person.setOccupation(newPerson.getOccupation());
            return repository.save(person);
        })
                .orElseGet(() -> {
                    newPerson.setId(id);
                    return repository.save(newPerson);
                });

        EntityModel<Person> entityModel = assembler.toModel(updatedPerson);
        URI selfLink = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();

        String endpoint = "Endpoint: /population/" + id + ", ";
        String method = "Method: " + HttpMethod.PUT + ", ";
        String status = "Status: " + HttpStatus.CREATED;
        LOGGER.info(endpoint + method + status);

        return ResponseEntity.created(selfLink).body(entityModel);
    }
    // ----------------------------------------------------------------- //


    // Delete ---------------------------------------------------------- //
    @DeleteMapping("/population/{id}")
    ResponseEntity<?> deletePerson(@PathVariable Long id) {
        repository.deleteById(id);

        String endpoint = "Endpoint: /population/" + id + ", ";
        String method = "Method: " + HttpMethod.DELETE + ", ";
        String status = "Status: " + HttpStatus.NO_CONTENT;
        LOGGER.info(endpoint + method + status);

        return ResponseEntity.noContent().build();
    }
    // ----------------------------------------------------------------- //


    // Login ----------------------------------------------------------- //
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
}
