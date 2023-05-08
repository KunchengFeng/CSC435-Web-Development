package population;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Person {
    private @Id @GeneratedValue Long id;
    private int age;
    private String firstName;
    private String lastName;
    private String occupation;


    // Constructors ------------------------------------------------------------- //
    public Person() {}

    public Person(int age, String firstName, String lastName, String occupation) {
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
    }
    // -------------------------------------------------------------------------- //


    // Getters and Setters ------------------------------------------------------ //
    public void setId(Long id) {this.id = id;}

    public Long getId() {return this.id;}

    public void setAge(int age) {this.age = age;}

    public int getAge() {return this.age;}

    // This method is actually used in JSONAssert
    public void setName(String name) {
        String[] names = name.split(" ");
        this.firstName = names[0];
        this.lastName = "";
        for (int i = 1; i < names.length; i++) {
            if (i > 1) {
                this.lastName = this.lastName + " ";
            }
            this.lastName = this.lastName + names[i];
        }
    }

    public String getName() {return this.firstName + " " + this.lastName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getFirstName() {return firstName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getLastName() {return lastName;}

    public void setOccupation(String occupation) {this.occupation = occupation;}

    public String getOccupation() {return occupation;}
    // -------------------------------------------------------------------------- //


    // Object functions --------------------------------------------------------- //
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (! (object instanceof Person)) {
            return false;
        }

        Person other = (Person) object;
        return  Objects.equals(this.id, other.id) &&
                Objects.equals(this.age, other.age) &&
                Objects.equals(this.firstName, other.firstName) &&
                Objects.equals(this.lastName, other.lastName) &&
                Objects.equals(this.occupation, other.occupation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.age, this.firstName, this.lastName, this.occupation);
    }

    @Override
    public String toString() {
        return "Person" + "{" +
                "id=" + this.id + ", " +
                "age=" + this.age + ", " +
                "firstName='" + this.firstName + "', " +
                "lastName='" + this.lastName + "', " +
                "occupation='" + this.occupation + "'" +
                "}";
    }
    // -------------------------------------------------------------------------- //
}
