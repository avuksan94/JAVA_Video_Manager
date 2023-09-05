/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.jbModels;

import javax.xml.bind.annotation.*;

/**
 *
 * @author antev
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectorJB {

    @XmlElement(name = "firstName")
    private String firstName;
    
    @XmlElement(name = "lastName")
    private String lastName;

    public DirectorJB(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public DirectorJB() {
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
    
    

}
