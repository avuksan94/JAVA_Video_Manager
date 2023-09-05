/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

//Validators (need to add the dependencies to pom)
//https://dzone.com/articles/validation-in-java-applications
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

/**
 *
 * @author antev
 */
public class Person {
    
    private int personId;
    
    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 2, max = 255, message = "First name must be between 2 and 255 characters!")
    private String firstName;
    
    @NotEmpty(message = "Last name cannot be empty!")
    @Size(min = 2, max = 255, message = "Last name must be between 2 and 255 characters!")
    private String lastName;
    
    //@URL(message = "Must be a valid URL!")
    private String imageUrl;
   
    @Min(value = 18, message = "Must be at least 18 years old")
    @Max(value = 120, message = "Must be less than or equal to 120 years old, Hello there Gandalf :) ")
    private int age;

    public Person() {
    }
    
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Person(String firstName, String lastName, String imageUrl, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.age = age;
    }

    public Person(int personId, String firstName, String lastName, String imageUrl, int age) {
        this(firstName,lastName,imageUrl,age);
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
