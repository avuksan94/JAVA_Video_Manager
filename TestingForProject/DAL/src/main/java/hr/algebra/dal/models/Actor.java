/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

/**
 *
 * @author antev
 */
public class Actor {
    private int actorId;
    private int personId;

    public Actor() {
    }

    public Actor(int personId) {
        this.personId = personId;
    }

    public Actor(int actorId, int personId) {
        this.actorId = actorId;
        this.personId = personId;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
