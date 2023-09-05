/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.controller;

import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.bll.services.DirectorService;
import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.view.ManageDirectorsView;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author antev
 */
public class UserDirectorController {

    private final ManageDirectorsView _manageDirectorsView;

    private final DirectorService _directorService;
    private final DuplicateManagementService _dupeMgmt;
    private final DirectorModel _directorModel;

    public UserDirectorController(DirectorModel directorModel, ManageDirectorsView manageDirectorsView) throws SQLException {
        _manageDirectorsView = manageDirectorsView;
        _directorModel = directorModel;
        _directorService = new DirectorService();
        _dupeMgmt = new DuplicateManagementService();
    }

    public List<DirectorModel> getDirectorsForModel() throws Exception {
        return _directorService.getAllDirectorsFromDB();
    }

    public DirectorModel getDirectorForModel(int id) throws Exception {
        return _directorService.getDirectorFromDB(id);
    }

    public void updateDirectorDB(DirectorModel dirModel) throws Exception {
        _directorService.updateDirectorDB(dirModel);
    }

    public void createDirectorDB(DirectorModel directorModel) throws Exception {
        _directorService.createDirectorDB(directorModel);
    }

    public void DeleteDirectorDB(int id) throws Exception {
        _directorService.DeleteDirectorDB(id);
    }
    
    public boolean checkIfDirectorExists(String firstName,String lastName) throws Exception
    {
        if (_dupeMgmt.checkIfPersonExists(firstName, lastName)) {
            int pId = _dupeMgmt.getPersonID(firstName, lastName);
            return _dupeMgmt.checkIfDPRelationExists(pId);
        }
        return false;
    }

    public String getFirstName() {
        return _directorModel.getFirstName();
    }

    public void setFirstName(String firstName) {
        _directorModel.setFirstName(firstName);
    }

    public String getLastName() {
        return _directorModel.getLastName();
    }

    public void setLastName(String lastName) {
        _directorModel.setLastName(lastName);
    }

    public int getDirectorId() {
        return _directorModel.getDirectorId();
    }

    public void setDirectorId(int directorId) {
        _directorModel.setDirectorId(directorId);
    }
}
