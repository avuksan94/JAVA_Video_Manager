/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.models;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.services.ActorService;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author antev
 */
public class ActorTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Id", "First name", "Last name"};

    private List<ActorModel> actors;
    private final ActorService _actorService;

    public ActorTableModel(List<ActorModel> actors) throws SQLException {
        _actorService = new ActorService();
        this.actors = actors;
    }

    public void setActors(List<ActorModel> actors) {
        this.actors = actors;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return actors.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return actors.get(rowIndex).getActorId();
            case 1:
                return actors.get(rowIndex).getFirstName();
            case 2:
                return actors.get(rowIndex).getLastName();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

}
