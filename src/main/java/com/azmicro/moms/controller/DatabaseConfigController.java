/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.controller;

import com.azmicro.moms.util.DatabaseConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Contrôleur pour la configuration de la base de données
 * @author Aissa
 */
public class DatabaseConfigController {
    
    @FXML
    private TextField hostField;
    
    @FXML
    private TextField portField;
    
    @FXML
    private TextField databaseField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button testButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label statusLabel;
    
    /**
     * Initialise le contrôleur
     */
    @FXML
    public void initialize() {
        loadCurrentConfig();
    }
    
    /**
     * Charge la configuration actuelle dans les champs
     */
    private void loadCurrentConfig() {
        hostField.setText(DatabaseConfig.getHost());
        portField.setText(DatabaseConfig.getPort());
        databaseField.setText(DatabaseConfig.getDatabaseName());
        usernameField.setText(DatabaseConfig.getUsername());
        passwordField.setText(DatabaseConfig.getPassword());
        statusLabel.setText("");
    }
    
    /**
     * Teste la connexion avec les paramètres saisis
     */
    @FXML
    private void handleTestConnection() {
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (!validateFields()) {
            return;
        }
        
        statusLabel.setText("Test de connexion en cours...");
        statusLabel.setStyle("-fx-text-fill: orange;");
        
        try {
            // Créer une URL JDBC temporaire pour tester
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/";
            java.sql.Connection conn = java.sql.DriverManager.getConnection(jdbcUrl, username, password);
            conn.close();
            
            statusLabel.setText("✓ Connexion réussie !");
            statusLabel.setStyle("-fx-text-fill: green;");
            
            showInfo("Test de connexion", "La connexion à la base de données a réussi !");
            
        } catch (Exception e) {
            statusLabel.setText("✗ Échec de la connexion");
            statusLabel.setStyle("-fx-text-fill: red;");
            
            showError("Erreur de connexion", 
                "Impossible de se connecter à la base de données.\n\n" +
                "Erreur: " + e.getMessage() + "\n\n" +
                "Vérifiez les paramètres suivants:\n" +
                "- L'adresse IP et le port sont corrects\n" +
                "- Le serveur MySQL est démarré\n" +
                "- Le nom d'utilisateur et le mot de passe sont valides\n" +
                "- Le pare-feu autorise la connexion"
            );
        }
    }
    
    /**
     * Sauvegarde la configuration
     */
    @FXML
    private void handleSaveConfig() {
        if (!validateFields()) {
            return;
        }
        
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String database = databaseField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        boolean success = DatabaseConfig.saveConfig(host, port, database, username, password);
        
        if (success) {
            statusLabel.setText("✓ Configuration sauvegardée !");
            statusLabel.setStyle("-fx-text-fill: green;");
            
            showInfo("Configuration sauvegardée", 
                "La configuration de la base de données a été sauvegardée avec succès.\n\n" +
                "Fichier: " + DatabaseConfig.getConfigFilePath() + "\n\n" +
                "Redémarrez l'application pour que les changements prennent effet."
            );
            
            // Fermer la fenêtre après 2 secondes
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(this::closeWindow);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            statusLabel.setText("✗ Erreur de sauvegarde");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    /**
     * Annule et ferme la fenêtre
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }
    
    /**
     * Valide les champs saisis
     */
    private boolean validateFields() {
        if (hostField.getText().trim().isEmpty()) {
            showError("Champ requis", "L'adresse IP/hôte est obligatoire.");
            hostField.requestFocus();
            return false;
        }
        
        if (portField.getText().trim().isEmpty()) {
            showError("Champ requis", "Le port est obligatoire.");
            portField.requestFocus();
            return false;
        }
        
        // Valider le port (nombre entre 1 et 65535)
        try {
            int port = Integer.parseInt(portField.getText().trim());
            if (port < 1 || port > 65535) {
                showError("Port invalide", "Le port doit être un nombre entre 1 et 65535.");
                portField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Port invalide", "Le port doit être un nombre valide.");
            portField.requestFocus();
            return false;
        }
        
        if (databaseField.getText().trim().isEmpty()) {
            showError("Champ requis", "Le nom de la base de données est obligatoire.");
            databaseField.requestFocus();
            return false;
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            showError("Champ requis", "Le nom d'utilisateur est obligatoire.");
            usernameField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Ferme la fenêtre
     */
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Affiche un message d'erreur
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Affiche un message d'information
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
