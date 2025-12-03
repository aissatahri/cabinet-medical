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
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
    private List<Imagerie> historique = new ArrayList<>();
    
    // Templates prédéfinis
    private static final String[] QUICK_TEMPLATES = {
        "Radiographie thoracique",
        "Scanner cérébral",
        "IRM lombaire",
        "Échographie abdominale"
    };

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
    
    @FXML
    private void addTemplateRadio(ActionEvent event) {
        addQuickTemplate("Radiographie thoracique", "Radio thorax de face et profil");
    }
    
    @FXML
    private void addTemplateScanner(ActionEvent event) {
        addQuickTemplate("Scanner cérébral", "Scanner cérébral sans injection");
    }
    
    @FXML
    private void addTemplateIRM(ActionEvent event) {
        addQuickTemplate("IRM lombaire", "IRM lombaire avec injection de produit de contraste");
    }
    
    @FXML
    private void addTemplateEcho(ActionEvent event) {
        addQuickTemplate("Échographie abdominale", "Échographie abdominale complète");
    }
    
    private void addQuickTemplate(String type, String description) {
        VBox ligneVBox = createImagerieLine();
        
        // Remplir automatiquement les champs
        VBox typeVBox = (VBox) ligneVBox.getChildren().get(2);
        HBox typeBox = (HBox) typeVBox.getChildren().get(1);
        @SuppressWarnings("unchecked")
        ComboBox<String> typeComboBox = (ComboBox<String>) typeBox.getChildren().get(1);
        typeComboBox.getEditor().setText(type);
        
        VBox descVBox = (VBox) ligneVBox.getChildren().get(3);
        javafx.scene.control.TextArea descArea = (javafx.scene.control.TextArea) descVBox.getChildren().get(1);
        descArea.setText(description);
        
        vboxLignesImagerie.getChildren().add(ligneVBox);
        updateImageryCount();
    }
    
    @FXML
    private void clearAll(ActionEvent event) {
        if (vboxLignesImagerie.getChildren().isEmpty()) {
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Tout effacer");
        alert.setContentText("Voulez-vous vraiment supprimer toutes les imageries ?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Animation de suppression
            for (Node node : new ArrayList<>(vboxLignesImagerie.getChildren())) {
                javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), node);
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.setOnFinished(e -> {
                    vboxLignesImagerie.getChildren().remove(node);
                    updateImageryCount();
                });
                fade.play();
            }
        }
    }
    
    private VBox duplicateLine(VBox original) {
        VBox newLine = createImagerieLine();
        
        // Copier la date
        HBox origDateBox = (HBox) original.getChildren().get(1);
        DatePicker origDatePicker = (DatePicker) origDateBox.getChildren().get(1);
        HBox newDateBox = (HBox) newLine.getChildren().get(1);
        DatePicker newDatePicker = (DatePicker) newDateBox.getChildren().get(1);
        newDatePicker.setValue(origDatePicker.getValue());
        
        // Copier le type
        VBox origTypeVBox = (VBox) original.getChildren().get(2);
        HBox origTypeBox = (HBox) origTypeVBox.getChildren().get(1);
        @SuppressWarnings("unchecked")
        ComboBox<String> origTypeCombo = (ComboBox<String>) origTypeBox.getChildren().get(1);
        
        VBox newTypeVBox = (VBox) newLine.getChildren().get(2);
        HBox newTypeBox = (HBox) newTypeVBox.getChildren().get(1);
        @SuppressWarnings("unchecked")
        ComboBox<String> newTypeCombo = (ComboBox<String>) newTypeBox.getChildren().get(1);
        newTypeCombo.getEditor().setText(origTypeCombo.getEditor().getText());
        
        // Copier la description
        VBox origDescVBox = (VBox) original.getChildren().get(3);
        javafx.scene.control.TextArea origDescArea = (javafx.scene.control.TextArea) origDescVBox.getChildren().get(1);
        VBox newDescVBox = (VBox) newLine.getChildren().get(3);
        javafx.scene.control.TextArea newDescArea = (javafx.scene.control.TextArea) newDescVBox.getChildren().get(1);
        newDescArea.setText(origDescArea.getText());
        
        return newLine;
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

        // Type d'imagerie avec ComboBox éditable combiné
        VBox typeVBox = new VBox(5.0);
        Label typeLabel = new Label("Type d'imagerie :");
        typeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        HBox typeBox = new HBox(8.0);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        
        // Icône de recherche
        FontIcon searchIcon = new FontIcon("fas-search");
        searchIcon.setIconSize(14);
        searchIcon.setIconColor(javafx.scene.paint.Color.web("#7f8c8d"));
        
        // ComboBox éditable qui combine recherche et saisie libre
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(allTypeImageries));
        typeComboBox.setPrefWidth(450);
        typeComboBox.setPromptText("Tapez pour rechercher ou saisir librement...");
        typeComboBox.setEditable(true);
        typeComboBox.setStyle("-fx-font-size: 12px;");
        
        // Filtrage automatique lors de la saisie
        typeComboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                typeComboBox.setItems(FXCollections.observableArrayList(allTypeImageries));
                typeComboBox.hide();
            } else {
                String input = newVal.toLowerCase();
                ObservableList<String> filtered = allTypeImageries.stream()
                    .filter(item -> item.toLowerCase().contains(input))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
                
                // Garder le texte saisi même s'il n'est pas dans la liste
                if (!filtered.isEmpty()) {
                    typeComboBox.setItems(filtered);
                    if (!typeComboBox.isShowing()) {
                        typeComboBox.show();
                    }
                } else {
                    // Permettre la saisie libre
                    typeComboBox.hide();
                }
            }
        });
        
        // Validation visuelle
        typeComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Perte de focus
                String text = typeComboBox.getEditor().getText();
                if (text == null || text.trim().isEmpty()) {
                    typeComboBox.setStyle("-fx-font-size: 12px; -fx-border-color: #e74c3c; -fx-border-width: 2px;");
                } else {
                    typeComboBox.setStyle("-fx-font-size: 12px; -fx-border-color: #27ae60; -fx-border-width: 2px;");
                }
            }
        });
        
        typeBox.getChildren().addAll(searchIcon, typeComboBox);
        typeVBox.getChildren().addAll(typeLabel, typeBox);

        // Description/Résultat avec TextArea enrichie
        VBox descVBox = new VBox(5.0);
        HBox descHeaderBox = new HBox(10.0);
        descHeaderBox.setAlignment(Pos.CENTER_LEFT);
        
        Label descLabel = new Label("Résultat / Description :");
        descLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        
        Label charCountLabel = new Label("0 / 500");
        charCountLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #95a5a6;");
        
        descHeaderBox.getChildren().addAll(descLabel, spacer2, charCountLabel);
        
        javafx.scene.control.TextArea descArea = new javafx.scene.control.TextArea();
        descArea.setPromptText("Entrez le résultat, description ou indication...");
        descArea.setPrefRowCount(3);
        descArea.setPrefWidth(600);
        descArea.setWrapText(true);
        descArea.setStyle("-fx-font-size: 12px;");
        
        // Compteur de caractères
        descArea.textProperty().addListener((obs, oldVal, newVal) -> {
            int length = newVal != null ? newVal.length() : 0;
            charCountLabel.setText(length + " / 500");
            if (length > 500) {
                charCountLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            } else if (length > 400) {
                charCountLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #f39c12;");
            } else {
                charCountLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #95a5a6;");
            }
        });
        
        // Boutons snippets
        HBox snippetsBox = new HBox(5.0);
        snippetsBox.setAlignment(Pos.CENTER_LEFT);
        
        Button btnNormal = new Button("Normal");
        btnNormal.setStyle("-fx-font-size: 10px; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
        btnNormal.setOnAction(e -> descArea.appendText("Examen normal, pas d'anomalie décelée. "));
        
        Button btnAnormal = new Button("Anomalie détectée");
        btnAnormal.setStyle("-fx-font-size: 10px; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        btnAnormal.setOnAction(e -> descArea.appendText("Anomalie détectée nécessitant investigation complémentaire. "));
        
        Button btnSuivi = new Button("À suivre");
        btnSuivi.setStyle("-fx-font-size: 10px; -fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
        btnSuivi.setOnAction(e -> descArea.appendText("À contrôler lors du prochain suivi. "));
        
        snippetsBox.getChildren().addAll(btnNormal, btnAnormal, btnSuivi);
        
        descVBox.getChildren().addAll(descHeaderBox, descArea, snippetsBox);

        // Bouton dupliquer
        HBox actionBox = new HBox(8.0);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnDuplicate = new Button("Dupliquer");
        btnDuplicate.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
        FontIcon dupIcon = new FontIcon("fas-copy");
        dupIcon.setIconSize(12);
        dupIcon.setIconColor(javafx.scene.paint.Color.WHITE);
        btnDuplicate.setGraphic(dupIcon);
        btnDuplicate.setOnAction(e -> {
            VBox duplicatedLine = duplicateLine(ligneVBox);
            int currentIndex = vboxLignesImagerie.getChildren().indexOf(ligneVBox);
            vboxLignesImagerie.getChildren().add(currentIndex + 1, duplicatedLine);
            updateImageryCount();
            renumberImageries();
        });
        
        actionBox.getChildren().add(btnDuplicate);

        ligneVBox.getChildren().addAll(headerBox, dateBox, typeVBox, descVBox, actionBox);
        
        // Animation d'apparition
        ligneVBox.setOpacity(0);
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), ligneVBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
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
                HBox typeBox = (HBox) typeVBox.getChildren().get(1);
                @SuppressWarnings("unchecked")
                ComboBox<String> typeComboBox = (ComboBox<String>) typeBox.getChildren().get(1);

                VBox descVBox = (VBox) ligneVBox.getChildren().get(3);
                javafx.scene.control.TextArea descArea = (javafx.scene.control.TextArea) descVBox.getChildren().get(1);

                // Validation
                String typeText = typeComboBox.getEditor().getText();
                if (typeText == null || typeText.trim().isEmpty()) {
                    showAlert("Validation", "Veuillez sélectionner ou saisir un type d'imagerie pour toutes les lignes.");
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
                
                // Rechercher le type dans la base ou créer avec le texte saisi
                String selectedItem = typeText;
                String[] parts = selectedItem.split(":");
                if (parts.length >= 1) {
                    String nomImagerie = parts[0].trim();
                    imagerie.setTypeImagerie(typeImagerieService.findByNomImagerieFr(nomImagerie));
                }

                imagerie.setDescription(descArea.getText());
                imagerie.setResultat(descArea.getText());
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

    public void loadExistingImageries() {
        if (consultation == null) {
            return;
        }
        
        List<Imagerie> imageries = imagerieService.findByConsultationId(consultation.getConsultationID());
        
        for (Imagerie imagerie : imageries) {
            VBox line = createImagerieLine();
            
            DatePicker datePicker = (DatePicker) line.getChildren().get(3);
            ComboBox<String> typeCombo = (ComboBox<String>) line.getChildren().get(5);
            TextArea descArea = (TextArea) line.getChildren().get(7);
            
            if (imagerie.getDateImagerie() != null) {
                datePicker.setValue(imagerie.getDateImagerie());
            }
            
            if (imagerie.getTypeImagerie() != null) {
                String typeName = imagerie.getTypeImagerie().getNomImagerieFr();
                String typeCode = imagerie.getTypeImagerie().getCodeImagerieFr();
                if (typeCode != null && !typeCode.isEmpty()) {
                    typeCombo.getEditor().setText(typeName + ":" + typeCode);
                } else {
                    typeCombo.getEditor().setText(typeName);
                }
                typeCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
            }
            
            if (imagerie.getResultat() != null && !imagerie.getResultat().isEmpty()) {
                descArea.setText(imagerie.getResultat());
            }
        }
    }
}

