/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.repo;

import hr.algebra.uow.GenericRepo;
import hr.algebra.dal.models.Director;
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
public class DirectorRepo implements GenericRepo<Director> {

    private final Connection connection;
    
    private static final String DIRECTOR_ID = "DirectorID";
    private static final String PERSON_ID = "PersonID";
    
    // SQL Queries
    private static final String CREATE_DIRECTOR = "{ CALL createDirector (?,?) }";
    //private static final String UPDATE_DIRECTOR = "{ CALL updateActor (?,?) }";
    private static final String DELETE_DIRECTOR = "{ CALL deleteDirector (?) }";
    private static final String SELECT_DIRECTOR = "{ CALL selectDirector (?) }";
    private static final String SELECT_DIRECTORS = "{ CALL selectDirectors }";

    public DirectorRepo(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public int createEntity(Director t) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_DIRECTOR)) {
            
            stmt.registerOutParameter(DIRECTOR_ID, Types.INTEGER);
            stmt.setInt(PERSON_ID, t.getPersonId());

            stmt.executeUpdate();
            return stmt.getInt(DIRECTOR_ID);
        }
    }

    @Override
    public void createAllEntities(List<Director> ts) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_DIRECTOR)) {

            for (Director director : ts) {
                stmt.registerOutParameter(DIRECTOR_ID, Types.INTEGER);
                stmt.setInt(PERSON_ID, director.getPersonId());

            stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateEntity(int id, Director t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteEntity(int id) throws Exception {
         try (CallableStatement stmt = connection.prepareCall(DELETE_DIRECTOR)) {

            stmt.setInt(DIRECTOR_ID, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Director> selectEntity(int id) throws Exception {
         try (CallableStatement stmt = connection.prepareCall(SELECT_DIRECTOR)) {
            stmt.setInt(DIRECTOR_ID, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Director(
                            rs.getInt(DIRECTOR_ID),
                            rs.getInt(PERSON_ID)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Director> selectEntities() throws Exception {
        List<Director> directors = new ArrayList<>();
        try (CallableStatement stmt = connection.prepareCall(SELECT_DIRECTORS); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                directors.add(new Director(
                        rs.getInt(DIRECTOR_ID),
                       rs.getInt(PERSON_ID)));
            }
        }
        return directors;
    }
    
}
