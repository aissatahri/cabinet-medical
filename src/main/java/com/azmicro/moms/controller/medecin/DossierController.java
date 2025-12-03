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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
        try {
            this.consultationService = new ConsultationService(DatabaseUtil.getConnection());
        } catch (SQLException ex) {
            Logger.getLogger(DossierController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO
        HistoriqueMedicalDAO historiqueMedicalDAO;
        try {
            historiqueMedicalDAO = new HistoriqueMedicalDAOImpl(new PatientDAOImpl(DatabaseUtil.getConnection()));
            this.historiqueMedicalService = new HistoriqueMedicalService(historiqueMedicalDAO);
            consultationService = new ConsultationService(DatabaseUtil.getConnection());
            this.analyseService = new AnalyseService(new AnalyseDAOImpl(DatabaseUtil.getConnection()));
            this.imagerieService = new ImagerieService();
            this.prescriptionService = new PrescriptionService();
            this.rendezVousService = new RendezVousService();
            this.paiementsService = new PaiementsService();
            this.consultationActeService = new ConsultationActeService();
        } catch (SQLException ex) {
            Logger.getLogger(FichePatientDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        dateConsultaion.setValue(LocalDate.now());
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
        tfPoid.textProperty().addListener((observable, oldValue, newValue) -> updateIMC());
        tfTaille.textProperty().addListener((observable, oldValue, newValue) -> updateIMC());
        setupPressionArterielleField();

        clmDetailsConsultation.setCellValueFactory(new PropertyValueFactory<>("symptome"));
        clmDateConsultation.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));

        //
        tvConsultation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });

        // Exemple de configuration avec une CellFactory
        clmLigneBilan.setCellValueFactory(new PropertyValueFactory<>("typeAnalyse"));

        clmLigneBilan.setCellFactory(column -> new TableCell<Analyse, TypeAnalyse>() {
            @Override
            protected void updateItem(TypeAnalyse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Assurez-vous que toString() ou une autre méthode appropriée est utilisée pour afficher la valeur
                    setText(item.getCodeAnalyseFr());
                }
            }
        });

        // Ajouter un listener pour détecter les changements de sélection dans la TableView
        tvBilan.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Analyse>() {
            @Override
            public void changed(ObservableValue<? extends Analyse> observable, Analyse oldValue, Analyse newValue) {
                if (newValue != null) {
                    // Charger les contrôles avec les données de l'analyse sélectionnée
                    dpDateAnalyse.setValue(newValue.getDateAnalyse());
                    txtRsltLigneBilan.setText(newValue.getResultat());
                }
            }
        });

        tvBilan.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tvBilan.getSelectionModel().getSelectedItem() != null) {
                // Récupérer l'analyse sélectionnée
                Analyse selectedAnalyse = tvBilan.getSelectionModel().getSelectedItem();
                // Charger les contrôles avec les données de l'analyse sélectionnée
                dpDateAnalyse.setValue(selectedAnalyse.getDateAnalyse());
                txtRsltLigneBilan.setText(selectedAnalyse.getResultat());
            }
        });

        // Associer la colonne avec la propriété du modèle
        clmLigneImagerie.setCellValueFactory(new PropertyValueFactory<>("TypeImagerie"));
        clmLigneImagerie.setCellFactory(column -> new TableCell<Imagerie, TypeImagerie>() {
            @Override
            protected void updateItem(TypeImagerie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Assurez-vous que toString() ou une autre méthode appropriée est utilisée pour afficher la valeur
                    setText(item.getNomImagerieFr());
                }
            }
        });

        // Ajouter un listener pour détecter les changements de sélection dans la TableView
        tvRadio.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Imagerie>() {

            @Override
            public void changed(ObservableValue<? extends Imagerie> observable, Imagerie oldValue, Imagerie newValue) {
                if (newValue != null) {
                    // Charger les contrôles avec les données de l'analyse sélectionnée
                    dpDateImagerie.setValue(newValue.getDateImagerie());
                    txtresultImagerie.setText(newValue.getResultat());
                }
            }
        });

        tvRadio.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tvRadio.getSelectionModel().getSelectedItem() != null) {
                // Récupérer l'analyse sélectionnée
                Imagerie selectedImagerie = tvRadio.getSelectionModel().getSelectedItem();
                // Charger les contrôles avec les données de l'analyse sélectionnée
                dpDateImagerie.setValue(selectedImagerie.getDateImagerie());
                txtresultImagerie.setText(selectedImagerie.getResultat());
            }
        });

        lvDateConsultationBilan.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Consultations>() {
            @Override
            public void changed(ObservableValue<? extends Consultations> observable, Consultations oldValue, Consultations newValue) {
                if (newValue != null) {
                    loadAnalyses(newValue);
                }
            }
        });

        lvDateConsultationRadio.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Consultations>() {
            @Override
            public void changed(ObservableValue<? extends Consultations> observable, Consultations oldValue, Consultations newValue) {
                if (newValue != null) {
                    loadImagerie(newValue);
                }
            }
        });

        clmDateConsultationOrd.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        clmMotifConsultationOrd.setCellValueFactory(new PropertyValueFactory<>("symptome"));

        // Optional: Set custom cell factories if needed (e.g., formatting date)
        clmDateConsultationOrd.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                }
            }
        });

        // Set up listener for selection changes in tvConsultationOrd
        tvConsultationOrd.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Fetch prescriptions based on selected consultation
                System.out.println(newValue.toString());
                loadPrescriptionsForConsultation(newValue);
            }
        });
        // Ajoutez un ChangeListener pour la sélection dans tvConsultationOrd
        tvConsultationOrd.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Consultations>() {
            @Override
            public void changed(ObservableValue<? extends Consultations> observable, Consultations oldValue, Consultations newValue) {
                if (newValue != null) {
                    loadPrescriptionsForConsultation(newValue);
                }
            }
        });

        // Initialize the medicament column
        clmMedicament.setCellValueFactory(cellData
                -> new SimpleObjectProperty<>(cellData.getValue().getMedicament())
        );
        clmMedicament.setCellFactory(column -> new TableCell<Prescriptions, Medicaments>() {
            @Override
            protected void updateItem(Medicaments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomMedicament()); // Assuming Medicaments has a getName() method
                }
            }
        });

        // Initialize the details column
        clmDetailsOrdonnance.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDose() + " / " + cellData.getValue().getDuree())
        );

        rendezVousList = FXCollections.observableArrayList();

        // Configurer les colonnes de la TableView
        clmTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        clmHourStart.setCellValueFactory(new PropertyValueFactory<>("hourStart"));
        clmHourEnd.setCellValueFactory(new PropertyValueFactory<>("hourEnd"));

        tvRendezVous.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            this.rendezVous = newSelection;
        });

//        clmDatePayment.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
//        clmDatePayment.setCellFactory(column -> new TableCell<>() {
//            @Override
//            protected void updateItem(LocalDate date, boolean empty) {
//                super.updateItem(date, empty);
//                if (empty || date == null) {
//                    setText(null);
//                } else {
//                    setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//                }
//            }
//        });
        // Lier les colonnes aux propriétés de la classe Paiements
        clmDatePayment.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        clmMontantPayement.setCellValueFactory(new PropertyValueFactory<>("montant"));
        clmVersementPayment.setCellValueFactory(new PropertyValueFactory<>("versment"));
        clmRestePayment.setCellValueFactory(new PropertyValueFactory<>("reste"));

        // Set up listener for selection changes in tvConsultationOrd
        tvConsultationPaiement.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Fetch prescriptions based on selected consultation
                System.out.println(newValue.toString());
                //loadPrescriptionsForConsultation(newValue.getConsultation());
                loadDataIntoTableView(newValue.getConsultation());
            }
        });
        // Ajoutez un ChangeListener pour la sélection dans tvConsultationOrd
        tvConsultationPaiement.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Paiements>() {
            @Override
            public void changed(ObservableValue<? extends Paiements> observable, Paiements oldValue, Paiements newValue) {
                if (newValue != null) {
                    //loadPrescriptionsForConsultation(newValue.getConsultation());
                    loadDataIntoTableView(newValue.getConsultation());
                }
            }
        });

        // Ajout d'un gestionnaire d'événements pour détecter le double-clic sur la TableView
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
        // Configure the 'Acte' column to display the name of the Acte
        // Colonne pour afficher le nom de l'acte
        clmActe.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActe().getNomActe()));

// Colonne pour afficher le montant de l'acte
        clmMontantActe.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getActe().getPrix()).asObject());

// Colonne pour les cases à cocher
        TableColumn<ConsultationActe, Boolean> selectionClm = new TableColumn<>("Sélectionner");

// Utiliser un CellFactory personnalisé pour créer des CheckBox
        selectionClm.setCellFactory(column -> new TableCell<ConsultationActe, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            private final HBox hbox = new HBox(checkBox);

            {
                // Centrer le CheckBox dans l'HBox
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(5));
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Récupérer l'élément actuel pour cette ligne
                    ConsultationActe acte = getTableView().getItems().get(getIndex());

                    // Décocher le CheckBox avant de le lier à la nouvelle ligne
                    checkBox.setSelected(acte.isSelected());

                    // Ajouter un Listener pour changer la valeur de l'objet ConsultationActe
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        acte.setSelected(newValue);
                    });

                    // Afficher le CheckBox dans la cellule
                    // Afficher le CheckBox dans la cellule
                    setGraphic(hbox);
                }
            }
        });

// Ajouter la colonne à la TableView
        tvActes.getColumns().add(selectionClm);

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
                // Convertir la saisie utilisateur en nombre entier
                Double glycemyLevel = Double.parseDouble(newValue);

                // Classifier la glycémie
                String classification = classifyGlycemiaFastingInGl(glycemyLevel);

                // Afficher le résultat dans un tooltip
                Tooltip tooltip = new Tooltip(classification);
                tfGlycimie.setTooltip(tooltip);
            } catch (NumberFormatException e) {
                tfGlycimie.setTooltip(new Tooltip("Veuillez entrer une valeur numérique valide en g/L."));
            }
        });
    }

    private String classifyGlycemiaFastingInGl(Double glycemyLevel) {
        // Conversion des niveaux en g/L
        if (glycemyLevel < 0.7) {
            return "Glycémie inférieure à la normale";
        } else if (glycemyLevel <= 0.99) {
            return "Glycémie normale à jeun";
        } else if (glycemyLevel <= 1.25) {
            return "Pré-diabète à jeun";
        } else {
            return "Diabète à jeun";
        }
    }

    private void setupSaturationOxygenField() {
        tfSaOpatient.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int spo2 = Integer.parseInt(newValue);
                String classification = classifyOxygenSaturation(spo2);
                Tooltip tooltip = new Tooltip(classification);
                tfSaOpatient.setTooltip(tooltip);
            } catch (NumberFormatException e) {
                tfSaOpatient.setTooltip(new Tooltip("Veuillez entrer une valeur numérique valide."));
            }
        });

    }

    private String classifyOxygenSaturation(int spo2) {
        if (spo2 >= 95) {
            return "Saturation normale";
        } else if (spo2 >= 91) {
            return "Hypoxémie légère";
        } else if (spo2 >= 86) {
            return "Hypoxémie modérée";
        } else {
            return "Hypoxémie sévère";
        }
    }

    private void setupFrequenceRespiratoireField() {
        tfFrequenceRespiratoire.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < oldValue.length()) {
                // Si l'utilisateur appuie sur 'Backspace', on ne modifie rien
                return;
            }

            // Retirer tous les caractères non numériques
            String cleanText = newValue.replaceAll("[^\\d]", "");

            if (cleanText.length() > 2) {
                cleanText = cleanText.substring(0, 2); // Limiter la longueur à 2 caractères
            }

            // Éviter la boucle infinie en ne mettant à jour le texte que s'il a réellement changé
            if (!cleanText.equals(tfFrequenceRespiratoire.getText())) {
                tfFrequenceRespiratoire.setText(cleanText);
                tfFrequenceRespiratoire.positionCaret(cleanText.length());
            }

            // Ajouter la classification en fonction de la fréquence respiratoire saisie
            if (!cleanText.isEmpty()) {
                int frequence = Integer.parseInt(cleanText);

                String classification = classifyRespiratoryRate(frequence, patient);

                Tooltip tooltip = new Tooltip(classification);
                tfFrequenceRespiratoire.setTooltip(tooltip);
            } else {
                tfFrequenceRespiratoire.setTooltip(null); // Supprime le tooltip si l'entrée est vide
            }
        });
    }

    /**
     * Classifie la fréquence respiratoire selon la valeur entrée et l'âge du
     * patient.
     *
     * @param frequence la fréquence respiratoire
     * @param age l'âge du patient
     * @return la classification correspondante
     */
    private String classifyRespiratoryRate(int frequence, Patient patient) {
        int ageInMonths = patient.getAgeInMonths();
        int ageInYears = patient.getAgeInYears();

        if (ageInMonths <= 12) {
            if (frequence >= 30 && frequence <= 60) {
                return "Fréquence respiratoire normale pour un nourrisson";
            }
        } else if (ageInYears <= 3) {
            if (frequence >= 24 && frequence <= 40) {
                return "Fréquence respiratoire normale pour un jeune enfant";
            }
        } else if (ageInYears <= 6) {
            if (frequence >= 22 && frequence <= 34) {
                return "Fréquence respiratoire normale pour un enfant d'âge préscolaire";
            }
        } else if (ageInYears <= 12) {
            if (frequence >= 18 && frequence <= 30) {
                return "Fréquence respiratoire normale pour un enfant d'âge scolaire";
            }
        } else if (ageInYears <= 18) {
            if (frequence >= 12 && frequence <= 20) {
                return "Fréquence respiratoire normale pour un adolescent";
            }
        } else {
            if (frequence >= 12 && frequence <= 20) {
                return "Fréquence respiratoire normale pour un adulte";
            }
        }

        // Si la fréquence n'est pas dans la plage normale
        if (frequence > 20) {
            return "Tachypnée (fréquence respiratoire élevée)";
        } else if (frequence < 12) {
            return "Bradypnée (fréquence respiratoire basse)";
        } else {
            return "Classification non disponible";
        }
    }

    private void setupPressionArterielleField() {
        tfPressionPatient.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < oldValue.length()) {
                // Si l'utilisateur appuie sur 'Backspace', on ne modifie rien
                return;
            }

            // Retirer tous les caractères non numériques
            String cleanText = newValue.replaceAll("[^\\d]", "");

            if (cleanText.length() > 5) {
                cleanText = cleanText.substring(0, 5); // Limiter la longueur totale à 5 caractères
            }

            String formattedText;
            if (cleanText.length() > 3) {
                // Ajouter la barre oblique après les trois premiers chiffres
                formattedText = cleanText.substring(0, 3) + "/" + cleanText.substring(3);
            } else {
                formattedText = cleanText; // Pas assez de chiffres pour ajouter la barre oblique
            }

            // Éviter la boucle infinie en ne mettant à jour le texte que s'il a réellement changé
            if (!formattedText.equals(tfPressionPatient.getText())) {
                int caretPosition = tfPressionPatient.getCaretPosition(); // Position actuelle du curseur

                tfPressionPatient.setText(formattedText);

                // Calcul de la nouvelle position du curseur
                if (caretPosition <= 3) {
                    tfPressionPatient.positionCaret(caretPosition);
                } else if (caretPosition <= formattedText.length()) {
                    tfPressionPatient.positionCaret(caretPosition + 1); // Ajustement pour le slash
                } else {
                    tfPressionPatient.positionCaret(formattedText.length()); // Positionner le curseur à la fin du texte valide
                }
            }

            // Ajouter la classification en fonction de la pression artérielle saisie
            if (cleanText.length() == 5) { // On s'assure que les deux valeurs sont présentes
                int systolic = Integer.parseInt(cleanText.substring(0, 3));  // Extrait la valeur systolique
                int diastolic = Integer.parseInt(cleanText.substring(3));    // Extrait la valeur diastolique

                String classification = classifyBloodPressure(systolic, diastolic);

                Tooltip tooltip = new Tooltip(classification);
                tfPressionPatient.setTooltip(tooltip);
            } else {
                tfPressionPatient.setTooltip(null); // Supprime le tooltip si l'entrée est incomplète
            }
        });
    }

    /**
     * Classifie la pression artérielle selon les valeurs systolique et
     * diastolique.
     *
     * @param systolic la pression artérielle systolique
     * @param diastolic la pression artérielle diastolique
     * @return la classification correspondante
     */
    private String classifyBloodPressure(int systolic, int diastolic) {
        if (systolic < 120 && diastolic < 80) {
            return "Pression artérielle optimale";
        } else if (systolic >= 120 && systolic <= 129 && diastolic >= 80 && diastolic <= 84) {
            return "Pression artérielle normale";
        } else if (systolic >= 130 && systolic <= 139 && diastolic >= 85 && diastolic <= 89) {
            return "Pression artérielle normale haute";
        } else if (systolic >= 140 && systolic <= 159 && diastolic >= 90 && diastolic <= 99) {
            return "Hypertension de stade 1 (légère)";
        } else if (systolic >= 160 && systolic <= 179 && diastolic >= 100 && diastolic <= 109) {
            return "Hypertension de stade 2 (modérée)";
        } else if (systolic >= 180 && diastolic >= 110) {
            return "Hypertension de stade 3 (sévère)";
        } else if (systolic >= 140 && diastolic < 90) {
            return "Hypertension systolique isolée";
        } else {
            return "Classification non disponible";
        }
    }

    private void setupFrequenceCardiaqueField() {
        tfFrquenceCardiaque.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int frequenceCardiaque = Integer.parseInt(newValue);
                    int age = patient.getAge(); // Assurez-vous d'avoir une méthode pour obtenir l'âge du patient
                    String sexe = patient.getSexe().getDescription(); // Assurez-vous d'avoir une méthode pour obtenir le sexe du patient

                    String interpretation = getInterpretation(age, sexe, frequenceCardiaque);
                    Tooltip tooltip = new Tooltip(interpretation);
                    tfFrquenceCardiaque.setTooltip(tooltip);
                } catch (NumberFormatException e) {
                    // Gérer le cas où la saisie n'est pas un entier
                    tfFrquenceCardiaque.setTooltip(new Tooltip("Veuillez entrer une valeur numérique valide."));
                }
            } else {
                tfFrquenceCardiaque.setTooltip(null);
            }
        });

    }

    public String getInterpretation(int age, String sexe, int frequenceCardiaque) {
        if (sexe.equalsIgnoreCase("Masculin")) {
            if (age >= 18 && age <= 25) {
                if (frequenceCardiaque >= 49 && frequenceCardiaque <= 55) {
                    return "Athlète";
                }
                if (frequenceCardiaque >= 56 && frequenceCardiaque <= 61) {
                    return "Excellente";
                }
                if (frequenceCardiaque >= 62 && frequenceCardiaque <= 65) {
                    return "Bonne";
                }
                if (frequenceCardiaque >= 66 && frequenceCardiaque <= 69) {
                    return "Au-dessus de la moyenne";
                }
                if (frequenceCardiaque >= 70 && frequenceCardiaque <= 73) {
                    return "Moyenne";
                }
                if (frequenceCardiaque >= 74 && frequenceCardiaque <= 81) {
                    return "En dessous de la moyenne";
                }
                if (frequenceCardiaque >= 82) {
                    return "Mauvaise";
                }
            }
            // Ajoutez des conditions pour les autres tranches d'âge
        } else if (sexe.equalsIgnoreCase("Féminin")) {
            if (age >= 18 && age <= 25) {
                if (frequenceCardiaque >= 56 && frequenceCardiaque <= 60) {
                    return "Athlète";
                }
                if (frequenceCardiaque >= 61 && frequenceCardiaque <= 65) {
                    return "Excellente";
                }
                if (frequenceCardiaque >= 66 && frequenceCardiaque <= 69) {
                    return "Bonne";
                }
                if (frequenceCardiaque >= 70 && frequenceCardiaque <= 73) {
                    return "Au-dessus de la moyenne";
                }
                if (frequenceCardiaque >= 74 && frequenceCardiaque <= 78) {
                    return "Moyenne";
                }
                if (frequenceCardiaque >= 79 && frequenceCardiaque <= 84) {
                    return "En dessous de la moyenne";
                }
                if (frequenceCardiaque >= 85) {
                    return "Mauvaise";
                }
            }
            // Ajoutez des conditions pour les autres tranches d'âge
        }
        return "Interprétation non disponible";
    }
// Méthode pour obtenir l'index de tranche d'âge

    private int getIndexForAge(int age) {
        if (age >= 18 && age <= 25) {
            return 0;
        }
        if (age >= 26 && age <= 35) {
            return 1;
        }
        if (age >= 36 && age <= 45) {
            return 2;
        }
        if (age >= 46 && age <= 55) {
            return 3;
        }
        if (age >= 56 && age <= 65) {
            return 4;
        }
        return 5; // 65+
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
        // Remplacer les virgules par des points avant d'analyser
        double poids = Double.parseDouble(tfPoid.getText().replace(",", "."));
        double taille = Double.parseDouble(tfTaille.getText().replace(",", ".")) / 100; // Convertir la taille de cm à m

        double imc = calculateIMC(poids, taille);
        tfImc.setText(String.format("%.2f", imc)); // Afficher l'IMC avec deux décimales

        this.consultation = new Consultations();
        consultation.setPoids(poids);
        consultation.setTaille(taille * 100); // Convertir la taille de m à cm
        consultation.setImc(imc);
        consultation.setFrequencequardiaque(Integer.parseInt(tfFrquenceCardiaque.getText().replace(",", ".")));
        consultation.setPression(tfPressionPatient.getText());
        consultation.setFrequencerespiratoire(Integer.parseInt(tfFrequenceRespiratoire.getText().replace(",", ".")));
        consultation.setGlycimie(Double.parseDouble(tfGlycimie.getText().replace(",", ".")));
        consultation.setTemperature(Double.parseDouble(tfTmperature.getText().replace(",", ".")));
        consultation.setSaO(Integer.parseInt(tfSaOpatient.getText().replace(",", ".")));
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
                double imc = calculateIMC(poids, tailleM);
                tfImc.setText(String.format("%.2f", imc));
                formatImcField(imc); // Appeler la méthode pour formater le champ tfImc
            } else {
                tfImc.setText("");
            }
        } catch (NumberFormatException e) {
            tfImc.setText("");
        }
    }

// Méthode pour calculer l'IMC
    private double calculateIMC(double poids, double taille) {
        return poids / (taille * taille);
    }

// Méthode pour formater le champ tfImc selon les normes
    private void formatImcField(double imc) {
        String interpretation;
        String style;

        if (imc < 18.5) {
            interpretation = "Insuffisance pondérale (maigreur)";
            style = "-fx-text-fill: blue;";
        } else if (imc >= 18.5 && imc < 25) {
            interpretation = "Corpulence normale";
            style = "-fx-text-fill: green;";
        } else if (imc >= 25 && imc < 30) {
            interpretation = "Surpoids";
            style = "-fx-text-fill: orange;";
        } else if (imc >= 30 && imc < 35) {
            interpretation = "Obésité modérée";
            style = "-fx-text-fill: darkorange;";
        } else if (imc >= 35 && imc < 40) {
            interpretation = "Obésité sévère";
            style = "-fx-text-fill: red;";
        } else {
            interpretation = "Obésité morbide ou massive";
            style = "-fx-text-fill: darkred;";
        }

        tfImc.setTooltip(new Tooltip(interpretation)); // Ajouter une infobulle avec l'interprétation
        tfImc.setStyle(style); // Appliquer le style au champ de texte
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
        selectedConsultation.setPoids(Double.parseDouble(tfPoid.getText().replace(",", ".")));
        selectedConsultation.setTaille(Double.parseDouble(tfTaille.getText().replace(",", ".")));
        selectedConsultation.setImc(Double.parseDouble(tfImc.getText().replace(",", ".")));
        selectedConsultation.setTemperature(Double.parseDouble(tfTmperature.getText().replace(",", ".")));
        selectedConsultation.setPression(tfPressionPatient.getText());
        selectedConsultation.setFrequencequardiaque(Integer.parseInt(tfFrquenceCardiaque.getText().replace(",", ".")));
        selectedConsultation.setFrequencerespiratoire(Integer.parseInt(tfFrequenceRespiratoire.getText().replace(",", ".")));
        selectedConsultation.setExamenClinique(txtExamenClinique.getText());
        selectedConsultation.setSymptome(txtSymptomes.getText());
        selectedConsultation.setDiagnostique(txtDiagnostiqueMedical.getText());
        selectedConsultation.setGlycimie(Double.parseDouble(tfGlycimie.getText().replace(",", ".")));
        selectedConsultation.setSaO(Integer.parseInt(tfSaOpatient.getText().replace(",", ".")));
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

}
