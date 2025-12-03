/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.patient;

import com.azmicro.moms.dao.FilesAttenteDAOImpl;
import com.azmicro.moms.dao.HistoriqueMedicalDAO;
import com.azmicro.moms.dao.HistoriqueMedicalDAOImpl;
import com.azmicro.moms.dao.PatientDAOImpl;
import com.azmicro.moms.model.EtatArrive;
import com.azmicro.moms.model.FilesAttente;
import com.azmicro.moms.model.HistoriqueMedical;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Sexe;
import com.azmicro.moms.model.SituationFamiliale;
import com.azmicro.moms.model.Statut;
import com.azmicro.moms.model.Type;
import com.azmicro.moms.service.FilesAttenteService;
import com.azmicro.moms.service.HistoriqueMedicalService;
import com.azmicro.moms.util.DatabaseUtil;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class FichePatientDetailsController implements Initializable {

    @FXML
    private TextField tfDossier;
    @FXML
    private TextField tfNomPatient;
    @FXML
    private TextField tfPrenomPatient;
    @FXML
    private DatePicker dpDateNaissancePatient;
    @FXML
    private TextField tfAgePatient;
    @FXML
    private RadioButton rbMale;
    @FXML
    private ToggleGroup tgSexe;
    @FXML
    private RadioButton rbFemelle;
    @FXML
    private ComboBox<SituationFamiliale> cbxSituationFamilial;
    @FXML
    private TextField tfProfession;
    @FXML
    private TextField tfTelephone1;
    @FXML
    private Button btnSavePatient;
    @FXML
    private TableView<FilesAttente> tvFileAttente;
    @FXML
    private TableColumn<FilesAttente, LocalDate> clmDateArrivee;
    @FXML
    private TableColumn<FilesAttente, LocalTime> clmHeureArrivee;
    @FXML
    private TableColumn<FilesAttente, EtatArrive> clmEtat;
    @FXML
    private TableColumn<FilesAttente, Statut> clmStatus;
    @FXML
    private DatePicker dateArrive;
    @FXML
    private Label lblHeureArrive;
    @FXML
    private ComboBox<EtatArrive> cbxEtatArrive;
    @FXML
    private ComboBox<Statut> cbxStatus;
    @FXML
    private Button btnAjouterFileAttente;
    @FXML
    private Button btnModifierFileAttente;
    @FXML
    private Button btnSupprimerFileAttente;
    @FXML
    private DatePicker dateAntecedent;
    @FXML
    private ComboBox<Type> cbxTypeAntecedent;
    @FXML
    private TextArea txtDescriptionAntecedent;
    @FXML
    private Button btnAjouterAntecedent;
    @FXML
    private Button btnModifierAntecedent;
    @FXML
    private Button btnSupprimerAntecedent;
    @FXML
    private TableView<HistoriqueMedical> tvAntecedent;
    @FXML
    private TableColumn<HistoriqueMedical, LocalDate> clmDateAntecedent;
    @FXML
    private TableColumn<HistoriqueMedical, String> clmTypeAntecedent;
    @FXML
    private TableColumn<HistoriqueMedical, String> clmDescriptionAntecedent;

    private Patient patient;
    FilesAttenteService filesAttenteService;
    HistoriqueMedicalService historiqueMedicalService;

    public FichePatientDetailsController() {
        // No-argument constructor is required by FXMLLoader
    }

    public FichePatientDetailsController(Patient patient) {
        this.patient = patient;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Création de l'instance de FilesAttenteDAO
        FilesAttenteDAOImpl filesAttenteDAO;
        try {
            filesAttenteDAO = new FilesAttenteDAOImpl(new PatientDAOImpl(DatabaseUtil.getConnection()));
            this.filesAttenteService = new FilesAttenteService(filesAttenteDAO);
        } catch (SQLException ex) {
            Logger.getLogger(FichePatientDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HistoriqueMedicalDAO historiqueMedicalDAO;
        try {
            historiqueMedicalDAO = new HistoriqueMedicalDAOImpl(new PatientDAOImpl(DatabaseUtil.getConnection()));
            this.historiqueMedicalService = new HistoriqueMedicalService(historiqueMedicalDAO);
        } catch (SQLException ex) {
            Logger.getLogger(FichePatientDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        cbxSituationFamilial.getItems().setAll(SituationFamiliale.values());
        // Set the StringConverter to display only the description
        cbxSituationFamilial.setItems(FXCollections.observableArrayList(SituationFamiliale.values()));
        cbxSituationFamilial.setConverter(new StringConverter<SituationFamiliale>() {
            @Override
            public String toString(SituationFamiliale situationFamiliale) {
                return situationFamiliale.getDescription();
            }

            @Override
            public SituationFamiliale fromString(String string) {
                for (SituationFamiliale situation : SituationFamiliale.values()) {
                    if (situation.getDescription().equals(string)) {
                        return situation;
                    }
                }
                return null; // or throw an exception
            }
        });
        cbxSituationFamilial.getSelectionModel().selectFirst();
        rbMale.setToggleGroup(tgSexe);
        rbFemelle.setToggleGroup(tgSexe);
        dpDateNaissancePatient.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Calculez l'âge à partir de la date de naissance
                int age = calculateAge(newValue);
                // Mettez à jour le champ de texte de l'âge
                tfAgePatient.setText(Integer.toString(age));
            }
        });
        // Initialize EtatArrive ComboBox
        cbxEtatArrive.getItems().setAll(EtatArrive.values());
        // Initialize EtatArrive ComboBox
        cbxEtatArrive.getItems().setAll(EtatArrive.values());
        if (!cbxEtatArrive.getItems().isEmpty()) {
            cbxEtatArrive.getSelectionModel().selectFirst(); // Select the first item
        }

        // Initialize Statut ComboBox
        cbxStatus.getItems().setAll(Statut.values());
        if (!cbxStatus.getItems().isEmpty()) {
            cbxStatus.getSelectionModel().selectFirst(); // Select the first item
        }
        dateArrive.setValue(LocalDate.now());
        lblHeureArrive.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        clmDateArrivee.setCellValueFactory(cellData
                -> new SimpleObjectProperty<>(cellData.getValue().getDateArrivee())
        );

        clmHeureArrivee.setCellValueFactory(cellData
                -> new SimpleObjectProperty<>(cellData.getValue().getHeureArrive())
        );

        clmEtat.setCellValueFactory(cellData
                -> new SimpleObjectProperty<>(cellData.getValue().getEtat())
        );

        clmStatus.setCellValueFactory(cellData
                -> new SimpleObjectProperty<>(cellData.getValue().getStatut())
        );

        // Add listener to the TableView selection model
        tvFileAttente.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFormFields(newValue);
            }
        });

        cbxTypeAntecedent.getItems().setAll(Type.values());
        if (!cbxTypeAntecedent.getItems().isEmpty()) {
            cbxTypeAntecedent.getSelectionModel().selectFirst(); // Select the first item
        }

        // Initialize Date Column
        clmDateAntecedent.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Initialize Type Column
        clmTypeAntecedent.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Initialize Description Column
        clmDescriptionAntecedent.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Ajoutez un écouteur pour la sélection de ligne
        tvAntecedent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateForm(newValue);
            }
        });
        // Load initial data into TableView

    }

    private int calculateAge(LocalDate dateNaissance) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateNaissance, currentDate).getYears();
    }

    @FXML
    private void savePatient(ActionEvent event) {
        if (success) {
            // Créer une alerte de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Le patient est en attente. Voulez-vous ajouter ce patient à la liste ou quitter sans ajouter ?");

            // Ajouter les boutons "Ajouter" et "Quitter"
            ButtonType addButton = new ButtonType("Ajouter");
            ButtonType cancelButton = new ButtonType("Quitter");
            confirmationAlert.getButtonTypes().setAll(addButton, cancelButton);

            // Afficher l'alerte et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == addButton) {
                // Si l'utilisateur choisit "Ajouter", ajouter le patient à la liste
                //addPatientToList(); // Implémentez cette méthode pour ajouter le patient

                // Afficher une alerte d'information
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Patient Ajouté");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Le patient a été ajouté à la liste avec succès.");
                infoAlert.showAndWait();

                // Fermer la scène courante
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                // Si l'utilisateur choisit "Quitter", fermer simplement la scène
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        }
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        tfNomPatient.setText(this.patient.getNom());
        tfDossier.setText(patient.getNumDossier());
        tfPrenomPatient.setText(patient.getPrenom());
        tfAgePatient.setText(patient.getAge() + "");
        dpDateNaissancePatient.setValue(patient.getDateNaissance());
        tfTelephone1.setText(this.patient.getTelephone());
        cbxSituationFamilial.setValue(patient.getSituationFamiliale());
        if (patient.getSexe() == Sexe.M) {
            rbMale.setSelected(true);
        } else {
            rbFemelle.setSelected(true);
        }
        refreshTableViewAntecedent();
        // Load data into TableView
        refreshTableViewFileAttente();
    }

    private boolean success = false;

    @FXML
    private void ajouterFileAttente(ActionEvent event) {
        // Collecter les données du formulaire
        LocalDate dateArrivee = dateArrive.getValue(); // Seulement la partie date
        LocalTime heureArrivee = LocalTime.now(); // Seulement l'heure actuelle en format HH:mm:ss

        EtatArrive etatArrive = cbxEtatArrive.getValue();
        Statut statut = cbxStatus.getValue();

        // Créer un nouvel objet FilesAttente
        FilesAttente newFilesAttente = new FilesAttente();
        newFilesAttente.setDateArrivee(dateArrivee);
        newFilesAttente.setHeureArrive(heureArrivee);
        newFilesAttente.setEtat(etatArrive);
        newFilesAttente.setStatut(statut);
        newFilesAttente.setPatient(this.patient);

        // Sauvegarder le filesAttente en utilisant FilesAttenteService
        success = filesAttenteService.save(newFilesAttente);

        if (success) {
            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "File d'attente ajoutée avec succès !");

            // Optionnel : rafraîchir la TableView ou d'autres éléments
            refreshTableViewFileAttente();

            // Appeler savePatient si l'ajout a réussi
            //savePatient(event);
        } else {
            // Afficher un message d'erreur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de la file d'attente. Veuillez réessayer.");
        }

        System.out.println("File Attente " + newFilesAttente.toString());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void modifierFileAttente(ActionEvent event) {
        FilesAttente selectedFilesAttente = tvFileAttente.getSelectionModel().getSelectedItem();
        if (selectedFilesAttente == null) {
            showErrorMessage("Aucun élément sélectionné pour la modification.");
            return;
        }

        // Collect data from the form
        LocalDate dateArrivee = dateArrive.getValue(); // Only the date part
        LocalTime heureArrivee = LocalTime.now(); // Only the current time in HH:mm:ss format

        EtatArrive etatArrive = cbxEtatArrive.getValue();
        Statut statut = cbxStatus.getValue();

        // Update the selected FilesAttente object
        selectedFilesAttente.setDateArrivee(dateArrivee);
        selectedFilesAttente.setHeureArrive(heureArrivee);
        selectedFilesAttente.setEtat(etatArrive);
        selectedFilesAttente.setStatut(statut);
        selectedFilesAttente.setPatient(this.patient);

        // Update the filesAttente using FilesAttenteService
        filesAttenteService.update(selectedFilesAttente);
        System.out.println("File Attente " + selectedFilesAttente.toString());

        // Optionally, refresh the TableView or show a success message
        refreshTableViewFileAttente();
        showSuccessMessage("File d'attente modifiée avec succès !");
    }

    @FXML
    private void supprimerFileAttente(ActionEvent event) {
        FilesAttente selectedFilesAttente = tvFileAttente.getSelectionModel().getSelectedItem();
        if (selectedFilesAttente == null) {
            showErrorMessage("Aucun élément sélectionné pour la suppression.");
            return;
        }
        // Delete the selected FilesAttente using FilesAttenteService
        filesAttenteService.delete(selectedFilesAttente.getFileAttenteID());
        // Optionally, refresh the TableView or show a success message
        refreshTableViewFileAttente();
        showSuccessMessage("File d'attente supprimée avec succès !");
    }

    private void refreshTableViewFileAttente() {
        tvFileAttente.setItems(FXCollections.observableArrayList(filesAttenteService.findAllByIdPatient(this.patient.getPatientID())));
    }

    private void showSuccessMessage(String message) {
        // Implement this method to show a success message to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    private void showErrorMessage(String message) {
        // Implement this method to show an error message to the user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void updateFormFields(FilesAttente filesAttente) {
        // Update the form fields based on the selected FilesAttente object
        dateArrive.setValue(filesAttente.getDateArrivee()); // Set LocalDate directly
        lblHeureArrive.setText(filesAttente.getHeureArrive().toString()); // Set LocalTime directly

        // Set selected values in the ComboBoxes
        cbxEtatArrive.setValue(filesAttente.getEtat());
        cbxStatus.setValue(filesAttente.getStatut());
    }

    @FXML
    private void btnAjouterAntecedent(ActionEvent event) {
        LocalDate date = dateAntecedent.getValue();
        Type type = cbxTypeAntecedent.getValue();
        String description = txtDescriptionAntecedent.getText();
        if (date == null || type == null || description.isEmpty()) {
            showErrorMessage("Tous les champs doivent être remplis.");
            return;
        }
        HistoriqueMedical newAntecedent = new HistoriqueMedical();
        newAntecedent.setDate(date);
        newAntecedent.setType(type);
        newAntecedent.setDescription(description);
        newAntecedent.setPatient(this.patient);
        // Assuming you have a service to handle CRUD operations
        historiqueMedicalService.saveHistoriqueMedical(newAntecedent);
        System.out.println("Antecedent " + newAntecedent.toString());
        // Clear the form and refresh the TableView
        clearForm();
        refreshTableViewAntecedent();
        showSuccessMessage("Antécédent ajouté avec succès !");
    }

    @FXML
    private void modifierAntecedent(ActionEvent event) {
        HistoriqueMedical selectedAntecedent = tvAntecedent.getSelectionModel().getSelectedItem();
        if (selectedAntecedent == null) {
            showErrorMessage("Aucun antécédent sélectionné pour la modification.");
            return;
        }
        LocalDate date = dateAntecedent.getValue();
        Type type = cbxTypeAntecedent.getValue();
        String description = txtDescriptionAntecedent.getText();
        if (date == null || type == null || description.isEmpty()) {
            showErrorMessage("Tous les champs doivent être remplis.");
            return;
        }
        selectedAntecedent.setDate(date);
        selectedAntecedent.setType(type);
        selectedAntecedent.setDescription(description);
        selectedAntecedent.setPatient(patient);
        // Update the record
        historiqueMedicalService.updateHistoriqueMedical(selectedAntecedent);
        // Clear the form and refresh the TableView
        clearForm();
        refreshTableViewAntecedent();
        showSuccessMessage("Antécédent modifié avec succès !");
    }

    @FXML
    private void supprimerAntecedent(ActionEvent event) {
        HistoriqueMedical selectedAntecedent = tvAntecedent.getSelectionModel().getSelectedItem();
        if (selectedAntecedent == null) {
            showErrorMessage("Aucun antécédent sélectionné pour la suppression.");
            return;
        }
        // Delete the selected record
        historiqueMedicalService.deleteHistoriqueMedical(selectedAntecedent);
        // Clear the form and refresh the TableView
        clearForm();
        refreshTableViewAntecedent();
        showSuccessMessage("Antécédent supprimé avec succès !");
    }

    private void refreshTableViewAntecedent() {
        // Fetch the latest data and set it to the TableView
        List<HistoriqueMedical> antecedents = historiqueMedicalService.findAllHistoriquesMedicalByIdPatient(this.patient.getPatientID());
        tvAntecedent.setItems(FXCollections.observableArrayList(antecedents));
        //tvAntecedent.setItems(FXCollections.observableArrayList(historiqueMedicalService.findAllHistoriquesMedical()));
    }

    private void clearForm() {
        dateAntecedent.setValue(null);
        cbxTypeAntecedent.setValue(null);
        txtDescriptionAntecedent.clear();
    }

    private void populateForm(HistoriqueMedical historiqueMedical) {
        if (historiqueMedical != null) {
            dateAntecedent.setValue(historiqueMedical.getDate());
            cbxTypeAntecedent.setValue(historiqueMedical.getType());
            txtDescriptionAntecedent.setText(historiqueMedical.getDescription());
            //txtNoteAntecedent.setText(historiqueMedical.getNote());
        }
    }

}
