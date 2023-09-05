/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.uow;

import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import java.sql.SQLException;

/**
 *
 * @author antev
 */
public interface UoW {
    GenericRepo<Movie> getMovieRepository();
    GenericRepo<Actor> getActorRepository();
    GenericRepo<Director> getDirectorRepository(); 
    GenericRepo<User> getUserRepository();
    GenericRepo<Person> getPersonRepository();
    JunctionTableRepo<Movie, Actor> getMovieActorRepository();
    JunctionTableRepo<Movie, Director> getMovieDirectorRepository();
    
    void begin() throws SQLException;

    void commit() throws SQLException;

    void rollback();

    void close();
}
