/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.assistante;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.model.ConsultationDetails;
import com.azmicro.moms.model.ModePaiement;
import com.azmicro.moms.model.Paiements;
import com.azmicro.moms.service.ConsultationService;
import com.azmicro.moms.service.PaiementsService;
import com.azmicro.moms.util.DatabaseUtil;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class EditPaiementController implements Initializable {

    ConsultationDetails consultationDetails;
    @FXML
    private TextField montantField;
    @FXML
    private TextField versementField;
    @FXML
    private TextField resteField;
    @FXML
    private DatePicker datePaiementPicker;
    @FXML
    private ComboBox<ModePaiement> modePaiementComboBox;
    @FXML
    private CheckBox etatPaiementCheckBox;

    private PaiementsService paiementsService;

    private ConsultationService consultationService;

    PaiementController paiementController;

    public void setPaiementController(PaiementController paiementController) {
        this.paiementController = paiementController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.paiementsService = new PaiementsService();
        try {
            this.consultationService = new ConsultationService(DatabaseUtil.getConnection());
        } catch (SQLException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Ajouter les valeurs de l'énumération ModePaiement à la ComboBox
        modePaiementComboBox.getItems().addAll(ModePaiement.values());

        // Sélectionner le premier élément par défaut dans le mode de paiement
        modePaiementComboBox.getSelectionModel().selectFirst();

        // Initialiser la date par la date courante
        datePaiementPicker.setValue(LocalDate.now());
        // Sélectionner le premier élément par défaut dans le mode de paiement
        modePaiementComboBox.getSelectionModel().selectFirst();
        // Coche l'état de paiement par défaut
        etatPaiementCheckBox.setSelected(true);
        // Ajouter un listener pour mettre à jour automatiquement le champ "reste" lorsque le montant ou le versement change
        versementField.textProperty().addListener((observable, oldValue, newValue) -> updateResteField());
        montantField.textProperty().addListener((observable, oldValue, newValue) -> updateResteField());
    }

    // Mise à jour automatique du champ "reste" (Montant - Versement)
    private void updateResteField() {
        try {
            double montant = Double.parseDouble(montantField.getText());
            double versement = Double.parseDouble(versementField.getText());
            double reste = montant - versement;
            resteField.setText(String.valueOf(reste));
        } catch (NumberFormatException e) {
            // Gérer les cas où les champs montant ou versement ne sont pas des nombres valides
            resteField.setText("0");
        }
    }

    void setConsultationDetails(ConsultationDetails details) {
        this.consultationDetails = details;
        montantField.setText(String.valueOf(details.getMontant()));
        versementField.setText(String.valueOf(details.getMontantVersement()));
        resteField.setText(String.valueOf(details.getMontantReste()));
        modePaiementComboBox.setValue(details.getModePaiement());
        etatPaiementCheckBox.setSelected(details.getEtatPaiement());
    }

    @FXML
    private void handleSavePaiement(ActionEvent event) {
        // Créer un objet Paiements avec les nouvelles valeurs du formulaire
        Paiements paiement = new Paiements();
        paiement.setMontant(Double.parseDouble(montantField.getText()));
        paiement.setVersment(Double.parseDouble(versementField.getText()));
        paiement.setReste(Double.parseDouble(resteField.getText()));
        paiement.setDatePaiement(datePaiementPicker.getValue());
        paiement.setModePaiement(modePaiementComboBox.getValue());
        paiement.setEtatPayment(etatPaiementCheckBox.isSelected());
        paiement.setConsultation(consultationService.findById(this.consultationDetails.getConsultationID()));
        // Vérifier si le paiement est nouveau ou une mise à jour
        if (this.consultationDetails.getIdPaiement() > 0) {
            paiement.setPaiementID(this.consultationDetails.getIdPaiement());
            paiementsService.updatePaiement(paiement); // Mettre à jour le paiement existant
        } else {
            paiementsService.savePaiement(paiement); // Sauvegarder un nouveau paiement
        }
        if (paiementController != null) {
            paiementController.loadTableData(); // Recharger les données dans PaiementController
        }
        closeWindow();
    }

    @FXML
    private void handleCancelPaiement(ActionEvent event) {
    }

    private void closeWindow() {
        Stage stage = (Stage) montantField.getScene().getWindow();
        stage.close();
    }
}
