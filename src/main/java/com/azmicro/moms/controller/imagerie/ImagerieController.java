package com.azmicro.moms.controller.imagerie;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.service.ImagerieService;
import com.azmicro.moms.service.TypeImagerieService;
import com.azmicro.moms.util.DatabaseUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class ImagerieController implements Initializable {

    @FXML
    private Button btnAjouterLigneImagerie;
    @FXML
    private Button btnSaveImagerie;
    @FXML
    private Button btnCancel;
    @FXML
    private VBox vboxLignesImagerie;
    @FXML
    private Label lblNombreImageries;

    private ObservableList<String> allTypeImageries;
    private TypeImagerieService typeImagerieService;
    private ImagerieService imagerieService;
    private Consultations consultation;
    private DossierController dossierController;

    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController;
    }

    public void setConsultation(Consultations selectedConsultation) {
        this.consultation = selectedConsultation;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.typeImagerieService = new TypeImagerieService();
        this.imagerieService = new ImagerieService();
        allTypeImageries = FXCollections.observableArrayList(typeImagerieService.getAllTypeImagerie());
        updateImageryCount();
    }

    @FXML
    private void ajouterLigneImagerie(ActionEvent event) {
        VBox ligneVBox = createImagerieLine();
        vboxLignesImagerie.getChildren().add(ligneVBox);
        updateImageryCount();
    }

    private VBox createImagerieLine() {
        VBox ligneVBox = new VBox(10.0);
        ligneVBox.setPadding(new Insets(15));
        ligneVBox.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        // Header avec numéro et bouton supprimer
        HBox headerBox = new HBox(10.0);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        FontIcon imageIcon = new FontIcon("fas-x-ray");
        imageIcon.setIconSize(18);
        imageIcon.setIconColor(javafx.scene.paint.Color.web("#3498db"));
        
        Label numeroLabel = new Label("Imagerie #" + (vboxLignesImagerie.getChildren().size() + 1));
        numeroLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button btnSupprimer = new Button("Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
        FontIcon deleteIcon = new FontIcon("fas-trash");
        deleteIcon.setIconSize(12);
        deleteIcon.setIconColor(javafx.scene.paint.Color.WHITE);
        btnSupprimer.setGraphic(deleteIcon);
        btnSupprimer.setOnAction(e -> {
            vboxLignesImagerie.getChildren().remove(ligneVBox);
            updateImageryCount();
            renumberImageries();
        });
        
        headerBox.getChildren().addAll(imageIcon, numeroLabel, spacer, btnSupprimer);

        // Date picker
        HBox dateBox = new HBox(10.0);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        Label dateLabel = new Label("Date :");
        dateLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-min-width: 120px;");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(200);
        datePicker.setStyle("-fx-font-size: 12px;");
        dateBox.getChildren().addAll(dateLabel, datePicker);

        // Type d'imagerie avec recherche améliorée
        HBox typeBox = new HBox(10.0);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox typeVBox = new VBox(5.0);
        Label typeLabel = new Label("Type d'imagerie :");
        typeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        HBox searchComboBox = new HBox(5.0);
        searchComboBox.setAlignment(Pos.CENTER_LEFT);
        
        // Icône de recherche
        FontIcon searchIcon = new FontIcon("fas-search");
        searchIcon.setIconSize(14);
        searchIcon.setIconColor(javafx.scene.paint.Color.web("#7f8c8d"));
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un type d'imagerie...");
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-font-size: 12px; -fx-border-color: #3498db; -fx-border-radius: 3px;");
        
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(allTypeImageries));
        typeComboBox.setPrefWidth(300);
        typeComboBox.setPromptText("Sélectionner un type");
        typeComboBox.setStyle("-fx-font-size: 12px;");
        typeComboBox.setEditable(false);
        
        // Logique de recherche améliorée
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                typeComboBox.setItems(FXCollections.observableArrayList(allTypeImageries));
                typeComboBox.hide();
            } else {
                ObservableList<String> filtered = allTypeImageries.stream()
                    .filter(item -> item.toLowerCase().contains(newVal.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
                typeComboBox.setItems(filtered);
                
                // Ouvrir automatiquement le ComboBox si des résultats existent
                if (!filtered.isEmpty()) {
                    if (!typeComboBox.isShowing()) {
                        typeComboBox.show();
                    }
                    
                    // Sélectionner automatiquement si un seul résultat
                    if (filtered.size() == 1) {
                        typeComboBox.getSelectionModel().selectFirst();
                    }
                } else {
                    typeComboBox.hide();
                }
            }
        });
        
        // Remplir le champ de recherche quand un élément est sélectionné dans le ComboBox
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                searchField.setText(newVal);
                typeComboBox.hide();
            }
        });
        
        // Focus sur le champ de recherche efface la sélection du ComboBox pour permettre une nouvelle recherche
        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchField.selectAll();
            }
        });
        
        searchComboBox.getChildren().addAll(searchIcon, searchField, typeComboBox);
        typeVBox.getChildren().addAll(typeLabel, searchComboBox);

        // Description/Résultat
        VBox descVBox = new VBox(5.0);
        Label descLabel = new Label("Résultat / Description :");
        descLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        TextField descField = new TextField();
        descField.setPromptText("Entrez le résultat ou une description...");
        descField.setPrefWidth(600);
        descField.setStyle("-fx-font-size: 12px;");
        descVBox.getChildren().addAll(descLabel, descField);

        ligneVBox.getChildren().addAll(headerBox, dateBox, typeVBox, descVBox);
        return ligneVBox;
    }

    private void updateImageryCount() {
        int count = vboxLignesImagerie.getChildren().size();
        lblNombreImageries.setText(count + " imagerie(s) ajoutée(s)");
    }

    private void renumberImageries() {
        for (int i = 0; i < vboxLignesImagerie.getChildren().size(); i++) {
            Node node = vboxLignesImagerie.getChildren().get(i);
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                HBox headerBox = (HBox) vbox.getChildren().get(0);
                Label numeroLabel = (Label) headerBox.getChildren().get(1);
                numeroLabel.setText("Imagerie #" + (i + 1));
            }
        }
    }
    @FXML
    private void saveBilan(ActionEvent event) {
        if (vboxLignesImagerie.getChildren().isEmpty()) {
            showAlert("Avertissement", "Veuillez ajouter au moins une imagerie.");
            return;
        }

        if (consultation == null) {
            showAlert("Erreur", "Aucune consultation sélectionnée.");
            return;
        }

        boolean hasError = false;
        for (Node node : vboxLignesImagerie.getChildren()) {
            if (node instanceof VBox) {
                VBox ligneVBox = (VBox) node;

                // Extraire les champs
                HBox dateBox = (HBox) ligneVBox.getChildren().get(1);
                DatePicker datePicker = (DatePicker) dateBox.getChildren().get(1);

                VBox typeVBox = (VBox) ligneVBox.getChildren().get(2);
                HBox searchComboBox = (HBox) typeVBox.getChildren().get(1);
                @SuppressWarnings("unchecked")
                ComboBox<String> typeComboBox = (ComboBox<String>) searchComboBox.getChildren().get(1);

                VBox descVBox = (VBox) ligneVBox.getChildren().get(3);
                TextField descField = (TextField) descVBox.getChildren().get(1);

                // Validation
                if (typeComboBox.getValue() == null || typeComboBox.getValue().trim().isEmpty()) {
                    showAlert("Validation", "Veuillez sélectionner un type d'imagerie pour toutes les lignes.");
                    hasError = true;
                    break;
                }

                if (datePicker.getValue() == null) {
                    showAlert("Validation", "Veuillez sélectionner une date pour toutes les imageries.");
                    hasError = true;
                    break;
                }

                // Créer l'objet Imagerie
                Imagerie imagerie = new Imagerie();
                String selectedItem = typeComboBox.getValue();
                String[] parts = selectedItem.split(":");
                if (parts.length == 2) {
                    String nomImagerie = parts[0].trim();
                    imagerie.setTypeImagerie(typeImagerieService.findByNomImagerieFr(nomImagerie));
                }

                imagerie.setDescription(descField.getText());
                imagerie.setResultat(descField.getText());
                imagerie.setDateImagerie(datePicker.getValue());
                imagerie.setConsultationID(consultation.getConsultationID());

                // Sauvegarder
                imagerieService.saveImagerie(imagerie);
            }
        }

        if (!hasError) {
            // Fermer la fenêtre
            Stage stage = (Stage) btnSaveImagerie.getScene().getWindow();
            stage.close();

            // Rafraîchir le dossier
            if (dossierController != null) {
                Platform.runLater(() -> {
                    try {
                        dossierController.refreshImagerie();
                        dossierController.initializeConsultationsDates(consultation.getPatient().getPatientID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(btnSaveImagerie.getScene().getWindow());
        alert.showAndWait();
    }
}
