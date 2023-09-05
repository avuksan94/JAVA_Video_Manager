package hr.algebra.uow;

import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
import hr.algebra.dal.models.User;
import hr.algebra.dal.repo.ActorRepo;
import hr.algebra.dal.repo.DirectorRepo;
import hr.algebra.dal.repo.MovieActorRepo;
import hr.algebra.dal.repo.MovieDirectorRepo;
import hr.algebra.dal.repo.MovieRepo;
import hr.algebra.dal.repo.PersonRepo;
import hr.algebra.dal.repo.UserRepo;
import hr.algebra.dal.sql.DataSourceSingleton;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 
 * @author antev
 */

//https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html

//https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html

//https://java-design-patterns.com/patterns/unit-of-work/#explanation

public class UnitOfWork implements UoW, AutoCloseable {

    private final Connection connection;
    private GenericRepo<Movie> movieRepository;
    private GenericRepo<Actor> actorRepository;
    private GenericRepo<Director> directorRepository;
    private GenericRepo<User> userRepository;
    private GenericRepo<Person> personRepository;
    private JunctionTableRepo<Movie,Actor> movieActorRepository;
    private JunctionTableRepo<Movie, Director> movieDirectorRepository;
    
    public UnitOfWork() throws SQLException {
        //Database connection
        this.connection = DataSourceSingleton.getInstance().getConnection();
    }
    
    @Override
    public GenericRepo<Movie> getMovieRepository() {
        if (movieRepository == null) {
            movieRepository = new MovieRepo(connection);
        }
        return movieRepository; 
    }

    @Override
    public GenericRepo<Actor> getActorRepository() {
      if (actorRepository == null) {
            actorRepository = new ActorRepo(connection);
        }
        return actorRepository;
    }

    @Override
    public GenericRepo<Director> getDirectorRepository() {
        if (directorRepository == null) {
            directorRepository = new DirectorRepo(connection);
        }
        return directorRepository;
    }

    @Override
    public GenericRepo<User> getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepo(connection);
        }
        return userRepository;
    }
    
     @Override
    public GenericRepo<Person> getPersonRepository() {
      if (personRepository == null) {
            personRepository = new PersonRepo(connection);
        }
        return personRepository;
    }
    
    @Override
    public JunctionTableRepo<Movie,Actor> getMovieActorRepository() {
        if (movieActorRepository == null) {
            movieActorRepository = new MovieActorRepo(connection);
        }
        return movieActorRepository;
    }
    
    @Override
    public JunctionTableRepo<Movie, Director> getMovieDirectorRepository() {
        if (movieDirectorRepository == null) {
            movieDirectorRepository = new MovieDirectorRepo(connection);
        }
        return movieDirectorRepository;
    }
    
    @Override
    public void begin() throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();    
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }
}
