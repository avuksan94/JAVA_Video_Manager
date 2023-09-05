/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.repo;

import hr.algebra.uow.GenericRepo;
import hr.algebra.dal.models.Actor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author antev
 */
public class ActorRepo implements GenericRepo<Actor> {

    private final Connection connection;
    
    private static final String ACTOR_ID = "ActorID";
    private static final String PERSON_ID = "PersonID";
    
    // SQL Queries
    private static final String CREATE_ACTOR = "{ CALL createActor (?,?) }";
    //private static final String UPDATE_ACTOR = "{ CALL updateActor (?,?) }";
    private static final String DELETE_ACTOR = "{ CALL deleteActor (?) }";
    private static final String SELECT_ACTOR = "{ CALL selectActor (?) }";
    private static final String SELECT_ACTORS = "{ CALL selectActors }";

    public ActorRepo(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public int createEntity(Actor t) throws Exception {
         try (CallableStatement stmt = connection.prepareCall(CREATE_ACTOR)) {
            
            stmt.registerOutParameter(ACTOR_ID, Types.INTEGER);
            stmt.setInt(PERSON_ID, t.getPersonId());

            stmt.executeUpdate();
            return stmt.getInt(ACTOR_ID);
        }
    }

    @Override
    public void createAllEntities(List<Actor> ts) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_ACTOR)) {

            for (Actor actor : ts) {
                stmt.registerOutParameter(ACTOR_ID, Types.INTEGER);
                stmt.setInt(PERSON_ID, actor.getPersonId());

            stmt.executeUpdate();
            }
        }
    }

    //I only need the update for person 
    @Override
    public void updateEntity(int id, Actor t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(DELETE_ACTOR)) {

            stmt.setInt(ACTOR_ID, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Actor> selectEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(SELECT_ACTOR)) {
            stmt.setInt(ACTOR_ID, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Actor(
                            rs.getInt(ACTOR_ID),
                            rs.getInt(PERSON_ID)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Actor> selectEntities() throws Exception {
        List<Actor> actors = new ArrayList<>();
        try (CallableStatement stmt = connection.prepareCall(SELECT_ACTORS); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                actors.add(new Actor(
                        rs.getInt(ACTOR_ID),
                       rs.getInt(PERSON_ID)));
            }
        }
        return actors;
    }   
}
