/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.controller.assistante.DashboardAssistanteController;
import com.azmicro.moms.controller.patient.FichePatientController;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.SituationFamiliale;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.PatientService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class PatientController implements Initializable {

    @FXML
    private TextField tfKeyword;
    @FXML
    private Button btnOpenFicheNewPatient;
    @FXML
    private Button btnPrint;
    @FXML
    private TableView<Patient> patientsTv;
    @FXML
    private TableColumn<Patient, String> dossierClm;
    @FXML
    private TableColumn<Patient, String> nomClm;
    @FXML
    private TableColumn<Patient, String> prenomClm;
    @FXML
    private TableColumn<Patient, LocalDate> birthClm;
    @FXML
    private TableColumn<Patient, String> telephoneClm;
    @FXML
    private TableColumn<Patient, String> sexeClm;
    @FXML
    private TableColumn<Patient, Integer> ageClm;
    @FXML
    private TableColumn<Patient, String> adresseClm;
    @FXML
    private TableColumn<Patient, String> situationFamilialClm;
    @FXML
    private TableColumn<Patient, Void> actionClm;
    private PatientService patientService;
    private static PatientController instance;

    private DashboardMedecinController dashboardMedecinController;

    public void setDashboardController(DashboardMedecinController dashboardMedecinController) {
        this.dashboardMedecinController = dashboardMedecinController;
    }

    public PatientController() {
        instance = this;
        this.patientService = patientService;
    }

    public static PatientController getInstance() {
        return instance;
    }

    private PatientController getPatientController() {
        // Implémentez le code pour obtenir l'instance du PatientController
        return instance;
    }

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        // Vous pouvez maintenant utiliser l'objet utilisateur pour initialiser les données de la fenêtre
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            // TODO
            this.patientService = new PatientService();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardAssistanteController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Définissez la cellule de la colonne d'action
        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                final TableCell<Patient, Void> cell = new TableCell<Patient, Void>() {

                    private final Button editButton = createIconButton("", "EDIT");
                    private final Button detailsButton = createIconButton("", "INFO_CIRCLE");
                    private final Button trashButton = createIconButton("", "TRASH");

                    {
                        editButton.setOnAction(event -> {
                            Patient patient = patientService.findByNumDossier(getTableView().getItems().get(getIndex()).getNumDossier());
                            System.out.println("edit button clicked for patient: " + patient.getPrenom());
                            try {
                                editerPatient(patient);
                            } catch (Exception ex) {
                                Logger.getLogger(DashboardAssistanteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        detailsButton.setOnAction(event -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            System.out.println("Details button clicked for patient: " + patient.getAge());
                            try {
                                detailsPatient(patient, detailsButton);
                            } catch (Exception ex) {
                                Logger.getLogger(DashboardAssistanteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        editButton.setOnMouseEntered(event -> {
                            editButton.setStyle("-fx-background-color: lightgray;"); // Changez le style lors du survol
                        });

                        // Ajoutez un écouteur d'événements pour le déplacement de la souris (pour restaurer l'aspect)
                        editButton.setOnMouseExited(event -> {
                            editButton.setStyle(""); // Réinitialisez le style pour restaurer l'apparence par défaut
                        });
                        detailsButton.setOnMouseEntered(event -> {
                            detailsButton.setStyle("-fx-background-color: lightgray;"); // Changez le style lors du survol
                        });

                        // Ajoutez un écouteur d'événements pour le déplacement de la souris (pour restaurer l'aspect)
                        detailsButton.setOnMouseExited(event -> {
                            detailsButton.setStyle(""); // Réinitialisez le style pour restaurer l'apparence par défaut
                        });
                        trashButton.setOnAction((event) -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            confirmerSuppressionPatient(patient);
                        });
                        trashButton.setOnMouseEntered(event -> {
                            trashButton.setStyle("-fx-background-color: lightgray;"); // Changez le style lors du survol
                        });

                        // Ajoutez un écouteur d'événements pour le déplacement de la souris (pour restaurer l'aspect)
                        trashButton.setOnMouseExited(event -> {
                            trashButton.setStyle(""); // Réinitialisez le style pour restaurer l'apparence par défaut
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Ajoutez les boutons à la cellule
                            setGraphic(new HBox(0, editButton, trashButton, detailsButton));
                        }
                    }
                };
                return cell;
            }
        };

        // Appliquez la cellule à la colonne d'action
        actionClm.setCellFactory(cellFactory);

        initialisTableview();

        applyCellFactoryToColumns();
    }

    private void applyCellFactoryToColumns() {
        // Appliquer le CellFactory personnalisé à toutes les colonnes sauf actionClm
        for (TableColumn<Patient, ?> column : patientsTv.getColumns()) {
            if (column != actionClm) { // Ne pas appliquer à la colonne des actions
                if (column == dossierClm || column == nomClm || column == prenomClm || column == telephoneClm
                        || column == sexeClm || column == adresseClm || column == situationFamilialClm) {

                    // Appliquer pour les colonnes de type String
                    ((TableColumn<Patient, String>) column).setCellFactory(col -> new TableCell<Patient, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                                setText(null); // Efface le texte si la cellule est vide
                                setStyle(""); // Réinitialise le style
                            } else {
                                // Récupérer l'objet Patient de la ligne actuelle
                                Patient patient = (Patient) getTableRow().getItem();
                                String sexe = patient.getSexe().toString();

                                // Appliquer le texte
                                setText(item);

                                // Appliquer la couleur du texte selon le sexe
                                if ("Féminin".equalsIgnoreCase(sexe)) {
                                    setStyle("-fx-text-fill: purple;"); // Texte en violet pour les femmes
                                } else if ("Masculin".equalsIgnoreCase(sexe)) {
                                    setStyle("-fx-text-fill: blue;"); // Texte en bleu clair pour les hommes
                                } else {
                                    setStyle(""); // Style par défaut
                                }
                            }
                        }
                    });

                } else if (column == birthClm) {

                    // Appliquer pour la colonne de type Date
                    birthClm.setCellFactory(col -> new TableCell<Patient, LocalDate>() {
                        @Override
                        protected void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);

                            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                                setText(null); // Efface le texte si la cellule est vide
                                setStyle(""); // Réinitialise le style
                            } else {
                                Patient patient = (Patient) getTableRow().getItem();
                                String sexe = patient.getSexe().toString();

                                // Afficher la date au format désiré (par exemple, "dd-MM-yyyy")
                                setText(item != null ? item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "");

                                // Appliquer la couleur du texte selon le sexe
                                if ("Féminin".equalsIgnoreCase(sexe)) {
                                    setStyle("-fx-text-fill: purple;");
                                } else if ("Masculin".equalsIgnoreCase(sexe)) {
                                    setStyle("-fx-text-fill: blue;");
                                } else {
                                    setStyle("");
                                }
                            }
                        }
                    });

                } else if (column == ageClm) {

                    // Appliquer pour la colonne de type Integer
                    ((TableColumn<Patient, Integer>) column).setCellFactory(col -> new TableCell<Patient, Integer>() {
                        @Override
                        protected void updateItem(Integer item, boolean empty) {
                            super.updateItem(item, empty);

                            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                                setText(null); // Efface le texte si la cellule est vide
                                setStyle(""); // Réinitialise le style
                            } else {
                                Patient patient = (Patient) getTableRow().getItem();
                                String sexe = patient.getSexe().toString();

                                // Afficher l'âge
                                setText(item != null ? item.toString() : "");

                                // Appliquer la couleur du texte selon le sexe
                                if ("Féminin".equalsIgnoreCase(sexe)) {
                                    setStyle("-fx-text-fill: purple;");
                                } else if ("Masculin".equalsIgnoreCase(sexe)) {
                                    setStyle("-fx-text-fill: blue;");
                                } else {
                                    setStyle("");
                                }
                            }
                        }
                    });

                }
            }
        }
    }

    @FXML
    private void openFicheNewPatient(ActionEvent event) throws IOException {
        openView("/com/azmicro/moms/view/patient/fichePatient-view.fxml");
    }
    
    public void openView(String view) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/patient/fichePatient-view.fxml"));
    Parent root = fxmlLoader.load();
    
    // Récupérer le contrôleur à partir du FXML loader
    FichePatientController controller = fxmlLoader.getController();
    
    // Passer this (le contrôleur actuel) au FichePatientController
    controller.setMainController(this);  // Vous pouvez maintenant utiliser 'this' dans FichePatientController
    
    // Autres configurations
    controller.setEditMode(false);

    // Ouvrir la vue comme une boîte de dialogue modale
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.initModality(Modality.APPLICATION_MODAL);  // Rendre la fenêtre modale
    stage.initOwner(btnOpenFicheNewPatient.getScene().getWindow());  // Définir la fenêtre principale comme propriétaire
    stage.setResizable(false);  // Facultatif : empêcher le redimensionnement
    stage.setTitle("Fiche de patient");  // Facultatif : définir le titre
    stage.showAndWait();  // Bloquer jusqu'à la fermeture de la fenêtre modale
}


    @FXML
    private void print(ActionEvent event) {
    }

    private Button createIconButton(String idButton, String glyphName) {
        FontAwesomeSolid iconType;
        switch (glyphName) {
            case "EDIT": iconType = FontAwesomeSolid.EDIT; break;
            case "INFO_CIRCLE": iconType = FontAwesomeSolid.INFO_CIRCLE; break;
            case "TRASH": iconType = FontAwesomeSolid.TRASH; break;
            default: iconType = FontAwesomeSolid.QUESTION; break;
        }
        
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(24);
        
        if ("EDIT".equals(glyphName)) {
            icon.setIconColor(Paint.valueOf("green"));
        } else if ("INFO_CIRCLE".equals(glyphName)) {
            icon.setIconColor(Paint.valueOf("orange"));
        } else if ("TRASH".equals(glyphName)) {
            icon.setIconColor(Paint.valueOf("RED"));
        }

        Button button = new Button(idButton);
        button.getStyleClass().add("transparent-button");
        button.setBackground(Background.EMPTY);
        button.setGraphic(icon);
        button.setMinWidth(8);
        return button;
    }

    private void editerPatient(Patient patient) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/patient/fichePatient-view.fxml"));
        Parent root = fxmlLoader.load();
        // Retrieve the controller from the FXML loader
        FichePatientController controller = fxmlLoader.getController();
        controller.setPatient(patient);
        controller.setEditMode(true);
        controller.setPatientController(instance);
        // Show the view as a modal dialog
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // This makes the new window modal
        stage.initOwner(btnOpenFicheNewPatient.getScene().getWindow()); // Set the owner window to the current window
        stage.setResizable(false); // Optional: Prevent resizing if not needed
        stage.setTitle("Fiche de patient"); // Optional: Set the title of the modal
        stage.showAndWait(); // This will block the calling window until the modal is closed
    }

    private void detailsPatient(Patient patient, Button detailsButton) {
        System.out.println("details");
        System.out.println("open consultation");
        try {
            if (dashboardMedecinController != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/medecin/dossier-view.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the patient object to it
                DossierController dossierController = loader.getController();
                dossierController.setPatient(patient);
                dossierController.setUtilisateur(utilisateur);

                // Load the UI
                dashboardMedecinController.loadUi(root);
            } else {
                System.err.println("dashboardController is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void confirmerSuppressionPatient(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le patient " + patient.getNom() + " " + patient.getPrenom() + "?");

        // Option pour confirmer la suppression
        ButtonType buttonTypeOui = new ButtonType("Oui");

        // Option pour annuler la suppression
        ButtonType buttonTypeAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeAnnuler);

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeOui) {
                Patient patientToDelete = patientService.findByNumDossier(patient.getNumDossier());
                System.out.println(patientToDelete.toString());
                patientService.delete(patientToDelete.getPatientID());
                patientsTv.getItems().clear();
                loadPatientsFromDatabase();
                // L'utilisateur a confirmé la suppression, vous pouvez mettre ici votre logique de suppression
                //supprimerPatient(patient.getIdPatient());
            } else {
                // L'utilisateur a annulé la suppression, vous pouvez gérer cela si nécessaire
            }
        });
    }

    public void initialisTableview() {
        // ... (initialisation des colonnes, par exemple)
        dossierClm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumDossier()));
        nomClm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        prenomClm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
// Assuming your model has a getter for dateNaissance which returns a StringProperty or LocalDate
        birthClm.setCellValueFactory(new PropertyValueFactory<>("dateNaissance")); // Ensure your model's method matches this property
        // Set the cell value factory to return the age property of the Patient object
        ageClm.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

        telephoneClm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        sexeClm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSexe().getDescription()));
        situationFamilialClm.setCellValueFactory(cellData -> {
            SituationFamiliale situationFamiliale = cellData.getValue().getSituationFamiliale();
            return new SimpleStringProperty(situationFamiliale != null ? situationFamiliale.getDescription() : "Unknown");
        });
        adresseClm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdresse()));
        loadPatientsFromDatabase();
        // TODO
    }

    public void loadPatientsFromDatabase() {
        // Utilisez votre implémentation DAO pour récupérer la liste des patients depuis la base de données
        List<Patient> patients = patientService.findAll();

        // Ajoutez les patients à la TableView
        patientsTv.getItems().addAll(patients);

        ObservableList<Patient> patientsObservableList = FXCollections.observableArrayList(patients);

        FilteredList<Patient> filterdPatients = new FilteredList<>(patientsObservableList, b -> true);

        tfKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                patientsTv.setItems(patientsObservableList); // Afficher toutes les données si le champ de recherche est vide
            } else {
                ObservableList<Patient> filteredList = FXCollections.observableArrayList();
                for (Patient patient : filterdPatients) {
                    if (patient.getNom().toLowerCase().contains(newValue.toLowerCase())
                            || patient.getPrenom().toLowerCase().contains(newValue.toLowerCase())
                            || patient.getNumDossier().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(patient);
                    }
                }
                patientsTv.setItems(filteredList); // Afficher les données filtrées
            }
        });

    }

    public void reloadScene() throws SQLException {
        System.out.println("Reloading scene in PatientController...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/medecin/patient-view.fxml"));
        try {
            Pane root = loader.load();
            Rectangle2D bounds = Screen.getPrimary().getBounds();
            Scene patient = new Scene(root, bounds.getWidth(), bounds.getHeight());
            patient.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/patient.css").toExternalForm());
            // Mettez à jour les données si nécessaire
            updatePatientsList();
            // Récupérez la scène actuelle et remplacez-la par la nouvelle

            Stage stage = (Stage) btnOpenFicheNewPatient.getScene().getWindow();
            stage.setScene(patient);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception correctement dans votre application
        }
    }

    public void updatePatientsList() {
        System.out.println("Updating patient list in PatientController...");
        patientsTv.getItems().clear();
        // Utilisez votre implémentation DAO pour récupérer la liste des patients depuis la base de données
        List<Patient> patients = patientService.findAll();
        patientsTv.getItems().setAll(patients);
    }

}
