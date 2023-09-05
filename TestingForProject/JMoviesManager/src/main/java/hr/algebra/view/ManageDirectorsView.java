/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hr.algebra.view;

import hr.algebra.bll.blModels.DirectorModel;
import hr.algebra.controller.UserDirectorController;
import hr.algebra.models.DirectorTableModel;
import hr.algebra.utilities.MessageUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ManageDirectorsView extends javax.swing.JPanel {

    /**
     * Creates new form ManageDirectorsView
     */
    private StringBuilder errorMessages;

    private List<JTextComponent> validationFields;
    private List<JLabel> errorLabels;

    private DirectorTableModel directorsTableModel;
    private DirectorModel selectedModel;

    private UserDirectorController userController;

    public ManageDirectorsView() {
        initComponents();
    }

    private void init() {
        try {
            initModel();
            initValidation();
            hideErrors();
            initController();
            initTable();
        } catch (Exception ex) {
            Logger.getLogger(ManageDirectorsView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }

    private void initModel() {
        selectedModel = new DirectorModel();
    }

    private void initValidation() {
        validationFields = Arrays.asList(tfDirectorFirstName, tfDirectorLastName);
        errorLabels = Arrays.asList(lbErrorFirstName, lbErrorLastName);
    }

    private void hideErrors() {
        errorLabels.forEach(e -> e.setVisible(false));
    }

    private void showErrors() {
        errorLabels.forEach(e -> e.setVisible(true));
    }

    private void initController() throws Exception {
        DirectorModel directorModel = new DirectorModel();
        userController = new UserDirectorController(directorModel, this);
    }

    private void initTable() throws Exception {
        tbDirectorModelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbDirectorModelTable.setAutoCreateRowSorter(true);
        tbDirectorModelTable.setRowHeight(25);
        directorsTableModel = new DirectorTableModel(userController.getDirectorsForModel());
        tbDirectorModelTable.setModel(directorsTableModel);
    }

    private void fillForm(DirectorModel directorModel) throws Exception {
        tfDirectorFirstName.setText(directorModel.getFirstName());
        tfDirectorLastName.setText(directorModel.getLastName());
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
        Set<ConstraintViolation<DirectorModel>> directorModelViolations = validator.validate(selectedModel);

        ok = validateActorModel(directorModelViolations);

        return ok;
    }

    public boolean validateActorModel(Set<ConstraintViolation<DirectorModel>> dv) {
        boolean isValid = true;
        errorMessages = new StringBuilder();
        // If there are any violations, print the error messages
        if (!dv.isEmpty()) {
            isValid = false;
            dv.forEach(violation -> {
                String path = violation.getPropertyPath().toString();
                switch (path) {
                    case "firstName":
                        lbErrorFirstName.setVisible(true);
                        lbErrorFirstName.setText(violation.getMessage());
                        break;
                    case "lastName":
                        lbErrorLastName.setVisible(true);
                        lbErrorLastName.setText(violation.getMessage());
                        break;
                }
                errorMessages.append(violation.getMessage());
                errorMessages.append("\n");
                //System.out.println(violation.getMessage());
            });
        }
        return isValid;
    }

    private void showDirector() {
        //System.out.println(new File(".").getAbsolutePath());
        clearForm();
        int selectedRow = tbDirectorModelTable.getSelectedRow();
        int rowIndex = tbDirectorModelTable.convertRowIndexToModel(selectedRow);
        int selectedDirectorId = (int) directorsTableModel.getValueAt(rowIndex, 0);

        try {
            selectedModel = userController.getDirectorForModel(selectedDirectorId);
            fillForm(selectedModel);
        } catch (Exception ex) {
            Logger.getLogger(ManageDirectorsView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to show director!");
        }
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

        lbErrorLastName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDirectorModelTable = new javax.swing.JTable();
        lbDirectorFirstName = new javax.swing.JLabel();
        lbDirectorLastName = new javax.swing.JLabel();
        tfDirectorFirstName = new javax.swing.JTextField();
        tfDirectorLastName = new javax.swing.JTextField();
        btnAddDirector = new javax.swing.JButton();
        btnUpdateDirector = new javax.swing.JButton();
        btnDeleteDirector = new javax.swing.JButton();
        lbErrorFirstName = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(790, 800));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        lbErrorLastName.setForeground(new java.awt.Color(255, 0, 0));
        lbErrorLastName.setText("X");

        tbDirectorModelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbDirectorModelTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDirectorModelTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDirectorModelTable);

        lbDirectorFirstName.setText("First name");

        lbDirectorLastName.setText("Last name");

        btnAddDirector.setText("Add director");
        btnAddDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDirectorActionPerformed(evt);
            }
        });

        btnUpdateDirector.setText("Update Director");
        btnUpdateDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDirectorActionPerformed(evt);
            }
        });

        btnDeleteDirector.setBackground(new java.awt.Color(255, 102, 102));
        btnDeleteDirector.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteDirector.setText("Delete Director");
        btnDeleteDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDirectorActionPerformed(evt);
            }
        });

        lbErrorFirstName.setForeground(new java.awt.Color(255, 0, 0));
        lbErrorFirstName.setText("X");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lbDirectorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfDirectorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbErrorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lbDirectorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfDirectorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbErrorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnAddDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnUpdateDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDeleteDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDirectorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDirectorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDirectorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDirectorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbDirectorModelTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDirectorModelTableMouseClicked
        showDirector();
    }//GEN-LAST:event_tbDirectorModelTableMouseClicked

    private void btnAddDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDirectorActionPerformed
        try {

            selectedModel = new DirectorModel(
                    tfDirectorFirstName.getText().trim(),
                    tfDirectorLastName.getText().trim()
            );

            boolean fNameError = handleFieldError(lbErrorFirstName,
                    userController.checkIfDirectorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Director already exists!");
            boolean lNameError = handleFieldError(lbErrorLastName,
                    userController.checkIfDirectorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Director already exists!");

            if (fNameError || lNameError) {
                return;
            }

            if (!formValid()) {
                return;
            }

            userController.createDirectorDB(selectedModel);
            directorsTableModel.setDirectors(userController.getDirectorsForModel());

            clearForm();
            //initModel();
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to create actor!");
        }
    }//GEN-LAST:event_btnAddDirectorActionPerformed

    private void btnUpdateDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDirectorActionPerformed
        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose director to update");
            return;
        }

        try {
            selectedModel.setFirstName(tfDirectorFirstName.getText().trim());
            selectedModel.setLastName(tfDirectorLastName.getText().trim());

            boolean fNameError = handleFieldError(lbErrorFirstName,
                    userController.checkIfDirectorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Director already exists!");
            boolean lNameError = handleFieldError(lbErrorLastName,
                    userController.checkIfDirectorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Director already exists!");

            if (fNameError || lNameError) {
                return;
            }

            if (!formValid()) {
                return;
            }

            userController.updateDirectorDB(selectedModel);
            directorsTableModel.setDirectors(userController.getDirectorsForModel());

            clearForm();
            initModel();
        } catch (Exception ex) {
            Logger.getLogger(ManageActorsView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to update actor!");
        }
    }//GEN-LAST:event_btnUpdateDirectorActionPerformed

    private void btnDeleteDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDirectorActionPerformed
        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose director to delete!");
            return;
        }
        if (MessageUtils.showConfirmDialog(
                "Delete director",
                "Do you really want to delete the director?")) {
            try {
                userController.DeleteDirectorDB(selectedModel.getDirectorId());
                directorsTableModel.setDirectors(userController.getDirectorsForModel());

                clearForm();
                initModel();
            } catch (Exception ex) {
                Logger.getLogger(ManageDirectorsView.class.getName()).log(Level.SEVERE, null, ex);
                MessageUtils.showErrorMessage("Error", "Unable to delete director!");
            }
        }
    }//GEN-LAST:event_btnDeleteDirectorActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        init();
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDirector;
    private javax.swing.JButton btnDeleteDirector;
    private javax.swing.JButton btnUpdateDirector;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbDirectorFirstName;
    private javax.swing.JLabel lbDirectorLastName;
    private javax.swing.JLabel lbErrorFirstName;
    private javax.swing.JLabel lbErrorLastName;
    private javax.swing.JTable tbDirectorModelTable;
    private javax.swing.JTextField tfDirectorFirstName;
    private javax.swing.JTextField tfDirectorLastName;
    // End of variables declaration//GEN-END:variables
}
