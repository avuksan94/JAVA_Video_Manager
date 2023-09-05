/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.models;

import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.bll.services.DirectorService;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author antev
 */
public class DirectorTableModel  extends AbstractTableModel {
    
     private static final String[] COLUMN_NAMES = {"Id", "First name", "Last name"};

    private List<DirectorModel> directors;
    private final DirectorService _directorService;

    public DirectorTableModel(List<DirectorModel> directors) throws SQLException {
        _directorService = new DirectorService();
        this.directors = directors;
    }

    public void setDirectors(List<DirectorModel> directors) {
        this.directors = directors;
        fireTableDataChanged();
    }

     @Override
    public int getRowCount() {
        return directors.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return directors.get(rowIndex).getDirectorId();
            case 1:
                return directors.get(rowIndex).getFirstName();
            case 2:
                return directors.get(rowIndex).getLastName();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
}
