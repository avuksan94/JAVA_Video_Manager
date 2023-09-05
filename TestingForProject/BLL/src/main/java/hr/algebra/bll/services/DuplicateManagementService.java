/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.services;

import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import hr.algebra.uow.UnitOfWork;
import hr.algebra.uow.UoW;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author antev
 */
//https://java-design-patterns.com/patterns/fluentinterface/#explanation
public class DuplicateManagementService {

    private final UoW _unitOfWork;

    public DuplicateManagementService() throws SQLException {
        this._unitOfWork = new UnitOfWork();
    }

    public boolean checkIfPersonExists(String firstName, String lastName) throws Exception {
        return selectPeople().stream()
                .anyMatch(person -> person.getFirstName().equals(firstName.trim()) && person.getLastName().equals(lastName));
    }

    public int getPersonID(String firstName, String lastName) throws Exception {
        return selectPeople().stream()
                .filter(person -> person.getFirstName().equals(firstName.trim()) && person.getLastName().equals(lastName.trim()))
                .findFirst()
                .map(Person::getPersonId)
                .orElse(0);
    }

    public List<Person> selectPeople() throws Exception {
        return _unitOfWork.getPersonRepository().selectEntities();
    }

    //USERS DUPLICATE CHECK:*********************************************************************
    public boolean checkIfUserExists(String username, String email) throws Exception {
        return selectUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getEmail().equals(email));
    }
    
    public boolean checkIfUsernameExists(String username) throws Exception {
        return selectUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
    
    public boolean checkIfEmailExists(String email) throws Exception {
        return selectUsers().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public int getUserID(String username, String email) throws Exception {
        return selectUsers().stream()
                .filter(user -> user.getUsername().equals(username) && user.getEmail().equals(email))
                .findFirst()
                .map(User::getUserId)
                .orElse(0);
    }

    public List<User> selectUsers() throws Exception {
        return _unitOfWork.getUserRepository().selectEntities();
    }

    //ACTORS DUPLICATE CHECK:*********************************************************************
    public List<Actor> selectActors() throws Exception {
        return _unitOfWork.getActorRepository().selectEntities();
    }

    public boolean checkIfAPRelationExists(int pId) throws Exception {
        return selectActors().stream().anyMatch(actor -> actor.getPersonId() == pId);
    }

    public int getActorID(int pId) throws Exception {
        return selectActors().stream()
                .filter(actor -> actor.getPersonId() == pId)
                .findFirst()
                .map(Actor::getActorId)
                .orElse(0);
    }

    //DIRECTORS DUPLICATE CHECK:*********************************************************************
    public List<Director> selectDirectors() throws Exception {
        return _unitOfWork.getDirectorRepository().selectEntities();
    }

    public boolean checkIfDPRelationExists(int pId) throws Exception {
        return selectDirectors().stream()
                .anyMatch(director -> director.getPersonId() == pId);
    }

    public int getDirectorID(int pId) throws Exception {
        return selectDirectors().stream()
                .filter(director -> director.getPersonId() == pId)
                .findFirst()
                .map(Director::getDirectorId)
                .orElse(0);
    }

    //MOVIES DUPLICATE CHECK:*********************************************************************
    public List<Movie> selectMovies() throws Exception {
        return _unitOfWork.getMovieRepository().selectEntities();
    }

    public boolean checkIfMovieExists(String title) throws Exception {
        return selectMovies().stream()
                .anyMatch(movie -> title.equals(movie.getTitle()));
    }

    public int getMovieID(String title) throws Exception {
         return selectMovies().stream()
                         .filter(movie -> title.equals(movie.getTitle()))
                         .findFirst()
                         .map(Movie::getMovieId)
                         .orElse(0);
    }
    //MOVIES-ACTOR DUPLICATE CHECK:*********************************************************************
    public boolean doesMARelationExist(int movieId,int actorId) throws Exception
    {
        return _unitOfWork.getMovieActorRepository().doesRelationExist(movieId, actorId);
    }
    
    //MOVIES-DIRECTOR DUPLICATE CHECK:*********************************************************************
    public boolean doesMDRelationExist(int movieId,int directorId) throws Exception
    {
        return _unitOfWork.getMovieDirectorRepository().doesRelationExist(movieId, directorId);
    }
}
