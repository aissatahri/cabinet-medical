/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.assistante;

import com.azmicro.moms.model.ConsultationDetails;
import com.azmicro.moms.service.ConsultationDetailsService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class PaiementController implements Initializable {

    @FXML
    private Button btnLogout;
    @FXML
    private TextField tfKeyword;
    @FXML
    private Button btnAccueil;

    @FXML
    private Button btnSalleAttente;
    @FXML
    private TableView<ConsultationDetails> patientTerminerPaiementTv;
    @FXML
    private TableColumn<ConsultationDetails, String> nomClm;
    @FXML
    private TableColumn<ConsultationDetails, String> prenomClm;
    @FXML
    private TableColumn<ConsultationDetails, String> etatConsultationClm;
    @FXML
    private TableColumn<ConsultationDetails, LocalDate> detailClm;
    @FXML
    private TableColumn<ConsultationDetails, Double> clmMontant;
    @FXML
    private TableColumn<ConsultationDetails, Double> clmVersement;
    @FXML
    private TableColumn<ConsultationDetails, Double> clmReste;
    @FXML
    private TableColumn<ConsultationDetails, String> clmModePaiment;
    @FXML
    private RadioButton brJour;
    @FXML
    private RadioButton brSemaine;
    @FXML
    private RadioButton brMois;
    @FXML
    private RadioButton brAll;
    private ToggleGroup toggleGroup;

    private ConsultationDetailsService consultationDetailsService;
    @FXML
    private Label lblTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        consultationDetailsService = new ConsultationDetailsService();
        setupTableColumns();
        setupRadioButtons();  // Initialiser les boutons radio
        loadTableData();      // Charger les données par défaut (jour)
        addRowClickListener();
    }

    private void addRowClickListener() {
        patientTerminerPaiementTv.setRowFactory(tv -> {
            TableRow<ConsultationDetails> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ConsultationDetails rowData = row.getItem();
                    openEditPaiementDialog(rowData);
                }
            });
            return row;
        });
    }

    private void openEditPaiementDialog(ConsultationDetails details) {
        try {
            // Charger la vue FXML de la boîte de dialogue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/assistante/editPaiement-view.fxml"));
            Pane pane = loader.load();

            // Optionnel : Passer les données à la boîte de dialogue
            EditPaiementController controller = loader.getController();
            controller.setConsultationDetails(details);  // Implémentez cette méthode dans EditPaiementController
            controller.setPaiementController(this);
            // Créer la scène et la boîte de dialogue
            Stage stage = new Stage();
            stage.setTitle("Modifier le Paiement");
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.APPLICATION_MODAL);  // La fenêtre est modale
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les erreurs en affichant une alerte
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir la boîte de dialogue");
            alert.setContentText("Une erreur est survenue lors de l'ouverture de la boîte de dialogue.");
            alert.showAndWait();
        }
    }

    private void setupTableColumns() {
        nomClm.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        prenomClm.setCellValueFactory(new PropertyValueFactory<>("prenomPatient"));
        etatConsultationClm.setCellValueFactory(new PropertyValueFactory<>("statut"));
        detailClm.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        clmMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        clmVersement.setCellValueFactory(new PropertyValueFactory<>("montantVersement"));
        clmReste.setCellValueFactory(new PropertyValueFactory<>("montantReste"));
        clmModePaiment.setCellValueFactory(new PropertyValueFactory<>("modePaiement"));
    }

//    public void loadTableData() {
//        ObservableList<ConsultationDetails> data = FXCollections.observableArrayList(consultationDetailsService.getConsultationDetailsForToday());
//        patientTerminerPaiementTv.setItems(data);
//    }
    private void setupRadioButtons() {
        // Créez un ToggleGroup pour assurer qu'un seul bouton radio est sélectionné à la fois
        toggleGroup = new ToggleGroup();

        // Assignez le ToggleGroup à chaque bouton radio
        brJour.setToggleGroup(toggleGroup);
        brSemaine.setToggleGroup(toggleGroup);
        brMois.setToggleGroup(toggleGroup);
        brAll.setToggleGroup(toggleGroup);

        // Définir des auditeurs de changement pour chaque bouton radio
        brJour.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loadTableDataForToday();
            }
        });

        brSemaine.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loadTableDataForWeek();
            }
        });

        brMois.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loadTableDataForMonth();
            }
        });

        brAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loadTableDataForAll();
            }
        });
    }

    public void loadTableData() {
        // Par défaut, charger les données du jour
        clearTableData();  // Effacez les anciennes données
        loadTableDataForToday();
    }

    // Charger les données de la journée
    private void loadTableDataForToday() {
        clearTableData();  // Effacez les anciennes données
        ObservableList<ConsultationDetails> data = FXCollections.observableArrayList(consultationDetailsService.getConsultationDetailsForToday());
        patientTerminerPaiementTv.setItems(data);

        // Calculer et afficher le total des versements
        double total = calculateTotalVersements();
        lblTotal.setText(String.format("Total Versements: %.2f DH", total));  // Mise à jour du label avec la somme
    }

    // Charger les données de la semaine
    private void loadTableDataForWeek() {
        clearTableData();  // Effacez les anciennes données
        ObservableList<ConsultationDetails> data = FXCollections.observableArrayList(consultationDetailsService.getConsultationDetailsForLastWeek());
        patientTerminerPaiementTv.setItems(data);

        double total = calculateTotalVersements();
        lblTotal.setText(String.format("Total Versements: %.2f DH", total));
    }

    // Charger les données du mois
    private void loadTableDataForMonth() {
        clearTableData();  // Effacez les anciennes données
        ObservableList<ConsultationDetails> data = FXCollections.observableArrayList(consultationDetailsService.getConsultationDetailsForLastMonth());
        patientTerminerPaiementTv.setItems(data);

        double total = calculateTotalVersements();
        lblTotal.setText(String.format("Total Versements: %.2f DH", total));
    }

    // Charger toutes les données sans filtrage
    private void loadTableDataForAll() {
        clearTableData();  // Effacez les anciennes données
        ObservableList<ConsultationDetails> data = FXCollections.observableArrayList(consultationDetailsService.getAllConsultationDetails());
        patientTerminerPaiementTv.setItems(data);

        double total = calculateTotalVersements();
        lblTotal.setText(String.format("Total Versements: %.2f DH", total ));
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
        Stage primaryStage = (Stage) btnSalleAttente.getScene().getWindow();
        primaryStage.close();
    }

    public void reloadPatientTerminerPaiementTv() {
        // Code pour recharger les données dans patientTerminerPaiementTv
        loadTableData();
    }

    private void clearTableData() {
        patientTerminerPaiementTv.getItems().clear();  // Efface les données actuelles
    }

    private double calculateTotalVersements() {
        double total = 0;
        for (ConsultationDetails consultation : patientTerminerPaiementTv.getItems()) {
            total += consultation.getMontantVersement();  // Suppose que getMontantVersement() renvoie le montant du versement
        }
        return total;
    }
}
