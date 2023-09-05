/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.blModels;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author antev
 */
public class DirectorModel implements Comparable<DirectorModel> {

    private int directorId;

    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 2, max = 255, message = "First name must be between 2 and 255 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty!")
    @Size(min = 2, max = 255, message = "Last name must be between 2 and 255 characters!")
    private String lastName;

    public DirectorModel() {
    }

    public DirectorModel(int directorId) {
        this.directorId = directorId;
    }
    
    public DirectorModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public DirectorModel(int directorId, String firstName, String lastName) {
        this.directorId = directorId;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int actorId) {
        this.directorId = actorId;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(DirectorModel o) {
        return lastName.compareTo(o.lastName);
    }
}
