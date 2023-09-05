/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.bll.services;

import hr.algebra.bll.blModels.MovieModel;
import hr.algebra.dal.models.Actor;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Movie;
import hr.algebra.uow.UnitOfWork;
import hr.algebra.uow.UoW;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author antev
 */
public class MovieService {

    private final UoW _unitOfWork;
    private final DuplicateManagementService _dupeManager;

    public MovieService() throws SQLException {
        this._unitOfWork = new UnitOfWork();
        _dupeManager = new DuplicateManagementService();
    }

    private List<Movie> selectMovies() throws Exception {
        return _unitOfWork.getMovieRepository().selectEntities();
    }

    /* private List<Actor> selectActors() throws Exception {
    return _unitOfWork.getActorRepository().selectEntities();
    }
    
    private List<Director> selectDirectors() throws Exception {
    return _unitOfWork.getDirectorRepository().selectEntities();
    }*/
    private void linkActorsToMovie(Movie movie, List<Actor> actors) throws Exception {
        for (Actor actor : actors) {
            _unitOfWork.getMovieActorRepository().linkEntities(movie, actor);
        }
    }

    private void linkDirectorsToMovie(Movie movie, List<Director> directors) throws Exception {
        for (Director director : directors) {
            _unitOfWork.getMovieDirectorRepository().linkEntities(movie, director);
        }
    }

    public List<MovieModel> getMoviesFromDB() throws Exception {
        List<MovieModel> movieModels = new ArrayList<MovieModel>();
        List<Movie> movies = selectMovies();

        for (Movie movie : movies) {
            List<Actor> actorsForMovie = _unitOfWork.getMovieActorRepository().getLinkedEntities(movie);
            List<Director> directorsForMovie = _unitOfWork.getMovieDirectorRepository().getLinkedEntities(movie);
            movieModels.add(
                    new MovieModel(
                            movie.getMovieId(),
                            movie.getTitle(),
                            movie.getDescription(),
                            movie.getReleaseYear(),
                            movie.getGenre(),
                            movie.getAddedAt(),
                            movie.getDuration(),
                            movie.getPoster(),
                            actorsForMovie,
                            directorsForMovie
                    )
            );
        }
        return movieModels;
    }

    public MovieModel getMovieFromDB(int movieId) throws Exception {
        //pretty cool way of throwing exception
        Movie movie = _unitOfWork.getMovieRepository().selectEntity(movieId).orElseThrow(() -> new Exception("Movie not found"));
        List<Actor> actorsForMovie = _unitOfWork.getMovieActorRepository().getLinkedEntities(movie);
        List<Director> directorsForMovie = _unitOfWork.getMovieDirectorRepository().getLinkedEntities(movie);
        return new MovieModel(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getReleaseYear(),
                movie.getGenre(),
                movie.getAddedAt(),
                movie.getDuration(),
                movie.getPoster(),
                actorsForMovie,
                directorsForMovie
        );
    }

    public void createMovieDB(MovieModel movModel) throws Exception {
        try {
            if (!_dupeManager.checkIfMovieExists(movModel.getTitle())) {
                Movie mov = new Movie(
                        movModel.getTitle(),
                        movModel.getDescription(),
                        movModel.getReleaseYear(),
                        movModel.getGenre(),
                        movModel.getAddedAt(),
                        movModel.getDuration(),
                        movModel.getPoster()
                );

                _unitOfWork.begin();  // start transaction

                //Had issues with the parser,forgot that i need to set the id
                int movId = _unitOfWork.getMovieRepository().createEntity(mov);
                mov.setMovieId(movId);

                linkActorsToMovie(mov, movModel.getActors());
                linkDirectorsToMovie(mov, movModel.getDirectors());

                _unitOfWork.commit();  // commit transaction
            }
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public void createMoviesDB(List<MovieModel> movies) throws Exception {
        for (MovieModel movie : movies) {
            createMovieDB(movie);
        }
    }

    public void updateMovieDB(MovieModel movModel) throws Exception {
        try {
            Movie m = new Movie(
                    movModel.getTitle(),
                    movModel.getDescription(),
                    movModel.getReleaseYear(),
                    movModel.getGenre(),
                    movModel.getAddedAt(),
                    movModel.getDuration(),
                    movModel.getPoster()
            );

            int mId = movModel.getMovieId();

            _unitOfWork.begin();  // start transaction
            Optional<Movie> movieToUpdate = _unitOfWork.getMovieRepository().selectEntity(mId);
            if (movieToUpdate.isPresent()) {
                _unitOfWork.getMovieRepository().updateEntity(mId, m);
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }

    public void DeleteMovieDB(int id) throws Exception {
        try {
            _unitOfWork.begin();  // start transaction

            Optional<Movie> movieToDelete = _unitOfWork.getMovieRepository().selectEntity(id);
            if (movieToDelete.isPresent()) {
                Movie movie = movieToDelete.get();
                List<Actor> actors = _unitOfWork.getMovieActorRepository().getLinkedEntities(movie);
                List<Director> directors = _unitOfWork.getMovieDirectorRepository().getLinkedEntities(movie);
                
                for (Actor actor : actors) {
                    _unitOfWork.getMovieActorRepository().unlinkEntities(movie, actor);
                }
                
                for (Director director : directors) {
                    _unitOfWork.getMovieDirectorRepository().unlinkEntities(movie, director);
                }
                _unitOfWork.getMovieRepository().deleteEntity(id); // Delete user first
            }

            _unitOfWork.commit();  // commit transaction
        } catch (Exception e) {
            _unitOfWork.rollback();  // rollback transaction
            throw e;
        }
    }
}
