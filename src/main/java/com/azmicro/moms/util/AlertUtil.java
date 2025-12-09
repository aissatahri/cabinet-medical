package com.azmicro.moms.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Classe utilitaire pour configurer les boîtes de dialogue Alert avec l'icône de l'application
 */
public class AlertUtil {
    
    private static Image appIcon;
    
    /**
     * Configure une Alert avec l'icône de l'application
     * @param alert L'Alert à configurer
     */
    public static void setAppIcon(Alert alert) {
        if (alert != null && alert.getDialogPane() != null && alert.getDialogPane().getScene() != null) {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            if (stage != null) {
                try {
                    if (appIcon == null) {
                        // Charger l'icône une seule fois
                        appIcon = new Image(AlertUtil.class.getResourceAsStream("/com/azmicro/moms/images/logo.png"));
                    }
                    stage.getIcons().add(appIcon);
                } catch (Exception e) {
                    // Si l'icône ne peut pas être chargée, continuer sans icône
                    System.err.println("Impossible de charger l'icône de l'application : " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Crée et configure une nouvelle Alert avec l'icône de l'application
     * @param alertType Le type d'Alert
     * @param title Le titre de l'Alert
     * @param header Le header de l'Alert (peut être null)
     * @param content Le contenu de l'Alert
     * @return L'Alert configurée
     */
    public static Alert createAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        // Attendre que la fenêtre soit initialisée avant d'ajouter l'icône
        alert.getDialogPane().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setAppIcon(alert);
            }
        });
        
        return alert;
    }
}
