package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.Jours;
import static com.azmicro.moms.model.Jours.convertDayOfWeekToJours;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.RendezVous;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.DisponibilitesService;
import com.azmicro.moms.service.RendezVousService;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

public class PlanningController implements Initializable {

    @FXML
    private Button btnDaily;
    @FXML
    private Button btnWeekly;
    @FXML
    private Button btnMonthly;
    @FXML
    private Button btnToday;
    @FXML
    private ScrollPane spAgenda;
    private VBox vboxAgenda;

    private RendezVousService rendezVousService;
    @FXML
    private GridPane gridAgenda;
    private DashboardMedecinController dashboardMedecinController;
    private Utilisateur utilisateur;
    private Medecin medecin;
    // Déclaration du service Disponibilites
    private DisponibilitesService disponibilitesService;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    private LocalDate currentDate;
    @FXML
    private Label lblDate;

    void setDashboardController(DashboardMedecinController dashboardMedecinController) {
        this.dashboardMedecinController = dashboardMedecinController;
    }

    void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.medecin = utilisateur.getMedecin();
        showWeeklyView();// Default view
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rendezVousService = new RendezVousService();
        this.disponibilitesService = new DisponibilitesService();
        this.currentDate = LocalDate.now();

        // Initialisation à la vue hebdomadaire par défaut
        //showWeeklyView();  // Default view
        // Affichage de la date
        updateDateLabel();
        setupKeyboardShortcuts();

    }

    private void setupKeyboardShortcuts() {
        spAgenda.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.W) {
                applyScaleTransition(gridAgenda);
                showWeeklyView();
            } else if (event.getCode() == KeyCode.D) {
                applyScaleTransition(gridAgenda);
                showDailyView();
            } else if (event.getCode() == KeyCode.M) {
                applyScaleTransition(gridAgenda);
                showMonthlyView();
            }
        });
    }

    // Animation de transition de mise à l'échelle pour les changements de vues
    private void applyScaleTransition(GridPane grid) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), grid);
        scaleTransition.setFromX(0.8);
        scaleTransition.setFromY(0.8);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    // Animation de fondu pour l'apparition des rendez-vous
    private void applyFadeTransition(VBox node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    // Méthode pour vérifier si un rendez-vous est valide en fonction des disponibilités
    private boolean isRendezVousValid(RendezVous rendezVous, Medecin medecin) {
        List<Disponibilites> disponibilitesList = disponibilitesService.findAllByMedecin(medecin);
        for (Disponibilites d : disponibilitesList) {
            if (rendezVous.getDate().getDayOfWeek().equals(d.getJours().getJour())
                    && !rendezVous.getHourStart().isBefore(d.getHeureDebut())
                    && !rendezVous.getHourEnd().isAfter(d.getHeureFin())) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void showDailyView() {
        btnDaily.setDisable(true);
        btnWeekly.setDisable(false);
        btnMonthly.setDisable(false);
        gridAgenda.getChildren().clear();
        gridAgenda.getColumnConstraints().clear();
        gridAgenda.getRowConstraints().clear();
        ColumnConstraints hourColumn = new ColumnConstraints();
        hourColumn.setPercentWidth(20);
        gridAgenda.getColumnConstraints().add(hourColumn);
        ColumnConstraints dayColumn = new ColumnConstraints();
        dayColumn.setPercentWidth(80);
        gridAgenda.getColumnConstraints().add(dayColumn);
        // Convert LocalDate to DayOfWeek and then to Jours
        Jours jour = convertDayOfWeekToJours(currentDate.getDayOfWeek());
        Disponibilites dailyAvailability = disponibilitesService.findByMedecinAndJour(medecin, jour);
        if (dailyAvailability != null) {
            LocalTime heureDebut = dailyAvailability.getHeureDebut();
            LocalTime heureFin = dailyAvailability.getHeureFin();
            // Calculate the number of hours to display
            int hoursToShow = heureFin.getHour() - heureDebut.getHour() + 1;
            // Add the rows for the available hours only
            for (int i = 0; i < hoursToShow; i++) {
                RowConstraints row = new RowConstraints();
                row.setPrefHeight(50);
                gridAgenda.getRowConstraints().add(row);
                // Create and add the hour labels within the available time range
                int hour = heureDebut.getHour() + i;
                Label hourLabel = new Label(hour + ":00");
                hourLabel.setStyle("-fx-padding: 5; -fx-border-color: black; -fx-border-width: 0 0 1 1;");
                GridPane.setConstraints(hourLabel, 0, i);
                gridAgenda.getChildren().add(hourLabel);
            }
            // Display the appointments only within the availability
            List<RendezVous> dailyAppointments = rendezVousService.getRendezVousByDate(currentDate);
            for (int i = 0; i < dailyAppointments.size(); i++) {
                RendezVous r = dailyAppointments.get(i);
                int startHour = r.getHourStart().getHour();
                // Ensure the appointment is within the available time range
                if (!r.getHourStart().isBefore(heureDebut) && !r.getHourEnd().isAfter(heureFin)) {
                    VBox appointmentBox = createAppointmentBox(r, i);
                    GridPane.setConstraints(appointmentBox, 1, startHour - heureDebut.getHour());
                    gridAgenda.getChildren().add(appointmentBox);
                }
            }
        }
    }

    @FXML
    private void showWeeklyView() {
        btnDaily.setDisable(false);
        btnWeekly.setDisable(true);
        btnMonthly.setDisable(false);

        gridAgenda.getChildren().clear();
        gridAgenda.getColumnConstraints().clear();
        gridAgenda.getRowConstraints().clear();

        // Variables pour stocker les disponibilités hebdomadaires
        Map<LocalDate, Disponibilites> weeklyAvailability = new HashMap<>();
        LocalTime earliestStart = LocalTime.MAX;
        LocalTime latestEnd = LocalTime.MIN;

        // Récupérer les disponibilités hebdomadaires
        LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            Jours jour = convertDayOfWeekToJours(date.getDayOfWeek());
            Disponibilites availability = disponibilitesService.findByMedecinAndJour(medecin, jour);
            weeklyAvailability.put(date, availability);

            if (availability != null) {
                LocalTime start = availability.getHeureDebut();
                LocalTime end = availability.getHeureFin();
                if (start.isBefore(earliestStart)) {
                    earliestStart = start;
                }
                if (end.isAfter(latestEnd)) {
                    latestEnd = end;
                }
            }
        }

        // Calculer l'intervalle total de disponibilité
        int totalHours = (int) (latestEnd.toSecondOfDay() - earliestStart.toSecondOfDay()) / 3600 + 1;

        // Créer les contraintes de colonnes pour les jours
        for (int i = 0; i < 8; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 8);
            gridAgenda.getColumnConstraints().add(column);
        }

        // Créer les contraintes de lignes en fonction de l'intervalle total d'heures de disponibilité
        for (int i = 0; i < totalHours; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(50);
            gridAgenda.getRowConstraints().add(row);
        }

        // Afficher les jours de la semaine
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            String dayOfWeekInFrench = date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH);
            Label dayLabel = new Label(dayOfWeekInFrench);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0 0 1 1;");
            GridPane.setConstraints(dayLabel, i + 1, 0);
            gridAgenda.getChildren().add(dayLabel);
        }

        // Afficher les heures de la journée (de l'heure la plus tôt à l'heure la plus tardive)
        for (int i = 0; i < totalHours; i++) {
            LocalTime hourLabelTime = earliestStart.plusHours(i);
            Label hourLabel = new Label(hourLabelTime.getHour() + ":00");
            hourLabel.setStyle("-fx-padding: 5; -fx-border-color: black; -fx-border-width: 0 0 1 1;");
            GridPane.setConstraints(hourLabel, 0, i + 1);
            gridAgenda.getChildren().add(hourLabel);
        }

        // Afficher les heures de disponibilité pour chaque jour en utilisant l'intervalle le plus large
        for (Map.Entry<LocalDate, Disponibilites> entry : weeklyAvailability.entrySet()) {
            LocalDate date = entry.getKey();
            Disponibilites availability = entry.getValue();

            if (availability != null) {
                LocalTime start = availability.getHeureDebut();
                LocalTime end = availability.getHeureFin();
                int column = date.getDayOfWeek().getValue() % 7;
                if (column == 0) {
                    column = 7;
                }

//                // Afficher la plage horaire de disponibilité
//                for (int hour = 0; hour < totalHours; hour++) {
//                    LocalTime hourTime = earliestStart.plusHours(hour);
//                    if (!hourTime.isBefore(start) && !hourTime.isAfter(end)) {
//                        // Marquer les heures de disponibilité avec une couleur
//                        Label availabilityLabel = new Label();
//                        availabilityLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 5; -fx-border-color: black; -fx-border-width: 0 0 1 1;");
//                        GridPane.setConstraints(availabilityLabel, column, hour + 1);
//                        gridAgenda.getChildren().add(availabilityLabel);
//                    }
//                }
            }
        }

        // Afficher les rendez-vous
        List<RendezVous> weeklyAppointments = rendezVousService.getRendezVousByWeek(startOfWeek);
        for (RendezVous r : weeklyAppointments) {
            int column = r.getDate().getDayOfWeek().getValue() % 7;
            if (column == 0) {
                column = 7;
            }
            int startHour = (int) (r.getHourStart().toSecondOfDay() - earliestStart.toSecondOfDay()) / 3600 + 1;
            VBox appointmentBox = createAccessibleAppointmentBox(r, weeklyAppointments.indexOf(r));
            applyFadeTransition(appointmentBox);
            GridPane.setConstraints(appointmentBox, column, startHour + 1);
            gridAgenda.getChildren().add(appointmentBox);
        }

        updateDateLabel();
    }

// Méthode pour attribuer une couleur différente à chaque rendez-vous
    private String getColorForAppointment(int index) {
        String[] colors = {
            "#ffcccb", // rose clair
            "#add8e6", // bleu clair
            "#90ee90", // vert clair
            "#ffd700", // or
            "#ffb6c1", // rose
            "#87ceeb", // bleu ciel
            "#98fb98", // vert pâle
            "#dda0dd", // prune
            "#f0e68c", // kaki
            "#ffa07a"  // saumon
        };
        return colors[index % colors.length];
    }

    @FXML
    private void showMonthlyView() {
        btnDaily.setDisable(false);
        btnWeekly.setDisable(false);
        btnMonthly.setDisable(true);

        gridAgenda.getChildren().clear();
        gridAgenda.getColumnConstraints().clear();
        gridAgenda.getRowConstraints().clear();

        // Configuration des colonnes pour les jours de la semaine
        for (int i = 0; i < 7; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 7);
            gridAgenda.getColumnConstraints().add(column);
        }

        // Configuration des lignes pour les semaines du mois (maximum 6 semaines)
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(100);
            gridAgenda.getRowConstraints().add(row);
        }

        // Définition des jours de la semaine
        String[] days = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0 0 1 1;");
            GridPane.setConstraints(dayLabel, i, 0);
            gridAgenda.getChildren().add(dayLabel);
        }

        // Calcul des jours du mois
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        LocalDate firstOfMonth = yearMonth.atDay(1);

        // Calculer à quel jour de la semaine commence le mois
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 = Lundi, 7 = Dimanche
        // Adapter pour que Lundi commence à 0
        dayOfWeek = (dayOfWeek == 7) ? 6 : dayOfWeek - 1;

        // Calcul du premier jour à afficher dans le calendrier (y compris les jours précédant le début du mois)
        LocalDate calendarDate = firstOfMonth.minusDays(dayOfWeek);

        // Remplissage du calendrier avec les jours
        for (int week = 0; week < 6; week++) {  // Jusqu'à 6 semaines
            for (int day = 0; day < 7; day++) {
                VBox dayBox = new VBox();
                dayBox.setStyle("-fx-padding: 5; -fx-border-color: black; -fx-border-width: 0 0 1 1;");

                Label dayNumber = new Label(String.valueOf(calendarDate.getDayOfMonth()));
                dayBox.getChildren().add(dayNumber);

                GridPane.setConstraints(dayBox, day, week + 1);
                gridAgenda.getChildren().add(dayBox);

                // Si la date appartient au mois courant, changer la couleur d'arrière-plan
                if (calendarDate.getMonth() == currentDate.getMonth()) {
                    dayBox.setStyle(dayBox.getStyle() + "-fx-background-color: lightgray;");
                    // Ajouter les rendez-vous à la case du jour
                    List<RendezVous> dailyAppointments = rendezVousService.getRendezVousByDate(calendarDate);
                    for (int i = 0; i < dailyAppointments.size(); i++) {
                        RendezVous r = dailyAppointments.get(i);
                        VBox appointmentBox = createAccessibleAppointmentBox(r, i);
                        applyFadeTransition(appointmentBox);  // Appliquer la transition de fondu
                        dayBox.getChildren().add(appointmentBox);
                    }
                }

                calendarDate = calendarDate.plusDays(1); // Passer au jour suivant
            }
        }

        updateDateLabel();
    }

// Méthode pour créer un rendez-vous sous forme de boîte avec des ajustements de padding, margin et taille de police
    private VBox createAccessibleAppointmentBox(RendezVous r, int index) {
        VBox appointmentBox = new VBox(2);
        // Ajout du style pour minimiser le padding et ajuster les marges
        appointmentBox.setStyle("-fx-background-color: " + getColorForAppointment(index)
                + "; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5; "
                + "-fx-border-color: #2c3e50; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 0, 1);");
        
        Tooltip tooltip = new Tooltip("Patient: " + r.getPatient().getNom() + " " + r.getPatient().getPrenom() + "\n"
                + "Rendez-vous: " + r.getTitre() + "\n"
                + "Heure: " + r.getHourStart() + " - " + r.getHourEnd() + "\n"
                + "Description: " + r.getDesc());
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(appointmentBox, tooltip);
        
        // Définir un texte accessible pour les lecteurs d'écran
        appointmentBox.setAccessibleText("Rendez-vous: " + r.getTitre() + " à " + r.getHourStart());
        
        // Titre avec nom du patient
        Label patientLabel = new Label("👤 " + r.getPatient().getNom() + " " + r.getPatient().getPrenom());
        patientLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Titre du rendez-vous
        Label titleLabel = new Label(r.getTitre());
        titleLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        // Heure
        Label timeLabel = new Label("🕒 " + r.getHourStart() + " - " + r.getHourEnd());
        timeLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #7f8c8d;");
        
        appointmentBox.getChildren().addAll(patientLabel, titleLabel, timeLabel);
        
        // Effet de survol
        appointmentBox.setOnMouseEntered(e -> {
            appointmentBox.setStyle(appointmentBox.getStyle() + "-fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
        });
        appointmentBox.setOnMouseExited(e -> {
            appointmentBox.setStyle(appointmentBox.getStyle().replace("-fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;", ""));
        });
        
        return appointmentBox;
    }

    private GridPane createGridForDailyView() {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        // Configuration des lignes pour les heures
        for (int i = 8; i <= 18; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
            Text timeText = new Text(i + ":00");
            gridPane.add(timeText, 0, i - 8);
        }
        return gridPane;
    }

    private GridPane createGridForWeeklyView() {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        // Ajouter les colonnes pour les jours
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(col);
            Text dayText = new Text(LocalDate.now().with(DayOfWeek.of(i + 1)).getDayOfWeek().toString());
            gridPane.add(dayText, i, 0);
        }
        // Ajouter les lignes pour les heures
        for (int i = 8; i <= 18; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
            Text timeText = new Text(i + ":00");
            gridPane.add(timeText, 0, i - 8 + 1);
        }

        return gridPane;
    }

    private GridPane createGridForMonthlyView() {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        // Configuration des lignes et colonnes pour les jours du mois
        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(row);
        }
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(col);
        }
        // Ajouter les jours du mois
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < firstDayOfMonth.lengthOfMonth(); i++) {
            LocalDate day = firstDayOfMonth.plusDays(i);
            Text dayText = new Text(day.toString());
            gridPane.add(dayText, i % 7, i / 7);
        }
        return gridPane;
    }

    private void addAppointmentsToGrid(GridPane gridPane, List<RendezVous> rendezVousList) {
        for (RendezVous r : rendezVousList) {
            // Positionner le rendez-vous en fonction de la date et de l'heure
            int column = r.getDate().getDayOfWeek().getValue() - 1; // Colonne pour le jour
            int row = r.getHourStart().getHour() - 8; // Ligne pour l'heure de début
            Text appointmentText = new Text(r.getTitre());
            gridPane.add(appointmentText, column, row);
        }
    }

    private VBox createAppointmentBox(RendezVous r, int index) {
        VBox box = new VBox();

        // Création du label pour le titre du rendez-vous
        Label titleLabel = new Label(r.getPatient().getNom() + " - " + r.getTitre());
        titleLabel.setStyle("-fx-padding: 5; -fx-border-color: black; -fx-background-color: " + getColorForAppointment(index) + "; -fx-font-weight: bold;");

        // Ajouter un Tooltip au titleLabel
        Tooltip titleTooltip = new Tooltip("Patient: " + r.getPatient().getNom() + "\nTitre: " + r.getTitre() + "\nDescription: " + r.getDesc());
        Tooltip.install(titleLabel, titleTooltip);

        // Créer un HBox pour aligner les labels horizontalement
        HBox infoBox = new HBox(10); // espacement de 10px entre les éléments

        // Label pour la date
        Label dateLabel = new Label("Date: " + r.getDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
        dateLabel.setStyle("-fx-padding: 2; -fx-border-color: black; -fx-background-color: " + getColorForAppointment(index) + ";");

        // Ajouter un Tooltip au dateLabel
        Tooltip dateTooltip = new Tooltip("Date du rendez-vous: " + r.getDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
        Tooltip.install(dateLabel, dateTooltip);

        // Label pour l'heure
        Label timeLabel = new Label("Time: " + r.getHourStart().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + r.getHourEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setStyle("-fx-padding: 2; -fx-border-color: black; -fx-background-color: " + getColorForAppointment(index) + ";");

        // Ajouter un Tooltip au timeLabel
        Tooltip timeTooltip = new Tooltip("Heure: " + r.getHourStart().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + r.getHourEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
        Tooltip.install(timeLabel, timeTooltip);

        // Ajouter les labels à l'HBox
        infoBox.getChildren().addAll(titleLabel, dateLabel, timeLabel);

        // Ajouter le titre et l'HBox au VBox
        box.getChildren().addAll(infoBox);

        // Style du VBox
        box.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 5;");

        // Optionnel : Définir une taille minimale pour le VBox
        box.setMinSize(150, 50);

        return box;
    }

    @FXML
    private void navigateToPrevious() {
        if (btnDaily.isDisable()) {
            currentDate = currentDate.minusDays(1);
            showDailyView();
        } else if (btnWeekly.isDisable()) {
            currentDate = currentDate.minusWeeks(1);
            showWeeklyView();
        } else if (btnMonthly.isDisable()) {
            currentDate = currentDate.minusMonths(1);
            showMonthlyView();
        }
        updateDateLabel();  // Mettre à jour la date affichée
    }

    @FXML
    private void navigateToNext() {
        if (btnDaily.isDisable()) {
            currentDate = currentDate.plusDays(1);
            showDailyView();
        } else if (btnWeekly.isDisable()) {
            currentDate = currentDate.plusWeeks(1);
            showWeeklyView();
        } else if (btnMonthly.isDisable()) {
            currentDate = currentDate.plusMonths(1);
            showMonthlyView();
        }
        updateDateLabel();  // Mettre à jour la date affichée
    }
    
    @FXML
    private void goToToday() {
        currentDate = LocalDate.now();
        if (btnDaily.isDisable()) {
            showDailyView();
        } else if (btnWeekly.isDisable()) {
            showWeeklyView();
        } else if (btnMonthly.isDisable()) {
            showMonthlyView();
        }
        updateDateLabel();
    }

    private void updateDateLabel() {
        DateTimeFormatter formatter;
        if (btnDaily.isDisable()) {
            formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", java.util.Locale.FRENCH);
            lblDate.setText(currentDate.format(formatter));
        } else if (btnWeekly.isDisable()) {
            LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            formatter = DateTimeFormatter.ofPattern("d MMMM", java.util.Locale.FRENCH);
            lblDate.setText(startOfWeek.format(formatter) + " - " + endOfWeek.format(DateTimeFormatter.ofPattern("d MMMM yyyy", java.util.Locale.FRENCH)));
        } else if (btnMonthly.isDisable()) {
            formatter = DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale.FRENCH);
            lblDate.setText(currentDate.format(formatter));
        }
    }

    private Jours convertDayOfWeekToJours(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return Jours.LUNDI;
            case TUESDAY:
                return Jours.MARDI;
            case WEDNESDAY:
                return Jours.MERCREDI;
            case THURSDAY:
                return Jours.JEUDI;
            case FRIDAY:
                return Jours.VENDREDI;
            case SATURDAY:
                return Jours.SAMEDI;
            case SUNDAY:
                return Jours.DIMANCHE;
            default:
                throw new IllegalArgumentException("Jour inconnu: " + dayOfWeek);
        }
    }

    private DayOfWeek convertJoursToDayOfWeek(Jours jour) {
        switch (jour) {
            case LUNDI:
                return DayOfWeek.MONDAY;
            case MARDI:
                return DayOfWeek.TUESDAY;
            case MERCREDI:
                return DayOfWeek.WEDNESDAY;
            case JEUDI:
                return DayOfWeek.THURSDAY;
            case VENDREDI:
                return DayOfWeek.FRIDAY;
            case SAMEDI:
                return DayOfWeek.SATURDAY;
            case DIMANCHE:
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Jour inconnu: " + jour);
        }
    }

}
