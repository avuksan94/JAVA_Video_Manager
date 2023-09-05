/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.repo;

import hr.algebra.uow.GenericRepo;
import hr.algebra.dal.models.User;
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

//https://www.digitalocean.com/community/tutorials/callablestatement-in-java-example
//https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/sql/ResultSet.html
public class UserRepo implements GenericRepo<User> {

    private final Connection connection;
    
    private static final String USER_ID = "UserID";
    private static final String USERNAME = "Username";
    private static final String PASSWORD_HASH = "PasswordHash";
    private static final String EMAIL = "Email";
    private static final String ROLE = "Role";
    private static final String CREATED_AT = "CreatedAt";
    private static final String PERSON_ID = "PersonID";

    // SQL Queries
    private static final String CREATE_USER = "{ CALL createUser (?,?,?,?,?,?,?) }";
    private static final String UPDATE_USER = "{ CALL updateUser (?,?,?,?,?) }";
    private static final String DELETE_USER = "{ CALL deleteUser (?) }";
    private static final String SELECT_USER = "{ CALL selectUser (?) }";
    private static final String SELECT_USERS = "{ CALL selectUsers }";

    public UserRepo(Connection connection) {
        this.connection = connection;
    }
    @Override
    public int createEntity(User t) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_USER)) {
            
            stmt.registerOutParameter(USER_ID, Types.INTEGER);
            stmt.setString(USERNAME, t.getUsername());
            stmt.setString(PASSWORD_HASH, t.getPasswordHash());
            stmt.setString(EMAIL, t.getEmail());
            stmt.setString(ROLE, t.getRole());
            stmt.setTimestamp(CREATED_AT, Timestamp.valueOf(t.getCreatedAt()));
            stmt.setInt(PERSON_ID, t.getPersonId());

            stmt.executeUpdate();
            return stmt.getInt(USER_ID);
        }
    }

    @Override
    public void createAllEntities(List<User> ts) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_USER)) {

            for (User user : ts) {
                stmt.registerOutParameter(USER_ID, Types.INTEGER);
                stmt.setString(USERNAME, user.getUsername());
                stmt.setString(PASSWORD_HASH, user.getPasswordHash());
                stmt.setString(EMAIL, user.getEmail());
                stmt.setString(ROLE, user.getRole());
                stmt.setTimestamp(CREATED_AT, Timestamp.valueOf(user.getCreatedAt()));
                stmt.setInt(PERSON_ID, user.getPersonId());

            stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateEntity(int id, User t) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(UPDATE_USER)) {

            stmt.setInt(USER_ID, id);
            stmt.setString(USERNAME, t.getUsername());
            stmt.setString(PASSWORD_HASH, t.getPasswordHash());
            stmt.setString(EMAIL, t.getEmail());
            stmt.setString(ROLE, t.getRole());
            //stmt.setTimestamp(CREATED_AT, Timestamp.valueOf(t.getCreatedAt()));

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(DELETE_USER)) {

            stmt.setInt(USER_ID, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<User> selectEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(SELECT_USER)) {
            stmt.setInt(USER_ID, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
                    .toFormatter();
                    return Optional.of(new User(
                            rs.getInt(USER_ID),
                            rs.getInt(PERSON_ID),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD_HASH),
                            rs.getString(EMAIL),
                            rs.getString(ROLE),
                        LocalDateTime.parse(rs.getString(CREATED_AT), formatter)));

                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> selectEntities() throws Exception {
        List<User> users = new ArrayList<>();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd HH:mm:ss")
                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
                    .toFormatter();
        try (CallableStatement stmt = connection.prepareCall(SELECT_USERS); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt(USER_ID),
                            rs.getInt(PERSON_ID),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD_HASH),
                            rs.getString(EMAIL),
                            rs.getString(ROLE),
                            LocalDateTime.parse(rs.getString(CREATED_AT), formatter)));
            }
        }
        return users;
    }   
}
