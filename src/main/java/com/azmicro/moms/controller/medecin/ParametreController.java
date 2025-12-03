/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.model.Acte;
import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.Jours;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Medicaments;
import com.azmicro.moms.model.Specialites;
import com.azmicro.moms.model.TypeAnalyse;
import com.azmicro.moms.model.TypeImagerie;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.ActeService;
import com.azmicro.moms.service.DisponibilitesService;
import com.azmicro.moms.service.MedecinService;
import com.azmicro.moms.service.MedicamentsService;
import com.azmicro.moms.service.SpecialitesService;
import com.azmicro.moms.service.TypeAnalyseService;
import com.azmicro.moms.service.TypeImagerieService;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javax.imageio.ImageIO;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class ParametreController implements Initializable {

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField prenomTextField;
    @FXML
    private ComboBox<Specialites> specialiteComboBox;
    @FXML
    private TextField telephoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField adresseTextField;
    @FXML
    private TableView<Medecin> medecinTableView;
    @FXML
    private TableColumn<Medecin, String> nomColumn;
    @FXML
    private TableColumn<Medecin, String> prenomColumn;
    @FXML
    private TableColumn<Medecin, Specialites> specialiteColumn;
    @FXML
    private TableColumn<Medecin, String> telephoneColumn;
    @FXML
    private TableColumn<Medecin, String> emailColumn;
    @FXML
    private TableColumn<Medecin, String> adresseColumn;
    @FXML
    private TextField heureDebutTextField;
    @FXML
    private TextField heureFinTextField;
    @FXML
    private TableView<Disponibilites> disponibiliteTableView;
    @FXML
    private TableColumn<Disponibilites, LocalDateTime> heureDebutColumn;
    @FXML
    private TableColumn<Disponibilites, LocalDateTime> heureFinColumn;
    @FXML
    private TableColumn<Disponibilites, String> jourClm;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ComboBox<Jours> joursComboBox;
    @FXML
    private TextField acteTf;
    @FXML
    private TextField prixActeTf;
    @FXML
    private TableView<Acte> tvActe;
    @FXML
    private TableColumn<Acte, String> clmActe;
    @FXML
    private TableColumn<Acte, Double> clmPrix;
    @FXML
    private TextField tfMedicament;
    @FXML
    private TextField tfFormeDosage;
    @FXML
    private TableView<Medicaments> tvMedicament;
    @FXML
    private TableColumn<Medicaments, String> clmMedicament;
    @FXML
    private TableColumn<Medicaments, String> clmFormeDosage;
    @FXML
    private TextField tfImagerie;
    @FXML
    private TextField tfDescriptionImagerie;
    @FXML
    private TableView<TypeImagerie> tvImagerie;
    @FXML
    private TableColumn<TypeImagerie, String> clmImagerie;
    @FXML
    private TableColumn<TypeImagerie, String> clmDescriptionImagerie;
    @FXML
    private TextField analyseTf;
    @FXML
    private TextField descriptionAnalyse;
    @FXML
    private TableView<TypeAnalyse> tvAnalyse;
    @FXML
    private TableColumn<TypeAnalyse, String> clmAnalyse;
    @FXML
    private TableColumn<TypeAnalyse, String> clmDescriptionAnalyse;

    private SpecialitesService specialitesService;
    private MedecinService medecinService; // Assurez-vous que ce service est injecté ou initialisé ailleurs
    Medecin medecinSelected;
    DisponibilitesService disponibilitesService;

    private DashboardMedecinController DashboardMedecinController = new DashboardMedecinController();
    private Utilisateur utilisateur;
    private Medecin medecin;
    private Disponibilites selectedDisponibilite;

    private ActeService acteService;
    private MedicamentsService medicamentsService;
    private TypeImagerieService typeImagerieService;
    private TypeAnalyseService typeAnalyseService;

    private ObservableList<Acte> actesList;
    private ObservableList<Medicaments> medicamentsList;
    private ObservableList<TypeImagerie> imagerieList;
    private ObservableList<TypeAnalyse> analyseList;
    @FXML
    private TextField outputDirectoryTextField;

    private static final String CONFIG_FILE_NAME = "config.properties";
    private String configFilePath;
    @FXML
    private TableColumn<TypeImagerie, String> clmCodeImagerie;
    @FXML
    private TableColumn<TypeAnalyse, String> clmCodeAnalyse;
    @FXML
    private TextField tfCodeImagerie;
    @FXML
    private TextField codeFrAnalyse;
    @FXML
    private TextField tfKeywordMedicament;
    @FXML
    private TextField tfKeywordImagerie;
    @FXML
    private TextField tfKeywordAnalyse;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection connection = null;
        acteService = new ActeService();
        medicamentsService = new MedicamentsService();
        typeImagerieService = new TypeImagerieService();
        typeAnalyseService = new TypeAnalyseService();
        // TODO
        this.specialitesService = new SpecialitesService();
        this.medecinService = new MedecinService();
        this.disponibilitesService = new DisponibilitesService();
        fillSpecialiteComboBox();
        // Configurer les colonnes de la TableView
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        nomColumn.setPrefWidth(100); // Ajustez la largeur comme nécessaire
        prenomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        prenomColumn.setPrefWidth(100); // Ajustez la largeur comme nécessaire
        specialiteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSpecialite()));
        specialiteColumn.setPrefWidth(100);
        specialiteColumn.setCellFactory(column -> new TableCell<Medecin, Specialites>() {
            @Override
            protected void updateItem(Specialites specialite, boolean empty) {
                super.updateItem(specialite, empty);
                if (empty || specialite == null) {
                    setText(null);
                } else {
                    setText(specialite.getNomSpecialite());
                }
            }
        });
        telephoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        telephoneColumn.setPrefWidth(150);
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        emailColumn.setPrefWidth(150);
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        adresseColumn.setPrefWidth(150);

        try {
            // Charger les données dans la TableView
            loadMedecinData();
        } catch (SQLException ex) {
            Logger.getLogger(ParametreController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Ajouter un listener pour gérer les sélections dans la TableView
        medecinTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                medecinSelected = loadMedecinDetails(newValue);
            }
        });

        // Ajouter tous les jours de l'énumération au ComboBox
        joursComboBox.getItems().addAll(Jours.values());

        // Optionnel: Sélectionner un jour par défaut (par exemple, le lundi)
        joursComboBox.setValue(Jours.LUNDI);
        // Configurer le TextField pour l'heure de début
        configureTimeTextField(heureDebutTextField);

        // Configurer le TextField pour l'heure de fin
        configureTimeTextField(heureFinTextField);

        // Initialisation des colonnes
        heureDebutColumn.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        heureFinColumn.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        jourClm.setCellValueFactory(new PropertyValueFactory<>("jours"));

        // Ajouter un écouteur de sélection
        disponibiliteTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Appeler la méthode pour mettre à jour les contrôles
                this.selectedDisponibilite = newValue;
                updateControls(newValue);
            }
        });

        // Ajouter un écouteur d'événements pour le double-clic
        disponibiliteTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-clic
                handleRowDoubleClick();
            }
        });

        // Initialize the TableView data
        loadTableViewData();

        setupTableViewSelectionHandlers();

        // Handle double-click for delete with confirmation
//        handleTableViewDelete(tvActe, acteService::deleteActe);
//        handleTableViewDelete(tvMedicament, medicamentsService::deleteMedicaments);
//        handleTableViewDelete(tvImagerie, typeImagerieService::deleteTypeImagerie);
//        handleTableViewDelete(tvAnalyse, typeAnalyseService::delete);
        tvAnalyse.setRowFactory(tv -> {
            TableRow<TypeAnalyse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    System.out.println(row.getItem().toString());
                    handleDeleteAnalyse(row.getItem());
                }
            });
            return row;
        });

        tvActe.setRowFactory(tv -> {
            TableRow<Acte> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    System.out.println(row.getItem().toString());
                    handleDeleteActe(row.getItem());
                }
            });
            return row;
        });

        // Ajoutez l'écouteur de double-clic pour supprimer un médicament
        tvMedicament.setRowFactory(tv -> {
            TableRow<Medicaments> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    System.out.println(row.getItem().toString());
                    handleDeleteMedicament(row.getItem());
                }
            });
            return row;
        });
        tvImagerie.setRowFactory(tv -> {
            TableRow<TypeImagerie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    System.out.println(row.getItem().toString());
                    handleDeleteImagerie(row.getItem());
                }
            });
            return row;
        });

        // Initialize file path for config.properties
        configFilePath = System.getProperty("user.home") + "/app-config/" + CONFIG_FILE_NAME;
        ensureConfigDirectoryExists();

        // Load configuration properties
        Properties properties = loadConfigProperties();
        String outputDirectory = properties.getProperty("output.directory", "D:/output");
        // Use outputDirectory as needed in your application
        outputDirectoryTextField.setText(outputDirectory); // Set the initial value in the text field
    }

    private void loadTableViewData() {
        actesList = FXCollections.observableArrayList(acteService.getAllActes());
        medicamentsList = FXCollections.observableArrayList(medicamentsService.findAllMedicaments());
        imagerieList = FXCollections.observableArrayList(typeImagerieService.findAll());
        analyseList = FXCollections.observableArrayList(typeAnalyseService.findAll());

        // Set data to TableViews
        tvActe.setItems(actesList);
        tvMedicament.setItems(medicamentsList);
        tvImagerie.setItems(imagerieList);
        tvAnalyse.setItems(analyseList);

        // Acte TableView
        clmActe.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomActe()));
        clmPrix.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());

        // Medicaments TableView
        clmMedicament.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomMedicament()));
        clmFormeDosage.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormeDosage()));

        // Imagerie TableView
        clmImagerie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomImagerieFr()));
        clmDescriptionImagerie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        clmCodeImagerie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodeImagerieFr()));

        // Analyse TableView
        clmAnalyse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomAnalyseFr()));
        clmDescriptionAnalyse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        clmCodeAnalyse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodeAnalyseFr()));

    }

    private void setupTableViewSelectionHandlers() {
        // Acte TableView
        tvActe.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Acte selectedActe = (Acte) newValue;
                acteTf.setText(selectedActe.getNomActe());
                prixActeTf.setText(String.valueOf(selectedActe.getPrix()));
            }
        });

        // Medicaments TableView
        tvMedicament.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Medicaments selectedMedicament = (Medicaments) newValue;
                tfMedicament.setText(selectedMedicament.getNomMedicament());
                tfFormeDosage.setText(selectedMedicament.getFormeDosage());
            }
        });

        // Imagerie TableView
        tvImagerie.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TypeImagerie selectedImagerie = (TypeImagerie) newValue;
                tfImagerie.setText(selectedImagerie.getNomImagerieFr());
                tfDescriptionImagerie.setText(selectedImagerie.getDescription());
                tfCodeImagerie.setText(selectedImagerie.getCodeImagerieFr());
            }
        });

        // Analyse TableView
        tvAnalyse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TypeAnalyse selectedAnalyse = (TypeAnalyse) newValue;
                analyseTf.setText(selectedAnalyse.getNomAnalyseFr());
                descriptionAnalyse.setText(selectedAnalyse.getDescription());
                codeFrAnalyse.setText(selectedAnalyse.getCodeAnalyseFr());
            }
        });
        // Ajoutez l'écouteur de double-clic pour supprimer une imagerie
        tvImagerie.setRowFactory(tv -> {
            TableRow<TypeImagerie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleDeleteImagerie(row.getItem());
                }
            });
            return row;
        });

        // Ajouter un écouteur de texte pour surveiller les changements dans le champ de recherche
        tfKeywordMedicament.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appeler la méthode de recherche lorsque l'utilisateur tape un texte
            searchMedicaments(newValue);
        });

        // Ajouter un écouteur de texte pour surveiller les changements dans le champ de recherche
        tfKeywordImagerie.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appeler la méthode de recherche lorsque l'utilisateur tape un texte
            searchImageries(newValue);
        });

        // Écouter les modifications dans le champ de recherche
        tfKeywordAnalyse.textProperty().addListener((observable, oldValue, newValue) -> {
            searchAndDisplayAnalyses(newValue);
        });

    }

    // Méthode de recherche
    private void searchMedicaments(String keyword) {
        // Appeler la méthode de recherche dans le service
        List<Medicaments> medicamentsList = medicamentsService.findByKeyword(keyword);

        // Mettre à jour la vue avec les résultats
        updateMedicamentsListView(medicamentsList);
    }

    private void searchImageries(String keyword) {
        List<TypeImagerie> imagerieList = typeImagerieService.findByKeyword(keyword);
        // Mettre à jour la TableView avec les résultats
        tvImagerie.getItems().setAll(imagerieList);
    }

    // Effectuer la recherche par mot-clé et afficher les résultats dans la TableView
    private void searchAndDisplayAnalyses(String keyword) {
        List<TypeAnalyse> results = typeAnalyseService.findByKeyword(keyword);
        tvAnalyse.getItems().setAll(results); // Mettre à jour la TableView avec les résultats de la recherche
    }

    // Exemple de méthode pour mettre à jour la vue avec les résultats
    private void updateMedicamentsListView(List<Medicaments> medicamentsList) {
        // Effacer les anciennes données
        tvMedicament.getItems().clear();

        // Ajouter les nouveaux résultats
        tvMedicament.getItems().addAll(medicamentsList);
    }

    private void configureTimeTextField(TextField textField) {
        textField.setPromptText("HH:MM");

        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                // Gérer la suppression et les touches Tab/Enter
                return;
            }
            // Empêcher les entrées non numériques
            if (!event.getText().matches("[0-9]")) {
                event.consume();
                return;
            }
            String currentText = textField.getText();
            int caretPosition = textField.getCaretPosition();

            // Ajouter les deux-points si nécessaire
            if (caretPosition == 2 && !currentText.contains(":")) {
                textField.setText(currentText + ":");
                textField.positionCaret(caretPosition + 1);
            }
        });

        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 5) {
                textField.setText(oldText);
            }
            if (newText.length() == 5 && !newText.matches("\\d{2}:\\d{2}")) {
                textField.setText(oldText);
            }
        });
    }

    private Medecin loadMedecinDetails(Medecin medecin) {
        // Remplir les champs du formulaire avec les détails du médecin sélectionné
        nomTextField.setText(medecin.getNom());
        prenomTextField.setText(medecin.getPrenom());
        specialiteComboBox.setValue(medecin.getSpecialite());
        telephoneTextField.setText(medecin.getTelephone());
        emailTextField.setText(medecin.getEmail());
        adresseTextField.setText(medecin.getAdresse());

        // Mettre à jour l'image du logo si disponible
        if (medecin.getLogo() != null) {
            Image logoImage = new Image(new ByteArrayInputStream(medecin.getLogo()));
            logoImageView.setImage(logoImage);
        } else {
            logoImageView.setImage(null);
        }
        return medecin;
    }

    private void loadMedecinData() throws SQLException {
        List<Medecin> medecins = medecinService.getAllMedecins(); // Supposons que getAllMedecins() retourne une liste de médecins
        ObservableList<Medecin> medecinList = FXCollections.observableArrayList(medecins);
        medecinTableView.setItems(medecinList);
    }

    private void fillSpecialiteComboBox() {
        List<Specialites> specialitesList = specialitesService.getAllSpecialites();
        specialiteComboBox.getItems().clear(); // Nettoie les éléments existants
        specialiteComboBox.getItems().addAll(specialitesList); // Ajoute les nouveaux éléments

        // Configurer la cellule du ComboBox pour afficher le nom de la spécialité
        specialiteComboBox.setCellFactory(cellFactory -> new ListCell<Specialites>() {
            @Override
            protected void updateItem(Specialites specialite, boolean empty) {
                super.updateItem(specialite, empty);
                setText(empty ? "" : specialite.getNomSpecialite());
            }
        });

        // Configurer le convertisseur pour afficher la spécialité sélectionnée
        specialiteComboBox.setConverter(new StringConverter<Specialites>() {
            @Override
            public String toString(Specialites specialite) {
                return specialite == null ? "" : specialite.getNomSpecialite();
            }

            @Override
            public Specialites fromString(String string) {
                // Méthode nécessaire mais non utilisée ici
                return null;
            }
        });
    }

    @FXML
    private void handleSaveMedecin(ActionEvent event) throws SQLException {
        System.out.println("Méthode save appelée");

        // Valider les champs du formulaire
        String validationMessage = validateForm();
        if (validationMessage.isEmpty()) {
            Medecin medecin;

            // Si un médecin est sélectionné, on met à jour cet objet existant
            if (medecinSelected != null) {
                medecin = medecinSelected;
            } else {
                // Sinon, on crée un nouvel objet Medecin
                medecin = new Medecin();
            }

            // Mettre à jour les données du médecin avec les valeurs du formulaire
            medecin.setNom(nomTextField.getText());
            medecin.setPrenom(prenomTextField.getText());

            // Récupérer la spécialité sélectionnée
            Specialites specialite = specialitesService.finByName(specialiteComboBox.getValue().getNomSpecialite());
            if (specialite != null) {
                medecin.setSpecialite(specialite);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur de validation", "Veuillez sélectionner une spécialité.");
                return; // Sortir de la méthode si aucune spécialité n'est sélectionnée
            }

            medecin.setTelephone(telephoneTextField.getText());
            medecin.setEmail(emailTextField.getText());
            medecin.setAdresse(adresseTextField.getText());

            // Convertir l'image en tableau d'octets (si disponible)
            byte[] logoBytes = null;
            if (logoImageView.getImage() != null) {
                try {
                    logoBytes = convertImageToBytes(logoImageView.getImage());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la conversion de l'image.");
                    e.printStackTrace();
                    return; // Sortir de la méthode en cas d'erreur
                }
            }
            medecin.setLogo(logoBytes);

            boolean isSaved;
            if (medecin.getMedecinID() > 0) {
                // Mise à jour du médecin existant
                isSaved = medecinService.updateMedecin(medecin);
                System.out.println("Mise à jour du médecin : " + medecin.toString());
            } else {
                // Création d'un nouveau médecin
                isSaved = medecinService.createMedecin(medecin);
                System.out.println("Création d'un nouveau médecin : " + medecin.toString());
            }

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Médecin enregistré avec succès.");
                // Vider les champs du formulaire
                clearFormFields();
                // Réinitialiser la sélection de médecin
                medecinSelected = null;
                // Recharger la TableView
                loadMedecinData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'enregistrement du médecin.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", validationMessage);
        }
    }

    private void clearFormFields() {
        nomTextField.clear();
        prenomTextField.clear();
        specialiteComboBox.getSelectionModel().clearSelection();
        telephoneTextField.clear();
        emailTextField.clear();
        adresseTextField.clear();
        logoImageView.setImage(null);
    }

    private String validateForm() {
        StringBuilder errorMessage = new StringBuilder();

        if (nomTextField.getText().isEmpty()) {
            errorMessage.append("Le nom ne peut pas être vide.\n");
        }
        if (prenomTextField.getText().isEmpty()) {
            errorMessage.append("Le prénom ne peut pas être vide.\n");
        }
        if (specialiteComboBox.getValue() == null) {
            errorMessage.append("La spécialité ne peut pas être vide.\n");
        }
        if (telephoneTextField.getText().isEmpty()) {
            errorMessage.append("Le téléphone ne peut pas être vide.\n");
        }
        if (emailTextField.getText().isEmpty()) {
            errorMessage.append("L'email ne peut pas être vide.\n");
        }
        if (adresseTextField.getText().isEmpty()) {
            errorMessage.append("L'adresse ne peut pas être vide.\n");
        }

        return errorMessage.toString();
    }

    private byte[] convertImageToBytes(Image image) {
        if (image == null) {
            return null;
        }

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void handleDeleteMedecin(ActionEvent event) {
    }

    @FXML
    private void handleUploadLogo(ActionEvent event) {
        // Create a FileChooser to select the image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Open the file chooser dialog and get the selected file
        File file = fileChooser.showOpenDialog(logoImageView.getScene().getWindow());

        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file)) {
                // Create an Image object from the file
                Image image = new Image(fis);

                // Set the Image object in the ImageView
                logoImageView.setImage(image);

                System.out.println("Logo uploaded and displayed successfully.");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to upload and display logo.");
            }
        }
    }

    @FXML
    private void handleAddOrUpdateDisponibilite() {
        String heureDebut = heureDebutTextField.getText();
        String heureFin = heureFinTextField.getText();
        Jours jour = joursComboBox.getValue();

        if (isValidTime(heureDebut) && isValidTime(heureFin)) {
            LocalTime startTime = LocalTime.parse(heureDebut);
            LocalTime endTime = LocalTime.parse(heureFin);

            if (startTime.isBefore(endTime)) {
                // Récupérer l'objet Medecin sélectionné ou courant
                // Assurez-vous que `this.medecin` est correctement initialisé et disponible
                if (this.medecin == null) {
                    showAlert("Erreur", "Aucun médecin sélectionné.");
                    return;
                }

                // Créer ou mettre à jour une disponibilité
                Disponibilites disponibilite;

                if (selectedDisponibilite == null) {
                    // Créer une nouvelle disponibilité
                    disponibilite = new Disponibilites();
                    disponibilite.setMedecin(this.medecin);
                } else {
                    // Mettre à jour une disponibilité existante
                    disponibilite = selectedDisponibilite;
                }

                disponibilite.setJours(jour);
                disponibilite.setHeureDebut(startTime);
                disponibilite.setHeureFin(endTime);

                // Utiliser le service pour ajouter ou mettre à jour la disponibilité
                boolean success;
                if (selectedDisponibilite == null) {
                    success = disponibilitesService.saveDisponibilite(disponibilite);
                } else {
                    success = disponibilitesService.updateDisponibilite(disponibilite);
                }

                if (success) {
                    showAlert("Succès", selectedDisponibilite == null ? "Disponibilité ajoutée avec succès." : "Disponibilité mise à jour avec succès.");
                    // Optionnel : Réinitialiser les contrôles ou recharger les données
                    loadDisponibilites(); // Si vous voulez recharger la TableView
                    clearControls(); // Méthode pour réinitialiser les contrôles (à définir si nécessaire)
                } else {
                    showAlert("Erreur", "Une erreur s'est produite lors de l'ajout ou de la mise à jour de la disponibilité.");
                }
            } else {
                showAlert("Erreur", "L'heure de début doit être antérieure à l'heure de fin.");
            }
        } else {
            showAlert("Erreur", "Veuillez entrer des heures valides au format HH:MM.");
        }
    }

    private boolean isValidTime(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Medecin getSelectedMedecin() {
        // Implémentez cette méthode pour récupérer le médecin sélectionné ou courant
        // Cela peut dépendre de la sélection d'un ComboBox, d'une TableView ou autre.
        return this.medecinSelected;  // Exemple par défaut
    }

    void setDashboardController(DashboardMedecinController dashboardMedecinController) {
        this.DashboardMedecinController = dashboardMedecinController; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        this.medecin = utilisateur.getMedecin();
        // Charger les données dans la TableView
        loadDisponibilites();
    }

    private void loadDisponibilites() {
        ObservableList<Disponibilites> disponibilitesList = FXCollections.observableArrayList();
        // Récupération des disponibilités du service
        List<Disponibilites> disponibilites = disponibilitesService.findAllByMedecin(this.medecin);
        // Ajouter les disponibilités à la liste observable
        disponibilitesList.addAll(disponibilites);
        // Assigner la liste observable à la TableView
        disponibiliteTableView.setItems(disponibilitesList);
    }

    private void updateControls(Disponibilites disponibilite) {
        // Mettre à jour les contrôles avec les données de la disponibilité sélectionnée
        heureDebutTextField.setText(disponibilite.getHeureDebut().toString());
        heureFinTextField.setText(disponibilite.getHeureFin().toString());
        joursComboBox.setValue(disponibilite.getJours());
    }

    private void clearControls() {
        // Réinitialiser le champ de texte pour l'heure de début
        heureDebutTextField.clear();
        // Réinitialiser le champ de texte pour l'heure de fin
        heureFinTextField.clear();
        // Réinitialiser le ComboBox pour le jour
        joursComboBox.getSelectionModel().clearSelection();
        // Réinitialiser la sélection dans la TableView si nécessaire
        disponibiliteTableView.getSelectionModel().clearSelection();
        // Si vous avez d'autres contrôles ou états à réinitialiser, ajoutez-les ici
    }

    private void handleRowDoubleClick() {
        Disponibilites selectedDisponibilite = disponibiliteTableView.getSelectionModel().getSelectedItem();
        if (selectedDisponibilite != null) {
            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmer la suppression");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette disponibilité ?");
            // Attendre la réponse de l'utilisateur
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                // Supprimer la disponibilité
                boolean success = disponibilitesService.deleteDisponibilite(selectedDisponibilite.getDisponibiliteID());
                if (success) {
                    showAlert("Succès", "Disponibilité supprimée avec succès.");
                    // Recharger les disponibilités dans la TableView
                    loadDisponibilites();
                    clearControls();
                    this.selectedDisponibilite = null;
                } else {
                    showAlert("Erreur", "Une erreur s'est produite lors de la suppression de la disponibilité.");
                }
            }
        }
    }

    @FXML
    private void handleAddOrUpdateActe(ActionEvent event) {
        Acte selectedActe = tvActe.getSelectionModel().getSelectedItem();
        if (selectedActe != null) {
            // Update existing Acte
            selectedActe.setNomActe(acteTf.getText());
            selectedActe.setPrix(Double.parseDouble(prixActeTf.getText()));
            if (acteService.updateActe(selectedActe)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Acte updated successfully!");
                tvActe.refresh();
                clearActe();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Acte.");
            }
        } else {
            // Add new Acte
            Acte newActe = new Acte();//(acteTf.getText(), Double.parseDouble(prixActeTf.getText()));
            newActe.setNomActe(acteTf.getText());
            newActe.setPrix(Double.parseDouble(prixActeTf.getText()));
            if (acteService.addActe(newActe)) {
                actesList.add(newActe);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Acte added successfully!");
                newActe = null;
                clearActe();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add Acte.");
            }
        }

    }

    @FXML
    private void handleAddOrUpdateMedicament(ActionEvent event) {
        Medicaments selectedMedicament = tvMedicament.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            // Update existing Medicament
            selectedMedicament.setNomMedicament(tfMedicament.getText());
            selectedMedicament.setFormeDosage(tfFormeDosage.getText());
            if (medicamentsService.updateMedicaments(selectedMedicament)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicament updated successfully!");
                tvMedicament.refresh();
                clearMedicament();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Medicament.");
            }
        } else {
            // Add new Medicament
            Medicaments newMedicament = new Medicaments();//tfMedicament.getText(), tfFormeDosage.getText());
            newMedicament.setNomMedicament(tfMedicament.getText());
            newMedicament.setFormeDosage(tfFormeDosage.getText());
            if (medicamentsService.saveMedicaments(newMedicament)) {
                medicamentsList.add(newMedicament);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicament added successfully!");
                newMedicament = null;
                clearMedicament();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add Medicament.");
            }
        }

    }

    @FXML
    private void handleAddOrUpdateImagerie(ActionEvent event) {
        TypeImagerie selectedImagerie = tvImagerie.getSelectionModel().getSelectedItem();
        if (selectedImagerie != null) {
            // Update existing Imagerie
            selectedImagerie.setNomImagerieFr(tfImagerie.getText());
            selectedImagerie.setDescription(tfDescriptionImagerie.getText());
            selectedImagerie.setCodeImagerieFr(tfCodeImagerie.getText());
            if (typeImagerieService.updateTypeImagerie(selectedImagerie)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Imagerie updated successfully!");
                tvImagerie.refresh();
                selectedImagerie = null;
                clearTypeImagerie();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Imagerie.");
            }
        } else {
            // Add new Imagerie
            TypeImagerie newImagerie = new TypeImagerie();//tfImagerie.getText(), tfDescriptionImagerie.getText());
            newImagerie.setNomImagerieFr(tfImagerie.getText());
            newImagerie.setDescription(tfDescriptionImagerie.getText());
            if (typeImagerieService.saveTypeImagerie(newImagerie)) {
                imagerieList.add(newImagerie);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Imagerie added successfully!");
                newImagerie = null;
                clearTypeImagerie();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add Imagerie.");
            }
        }

    }

    @FXML
    private void handleAddOrUpdateAnalyse(ActionEvent event) {
        // Récupération des valeurs des champs de texte
        String nomAnalyse = analyseTf.getText();
        String description = descriptionAnalyse.getText();
        String code = codeFrAnalyse.getText();

        // Vérification des champs obligatoires
        if (nomAnalyse.isEmpty() || description.isEmpty() || code.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Récupérer l'analyse sélectionnée ou en créer une nouvelle
        TypeAnalyse analyse;
        TypeAnalyse selectedAnalyse = tvAnalyse.getSelectionModel().getSelectedItem();
        System.out.println(selectedAnalyse.toString());

        if (selectedAnalyse != null) {
            // Mise à jour de l'analyse existante
            analyse = selectedAnalyse;
            analyse.setNomAnalyseFr(nomAnalyse);
            analyse.setDescription(description);
            analyse.setCodeAnalyseFr(code);

            // Mise à jour de l'analyse dans le service
            if (typeAnalyseService.update(analyse)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Analyse mise à jour avec succès!");
                tvAnalyse.refresh();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour de l'analyse.");
            }
        } else {
            // Création d'une nouvelle analyse
            analyse = new TypeAnalyse();
            analyse.setNomAnalyseFr(nomAnalyse);
            analyse.setDescription(description);

            // Sauvegarde de la nouvelle analyse dans le service
            if (typeAnalyseService.save(analyse)) {
                //analyseList.add(analyse);
                loadTableViewData();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Analyse ajoutée avec succès!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'analyse.");
            }
        }

        // Réinitialisation de l'objet analyse et des champs du formulaire
        clearAnalyse();
        selectedAnalyse = null; // Réinitialiser l'analyse sélectionnée
    }

    private <T> void handleTableViewDelete(TableView<T> tableView, Function<T, Boolean> deleteAction) {
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    T selectedItem = row.getItem();

                    // Afficher l'alerte de confirmation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Delete Confirmation");
                    alert.setContentText("Are you sure you want to delete this item?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Appeler la méthode de suppression en passant l'objet directement
                        if (deleteAction.apply(selectedItem)) {
                            System.out.println(selectedItem.toString());
                            tableView.getItems().remove(selectedItem);
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Item deleted successfully!");
                            clearMedicament();  // Vider les champs de texte après la suppression
                            clearActe();  // Vider les champs de texte après la suppression
                            clearAnalyse();  // Vider les champs de texte après la suppression
                            clearTypeImagerie();  // Vider les champs de texte après la suppression
                            selectedItem = null;
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete item.");
                        }
                    }
                }
            });
            return row;
        });
    }

    private void clearMedicament() {
        tfMedicament.clear();
        tfFormeDosage.clear();
    }

    private void clearActe() {
        acteTf.clear();
        prixActeTf.clear();
    }

    private void clearTypeImagerie() {
        tfImagerie.clear();
        tfDescriptionImagerie.clear();
        tfCodeImagerie.clear();
    }

    private void clearAnalyse() {
        analyseTf.clear();
        descriptionAnalyse.clear();
        codeFrAnalyse.clear();
    }

    private void handleDeleteAnalyse(TypeAnalyse analyse) {
        if (analyse != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer cette analyse ?");
            confirmationAlert.setContentText("Analyse: " + analyse.getNomAnalyseFr() + " id : " + analyse.getIdTypeAnalyse());

            // Attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Si l'utilisateur confirme la suppression
                boolean success = typeAnalyseService.delete(analyse);
                if (success) {
                    analyseList.remove(analyse);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Analyse supprimée avec succès!");
                    clearAnalyse();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de l'analyse.");
                }
            }
        }
    }

    private void handleDeleteActe(Acte acte) {
        if (acte != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer cet acte ?");
            confirmationAlert.setContentText("Acte: " + acte.getNomActe() + " id = " + acte.getIdActe());

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = acteService.deleteActe(acte);
                if (success) {
                    actesList.remove(acte);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Acte supprimé avec succès!");
                    clearActe();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de l'acte.");
                }
            }
        }
    }

    private void handleDeleteMedicament(Medicaments medicament) {
        if (medicament != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer ce médicament ?");
            confirmationAlert.setContentText("Médicament: " + medicament.getNomMedicament());

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = medicamentsService.deleteMedicaments(medicament);
                if (success) {
                    medicamentsList.remove(medicament);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Médicament supprimé avec succès!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression du médicament.");
                }
            }
        }
    }

    private void handleDeleteImagerie(TypeImagerie imagerie) {
        if (imagerie != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer cette imagerie ?");
            confirmationAlert.setContentText("Imagerie: " + imagerie.getNomImagerieFr() + " id : " + imagerie.getIdTypeImagerie());

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = typeImagerieService.deleteTypeImagerie(imagerie);
                if (success) {
                    imagerieList.remove(imagerie);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Imagerie supprimée avec succès!");
                    clearTypeImagerie();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression de l'imagerie.");
                }
            }
        }
    }

    // Ensures that the configuration directory exists
    private void ensureConfigDirectoryExists() {
        File configFile = new File(configFilePath);
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
    }

    // Loads properties from the config file
    private Properties loadConfigProperties() {
        Properties configProperties = new Properties();
        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                configProperties.load(input);
            } catch (IOException e) {
                showErrorDialog("Error reading configuration file", e.getMessage());
            }
        } else {
            // Optional: Initialize with default properties
            configProperties.setProperty("output.directory", "D:/output");
            saveConfigProperties(configProperties); // Save default properties if file does not exist
        }

        return configProperties;
    }

    // Saves properties to the config file
    private void saveConfigProperties(Properties configProperties) {
        try (OutputStream output = new FileOutputStream(configFilePath)) {
            configProperties.store(output, "Application Configuration");
        } catch (IOException e) {
            showErrorDialog("Error writing to configuration file", e.getMessage());
        }
    }

    @FXML
    private void handleBrowseOutputDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            Properties properties = loadConfigProperties();
            properties.setProperty("output.directory", selectedDirectory.getAbsolutePath());
            saveConfigProperties(properties);

            // Update the UI with the new path
            outputDirectoryTextField.setText(selectedDirectory.getAbsolutePath());

            // Optionally update the UI or inform the user
            Alert alert = new Alert(AlertType.INFORMATION, "Output directory updated to: " + selectedDirectory.getAbsolutePath(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
