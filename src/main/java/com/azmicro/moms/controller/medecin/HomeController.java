package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.controller.patient.FichePatientDetailsController;
import com.azmicro.moms.dao.FilesAttenteDAOImpl;
import com.azmicro.moms.dao.PatientDAOImpl;
import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.FilesAttente;
import com.azmicro.moms.model.Jours;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.RendezVous;
import com.azmicro.moms.model.Statut;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.DisponibilitesService;
import com.azmicro.moms.service.FilesAttenteService;
import com.azmicro.moms.service.PaiementsService;
import com.azmicro.moms.service.PatientService;
import com.azmicro.moms.service.RendezVousService;
import com.azmicro.moms.model.Paiements;
import com.azmicro.moms.util.DatabaseUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class HomeController implements Initializable {

    @FXML
    private Button btnOpenPlanning;
    @FXML
    private VBox lstRendezVousPane;
    @FXML
    private RadioButton JourRb;
    @FXML
    private RadioButton semaineRb;
    @FXML
    private RadioButton moisRb;
    @FXML
    private Label lblNombrePatient;
    @FXML
    private Label lblNombrePatientListAttente;
    @FXML
    private Label lblNombreRendezVous;
    @FXML
    private Label lblMontantTotal;
    @FXML
    private Button btnNotifications;
    @FXML
    private Label lblNotificationBadge;
    // ToggleGroup pour les RadioButtons
    private ToggleGroup filterGroup;

    private Medecin medecin;
    @FXML
    private VBox lstPatientAttenteBox;
    @FXML
    private Label lblCounter;
    @FXML
    private ScrollPane scrollPane;
    // Service pour la gestion des rendez-vous
    private RendezVousService rendezVousService;
    private FilesAttenteService filesAttenteService;
    private DisponibilitesService disponibilitesService;
    private PatientService patientService;
    private PaiementsService paiementsService;
    private DashboardMedecinController dashboardMedecinController;
    private Utilisateur utilisateur;
    List<FilesAttente> filesAttenteList;

    public void setDashboardController(DashboardMedecinController dashboardMedecinController) {
        this.dashboardMedecinController = dashboardMedecinController; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        this.medecin = utilisateur.getMedecin();
        loadFilesAttenteData();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FilesAttenteDAOImpl filesAttenteDAO;
        try {
            filesAttenteDAO = new FilesAttenteDAOImpl(new PatientDAOImpl(DatabaseUtil.getConnection()));
            this.filesAttenteService = new FilesAttenteService(filesAttenteDAO);
            this.patientService = new PatientService();
        } catch (SQLException ex) {
            Logger.getLogger(FichePatientDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.disponibilitesService = new DisponibilitesService();
        // Initialiser le service de rendez-vous
        this.rendezVousService = new RendezVousService();
        this.paiementsService = new PaiementsService();

        // Création du ToggleGroup et assignation des RadioButtons
        filterGroup = new ToggleGroup();
        JourRb.setToggleGroup(filterGroup);
        semaineRb.setToggleGroup(filterGroup);
        moisRb.setToggleGroup(filterGroup);

        // Assigner des EventHandlers aux RadioButtons pour mettre à jour la vue
        JourRb.setOnAction(event -> updateRendezVousView());
        semaineRb.setOnAction(event -> updateRendezVousView());
        moisRb.setOnAction(event -> updateRendezVousView());

        // Initialiser avec la vue par jour
        JourRb.setSelected(true);
        updateRendezVousView();
        //loadFilesAttenteData();
        
        // Charger les statistiques
        loadStatistics();
        
        // Initialiser les notifications
        initializeNotifications();
    }
    
    /**
     * Charge les statistiques du dashboard
     */
    private void loadStatistics() {
        // Nombre de patients
        lblNombrePatient.setText("" + patientService.findAll().size());
        
        // Nombre de patients en attente
        if(filesAttenteList == null){
            lblNombrePatientListAttente.setText("0");
        }else{
            lblNombrePatientListAttente.setText("" + filesAttenteList.size());
        }
        
        // Nombre de rendez-vous aujourd'hui
        List<RendezVous> lst = rendezVousService.findRendezVousFromToday();
        if(lst == null){
            lblNombreRendezVous.setText("0");
        }
        else{
            lblNombreRendezVous.setText("" + lst.size());
        }
        
        // Calculer le montant total des paiements du mois
        calculateMonthlyPayments();
    }
    
    /**
     * Calcule le montant total des paiements du mois en cours
     */
    private void calculateMonthlyPayments() {
        try {
            // Récupérer tous les paiements
            List<Paiements> allPaiements = paiementsService.getAllPaiements();
            
            // Filtrer les paiements du mois en cours
            LocalDate now = LocalDate.now();
            int currentMonth = now.getMonthValue();
            int currentYear = now.getYear();
            
            double totalMensuel = allPaiements.stream()
                .filter(p -> p.getDatePaiement() != null)
                .filter(p -> p.getDatePaiement().getMonthValue() == currentMonth 
                          && p.getDatePaiement().getYear() == currentYear)
                .mapToDouble(Paiements::getVersment) // Utiliser le montant versé
                .sum();
            
            // Formater le montant avec séparateur de milliers
            String montantFormate = String.format("%,.2f DH", totalMensuel);
            lblMontantTotal.setText(montantFormate);
            
        } catch (Exception e) {
            e.printStackTrace();
            lblMontantTotal.setText("0 DH");
        }
    }
    
    /**
     * Initialise le système de notifications
     */
    private void initializeNotifications() {
        // Compter les notifications
        int notificationCount = getNotificationCount();
        
        if (notificationCount > 0) {
            lblNotificationBadge.setText(String.valueOf(notificationCount));
            lblNotificationBadge.setVisible(true);
        } else {
            lblNotificationBadge.setVisible(false);
        }
        
        // Ajouter l'événement au clic sur le bouton notifications
        if (btnNotifications != null) {
            btnNotifications.setOnAction(event -> showNotifications());
        }
    }
    
    /**
     * Compte le nombre de notifications
     */
    private int getNotificationCount() {
        int count = 0;
        
        // Notifications pour les rendez-vous proches (dans les 30 prochaines minutes)
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime thirtyMinutesLater = now.plusMinutes(30);
        
        List<RendezVous> todayAppointments = rendezVousService.findRendezVousByDate(today);
        if (todayAppointments != null) {
            for (RendezVous rdv : todayAppointments) {
                if (rdv.getHourStart() != null && 
                    !rdv.getHourStart().isBefore(now) && 
                    !rdv.getHourStart().isAfter(thirtyMinutesLater)) {
                    count++;
                }
            }
        }
        
        // Ajouter les patients en attente comme notifications
        if (filesAttenteList != null && !filesAttenteList.isEmpty()) {
            count += filesAttenteList.size();
        }
        
        return count;
    }
    
    /**
     * Affiche le panneau de notifications
     */
    private void showNotifications() {
        // TODO: Implémenter l'affichage du panneau de notifications
        System.out.println("Affichage des notifications");
    }

    @FXML
    private void openPlanning(ActionEvent event) throws IOException {
        System.out.println("details");
        System.out.println("open consultation");
        try {
            if (dashboardMedecinController != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/medecin/planning-view.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the patient object to it
                PlanningController planningController = loader.getController();
                planningController.setUtilisateur(utilisateur);

                // Load the UI
                dashboardMedecinController.loadUi(root);
            } else {
                System.err.println("dashboardController is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Met à jour la vue des rendez-vous en fonction de la sélection de filtre
     * (jour, semaine, mois).
     */
    private void updateRendezVousView() {
        // Effacer le contenu actuel du pane
        lstRendezVousPane.getChildren().clear();

        // Récupérer les rendez-vous filtrés
        List<RendezVous> rendezVousList = getFilteredRendezVous();

        // Conteneur VBox pour les rendez-vous
        VBox container = new VBox(10); // 10px d'espacement vertical entre les rendez-vous

        // Ajouter chaque rendez-vous au conteneur
        for (int i = 0; i < rendezVousList.size(); i++) {
            RendezVous r = rendezVousList.get(i);
            HBox rendezVousBox = createAppointmentBox(r, i);
            container.getChildren().add(rendezVousBox);
        }

        // Ajouter le conteneur VBox à l'AnchorPane
        lstRendezVousPane.getChildren().add(container);
    }

    /**
     * Récupère les rendez-vous filtrés en fonction de la sélection du filtre
     * (jour, semaine, mois).
     *
     * @return Liste des rendez-vous filtrés.
     */
    private List<RendezVous> getFilteredRendezVous() {
        // Récupérer la date actuelle
        LocalDate currentDate = LocalDate.now();

        // Appliquer le filtre en fonction de la sélection du RadioButton
        if (JourRb.isSelected()) {
            return getRendezVousByDay(currentDate);
        } else if (semaineRb.isSelected()) {
            return getRendezVousByWeek(currentDate);
        } else if (moisRb.isSelected()) {
            return getRendezVousByMonth(currentDate);
        }

        // Par défaut, retourner les rendez-vous du jour
        return getRendezVousByDay(currentDate);
    }

    /**
     * Récupère les rendez-vous pour une journée donnée.
     *
     * @param date La date du jour à récupérer.
     * @return Liste des rendez-vous pour ce jour.
     */
    private List<RendezVous> getRendezVousByDay(LocalDate date) {
        return rendezVousService.findRendezVousByDate(date);
    }

    /**
     * Récupère les rendez-vous pour une semaine donnée.
     *
     * @param date La date à partir de laquelle récupérer les rendez-vous de la
     * semaine.
     * @return Liste des rendez-vous pour cette semaine.
     */
    private List<RendezVous> getRendezVousByWeek(LocalDate date) {
        return rendezVousService.getRendezVousByWeek(date);
    }

    /**
     * Récupère les rendez-vous pour un mois donné.
     *
     * @param date La date à partir de laquelle récupérer les rendez-vous du
     * mois.
     * @return Liste des rendez-vous pour ce mois.
     */
    private List<RendezVous> getRendezVousByMonth(LocalDate date) {
        return rendezVousService.getRendezVousByMonth(date);
    }

    /**
     * Crée une boîte HBox pour afficher les détails d'un rendez-vous.
     *
     * @param r Le rendez-vous à afficher.
     * @param index L'index du rendez-vous (pour la coloration ou
     * l'identification éventuelle).
     * @return Une HBox représentant visuellement le rendez-vous.
     */
    private HBox createAppointmentBox(RendezVous r, int index) {
        // Création de la HBox avec un espacement horizontal de 10px entre les éléments
        HBox box = new HBox(10);

        // Appliquer un style arrondi et un espacement intérieur
        box.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 1;");

        // Alterner les couleurs des boîtes
        if (index % 2 == 0) {
            box.setStyle(box.getStyle() + "-fx-background-color: #f0f0f0; -fx-border-color: #cccccc;");
        } else {
            box.setStyle(box.getStyle() + "-fx-background-color: #e6e6e6; -fx-border-color: #999999;");
        }

        // Rendre la boîte responsive pour qu'elle prenne toute la largeur disponible
        box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(box, javafx.scene.layout.Priority.ALWAYS);

        // Label pour le titre et le nom du patient
        Label titleLabel = new Label(r.getPatient().getNom() + " - " + r.getTitre());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Label pour la date
        Label dateLabel = new Label("Date: " + r.getDate().toString());
        dateLabel.setStyle("-fx-font-size: 12px;");

        // Label pour l'heure
        Label timeLabel = new Label("Heure: " + r.getHourStart().toString() + " - " + r.getHourEnd().toString());
        timeLabel.setStyle("-fx-font-size: 12px;");

        // Espace flexible pour pousser le bouton vers la droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Création du bouton d'information avec icône
        Button infoButton = new Button();
        infoButton.setStyle("-fx-background-color: transparent;");

        // Ajout de l'icône dans le bouton (assurez-vous d'avoir le fichier info.png dans vos ressources)
        ImageView infoIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/azmicro/moms/images/info.png")));
        infoIcon.setFitHeight(20);
        infoIcon.setFitWidth(20);
        infoButton.setGraphic(infoIcon);

        // Définir l'action du bouton pour ouvrir le dossier médical
        infoButton.setOnAction(event -> openMedicalRecord(r.getPatient()));

        // Ajouter les éléments dans la boîte (les labels à gauche, espace au centre, bouton à droite)
        box.getChildren().addAll(titleLabel, dateLabel, timeLabel, spacer, infoButton);

        // Retourner la boîte qui occupe toute la largeur de son parent
        return box;
    }

    /**
     * Ouvre le dossier médical du patient en fonction de son ID.
     *
     * @param patientId L'identifiant du patient dont on veut ouvrir le dossier
     * médical.
     */
    private void openMedicalRecord(Patient patient) {
        // Implémentez la logique pour ouvrir le dossier médical du patient
        // Par exemple, vous pouvez afficher une nouvelle fenêtre ou vue avec les détails du patient
        System.out.println("Ouverture du dossier médical pour le patient ID: " + patient.getPatientID());
        detailsPatient(patient);

        // Vous pouvez également utiliser une méthode de navigation ou charger une nouvelle vue ici
        // Exemple : 
        // Stage stage = new Stage();
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/medicalRecordView.fxml"));
        // Parent root = loader.load();
        // Scene scene = new Scene(root);
        // stage.setScene(scene);
        // stage.show();
    }

    private void detailsPatient(Patient patient) {
        System.out.println("details");
        System.out.println("open consultation");
        try {
            if (dashboardMedecinController != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/medecin/dossier-view.fxml"));
                Parent root = loader.load();

                FilesAttente filesAttente = filesAttenteService.findAllByIdPatient(patient.getPatientID()).getLast();
                filesAttente.setStatut(Statut.EN_CONSULTATION);
                filesAttenteService.update(filesAttente);

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

    private void loadFilesAttenteData() {
        LocalDate today = LocalDate.now();
        LocalTime startTime = LocalTime.of(0, 0); // Début de la journée
        LocalTime endTime = LocalTime.of(23, 59); // Fin de la journée

        // Liste pour les statuts sélectionnés
        List<String> selectedStatuses = new ArrayList<>();
        selectedStatuses.add(Statut.EN_ATTENTE.name());

        // Convertir la liste en tableau
        String[] statusArray = selectedStatuses.toArray(new String[0]);

        // Récupérer les disponibilités du médecin pour aujourd'hui
        Medecin currentMedecin = this.utilisateur.getMedecin();
        // Convertir le jour de la semaine en Jours
        Jours jour = Jours.valueOf(Jours.convertDayOfWeekToJours(today.getDayOfWeek()));

        Disponibilites disponibilites = disponibilitesService.findByMedecinAndJour(currentMedecin, jour);

        if (disponibilites != null) {
            startTime = disponibilites.getHeureDebut();
            endTime = disponibilites.getHeureFin();
        }
        // Appeler la méthode de service avec les statuts sélectionnés
        filesAttenteList = filesAttenteService.findAll(today, startTime, endTime, statusArray);

        int waitingCount = filesAttenteList != null ? filesAttenteList.size() : 0;
        System.out.println(waitingCount);
        // Mettre à jour les compteurs (carte et label)
        lblCounter.setText("Nombre de patients : " + waitingCount);
        lblNombrePatientListAttente.setText(String.valueOf(waitingCount));

        // Effacer les anciens patients affichés
        lstPatientAttenteBox.getChildren().clear();

        // Conteneur VBox pour les patients en attente
        VBox container = new VBox(10); // 10px d'espacement vertical entre les patients

        // Ajouter chaque patient en attente au conteneur
        for (int i = 0; i < filesAttenteList.size(); i++) {
            FilesAttente fileAttente = filesAttenteList.get(i);
            HBox fileAttenteBox = createFilesAttenteBox(fileAttente, i);
            container.getChildren().add(fileAttenteBox);
        }

        // Ajouter le conteneur VBox à l'AnchorPane ou ScrollPane
        lstPatientAttenteBox.getChildren().add(container);
    }

    /**
     * Crée une boîte HBox pour afficher les détails d'un patient en attente.
     *
     * @param fileAttente Le patient en attente à afficher.
     * @param index L'index du patient (pour la coloration ou l'identification
     * éventuelle).
     * @return Une HBox représentant visuellement le patient en attente.
     */
    private HBox createFilesAttenteBox(FilesAttente fileAttente, int index) {
        // Création de la HBox avec un espacement horizontal de 10px entre les éléments
        HBox box = new HBox(10);

        // Appliquer un style arrondi et un espacement intérieur
        box.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-width: 1;");

        // Alterner les couleurs des boîtes
        if (index % 2 == 0) {
            box.setStyle(box.getStyle() + "-fx-background-color: #f0f0f0; -fx-border-color: #cccccc;");
        } else {
            box.setStyle(box.getStyle() + "-fx-background-color: #e6e6e6; -fx-border-color: #999999;");
        }

        // Rendre la boîte responsive pour qu'elle prenne toute la largeur disponible
        box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(box, javafx.scene.layout.Priority.ALWAYS);

        // Label pour le nom du patient
        Label patientNameLabel = new Label(fileAttente.getPatient().getNom());
        patientNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Label pour l'heure d'arrivée du patient
        Label arrivalTimeLabel = new Label("Arrivée: " + fileAttente.getHeureArrive().toString());
        arrivalTimeLabel.setStyle("-fx-font-size: 12px;");

        // Label pour le statut
        Label statutLabel = new Label("Statut: " + fileAttente.getStatut().toString());
        statutLabel.setStyle("-fx-font-size: 12px;");

        // Espace flexible pour pousser le bouton vers la droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Création du bouton d'information avec icône
        Button infoButton = new Button();
        infoButton.setStyle("-fx-background-color: transparent;");

        // Ajout de l'icône dans le bouton (assurez-vous d'avoir le fichier info.png dans vos ressources)
        ImageView infoIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/azmicro/moms/images/info.png")));
        infoIcon.setFitHeight(20);
        infoIcon.setFitWidth(20);
        infoButton.setGraphic(infoIcon);

        // Définir l'action du bouton pour ouvrir le dossier médical du patient
        infoButton.setOnAction(event -> openMedicalRecord(fileAttente.getPatient()));

        // Ajouter les éléments dans la boîte (les labels à gauche, espace au centre, bouton à droite)
        box.getChildren().addAll(patientNameLabel, arrivalTimeLabel, statutLabel, spacer, infoButton);

        // Retourner la boîte qui occupe toute la largeur de son parent
        return box;
    }

}
