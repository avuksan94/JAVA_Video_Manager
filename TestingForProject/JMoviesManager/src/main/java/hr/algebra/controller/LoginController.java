/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.controller;

import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.bll.services.UserService;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import hr.algebra.view.LoginView;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author antev
 */
public class LoginController {
    private final User _userModel;
    private final LoginView _loginView;
    private final UserService _userService;
    private final DuplicateManagementService _dupeMgmt;
    
    public LoginController(User userModel,LoginView loginView) throws SQLException{
        _userModel = userModel;
        _loginView = loginView;
        _userService = new UserService();
        _dupeMgmt = new DuplicateManagementService();
    }
    
    public boolean logInUser() throws Exception
    {
        return _userService.validateUser(_userModel.getUsername(), _userModel.getPasswordHash());
    }
    
    public String getUserRole() throws Exception
    {
        return _userService.getRoleForUser(_userModel.getUsername());
    }
    
    public boolean checkIfAdminAccountExists(String username, String email) throws Exception
    {
       return _dupeMgmt.checkIfUserExists(username, email);
    }
    
    public void createAdmin() throws Exception
    {
        Person p =  new Person(
                "Admin",
                "Admin",
                30
        );
        
        User u = new User(
                "admin",
                "123456789",
                "admin@test.com",
                "Admin",
                LocalDateTime.now()
        );
        _userService.createUser(u, p);
    }
    
    
    public User getUser() {
        return _userModel;
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
}
