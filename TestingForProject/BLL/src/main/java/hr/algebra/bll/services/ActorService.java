/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.services;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.dal.models.Actor;
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
public class ActorService {

    private final UoW _unitOfWork;

    public ActorService() throws SQLException {
        this._unitOfWork = new UnitOfWork();
    }

    private List<Actor> selectActors() throws Exception {
        return _unitOfWork.getActorRepository().selectEntities();
    }

    public int createActor(Person p) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            int personId = _unitOfWork.getPersonRepository().createEntity(p);
            Actor actor = new Actor(personId);
            int actorId = _unitOfWork.getActorRepository().createEntity(actor);

            _unitOfWork.commit();  // commit transaction
            return actorId;
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
    
    public int createActorByID(int pId) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction
            
            Actor actor = new Actor(pId);
            int actorId = _unitOfWork.getActorRepository().createEntity(actor);

            _unitOfWork.commit();  // commit transaction
            return actorId;
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public Actor selectActor(int id) throws Exception {
        try {
            Optional<Actor> actor = _unitOfWork.getActorRepository().selectEntity(id);
            if (actor.isPresent()) {
                Actor actorSelect = actor.get();
                return actorSelect;
            } else {
                throw new Exception("Actor not found"); // throw an exception when the person isnt present
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String getActorNames(List<Actor> actors) throws Exception {
        StringBuilder actorNames = new StringBuilder();
        for (Actor actor : actors) {
            Optional<Person> personOptional = _unitOfWork.getPersonRepository().selectEntity(actor.getPersonId());
            if (personOptional.isPresent()) {
                Person person = personOptional.get();
                actorNames.append(person.getFirstName()).append(" ").append(person.getLastName()).append(", ");
            } else {
                throw new Exception("Person not found - Actor service"); // throw an exception when the person isnt present
            }
        }
        if (actorNames.length() > 0) {
            actorNames.delete(actorNames.length() - 2, actorNames.length());
        }
        return actorNames.toString();
    }
    
    public void createActorDB(ActorModel actorModel) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            Person person = new Person(
                    actorModel.getFirstName(),
                    actorModel.getLastName()
            );
            
            int pId = _unitOfWork.getPersonRepository().createEntity(person);
            Actor actor = new Actor(pId);
            
            _unitOfWork.getActorRepository().createEntity(actor);
            
            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public List<ActorModel> getAllActorsFromDB() throws Exception {
        List<ActorModel> actorModels = new ArrayList<ActorModel>();
        List<Actor> actors = selectActors();
        List<Person> people = _unitOfWork.getPersonRepository().selectEntities();

        Map<Integer, Person> personMap = people.stream()
                .collect(Collectors.toMap(Person::getPersonId, Function.identity()));

        for (Actor actor : actors) {
            Person person = personMap.get(actor.getPersonId());
            if (person != null) {
                actorModels.add(
                        new ActorModel(
                                actor.getActorId(),
                                person.getFirstName(),
                                person.getLastName()
                        )
                );
            }
        }
        return actorModels;
    }

    public ActorModel getActorFromDB(int actorId) throws Exception {
        //pretty cool way of throwing exception
        Actor actor = _unitOfWork.getActorRepository().selectEntity(actorId).orElseThrow(() -> new Exception("Actor not found"));
        Optional<Person> optionalPerson = _unitOfWork.getPersonRepository().selectEntity(actor.getPersonId());
        Person person = optionalPerson.get();
        return new ActorModel(
                actorId,
                person.getFirstName(),
                person.getLastName()
        );
    }

    public List<ActorModel> getActorsForMovieFromDB(MovieModel movie) throws Exception {
        List<ActorModel> actorModels = new ArrayList<ActorModel>();
        Optional<Movie> movieOptional = _unitOfWork.getMovieRepository().selectEntity(movie.getMovieId());
        Movie movieSelect = movieOptional.get();
        List<Actor> actors = _unitOfWork.getMovieActorRepository().getLinkedEntities(movieSelect);
        List<Person> people = _unitOfWork.getPersonRepository().selectEntities();

        Map<Integer, Person> personMap = people.stream()
                .collect(Collectors.toMap(Person::getPersonId, Function.identity()));

        for (Actor actor : actors) {
            Person person = personMap.get(actor.getPersonId());
            if (person != null) {
                actorModels.add(
                        new ActorModel(
                                actor.getActorId(),
                                person.getFirstName(),
                                person.getLastName()
                        )
                );
            }
        }
        return actorModels;
    }

    public void updateActorDB(ActorModel actModel) throws Exception {
        try {
            Person p = new Person(
                   actModel.getFirstName(),
                    actModel.getLastName()
            );
            _unitOfWork.begin();  // start transaction
            Optional<Actor> actorOptional = _unitOfWork.getActorRepository().selectEntity(actModel.getActorId());
            if (actorOptional.isPresent()) {
                Actor actor = actorOptional.get();
                
                _unitOfWork.getPersonRepository().updateEntity(actor.getPersonId(), p);
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public void removeActorFromMovieDB(MovieModel movie, ActorModel actor) throws Exception {
        Optional<Movie> movieOptional = _unitOfWork.getMovieRepository().selectEntity(movie.getMovieId());
        Movie mov = movieOptional.get();

        Optional<Actor> actorOptional = _unitOfWork.getActorRepository().selectEntity(actor.getActorId());
        Actor act = actorOptional.get();

        _unitOfWork.getMovieActorRepository().unlinkEntities(mov, act);
    }

    public void addActorToMovieDB(MovieModel movie, ActorModel actor) throws Exception {
        Optional<Movie> movieOptional = _unitOfWork.getMovieRepository().selectEntity(movie.getMovieId());
        Movie mov = movieOptional.get();

        Optional<Actor> actorOptional = _unitOfWork.getActorRepository().selectEntity(actor.getActorId());
        Actor act = actorOptional.get();

        _unitOfWork.getMovieActorRepository().linkEntities(mov, act);
    }
    
    public void DeleteActorDB(int id) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            Optional<Actor> actorToDelete = _unitOfWork.getActorRepository().selectEntity(id);
            if (actorToDelete.isPresent()) {
                Actor actor = actorToDelete.get();
                
                int pId = actor.getPersonId();
                _unitOfWork.getActorRepository().deleteEntity(id);
                _unitOfWork.getPersonRepository().deleteEntity(pId);
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
}
