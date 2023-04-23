package app.objects;

public class Person {
    private int id;
    private String name;
    private String occupation;
    private int age;

    public Person(String name, String occupation, int age) {
        this.name = name;
        this.occupation = occupation;
        this.age = age;
    }

    public Person(Integer id, String name, String occupation, int age) {
        this.id = id;
        this.name = name;
        this.occupation = occupation;
        this.age = age;
    }

    // getters
    public int getId() {return id;}
    public String getName() {return name;}
    public String getOccupation() {return occupation;}
    public int getAge() {return age;}

    // setters
    public void setName(String name) {this.name = name;}
    public void setOccupation(String occupation) {this.occupation = occupation;}
    public void setAge(int age) {this.age = age;}
}
