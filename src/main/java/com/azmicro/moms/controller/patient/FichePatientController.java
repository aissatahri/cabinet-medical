/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.patient;

import com.azmicro.moms.controller.assistante.DashboardAssistanteController;
import com.azmicro.moms.controller.medecin.PatientController;
import javafx.util.StringConverter;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Sexe;
import com.azmicro.moms.model.SituationFamiliale;
import com.azmicro.moms.service.HistoriqueMedicalService;
import com.azmicro.moms.service.PatientService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class FichePatientController implements Initializable {

    @FXML
    private Button btnClose;
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
    private TextField tfCouvertureSanitaire;
    @FXML
    private TextField tfTelephone1;
    @FXML
    private TextArea taAdresse;
    @FXML
    private TextArea taNote;

    @FXML
    private Button btnSavePatient;
    private boolean isEditMode;
    /**
     * Initializes the controller class.
     */
    private PatientService patientService;
    private Stage primaryStage;

    private Patient patient;

    private PatientController patientController; // Add this field

// Method to set PatientController instance
    public void setPatientController(PatientController patientController) {
        this.patientController = patientController;
    }

    private DashboardAssistanteController dashboardController; // Add this field

// Method to set PatientController instance
    public void setDashboardController(DashboardAssistanteController dashboardController) {
        this.dashboardController = dashboardController;
    }
    
    public void setMainController(PatientController patientController) {
        this.patientController = patientController; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    public FichePatientController() {
    }

    public FichePatientController(Patient patient) {
        this.patient = patient;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        if (isEditMode) {
            // Configurer le formulaire pour le mode édition
            loadPatientData();
            System.out.println("Mode edit " + isEditMode);
        } else {
            // Configurer le formulaire pour le mode nouveau
            clearForm();
            System.out.println("Mode New " + isEditMode);
        }
    }

    private void loadPatientData() {
        // Charge les données du patient dans le formulaire
        System.out.println("L age de patient" + this.patient.getAge() + " id " + this.patient.getPatientID());
        tfNomPatient.setText(patient.getNom());
        tfPrenomPatient.setText(patient.getPrenom());
        tfNomPatient.setText(this.patient.getNom());
        tfDossier.setText(patient.getNumDossier());
        tfPrenomPatient.setText(patient.getPrenom());
        tfAgePatient.setText(patient.getAge() + "");
        dpDateNaissancePatient.setValue(patient.getDateNaissance());
        tfTelephone1.setText(this.patient.getTelephone());
        cbxSituationFamilial.setValue(patient.getSituationFamiliale());
        tfProfession.setText(patient.getProfession());
        tfCouvertureSanitaire.setText(patient.getCouvertureSanitaire());
        if (patient.getSexe() == Sexe.M) {
            rbMale.setSelected(true);
        } else {
            rbFemelle.setSelected(true);
        }
    }

    private void clearForm() {
        // Efface les champs du formulaire
        tfNomPatient.clear();
        tfPrenomPatient.clear();
        tfDossier.clear();
        tfAgePatient.clear();
        tfTelephone1.clear();
        tfProfession.clear();
        tfCouvertureSanitaire.clear();
        // Ajoutez un ChangeListener au champ de texte tfNom
        tfNomPatient.textProperty().addListener((observable, oldValue, newValue) -> generateNumeroDossier(tfNomPatient.getText(), tfPrenomPatient.getText()));
        // Ajoutez un ChangeListener au champ de texte tfPrenom
        tfPrenomPatient.textProperty().addListener((observable, oldValue, newValue) -> generateNumeroDossier(tfNomPatient.getText(), tfPrenomPatient.getText()));

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
        tfProfession.setText(patient.getProfession());
        tfCouvertureSanitaire.setText(patient.getCouvertureSanitaire());
        if (patient.getSexe() == Sexe.M) {
            rbMale.setSelected(true);
        } else {
            rbFemelle.setSelected(true);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            this.patientService = new PatientService();
        } catch (SQLException ex) {
            Logger.getLogger(FichePatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.historiqueMedicalService = new HistoriqueMedicalService();
        // Populate ComboBox with enum values
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
    }

    @FXML
    private void savePatient(ActionEvent event) throws IOException, SQLException {
        //saveNewPatient();
        if (this.patient == null) {
            System.out.println("patient null ---------------------------------- save patient");
            saveNewPatient();
        } else {
            System.out.println("patient not null ---------------------------------- edit patient " + this.patient.getNom());
            editPatient();
        }
    }

    public void saveNewPatient() throws SQLException {
        // Validation des champs obligatoires
        if (validateFields()) {
            // Création du nouvel objet Patient
            Patient newPatient = new Patient();
            newPatient.setNumDossier(tfDossier.getText().trim());
            newPatient.setNom(tfNomPatient.getText().trim());
            newPatient.setPrenom(tfPrenomPatient.getText().trim());
            newPatient.setDateNaissance(dpDateNaissancePatient.getValue());
            newPatient.setAge(Integer.parseInt(tfAgePatient.getText()));
            newPatient.setSexe(rbMale.isSelected() ? Sexe.M : Sexe.F);
            newPatient.setProfession(tfProfession.getText().trim());
            newPatient.setTelephone(tfTelephone1.getText().trim());
            newPatient.setEmail(""); // Gérer l'email s'il est requis
            newPatient.setAdresse(taAdresse.getText().trim());
            newPatient.setSituationFamiliale(cbxSituationFamilial.getValue());
            newPatient.setCouvertureSanitaire(tfCouvertureSanitaire.getText().trim());
            // Définir d'autres attributs si nécessaire

            // Appeler le service pour sauvegarder le patient
            boolean success = patientService.save(newPatient);
            if (success) {
                System.out.println("Patient " + newPatient.toString());
                 if (dashboardController != null) {
                    dashboardController.reloadScene();
                }else if (patientController != null) {
                    patientController.updatePatientsList();
                }else {
                    System.out.println(" le controlleur sont null");
                } // Gérer l'exception correctement dans votre application
                Stage stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
                showAlert(AlertType.INFORMATION, "Patient sauvegardé", "Le patient a été sauvegardé avec succès.");

            } else {
                showAlert(AlertType.ERROR, "Échec de la sauvegarde", "La sauvegarde du patient a échoué.");
            }
        }
    }

    private void editPatient() throws SQLException {
        if (validateFields()) {
            // Mise à jour des attributs de l'objet Patient existant
            patient.setNumDossier(tfDossier.getText().trim());
            patient.setNom(tfNomPatient.getText().trim());
            patient.setPrenom(tfPrenomPatient.getText().trim());
            patient.setDateNaissance(dpDateNaissancePatient.getValue());
            patient.setAge(Integer.parseInt(tfAgePatient.getText()));
            patient.setSexe(rbMale.isSelected() ? Sexe.M : Sexe.F);
            patient.setSituationFamiliale(cbxSituationFamilial.getValue());
            patient.setProfession(tfProfession.getText().trim());
            patient.setTelephone(tfTelephone1.getText().trim());
            patient.setEmail(""); // Gérer l'email s'il est requis
            patient.setAdresse(taAdresse.getText().trim());
            patient.setCouvertureSanitaire(tfCouvertureSanitaire.getText().trim());

            // Appeler le service pour mettre à jour le patient
            boolean success = patientService.update(patient);
            if (success) {
                System.out.println("Patient " + patient.toString());
                if (dashboardController != null) {
                    dashboardController.reloadScene();
                }else if (patientController != null) {
                    patientController.updatePatientsList();
                }else {
                    System.out.println(" le controlleur sont null");
                }
                Stage stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
                showAlert(AlertType.INFORMATION, "Patient mis à jour", "Le patient a été mis à jour avec succès.");
            } else {
                showAlert(AlertType.ERROR, "Échec de la mise à jour", "La mise à jour du patient a échoué.");
            }
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateFields() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        if (tfNomPatient.getText().trim().isEmpty()) {
            errorMessage.append("Nom est obligatoire.\n");
            isValid = false;
        }
        if (tfPrenomPatient.getText().trim().isEmpty()) {
            errorMessage.append("Prénom est obligatoire.\n");
            isValid = false;
        }
        if (tfDossier.getText().trim().isEmpty()) {
            errorMessage.append("Numéro de dossier est obligatoire.\n");
            isValid = false;
        }
        if (dpDateNaissancePatient.getValue() == null) {
            errorMessage.append("Date de naissance est obligatoire.\n");
            isValid = false;
        }
        if (tfTelephone1.getText().trim().isEmpty()) {
            errorMessage.append("Téléphone est obligatoire.\n");
            isValid = false;
        }

        if (!isValid) {
            showAlert("Erreur de validation", errorMessage.toString());
        }

        return isValid;
    }

// Afficher une alerte pour les erreurs de validation
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void generateNumeroDossier(String nom, String prenom) {
        // Logique pour générer le numéro de dossier
        // Exemple simple : utilisez les deux premières lettres du nom et du prénom, l'année de naissance et un identifiant unique
        String deuxPremieresLettresNom = (nom.length() >= 2) ? nom.substring(0, 2).toUpperCase() : "";
        String deuxPremieresLettresPrenom = (prenom.length() >= 2) ? prenom.substring(0, 2).toUpperCase() : "";
        int anneeActuelle = LocalDate.now().getYear();
        int idPatient = generateUniquePatientId(); // À remplacer par votre logique d'obtention de l'id

        tfDossier.setText(deuxPremieresLettresNom + deuxPremieresLettresPrenom + "-" + (idPatient + 1) + "/" + anneeActuelle);
    }

    private int generateUniquePatientId() {
        return patientService.getLastPatientId();
    }

    private int calculateAge(LocalDate dateNaissance) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateNaissance, currentDate).getYears();
    }

    public void setPatient(Patient patient, int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setDashboardAssistanteController(DashboardAssistanteController dashboardAssistanteController) {
        this.dashboardController = dashboardAssistanteController; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    
}
