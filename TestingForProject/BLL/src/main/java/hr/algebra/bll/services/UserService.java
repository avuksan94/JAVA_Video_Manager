/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.services;

import hr.algebra.bll.blModels.UserModel;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import hr.algebra.uow.UnitOfWork;
import hr.algebra.uow.UoW;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author antev
 */
public class UserService {
    
    private final UoW _unitOfWork;
    
    public UserService() throws SQLException
    {
        this._unitOfWork = new UnitOfWork();
    }
    
    public String getRoleForUser(String username) throws Exception
    {
        String userRole = "";
        try {
            List<User> users = selectUsers();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    userRole = user.getRole();
                    break;  // Exit the loop as soon as a match is found
            }
        }
        } catch (Exception e) {
            System.err.println("An error occurred while getting the role for user: " + e.getMessage());
            e.printStackTrace();  //this helps me with debugging
        }
        return userRole;
    }
    
    public boolean validateUser(String username,String password) throws Exception
    {
        boolean isValid = false;
        try {
            List<User> users = selectUsers();
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPasswordHash().equals(password)) {
                    isValid = true;
                    break;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return isValid;
    }
    
    public Person selectPerson(int id) throws Exception
    {
       try {
           Optional<Person> person = _unitOfWork.getPersonRepository().selectEntity(id);
            if (person.isPresent()) {
                Person personSelect = person.get();
                return personSelect;
            }
            else {
            throw new Exception("Person not found"); // throw an exception when the person isnt present
            }
        } catch (Exception e) {
            throw e;
        } 
    }
    
    public List<Person> selectPeople() throws Exception
    {
        try {
           List<Person> people = _unitOfWork.getPersonRepository().selectEntities();
           return people;
        } catch (Exception e) {
            throw e;
        } 
    }
    
    public User selectUser(int id) throws Exception
    {
       try {
           Optional<User> user = _unitOfWork.getUserRepository().selectEntity(id);
            if (user.isPresent()) {
                User userSelect = user.get();
                return userSelect;
            }
            else {
            throw new Exception("User not found"); // throw an exception when the user isnt present
            }
        } catch (Exception e) {
            throw e;
        } 
    }
    
    public List<User> selectUsers() throws Exception
    {
        try {
           List<User> users = _unitOfWork.getUserRepository().selectEntities();
           return users;
        } catch (Exception e) {
            throw e;
        } 
    }
    
    public void createUser(User u,Person p) throws Exception
    {
       try {
            _unitOfWork.begin();  // start transaction

            int personId = _unitOfWork.getPersonRepository().createEntity(p);
            u.setPersonId(personId);
            _unitOfWork.getUserRepository().createEntity(u);

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
    
    //this is bad practice, methods should only take max 2 parameters(Clean Code Arch)
    public void updateUser(int uId,User u,Person p) throws Exception
    {
       try {
            _unitOfWork.begin();  // start transaction
            Optional<User> userToUpdate = _unitOfWork.getUserRepository().selectEntity(uId);
            if (userToUpdate.isPresent()) {
                User user = userToUpdate.get();
                int pId = user.getPersonId();
                _unitOfWork.getUserRepository().updateEntity(uId, u); 
                _unitOfWork.getPersonRepository().updateEntity(pId,p); 
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        } finally {
            _unitOfWork.close();  // close
        }
    }
    
    public void deleteUser(int id) throws Exception
    {
        try {
            _unitOfWork.begin();  // start transaction

            Optional<User> userToDelete = _unitOfWork.getUserRepository().selectEntity(id);
            if (userToDelete.isPresent()) {
                User user = userToDelete.get();
                int pId = user.getPersonId();
                _unitOfWork.getUserRepository().deleteEntity(id); // Delete user first
                _unitOfWork.getPersonRepository().deleteEntity(pId); // Then delete person
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        } finally {
            _unitOfWork.close();  // close
        }
    }
    
    //perhaps a better approach BLMODELS:
    public List<UserModel> getUsersFromDB() throws Exception
    {
        List<UserModel> userModels = new ArrayList<UserModel>();
        List<User> users = selectUsers();
        List<Person> people = selectPeople();

        // This creates a map that associates person IDs with Person objects
        //https://docs.oracle.com/javase/8/docs/api/java/util/Map.html
        //https://www.freecodecamp.org/news/functional-programming-in-java/#:~:text=Function%20is%20a%20functional%20interface,return%20values%20of%20other%20functions.
        Map<Integer, Person> personMap = people.stream()
        .collect(Collectors.toMap(Person::getPersonId, Function.identity()));

        for (User user : users) {
            Person person = personMap.get(user.getPersonId());
            if (person != null) {
                userModels.add(
                new UserModel(
                    user.getUserId(),
                    user.getPersonId(),
                    user.getUsername(),
                    user.getPasswordHash(),
                    user.getEmail(),
                    user.getRole(),
                    user.getCreatedAt(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getImageUrl(),
                    person.getAge()
                    )
                );
            }
        }
        return userModels;
    }
    
    public UserModel getUserFromDB(int userId) throws Exception {
        List<User> users = selectUsers();
        List<Person> people = selectPeople();

        // Create a map that associates person IDs with Person objects
        Map<Integer, Person> personMap = people.stream()
            .collect(Collectors.toMap(Person::getPersonId, Function.identity()));

        for (User user : users) {
            if (user.getUserId() == userId) {
                Person person = personMap.get(user.getPersonId());
                if (person != null) {
                    return new UserModel(
                        user.getUserId(),
                        user.getPersonId(),
                        user.getUsername(),
                        user.getPasswordHash(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getImageUrl(),
                        person.getAge()
                    );
                }
            }
        }
        return null;
    }
    
    public void updateUserDB(int uId,UserModel um) throws Exception
    {
       try {
           Person p = new Person(
                   um.getFirstName(),
                   um.getLastName(),
                   um.getImageUrl(),
                   um.getAge()
           );
           
           User u =  new User(
                   um.getUsername(),
                   um.getPasswordHash(),
                   um.getEmail(),
                   um.getRole()
           );
           
            _unitOfWork.begin();  // start transaction
            Optional<User> userToUpdate = _unitOfWork.getUserRepository().selectEntity(uId);
            if (userToUpdate.isPresent()) {
                User user = userToUpdate.get();
                int pId = user.getPersonId();
                _unitOfWork.getUserRepository().updateEntity(uId, u); 
                _unitOfWork.getPersonRepository().updateEntity(pId,p); 
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
    
    public void createUserDB(UserModel um) throws Exception
    {
       try {
           Person p = new Person(
                   um.getFirstName(),
                   um.getLastName(),
                   um.getImageUrl(),
                   um.getAge()
           );
           
           User u =  new User(
                   um.getUsername(),
                   um.getPasswordHash(),
                   um.getEmail(),
                   um.getRole(),
                   um.getCreatedAt()
           );
           
            _unitOfWork.begin();  // start transaction

            int personId = _unitOfWork.getPersonRepository().createEntity(p);
            u.setPersonId(personId);
            _unitOfWork.getUserRepository().createEntity(u);

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        } 
    }
    
    public void DeleteUserDB(int id) throws Exception
    {
        try {
            _unitOfWork.begin();  // start transaction

            Optional<User> userToDelete = _unitOfWork.getUserRepository().selectEntity(id);
            if (userToDelete.isPresent()) {
                User user = userToDelete.get();
                int pId = user.getPersonId();
                _unitOfWork.getUserRepository().deleteEntity(id); // Delete user first
                _unitOfWork.getPersonRepository().deleteEntity(pId); // Then delete person
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
}