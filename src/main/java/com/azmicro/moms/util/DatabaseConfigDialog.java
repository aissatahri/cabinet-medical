/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Classe utilitaire pour ouvrir la fenêtre de configuration de la base de données
 * @author Aissa
 */
public class DatabaseConfigDialog {
    
    /**
     * Ouvre la fenêtre de configuration de la base de données
     */
    public static void show() {
        try {
            FXMLLoader loader = new FXMLLoader(
                DatabaseConfigDialog.class.getResource("/com/azmicro/moms/view/database-config-dialog.fxml")
            );
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Configuration Base de Données");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de la fenêtre de configuration: " + e.getMessage());
        }
    }
}
