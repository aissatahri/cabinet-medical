/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.App;
import com.azmicro.moms.controller.medecin.HomeController;
import com.azmicro.moms.dao.FilesAttenteDAOImpl;
import com.azmicro.moms.dao.PatientDAOImpl;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Statut;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.FilesAttenteService;
import com.azmicro.moms.util.DatabaseInitializer;
import com.azmicro.moms.util.DatabaseUtil;
import com.azmicro.moms.util.TimeUtil;
import static com.mysql.cj.conf.PropertyKey.logger;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class DashboardMedecinController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Utilisateur utilisateur;
    @FXML
    private VBox sideBar;
    @FXML
    private AnchorPane header;
    @FXML
    private Button btnHamburger;
    @FXML
    private AnchorPane container;
    @FXML
    private VBox vbRight;
    @FXML
    private HBox rootHBox;
    private boolean isSidebarVisible = true;
    private Button activeButton = null;
    @FXML
    private Button btnOpenParametre;
    @FXML
    private Button btnOpenPatients;
    @FXML
    private Button btnOpenHome;
    @FXML
    private Button btnRendezVous;
    @FXML
    private Button btnDossier;
    @FXML
    private Button btnFacturation;
    @FXML
    private Button btnLogout;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    
    @FXML
    private Label badgePlanning;
    @FXML
    private Label badgeDossier;
    @FXML
    private Label badgeFacturation;
    @FXML
    private Label badgeParametre;
    private FilesAttenteService filesAttenteService;
    
    private Medecin medecin;
    @FXML
    private Label lblDoctor;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.medecin = this.utilisateur.getMedecin();
        // Vous pouvez maintenant utiliser l'objet utilisateur pour initialiser les données de la fenêtre
        try {
            loadUi("/com/azmicro/moms/view/medecin/home-view");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lblDoctor.setText("Dr "+this.medecin.getPrenom()+" "+this.medecin.getNom());
        updateBadges();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnHamburger.setOnAction(event -> toggleSidebar());
        
        // Mettre à jour les badges de notification
        updateBadges();

        Timeline timeline;
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        updateTimeLabele();
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // La timeline se répète indéfiniment
        timeline.play(); // Démarrez la timeline

    }

    private void toggleSidebar() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(150), sideBar);

        if (isSidebarVisible) {
            transition.setToX(-sideBar.getWidth());
            transition.setOnFinished(event -> {
                sideBar.setVisible(false);
                rootHBox.getChildren().remove(sideBar);
                vbRight.setPrefWidth(rootHBox.getWidth());
            });
            isSidebarVisible = false;
        } else {
            sideBar.setVisible(true);
            transition.setToX(0);
            transition.setOnFinished(event -> {
                rootHBox.getChildren().add(0, sideBar);
                vbRight.setPrefWidth(rootHBox.getWidth() - sideBar.getPrefWidth());
            });
            isSidebarVisible = true;
        }

        transition.play();
    }
    
    /**
     * Marque un bouton comme actif
     */
    private void setActiveButton(Button button) {
        // Retirer la classe active de l'ancien bouton
        if (activeButton != null) {
            activeButton.getStyleClass().remove("menu-button-active");
        }
        
        // Ajouter la classe active au nouveau bouton
        if (button != null && !button.getStyleClass().contains("menu-button-active")) {
            button.getStyleClass().add("menu-button-active");
        }
        
        activeButton = button;
    }

    public void loadUi(Parent ui) {
        container.getChildren().setAll(ui);
        AnchorPane.setTopAnchor(ui, 0.0);
        AnchorPane.setRightAnchor(ui, 0.0);
        AnchorPane.setBottomAnchor(ui, 0.0);
        AnchorPane.setLeftAnchor(ui, 0.0);
    }

    public void loadUi(String ui) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ui + ".fxml"));
        AnchorPane anchorPane = loader.load();
        // Récupérer le contrôleur associé au fichier FXML
        Object controller = loader.getController();

        if (controller instanceof PatientController) {
            // Injecter le DashboardController dans SalleAttenteController
            ((PatientController) controller).setDashboardController(this);
            ((PatientController) controller).setUtilisateur(utilisateur);
        }

        if (controller instanceof DossierController) {
            // Injecter le DashboardController dans SalleAttenteController
            ((DossierController) controller).setDashboardController(this);
            ((DossierController) controller).setUtilisateur(utilisateur);
        }

        if (controller instanceof ParametreController) {
            // Injecter le DashboardController dans SalleAttenteController
            ((ParametreController) controller).setDashboardController(this);
            ((ParametreController) controller).setUtilisateur(utilisateur);
        }

        if (controller instanceof PlanningController) {
            // Injecter le DashboardController dans SalleAttenteController
            ((PlanningController) controller).setDashboardController(this);
            ((PlanningController) controller).setUtilisateur(utilisateur);
        }

        if (controller instanceof HomeController) {
            // Injecter le DashboardController dans SalleAttenteController
            ((HomeController) controller).setDashboardController(this);
            ((HomeController) controller).setUtilisateur(utilisateur);
        }

        // Remplacer le contenu de l'AnchorPane actuel par le nouveau AnchorPane chargé
        container.getChildren().setAll(anchorPane);
        AnchorPane.setTopAnchor(anchorPane, 0.0);
        AnchorPane.setRightAnchor(anchorPane, 0.0);
        AnchorPane.setBottomAnchor(anchorPane, 0.0);
        AnchorPane.setLeftAnchor(anchorPane, 0.0);
    }

    @FXML
    private void openParametre(ActionEvent event) {
        setActiveButton(btnOpenParametre);
        try {
            loadUi("/com/azmicro/moms/view/medecin/parametre-view");
        } catch (IOException ex) {
            Logger.getLogger(DashboardMedecinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openPatient(ActionEvent event) {
        setActiveButton(btnOpenPatients);
        try {
            loadUi("/com/azmicro/moms/view/medecin/patient-view");
        } catch (IOException ex) {
            Logger.getLogger(DashboardMedecinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openDossier(ActionEvent event) {
        setActiveButton(btnDossier);
//        try {
//            loadUi("/com/azmicro/moms/view/medecin/dossier-view");
//        } catch (IOException ex) {
//            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    @FXML
    private void openHome(ActionEvent event) {
        setActiveButton(btnOpenHome);
        try {
            loadUi("/com/azmicro/moms/view/medecin/home-view");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openRendezVous(ActionEvent event) {
        setActiveButton(btnRendezVous);
        try {
            loadUi("/com/azmicro/moms/view/medecin/planning-view");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openFacturation(ActionEvent event) {
        setActiveButton(btnFacturation);
//        try {
//            loadUi("/com/azmicro/moms/view/medecin/facturation-view");
//        } catch (IOException ex) {
//            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(App.class);

    private void logout(MouseEvent event) throws IOException {

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        // Ne PAS effacer les préférences pour que "Se souvenir de moi" fonctionne
        // Les préférences seront gérées par l'utilisateur via la checkbox
        
        Stage stage = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SGM"); // Set the window title
        stage.initStyle(StageStyle.UNDECORATED); // Supprimer la barre de titre
        stage.setScene(scene);
        stage.setResizable(false); // Rendre la fenêtre non redimensionnable
        stage.setWidth(900);
        stage.setHeight(600);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        double x = (bounds.getWidth() / 2 - stage.getWidth() / 2);
        double y = (bounds.getHeight() / 2 - stage.getHeight() / 2);
        stage.setX(x);
        stage.setY(y);
        stage.show();
        Stage primaryStage = (Stage) btnLogout.getScene().getWindow();
        primaryStage.close();
    }

    private void updateHeureLabel() {
        // Utilisez la classe utilitaire pour obtenir l'heure actuelle
        String heureActuelle = TimeUtil.getCurrentTime();

        // Mettre à jour le label avec l'heure actuelle
        //heureLbl.setText(heureActuelle);
    }

    private void updateDateLabel() {
        // Utilisez la classe utilitaire pour obtenir la date actuelle en temps réel
        String currentDate = TimeUtil.getCurrentDate();

        // Mettez à jour le label avec la date actuelle
        //dateLbl.setText(currentDate);
    }
    
    private void updateTimeLabele(){
        String heureActuelle = TimeUtil.getCurrentTime();
        String currentDate = TimeUtil.getCurrentDate();
        lblDate.setText(currentDate);
        lblTime.setText(heureActuelle);
    }
    
    /**
     * Met à jour les badges de notification sur les boutons du menu
     */
    private void updateBadges() {
        if (utilisateur == null || medecin == null) {
            // badges removed
            return;
        }

        try {
            if (filesAttenteService == null) {
                filesAttenteService = new FilesAttenteService(new FilesAttenteDAOImpl(new PatientDAOImpl(DatabaseUtil.getConnection())));
            }

            LocalDate today = LocalDate.now();
            LocalTime start = LocalTime.MIN;
            LocalTime end = LocalTime.MAX;

            int waitingCount = filesAttenteService.findAll(today, start, end, Statut.EN_ATTENTE.name()).size();

            // badges removed
        } catch (Exception e) {
            Logger.getLogger(DashboardMedecinController.class.getName()).log(Level.SEVERE, "Impossible de mettre à jour le badge patients", e);
            // badges removed
        }
    }

    private void applyBadgeValue(Label badge, int value) {
        if (badge == null) {
            return;
        }
        boolean visible = value > 0;
        badge.setVisible(visible);
        if (visible) {
            badge.setText(String.valueOf(value));
        }
    }

    private void hideBadge(Label badge) {
        if (badge != null) {
            badge.setVisible(false);
        }
    }
}
