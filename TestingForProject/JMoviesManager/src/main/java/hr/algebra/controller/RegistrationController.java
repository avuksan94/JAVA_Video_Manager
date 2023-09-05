/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.controller;

import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.bll.services.UserService;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import hr.algebra.view.RegistrationView;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author antev
 */
public class RegistrationController {
    private final Person _personModel;
    private final User _userModel;
    private final RegistrationView _registrationView;
    private final UserService _userService;
    private final DuplicateManagementService _dupeMgmt;
    private final String userDefaultImage = "https://img.freepik.com/premium-vector/user-icon-vector-with-blue-background-suitable-web-design-etc_266866-110.jpg?w=996";
    
    public RegistrationController(Person personModel, User userModel, RegistrationView registrationView) throws SQLException{
        _personModel = personModel;
        _userModel = userModel;
        _registrationView = registrationView;
        _userService = new UserService();
        _dupeMgmt = new DuplicateManagementService();
    }
    
    public void registerUser() throws Exception {
        _userService.createUser(_userModel, _personModel);
    }
    
    public boolean checkIfUsernameTaken(String username) throws Exception
    {
        return _dupeMgmt.checkIfUsernameExists(username);
    }
    
    public boolean checkIfEmailTaken(String email) throws Exception
    {
        return _dupeMgmt.checkIfEmailExists(email);
    }
    
    public User getUser() {
        return _userModel;
    }

    public Person getPerson() {
        return _personModel;
    }
    
    public void setFirstName(String firstName){
        _personModel.setFirstName(firstName);      
    }
 
    public String getFirstName(){
        return _personModel.getFirstName();       
    }
    
    public void setLastName(String lastName){
        _personModel.setLastName(lastName);      
    }
 
    public String getLastName(){
        return _personModel.getLastName();       
    }
    
    public void setAge(int age){
        _personModel.setAge(age);      
    }
 
    public int getAge(){
        return _personModel.getAge();       
    }
    
    public void setImageURL(){
        _personModel.setImageUrl(userDefaultImage);      
    }
 
    public String getImageURL(){
        return _personModel.getImageUrl();       
    }
    
    public void setUsername(String username){
        _userModel.setUsername(username);      
    }
 
    public String getUsername(){
        return _userModel.getUsername();       
    }
    
    public void setPassword(String password){
        _userModel.setPasswordHash(password);      
    }
 
    public String getPassword(){
        return _userModel.getPasswordHash();       
    }
    
    public void setEmail(String email){
        _userModel.setEmail(email);      
    }
 
    public String getEmail(){
        return _userModel.getEmail();       
    }
    
    public void setRole(){
        _userModel.setRole("Unconfirmed User");      
    }
 
    public String getRole(){
        return _userModel.getRole();       
    }
    
     public void setCreatedAt(){
        _userModel.setCreatedAt(LocalDateTime.now());      
    }
 
    public LocalDateTime getCreatedAt(){
        return _userModel.getCreatedAt();       
    }
}
