/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.uow;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author antev
 */
public interface GenericRepo<T> {
    
    int createEntity(T t) throws Exception;

    void createAllEntities(List<T> ts) throws Exception;

    void updateEntity(int id, T t) throws Exception;

    void deleteEntity(int id) throws Exception;

    Optional<T> selectEntity(int id) throws Exception;
    
    List<T> selectEntities() throws Exception;

}
