package population;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PersonModelAssembler implements RepresentationModelAssembler<Person, EntityModel<Person>> {
    @Override
    public EntityModel<Person> toModel(Person person) {
        Link selfLink = linkTo(methodOn(PersonController.class).getOne(person.getId())).withSelfRel();
        Link rootLink = linkTo(methodOn(PersonController.class).getAll()).withRel("population");
        return EntityModel.of(person, selfLink, rootLink);
    }

}