package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.model.CompteRenduETT;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.util.impression.ImpressionUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class CompteRenduETTDialogController {
    
    @FXML
    private Label lblPatientInfo;
    
    @FXML
    private Label lblDate;
    
    @FXML
    private TextArea txtCompteRendu;
    
    @FXML
    private TextArea txtConclusion;
    
    @FXML
    private Label lblCharCount;
    
    private Stage dialogStage;
    private Patient patient;
    private Consultations consultation;
    private Medecin medecin;
    
    @FXML
    private void initialize() {
        // Ajouter un listener pour compter les caractères
        txtCompteRendu.textProperty().addListener((observable, oldValue, newValue) -> {
            lblCharCount.setText(newValue.length() + " caractères");
        });
        // Optionnel: on peut ajouter un listener pour la conclusion si besoin
        if (txtConclusion != null) {
            txtConclusion.textProperty().addListener((o, oldV, newV) -> {
                // Pas d'affichage séparé pour l'instant; conserve place pour évolution
            });
        }
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setData(Patient patient, Consultations consultation, Medecin medecin) {
        this.patient = patient;
        this.consultation = consultation;
        this.medecin = medecin;
        
        // Afficher les informations du patient
        if (patient != null) {
            int age = Period.between(patient.getDateNaissance(), LocalDate.now()).getYears();
            String info = String.format("%s %s - Dossier: %s - Age: %d ans",
                    patient.getNom(),
                    patient.getPrenom(),
                    patient.getNumDossier() != null ? patient.getNumDossier() : "N/A",
                    age);
            lblPatientInfo.setText(info);
        }
        
        // Afficher la date actuelle
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblDate.setText("Date : " + LocalDate.now().format(formatter));
    }
    
    @FXML
    private void handleAnnuler() {
        dialogStage.close();
    }
    
    @FXML
    private void handleImprimer() {
        String contenu = txtCompteRendu.getText();
        String conclusion = txtConclusion != null ? txtConclusion.getText() : null;
        
        if (contenu == null || contenu.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Compte rendu vide");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir le contenu du compte rendu.");
            alert.showAndWait();
            return;
        }
        
        try {
            // Créer l'objet CompteRenduETT
            CompteRenduETT compteRendu = new CompteRenduETT(consultation, patient, contenu, conclusion);
            
            // Appeler la méthode d'impression
            ImpressionUtil.imprimerCompteRenduETT(compteRendu, medecin, dialogStage.getOwner());
            
            // Fermer le dialogue après l'impression
            dialogStage.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la génération du PDF : " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur inattendue : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
