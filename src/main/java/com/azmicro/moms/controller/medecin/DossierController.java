/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.controller.AjoutRendezVousController;
import com.azmicro.moms.controller.acte.ActeController;
import com.azmicro.moms.controller.bilan.BilanController;
import com.azmicro.moms.controller.imagerie.ImagerieController;
import com.azmicro.moms.controller.patient.FichePatientDetailsController;
import com.azmicro.moms.controller.prescription.PrescriptionController;
import com.azmicro.moms.dao.AnalyseDAOImpl;
import com.azmicro.moms.dao.HistoriqueMedicalDAO;
import com.azmicro.moms.dao.HistoriqueMedicalDAOImpl;
import com.azmicro.moms.dao.PatientDAOImpl;
import com.azmicro.moms.model.Analyse;
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.HistoriqueMedical;
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Medicaments;
import com.azmicro.moms.model.ModePaiement;
import com.azmicro.moms.model.Paiements;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Prescriptions;
import com.azmicro.moms.model.RendezVous;
import com.azmicro.moms.model.Sexe;
import com.azmicro.moms.model.SituationFamiliale;
import com.azmicro.moms.model.Type;
import com.azmicro.moms.model.TypeAnalyse;
import com.azmicro.moms.model.TypeImagerie;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.AnalyseService;
import com.azmicro.moms.service.ConsultationActeService;
import com.azmicro.moms.service.ConsultationService;
import com.azmicro.moms.service.HistoriqueMedicalService;
import com.azmicro.moms.service.ImagerieService;
import com.azmicro.moms.service.PaiementsService;
import com.azmicro.moms.service.PrescriptionService;
import com.azmicro.moms.service.RendezVousService;
import com.azmicro.moms.util.DatabaseUtil;
import com.azmicro.moms.util.MedicalCalculationService;
import com.azmicro.moms.util.MedicalFieldValidator;
import com.azmicro.moms.util.TableViewConfigurator;
import com.azmicro.moms.util.ListViewConfigurator;
import com.azmicro.moms.util.impression.ImpressionUtil;
import com.azmicro.moms.util.impression.TypeImpression;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class DossierController implements Initializable {

    private Patient patient;
    @FXML
    private Label tfTitle;
    @FXML
    private TextField tfNumDossier;
    @FXML
    private TextField tfNomPatient;
    @FXML
    private TextField tfPrenomPatient;
    @FXML
    private DatePicker dpDateNaissance;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfProfession;
    @FXML
    private RadioButton rbMale;
    @FXML
    private ToggleGroup tgSexe;
    @FXML
    private RadioButton rbFemelle;
    @FXML
    private ComboBox<SituationFamiliale> cbxSituationFamilial;
    @FXML
    private TextField tfTelephone;
    @FXML
    private TextArea txtAdresse;
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
    @FXML
    private TextField tfPoid;
    @FXML
    private TextField tfTaille;
    @FXML
    private TextField tfImc;
    @FXML
    private TextField tfPressionPatient;
    @FXML
    private TextField tfFrequenceRespiratoire;
    @FXML
    private TextField tfGlycimie;
    @FXML
    private TextField tfTmperature;
    @FXML
    private TextField tfSaOpatient;
    @FXML
    private TextArea txtSymptomes;
    @FXML
    private TextArea txtExamenClinique;
    @FXML
    private TextArea txtDiagnostiqueMedical;
    @FXML
    private TextArea txtCat;
    @FXML
    private Button btnAjouterConsultation;
    @FXML
    private Button btnEditConsultation;
    @FXML
    private Button btnSupprimerConsultation;
    @FXML
    private Button btnImprimerConsultation;

    private Consultations consultation;

    HistoriqueMedicalService historiqueMedicalService;
    private ConsultationService consultationService;
    @FXML
    private TextField tfFrquenceCardiaque;
    @FXML
    private TableView<Consultations> tvConsultation;
    @FXML
    private TableColumn<Consultations, String> clmDetailsConsultation;
    @FXML
    private TableColumn<Consultations, LocalDate> clmDateConsultation;
    @FXML
    private TableColumn<?, ?> clmMotifConsultation;
    @FXML
    private TableColumn<?, ?> clmMontantConsultation;
    @FXML
    private TableColumn<?, ?> clmVersmentConsultation;
    @FXML
    private TableColumn<?, ?> clmResteConsultation;

    private ConsultationService consultationService1;
    @FXML
    private ListView<Consultations> lvDateConsultationBilan;
    @FXML
    private DatePicker dpDateAnalyse;
    @FXML
    private TextArea txtRsltLigneBilan;
    @FXML
    private Button btnAjouterBilan;
    @FXML
    private TableView<Analyse> tvBilan;
    @FXML
    private TableColumn<Analyse, TypeAnalyse> clmLigneBilan;

    // Appel du service pour obtenir les données
    private AnalyseService analyseService;
    private ImagerieService imagerieService;
    @FXML
    private Button btnEditBilan;
    @FXML
    private Button btnDeleteBilan;
    @FXML
    private Button btnSaveImagerie;
    @FXML
    private Button btnEditEmagerie;
    @FXML
    private Button btnDeleteImagerie;
    @FXML
    private ListView<Consultations> lvDateConsultationRadio;
    @FXML
    private TableView<Imagerie> tvRadio;
    @FXML
    private TableColumn<Imagerie, TypeImagerie> clmLigneImagerie;
    @FXML
    private TextArea txtresultImagerie;
    @FXML
    private DatePicker dpDateImagerie;
    @FXML
    private Button btnAjouterOrdonnance;
    @FXML
    private Button btnEditOrdonnance;
    @FXML
    private Button btnDeleteOrdonnance;
    @FXML
    private TableView<Consultations> tvConsultationOrd;
    @FXML
    private TableColumn<Consultations, LocalDate> clmDateConsultationOrd;
    @FXML
    private TableColumn<Consultations, String> clmMotifConsultationOrd;
    @FXML
    private TableView<Prescriptions> tvOrdonnance;
    @FXML
    private TableColumn<Prescriptions, Medicaments> clmMedicament;
    @FXML
    private TableColumn<Prescriptions, String> clmDetailsOrdonnance;

    private PrescriptionService prescriptionService;
    @FXML
    private Button btnOrdonnanceBilan;
    @FXML
    private Button btnOrdonnanceImagerie;
    @FXML
    private Button btnOrdonnance;
    @FXML
    private Button btnAjouterRendezVous;
    @FXML
    private Button btnEditRendezVous;
    @FXML
    private Button btndeleteRendzVous;
    @FXML
    private TableView<RendezVous> tvRendezVous;
    @FXML
    private TableColumn<RendezVous, String> clmTitre;
    @FXML
    private TableColumn<RendezVous, LocalDate> clmDate;
    @FXML
    private TableColumn<RendezVous, String> clmDesc;
    @FXML
    private TableColumn<RendezVous, LocalDateTime> clmHourStart;
    @FXML
    private TableColumn<RendezVous, LocalDateTime> clmHourEnd;
    @FXML
    private TableView<Paiements> tvConsultationPaiement;
    @FXML
    private TableColumn<Paiements, LocalDate> clmDatePayment;
    @FXML
    private TableColumn<Paiements, Double> clmMontantPayement;
    @FXML
    private TableColumn<Paiements, Double> clmVersementPayment;
    @FXML
    private TableColumn<Paiements, Double> clmRestePayment;

    private ObservableList<RendezVous> rendezVousList;
    private RendezVousService rendezVousService;
    private PaiementsService paiementsService;
    private RendezVous rendezVous;
    private Medecin medecin;
    @FXML
    private TableView<ConsultationActe> tvActes;
    @FXML
    private TableColumn<ConsultationActe, String> clmActe;
    @FXML
    private TableColumn<ConsultationActe, Double> clmMontantActe;
    @FXML
    private TableColumn<ConsultationActe, Boolean> selectionClm;
    ConsultationActeService consultationActeService;
    @FXML
    private Button btnPrintFacture;
    @FXML
    private DatePicker dateConsultaion;

    /**
     * Initializes the controller class.
     */
    void setPatient(Patient patient) {
        this.patient = patient; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        tfTitle.setText(tfTitle.getText() + " " + patient.getPrenom() + " " + patient.getNom() + " Dossier N : " + patient.getNumDossier());
        tfNomPatient.setText(patient.getNom());
        tfPrenomPatient.setText(patient.getPrenom());
        tfNumDossier.setText(patient.getNumDossier());
        dpDateNaissance.setValue(patient.getDateNaissance());
        tfAge.setText(patient.getAge() + " ans");
        tfProfession.setText(patient.getProfession());
        cbxSituationFamilial.setValue(patient.getSituationFamiliale());
        if (patient.getSexe() == Sexe.M) {
            rbMale.setSelected(true);
        } else {
            rbFemelle.setSelected(true);
        }
        tfTelephone.setText(patient.getTelephone());
        txtAdresse.setText(patient.getAdresse());
        cbxTypeAntecedent.getItems().setAll(Type.values());

        refreshTableViewAntecedent();

        setupFrequenceCardiaqueField();

        setupFrequenceRespiratoireField();

        setupSaturationOxygenField();

        setupGlycemiaField();

        loadConsultations();

        getConsultationSelected();

        // Charger les données dans la TableView
        loadRendezVousData();

        loadPaiments();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeServices();
        initializeDateAndComboBoxes();
        initializeAntecedentsTable();
        initializeConsultationsTable();
        initializeBilansTable();
        initializeImagerieTable();
        initializeOrdonnancesTable();
        initializePrescriptionsTable();
        initializeRendezVousTable();
        initializePaiementsTable();
        initializeActesTable();
    }

    private void initializeServices() {
        try {
            this.consultationService = new ConsultationService(DatabaseUtil.getConnection());
            HistoriqueMedicalDAO historiqueMedicalDAO = new HistoriqueMedicalDAOImpl(
                new PatientDAOImpl(DatabaseUtil.getConnection())
            );
            this.historiqueMedicalService = new HistoriqueMedicalService(historiqueMedicalDAO);
            this.analyseService = new AnalyseService(new AnalyseDAOImpl(DatabaseUtil.getConnection()));
            this.imagerieService = new ImagerieService();
            this.prescriptionService = new PrescriptionService();
            this.rendezVousService = new RendezVousService();
            this.paiementsService = new PaiementsService();
            this.consultationActeService = new ConsultationActeService();
        } catch (SQLException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeDateAndComboBoxes() {
        dateConsultaion.setValue(LocalDate.now());
        cbxSituationFamilial.setItems(FXCollections.observableArrayList(SituationFamiliale.values()));
        cbxSituationFamilial.setConverter(new StringConverter<SituationFamiliale>() {
            @Override
            public String toString(SituationFamiliale situationFamiliale) {
                return situationFamiliale != null ? situationFamiliale.getDescription() : "";
            }

            @Override
            public SituationFamiliale fromString(String string) {
                for (SituationFamiliale situation : SituationFamiliale.values()) {
                    if (situation.getDescription().equals(string)) {
                        return situation;
                    }
                }
                return null;
            }
        });
        cbxSituationFamilial.getSelectionModel().selectFirst();

        if (!cbxTypeAntecedent.getItems().isEmpty()) {
            cbxTypeAntecedent.getSelectionModel().selectFirst();
        }
    }

    private void initializeAntecedentsTable() {
        TableViewConfigurator.configureAntecedentsTable(
            tvAntecedent, clmDateAntecedent, clmTypeAntecedent, clmDescriptionAntecedent
        );
        
        tvAntecedent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateForm(newValue);
            }
        });
    }

    private void initializeConsultationsTable() {
        TableViewConfigurator.configureConsultationsTable(
            tvConsultation, clmDetailsConsultation, clmDateConsultation
        );

        tvConsultation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });

        tfPoid.textProperty().addListener((observable, oldValue, newValue) -> updateIMC());
        tfTaille.textProperty().addListener((observable, oldValue, newValue) -> updateIMC());
        setupPressionArterielleField();
    }

    private void initializeBilansTable() {
        TableViewConfigurator.configureBilansTable(tvBilan, clmLigneBilan);

        tvBilan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dpDateAnalyse.setValue(newValue.getDateAnalyse());
                txtRsltLigneBilan.setText(newValue.getResultat());
            }
        });

        tvBilan.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tvBilan.getSelectionModel().getSelectedItem() != null) {
                Analyse selectedAnalyse = tvBilan.getSelectionModel().getSelectedItem();
                dpDateAnalyse.setValue(selectedAnalyse.getDateAnalyse());
                txtRsltLigneBilan.setText(selectedAnalyse.getResultat());
            }
        });

        lvDateConsultationBilan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadAnalyses(newValue);
            }
        });
    }

    private void initializeImagerieTable() {
        TableViewConfigurator.configureImagerieTable(tvRadio, clmLigneImagerie);

        tvRadio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dpDateImagerie.setValue(newValue.getDateImagerie());
                txtresultImagerie.setText(newValue.getResultat());
            }
        });

        tvRadio.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tvRadio.getSelectionModel().getSelectedItem() != null) {
                Imagerie selectedImagerie = tvRadio.getSelectionModel().getSelectedItem();
                dpDateImagerie.setValue(selectedImagerie.getDateImagerie());
                txtresultImagerie.setText(selectedImagerie.getResultat());
            }
        });

        lvDateConsultationRadio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadImagerie(newValue);
            }
        });
    }

    private void initializeOrdonnancesTable() {
        TableViewConfigurator.configureConsultationOrdonnanceTable(
            tvConsultationOrd, clmDateConsultationOrd, clmMotifConsultationOrd
        );

        tvConsultationOrd.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadPrescriptionsForConsultation(newValue);
            }
        });
    }

    private void initializePrescriptionsTable() {
        TableViewConfigurator.configurePrescriptionsTable(
            tvOrdonnance, clmMedicament, clmDetailsOrdonnance
        );
    }

    private void initializeRendezVousTable() {
        rendezVousList = FXCollections.observableArrayList();
        TableViewConfigurator.configureRendezVousTable(
            tvRendezVous, clmTitre, clmDate, clmDesc, clmHourStart, clmHourEnd
        );

        tvRendezVous.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            this.rendezVous = newSelection;
        });
    }

    private void initializePaiementsTable() {
        TableViewConfigurator.configurePaiementsTable(
            tvConsultationPaiement, clmDatePayment, clmMontantPayement, clmVersementPayment, clmRestePayment
        );

        tvConsultationPaiement.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadDataIntoTableView(newValue.getConsultation());
            }
        });

        tvConsultationPaiement.setRowFactory(tv -> {
            TableRow<Paiements> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Paiements rowData = row.getItem();
                    showActeDialog(rowData.getConsultation());
                }
            });
            return row;
        });
    }

    private void initializeActesTable() {
        TableViewConfigurator.configureActesTable(tvActes, clmActe, clmMontantActe);
    }

    private void loadDataIntoTableView(Consultations consultations) {
        List<ConsultationActe> consultationActeList = consultationActeService.findByConsultation(consultations);
        ObservableList<ConsultationActe> observableList = FXCollections.observableArrayList(consultationActeList);
        tvActes.setItems(observableList);
    }

    public void loadPrescriptionsForConsultation(Consultations consultation) {
        try {
            // Fetch the prescriptions based on the selected consultation
            if (consultation != null) {
                List<Prescriptions> prescriptions = prescriptionService.getPrescriptionByConsultation(consultation.getConsultationID());
                // Update tvOrdonnance with the fetched prescriptions
                tvOrdonnance.setItems(FXCollections.observableArrayList(prescriptions));
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error, e.g., show an alert to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement");
            alert.setContentText("Une erreur est survenue lors du chargement des prescriptions.");
            alert.showAndWait();
        }
    }

    private void setupGlycemiaField() {
        tfGlycimie.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Double glycemyLevel = Double.parseDouble(newValue);
                String classification = MedicalCalculationService.classifyGlycemiaFastingInGl(glycemyLevel);
                Tooltip tooltip = new Tooltip(classification);
                tfGlycimie.setTooltip(tooltip);
            } catch (NumberFormatException e) {
                tfGlycimie.setTooltip(new Tooltip("Veuillez entrer une valeur numérique valide en g/L."));
            }
        });
    }

    private void setupSaturationOxygenField() {
        tfSaOpatient.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int spo2 = Integer.parseInt(newValue);
                String classification = MedicalCalculationService.classifyOxygenSaturation(spo2);
                Tooltip tooltip = new Tooltip(classification);
                tfSaOpatient.setTooltip(tooltip);
            } catch (NumberFormatException e) {
                tfSaOpatient.setTooltip(new Tooltip("Veuillez entrer une valeur numérique valide."));
            }
        });

    }

    private void setupFrequenceRespiratoireField() {
        MedicalFieldValidator.setupFrequenceField(tfFrequenceRespiratoire, 2);
        
        tfFrequenceRespiratoire.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                int frequence = Integer.parseInt(newValue);
                String classification = MedicalCalculationService.classifyRespiratoryRate(frequence, patient);
                Tooltip tooltip = new Tooltip(classification);
                tfFrequenceRespiratoire.setTooltip(tooltip);
            } else {
                tfFrequenceRespiratoire.setTooltip(null);
            }
        });
    }

    private void setupPressionArterielleField() {
        MedicalFieldValidator.setupPressionArterielleField(tfPressionPatient);
    }

    private void setupFrequenceCardiaqueField() {
        tfFrquenceCardiaque.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int frequenceCardiaque = Integer.parseInt(newValue);
                    int age = patient.getAge();
                    String sexe = patient.getSexe().getDescription();

                    String interpretation = MedicalCalculationService.getHeartRateInterpretation(age, sexe, frequenceCardiaque);
                    Tooltip tooltip = new Tooltip(interpretation);
                    tfFrquenceCardiaque.setTooltip(tooltip);
                } catch (NumberFormatException e) {
                    tfFrquenceCardiaque.setTooltip(new Tooltip("Veuillez entrer une valeur numérique valide."));
                }
            } else {
                tfFrquenceCardiaque.setTooltip(null);
            }
        });

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

    @FXML
    private void ajouterConsultation(ActionEvent event) {
        double poids = MedicalFieldValidator.parseDecimal(tfPoid.getText());
        double taille = MedicalFieldValidator.parseDecimal(tfTaille.getText()) / 100;

        double imc = MedicalCalculationService.calculateIMC(poids, taille);
        tfImc.setText(String.format("%.2f", imc));

        this.consultation = new Consultations();
        consultation.setPoids(poids);
        consultation.setTaille(taille * 100);
        consultation.setImc(imc);
        consultation.setFrequencequardiaque(MedicalFieldValidator.parseInt(tfFrquenceCardiaque.getText()));
        consultation.setPression(tfPressionPatient.getText());
        consultation.setFrequencerespiratoire(MedicalFieldValidator.parseInt(tfFrequenceRespiratoire.getText()));
        consultation.setGlycimie(MedicalFieldValidator.parseDecimal(tfGlycimie.getText()));
        consultation.setTemperature(MedicalFieldValidator.parseDecimal(tfTmperature.getText()));
        consultation.setSaO(MedicalFieldValidator.parseInt(tfSaOpatient.getText()));
        consultation.setSymptome(txtSymptomes.getText());
        consultation.setExamenClinique(txtExamenClinique.getText());
        consultation.setDiagnostique(txtDiagnostiqueMedical.getText());
        consultation.setCat(txtCat.getText());
        consultation.setPatient(patient);
        consultation.setDateConsultation(dateConsultaion.getValue());

        // Sauvegarder la consultation
        boolean success = consultationService.save(consultation);
        if (success) {
            // Vérifiez que l'ID de la consultation a été généré et récupéré
            int consultationId = consultation.getConsultationID();
            if (consultationId > 0) {
                showAlert(AlertType.INFORMATION, "Consultation Added", "The consultation has been added successfully.");

                // Créer un paiement après avoir ajouté la consultation
                Paiements paiement = new Paiements();
                paiement.setConsultation(consultation);  // Attacher la consultation avec l'ID valide
                paiement.setDatePaiement(consultation.getDateConsultation());  // Utiliser la date de consultation comme date de paiement
                paiement.setEtatPayment(false);  // Par défaut à "false"
                paiement.setModePaiement(ModePaiement.ESPECES);  // Mode de paiement par défaut
                paiement.setMontant(0.0);  // Montant initial à 0
                paiement.setReste(0.0);  // Aucun reste initial
                paiement.setVersment(0.0);  // Aucun versement initial

                // Sauvegarder le paiement
                boolean paiementSaved = paiementsService.savePaiement(paiement);
                if (!paiementSaved) {
                    showAlert(AlertType.ERROR, "Error", "Failed to save the payment.");
                } else {
                    showAlert(AlertType.INFORMATION, "Payment Added", "The payment has been added successfully.");
                    loadPaiments();
                }

                // Rafraîchir les consultations et la vue
                loadConsultations();
                tvConsultation.refresh();
                clearFields();  // Effacer les champs après l'enregistrement
            } else {
                showAlert(AlertType.ERROR, "Error", "Consultation ID is missing.");
            }
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to add the consultation.");
        }
    }

    // Méthode pour mettre à jour l'IMC
    private void updateIMC() {
        try {
            double poids = Double.parseDouble(tfPoid.getText());
            double tailleCm = Double.parseDouble(tfTaille.getText());
            double tailleM = tailleCm / 100; // Convertir la taille en mètres

            if (tailleM > 0) { // Éviter la division par zéro
                double imc = MedicalCalculationService.calculateIMC(poids, tailleM);
                tfImc.setText(String.format("%.2f", imc));
                formatImcField(imc);
            } else {
                tfImc.setText("");
            }
        } catch (NumberFormatException e) {
            tfImc.setText("");
        }
    }

    private void formatImcField(double imc) {
        MedicalCalculationService.IMCClassification classification = MedicalCalculationService.classifyIMC(imc);
        tfImc.setTooltip(new Tooltip(classification.getInterpretation()));
        tfImc.setStyle(classification.getStyle());
    }

    @FXML
    private void supprimerConsultation(ActionEvent event) {
        // Récupérer la consultation sélectionnée
        Consultations selectedConsultation = tvConsultation.getSelectionModel().getSelectedItem();
        if (selectedConsultation == null) {
            // Afficher un message d'erreur si aucune consultation n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une consultation à supprimer.");
            alert.showAndWait();
            return;
        }

        // Demander confirmation de la suppression
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de la suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette consultation ?");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Si l'utilisateur confirme, supprimer la consultation
            boolean success = consultationService.delete(selectedConsultation.getConsultationID());

            // Afficher un message de confirmation ou d'erreur
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("La consultation a été supprimée avec succès.");
                alert.showAndWait();

                // Retirer la consultation de la TableView
                tvConsultation.getItems().remove(selectedConsultation);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la suppression de la consultation.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void imprimeConsulation(ActionEvent event) {
    }

    @FXML
    private void editConsultation(ActionEvent event) {
        // Récupérer la consultation sélectionnée
        Consultations selectedConsultation = tvConsultation.getSelectionModel().getSelectedItem();
        if (selectedConsultation == null) {
            // Afficher un message d'erreur si aucune consultation n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une consultation à modifier.");
            alert.showAndWait();
            return;
        }

        // Lors de la validation des modifications
        selectedConsultation.setPoids(MedicalFieldValidator.parseDecimal(tfPoid.getText()));
        selectedConsultation.setTaille(MedicalFieldValidator.parseDecimal(tfTaille.getText()));
        selectedConsultation.setImc(MedicalFieldValidator.parseDecimal(tfImc.getText()));
        selectedConsultation.setTemperature(MedicalFieldValidator.parseDecimal(tfTmperature.getText()));
        selectedConsultation.setPression(tfPressionPatient.getText());
        selectedConsultation.setFrequencequardiaque(MedicalFieldValidator.parseInt(tfFrquenceCardiaque.getText()));
        selectedConsultation.setFrequencerespiratoire(MedicalFieldValidator.parseInt(tfFrequenceRespiratoire.getText()));
        selectedConsultation.setExamenClinique(txtExamenClinique.getText());
        selectedConsultation.setSymptome(txtSymptomes.getText());
        selectedConsultation.setDiagnostique(txtDiagnostiqueMedical.getText());
        selectedConsultation.setGlycimie(MedicalFieldValidator.parseDecimal(tfGlycimie.getText()));
        selectedConsultation.setSaO(MedicalFieldValidator.parseInt(tfSaOpatient.getText()));
        selectedConsultation.setCat(txtCat.getText());
        selectedConsultation.setPatient(patient);
        selectedConsultation.setDateConsultation(dateConsultaion.getValue());

        boolean success = consultationService.update(selectedConsultation);
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("La consultation a été mise à jour avec succès.");
            alert.showAndWait();
            // Actualiser la table des consultations
            tvConsultation.refresh();
            clearFields();  // Clear fields after successful update
            tvConsultation.getSelectionModel().clearSelection(); // Clear table selection
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la mise à jour de la consultation.");
            alert.showAndWait();
        }
    }

    private void clearFields() {
        tfPoid.clear();
        tfTaille.clear();
        tfImc.clear();
        tfFrquenceCardiaque.clear();
        tfPressionPatient.clear();
        tfFrequenceRespiratoire.clear();
        tfGlycimie.clear();
        tfTmperature.clear();
        tfSaOpatient.clear();
        txtSymptomes.clear();
        txtExamenClinique.clear();
        txtDiagnostiqueMedical.clear();
        txtCat.clear();
        dateConsultaion.setValue(null); // Clear the DatePicker
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(AlertType type, String title, String message, javafx.stage.Window owner) {
        Alert alert = new Alert(type);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadConsultations() {
        List<Consultations> consultationsList = consultationService.findAllByIdPatient(this.patient.getPatientID());
        ObservableList<Consultations> consultationsObservableList = FXCollections.observableArrayList(consultationsList);
        tvConsultation.setItems(consultationsObservableList);
        tvConsultationOrd.setItems(consultationsObservableList);
        //tvConsultationPaiement.setItems(consultationsObservableList);
        initializeConsultationsDates(this.patient.getPatientID());
        getConsultationSelected();
    }

    public void loadPaiments() {
        List<Paiements> paiementsList = paiementsService.getPaiementsByConsultation(this.patient);
        System.out.println("taille loadPaiments " + paiementsList.size());
        ObservableList<Paiements> paiementObservableList = FXCollections.observableArrayList(paiementsList);
        tvConsultationPaiement.setItems(paiementObservableList);
        if (!paiementObservableList.isEmpty()) {
            tvConsultationPaiement.getSelectionModel().selectLast();
            tvConsultationPaiement.scrollTo(paiementObservableList.size() - 1);
        }
    }

    private void populateFields(Consultations consultation) {
        tfPoid.setText(String.valueOf(consultation.getPoids()));
        tfTaille.setText(String.valueOf(consultation.getTaille()));
        tfImc.setText(String.valueOf(consultation.getImc()));
        tfFrquenceCardiaque.setText(String.valueOf(consultation.getFrequencequardiaque()));
        tfPressionPatient.setText(consultation.getPression());
        tfFrequenceRespiratoire.setText(String.valueOf(consultation.getFrequencerespiratoire()));
        tfGlycimie.setText(String.valueOf(consultation.getGlycimie()));
        tfTmperature.setText(String.valueOf(consultation.getTemperature()));
        tfSaOpatient.setText(String.valueOf(consultation.getSaO()));
        txtSymptomes.setText(consultation.getSymptome());
        txtExamenClinique.setText(consultation.getExamenClinique());
        txtDiagnostiqueMedical.setText(consultation.getDiagnostique());
        txtCat.setText(consultation.getCat());
        dateConsultaion.setValue(consultation.getDateConsultation());
    }

    private Consultations selectedConsultation;

    public void initializeConsultationsDates(int idPatient) {
        // Récupérer la liste des consultations du patient

        // Récupérer la liste des consultations du patient
        List<Consultations> consultations = consultationService.findAllByIdPatient(idPatient);
        System.out.println("Consultations récupérées : " + consultations.size());

        // Créer une liste observable pour les consultations
        ObservableList<Consultations> consultationsList = FXCollections.observableArrayList(consultations);

        // Configurer le ListView pour afficher les dates des consultations
        lvDateConsultationBilan.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Consultations item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDateConsultation().toString());
                }
            }

        });

        lvDateConsultationRadio.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Consultations item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDateConsultation().toString());
                }
            }
        });

        // Initialiser les ListViews avec les consultations
        lvDateConsultationBilan.setItems(consultationsList);
        lvDateConsultationRadio.setItems(consultationsList);
        System.out.println("ListView initialisée avec les consultations.");

        // Initialiser les ListViews avec les consultations
        lvDateConsultationBilan.setItems(consultationsList);
        lvDateConsultationRadio.setItems(consultationsList);

        // Sélectionner automatiquement le dernier élément de la ListView
        if (!consultationsList.isEmpty()) {
            lvDateConsultationBilan.getSelectionModel().selectLast();
            lvDateConsultationBilan.scrollTo(consultationsList.size() - 1);

            lvDateConsultationRadio.getSelectionModel().selectLast();
            lvDateConsultationRadio.scrollTo(consultationsList.size() - 1);
        }

        // Ajouter un écouteur pour gérer la sélection dans la ListView
        lvDateConsultationBilan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.selectedConsultation = newValue;
                loadAnalyses();
                System.out.println("Consultation sélectionnée : " + selectedConsultation);
            }
        });

        // Ajouter un gestionnaire de clics de souris pour la ListView
        lvDateConsultationBilan.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && lvDateConsultationBilan.getSelectionModel().getSelectedItem() != null) {
                // Obtient la consultation sélectionnée
                Consultations selectedConsultation = lvDateConsultationBilan.getSelectionModel().getSelectedItem();
                // Met à jour la consultation sélectionnée
                this.selectedConsultation = selectedConsultation;
                // Charge les analyses pour la consultation sélectionnée
                loadAnalyses();
            }
        });

        lvDateConsultationRadio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.selectedConsultation = newValue;
                loadImagerie(newValue);
                System.out.println("Consultation sélectionnée : " + selectedConsultation);
            }
        });
        // Ajouter un gestionnaire de clics de souris pour la ListView
        lvDateConsultationRadio.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && lvDateConsultationRadio.getSelectionModel().getSelectedItem() != null) {
                // Obtient la consultation sélectionnée
                Consultations selectedConsultation = lvDateConsultationRadio.getSelectionModel().getSelectedItem();
                // Met à jour la consultation sélectionnée
                this.selectedConsultation = selectedConsultation;
                // Charge les analyses pour la consultation sélectionnée
                loadImagerie(this.selectedConsultation);
            }
        });

//        List<Consultations> consultations = consultationService.findAllByIdPatient(idPatient);
//        System.out.println("Consultations récupérées : " + consultations.size());
//        // Créer une map pour associer chaque date à sa consultation
//        Map<LocalDate, Consultations> dateToConsultationMap = new HashMap<>();
//        // Extraire les dates et les associer à leur consultation
//        List<LocalDate> dates = consultations.stream()
//                .map(consultation -> {
//                    dateToConsultationMap.put(consultation.getDateConsultation(), consultation);
//                    return consultation.getDateConsultation();
//                })
//                .collect(Collectors.toList());
//        System.out.println("Dates extraites : " + dates);
//        // Créer une liste observable pour les dates
//        ObservableList<LocalDate> datesList = FXCollections.observableArrayList(dates);
//        // Initialiser le ListView avec les dates
//        lvDateConsultationBilan.setItems(datesList);
//        lvDateConsultationRadio.setItems(datesList);
//        System.out.println("ListView initialisée avec les dates.");
//
//        // Vérifiez la ListView
//        System.out.println("Nombre d'éléments dans la ListView : " + lvDateConsultationBilan.getItems().size());
//
//        // Ajouter un écouteur pour gérer la sélection dans la ListView
//        lvDateConsultationBilan.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LocalDate>() {
//            @Override
//            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//                if (newValue != null && (oldValue == null || !newValue.equals(oldValue))) {
//                    // Récupérer l'objet Consultation correspondant à la date sélectionnée
//                    selectedConsultation = dateToConsultationMap.get(newValue);
//                    if (selectedConsultation != null) {
//                        loadAnalyses();
//                        System.out.println("Consultation sélectionnée : " + selectedConsultation);
//                    } else {
//                        System.out.println("Aucune consultation trouvée pour la date sélectionnée.");
//                    }
//                }
//            }
//        });
//
//        lvDateConsultationBilan.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Sélection modifiée : Ancienne valeur = " + oldValue + ", Nouvelle valeur = " + newValue);
//            if (lvDateConsultationBilan.getItems().size() == 1) {
//                // Traitement spécial pour une seule entrée
//                if (newValue != null) {
//                    selectedConsultation = dateToConsultationMap.get(newValue);
//                    if (selectedConsultation != null) {
//                        loadAnalyses();
//                        System.out.println("Consultation sélectionnée : " + selectedConsultation);
//                    } else {
//                        System.out.println("Aucune consultation trouvée pour la date sélectionnée.");
//                    }
//                }
//            } else {
//                // Traitement standard
//                if (newValue != null) {
//                    selectedConsultation = dateToConsultationMap.get(newValue);
//                    if (selectedConsultation != null) {
//                        loadAnalyses();
//                        System.out.println("Consultation sélectionnée : " + selectedConsultation);
//                    } else {
//                        System.out.println("Aucune consultation trouvée pour la date sélectionnée.");
//                    }
//                }
//            }
//        });
//
//        lvDateConsultationBilan.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 1 && lvDateConsultationBilan.getSelectionModel().getSelectedItem() != null) {
//                // Obtient la date sélectionnée
//                LocalDate selectedDate = lvDateConsultationBilan.getSelectionModel().getSelectedItem();
//                // Met à jour la consultation sélectionnée
//                selectedConsultation = dateToConsultationMap.get(selectedDate);
//                // Charge les analyses pour la consultation sélectionnée
//                loadAnalyses();
//            }
//        });
//        lvDateConsultationRadio.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LocalDate>() {
//            @Override
//            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//                if (newValue != null && (oldValue == null || !newValue.equals(oldValue))) {
//                    // Récupérer l'objet Consultation correspondant à la date sélectionnée
//                    selectedConsultation = dateToConsultationMap.get(newValue);
//                    if (selectedConsultation != null) {
//                        loadImagerie();
//                        System.out.println("Consultation sélectionnée : " + selectedConsultation);
//                    } else {
//                        System.out.println("Aucune consultation trouvée pour la date sélectionnée.");
//                    }
//                }
//            }
//        });
//
//        lvDateConsultationRadio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Sélection modifiée : Ancienne valeur = " + oldValue + ", Nouvelle valeur = " + newValue);
//            if (lvDateConsultationRadio.getItems().size() == 1) {
//                // Traitement spécial pour une seule entrée
//                if (newValue != null) {
//                    selectedConsultation = dateToConsultationMap.get(newValue);
//                    if (selectedConsultation != null) {
//                        loadImagerie();
//                        System.out.println("Consultation sélectionnée : " + selectedConsultation);
//                    } else {
//                        System.out.println("Aucune consultation trouvée pour la date sélectionnée.");
//                    }
//                }
//            } else {
//                // Traitement standard
//                if (newValue != null) {
//                    selectedConsultation = dateToConsultationMap.get(newValue);
//                    if (selectedConsultation != null) {
//                        loadImagerie();
//                        System.out.println("Consultation sélectionnée : " + selectedConsultation);
//                    } else {
//                        System.out.println("Aucune consultation trouvée pour la date sélectionnée.");
//                    }
//                }
//            }
//        });
//
//        lvDateConsultationRadio.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 1 && lvDateConsultationRadio.getSelectionModel().getSelectedItem() != null) {
//                // Obtient la date sélectionnée
//                LocalDate selectedDate = lvDateConsultationRadio.getSelectionModel().getSelectedItem();
//                // Met à jour la consultation sélectionnée
//                selectedConsultation = dateToConsultationMap.get(selectedDate);
//                // Charge les analyses pour la consultation sélectionnée
//                loadImagerie();
//            }
//        });
    }

    // Méthode appelée lorsqu'une analyse est sélectionnée
    private void handleAnalyseSelection(Analyse selectedAnalyse) {
        System.out.println("Analyse sélectionnée : " + selectedAnalyse);
        // Traitez l'objet Analyse sélectionné ici, par exemple en chargeant des détails supplémentaires
    }

    @FXML
    private void ajouterBilan(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée dans la TableView
        Consultations selectedDate = lvDateConsultationBilan.getSelectionModel().getSelectedItem();

        if (selectedDate != null) {
            try {
                // Charger la vue FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/bilan/bilan-view.fxml"));
                Parent root = fxmlLoader.load();

                // Récupérer le contrôleur de la vue Bilan
                BilanController bilanController = fxmlLoader.getController();

                // Passer l'instance de Consultation au BilanController
                bilanController.setConsultation(selectedConsultation);

                // Créer une nouvelle scène
                Scene scene = new Scene(root);

                // Créer un nouveau stage (fenêtre)
                Stage stage = new Stage();
                stage.setTitle("Ajouter Bilan");

                // Appliquer la scène au stage
                stage.setScene(scene);

                // Configurer le stage comme une fenêtre modale
                stage.initModality(Modality.WINDOW_MODAL);

                // Lier le stage modal à la fenêtre principale (si nécessaire)
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initOwner(primaryStage);

                // Désactiver la possibilité d'agrandir la fenêtre
                stage.setResizable(false);

                // Afficher la fenêtre modale et attendre sa fermeture
                stage.showAndWait();
                loadAnalyses();

            } catch (IOException e) {
                e.printStackTrace();
                // Vous pouvez aussi gérer l'erreur avec une boîte de dialogue d'alerte ici
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de chargement");
                alert.setContentText("Une erreur est survenue lors du chargement de la vue bilan. Veuillez réessayer.");
                alert.showAndWait();
            }
        } else {
            // Afficher un message d'alerte si aucune ligne n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ligne dans la table avant d'ajouter un bilan.");
            alert.showAndWait();
        }
    }

    public void loadAnalyses() {
        if (selectedConsultation != null) {
            List<Analyse> analyses = analyseService.getAnalysesByConsultationId(selectedConsultation.getConsultationID());

            // Convertir la liste en ObservableList et assigner à la TableView
            ObservableList<Analyse> observableAnalyses = FXCollections.observableArrayList(analyses);
            tvBilan.setItems(observableAnalyses);
        } else {
            System.out.println("Aucune consultation sélectionnée. Impossible de charger les analyses.");
        }
    }

    public void loadAnalyses(Consultations consultations) {
        if (consultations != null) {
            List<Analyse> analyses = analyseService.getAnalysesByConsultationId(consultations.getConsultationID());

            // Convertir la liste en ObservableList et assigner à la TableView
            ObservableList<Analyse> observableAnalyses = FXCollections.observableArrayList(analyses);
            tvBilan.setItems(observableAnalyses);
        } else {
            System.out.println("Aucune consultation sélectionnée. Impossible de charger les analyses.");
        }
    }

    public void refreshBilan() {
        loadAnalyses(); // Recharger les analyses
    }

    @FXML
    private void editBilan(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée
        Analyse selectedAnalyse = tvBilan.getSelectionModel().getSelectedItem();
        if (selectedAnalyse != null) {
            // Mettre à jour les informations de l'analyse sélectionnée avec les valeurs des contrôles
            selectedAnalyse.setDateAnalyse(dpDateAnalyse.getValue());
            selectedAnalyse.setResultat(txtRsltLigneBilan.getText());

            // Sauvegarder les modifications dans la base de données
            analyseService.updateAnalyse(selectedAnalyse);
            System.out.println(selectedAnalyse.toString());

            // Rafraîchir la TableView pour montrer les changements
            tvBilan.refresh();
            loadAnalyses();

            // Afficher une alerte de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le bilan a été mis à jour avec succès.");
        } else {
            // Gérer le cas où aucune ligne n'est sélectionnée (afficher un message d'alerte, par exemple)
            showAlert(Alert.AlertType.WARNING, "Sélectionnez un bilan", "Veuillez sélectionner un bilan à modifier.");
        }
    }

    @FXML
    private void deleteBilan(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée
        Analyse selectedAnalyse = tvBilan.getSelectionModel().getSelectedItem();
        if (selectedAnalyse != null) {
            // Confirmer la suppression avec l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce bilan ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer l'analyse de la base de données
                analyseService.deleteAnalyse(selectedAnalyse.getAnalyseID());

                // Supprimer l'analyse de la TableView
                tvBilan.getItems().remove(selectedAnalyse);

                // Afficher une alerte de succès
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le bilan a été supprimé avec succès.");
            }
        } else {
            // Gérer le cas où aucune ligne n'est sélectionnée (afficher un message d'alerte)
            showAlert(Alert.AlertType.WARNING, "Sélectionnez un bilan", "Veuillez sélectionner un bilan à supprimer.");
        }
    }

    @FXML
    private void saveImagerie(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée dans la TableView
        Consultations selectedDateConsultation = lvDateConsultationRadio.getSelectionModel().getSelectedItem();
        if (selectedDateConsultation != null) {
            try {
                // Charger la vue FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/imagerie/imagerie-view.fxml"));
                Parent root = fxmlLoader.load();

                // Récupérer le contrôleur de la vue Imagerie
                ImagerieController imagerieController = fxmlLoader.getController();

                // Passer l'instance de Consultation au ImagerieController
                imagerieController.setConsultation(selectedConsultation);

                // Créer une nouvelle scène
                Scene scene = new Scene(root);

                // Créer un nouveau stage (fenêtre)
                Stage stage = new Stage();
                stage.setTitle("Ajouter Imagerie");

                // Appliquer la scène au stage
                stage.setScene(scene);

                // Configurer le stage comme une fenêtre modale
                stage.initModality(Modality.WINDOW_MODAL);

                // Lier le stage modal à la fenêtre principale (si nécessaire)
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initOwner(primaryStage);

                // Désactiver la possibilité d'agrandir la fenêtre
                stage.setResizable(false);

                // Afficher la fenêtre modale et attendre sa fermeture
                stage.showAndWait();
                loadImagerie();

            } catch (IOException e) {
                e.printStackTrace();
                // Afficher une alerte d'erreur en cas de problème
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de chargement");
                alert.setContentText("Une erreur est survenue lors du chargement de la vue imagerie. Veuillez réessayer.");
                alert.showAndWait();
            }
        } else {
            // Afficher un message d'alerte si aucune ligne n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ligne dans la table avant d'ajouter une imagerie.");
            alert.showAndWait();
        }
    }

    @FXML
    private void editImagerie(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée
        Imagerie selectedImagerie = tvRadio.getSelectionModel().getSelectedItem();
        if (selectedImagerie != null) {
            // Mettre à jour les informations de l'analyse sélectionnée avec les valeurs des contrôles
            selectedImagerie.setDateImagerie(dpDateImagerie.getValue());
            selectedImagerie.setResultat(txtresultImagerie.getText());

            // Sauvegarder les modifications dans la base de données
            imagerieService.updateImagerie(selectedImagerie);
            System.out.println(selectedImagerie.toString());

            // Rafraîchir la TableView pour montrer les changements
            tvRadio.refresh();
            loadImagerie();

            // Afficher une alerte de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le bilan a été mis à jour avec succès.");
        } else {
            // Gérer le cas où aucune ligne n'est sélectionnée (afficher un message d'alerte, par exemple)
            showAlert(Alert.AlertType.WARNING, "Sélectionnez un bilan", "Veuillez sélectionner un bilan à modifier.");
        }
    }

    @FXML
    private void deleteImagerie(ActionEvent event) {
        Imagerie selectedImagerie = tvRadio.getSelectionModel().getSelectedItem();
        System.out.println(selectedImagerie.toString());
        if (selectedImagerie != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette imagerie ?");
            confirmationAlert.setContentText("Nom de l'imagerie : " + selectedImagerie.getTypeImagerie().getNomImagerieFr());

            // Attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // L'utilisateur a confirmé la suppression
                imagerieService.deleteImagerie(selectedImagerie.getImagerieID());
                tvRadio.getItems().remove(selectedImagerie); // Supprimer l'imagerie de la TableView

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("L'imagerie a été supprimée avec succès.");
                successAlert.showAndWait();
            } else {
                // L'utilisateur a annulé la suppression
                System.out.println("Suppression annulée.");
            }
        } else {
            // Aucune imagerie n'est sélectionnée
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Aucune imagerie sélectionnée !");
            errorAlert.showAndWait();
        }
    }

    public void loadImagerie() {
        if (selectedConsultation != null) {
            List<Imagerie> imageries = imagerieService.findByConsultationId(selectedConsultation.getConsultationID());

            // Convertir la liste en ObservableList et assigner à la TableView
            ObservableList<Imagerie> observableImageries = FXCollections.observableArrayList(imageries);
            tvRadio.setItems(observableImageries);
        } else {
            System.out.println("Aucune consultation sélectionnée. Impossible de charger les imagerie.");
        }
    }

    public void loadImagerie(Consultations consultations) {
        if (consultations != null) {
            List<Imagerie> imageries = imagerieService.findByConsultationId(consultations.getConsultationID());

            // Convertir la liste en ObservableList et assigner à la TableView
            ObservableList<Imagerie> observableImageries = FXCollections.observableArrayList(imageries);
            tvRadio.setItems(observableImageries);
        } else {
            System.out.println("Aucune consultation sélectionnée. Impossible de charger les imagerie.");
        }
    }

    public void refreshImagerie() {
        loadImagerie(); // Recharger les analyses
    }

    public void getConsultationSelected() {
        // Initialisez la TableView avec les consultations (par exemple, depuis la base de données)
        List<Consultations> consultations = consultationService.findAllByIdPatient(this.patient.getPatientID());
        ObservableList<Consultations> consultationList = FXCollections.observableArrayList(consultations);
        tvConsultationOrd.setItems(consultationList);

        // Ajouter un écouteur de sélection pour la TableView
        tvConsultationOrd.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Consultations>() {
            @Override
            public void changed(ObservableValue<? extends Consultations> observable, Consultations oldValue, Consultations newValue) {
                if (newValue != null && (oldValue == null || !newValue.equals(oldValue))) {
                    // La consultation sélectionnée change
                    selectedConsultation = newValue;
                    loadPrescriptionsForConsultation(selectedConsultation);
                }
            }
        });

        // Optionnel : Réagir aux clics de la souris
        tvConsultationOrd.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tvConsultationOrd.getSelectionModel().getSelectedItem() != null) {
                // Mettre à jour la consultation sélectionnée
                selectedConsultation = tvConsultationOrd.getSelectionModel().getSelectedItem();
                // Charger les détails de la prescription
                loadPrescriptionsForConsultation(selectedConsultation);
            }
        });

        // Sélectionner automatiquement la dernière consultation dans la liste si elle existe
        if (!consultationList.isEmpty()) {
            int lastIndex = consultationList.size() - 1;
            selectedConsultation = consultationList.get(lastIndex);
            tvConsultationOrd.getSelectionModel().select(lastIndex); // Sélectionner automatiquement la dernière consultation
            tvConsultationPaiement.getSelectionModel().select(lastIndex);
            loadPrescriptionsForConsultation(selectedConsultation);
        }
    }

    @FXML
    private void ajouterOrdonnace(ActionEvent event) {
        // Vérifier si une ligne est sélectionnée dans la TableView
        Consultations selectedConsultation = tvConsultationOrd.getSelectionModel().getSelectedItem();

        if (selectedConsultation != null) {
            try {
                // Charger la vue FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/prescription/prescription-view.fxml"));
                Parent root = fxmlLoader.load();

                // Récupérer le contrôleur de la vue Prescription
                PrescriptionController prescriptionController = fxmlLoader.getController();

                // Passer l'instance de Consultation au PrescriptionController
                prescriptionController.setConsultation(selectedConsultation);

                prescriptionController.setDossierController(this);

                // Créer une nouvelle scène
                Scene scene = new Scene(root);

                // Créer un nouveau stage (fenêtre)
                Stage stage = new Stage();
                stage.setTitle("Ajouter ordonnance");

                // Appliquer la scène au stage
                stage.setScene(scene);

                // Configurer le stage comme une fenêtre modale
                stage.initModality(Modality.WINDOW_MODAL);

                // Lier le stage modal à la fenêtre principale (si nécessaire)
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initOwner(primaryStage);

                // Désactiver la possibilité d'agrandir la fenêtre
                stage.setResizable(false);

                // Afficher la fenêtre modale et attendre sa fermeture
                stage.showAndWait();
                loadPrescriptionsForConsultation(tvConsultationOrd.getSelectionModel().getSelectedItem());

            } catch (IOException e) {
                e.printStackTrace();
                // Vous pouvez aussi gérer l'erreur avec une boîte de dialogue d'alerte ici
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de chargement");
                alert.setContentText("Une erreur est survenue lors du chargement de la vue prescription. Veuillez réessayer.");
                alert.showAndWait();
            }
        } else {
            // Afficher un message d'alerte si aucune ligne n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ligne dans la table avant d'ajouter une ordonnance.");
            alert.showAndWait();
        }
    }

    @FXML
    private void editOrdonnance(ActionEvent event) {
        Consultations selectedConsultation = tvConsultationOrd.getSelectionModel().getSelectedItem();
        if (selectedConsultation != null) {
            try {
                // Charger la vue prescription-view.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/prescription/prescription-view.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur associé à la vue
                PrescriptionController prescriptionController = loader.getController();

                // Passer la consultation et les prescriptions existantes au contrôleur
                prescriptionController.setConsultation(selectedConsultation);
                prescriptionController.loadExistingPrescriptions();

                // Créer une nouvelle scène
                Scene scene = new Scene(root);

                // Créer un nouveau stage (fenêtre)
                Stage stage = new Stage();
                stage.setTitle("modifier ordonnance");

                // Appliquer la scène au stage
                stage.setScene(scene);

                // Configurer le stage comme une fenêtre modale
                stage.initModality(Modality.WINDOW_MODAL);

                // Lier le stage modal à la fenêtre principale (si nécessaire)
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initOwner(primaryStage);

                // Désactiver la possibilité d'agrandir la fenêtre
                stage.setResizable(false);

                // Afficher la fenêtre modale et attendre sa fermeture
                stage.showAndWait();
                // Après la fermeture du modal, recharger les prescriptions
                loadPrescriptionsForConsultation(tvConsultationOrd.getSelectionModel().getSelectedItem());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Afficher un message d'alerte si aucune ligne n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ligne dans la table avant d'ajouter une ordonnance.");
            alert.showAndWait();
        }

    }

    @FXML
    private void deleteOrdonnance(ActionEvent event) {
        // Vérifier si une ordonnance est sélectionnée
        Prescriptions selectedPrescription = tvOrdonnance.getSelectionModel().getSelectedItem();

        if (selectedPrescription != null) {
            // Créer une alerte de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer l'ordonnance");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette ordonnance ?");

            // Obtenir la réponse de l'utilisateur
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // L'utilisateur a confirmé la suppression

                // Supprimer l'ordonnance de la base de données
                PrescriptionService prescriptionService = new PrescriptionService();
                boolean success = prescriptionService.deletePrescription(selectedPrescription.getPrescriptionID());

                if (success) {
                    // Supprimer l'ordonnance de la TableView
                    tvOrdonnance.getItems().remove(selectedPrescription);

                    // Afficher un message de succès
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Suppression réussie");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("L'ordonnance a été supprimée avec succès.");
                    successAlert.showAndWait();
                } else {
                    // Afficher un message d'erreur en cas d'échec
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur de suppression");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Une erreur est survenue lors de la suppression de l'ordonnance.");
                    errorAlert.showAndWait();
                }
            }
        } else {
            // Afficher un message d'alerte si aucune ordonnance n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une ordonnance dans la table avant de la supprimer.");
            alert.showAndWait();
        }
    }

    private PrescriptionController prescriptionController;

    public void setPrescriptionController(PrescriptionController prescriptionController) {
        this.prescriptionController = prescriptionController;
    }

    private void handleConsultationSelected() {
        Consultations selectedConsultation = tvConsultationOrd.getSelectionModel().getSelectedItem();
        if (selectedConsultation != null && prescriptionController != null) {
            prescriptionController.setConsultation(selectedConsultation);
            prescriptionController.loadExistingPrescriptions();
        }
    }

    @FXML
    public void handlePrintAction(ActionEvent event) throws FileNotFoundException {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();

        Consultations selectedConsultation = null;
        List<ConsultationActe> selectedConsultationActes = new ArrayList<>();
        boolean isConsultationSelected = false;
        boolean isConsultationActeSelected = false;

        switch (buttonId) {
            case "btnOrdonnance":
                selectedConsultation = tvConsultationOrd.getSelectionModel().getSelectedItem();
                if (selectedConsultation != null) {
                    List<Prescriptions> prescriptions = prescriptionService.getPrescriptionByConsultation(selectedConsultation.getConsultationID());
                    ImpressionUtil.imprimerDocument(TypeImpression.ORDONNANCE, prescriptions, this.patient, this.medecin);
                    isConsultationSelected = true;
                }
                break;

            case "btnOrdonnanceBilan":
                selectedConsultation = lvDateConsultationBilan.getSelectionModel().getSelectedItem();
                if (selectedConsultation != null) {
                    List<Analyse> bilans = analyseService.getAnalysesByConsultationId(selectedConsultation.getConsultationID());
                    ImpressionUtil.imprimerDocument(TypeImpression.ORDONNANCE_BILAN, bilans, this.patient, this.medecin);
                    isConsultationSelected = true;
                }
                break;

            case "btnOrdonnanceImagerie":
                selectedConsultation = lvDateConsultationRadio.getSelectionModel().getSelectedItem();
                if (selectedConsultation != null) {
                    List<Imagerie> imageries = imagerieService.findByConsultationId(selectedConsultation.getConsultationID());
                    ImpressionUtil.imprimerDocument(TypeImpression.ORDONNANCE_IMAGERIE, imageries, this.patient, this.medecin);
                    isConsultationSelected = true;
                }
                break;

            case "btnPrintFacture":
                // Obtenir tous les éléments sélectionnés
                for (ConsultationActe acte : tvActes.getItems()) {
                    if (acte.isSelected()) {
                        selectedConsultationActes.add(acte);
                    }
                }
                if (!selectedConsultationActes.isEmpty()) {
                    // Imprimer la facture basée sur les éléments sélectionnés
                    ImpressionUtil.imprimerFacture(TypeImpression.FACTURE, selectedConsultationActes, this.patient, this.medecin);
                    isConsultationActeSelected = true;
                }
                break;

            default:
                System.out.println("Aucun cas correspondant trouvé pour l'ID: " + buttonId);
                break;
        }

        // Si aucune consultation ou acte n'est sélectionné, afficher une alerte
        if (!isConsultationSelected && !isConsultationActeSelected) {
            afficherAlerte("Sélectionner une Consultation", "Veuillez sélectionner une consultation ou un acte dans l'onglet correspondant avant d'imprimer.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private DashboardMedecinController dashboardMedecinController;

    public void setDashboardController(DashboardMedecinController dashboardMedecinController) {
        this.dashboardMedecinController = dashboardMedecinController;
    }

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.medecin = utilisateur.getMedecin();

        System.out.println("Dossier controlleur " + this.utilisateur.toString());
        System.out.println(this.medecin.toString());
        // Vous pouvez maintenant utiliser l'objet utilisateur pour initialiser les données de la fenêtre
    }

    @FXML
    private void ajouterRendezVous(ActionEvent event) {
        try {
            // Charger la vue ajoutRendezVous-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/ajoutRendezVous-view.fxml"));
            Parent root = loader.load();
            // Obtenir le contrôleur de la vue AjoutRendezVousController
            AjoutRendezVousController ajoutRendezVousController = loader.getController();
            // Passer les objets Patient et Medecin au contrôleur
            // Obtenir le contrôleur de la vue AjoutRendezVousController
            // Passer les objets Patient, Medecin, et RendezVous au contrôleur
            ajoutRendezVousController.setPatient(this.patient);
            ajoutRendezVousController.setMedecin(this.medecin);
            ajoutRendezVousController.setDossierController(this);
            // Créer une nouvelle fenêtre pour le modal
            Stage stage = new Stage();
            stage.setTitle("Ajouter Rendez-Vous");
            stage.initModality(Modality.APPLICATION_MODAL); // Définit le stage comme un modal
            stage.setScene(new Scene(root));
            // Lier le stage modal à la fenêtre principale (si nécessaire)
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.initOwner(primaryStage);
            // Désactiver la possibilité d'agrandir la fenêtre
            stage.setResizable(false);
            stage.showAndWait(); // Afficher la fenêtre et attendre sa fermeture avant de revenir à la fenêtre principale
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editRendezVous(ActionEvent event) {
        // Supposons que vous avez sélectionné un rendez-vous dans la TableView
        RendezVous selectedRendezVous = tvRendezVous.getSelectionModel().getSelectedItem();

        if (selectedRendezVous != null) {
            try {
                // Charger la vue ajoutRendezVous-view.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/ajoutRendezVous-view.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de la vue AjoutRendezVousController
                AjoutRendezVousController ajoutRendezVousController = loader.getController();

                // Passer les objets Patient, Medecin, et RendezVous au contrôleur
                ajoutRendezVousController.setPatient(this.patient);
                ajoutRendezVousController.setMedecin(this.medecin);
                ajoutRendezVousController.setRendezVous(selectedRendezVous);
                ajoutRendezVousController.setDossierController(this);

                // Créer une nouvelle fenêtre pour le modal
                Stage stage = new Stage();
                stage.setTitle("Éditer Rendez-Vous");
                stage.initModality(Modality.APPLICATION_MODAL); // Définit le stage comme un modal
                stage.setScene(new Scene(root));
                // Lier le stage modal à la fenêtre principale (si nécessaire)
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initOwner(primaryStage);

                // Désactiver la possibilité d'agrandir la fenêtre
                stage.setResizable(false);
                stage.showAndWait(); // Afficher la fenêtre et attendre sa fermeture avant de revenir à la fenêtre principale

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Afficher une alerte si aucun rendez-vous n'est sélectionné
            showAlert(Alert.AlertType.WARNING, "Aucun Rendez-Vous Sélectionné", "Veuillez sélectionner un rendez-vous à éditer.");
        }
    }

    @FXML
    private void deleteRendezVous(ActionEvent event) {
        RendezVous selectedRendezVous = tvRendezVous.getSelectionModel().getSelectedItem();

        if (selectedRendezVous != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le rendez-vous");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce rendez-vous ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean success = rendezVousService.deleteRendezVous(selectedRendezVous.getRendezVousID());

                    if (success) {
                        tvRendezVous.getItems().remove(selectedRendezVous);
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Rendez-vous supprimé avec succès.");

                        // Réinitialiser l'objet this.rendezVous après suppression
                        this.rendezVous = null;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression du rendez-vous.");
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression du rendez-vous.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner un rendez-vous à supprimer.");
        }
    }

    public void loadRendezVousData() {
        // Charger les rendez-vous depuis le service
        rendezVousList.setAll(rendezVousService.findRendezVousByPatient(this.patient.getPatientID()));
        // Assigner la liste observable à la TableView
        tvRendezVous.setItems(rendezVousList);
    }

    private void showActeDialog(Consultations consultation) {
        try {
            // Charger la vue FXML pour la boîte de dialogue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/acte/acte-view.fxml"));
            Parent root = loader.load();

            // Si nécessaire, vous pouvez passer l'objet consultation au contrôleur de la boîte de dialogue
            ActeController controller = loader.getController();
            controller.setConsultation(consultation); // Si vous avez besoin de passer des données
            controller.setPatient(this.patient);
            controller.setDossierController(this);

            // Créer et afficher la boîte de dialogue
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Détails de l'acte");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tvConsultationPaiement.getScene().getWindow());
            dialogStage.setResizable(false);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la boîte de dialogue.");
        }
    }

    // ==================== MÉTHODES D'IMPRESSION DES CERTIFICATS ====================
    
    @FXML
    private Button btnCertificatConsultation;
    @FXML
    private Button btnCertificatAptitudeSportive;
    @FXML
    private Button btnCertificatArretScolaire;
    @FXML
    private Button btnCertificatArretTravail;
    @FXML
    private Button btnCertificatMaladieChronique;
    @FXML
    private Button btnFicheSoinsLocaux;
    @FXML
    private Button btnLettreOrientation;

    @FXML
    private void handlePrintCertificatConsultation(ActionEvent event) {
        if (patient == null) {
            showAlert(AlertType.WARNING, "Avertissement", "Veuillez sélectionner un patient.");
            return;
        }
        
        // Essayer d'obtenir la consultation sélectionnée, sinon utiliser la dernière consultation
        Consultations selectedConsultation = tvConsultation.getSelectionModel().getSelectedItem();
        
        // Si aucune consultation n'est sélectionnée, prendre la dernière de la liste
        if (selectedConsultation == null && tvConsultation.getItems() != null && !tvConsultation.getItems().isEmpty()) {
            selectedConsultation = tvConsultation.getItems().get(tvConsultation.getItems().size() - 1);
        }

        try {
            String motifConsultation = "";
            String dateConsultation = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            if (selectedConsultation != null) {
                motifConsultation = selectedConsultation.getSymptome();
                if (selectedConsultation.getDateConsultation() != null) {
                    dateConsultation = selectedConsultation.getDateConsultation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
            }
            
            String certificatText = "Je soussigné Dr " + medecin.getNom() + " " + medecin.getPrenom() 
                + ", certifie avoir examiné " + patient.getSexe().getDescription() + " " + patient.getNom() + " " + patient.getPrenom()
                + " le " + dateConsultation + ".";
            
            if (!motifConsultation.isEmpty()) {
                certificatText += "\n\nMotif de consultation : " + motifConsultation;
            }
            
            certificatText += "\n\nCertificat établi à la demande de l'intéressé(e) pour faire valoir ce que de droit.";
            
            String pdfPath = com.azmicro.moms.util.impression.PdfGenerator.generateCertificatConsultationPdf(
                certificatText, patient, medecin
            );
            
            com.azmicro.moms.util.impression.PdfViewer.openPdf(pdfPath);
            showAlert(AlertType.INFORMATION, "Succès", "Certificat médical généré avec succès.", 
                     btnCertificatConsultation.getScene().getWindow());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération du certificat.",
                     btnCertificatConsultation.getScene().getWindow());
        }
    }

    @FXML
    private void handlePrintCertificatAptitudeSportive(ActionEvent event) {
        if (patient == null) {
            showAlert(AlertType.WARNING, "Avertissement", "Veuillez sélectionner un patient.");
            return;
        }

        try {
            String certificatText = "Je soussigné Dr " + medecin.getNom() + " " + medecin.getPrenom() 
                + ", certifie que " + patient.getSexe().getDescription() + " " + patient.getNom() + " " + patient.getPrenom()
                + ", né(e) le " + patient.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + ", ne présente aucune contre-indication à la pratique du sport."
                + "\n\nCertificat établi le " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " à la demande de l'intéressé(e) pour faire valoir ce que de droit.";
            
            String pdfPath = com.azmicro.moms.util.impression.PdfGenerator.generateCertificatAptitudeSportivePdf(
                certificatText, patient, medecin
            );
            
            com.azmicro.moms.util.impression.PdfViewer.openPdf(pdfPath);
            showAlert(AlertType.INFORMATION, "Succès", "Certificat d'aptitude sportive généré avec succès.",
                     btnCertificatAptitudeSportive.getScene().getWindow());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération du certificat.",
                     btnCertificatAptitudeSportive.getScene().getWindow());
        }
    }

    @FXML
    private void handlePrintCertificatArretScolaire(ActionEvent event) {
        if (patient == null) {
            showAlert(AlertType.WARNING, "Avertissement", "Veuillez sélectionner un patient.");
            return;
        }

        // Créer un dialogue pour saisir les dates
        Dialog<LocalDate[]> dialog = new Dialog<>();
        dialog.setTitle("Certificat d'Arrêt Scolaire");
        dialog.setHeaderText("Veuillez saisir la période d'arrêt scolaire");

        // Configurer les boutons
        ButtonType validerButtonType = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, ButtonType.CANCEL);

        // Créer les champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker dateDebutPicker = new DatePicker(LocalDate.now());
        DatePicker dateFinPicker = new DatePicker(LocalDate.now().plusDays(3));

        grid.add(new Label("Date de début:"), 0, 0);
        grid.add(dateDebutPicker, 1, 0);
        grid.add(new Label("Date de fin:"), 0, 1);
        grid.add(dateFinPicker, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validerButtonType) {
                return new LocalDate[]{dateDebutPicker.getValue(), dateFinPicker.getValue()};
            }
            return null;
        });

        Optional<LocalDate[]> result = dialog.showAndWait();
        
        result.ifPresent(dates -> {
            LocalDate dateDebut = dates[0];
            LocalDate dateFin = dates[1];
            
            if (dateFin.isBefore(dateDebut)) {
                showAlert(AlertType.ERROR, "Erreur", "La date de fin doit être après la date de début.",
                         btnCertificatArretScolaire.getScene().getWindow());
                return;
            }

            try {
                String certificatText = "Je soussigné Dr " + medecin.getNom() + " " + medecin.getPrenom() 
                    + ", certifie que l'état de santé de " + patient.getNom() + " " + patient.getPrenom()
                    + " nécessite un arrêt scolaire"
                    + "\ndu " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " inclus."
                    + "\n\nCertificat établi à la demande de l'intéressé(e) pour faire valoir ce que de droit.";
                
                String pdfPath = com.azmicro.moms.util.impression.PdfGenerator.generateCertificatArretScolairePdf(
                    certificatText, patient, medecin
                );
                
                com.azmicro.moms.util.impression.PdfViewer.openPdf(pdfPath);
                showAlert(AlertType.INFORMATION, "Succès", "Certificat d'arrêt scolaire généré avec succès.",
                         btnCertificatArretScolaire.getScene().getWindow());
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération du certificat.",
                         btnCertificatArretScolaire.getScene().getWindow());
            }
        });
    }

    @FXML
    private void handlePrintCertificatArretTravail(ActionEvent event) {
        if (patient == null) {
            showAlert(AlertType.WARNING, "Avertissement", "Veuillez sélectionner un patient.");
            return;
        }

        // Créer un dialogue pour saisir les dates
        Dialog<LocalDate[]> dialog = new Dialog<>();
        dialog.setTitle("Certificat d'Arrêt de Travail");
        dialog.setHeaderText("Veuillez saisir la période d'arrêt de travail");

        // Configurer les boutons
        ButtonType validerButtonType = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, ButtonType.CANCEL);

        // Créer les champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker dateDebutPicker = new DatePicker(LocalDate.now());
        DatePicker dateFinPicker = new DatePicker(LocalDate.now().plusDays(7));

        grid.add(new Label("Date de début:"), 0, 0);
        grid.add(dateDebutPicker, 1, 0);
        grid.add(new Label("Date de fin:"), 0, 1);
        grid.add(dateFinPicker, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == validerButtonType) {
                return new LocalDate[]{dateDebutPicker.getValue(), dateFinPicker.getValue()};
            }
            return null;
        });

        Optional<LocalDate[]> result = dialog.showAndWait();
        
        result.ifPresent(dates -> {
            LocalDate dateDebut = dates[0];
            LocalDate dateFin = dates[1];
            
            if (dateFin.isBefore(dateDebut)) {
                showAlert(AlertType.ERROR, "Erreur", "La date de fin doit être après la date de début.",
                         btnCertificatArretTravail.getScene().getWindow());
                return;
            }

            try {
                String certificatText = "Je soussigné Dr " + medecin.getNom() + " " + medecin.getPrenom() 
                    + ", certifie que l'état de santé de " + patient.getSexe().getDescription() + " " + patient.getNom() + " " + patient.getPrenom()
                    + " nécessite un arrêt de travail"
                    + "\ndu " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " au " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " inclus."
                    + "\n\nSauf complication, la reprise du travail pourra avoir lieu le "
                    + dateFin.plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "."
                    + "\n\nCertificat établi à la demande de l'intéressé(e) pour faire valoir ce que de droit.";
                
                String pdfPath = com.azmicro.moms.util.impression.PdfGenerator.generateCertificatArretTravailPdf(
                    certificatText, patient, medecin
                );
                
                com.azmicro.moms.util.impression.PdfViewer.openPdf(pdfPath);
                showAlert(AlertType.INFORMATION, "Succès", "Certificat d'arrêt de travail généré avec succès.",
                         btnCertificatArretTravail.getScene().getWindow());
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération du certificat.",
                         btnCertificatArretTravail.getScene().getWindow());
            }
        });
    }

    @FXML
    private void handlePrintCertificatMaladieChronique(ActionEvent event) {
        if (patient == null) {
            showAlert(AlertType.WARNING, "Avertissement", "Veuillez sélectionner un patient.");
            return;
        }

        try {
            String certificatText = "Je soussigné Dr " + medecin.getNom() + " " + medecin.getPrenom() 
                + ", certifie que " + patient.getSexe().getDescription() + " " + patient.getNom() + " " + patient.getPrenom()
                + " est atteint(e) d'une affection de longue durée nécessitant un traitement prolongé et une thérapeutique particulièrement coûteuse."
                + "\n\nCe certificat est établi conformément aux dispositions réglementaires en vigueur."
                + "\n\nCertificat établi le " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " à la demande de l'intéressé(e) pour faire valoir ce que de droit.";
            
            String pdfPath = com.azmicro.moms.util.impression.PdfGenerator.generateCertificatMaladieChroniquePdf(
                certificatText, patient, medecin
            );
            
            com.azmicro.moms.util.impression.PdfViewer.openPdf(pdfPath);
            showAlert(AlertType.INFORMATION, "Succès", "Certificat maladie chronique généré avec succès.",
                     btnCertificatMaladieChronique.getScene().getWindow());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération du certificat.",
                     btnCertificatMaladieChronique.getScene().getWindow());
        }
    }

    @FXML
    private void handlePrintFicheSoinsLocaux(ActionEvent event) {
        if (patient == null) {
            showAlert(AlertType.WARNING, "Avertissement", "Veuillez sélectionner un patient.");
            return;
        }

        try {
            String ficheText = "FICHE DE SOINS LOCAUX"
                + "\n\nPatient : " + patient.getNom() + " " + patient.getPrenom()
                + "\nDate de naissance : " + patient.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + "\nÂge : " + patient.getAge() + " ans"
                + "\n\nPrescription de soins locaux :"
                + "\n- Pansements à renouveler quotidiennement"
                + "\n- Désinfection locale"
                + "\n- Surveillance de l'évolution"
                + "\n\nDurée des soins : À poursuivre selon évolution"
                + "\n\nFait le " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            String pdfPath = com.azmicro.moms.util.impression.PdfGenerator.generateFicheSoinsLocauxPdf(
                ficheText, patient, medecin
            );
            
            com.azmicro.moms.util.impression.PdfViewer.openPdf(pdfPath);
            showAlert(AlertType.INFORMATION, "Succès", "Fiche de soins locaux générée avec succès.",
                     btnFicheSoinsLocaux.getScene().getWindow());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération de la fiche.",
                     btnFicheSoinsLocaux.getScene().getWindow());
        }
    }

    @FXML
    private void handlePrintLettreOrientation(ActionEvent event) {
        if (patient == null) {
            showAlert(AlertType.WARNING, "Avertissement", "Veuillez sélectionner un patient.");
            return;
        }

        try {
            String lettreText = "LETTRE D'ORIENTATION"
                + "\n\nÀ l'attention du confrère spécialiste,"
                + "\n\nJe vous adresse " + patient.getSexe().getDescription() + " " + patient.getNom() + " " + patient.getPrenom()
                + ", né(e) le " + patient.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + ", pour avis spécialisé et prise en charge."
                + "\n\nMotif de la consultation : [À compléter]"
                + "\n\nAntécédents notables : [À compléter]"
                + "\n\nTraitement en cours : [À compléter]"
                + "\n\nJe vous remercie de bien vouloir me tenir informé(e) de vos conclusions."
                + "\n\nAvec mes confraternels salutations."
                + "\n\nFait le " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            String pdfPath = com.azmicro.moms.util.impression.PdfGenerator.generateLettreOrientationPdf(
                lettreText, patient, medecin
            );
            
            com.azmicro.moms.util.impression.PdfViewer.openPdf(pdfPath);
            showAlert(AlertType.INFORMATION, "Succès", "Lettre d'orientation générée avec succès.",
                     btnLettreOrientation.getScene().getWindow());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la génération de la lettre.",
                     btnLettreOrientation.getScene().getWindow());
        }
    }

}
