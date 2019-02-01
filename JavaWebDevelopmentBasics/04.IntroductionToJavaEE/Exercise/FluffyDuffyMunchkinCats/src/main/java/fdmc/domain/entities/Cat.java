package fdmc.domain.entities;

public class Cat {

    private String name;
    private String breed;
    private String color;
    private int age;

    public Cat() {}

    public Cat(String name, String breed, String color, int age) {
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public String getBreed() {
        return this.breed;
    }

    public String getColor() {
        return this.color;
    }

    public int getAge() {
        return this.age;
    }
}
