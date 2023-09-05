/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.blModels;
//https://dzone.com/articles/validation-in-java-applications
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author antev
 */
public class UserModel {
    
    public UserModel() {
    }
    
      public UserModel(int userId, int personId, String username, String passwordHash, String email, String role, LocalDateTime createdAt, String firstName, String lastName, String imageUrl, int age) {
        this.userId = userId;
        this.personId = personId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.age = age;
    }
      
    public UserModel(int personId, String username, String passwordHash, String email, String role, LocalDateTime createdAt, String firstName, String lastName, String imageUrl, int age) {
        this.personId = personId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.age = age;
    }
    
    public UserModel(String username, String passwordHash, String email, String role, LocalDateTime createdAt, String firstName, String lastName, String imageUrl, int age) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.age = age;
    }

    private int userId;
    private int personId;
    @NotEmpty(message = "Username cannot be empty!")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters!")
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 8, message = "Password must be at least 8 characters!")
    private String passwordHash;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Email should be valid!")
    private String email;

    @NotEmpty(message = "User must have a defined role!")
    @Size(min = 4, message = "Role must be at least 4 characters!")
    private String role;

    private LocalDateTime createdAt;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
}
