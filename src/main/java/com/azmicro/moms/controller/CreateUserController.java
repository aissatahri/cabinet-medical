/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller;

import com.azmicro.moms.model.Role;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.UtilisateurService;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class CreateUserController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker dateCreationPicker;
    @FXML
    private ComboBox<String> roleComboBox;
    private UtilisateurService utilisateurService = new UtilisateurService();
    private ChangeListener<String> usernameChangeListener;
    private static final Logger logger = Logger.getLogger(CreateUserController.class.getName());
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.utilisateurService = new UtilisateurService();
        dateCreationPicker.setValue(LocalDate.now());

        usernameChangeListener = (observable, oldValue, newValue) -> {
            checkUserExistence(newValue);
        };

        usernameField.textProperty().addListener(usernameChangeListener);

        // Initialize the ComboBox with roles
        ObservableList<String> roles = FXCollections.observableArrayList("Admin", "Secretaire", "Medecin");
        roleComboBox.setItems(roles);
    }

    private void checkUserExistence(String username) {
        logger.log(Level.INFO, "Checking existence for username: {0}", username);
        if (utilisateurService.doesUserExist(username)) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Un utilisateur avec ce nom d'utilisateur existe déjà.");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        clearFields();
                    }
                });
            });
        }
    }

    public void clearFields() {
        Platform.runLater(() -> {
            usernameField.textProperty().removeListener(usernameChangeListener);
            usernameField.clear();
            passwordField.clear();
            roleComboBox.getSelectionModel().clearSelection(); // Clear ComboBox selection
            dateCreationPicker.setValue(LocalDate.now());
            usernameField.textProperty().addListener(usernameChangeListener);
        });
    }

    public Utilisateur getUserInput() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Role role = getSelectedRole();
        LocalDate dateCreation = dateCreationPicker.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null || dateCreation == null) {
            return null;
        }

        return new Utilisateur(0, username, password, role, dateCreation);
    }

    private Role getSelectedRole() {
        String roleText = roleComboBox.getValue(); // Use ComboBox to get the selected value
        if (roleText == null) {
            // Handle null case
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un rôle.");
            alert.showAndWait();
            return null;
        }

        try {
            return Role.valueOf(roleText.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle invalid role input
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Le rôle sélectionné est invalide.");
            alert.showAndWait();
            return null;
        }
    }
}