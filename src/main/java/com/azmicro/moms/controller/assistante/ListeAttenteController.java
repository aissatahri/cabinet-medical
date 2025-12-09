/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.assistante;

import com.azmicro.moms.controller.patient.FichePatientDetailsController;
import com.azmicro.moms.dao.FilesAttenteDAOImpl;
import com.azmicro.moms.dao.PatientDAOImpl;
import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.FilesAttente;
import com.azmicro.moms.model.Jours;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Statut;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.DisponibilitesService;
import com.azmicro.moms.service.FilesAttenteService;
import com.azmicro.moms.service.MedecinService;
import com.azmicro.moms.util.DatabaseUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class ListeAttenteController implements Initializable {

    private Patient patient;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnAccueil;
    @FXML
    private TableView<FilesAttente> patientEnAttentTv;
    @FXML
    private TableColumn<FilesAttente, Void> detailClm;
    @FXML
    private TableColumn<FilesAttente, String> nomClm;
    @FXML
    private TableColumn<FilesAttente, String> prenomClm;
    @FXML
    private TableColumn<FilesAttente, LocalDate> birthClm;
    @FXML
    private TableColumn<FilesAttente, LocalDate> dateConsultationClm;
    @FXML
    private TableColumn<FilesAttente, String> telephoneClm;
    @FXML
    private TableColumn<FilesAttente, String> adressClm;
    @FXML
    private TableColumn<FilesAttente, String> sexeClm;
    @FXML
    private TableColumn<FilesAttente, String> etatMaladieClm;
    @FXML
    private TableColumn<FilesAttente, String> etatConsultationClm;
    @FXML
    private Label lblCounter;
    @FXML
    private TextField tfKeyword;
    @FXML
    private CheckBox checkedEnAttente;
    @FXML
    private CheckBox checkedEnConsultation;
    @FXML
    private CheckBox checkedTermine;

    private FilesAttenteService filesAttenteService;

// Ajoutez un champ pour DisponibilitesService
    private DisponibilitesService disponibilitesService;
    private MedecinService medecinService;
    private Utilisateur utilisateur;
    @FXML
    private Button btnPaiement;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;

    /**
     * Initializes the controller class.
     */
    public ListeAttenteController() {
    }

    public ListeAttenteController(Patient patient) {
        this.patient = patient;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        // Update label immediately if it's already initialized
        if (lblUser != null && utilisateur != null) {
            lblUser.setText(utilisateur.getNomUtilisateur());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set current date
        LocalDate currentDate = LocalDate.now();
        lblDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        
        // Real-time clock
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        
        // Set user name if utilisateur was set before initialize
        if (utilisateur != null) {
            lblUser.setText(utilisateur.getNomUtilisateur());
        }
        
        FilesAttenteDAOImpl filesAttenteDAO;
        try {
            filesAttenteDAO = new FilesAttenteDAOImpl(new PatientDAOImpl(DatabaseUtil.getConnection()));
            this.filesAttenteService = new FilesAttenteService(filesAttenteDAO);
        } catch (SQLException ex) {
            Logger.getLogger(FichePatientDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.disponibilitesService = new DisponibilitesService();
        this.medecinService = new MedecinService();

        nomClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatient().getNom()));
        prenomClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatient().getPrenom()));
        birthClm.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPatient().getDateNaissance()));
        dateConsultationClm.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateArrivee()));
        telephoneClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatient().getTelephone()));
        adressClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatient().getAdresse()));
        sexeClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatient().getSexe().toString()));
        etatMaladieClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));
        etatConsultationClm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut().toString()));

        try {
            // Initialize the TableView with data
            loadFilesAttenteData();
        } catch (SQLException ex) {
            Logger.getLogger(ListeAttenteController.class.getName()).log(Level.SEVERE, null, ex);
        }

        addDetailButtonToTable();
        checkedEnAttente.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadFilesAttenteData();
            } catch (SQLException ex) {
                Logger.getLogger(ListeAttenteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        checkedEnConsultation.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadFilesAttenteData();
            } catch (SQLException ex) {
                Logger.getLogger(ListeAttenteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        checkedTermine.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadFilesAttenteData();
            } catch (SQLException ex) {
                Logger.getLogger(ListeAttenteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Configure the TableRowFactory to style rows based on the patient sex
        patientEnAttentTv.setRowFactory(tv -> new TableRow<FilesAttente>() {
            @Override
            protected void updateItem(FilesAttente item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                } else {
                    String sexe = item.getPatient().getSexe().toString();
                    if ("Féminin".equalsIgnoreCase(sexe)) {
                        setStyle("-fx-background-color: pink;");
                    } else if ("Masculin".equalsIgnoreCase(sexe)) {
                        setStyle("-fx-background-color: lightblue;");
                    } else {
                        setStyle(""); // Default style
                    }
                }
            }
        });
    }

    void setPatient(Patient patient) {
        this.patient = patient; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    private void openAcceuilAssistant(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/dashboardAssistante-view.fxml"));

        Pane root = loader.load();
        
        // Pass utilisateur to controller
        DashboardAssistanteController controller = loader.getController();
        if (controller != null && utilisateur != null) {
            controller.setUtilisateur(utilisateur);
        }
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        Scene dashboardScene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        dashboardScene.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/dashboardassistante.css").toExternalForm());
        stage.setScene(dashboardScene);
        stage.setMaximized(true);
        stage.setTitle("SGM");
        stage.show();
        Stage primaryStage = (Stage) btnAccueil.getScene().getWindow();
        primaryStage.close();
    }

    private void loadFilesAttenteData() throws SQLException {
        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0); // Début de la journée
        LocalTime endTime = LocalTime.of(23, 59); // Fin de la journée

        // Liste pour les statuts sélectionnés
        List<String> selectedStatuses = new ArrayList<>();

        // Ajoutez les statuts sélectionnés
        if (checkedEnAttente.isSelected()) {
            selectedStatuses.add(Statut.EN_ATTENTE.name());
        }
        if (checkedEnConsultation.isSelected()) {
            selectedStatuses.add(Statut.EN_CONSULTATION.name());
        }
        if (checkedTermine.isSelected()) {
            selectedStatuses.add(Statut.TERMINE.name());
        }

        // Convertir la liste en tableau
        String[] statusArray = selectedStatuses.toArray(new String[0]);

        // Récupérer les disponibilités du médecin pour aujourd'hui
        Medecin currentMedecin = getCurrentMedecin(); // Implémentez cette méthode pour obtenir le médecin courant

        // Convertir le jour de la semaine en Jours
        Jours jour = Jours.valueOf(Jours.convertDayOfWeekToJours(today.getDayOfWeek()));

        Disponibilites disponibilites = disponibilitesService.findByMedecinAndJour(currentMedecin, jour);
        
        if (disponibilites != null) {
            System.out.println(disponibilites.toString());
            System.out.println(" Jour "+jour);
            startTime = disponibilites.getHeureDebut();
            System.out.println(" startTime "+startTime);
            endTime = disponibilites.getHeureFin();
            System.out.println(" endTime "+endTime);
        } else {
            System.out.println("Aucune disponibilité trouvée pour le médecin " + currentMedecin.getNom() + " le " + jour);
        }

        // Appeler la méthode de service avec les statuts sélectionnés et les horaires de disponibilité
        List<FilesAttente> filesAttenteList = filesAttenteService.findAll(today, startTime, endTime, statusArray);

        // Mettre à jour la TableView
        ObservableList<FilesAttente> observableList = FXCollections.observableArrayList(filesAttenteList);
        patientEnAttentTv.setItems(observableList);

        // Mettre à jour le label avec le nombre de patients
        lblCounter.setText("Nombre de patients : " + filesAttenteList.size());
    }

    public void addDetailButtonToTable() {
        Callback<TableColumn<FilesAttente, Void>, TableCell<FilesAttente, Void>> cellFactory = new Callback<TableColumn<FilesAttente, Void>, TableCell<FilesAttente, Void>>() {
            @Override
            public TableCell<FilesAttente, Void> call(final TableColumn<FilesAttente, Void> param) {
                final TableCell<FilesAttente, Void> cell = new TableCell<FilesAttente, Void>() {
                    private final Button btn = new Button();

                    {
                        // Create an ImageView for the icon
                        Image icon = new Image(getClass().getResourceAsStream("/com/azmicro/moms/images/info.png")); // Path to your icon
                        ImageView iconView = new ImageView(icon);
                        iconView.setFitHeight(16); // Adjust size as needed
                        iconView.setFitWidth(16); // Adjust size as needed
                        // Set the icon on the button
                        btn.setGraphic(iconView);
                        btn.getStyleClass().add("button");
                        btn.setOnAction((ActionEvent event) -> {
                            FilesAttente filesAttente = getTableView().getItems().get(getIndex());
                            // Handle button click action - Open constantes vitales form
                            System.out.println("Details of " + filesAttente.getPatient().getNom());
                            try {
                                openConstantesVitalesForm(filesAttente);
                            } catch (IOException ex) {
                                Logger.getLogger(ListeAttenteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        detailClm.setCellFactory(cellFactory);
    }
// Implémentez une méthode pour obtenir le médecin courant

    private Medecin getCurrentMedecin() throws SQLException {
        // Vous devez remplacer cette méthode par votre propre logique pour obtenir le médecin courant

        return medecinService.getAllMedecins().get(0); // Retournez l'instance du médecin courant
    }

    @FXML
    private void openPaiment(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/paiement-view.fxml"));

        Pane root = loader.load();
        
        // Pass utilisateur to controller
        PaiementController controller = loader.getController();
        if (controller != null && utilisateur != null) {
            controller.setUtilisateur(utilisateur);
        }
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        Scene dashboardScene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        dashboardScene.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/paiement.css").toExternalForm());
        stage.setScene(dashboardScene);
        stage.setMaximized(true);
        stage.setTitle("SGM");
        stage.show();
        Stage primaryStage = (Stage) btnAccueil.getScene().getWindow();
        primaryStage.close();
    }

    /**
     * Open the constantes vitales form for the selected patient
     */
    private void openConstantesVitalesForm(FilesAttente filesAttente) throws IOException {
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/constantesVitales-view.fxml"));
        Pane root = loader.load();
        
        // Get the controller and set the filesAttente data
        ConstantesVitalesController controller = loader.getController();
        controller.setFilesAttente(filesAttente);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/constantesvitales.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Constantes Vitales - " + filesAttente.getPatient().getPrenom() + " " + filesAttente.getPatient().getNom());
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.showAndWait(); // Wait for the window to close before continuing
        
        // Refresh the table after closing the form
        try {
            loadFilesAttenteData();
        } catch (SQLException ex) {
            Logger.getLogger(ListeAttenteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
