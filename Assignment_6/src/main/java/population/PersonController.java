package population;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonController {
    private final PersonRepository repository;
    private final PersonModelAssembler assembler;

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

        return ResponseEntity.created(selfLink).body(personModel);
    }
    // ----------------------------------------------------------------- //


    // Review ---------------------------------------------------------- //
    // Review one //
    @GetMapping("/population/{id}")
    EntityModel<Person> getOne(@PathVariable Long id) {
        Person person = repository
                .findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        return assembler.toModel(person);
    }

    // Review all //
    @GetMapping("/population")
    CollectionModel<EntityModel<Person>> getAll() {
        List<EntityModel<Person>> population = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        Link selfLink = linkTo(methodOn(PersonController.class).getAll()).withSelfRel();
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
        return ResponseEntity.created(selfLink).body(entityModel);
    }
    // ----------------------------------------------------------------- //


    // Delete ---------------------------------------------------------- //
    @DeleteMapping("/population/{id}")
    ResponseEntity<?> deletePerson(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // ----------------------------------------------------------------- //
}
