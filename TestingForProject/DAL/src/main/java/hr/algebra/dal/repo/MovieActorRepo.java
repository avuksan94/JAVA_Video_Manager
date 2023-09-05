/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.repo;

import hr.algebra.uow.JunctionTableRepo;
import hr.algebra.dal.models.Actor;
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
public class MovieActorRepo implements JunctionTableRepo<Movie, Actor> {

    //MOVIE ACTOR IS JUCNTION(BRIDGE TABLE)
    private final Connection connection;

    private static final String MOVIE_ID = "MovieID";
    private static final String ACTOR_ID = "ActorID";

    private static final String PERSON_ID = "PersonID";

    // SQL Queries
    private static final String CREATE_MOVIE_ACTOR = "{ CALL createMovieActor (?,?) }";
    private static final String DELETE_MOVIE_ACTOR = "{ CALL deleteMovieActor (?,?) }";
    private static final String SELECT_ACTORS_BY_MOVIE = "{ CALL selectActorsByMovie (?) }";
    private static final String CHECK_MOVIE_ACTOR_RELATION = "{ CALL checkMovieActorRelation (?,?) }";

    public MovieActorRepo(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void linkEntities(Movie movie, Actor actor) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_MOVIE_ACTOR)) {
            stmt.setInt(MOVIE_ID, movie.getMovieId());
            stmt.setInt(ACTOR_ID, actor.getActorId());
            stmt.execute();
        }
    }

    @Override
    public void unlinkEntities(Movie movie, Actor actor) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(DELETE_MOVIE_ACTOR)) {
            stmt.setInt(MOVIE_ID, movie.getMovieId());
            stmt.setInt(ACTOR_ID, actor.getActorId());
            stmt.execute();
        }
    }

    @Override
    public List<Actor> getLinkedEntities(Movie movie) throws Exception {
        List<Actor> actors = new ArrayList<>();
        try (CallableStatement stmt = connection.prepareCall(SELECT_ACTORS_BY_MOVIE)) {
            stmt.setInt(MOVIE_ID, movie.getMovieId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Actor(
                            rs.getInt(ACTOR_ID),
                            rs.getInt(PERSON_ID)
                    ));
                }
            }
        }
        return actors;
    }

    @Override
    public boolean doesRelationExist(int movieId, int actorId) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CHECK_MOVIE_ACTOR_RELATION)) {
            stmt.setInt(MOVIE_ID, movieId);
            stmt.setInt(ACTOR_ID, actorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1;
                }
            }
        }
        return false;
    }
}
