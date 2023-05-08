package population;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    @Test
    public void testGetters() {
        Person person = new Person(23, "Kevin", "Feng", "Student");

        assertThat(person.getAge()).isEqualTo(23);
        assertThat(person.getFirstName()).isEqualTo("Kevin");
        assertThat(person.getLastName()).isEqualTo("Feng");
        assertThat(person.getOccupation()).isEqualTo("Student");
    }

    @Test
    public void testSetters() {
        Person person = new Person(1, "1", "1", "1");
        person.setAge(23);
        person.setFirstName("Kevin");
        person.setLastName("Feng");
        person.setOccupation("Student");

        assertThat(person.getAge()).isEqualTo(23);
        assertThat(person.getFirstName()).isEqualTo("Kevin");
        assertThat(person.getLastName()).isEqualTo("Feng");
        assertThat(person.getOccupation()).isEqualTo("Student");
    }

    @Test
    public void testEquals() {
        Person p1 = new Person(23, "Kevin", "Feng", "Student");
        Person p2 = new Person(23, "Kevin", "Feng", "Student");
        Person p3 = new Person(23, "Ryan", "Smith", "Student");

        assertTrue(p1.equals(p1));
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
    }

    @Test
    public void testHashCode() {
        Person p1 = new Person(23, "Kevin", "Feng", "Student");
        Person p2 = new Person(23, "Kevin", "Feng", "Student");

        assertEquals(p1.hashCode(), p2.hashCode());
        p2.setAge(24);
        assertTrue(p1.hashCode() != p2.hashCode());
    }

    @Test
    public void testToString() {
        Person p = new Person(23, "Kevin", "Feng", "Student");
        String expected = "Person" + "{" +
                "id=null, " +
                "age=23, " +
                "firstName='Kevin', " +
                "lastName='Feng', " +
                "occupation='Student'" +
                "}";

        assertEquals(p.toString(), expected);
    }
}
