/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hr.algebra.view;

import hr.algebra.controller.AdminManageUsersController;
import hr.algebra.bll.blModels.UserModel;
import hr.algebra.models.UserTableModel;
import hr.algebra.utilities.MessageUtils;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.text.JTextComponent;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author antev
 */
public class ManageUsersView extends javax.swing.JPanel {

    /**
     * Creates new form ManageUsersView
     */
    /* TEST
    enum UserType {
    USER,
    UNCONFIRMED_USER,
    ADMIN
    }*/
    public ManageUsersView() {
        initComponents();
    }
    private StringBuilder errorMessages;
    private final String DEFAULT_PIC_PATH = "https://img.freepik.com/premium-vector/user-icon-vector-with-blue-background-suitable-web-design-etc_266866-110.jpg?w=996";

    private List<JTextComponent> validationFields;
    private List<JLabel> errorLabels;

    private DefaultListModel<UserModel> userModelDef;
    private UserTableModel usersTableModel;
    private UserModel selectedModel;

    private AdminManageUsersController adminController;

    private void init() {
        try {
            initModel();
            initValidation();
            hideErrors();
            initController();
            initTable();
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }

    private void initModel() {
        selectedModel = new UserModel();
    }

    private void initValidation() {
        validationFields = Arrays.asList(tfUsername, pfPassword, tfEmail, tfFirstName, tfLastName, tfAge);
        errorLabels = Arrays.asList(lbErrorUsername, lbErrorPassword, lbErrorEmail, lbErrorFirstName, lbErrorLastName, lbErrorAge);
    }

    private void hideErrors() {
        errorLabels.forEach(e -> e.setVisible(false));
    }

    private void initController() throws Exception {
        UserModel userModel = new UserModel();
        adminController = new AdminManageUsersController(userModel, this);
    }

    //This is for my custom Table init
    private void initTable() throws Exception {
        tbUserModelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbUserModelTable.setAutoCreateRowSorter(true);
        tbUserModelTable.setRowHeight(25);
        usersTableModel = new UserTableModel(adminController.getUsersForModel());
        tbUserModelTable.setModel(usersTableModel);
    }

    private void fillForm(UserModel userModel) {
        tfUsername.setText(userModel.getUsername());
        pfPassword.setText(userModel.getPasswordHash());
        tfEmail.setText(userModel.getEmail());

        tfFirstName.setText(userModel.getFirstName());
        tfLastName.setText(userModel.getLastName());
        String age = Integer.toString(userModel.getAge());
        tfAge.setText(age);

        switch (userModel.getRole()) {
            case "User":
                cbRole.setSelectedIndex(0);
                break;
            case "Unconfirmed User":
                cbRole.setSelectedIndex(1);
                break;
            case "Admin":
                cbRole.setSelectedIndex(2);
                break;
            default:
                throw new AssertionError();
        }
    }

    private void clearForm() {
        hideErrors();
        validationFields.forEach(e -> e.setText(""));
        selectedModel = null;
    }

    private boolean formValid() {
        hideErrors();
        boolean ok = true;

        // Create validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Validate models
        Set<ConstraintViolation<UserModel>> userModelViolations = validator.validate(selectedModel);

        ok = validateUserModel(userModelViolations);

        return ok;
    }

    private boolean validateUserModel(Set<ConstraintViolation<UserModel>> uv) {
        boolean isValid = true;
        errorMessages = new StringBuilder();
        // If there are any violations, print the error messages
        if (!uv.isEmpty()) {
            isValid = false;
            uv.forEach(violation -> {
                String path = violation.getPropertyPath().toString();
                switch (path) {
                    case "username":
                        lbErrorUsername.setVisible(true);
                        lbErrorUsername.setText(violation.getMessage());
                        break;
                    case "passwordHash":
                        lbErrorPassword.setVisible(true);
                        lbErrorPassword.setText(violation.getMessage());
                        break;
                    case "email":
                        lbErrorEmail.setVisible(true);
                        lbErrorEmail.setText(violation.getMessage());
                        break;
                    case "firstName":
                        lbErrorFirstName.setVisible(true);
                        lbErrorFirstName.setText(violation.getMessage());
                        break;
                    case "lastName":
                        lbErrorLastName.setVisible(true);
                        lbErrorLastName.setText(violation.getMessage());
                        break;
                    case "age":
                        lbErrorAge.setVisible(true);
                        lbErrorAge.setText(violation.getMessage());
                        break;
                }
                errorMessages.append(violation.getMessage());
                errorMessages.append("\n");
                //System.out.println(violation.getMessage());
            });
        }

        return isValid;
    }

    private void showUser() {
        clearForm();
        int selectedRow = tbUserModelTable.getSelectedRow();
        int rowIndex = tbUserModelTable.convertRowIndexToModel(selectedRow);
        int selectedUserId = (int) usersTableModel.getValueAt(rowIndex, 0);

        try {
            selectedModel = adminController.getUserForModel(selectedUserId);;
            fillForm(selectedModel);
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to show user!");
        }
    }

    private boolean checkIfUsernameTaken(String username) throws Exception {
        return adminController.checkIfUsernameTaken(username);
    }

    private boolean checkIfEmailTaken(String email) throws Exception {
        return adminController.checkIfEmailTaken(email);
    }

    private boolean handleFieldError(JLabel label, boolean condition, String errorMessage) {
        if (condition) {
            label.setVisible(true);
            label.setText(errorMessage);
            return true;
        }
        label.setVisible(false);
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbEmail = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        lbUsername = new javax.swing.JLabel();
        lbPassword = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        lbRole = new javax.swing.JLabel();
        lbFirstName = new javax.swing.JLabel();
        tfFirstName = new javax.swing.JTextField();
        lbLastName = new javax.swing.JLabel();
        tfLastName = new javax.swing.JTextField();
        lbAge = new javax.swing.JLabel();
        tfAge = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        lbErrorUsername = new javax.swing.JLabel();
        lbErrorPassword = new javax.swing.JLabel();
        lbErrorEmail = new javax.swing.JLabel();
        lbErrorFirstName = new javax.swing.JLabel();
        lbErrorLastName = new javax.swing.JLabel();
        lbErrorAge = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUserModelTable = new javax.swing.JTable();
        cbRole = new javax.swing.JComboBox<>();
        pfPassword = new javax.swing.JPasswordField();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        lbEmail.setText("Email");

        lbUsername.setText("Username");

        lbPassword.setText("Password");

        tfEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfEmailActionPerformed(evt);
            }
        });

        lbRole.setText("Role");

        lbFirstName.setText("First Name");

        tfFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfFirstNameActionPerformed(evt);
            }
        });

        lbLastName.setText("Last Name");

        tfLastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfLastNameActionPerformed(evt);
            }
        });

        lbAge.setText("Age");

        tfAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfAgeActionPerformed(evt);
            }
        });

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 102, 102));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        lbErrorUsername.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorUsername.setText("X");

        lbErrorPassword.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorPassword.setText("X");

        lbErrorEmail.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorEmail.setText("X");

        lbErrorFirstName.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorFirstName.setText("X");

        lbErrorLastName.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorLastName.setText("X");

        lbErrorAge.setForeground(new java.awt.Color(204, 0, 0));
        lbErrorAge.setText("X");

        tbUserModelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbUserModelTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUserModelTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUserModelTable);

        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User", "Unconfirmed User", "Admin" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbErrorUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbErrorEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbErrorPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbRole, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbErrorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbErrorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbAge, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfAge, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbErrorAge, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(315, 315, 315))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(lbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1038, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorUsername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorPassword)
                    .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbErrorEmail)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbRole, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorFirstName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorLastName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbAge, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbErrorAge)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(29, 29, 29)
                    .addComponent(lbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(745, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tfEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfEmailActionPerformed

    private void tfFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfFirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfFirstNameActionPerformed

    private void tfLastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfLastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfLastNameActionPerformed

    private void tfAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfAgeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfAgeActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        init();
    }//GEN-LAST:event_formComponentShown

    private void tbUserModelTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUserModelTableMouseClicked
        showUser();
    }//GEN-LAST:event_tbUserModelTableMouseClicked

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose user to update");
            return;
        }

        try {
            int age = 0;
            String ageText = tfAge.getText();
            if (!ageText.isEmpty()) {
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException e) {
                    lbErrorAge.setVisible(true);
                }
            } else {
                lbErrorAge.setVisible(true);
            }
            selectedModel.setUsername(tfUsername.getText().trim());
            selectedModel.setPasswordHash(pfPassword.getText().trim());
            selectedModel.setEmail(tfEmail.getText().trim());
            selectedModel.setRole(cbRole.getSelectedItem().toString().trim());

            selectedModel.setFirstName(tfFirstName.getText().trim());
            selectedModel.setLastName(tfLastName.getText().trim());
            //int age = Integer.parseInt(tfAge.getText().trim()); this will cause the number exception
            selectedModel.setAge(age);

            if (!formValid()) {
                return;
            }

            adminController.updateUser(selectedModel.getUserId(), selectedModel);
            usersTableModel.setUsers(adminController.getUsersForModel());

            clearForm();
            initModel();
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to update user!");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        try {

            int age = 0;
            String ageText = tfAge.getText();
            if (!ageText.isEmpty()) {
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException e) {
                    lbErrorAge.setVisible(true);
                }
            } else {
                lbErrorAge.setVisible(true);
            }

            //int age = Integer.parseInt(tfAge.getText().trim());
            selectedModel = new UserModel(
                    tfUsername.getText().trim(),
                    pfPassword.getText().trim(),
                    tfEmail.getText().trim(),
                    cbRole.getSelectedItem().toString().trim(),
                    LocalDateTime.now(),
                    tfFirstName.getText().trim(),
                    tfLastName.getText().trim(),
                    DEFAULT_PIC_PATH,
                    age
            );

            boolean usernameError = handleFieldError(lbErrorUsername, checkIfUsernameTaken(selectedModel.getUsername()), "Username already in use!");
            boolean emailError = handleFieldError(lbErrorEmail, checkIfEmailTaken(selectedModel.getEmail()), "Email already in use!");

            if (usernameError || emailError) {
                return;
            }

            if (!formValid()) {
                return;
            }

            adminController.addUser(selectedModel);
            usersTableModel.setUsers(adminController.getUsersForModel());

            clearForm();
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to create user!");
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose user to delete!");
            return;
        }
        if (MessageUtils.showConfirmDialog(
                "Delete user",
                "Do you really want to delete user?")) {
            try {
                adminController.deleteUser(selectedModel.getUserId());
                usersTableModel.setUsers(adminController.getUsersForModel());

                clearForm();
                initModel();
            } catch (Exception ex) {
                Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
                MessageUtils.showErrorMessage("Error", "Unable to delete user!");
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbRole;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbAge;
    private javax.swing.JLabel lbEmail;
    private javax.swing.JLabel lbErrorAge;
    private javax.swing.JLabel lbErrorEmail;
    private javax.swing.JLabel lbErrorFirstName;
    private javax.swing.JLabel lbErrorLastName;
    private javax.swing.JLabel lbErrorPassword;
    private javax.swing.JLabel lbErrorUsername;
    private javax.swing.JLabel lbFirstName;
    private javax.swing.JLabel lbLastName;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbRole;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JTable tbUserModelTable;
    private javax.swing.JTextField tfAge;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfFirstName;
    private javax.swing.JTextField tfLastName;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}
