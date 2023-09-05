/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.repo;

import hr.algebra.uow.JunctionTableRepo;
import hr.algebra.dal.models.Director;
import hr.algebra.dal.models.Movie;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antev
 */
public class MovieDirectorRepo implements JunctionTableRepo<Movie, Director> {

    private final Connection connection;

    private static final String MOVIE_ID = "MovieID";
    private static final String DIRECTOR_ID = "DirectorID";

    private static final String PERSON_ID = "PersonID";

    // SQL Queries
    private static final String CREATE_MOVIE_DIRECTOR = "{ CALL createMovieDirector (?,?) }";
    private static final String DELETE_MOVIE_DIRECTOR = "{ CALL deleteMovieDirector (?,?) }";
    private static final String SELECT_DIRECTORS_BY_MOVIE = "{ CALL selectDirectorsByMovie (?) }";
    private static final String CHECK_MOVIE_DIRECTOR_RELATION = "{ CALL checkMovieDirectorRelation (?,?) }";

    public MovieDirectorRepo(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void linkEntities(Movie movie, Director director) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_MOVIE_DIRECTOR)) {
            stmt.setInt(MOVIE_ID, movie.getMovieId());
            stmt.setInt(DIRECTOR_ID, director.getDirectorId());
            stmt.execute();
        }
    }

    @Override
    public void unlinkEntities(Movie movie, Director director) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(DELETE_MOVIE_DIRECTOR)) {
            stmt.setInt(MOVIE_ID, movie.getMovieId());
            stmt.setInt(DIRECTOR_ID, director.getDirectorId());
            stmt.execute();
        }
    }

    @Override
    public List<Director> getLinkedEntities(Movie movie) throws Exception {
        List<Director> directors = new ArrayList<>();
        try (CallableStatement stmt = connection.prepareCall(SELECT_DIRECTORS_BY_MOVIE)) {
            stmt.setInt(MOVIE_ID, movie.getMovieId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    directors.add(new Director(
                            rs.getInt(DIRECTOR_ID),
                            rs.getInt(PERSON_ID)
                    ));
                }
            }
        }
        return directors;
    }

    @Override
    public boolean doesRelationExist(int movieId, int directorId) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CHECK_MOVIE_DIRECTOR_RELATION)) {
            stmt.setInt(MOVIE_ID, movieId);
            stmt.setInt(DIRECTOR_ID, directorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1;
                }
            }
        }
        return false;
    }

}
