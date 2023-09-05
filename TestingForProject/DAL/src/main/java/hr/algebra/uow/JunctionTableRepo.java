/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.uow;

import java.util.List;

/**
 *
 * @author antev
 */

//https://megocode3.wordpress.com/2008/01/04/understanding-a-sql-junction-table/

public interface JunctionTableRepo<EN1, EN2> {
    void linkEntities(EN1 a, EN2 b) throws Exception;
    void unlinkEntities(EN1 a, EN2 b) throws Exception;
    List<EN2> getLinkedEntities(EN1 a) throws Exception;
    boolean doesRelationExist(int a,int b) throws Exception;
}
