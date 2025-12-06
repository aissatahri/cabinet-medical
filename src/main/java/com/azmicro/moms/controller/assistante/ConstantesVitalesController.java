package com.azmicro.moms.controller.assistante;

import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.FilesAttente;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.service.ConsultationService;
import com.azmicro.moms.util.DatabaseUtil;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller pour la saisie des constantes vitales par l'assistante
 */
public class ConstantesVitalesController implements Initializable {

    @FXML
    private Label lblPatientName;
    @FXML
    private DatePicker dpDateConsultation;
    @FXML
    private TextField tfPoids;
    @FXML
    private TextField tfTaille;
    @FXML
    private TextField tfIMC;
    @FXML
    private TextField tfTemperature;
    @FXML
    private TextField tfFrequenceCardiaque;
    @FXML
    private TextField tfPressionArterielleGauche;
    @FXML
    private TextField tfPressionArterielleDroite;
    @FXML
    private TextField tfFrequenceRespiratoire;
    @FXML
    private TextField tfSaO2;
    @FXML
    private TextField tfGlycimie;
    @FXML
    private Button btnEnregistrer;
    @FXML
    private Button btnAnnuler;

    private FilesAttente filesAttente;
    private Patient patient;
    private ConsultationService consultationService;
    private Consultations existingConsultation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.consultationService = new ConsultationService(DatabaseUtil.getConnection());
        } catch (SQLException ex) {
            Logger.getLogger(ConstantesVitalesController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Erreur de connexion", "Impossible de se connecter à la base de données");
        }

        // Set today's date as default
        dpDateConsultation.setValue(LocalDate.now());

        // Add listener to calculate IMC when poids or taille changes
        tfPoids.textProperty().addListener((observable, oldValue, newValue) -> calculateIMC());
        tfTaille.textProperty().addListener((observable, oldValue, newValue) -> calculateIMC());
    }

    /**
     * Set the FilesAttente data for this form
     */
    public void setFilesAttente(FilesAttente filesAttente) {
        this.filesAttente = filesAttente;
        this.patient = filesAttente.getPatient();
        
        // Update UI with patient info
        lblPatientName.setText(patient.getPrenom() + " " + patient.getNom());
        dpDateConsultation.setValue(filesAttente.getDateArrivee());

        // Try to load existing consultation for this patient and date
        loadExistingConsultation();
    }

    /**
     * Load existing consultation if it exists
     */
    private void loadExistingConsultation() {
        try {
            List<Consultations> consultations = consultationService.findAllByIdPatient(patient.getPatientID());
            
            // Find consultation for the selected date
            LocalDate selectedDate = dpDateConsultation.getValue();
            for (Consultations consultation : consultations) {
                if (consultation.getDateConsultation().equals(selectedDate)) {
                    existingConsultation = consultation;
                    break;
                }
            }

            if (existingConsultation != null) {
                // Fill form with existing data
                if (existingConsultation.getPoids() > 0) {
                    tfPoids.setText(String.valueOf(existingConsultation.getPoids()));
                }
                if (existingConsultation.getTaille() > 0) {
                    tfTaille.setText(String.valueOf(existingConsultation.getTaille()));
                }
                if (existingConsultation.getTemperature() > 0) {
                    tfTemperature.setText(String.valueOf(existingConsultation.getTemperature()));
                }
                if (existingConsultation.getFrequencequardiaque() > 0) {
                    tfFrequenceCardiaque.setText(String.valueOf(existingConsultation.getFrequencequardiaque()));
                }
                if (existingConsultation.getPression() != null && !existingConsultation.getPression().isEmpty()) {
                    tfPressionArterielleGauche.setText(existingConsultation.getPression());
                }
                if (existingConsultation.getPressionDroite() != null && !existingConsultation.getPressionDroite().isEmpty()) {
                    tfPressionArterielleDroite.setText(existingConsultation.getPressionDroite());
                }
                if (existingConsultation.getFrequencerespiratoire() > 0) {
                    tfFrequenceRespiratoire.setText(String.valueOf(existingConsultation.getFrequencerespiratoire()));
                }
                if (existingConsultation.getSaO() > 0) {
                    tfSaO2.setText(String.valueOf(existingConsultation.getSaO()));
                }
                if (existingConsultation.getGlycimie() > 0) {
                    tfGlycimie.setText(String.valueOf(existingConsultation.getGlycimie()));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ConstantesVitalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Calculate and display IMC
     */
    private void calculateIMC() {
        try {
            if (!tfPoids.getText().isEmpty() && !tfTaille.getText().isEmpty()) {
                double poids = Double.parseDouble(tfPoids.getText());
                double taille = Double.parseDouble(tfTaille.getText());
                
                if (poids > 0 && taille > 0) {
                    double imc = poids / (taille * taille);
                    tfIMC.setText(String.format("%.2f", imc));
                }
            } else {
                tfIMC.clear();
            }
        } catch (NumberFormatException e) {
            tfIMC.clear();
        }
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        try {
            // Create or update consultation
            if (existingConsultation == null) {
                existingConsultation = new Consultations();
                existingConsultation.setPatient(patient);
                existingConsultation.setDateConsultation(dpDateConsultation.getValue());
            }

            // Update vital signs (all optional)
            if (!tfPoids.getText().isEmpty()) {
                existingConsultation.setPoids(Double.parseDouble(tfPoids.getText()));
            } else {
                existingConsultation.setPoids(0);
            }
            if (!tfTaille.getText().isEmpty()) {
                existingConsultation.setTaille(Double.parseDouble(tfTaille.getText()));
            } else {
                existingConsultation.setTaille(0);
            }
            if (!tfTemperature.getText().isEmpty()) {
                existingConsultation.setTemperature(Double.parseDouble(tfTemperature.getText()));
            } else {
                existingConsultation.setTemperature(0);
            }
            if (!tfFrequenceCardiaque.getText().isEmpty()) {
                existingConsultation.setFrequencequardiaque(Integer.parseInt(tfFrequenceCardiaque.getText()));
            } else {
                existingConsultation.setFrequencequardiaque(0);
            }
            if (!tfPressionArterielleGauche.getText().isEmpty()) {
                existingConsultation.setPression(tfPressionArterielleGauche.getText());
            } else {
                existingConsultation.setPression("");
            }
            if (!tfPressionArterielleDroite.getText().isEmpty()) {
                existingConsultation.setPressionDroite(tfPressionArterielleDroite.getText());
            } else {
                existingConsultation.setPressionDroite("");
            }
            if (!tfFrequenceRespiratoire.getText().isEmpty()) {
                existingConsultation.setFrequencerespiratoire(Integer.parseInt(tfFrequenceRespiratoire.getText()));
            } else {
                existingConsultation.setFrequencerespiratoire(0);
            }
            if (!tfSaO2.getText().isEmpty()) {
                existingConsultation.setSaO(Integer.parseInt(tfSaO2.getText()));
            } else {
                existingConsultation.setSaO(0);
            }
            if (!tfGlycimie.getText().isEmpty()) {
                existingConsultation.setGlycimie(Double.parseDouble(tfGlycimie.getText()));
            } else {
                existingConsultation.setGlycimie(0);
            }

            // Save to database
            if (existingConsultation.getConsultationID() == 0) {
                consultationService.save(existingConsultation);
                showSuccess("Succès", "Constantes vitales enregistrées avec succès");
            } else {
                consultationService.update(existingConsultation);
                showSuccess("Succès", "Constantes vitales mises à jour avec succès");
            }

            // Close window
            closeWindow();

        } catch (NumberFormatException e) {
            showError("Erreur de saisie", "Veuillez vérifier que les valeurs numériques sont correctes");
        } catch (Exception ex) {
            Logger.getLogger(ConstantesVitalesController.class.getName()).log(Level.SEVERE, null, ex);
            showError("Erreur", "Impossible d'enregistrer les constantes vitales: " + ex.getMessage());
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
