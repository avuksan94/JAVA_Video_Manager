/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.services;

import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
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
public class DirectorService {

    private final UoW _unitOfWork;

    public DirectorService() throws SQLException {
        this._unitOfWork = new UnitOfWork();
    }

    private List<Director> selectDirectors() throws Exception {
        return _unitOfWork.getDirectorRepository().selectEntities();
    }

    public int createDirector(Person p) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            int personId = _unitOfWork.getPersonRepository().createEntity(p);
            Director director = new Director(personId);
            int directorId = _unitOfWork.getDirectorRepository().createEntity(director);

            _unitOfWork.commit();  // commit transaction
            return directorId;
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
    
    public int createDirectorByID(int pId) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            Director director = new Director(pId);
            int directorId = _unitOfWork.getDirectorRepository().createEntity(director);

            _unitOfWork.commit();  // commit transaction
            return directorId;
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public Director SelectDirector(int id) throws Exception {
        try {
            Optional<Director> director = _unitOfWork.getDirectorRepository().selectEntity(id);
            if (director.isPresent()) {
                Director directorSelect = director.get();
                return directorSelect;
            } else {
                throw new Exception("Director not found"); // throw an exception when the person isnt present
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String getDirectorNames(List<Director> directors) throws Exception {
        StringBuilder directorNames = new StringBuilder();
        for (Director director : directors) {
            Optional<Person> personOptional = _unitOfWork.getPersonRepository().selectEntity(director.getPersonId());
            if (personOptional.isPresent()) {
                Person person = personOptional.get();
                directorNames.append(person.getFirstName()).append(" ").append(person.getLastName()).append(", ");
            } else {
                throw new Exception("Person not found - Actor service"); // throw an exception when the person isnt present
            }
        }
        if (directorNames.length() > 0) {
            directorNames.delete(directorNames.length() - 2, directorNames.length());
        }
        return directorNames.toString();
    }
    
    public void createDirectorDB(DirectorModel directorModel) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            Person person = new Person(
                    directorModel.getFirstName(),
                    directorModel.getLastName()
            );
            
            int pId = _unitOfWork.getPersonRepository().createEntity(person);
            Director director = new Director(pId);
            
            _unitOfWork.getDirectorRepository().createEntity(director);
            
            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public List<DirectorModel> getAllDirectorsFromDB() throws Exception {
        List<DirectorModel> directorModels = new ArrayList<DirectorModel>();
        List<Director> directors = selectDirectors();
        List<Person> people = _unitOfWork.getPersonRepository().selectEntities();

        Map<Integer, Person> personMap = people.stream()
                .collect(Collectors.toMap(Person::getPersonId, Function.identity()));

        for (Director director : directors) {
            Person person = personMap.get(director.getPersonId());
            if (person != null) {
                directorModels.add(
                        new DirectorModel(
                                director.getDirectorId(),
                                person.getFirstName(),
                                person.getLastName()
                        )
                );
            }
        }
        return directorModels;
    }

    public List<DirectorModel> getDirectorsForMovieFromDB(MovieModel movie) throws Exception {
        List<DirectorModel> directorModels = new ArrayList<DirectorModel>();
        Optional<Movie> movieOptional = _unitOfWork.getMovieRepository().selectEntity(movie.getMovieId());
        Movie movieSelect = movieOptional.get();
        List<Director> directors = _unitOfWork.getMovieDirectorRepository().getLinkedEntities(movieSelect);
        List<Person> people = _unitOfWork.getPersonRepository().selectEntities();

        Map<Integer, Person> personMap = people.stream()
                .collect(Collectors.toMap(Person::getPersonId, Function.identity()));

        for (Director director : directors) {
            Person person = personMap.get(director.getPersonId());
            if (person != null) {
                directorModels.add(
                        new DirectorModel(
                                director.getDirectorId(),
                                person.getFirstName(),
                                person.getLastName()
                        )
                );
            }
        }
        return directorModels;
    }
    
    public DirectorModel getDirectorFromDB(int directorId) throws Exception {
        //pretty cool way of throwing exception
        Director director = _unitOfWork.getDirectorRepository().selectEntity(directorId).orElseThrow(() -> new Exception("Actor not found"));
        Optional<Person> optionalPerson = _unitOfWork.getPersonRepository().selectEntity(director.getPersonId());
        Person person = optionalPerson.get();
        return new DirectorModel(
                directorId,
                person.getFirstName(),
                person.getLastName()
        );
    }
    
    public void updateDirectorDB(DirectorModel dirModel) throws Exception {
        try {
            Person p = new Person(
                   dirModel.getFirstName(),
                    dirModel.getLastName()
            );
            _unitOfWork.begin();  // start transaction
            Optional<Director> directorOptional = _unitOfWork.getDirectorRepository().selectEntity(dirModel.getDirectorId());
            if (directorOptional.isPresent()) {
                Director director = directorOptional.get();
                
                _unitOfWork.getPersonRepository().updateEntity(director.getPersonId(), p);
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public void removeDirectorFromMovieDB(MovieModel movie, DirectorModel director) throws Exception {
        Optional<Movie> movieOptional = _unitOfWork.getMovieRepository().selectEntity(movie.getMovieId());
        Movie mov = movieOptional.get();

        Optional<Director> directorOptional = _unitOfWork.getDirectorRepository().selectEntity(director.getDirectorId());
        Director dir = directorOptional.get();

        _unitOfWork.getMovieDirectorRepository().unlinkEntities(mov, dir);
    }

    public void addDirectorToMovieDB(MovieModel movie, DirectorModel director) throws Exception {
        Optional<Movie> movieOptional = _unitOfWork.getMovieRepository().selectEntity(movie.getMovieId());
        Movie mov = movieOptional.get();

        Optional<Director> directorOptional = _unitOfWork.getDirectorRepository().selectEntity(director.getDirectorId());
        Director dir = directorOptional.get();

        _unitOfWork.getMovieDirectorRepository().linkEntities(mov, dir);
    }
    
     public void DeleteDirectorDB(int id) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            Optional<Director> directorToDelete = _unitOfWork.getDirectorRepository().selectEntity(id);
            if (directorToDelete.isPresent()) {
                Director director = directorToDelete.get();
                
                int pId = director.getPersonId();
                _unitOfWork.getDirectorRepository().deleteEntity(id);
                _unitOfWork.getPersonRepository().deleteEntity(pId);
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
}
