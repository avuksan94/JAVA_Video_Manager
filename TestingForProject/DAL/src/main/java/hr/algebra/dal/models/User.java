/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Validators (need to add the dependencies to pom)
//https://dzone.com/articles/validation-in-java-applications
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;


/**
 *
 * @author antev
 */
public class User {
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
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
    
    public User() {
    }

    public User(int userId, String username, String passwordHash, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    public User(String username, String passwordHash, String email, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    public User(String username, String passwordHash, String email, String role, LocalDateTime createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(int personId, String username, String passwordHash, String email, String role, LocalDateTime createdAt) {
        this(username,passwordHash,email,role,createdAt);
        this.personId = personId;
    }

    public User(int userId, int personId, String username, String passwordHash, String email, String role, LocalDateTime createdAt) {
        this(username,passwordHash,email,role,createdAt);
        this.userId = userId;
        this.personId = personId;
    }
    
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
    
    //ne znam da li mi se isplati implementirati DeletedAt za soft delete
}
