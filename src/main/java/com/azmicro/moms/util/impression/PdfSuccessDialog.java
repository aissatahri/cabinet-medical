package com.azmicro.moms.util.impression;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Utilitaire pour afficher un message de succès après génération de PDF
 * et ouvrir le fichier quand l'utilisateur clique sur OK
 */
public class PdfSuccessDialog {

    /**
     * Affiche une boîte de dialogue de succès et ouvre le PDF quand l'utilisateur clique sur OK
     * 
     * @param pdfPath Le chemin du fichier PDF généré
     * @param parentWindow La fenêtre parente pour attacher la boîte de dialogue
     * @param title Le titre du document généré (ex: "Prescription", "Facture", etc.)
     */
    public static void showSuccessAndOpenPdf(String pdfPath, Window parentWindow, String title) {
        if (pdfPath == null || pdfPath.isEmpty()) {
            showError(parentWindow, "Le fichier PDF n'a pas pu être généré.");
            return;
        }

        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists()) {
            showError(parentWindow, "Le fichier PDF n'existe pas: " + pdfPath);
            return;
        }

        // Utiliser Platform.runLater pour s'assurer que la boîte de dialogue s'affiche sur le thread JavaFX
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(title + " générée avec succès");
            alert.setContentText("Le document a été enregistré dans:\n" + pdfPath + "\n\nCliquez sur OK pour ouvrir le fichier.");
            
            // Attacher au parent si fourni
            if (parentWindow != null) {
                alert.initOwner(parentWindow);
            }

            Optional<ButtonType> result = alert.showAndWait();
            
            // Ouvrir le PDF quand l'utilisateur clique sur OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                openPdfFile(pdfFile, parentWindow);
            }
        });
    }

    /**
     * Ouvre le fichier PDF avec l'application par défaut du système
     * 
     * @param pdfFile Le fichier PDF à ouvrir
     * @param parentWindow La fenêtre parente pour afficher les erreurs
     */
    private static void openPdfFile(File pdfFile, Window parentWindow) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(pdfFile);
                } else {
                    showError(parentWindow, "L'ouverture automatique de fichiers n'est pas supportée sur ce système.");
                }
            } else {
                showError(parentWindow, "Desktop n'est pas supporté sur ce système.");
            }
        } catch (IOException e) {
            showError(parentWindow, "Erreur lors de l'ouverture du fichier PDF:\n" + e.getMessage());
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur
     * 
     * @param parentWindow La fenêtre parente
     * @param message Le message d'erreur
     */
    private static void showError(Window parentWindow, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ouverture du PDF");
            alert.setContentText(message);
            
            if (parentWindow != null) {
                alert.initOwner(parentWindow);
            }
            
            alert.showAndWait();
        });
    }
}
