/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.parsers;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.bll.services.ActorService;
import hr.algebra.bll.services.DirectorService;
import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.dal.jbModels.ActorJB;
import hr.algebra.dal.jbModels.DirectorJB;
import hr.algebra.dal.jbModels.MovieJB;
import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Person;
import hr.algebra.view.ManageMoviesView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author antev
 */
public class LocalMovieParser {

    private final ActorService _actorService;
    private final DirectorService _directorService;
    private final DuplicateManagementService _dupeMgmt;

    public LocalMovieParser() throws SQLException {
        _actorService = new ActorService();
        _directorService = new DirectorService();
        _dupeMgmt = new DuplicateManagementService();
    }

    //JAX B
    public List<MovieModel> convertMovieJBToDB(List<MovieJB> moviesJB) {
        return moviesJB.stream().map(movieJB -> {
            List<DirectorModel> directorsModel;
            List<ActorModel> actorsModel;

            try {
                directorsModel = convertDirectorsJBToDB(movieJB.getDirectors());
                actorsModel = convertActorsJBToDB(movieJB.getActors());
            } catch (Exception ex) {
                Logger.getLogger(ManageMoviesView.class.getName()).log(Level.SEVERE, null, ex);
                directorsModel = new ArrayList<>();
                actorsModel = new ArrayList<>();
            }

            List<Actor> actors = new ArrayList<>();
            List<Director> directors = new ArrayList<>();
            try {
                for (ActorModel actorMod : actorsModel) {
                    String firstName = actorMod.getFirstName();
                    String lastName = actorMod.getLastName();
                    int actorId = 0;
                    if (!_dupeMgmt.checkIfPersonExists(firstName, lastName)) {
                        actorId = _actorService.createActor(new Person(firstName, lastName));
                    } else {
                        int personId = _dupeMgmt.getPersonID(firstName, lastName);
                        if (!_dupeMgmt.checkIfAPRelationExists(personId)) {
                            _actorService.createActorByID(personId);
                        }
                        actorId = _dupeMgmt.getActorID(personId);
                    }
                    Actor actor = _actorService.selectActor(actorId);
                    if (actor != null) {
                        actors.add(actor);
                    } else {
                        System.out.println("Actor with ID " + actorId + " was not found.");
                    }
                }
            } catch (Exception exception) {
                System.out.println("ACTOR PARSING JAXB" + exception);
            }

            try {
                for (DirectorModel directorMod : directorsModel) {
                    String firstName = directorMod.getFirstName();
                    String lastName = directorMod.getLastName();
                    int directorId;
                    if (!_dupeMgmt.checkIfPersonExists(firstName, lastName)) {
                        directorId = _directorService.createDirector(new Person(firstName, lastName));
                    } else {
                        int personId = _dupeMgmt.getPersonID(firstName, lastName);
                        if (!_dupeMgmt.checkIfDPRelationExists(personId)) {
                            _directorService.createDirectorByID(personId);
                        }
                        directorId = _dupeMgmt.getDirectorID(personId);
                    }
                    Director director = _directorService.SelectDirector(directorId);
                    if (director != null) {
                        directors.add(director);
                    }
                }
            } catch (Exception exception) {
                System.out.println("DIRECTOR PARSING JAXB" + exception);
            }

            return new MovieModel(
                    movieJB.getTitle(),
                    movieJB.getDescription(),
                    movieJB.getReleaseYear(),
                    movieJB.getGenre(),
                    movieJB.getPubDate(),
                    movieJB.getDuration(),
                    movieJB.getPoster(),
                    actors,
                    directors
            );
        }).collect(Collectors.toList());
    }

    private List<ActorModel> convertActorsJBToDB(List<ActorJB> actorsJB) {
        if (actorsJB == null) {
            return Collections.emptyList();
        }

        return actorsJB.stream()
                .map(model -> new ActorModel(model.getFirstName(), model.getLastName()))
                .collect(Collectors.toList());
    }

    private List<DirectorModel> convertDirectorsJBToDB(List<DirectorJB> directorsJB) {
        if (directorsJB == null) {
            return Collections.emptyList();
        }

        return directorsJB.stream()
                .map(model -> new DirectorModel(model.getFirstName(), model.getLastName()))
                .collect(Collectors.toList());
    }
}
