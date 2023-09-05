/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.repo;

import hr.algebra.uow.GenericRepo;
import hr.algebra.dal.models.Person;
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
public class PersonRepo implements GenericRepo<Person> {
    private final Connection connection;

    public PersonRepo(Connection connection) {
        this.connection = connection;
    }
    
    private static final String PERSON_ID = "PersonID";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String IMAGE_URL = "ImageUrl";
    private static final String AGE = "Age";

    // SQL Queries
    private static final String CREATE_PERSON = "{ CALL createPerson (?,?,?,?,?) }";
    private static final String UPDATE_PERSON = "{ CALL updatePerson (?,?,?,?,?) }";
    private static final String DELETE_PERSON = "{ CALL deletePerson (?) }";
    private static final String SELECT_PERSON = "{ CALL selectPerson (?) }";
    private static final String SELECT_PERSONS = "{ CALL selectPersons }";
    
    @Override
    public int createEntity(Person t) throws Exception {
      try (CallableStatement stmt = connection.prepareCall(CREATE_PERSON)) {
        
        stmt.registerOutParameter(PERSON_ID, Types.INTEGER);
        stmt.setString(FIRST_NAME, t.getFirstName());
        stmt.setString(LAST_NAME, t.getLastName());
        stmt.setString(IMAGE_URL, t.getImageUrl());
        stmt.setInt(AGE, t.getAge());

        stmt.executeUpdate();
        return stmt.getInt(PERSON_ID);
        }
    }

    @Override
    public void createAllEntities(List<Person> ts) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(CREATE_PERSON)) {

            for (Person person : ts) {
                stmt.registerOutParameter(PERSON_ID, Types.INTEGER);
                stmt.setString(FIRST_NAME, person.getFirstName());
                stmt.setString(LAST_NAME, person.getLastName());
                stmt.setString(IMAGE_URL, person.getImageUrl());
                stmt.setInt(AGE, person.getAge());

            stmt.executeUpdate();
            }
        }
   }

    @Override
    public void updateEntity(int id, Person t) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(UPDATE_PERSON)) {

            stmt.setInt(PERSON_ID, id);
            stmt.setString(FIRST_NAME, t.getFirstName());
            stmt.setString(LAST_NAME, t.getLastName());
            stmt.setString(IMAGE_URL, t.getImageUrl());
            stmt.setInt(AGE, t.getAge());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(DELETE_PERSON)) {

            stmt.setInt(PERSON_ID, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Person> selectEntity(int id) throws Exception {
        try (CallableStatement stmt = connection.prepareCall(SELECT_PERSON)) {
            stmt.setInt(PERSON_ID, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Person(
                            rs.getInt(PERSON_ID),
                            rs.getString(FIRST_NAME),
                            rs.getString(LAST_NAME),
                            rs.getString(IMAGE_URL),
                            rs.getInt(AGE)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Person> selectEntities() throws Exception {
        List<Person> people = new ArrayList<>();
        try (CallableStatement stmt = connection.prepareCall(SELECT_PERSONS); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                people.add(new Person(
                        rs.getInt(PERSON_ID),
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME),
                        rs.getString(IMAGE_URL),
                        rs.getInt(AGE)));
            }
        }
        return people;
    }
}
