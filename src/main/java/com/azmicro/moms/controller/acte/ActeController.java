/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.acte;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.controller.patient.FichePatientDetailsController;
import com.azmicro.moms.dao.FilesAttenteDAOImpl;
import com.azmicro.moms.dao.PatientDAOImpl;
import com.azmicro.moms.model.Acte;
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.FilesAttente;
import com.azmicro.moms.model.ModePaiement;
import com.azmicro.moms.model.Paiements;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Statut;
import com.azmicro.moms.service.ActeService;
import com.azmicro.moms.service.ConsultationActeService;
import com.azmicro.moms.service.FilesAttenteService;
import com.azmicro.moms.service.PaiementsService;
import com.azmicro.moms.util.DatabaseUtil;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class ActeController implements Initializable {

    @FXML
    private VBox container;
    @FXML
    private CheckBox statusCheck;
    @FXML
    private Button btnSave;
    @FXML
    private Label totalLabel;
    private Consultations consultation;
    private Patient patient;

    // Instanciation du service ActeService
    private ActeService acteService;

    private ConsultationActeService consultationActeService;
    private PaiementsService paiementsService;
    private FilesAttenteService filesAttenteService;

    // Map pour suivre les états des actes
    private final Map<CheckBox, Acte> checkBoxToActeMap = new HashMap<>();
    private final Map<CheckBox, ToggleGroup> checkBoxToToggleGroupMap = new HashMap<>();
    private DossierController dossierController;

    // Setter pour injecter le DossierController
    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController;
    }

    /**
     * Initializes the controller class.
     */
    public void setConsultation(Consultations consultation) {
        this.consultation = consultation; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        loadActes();;
    }

    public void setPatient(Patient patient) {
        this.patient = patient; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        loadActes();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.acteService = new ActeService();
        this.consultationActeService = new ConsultationActeService();
        this.paiementsService = new PaiementsService();
        FilesAttenteDAOImpl filesAttenteDAO;
        try {
            filesAttenteDAO = new FilesAttenteDAOImpl(new PatientDAOImpl(DatabaseUtil.getConnection()));
            this.filesAttenteService = new FilesAttenteService(filesAttenteDAO);
        } catch (SQLException ex) {
            Logger.getLogger(FichePatientDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //loadActes();
    }

    // Méthode pour charger les actes depuis la base de données via ActeService
    private void loadActes() {
        List<Acte> actes = acteService.getAllActes();
        List<ConsultationActe> consultationActes = consultationActeService.findByConsultation(consultation);
        generateActeRows(actes, consultationActes);
        updateTotalLabel();
    }

    private void generateActeRows(List<Acte> actes, List<ConsultationActe> consultationActes) {
        container.getChildren().clear();
        checkBoxToActeMap.clear();
        checkBoxToToggleGroupMap.clear();

        for (Acte acte : actes) {
            HBox acteRow = new HBox(10);
            acteRow.getStyleClass().add("acte-row");

            CheckBox acteCheckBox = new CheckBox(acte.getNomActe());
            acteCheckBox.getStyleClass().add("acte-checkbox");
            acteCheckBox.setSelected(false);
            acteCheckBox.setOnAction(event -> handleCheckBoxAction(acteCheckBox));

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            ToggleGroup paymentGroup = new ToggleGroup();

            RadioButton paidRadioButton = new RadioButton("Payé");
            paidRadioButton.setToggleGroup(paymentGroup);
            paidRadioButton.getStyleClass().add("acte-radio-button");
            paidRadioButton.setDisable(true);
            paidRadioButton.setOnAction(event -> handleRadioButtonAction(paidRadioButton));

            RadioButton notPaidRadioButton = new RadioButton("Non payé");
            notPaidRadioButton.setToggleGroup(paymentGroup);
            notPaidRadioButton.getStyleClass().add("acte-radio-button");
            notPaidRadioButton.setDisable(true);
            notPaidRadioButton.setOnAction(event -> handleRadioButtonAction(notPaidRadioButton));

            for (ConsultationActe consultationActe : consultationActes) {
                if (consultationActe.getActe().getIdActe() == acte.getIdActe()) {
                    acteCheckBox.setSelected(true);
                    paidRadioButton.setDisable(false);
                    notPaidRadioButton.setDisable(false);
                    paidRadioButton.setSelected(true); // Default to "Payé"
                    break;
                }
            }

            acteRow.getChildren().addAll(acteCheckBox, spacer, paidRadioButton, notPaidRadioButton);
            container.getChildren().add(acteRow);

            checkBoxToActeMap.put(acteCheckBox, acte);
            checkBoxToToggleGroupMap.put(acteCheckBox, paymentGroup);
        }
    }

    private void updateTotalLabel() {
        double total = 0.0;

        for (Map.Entry<CheckBox, Acte> entry : checkBoxToActeMap.entrySet()) {
            CheckBox checkBox = entry.getKey();
            Acte acte = entry.getValue();

            if (checkBox.isSelected()) {
                ToggleGroup paymentGroup = checkBoxToToggleGroupMap.get(checkBox);
                RadioButton selectedRadioButton = (RadioButton) paymentGroup.getSelectedToggle();

                if (selectedRadioButton != null && selectedRadioButton.getText().equals("Payé")) {
                    total += acte.getPrix();
                }
            }
        }

        totalLabel.setText(String.format("Total: %.2f", total));
    }

    @FXML
    private void save(ActionEvent event) {
        try {
            double total = 0.0;
            List<ConsultationActe> existingConsultationActes = consultationActeService.findByConsultation(consultation);
            List<ConsultationActe> toDelete = new ArrayList<>(existingConsultationActes);

            for (Map.Entry<CheckBox, Acte> entry : checkBoxToActeMap.entrySet()) {
                CheckBox checkBox = entry.getKey();
                Acte acte = entry.getValue();

                if (checkBox.isSelected()) {
                    ToggleGroup paymentGroup = checkBoxToToggleGroupMap.get(checkBox);
                    RadioButton selectedRadioButton = (RadioButton) paymentGroup.getSelectedToggle();
                    ConsultationActe existingActe = null;

                    for (ConsultationActe ca : existingConsultationActes) {
                        if (ca.getActe().equals(acte)) {
                            existingActe = ca;
                            toDelete.remove(ca);
                            break;
                        }
                    }

                    if (existingActe == null) {
                        ConsultationActe consultationActe = new ConsultationActe();
                        consultationActe.setConsultation(consultation);
                        consultationActe.setActe(acte);
                        consultationActeService.save(consultationActe);
                    }

                    if (selectedRadioButton != null && selectedRadioButton.getText().equals("Payé")) {
                        total += acte.getPrix();
                    }
                }
            }

            for (ConsultationActe ca : toDelete) {
                consultationActeService.delete(ca.getIdConsultationActe());
            }

            if (total > 0) {
                Paiements existingPaiement = null;
                List<Paiements> paiementsForConsultation = paiementsService.getPaiementsByConsultation(consultation);
                if (!paiementsForConsultation.isEmpty()) {
                    existingPaiement = paiementsForConsultation.get(0);
                }

                if (existingPaiement == null) {
                    Paiements paiement = new Paiements();
                    paiement.setConsultation(consultation);
                    paiement.setMontant(total);
                    paiement.setDatePaiement(LocalDate.now());
                    paiement.setModePaiement(ModePaiement.ESPECES); // Modifier si nécessaire
                    paiement.setEtatPayment(false);
                    paiementsService.savePaiement(paiement);
                } else {
                    existingPaiement.setMontant(total);
                    existingPaiement.setDatePaiement(LocalDate.now());
                    paiementsService.updatePaiement(existingPaiement);
                }
            }

            updateTotalLabel();

            // Gestion du statut du fichier en attente
            if (statusCheck.isSelected()) {
                // Récupérer les fichiers en attente du patient
                List<FilesAttente> filesAttenteList = filesAttenteService.findAllByIdPatient(patient.getPatientID());
                if (!filesAttenteList.isEmpty()) {
                    // Mettre à jour le statut du dernier fichier
                    FilesAttente lastFileAttente = filesAttenteList.get(filesAttenteList.size() - 1);
                    lastFileAttente.setStatut(Statut.TERMINE);
                    filesAttenteService.update(lastFileAttente);
                }
            }

            // Afficher une alerte de succès
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data saved successfully!");

            if (dossierController != null) {
                dossierController.loadPaiments();
            }

            closeView(event);
        } catch (Exception e) {
            // Afficher une alerte d'erreur
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeView(ActionEvent event) {
        // Get the current window (Stage) and close it
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleCheckBoxAction(CheckBox checkBox) {
        Acte acte = checkBoxToActeMap.get(checkBox);
        ToggleGroup paymentGroup = checkBoxToToggleGroupMap.get(checkBox);

        if (checkBox.isSelected()) {
            // Enable the radio buttons when the checkbox is selected
            paymentGroup.getToggles().forEach(toggle -> ((RadioButton) toggle).setDisable(false));

            // Default to "Non payé" if none is selected
            if (paymentGroup.getSelectedToggle() == null) {
                ((RadioButton) paymentGroup.getToggles().get(1)).setSelected(true); // "Non payé"
            }
        } else {
            // Disable the radio buttons and deselect them when the checkbox is deselected
            paymentGroup.getToggles().forEach(toggle -> {
                ((RadioButton) toggle).setDisable(true);
                ((RadioButton) toggle).setSelected(false);
            });
        }

        updateTotalLabel();
    }

    private void handleRadioButtonAction(RadioButton radioButton) {
        updateTotalLabel();
    }

}
