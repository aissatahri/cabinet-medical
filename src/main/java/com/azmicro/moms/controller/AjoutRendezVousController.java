/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.Jours;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.RendezVous;
import com.azmicro.moms.service.DisponibilitesService;
import com.azmicro.moms.service.RendezVousService;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class AjoutRendezVousController implements Initializable {

    private Medecin medecin;
    private Patient patient;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextField tfTitre;
    @FXML
    private TextArea taDesc;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField tfHourStart;
    @FXML
    private TextField tfMinuteStart;
    @FXML
    private TextField tfHourEnd;
    @FXML
    private TextField tfMinuteEnd;

    DisponibilitesService disponibilitesService;
    RendezVousService rendezVousService;
    
    private RendezVous rendezVous;
    private DossierController dossierController;

    /**
     * Initializes the controller class.
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
        System.out.println(this.patient);
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
        System.out.println(this.medecin);
        // Déclencher manuellement la mise à jour pour la date initiale
        if (dpDate.getValue() != null) {
            updateFieldsForSelectedDate(dpDate.getValue());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.disponibilitesService = new DisponibilitesService();
        this.rendezVousService = new RendezVousService();
        // Obtenez la date actuelle
        LocalDate currentDate = LocalDate.now();
        // Ajoutez une semaine à la date actuelle
        LocalDate datePlusOneWeek = currentDate.plusWeeks(1);
        // Initialisez le DatePicker avec la nouvelle date
        dpDate.setValue(datePlusOneWeek);
    }

    private void updateFieldsForSelectedDate(LocalDate selectedDate) {
        System.out.println("Medecin updateFieldsForSelectedDate " + this.medecin.toString());
        // Convertir la date en jour de la semaine
        Jours selectedJour = Jours.values()[selectedDate.getDayOfWeek().getValue() - 1];
        System.out.println("Selected day: " + selectedJour);
        // Récupérer les disponibilités pour le médecin
        List<Disponibilites> disponibilitesList = disponibilitesService.findAllByMedecin(this.medecin);
        System.out.println("Disponibilités trouvées : " + disponibilitesList.size());
        // Rechercher la disponibilité correspondant au jour sélectionné
        Disponibilites selectedDisponibilite = disponibilitesList.stream()
                .filter(d -> d.getJours() == selectedJour)
                .findFirst()
                .orElse(null);

        if (selectedDisponibilite != null) {
            LocalTime startTime = selectedDisponibilite.getHeureDebut();
            LocalTime endTime = selectedDisponibilite.getHeureFin();
            System.out.println("Disponibilité trouvée : " + startTime + " - " + endTime);
            tfHourStart.setText(String.format("%02d", startTime.getHour()));
            tfMinuteStart.setText(String.format("%02d", startTime.getMinute()));
            tfHourEnd.setText(String.format("%02d", endTime.getHour()));
            tfMinuteEnd.setText(String.format("%02d", endTime.getMinute()));
        } else {
            // Effacer les champs si aucune disponibilité n'est trouvée pour la date sélectionnée
            clearTimeFields();
        }
    }

    private void clearTimeFields() {
        tfHourStart.clear();
        tfMinuteStart.clear();
        tfHourEnd.clear();
        tfMinuteEnd.clear();
    }

    @FXML
    private void saveRendezVous(ActionEvent event) {
        LocalDate dateRendezVous = dpDate.getValue();
        String titre = tfTitre.getText();
        String description = taDesc.getText();
        String heureDebut = tfHourStart.getText();
        String minuteDebut = tfMinuteStart.getText();
        String heureFin = tfHourEnd.getText();
        String minuteFin = tfMinuteEnd.getText();

        if (dateRendezVous == null || titre.isEmpty() || heureDebut.isEmpty() || minuteDebut.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        LocalTime timeDebut = LocalTime.of(Integer.parseInt(heureDebut), Integer.parseInt(minuteDebut));
        LocalTime timeFin = LocalTime.of(Integer.parseInt(heureFin), Integer.parseInt(minuteFin));

        RendezVous rendezVous = this.rendezVous != null ? this.rendezVous : new RendezVous();
        rendezVous.setDate(dateRendezVous);
        rendezVous.setTitre(titre);
        rendezVous.setDesc(description);
        rendezVous.setHourStart(timeDebut);
        rendezVous.setHourEnd(timeFin);
        rendezVous.setMedecin(medecin);
        rendezVous.setPatient(patient);

        try {
            boolean success;
            if (this.rendezVous == null) {
                success = rendezVousService.saveRendezVous(rendezVous);
            } else {
                success = rendezVousService.updateRendezVous(rendezVous);
            }
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Rendez-vous enregistré avec succès.");
                dossierController.loadRendezVousData();
                clearForm();
                this.rendezVous = null; // Réinitialiser l'objet rendezVous après l'enregistrement
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'enregistrement du rendez-vous.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'enregistrement du rendez-vous.");
        }
    }
    @FXML
    private void cancel(ActionEvent event) {
    }

    private void clearForm() {
        dpDate.setValue(null);
        tfTitre.clear();
        taDesc.clear();
        tfHourStart.clear();
        tfMinuteStart.clear();
        tfHourEnd.clear();
        tfMinuteEnd.clear();
    }

// Méthode utilitaire pour afficher une alerte
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Méthode pour passer le rendez-vous au contrôleur
    public void setRendezVous(RendezVous rendezVous) {
        this.rendezVous = rendezVous;
        if (rendezVous != null) {
            dpDate.setValue(rendezVous.getDate());
            tfTitre.setText(rendezVous.getTitre());
            taDesc.setText(rendezVous.getDesc());

            // Extraire les heures et minutes et les afficher dans les champs
            LocalTime startTime = rendezVous.getHourStart();
            tfHourStart.setText(String.format("%02d", startTime.getHour()));
            tfMinuteStart.setText(String.format("%02d", startTime.getMinute()));

            LocalTime endTime = rendezVous.getHourEnd();
            tfHourEnd.setText(String.format("%02d", endTime.getHour()));
            tfMinuteEnd.setText(String.format("%02d", endTime.getMinute()));
        }
    }

}
