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
import hr.algebra.bll.services.MovieService;
import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.view.UploadMoviesView;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author antev
 */
public class MovieUploadController {

    private final MovieModel _movieModel;
    private final UploadMoviesView _uploadMoviesView;
    private final MovieService _movieService;
    private final ActorService _actorService;
    private final DirectorService _directorService;

    public MovieUploadController(MovieModel movieModel, UploadMoviesView uploadMoviesView) throws SQLException {
        _movieModel = movieModel;
        _uploadMoviesView = uploadMoviesView;
        _movieService = new MovieService();
        _actorService = new ActorService();
        _directorService = new DirectorService();
    }
    
    public List<MovieModel> getMoviesModel() throws Exception
    {
       return  _movieService.getMoviesFromDB();
    }
    
    public void createMoviesModel(List<MovieModel> movies) throws Exception
    {
       _movieService.createMoviesDB(movies);
    }
    
     public List<ActorModel> getActorsForMovie(MovieModel movie) throws Exception {
        return _actorService.getActorsForMovieFromDB(movie);
    }

    public List<DirectorModel> getDirectorsForMovie(MovieModel movie) throws Exception {
        return _directorService.getDirectorsForMovieFromDB(movie);
    }

    public int getMovieId() {
        return _movieModel.getMovieId();
    }

    public void setMovieId(int movieId) {
        _movieModel.setMovieId(movieId);
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
        _movieModel.setAddedAt(addedAt);
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
