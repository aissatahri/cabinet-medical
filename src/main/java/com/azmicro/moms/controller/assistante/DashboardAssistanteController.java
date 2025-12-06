/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.assistante;

import com.azmicro.moms.controller.patient.FichePatientController;
import com.azmicro.moms.controller.patient.FichePatientDetailsController;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.SituationFamiliale;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.PatientService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.control.Pagination;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

/**
import javafx.scene.control.Pagination;
 * FXML Controller class
 *
 * @author Aissa
 */
public class DashboardAssistanteController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Utilisateur utilisateur;
    @FXML
    private Button btnLogout;
    @FXML
    private TextField tfKeyword;
    @FXML
    private Button btnOpenFicheNewPatient;
    @FXML
    private Button btnOpenSalleAttente;
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
    private static DashboardAssistanteController instance;
    @FXML
    private Button btnOpenRendezvous;
    @FXML
    private Button btnOpenListeRendezVous;
    @FXML
    private Pagination patientsPagination;

    private ObservableList<Patient> patientsData = FXCollections.observableArrayList();
    private FilteredList<Patient> filteredPatients = new FilteredList<>(patientsData, p -> true);
    private static final int ROWS_PER_PAGE = 10;

    public DashboardAssistanteController() {
        instance = this;
        this.patientService = patientService;
    }

    private DashboardAssistanteController getDashboardAssistanteController() {
        // Implémentez le code pour obtenir l'instance du DashboardAssistanteController
        return instance;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        // Vous pouvez maintenant utiliser l'objet utilisateur pour initialiser les données de la fenêtre
    }

    public static DashboardAssistanteController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

                    private final Button addButton = createIconButton("addButton", "PLUS");
                    private final Button editButton = createIconButton("editButton", "EDIT");
                    private final Button trashButton = createIconButton("trashButton", "TRASH");

                    {
                        // Ajoutez des actions aux boutons
                        addButton.setOnAction(event -> {
                            Patient patient = patientService.findByNumDossier(getTableView().getItems().get(getIndex()).getNumDossier());
                            System.out.println("add button clicked for patient: " + patient.getNom());
                            confirmerAjouterPatient(patient);
                        });

                        editButton.setOnAction(event -> {
                            Patient patient = patientService.findByNumDossier(getTableView().getItems().get(getIndex()).getNumDossier());
                            System.out.println("edit button clicked for patient: " + patient.getPrenom());
                            try {
                                editerPatient(patient);
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
                        addButton.setOnMouseEntered(event -> {
                            addButton.setStyle("-fx-background-color: lightgray;"); // Changez le style lors du survol
                        });

                        // Ajoutez un écouteur d'événements pour le déplacement de la souris (pour restaurer l'aspect)
                        addButton.setOnMouseExited(event -> {
                            addButton.setStyle(""); // Réinitialisez le style pour restaurer l'apparence par défaut
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
                            HBox box = new HBox(6, addButton, editButton, trashButton);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
                return cell;
            }
        };

        // Appliquez la cellule à la colonne d'action
        actionClm.setCellFactory(cellFactory);
        actionClm.setMinWidth(130);
        actionClm.setPrefWidth(150);
        actionClm.setMaxWidth(200);

        initialisTableview();
        applyCellFactoryToColumns();

        setupSearchFiltering();

        patientsPagination.setPageFactory(pageIndex -> {
            refreshPage(pageIndex);
            return new Pane();
        });
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

        patientsData.setAll(patients);
        refreshPagination();

    }

    private void setupSearchFiltering() {
        tfKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            String keyword = newValue == null ? "" : newValue.trim().toLowerCase();
            filteredPatients.setPredicate(patient -> {
                if (keyword.isEmpty()) {
                    return true;
                }
                return patient.getNom().toLowerCase().contains(keyword)
                        || patient.getPrenom().toLowerCase().contains(keyword)
                        || patient.getNumDossier().toLowerCase().contains(keyword)
                        || patient.getTelephone().toLowerCase().contains(keyword);
            });
            refreshPagination();
        });
    }

    private void refreshPagination() {
        int totalItems = filteredPatients.size();
        int pageCount = (int) Math.ceil((double) totalItems / ROWS_PER_PAGE);
        patientsPagination.setPageCount(Math.max(pageCount, 1));
        int currentIndex = patientsPagination.getCurrentPageIndex();
        if (currentIndex >= patientsPagination.getPageCount()) {
            currentIndex = patientsPagination.getPageCount() - 1;
            patientsPagination.setCurrentPageIndex(Math.max(currentIndex, 0));
        }
        refreshPage(patientsPagination.getCurrentPageIndex());
    }

    private void refreshPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, filteredPatients.size());
        if (fromIndex > toIndex) {
            patientsTv.setItems(FXCollections.observableArrayList());
            return;
        }
        patientsTv.setItems(FXCollections.observableArrayList(filteredPatients.subList(fromIndex, toIndex)));
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.DECORATED.UNDECORATED);
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        double x = (bounds.getWidth() / 2 - stage.getWidth() / 2);
        double y = (bounds.getHeight() / 2 - stage.getHeight() / 2);
        stage.setX(x);
        stage.setY(y);
        stage.setTitle("SGM");
        stage.show();
        Stage primaryStage = (Stage) btnLogout.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    private void openFicheNewPatient(ActionEvent event) throws IOException {
        openView("/com/azmicro/moms/view/patient/fichePatient-view.fxml");
    }

    @FXML
    private void openSalleAttente(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/listeAttente-view.fxml"));
        Pane root = loader.load();
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        Scene dashboardScene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        dashboardScene.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/listeattente.css").toExternalForm());
        stage.setScene(dashboardScene);
        stage.setMaximized(true);
        stage.setTitle("SGM");
        stage.show();
        Stage primaryStage = (Stage) btnOpenSalleAttente.getScene().getWindow();
        primaryStage.close();
    }

    public void openView(String view) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/patient/fichePatient-view.fxml"));
        Parent root = fxmlLoader.load();
        // Retrieve the controller from the FXML loader
        FichePatientController controller = fxmlLoader.getController();
        controller.setEditMode(false);

        // Passez le contrôleur DashboardAssistanteController au FichePatientController
        controller.setDashboardAssistanteController(this);
//        controller.setCurrentStage((Stage) btnOpenFicheNewPatient.getScene().getWindow());
        // Show the view as a modal dialog
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // This makes the new window modal
        stage.initOwner(btnOpenFicheNewPatient.getScene().getWindow()); // Set the owner window to the current window
        stage.setResizable(false); // Optional: Prevent resizing if not needed
        stage.setTitle("Fiche de patient"); // Optional: Set the title of the modal
        stage.showAndWait(); // This will block the calling window until the modal is closed
    }

    private void confirmerAjouterPatient(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation d'ajouter");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir ajouter le patient " + patient.getNom() + " " + patient.getPrenom() + " a la liste d'attente ?");

        // Option pour confirmer la suppression
        ButtonType buttonTypeOui = new ButtonType("Oui");

        // Option pour annuler la suppression
        ButtonType buttonTypeAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeAnnuler);

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType response) {
                if (response == buttonTypeOui) {

                    try {
                        ajouterPatientListAttent(patient);
                        // L'utilisateur a confirmé la suppression, vous pouvez mettre ici votre logique de suppression
                        //supprimerPatient(patient.getIdPatient());
                    } catch (IOException ex) {
                        Logger.getLogger(DashboardAssistanteController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // L'utilisateur a annulé la suppression, vous pouvez gérer cela si nécessaire
                }
            }
        });
    }

    private void editerPatient(Patient patient) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/patient/fichePatient-view.fxml"));
        Parent root = fxmlLoader.load();
        // Retrieve the controller from the FXML loader
        FichePatientController controller = fxmlLoader.getController();
        controller.setPatient(patient);
        controller.setDashboardController(instance);
        controller.setEditMode(true);
        // Show the view as a modal dialog
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // This makes the new window modal
        stage.initOwner(btnOpenFicheNewPatient.getScene().getWindow()); // Set the owner window to the current window
        stage.setResizable(false); // Optional: Prevent resizing if not needed
        stage.setTitle("Fiche de patient"); // Optional: Set the title of the modal
        stage.showAndWait(); // This will block the calling window until the modal is closed
    }

    private void openView(FXMLLoader loader) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);
        Scene scene = new Scene(loader.load());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.initOwner(btnOpenFicheNewPatient.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(800);
        stage.setHeight(628);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        double x = (bounds.getWidth() / 2 - stage.getWidth() / 2);
        double y = (bounds.getHeight() / 2 - stage.getHeight() / 2);
        stage.setX(x);
        stage.setY(y);
        stage.show();
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

    private Button createIconButton(String idButton, String glyphName) {
        FontAwesomeSolid iconType;
        switch (glyphName) {
            case "EDIT": iconType = FontAwesomeSolid.EDIT; break;
            case "PLUS": iconType = FontAwesomeSolid.PLUS; break;
            case "INFO_CIRCLE": iconType = FontAwesomeSolid.INFO_CIRCLE; break;
            case "TRASH": iconType = FontAwesomeSolid.TRASH; break;
            default: iconType = FontAwesomeSolid.QUESTION; break;
        }
        
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(18);
        
        if ("EDIT".equals(glyphName)) {
            icon.setIconColor(Paint.valueOf("green"));
        } else if ("PLUS".equals(glyphName)) {
            icon.setIconColor(Paint.valueOf("blue"));
        } else if ("INFO_CIRCLE".equals(glyphName)) {
            icon.setIconColor(Paint.valueOf("orange"));
        } else if ("TRASH".equals(glyphName)) {
            icon.setIconColor(Paint.valueOf("RED"));
        }

        Button button = new Button();
        button.getStyleClass().add("transparent-button");
        button.setBackground(Background.EMPTY);
        button.setGraphic(icon);
        button.setText(null); // keep only the icon
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setFocusTraversable(false);
        button.setMinSize(28, 28);
        button.setPrefSize(32, 32);
        button.setMaxSize(36, 36);
        return button;
    }

    public void openView(String view, Button button) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.DECORATED.UNDECORATED);
        stage.setScene(scene);
        stage.initOwner(btnOpenFicheNewPatient.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(800);
        stage.setHeight(628);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        double x = (bounds.getWidth() / 2 - stage.getWidth() / 2);
        double y = (bounds.getHeight() / 2 - stage.getHeight() / 2);
        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    private void detailsPatient(Patient patient) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/patient/fichePatientDetails-view.fxml"));
        FichePatientDetailsController controller = new FichePatientDetailsController(patient);
        loader.setController(controller);
        openView(loader);
    }

    private void openModalWindow(String title, FXMLLoader loader) throws IOException {
        Pane pane = loader.load();
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.setTitle(title);
        stage.setScene(new Scene(pane));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(patientsTv.getScene().getWindow());

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double x = (screenBounds.getWidth() - stage.getWidth()) / 2;
        double y = (screenBounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);

        stage.showAndWait();
    }

    public void ajouterPatientListAttent(Patient patient) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/patient/fichePatientDetails-view.fxml"));
        Parent root = fxmlLoader.load();
        // Retrieve the controller from the FXML loader
        FichePatientDetailsController controller = fxmlLoader.getController();
        controller.setPatient(patient);
        // Show the view as a modal dialog
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // This makes the new window modal
        stage.initOwner(btnOpenFicheNewPatient.getScene().getWindow()); // Set the owner window to the current window
        stage.setResizable(false); // Optional: Prevent resizing if not needed
        stage.setTitle("Fiche patient : Antecedent et file d'attente "); // Optional: Set the title of the modal
        stage.showAndWait();
    }

    public void reloadScene() throws SQLException {
        // Charger à nouveau la scène (dashboard-view.fxml)
        System.out.println("Reloading scene in DashboardAssistanteController...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/dashboardAssistante-view.fxml"));
        try {
            Pane root = loader.load();
            Rectangle2D bounds = Screen.getPrimary().getBounds();
            Scene dashboardScene = new Scene(root, bounds.getWidth(), bounds.getHeight());
            dashboardScene.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/dashboardassistante.css").toExternalForm());
            // Mettez à jour les données si nécessaire
            updatePatientsList();
            // Récupérez la scène actuelle et remplacez-la par la nouvelle

            Stage stage = (Stage) btnOpenSalleAttente.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setMaximized(true);
            stage.setTitle("SGM");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception correctement dans votre application
        }
    }

    private void updatePatientsList() {
        System.out.println("Updating patient list in DashboardAssistanteController...");
        // Utilisez votre implémentation DAO pour récupérer la liste des patients depuis la base de données
        List<Patient> patients = patientService.findAll();
        patientsData.setAll(patients);
        refreshPagination();
    }

    @FXML
    private void openRendervous(ActionEvent event) {
        // Récupérer le patient sélectionné
        Patient selectedPatient = patientsTv.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            try {
                // Si un patient est sélectionné, ouvrir la vue des rendez-vous
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/ajoutRendezvous-view.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de la vue des rendez-vous
                AjoutRendervousController controller = loader.getController();

                // Envoyer le patient sélectionné au contrôleur
                controller.setPatient(selectedPatient);

                // Ouvrir la vue
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(btnOpenRendezvous.getScene().getWindow());
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(DashboardAssistanteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Si aucun patient n'est sélectionné, afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun patient sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un patient dans la liste avant d'ouvrir les rendez-vous.");
            alert.showAndWait();
        }

    }

    @FXML
    private void openListeRendezVous(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/rendezVous-view.fxml"));
        Pane root = loader.load();
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        Scene dashboardScene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        dashboardScene.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/listeattente.css").toExternalForm());
        stage.setScene(dashboardScene);
        stage.setMaximized(true);
        stage.setTitle("SGM");
        stage.show();
        Stage primaryStage = (Stage) btnOpenSalleAttente.getScene().getWindow();
        primaryStage.close();
    }

}
