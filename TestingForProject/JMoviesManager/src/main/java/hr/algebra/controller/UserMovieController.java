/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.controller;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.bll.services.ActorService;
import hr.algebra.bll.services.DirectorService;
import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.bll.services.MovieService;
import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Movie;
import hr.algebra.view.ManageActorsView;
import hr.algebra.view.ManageMoviesView;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

//https://www.edureka.co/blog/mvc-architecture-in-java/
/**
 *
 * @author antev
 */
public class UserMovieController {

    private final ManageMoviesView _manageMoviesView;
    
    private final MovieService _movieService;
    private final ActorService _actorService;
    private final DirectorService _directorService;
    private final MovieModel _movieModel;
    private final DuplicateManagementService _dupeMgmtService;

    public UserMovieController(MovieModel movieModel, ManageMoviesView manageMoviesView) throws SQLException {
        _movieModel = movieModel;
        _manageMoviesView = manageMoviesView;
        _movieService = new MovieService();
        _actorService = new ActorService();
        _directorService = new DirectorService();
        _dupeMgmtService = new DuplicateManagementService();
    }
   
    
    public boolean doesMovieExist(String title) throws Exception
    {
        return _dupeMgmtService.checkIfMovieExists(title);
    }

    public List<MovieModel> getMoviesForModel() throws Exception {
        return _movieService.getMoviesFromDB();
    }

    public MovieModel getMovieForModel(int id) throws Exception {
        return _movieService.getMovieFromDB(id);
    }

    public List<ActorModel> getActorsForModel() throws Exception {
        return _actorService.getAllActorsFromDB();
    }

    public List<DirectorModel> getDirectorsForModel() throws Exception {
        return _directorService.getAllDirectorsFromDB();
    }

    public List<ActorModel> getActorsForMovie(MovieModel movie) throws Exception {
        return _actorService.getActorsForMovieFromDB(movie);
    }

    public List<DirectorModel> getDirectorsForMovie(MovieModel movie) throws Exception {
        return _directorService.getDirectorsForMovieFromDB(movie);
    }

    public void removeActorFromMovieDB(MovieModel movie, ActorModel actor) throws Exception {
        _actorService.removeActorFromMovieDB(movie, actor);
    }

    public void addActorToMovieDB(MovieModel movie, ActorModel actor) throws Exception {
        _actorService.addActorToMovieDB(movie, actor);
    }

    public void removeDirectorFromMovieDB(MovieModel movie, DirectorModel director) throws Exception {
        _directorService.removeDirectorFromMovieDB(movie, director);
    }

    public void addDirectorToMovieDB(MovieModel movie, DirectorModel director) throws Exception {
        _directorService.addDirectorToMovieDB(movie, director);
    }
    
    public void updateMovieDB(MovieModel movModel) throws Exception
    {
        _movieService.updateMovieDB(movModel);
    }
    
    public void DeleteMovieDB(int id) throws Exception 
    {
        _movieService.DeleteMovieDB(id);
    }
    
    public void createMovieDB(MovieModel movModel) throws Exception
    {
        _movieService.createMovieDB(movModel);
    }
    
    public String getTitle() {
        return _movieModel.getTitle();
    }

    public void setTitle(String title) {
        _movieModel.setTitle(title);
    }

    public String getDescription() {
        return _movieModel.getDescription();
    }

    public void setDescription(String description) {
        _movieModel.setDescription(description);
    }

    public int getReleaseYear() {
        return _movieModel.getReleaseYear();
    }

    public void setReleaseYear(int releaseYear) {
        _movieModel.setReleaseYear(releaseYear);
    }

    public String getGenre() {
        return _movieModel.getGenre();
    }

    public void setGenre(String genre) {
        _movieModel.setGenre(genre);
    }

    public LocalDateTime getAddedAt() {
        return _movieModel.getAddedAt();
    }

    public void setAddedAt(LocalDateTime addedAt) {
        _movieModel.getAddedAt();
    }

    public int getDuration() {
        return _movieModel.getDuration();
    }

    public void setDuration(int duration) {
        _movieModel.setDuration(duration);
    }

    public String getPoster() {
        return _movieModel.getPoster();
    }

    public void setPoster(String poster) {
        _movieModel.setPoster(poster);
    }

    public List<Actor> getActors() {
        return _movieModel.getActors();
    }

    public void setActors(List<Actor> actors) {
        _movieModel.setActors(actors);
    }

    public List<Director> getDirectors() {
        return _movieModel.getDirectors();
    }

    public void setDirectors(List<Director> directors) {
        _movieModel.setDirectors(directors);
    }
}
