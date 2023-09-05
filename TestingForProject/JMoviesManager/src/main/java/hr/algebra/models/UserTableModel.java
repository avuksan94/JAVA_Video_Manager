/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.models;
import hr.algebra.bll.blModels.UserModel;
import hr.algebra.dal.models.User;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author antev
 */
public class UserTableModel extends AbstractTableModel{

    private static final String[] COLUMN_NAMES = {"Id", "Username", "Password", "Email", "Role", "CreatedAt","Person Id", "First name", "Last name", "ImageURL", "Age"};
    
    private List<UserModel> users;

    public UserTableModel(List<UserModel> users) {
        this.users = users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
        fireTableDataChanged();
    } 
    
    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return users.get(rowIndex).getUserId();
            case 1:
                return users.get(rowIndex).getUsername();
            case 2:
                return users.get(rowIndex).getPasswordHash();
            case 3:
                return users.get(rowIndex).getEmail();
            case 4:
                return users.get(rowIndex).getRole();
            case 5:
                return users.get(rowIndex).getCreatedAt();
            case 6:
                return users.get(rowIndex).getPersonId();
            case 7:
                return users.get(rowIndex).getFirstName();
            case 8:
                return users.get(rowIndex).getLastName();
            case 9:
                return users.get(rowIndex).getImageUrl();
            case 10:
                return users.get(rowIndex).getAge();
            default:
                throw new RuntimeException("No such column");
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
}
