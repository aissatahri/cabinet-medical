/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.assistante;

import com.azmicro.moms.model.RendezVous;
import com.azmicro.moms.service.RendezVousService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class RendezVousController implements Initializable {

    @FXML
    private Button btnLogout;
    @FXML
    private TextField tfKeyword;
    @FXML
    private Button btnAccueil;
    @FXML
    private TableView<RendezVous> tvRendezVous;
    @FXML
    private TableColumn<RendezVous, String> clmNomPatient;
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
    private TableColumn<RendezVous, Void> clmActions; // Colonne pour les boutons

    private ObservableList<RendezVous> rendezVousList;
    private RendezVousService rendezVousService;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.rendezVousService = new RendezVousService();

        rendezVousList = FXCollections.observableArrayList();

        // Configurer les colonnes de la TableView
        // Configure the columns of the TableView
        clmNomPatient.setCellValueFactory(cellData -> {
            RendezVous rdv = cellData.getValue();
            return new SimpleStringProperty(rdv.getPatient().getNom() + " " + rdv.getPatient().getPrenom());
        });
        clmTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        clmHourStart.setCellValueFactory(new PropertyValueFactory<>("hourStart"));
        clmHourEnd.setCellValueFactory(new PropertyValueFactory<>("hourEnd"));
        addActionsColumn();
        loadRendezVousData();
        tvRendezVous.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // this.rendezVous = newSelection;
        });
    }

    private void addActionsColumn() {
        clmActions.setCellFactory(param -> {
            return new TableCell<>() {
                private final Button btnModify = new Button("Modifier");
                private final Button btnDelete = new Button("Supprimer");

                {
                    btnModify.setOnAction(event -> {
                        RendezVous rdv = getTableView().getItems().get(getIndex());
                        handleModifyAction(rdv);
                    });
                    btnDelete.setOnAction(event -> {
                        RendezVous rdv = getTableView().getItems().get(getIndex());
                        handleDeleteAction(rdv);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Pane actionPane = new Pane();
                        btnModify.getStyleClass().add("btn-modify");
                        btnDelete.getStyleClass().add("btn-delete");
                        actionPane.getChildren().addAll(btnModify, btnDelete);
                        setGraphic(actionPane);
                    }
                }
            };
        });
    }

    public void loadRendezVousData() {
        // Charger les rendez-vous depuis le service
        rendezVousList.setAll(rendezVousService.findAllRendezVous());
        // Assigner la liste observable à la TableView
        tvRendezVous.setItems(rendezVousList);
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

    private void handleModifyAction(RendezVous rdv) {
        // Logique pour ouvrir une nouvelle fenêtre ou afficher un formulaire pour modifier
        System.out.println("Modifier " + rdv);

        // Exemples :
        // - Ouvrir une fenêtre modale pour modifier
        // - Rafraîchir les données après modification
    }

    private void handleDeleteAction(RendezVous rdv) {
        // Confirmer la suppression
        boolean confirm = confirmDeleteDialog(); // Implémentez une boîte de dialogue si nécessaire
        if (confirm) {
            rendezVousService.deleteRendezVous(rdv.getRendezVousID()); // Implémenter la méthode dans RendezVousService
            rendezVousList.remove(rdv); // Mettre à jour la liste observable
        }
    }

    private boolean confirmDeleteDialog() {
        // Crée une alerte de type CONFIRMATION
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet élément ?");
        alert.setContentText("Cette action est irréversible.");

        // Boutons de confirmation
        ButtonType buttonYes = new ButtonType("Oui");
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Ajout des boutons à l'alerte
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        // Affiche la boîte de dialogue et capture la réponse
        Optional<ButtonType> result = alert.showAndWait();

        // Retourne vrai si l'utilisateur clique sur "Oui"
        return result.isPresent() && result.get() == buttonYes;
    }

}
