package population;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository repository;

    @Test
    public void testSavePerson() {
        Person person = new Person(23, "Kevin", "Feng", "Student");
        repository.save(person);
        assertNotNull(person.getId());
    }

    @Test
    public void testFindAll() {
        // The LoadDatabase should load 6 people when the program starts
        List<Person> people = repository.findAll();
        assertEquals(6, people.size());
    }

    @Test
    public void testFindById() {
        // The LoadDatabase should load this person first
        Optional<Person> personOptional = repository.findById(1L);
        assertTrue(personOptional.isPresent());
        Person person = personOptional.get();
        assertEquals("Fredrick von Oldenburg", person.getName());
    }

    @Test
    public void testDeletePerson() {
        repository.deleteById(1L);
        assertFalse(repository.findById(1L).isPresent());
    }
}
