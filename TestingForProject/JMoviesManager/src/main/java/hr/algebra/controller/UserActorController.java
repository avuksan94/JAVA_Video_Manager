/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.controller;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.services.ActorService;
import hr.algebra.bll.services.DuplicateManagementService;
import hr.algebra.view.ManageActorsView;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author antev
 */
public class UserActorController {

    private final ManageActorsView _manageActorsView;
    private final DuplicateManagementService _dupeMgmt;

    private final ActorService _actorService;
    private final ActorModel _actorModel;

    public UserActorController(ActorModel actorModel, ManageActorsView manageActorsView) throws SQLException {
        _manageActorsView = manageActorsView;
        _actorModel = actorModel;
        _actorService = new ActorService();
        _dupeMgmt = new DuplicateManagementService();
    }

    public List<ActorModel> getActorsForModel() throws Exception {
        return _actorService.getAllActorsFromDB();
    }

    public ActorModel getActorForModel(int id) throws Exception {
        return _actorService.getActorFromDB(id);
    }

    public void updateActorDB(ActorModel actModel) throws Exception {
        _actorService.updateActorDB(actModel);
    }

    public void createActorDB(ActorModel actorModel) throws Exception {
        _actorService.createActorDB(actorModel);
    }

    public void DeleteActorDB(int id) throws Exception {
        _actorService.DeleteActorDB(id);
    }
    
    public boolean checkIfActorExists(String firstName,String lastName) throws Exception
    {
        if (_dupeMgmt.checkIfPersonExists(firstName, lastName)) {
            int pId = _dupeMgmt.getPersonID(firstName, lastName);
            return _dupeMgmt.checkIfAPRelationExists(pId);
        }
        return false;
    }

    public String getFirstName() {
        return _actorModel.getFirstName();
    }

    public void setFirstName(String firstName) {
        _actorModel.setFirstName(firstName);
    }

    public String getLastName() {
        return _actorModel.getLastName();
    }

    public void setLastName(String lastName) {
        _actorModel.setLastName(lastName);
    }

    public int getActorId() {
        return _actorModel.getActorId();
    }

    public void setActorId(int actorId) {
        _actorModel.setActorId(actorId);
    }

}
