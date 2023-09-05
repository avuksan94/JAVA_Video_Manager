/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.repo;

import hr.algebra.uow.GenericRepo;
import hr.algebra.dal.models.Movie;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author antev
 */
public class MovieRepo implements GenericRepo<Movie> {

    private final Connection connection;
    
    private static final String MOVIE_ID = "MovieID";
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String RELEASE_YEAR = "ReleaseYear";
    private static final String GENRE = "Genre";
    private static final String ADDED_AT = "AddedAt";
    private static final String DURATION = "Duration";
    private static final String POSTER = "Poster";
    
    // SQL Queries
    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?,?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";

    public MovieRepo(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public int createEntity(Movie t) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_MOVIE)) {
            stmt.registerOutParameter(MOVIE_ID, Types.INTEGER);
            stmt.setString(TITLE, t.getTitle());
            stmt.setString(DESCRIPTION, t.getDescription());
            stmt.setInt(RELEASE_YEAR, t.getReleaseYear());
            stmt.setString(GENRE, t.getGenre());
            stmt.setTimestamp(ADDED_AT, Timestamp.valueOf(t.getAddedAt()));
            stmt.setInt(DURATION, t.getDuration());
            stmt.setString(POSTER, t.getPoster());

            stmt.executeUpdate();
            return stmt.getInt(MOVIE_ID);
        }
    }

    @Override
    public void createAllEntities(List<Movie> ts) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_MOVIE)) {
            for (Movie movie : ts) {
            stmt.registerOutParameter(MOVIE_ID, Types.INTEGER);
            stmt.setString(TITLE, movie.getTitle());
            stmt.setString(DESCRIPTION, movie.getDescription());
            stmt.setInt(RELEASE_YEAR, movie.getReleaseYear());
            stmt.setString(GENRE, movie.getGenre());
            stmt.setTimestamp(ADDED_AT, Timestamp.valueOf(movie.getAddedAt()));
            stmt.setInt(DURATION, movie.getDuration());
            stmt.setString(POSTER, movie.getPoster());

            stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateEntity(int id, Movie t) throws Exception {
         try (CallableStatement stmt = connection.prepareCall(UPDATE_MOVIE)) {
            stmt.setInt(MOVIE_ID, id);
            stmt.setString(TITLE, t.getTitle());
            stmt.setString(DESCRIPTION, t.getDescription());
            stmt.setInt(RELEASE_YEAR, t.getReleaseYear());
            stmt.setString(GENRE, t.getGenre());
            stmt.setInt(DURATION, t.getDuration());
            stmt.setString(POSTER, t.getPoster());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(DELETE_MOVIE)) {

            stmt.setInt(MOVIE_ID, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Movie> selectEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(SELECT_MOVIE)) {
            stmt.setInt(MOVIE_ID, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
                    .toFormatter();
                    return Optional.of(new Movie(
                            rs.getInt(MOVIE_ID),
                            rs.getString(TITLE),
                            rs.getString(DESCRIPTION),
                            rs.getInt(RELEASE_YEAR),
                            rs.getString(GENRE),
                            LocalDateTime.parse(rs.getString(ADDED_AT), formatter),
                            rs.getInt(DURATION),
                            rs.getString(POSTER)));

                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> selectEntities() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
                    .toFormatter();
        try (CallableStatement stmt = connection.prepareCall(SELECT_MOVIES); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                movies.add(new Movie(
                            rs.getInt(MOVIE_ID),
                            rs.getString(TITLE),
                            rs.getString(DESCRIPTION),
                            rs.getInt(RELEASE_YEAR),
                            rs.getString(GENRE),
                            LocalDateTime.parse(rs.getString(ADDED_AT), formatter),
                            rs.getInt(DURATION),
                            rs.getString(POSTER)));
            }
        }
        return movies;
    }
    
}
