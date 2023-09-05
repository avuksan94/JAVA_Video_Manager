/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hr.algebra.view;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.controller.UserActorController;
import hr.algebra.models.ActorTableModel;
import hr.algebra.utilities.MessageUtils;
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
public class ManageActorsView extends javax.swing.JPanel {

    /**
     * Creates new form ManageActorsView
     */
    private StringBuilder errorMessages;

    private List<JTextComponent> validationFields;
    private List<JLabel> errorLabels;

    private DefaultListModel<ActorModel> actorModelDef;
    private ActorTableModel actorsTableModel;
    private ActorModel selectedModel;

    private UserActorController userController;

    public ManageActorsView() {
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
            Logger.getLogger(ManageActorsView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }

    private void initModel() {
        selectedModel = new ActorModel();
    }

    private void initValidation() {
        validationFields = Arrays.asList(tfActorFirstName, tfActorLastName);
        errorLabels = Arrays.asList(lbErrorFirstName, lbErrorLastName);
    }

    private void hideErrors() {
        errorLabels.forEach(e -> e.setVisible(false));
    }

    private void showErrors() {
        errorLabels.forEach(e -> e.setVisible(true));
    }

    private void initController() throws Exception {
        ActorModel actorModel = new ActorModel();
        userController = new UserActorController(actorModel, this);
    }

    private void initTable() throws Exception {
        tbActorModelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbActorModelTable.setAutoCreateRowSorter(true);
        tbActorModelTable.setRowHeight(25);
        actorsTableModel = new ActorTableModel(userController.getActorsForModel());
        tbActorModelTable.setModel(actorsTableModel);
    }

    private void fillForm(ActorModel actorModel) throws Exception {
        tfActorFirstName.setText(actorModel.getFirstName());
        tfActorLastName.setText(actorModel.getLastName());
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
        Set<ConstraintViolation<ActorModel>> actorModelViolations = validator.validate(selectedModel);

        ok = validateActorModel(actorModelViolations);

        return ok;
    }

    public boolean validateActorModel(Set<ConstraintViolation<ActorModel>> av) {
        boolean isValid = true;
        errorMessages = new StringBuilder();
        // If there are any violations, print the error messages
        if (!av.isEmpty()) {
            isValid = false;
            av.forEach(violation -> {
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

    private void showActor() {
        //System.out.println(new File(".").getAbsolutePath());
        clearForm();
        int selectedRow = tbActorModelTable.getSelectedRow();
        int rowIndex = tbActorModelTable.convertRowIndexToModel(selectedRow);
        int selectedActorId = (int) actorsTableModel.getValueAt(rowIndex, 0);

        try {
            selectedModel = userController.getActorForModel(selectedActorId);
            fillForm(selectedModel);
        } catch (Exception ex) {
            Logger.getLogger(ManageActorsView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to show actor!");
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tbActorModelTable = new javax.swing.JTable();
        lbActorFirstName = new javax.swing.JLabel();
        lbActorLastName = new javax.swing.JLabel();
        tfActorFirstName = new javax.swing.JTextField();
        tfActorLastName = new javax.swing.JTextField();
        btnAddActor = new javax.swing.JButton();
        btnUpdateActor = new javax.swing.JButton();
        btnDeleteActor = new javax.swing.JButton();
        lbErrorFirstName = new javax.swing.JLabel();
        lbErrorLastName = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(790, 800));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        tbActorModelTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbActorModelTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbActorModelTable);

        lbActorFirstName.setText("First name");

        lbActorLastName.setText("Last name");

        btnAddActor.setText("Add actor");
        btnAddActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActorActionPerformed(evt);
            }
        });

        btnUpdateActor.setText("Update Actor");
        btnUpdateActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActorActionPerformed(evt);
            }
        });

        btnDeleteActor.setBackground(new java.awt.Color(255, 102, 102));
        btnDeleteActor.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteActor.setText("Delete Actor");
        btnDeleteActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActorActionPerformed(evt);
            }
        });

        lbErrorFirstName.setForeground(new java.awt.Color(255, 0, 0));
        lbErrorFirstName.setText("X");

        lbErrorLastName.setForeground(new java.awt.Color(255, 0, 0));
        lbErrorLastName.setText("X");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lbActorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfActorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbErrorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lbActorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfActorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lbErrorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnAddActor, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnUpdateActor, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDeleteActor, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbActorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfActorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbActorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfActorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbErrorLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddActor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateActor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteActor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        init();
    }//GEN-LAST:event_formComponentShown

    private void tbActorModelTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbActorModelTableMouseClicked
        showActor();
    }//GEN-LAST:event_tbActorModelTableMouseClicked

    private void btnUpdateActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActorActionPerformed
        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose actor to update");
            return;
        }

        try {

            selectedModel.setFirstName(tfActorFirstName.getText().trim());
            selectedModel.setLastName(tfActorLastName.getText().trim());

            boolean fNameError = handleFieldError(lbErrorFirstName,
                    userController.checkIfActorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Actor already exists!");
            boolean lNameError = handleFieldError(lbErrorLastName,
                    userController.checkIfActorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Actor already exists!");

            if (fNameError || lNameError) {
                return;
            }

            if (!formValid()) {
                return;
            }

            userController.updateActorDB(selectedModel);
            actorsTableModel.setActors(userController.getActorsForModel());

            clearForm();
            initModel();
        } catch (Exception ex) {
            Logger.getLogger(ManageActorsView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to update actor!");
        }
    }//GEN-LAST:event_btnUpdateActorActionPerformed

    private void btnAddActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActorActionPerformed
        try {

            selectedModel = new ActorModel(
                    tfActorFirstName.getText().trim(),
                    tfActorLastName.getText().trim()
            );

            boolean fNameError = handleFieldError(lbErrorFirstName,
                    userController.checkIfActorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Actor already exists!");
            boolean lNameError = handleFieldError(lbErrorLastName,
                    userController.checkIfActorExists(selectedModel.getFirstName(),
                            selectedModel.getLastName()),
                    "Actor already exists!");

            if (fNameError || lNameError) {
                return;
            }

            if (!formValid()) {
                return;
            }

            userController.createActorDB(selectedModel);
            actorsTableModel.setActors(userController.getActorsForModel());

            clearForm();
            //initModel();
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersView.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to create actor!");
        }
    }//GEN-LAST:event_btnAddActorActionPerformed

    private void btnDeleteActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActorActionPerformed
        if (selectedModel == null) {
            MessageUtils.showInformationMessage("Wrong operation", "Please choose actor to delete!");
            return;
        }
        if (MessageUtils.showConfirmDialog(
                "Delete actor",
                "Do you really want to delete the actor?")) {
            try {
                userController.DeleteActorDB(selectedModel.getActorId());
                actorsTableModel.setActors(userController.getActorsForModel());

                clearForm();
                initModel();
            } catch (Exception ex) {
                Logger.getLogger(ManageActorsView.class.getName()).log(Level.SEVERE, null, ex);
                MessageUtils.showErrorMessage("Error", "Unable to delete actor!");
            }
        }
    }//GEN-LAST:event_btnDeleteActorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddActor;
    private javax.swing.JButton btnDeleteActor;
    private javax.swing.JButton btnUpdateActor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbActorFirstName;
    private javax.swing.JLabel lbActorLastName;
    private javax.swing.JLabel lbErrorFirstName;
    private javax.swing.JLabel lbErrorLastName;
    private javax.swing.JTable tbActorModelTable;
    private javax.swing.JTextField tfActorFirstName;
    private javax.swing.JTextField tfActorLastName;
    // End of variables declaration//GEN-END:variables
}
