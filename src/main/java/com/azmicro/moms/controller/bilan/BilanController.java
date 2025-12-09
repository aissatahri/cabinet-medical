package com.azmicro.moms.controller.bilan;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.dao.AnalyseDAOImpl;
import com.azmicro.moms.model.Analyse;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.TypeAnalyse;
import com.azmicro.moms.service.AnalyseService;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import com.azmicro.moms.service.TypeAnalyseService;
import com.azmicro.moms.util.DatabaseUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.kordamp.ikonli.javafx.FontIcon;

public class BilanController implements Initializable {

    @FXML
    private VBox vboxLignesBilan;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button btnAjouterLigneBilan;
    @FXML
    private Button btnAjouteBilan;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblNombreAnalyses;

    private ObservableList<String> allTypeAnalyses;
    private TypeAnalyseService typeAnalyseService;
    private AnalyseService analyseService;
    private Consultations consultation;
    private DossierController dossierController;

    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController;
    }

    public void setConsultation(Consultations consultation) {
        this.consultation = consultation;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            typeAnalyseService = new TypeAnalyseService();
            AnalyseDAOImpl analyseDAO = new AnalyseDAOImpl(connection);
            this.analyseService = new AnalyseService(analyseDAO);
            allTypeAnalyses = FXCollections.observableArrayList(typeAnalyseService.getAllTypeAnalyses());
            updateAnalyseCount();
        } catch (SQLException ex) {
            Logger.getLogger(BilanController.class.getName()).log(Level.SEVERE, "Erreur lors de la connexion à la base de données", ex);
        }
    }

    @FXML
    private void ajouterLigneBilan(ActionEvent event) {
        createAnalyseLine();
    }

    private VBox createAnalyseLine() {
        VBox lineContainer = new VBox(8);
        lineContainer.setPadding(new Insets(12));
        lineContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );

        int lineNumber = vboxLignesBilan.getChildren().size() + 1;
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label numberLabel = new Label("Analyse #" + lineNumber);
        numberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #9b59b6;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button deleteBtn = new Button();
        FontIcon trashIcon = new FontIcon("fas-trash");
        trashIcon.setIconSize(14);
        deleteBtn.setGraphic(trashIcon);
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e -> {
            vboxLignesBilan.getChildren().remove(lineContainer);
            renumberAnalyses();
            updateAnalyseCount();
        });
        
        header.getChildren().addAll(numberLabel, spacer, deleteBtn);

        Label dateLabel = new Label("Date de l'analyse:");
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(200);

        Label typeLabel = new Label("Type d'analyse:");
        typeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        
        ComboBox<String> typeComboBox = new ComboBox<>(FXCollections.observableArrayList(allTypeAnalyses));
        typeComboBox.setEditable(true);
        typeComboBox.setPrefWidth(400);
        typeComboBox.setPromptText("Rechercher ou saisir un type d'analyse...");
        
        typeComboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!typeComboBox.isShowing()) {
                typeComboBox.show();
            }
            String filter = newVal.toLowerCase();
            ObservableList<String> filtered = allTypeAnalyses.stream()
                .filter(item -> item.toLowerCase().contains(filter))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            typeComboBox.setItems(filtered);
            
            boolean isValid = !newVal.trim().isEmpty();
            typeComboBox.setStyle(isValid 
                ? "-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;" 
                : "-fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-border-radius: 4px;");
        });

        Label resultLabel = new Label("Résultats / Description:");
        resultLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        
        TextArea resultArea = new TextArea();
        resultArea.setPrefRowCount(4);
        resultArea.setWrapText(true);
        resultArea.setPromptText("Saisir les résultats de l'analyse ou des observations...");
        
        Label charCountLabel = new Label("0/500");
        charCountLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");
        charCountLabel.setAlignment(Pos.CENTER_RIGHT);
        
        resultArea.textProperty().addListener((obs, oldVal, newVal) -> {
            int length = newVal.length();
            if (length > 500) {
                resultArea.setText(oldVal);
                return;
            }
            charCountLabel.setText(length + "/500");
            if (length > 450) {
                charCountLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            } else if (length > 400) {
                charCountLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #f39c12;");
            } else {
                charCountLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");
            }
        });

        HBox snippetBox = new HBox(8);
        snippetBox.setAlignment(Pos.CENTER_LEFT);
        Label snippetLabel = new Label("Textes rapides:");
        snippetLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #777;");
        
        Button normalBtn = createSnippetButton("Normal", "#27ae60", resultArea);
        Button anomalieBtn = createSnippetButton("Anomalie", "#e74c3c", resultArea);
        Button suiviBtn = createSnippetButton("À suivre", "#f39c12", resultArea);
        
        snippetBox.getChildren().addAll(snippetLabel, normalBtn, anomalieBtn, suiviBtn);

        HBox actionBox = new HBox(8);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        
        Button duplicateBtn = new Button("Dupliquer");
        FontIcon copyIcon = new FontIcon("fas-copy");
        copyIcon.setIconSize(12);
        duplicateBtn.setGraphic(copyIcon);
        duplicateBtn.getStyleClass().add("secondary-button");
        duplicateBtn.setOnAction(e -> duplicateLine(lineContainer));
        
        actionBox.getChildren().add(duplicateBtn);

        lineContainer.getChildren().addAll(
            header,
            new javafx.scene.control.Separator(),
            dateLabel, datePicker,
            typeLabel, typeComboBox,
            resultLabel, resultArea,
            charCountLabel,
            snippetBox,
            actionBox
        );

        lineContainer.setOpacity(0);
        vboxLignesBilan.getChildren().add(lineContainer);
        updateAnalyseCount();

        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), lineContainer);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        return lineContainer;
    }

    private Button createSnippetButton(String text, String color, TextArea targetArea) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 10px;" +
            "-fx-padding: 4 8 4 8;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        btn.setOnAction(e -> {
            String current = targetArea.getText();
            String snippet = text + ": ";
            if (!current.isEmpty() && !current.endsWith("\n")) {
                snippet = "\n" + snippet;
            }
            targetArea.appendText(snippet);
            targetArea.requestFocus();
            targetArea.positionCaret(targetArea.getLength());
        });
        return btn;
    }

    @FXML
    private void addTemplateSang() {
        VBox line = createAnalyseLine();
        ComboBox<String> typeCombo = (ComboBox<String>) line.getChildren().get(5);
        typeCombo.getEditor().setText("Bilan sanguin complet: NFS, CRP, VS");
        typeCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
    }

    @FXML
    private void addTemplateUrine() {
        VBox line = createAnalyseLine();
        ComboBox<String> typeCombo = (ComboBox<String>) line.getChildren().get(5);
        typeCombo.getEditor().setText("Analyse d'urine: ECBU");
        typeCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
    }

    @FXML
    private void addTemplateHormonal() {
        VBox line = createAnalyseLine();
        ComboBox<String> typeCombo = (ComboBox<String>) line.getChildren().get(5);
        typeCombo.getEditor().setText("Bilan hormonal: TSH, T3, T4");
        typeCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
    }

    @FXML
    private void addTemplateThyroid() {
        VBox line = createAnalyseLine();
        ComboBox<String> typeCombo = (ComboBox<String>) line.getChildren().get(5);
        typeCombo.getEditor().setText("Bilan thyroïdien: TSH, T4 libre, anticorps anti-TPO");
        typeCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
    }

    private void duplicateLine(VBox sourceLine) {
        DatePicker sourceDatePicker = (DatePicker) sourceLine.getChildren().get(3);
        ComboBox<String> sourceTypeCombo = (ComboBox<String>) sourceLine.getChildren().get(5);
        TextArea sourceResultArea = (TextArea) sourceLine.getChildren().get(7);

        VBox newLine = createAnalyseLine();
        DatePicker newDatePicker = (DatePicker) newLine.getChildren().get(3);
        ComboBox<String> newTypeCombo = (ComboBox<String>) newLine.getChildren().get(5);
        TextArea newResultArea = (TextArea) newLine.getChildren().get(7);

        newDatePicker.setValue(sourceDatePicker.getValue());
        newTypeCombo.getEditor().setText(sourceTypeCombo.getEditor().getText());
        newResultArea.setText(sourceResultArea.getText());
    }

    @FXML
    private void clearAll() {
        if (vboxLignesBilan.getChildren().isEmpty()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Effacer toutes les analyses?");
        alert.setContentText("Cette action supprimera toutes les lignes d'analyse. Continuer?");
        alert.initOwner(rootPane.getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            vboxLignesBilan.getChildren().clear();
            updateAnalyseCount();
        }
    }

    private void updateAnalyseCount() {
        int count = vboxLignesBilan.getChildren().size();
        if (lblNombreAnalyses != null) {
            lblNombreAnalyses.setText(count + " analyse" + (count > 1 ? "s" : ""));
        }
    }

    private void renumberAnalyses() {
        for (int i = 0; i < vboxLignesBilan.getChildren().size(); i++) {
            VBox lineContainer = (VBox) vboxLignesBilan.getChildren().get(i);
            HBox header = (HBox) lineContainer.getChildren().get(0);
            Label numberLabel = (Label) header.getChildren().get(0);
            numberLabel.setText("Analyse #" + (i + 1));
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void ajouterBilan(ActionEvent event) {
        int savedCount = 0;
        
        for (Node node : vboxLignesBilan.getChildren()) {
            if (node instanceof VBox) {
                VBox lineContainer = (VBox) node;
                
                DatePicker datePicker = (DatePicker) lineContainer.getChildren().get(3);
                ComboBox<String> typeComboBox = (ComboBox<String>) lineContainer.getChildren().get(5);
                TextArea resultArea = (TextArea) lineContainer.getChildren().get(7);

                String typeText = typeComboBox.getEditor().getText().trim();
                System.out.println("DEBUG - Type: " + typeText + ", Date: " + datePicker.getValue() + ", Desc: " + resultArea.getText());
                
                if (typeText.isEmpty()) {
                    System.out.println("DEBUG - Ligne ignorée car type vide");
                    continue;
                }

                Analyse analyse = new Analyse();
                
                TypeAnalyse typeAnalyse = null;
                if (typeText.contains(":")) {
                    String[] parts = typeText.split(":", 2);
                    String nomAnalyse = parts[0].trim();
                    typeAnalyse = typeAnalyseService.findByNomAnalyseFr(nomAnalyse);
                } else {
                    typeAnalyse = typeAnalyseService.findByNomAnalyseFr(typeText);
                }
                
                // Si le type n'existe pas en base, créer un nouveau type
                if (typeAnalyse == null) {
                    typeAnalyse = new com.azmicro.moms.model.TypeAnalyse();
                    typeAnalyse.setNomAnalyseFr(typeText);
                    typeAnalyse.setCodeAnalyseFr("");
                    // Sauvegarder le nouveau type
                    if (typeAnalyseService.save(typeAnalyse)) {
                        // Récupérer le type sauvegardé avec son ID
                        typeAnalyse = typeAnalyseService.findByNomAnalyseFr(typeText);
                    }
                }
                
                analyse.setTypeAnalyse(typeAnalyse);
                analyse.setDescription(resultArea.getText());
                analyse.setResultat(resultArea.getText());
                analyse.setDateAnalyse(datePicker.getValue() != null ? datePicker.getValue() : LocalDate.now());
                analyse.setConsultationID(this.consultation.getConsultationID());

                Analyse existingAnalyse = (Analyse) lineContainer.getUserData();
                boolean saved;
                if (existingAnalyse != null && existingAnalyse.getAnalyseID() > 0) {
                    analyse.setAnalyseID(existingAnalyse.getAnalyseID());
                    saved = analyseService.updateAnalyse(analyse);
                } else {
                    saved = analyseService.saveAnalyse(analyse);
                }
                System.out.println("DEBUG - Résultat sauvegarde: " + saved);
                if (saved) {
                    savedCount++;
                }
            }
        }

        final int count = savedCount;
        Stage stage = (Stage) rootPane.getScene().getWindow();
        final Window owner = stage.getOwner();
        stage.close();

        Platform.runLater(() -> {
            if (dossierController != null) {
                dossierController.refreshBilan();
                dossierController.initializeConsultationsDates(consultation.getPatient().getPatientID());
            }
            
            // Afficher message de succès
            if (count > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText(count + " analyse(s) ajoutée(s) avec succès!");
                alert.initOwner(owner);
                alert.showAndWait();
            }
        });
    }

    public void loadExistingAnalyses() {
        if (consultation == null) {
            return;
        }
        
        List<Analyse> analyses = analyseService.getAnalysesByConsultationId(consultation.getConsultationID());
        
        for (Analyse analyse : analyses) {
            loadSingleAnalyse(analyse);
        }
    }
    
    public void loadSelectedAnalyses(List<Analyse> analyses) {
        if (analyses == null || analyses.isEmpty()) {
            return;
        }
        for (Analyse analyse : analyses) {
            loadSingleAnalyse(analyse);
        }
    }
    
    public void loadSingleAnalyse(Analyse analyse) {
        if (analyse == null) {
            return;
        }
        
        VBox line = createAnalyseLine();
        
        DatePicker datePicker = (DatePicker) line.getChildren().get(3);
        ComboBox<String> typeCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea resultArea = (TextArea) line.getChildren().get(7);
        
        if (analyse.getDateAnalyse() != null) {
            datePicker.setValue(analyse.getDateAnalyse());
        }
        
        if (analyse.getTypeAnalyse() != null) {
            String typeName = analyse.getTypeAnalyse().getNomAnalyseFr();
            String typeCode = analyse.getTypeAnalyse().getCodeAnalyseFr();
            if (typeCode != null && !typeCode.isEmpty()) {
                typeCombo.getEditor().setText(typeName + ":" + typeCode);
            } else {
                typeCombo.getEditor().setText(typeName);
            }
            typeCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        }
        
        if (analyse.getDescription() != null && !analyse.getDescription().isEmpty()) {
            resultArea.setText(analyse.getDescription());
        } else if (analyse.getResultat() != null && !analyse.getResultat().isEmpty()) {
            resultArea.setText(analyse.getResultat());
        }

        // Conserver l'ID pour activer le mode mise à jour lors de la sauvegarde
        line.setUserData(analyse);
    }

    @FXML
    private void openBatchMode(ActionEvent event) {
        try {
            // Step 1: Select analyses
            FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/com/azmicro/moms/view/bilan/batch-analyse-dialog.fxml")
            );
            
            Parent root1 = loader1.load();
            BatchAnalyseController controller1 = loader1.getController();
            
            Stage stage1 = new Stage();
            stage1.setTitle("Sélection Multiple");
            stage1.setScene(new javafx.scene.Scene(root1));
            stage1.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage1.showAndWait();
            
            List<String> selectedAnalyses = controller1.getSelectedAnalyses();
            if (selectedAnalyses == null || selectedAnalyses.isEmpty()) {
                return;
            }
            
            // Step 2: Fill descriptions
            FXMLLoader loader2 = new FXMLLoader(
                getClass().getResource("/com/azmicro/moms/view/bilan/description-form-dialog.fxml")
            );
            
            Parent root2 = loader2.load();
            DescriptionFormController controller2 = loader2.getController();
            controller2.setAnalyses(selectedAnalyses);
            
            Stage stage2 = new Stage();
            stage2.setTitle("Description Rapide");
            stage2.setScene(new javafx.scene.Scene(root2));
            stage2.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage2.showAndWait();
            
            List<com.azmicro.moms.model.AnalyseTemplate> completed = controller2.getCompletedAnalyses();
            if (completed != null && !completed.isEmpty()) {
                for (com.azmicro.moms.model.AnalyseTemplate template : completed) {
                    createAnalyseLineFromTemplate(template);
                }
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText(completed.size() + " analyse(s) ajoutée(s) avec succès!");
                alert.initOwner(rootPane.getScene().getWindow());
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ouverture du mode batch: " + e.getMessage());
            alert.initOwner(rootPane.getScene().getWindow());
            alert.showAndWait();
        }
    }

    private void createAnalyseLineFromTemplate(com.azmicro.moms.model.AnalyseTemplate template) {
        VBox line = createAnalyseLine();
        
        DatePicker datePicker = (DatePicker) line.getChildren().get(3);
        ComboBox<String> typeCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea descArea = (TextArea) line.getChildren().get(7);
        
        typeCombo.getEditor().setText(template.getTypeAnalyse());
        typeCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        
        if (template.getDescription() != null && !template.getDescription().isEmpty()) {
            descArea.setText(template.getDescription());
        }
        
        updateAnalyseCount();
    }

}

