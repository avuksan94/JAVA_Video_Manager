/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.models;

/**
 *
 * @author antev
 */
public class Director {
    private int directorId;
    private int personId;

    public Director() {
    }

    public Director(int personId) {
        this.personId = personId;
    }

    public Director(int directorId, int personId) {
        this.directorId = directorId;
        this.personId = personId;
    }
    
    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
