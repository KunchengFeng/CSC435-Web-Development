package org.example.objects;

public class Person {
    private int id;
    private int age;
    private String name;
    private String occupation;

    public Person(int age, String name, String occupation) {
        this.age = age;
        this.name = name;
        this.occupation = occupation;
    }

    public Person(int id, int age, String name, String occupation) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.occupation = occupation;
    }

    // Getters
    public int getId() {return id;}
    public int getAge() {return age;}
    public String getName() {return name;}
    public String getOccupation() {return occupation;}

    // Setters
    public void setId(int id) {this.id = id;}
    public void setAge(int age) {this.age = age;}
    public void setName(String name) {this.name = name;}
    public void setOccupation(String occupation) {this.occupation = occupation;}
}
