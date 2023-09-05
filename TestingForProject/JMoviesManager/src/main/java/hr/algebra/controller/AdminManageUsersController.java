/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.controller;

import hr.algebra.bll.services.UserService;
import hr.algebra.bll.blModels.UserModel;
import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.view.ManageUsersView;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author antev
 */
public class AdminManageUsersController {

    private final UserModel _userModel;
    private final ManageUsersView _manageUsersView;
    private final DuplicateManagementService _dupeMgmt;
    private final UserService _userService;
    private final String USER_DEFAULT_IMAGE = "https://img.freepik.com/premium-vector/user-icon-vector-with-blue-background-suitable-web-design-etc_266866-110.jpg?w=996";

    public AdminManageUsersController(UserModel userModel, ManageUsersView manageUsersView) throws SQLException {
        _userModel = userModel;
        _manageUsersView = manageUsersView;
        _userService = new UserService();
        _dupeMgmt = new DuplicateManagementService();
    }

    public List<UserModel> getUsersForModel() throws Exception {
        return _userService.getUsersFromDB();
    }

    public UserModel getUserForModel(int id) throws Exception {
        return _userService.getUserFromDB(id);
    }

    public void addUser(UserModel um) throws Exception {
        _userService.createUserDB(um);
    }

    public void updateUser(int id, UserModel um) throws Exception {
        _userService.updateUserDB(id, um);
    }

    public void deleteUser(int id) throws Exception {
        _userService.DeleteUserDB(id);
    }

    public UserModel getUser() {
        return _userModel;
    }

    public boolean checkIfUsernameTaken(String username) throws Exception {
        return _dupeMgmt.checkIfUsernameExists(username);
    }

    public boolean checkIfEmailTaken(String email) throws Exception {
        return _dupeMgmt.checkIfEmailExists(email);
    }

    public void setFirstName(String firstName) {
        _userModel.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        _userModel.setLastName(lastName);
    }

    public String getLastName() {
        return _userModel.getLastName();
    }

    public void setAge(int age) {
        _userModel.setAge(age);
    }

    public int getAge() {
        return _userModel.getAge();
    }

    public void setImageURL() {
        _userModel.setImageUrl(USER_DEFAULT_IMAGE);
    }

    public String getImageURL() {
        return _userModel.getImageUrl();
    }

    public void setUsername(String username) {
        _userModel.setUsername(username);
    }

    public String getUsername() {
        return _userModel.getUsername();
    }

    public void setPassword(String password) {
        _userModel.setPasswordHash(password);
    }

    public String getPassword() {
        return _userModel.getPasswordHash();
    }

    public void setEmail(String email) {
        _userModel.setEmail(email);
    }

    public String getEmail() {
        return _userModel.getEmail();
    }

    public void setRole(String role) {
        _userModel.setRole(role);
    }

    public String getRole() {
        return _userModel.getRole();
    }

    public void setCreatedAt() {
        _userModel.setCreatedAt(LocalDateTime.now());
    }

    public LocalDateTime getCreatedAt() {
        return _userModel.getCreatedAt();
    }
}
