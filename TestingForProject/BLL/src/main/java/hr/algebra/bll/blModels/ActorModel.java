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
public class ActorModel implements Comparable<ActorModel> {

    private int actorId;

    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 2, max = 255, message = "First name must be between 2 and 255 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty!")
    @Size(min = 2, max = 255, message = "Last name must be between 2 and 255 characters!")
    private String lastName;

    public ActorModel() {

    }

    public ActorModel(int actorId) {
        this.actorId = actorId;
    }
    
    public ActorModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ActorModel(int actorId, String firstName, String lastName) {
        this.actorId = actorId;
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

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(ActorModel o) {
        return lastName.compareTo(o.lastName);
    }
}
