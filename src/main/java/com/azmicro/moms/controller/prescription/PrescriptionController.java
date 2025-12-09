/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.prescription;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.Medicaments;
import com.azmicro.moms.model.Prescriptions;
import com.azmicro.moms.service.MedicamentsService;
import com.azmicro.moms.service.PrescriptionService;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class PrescriptionController implements Initializable {

    @FXML
    private VBox vboxLignesPrescription;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button btnAjouterLignePrescription;
    @FXML
    private Button btnSavePresciption;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblNombreMedicaments;

    private ObservableList<String> allMedicaments;
    private MedicamentsService medicamentsService;
    private PrescriptionService prescriptionService;
    private Consultations consultations;
    private DossierController dossierController;

    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController;
    }

    public void setConsultation(Consultations consultations) {
        this.consultations = consultations;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.medicamentsService = new MedicamentsService();
        this.prescriptionService = new PrescriptionService();
        
        List<Medicaments> medicamentsList = medicamentsService.findAllMedicaments();
        allMedicaments = FXCollections.observableArrayList(
            medicamentsList.stream()
                .map(m -> m.getNomMedicament() + " - " + m.getFormeDosage())
                .collect(Collectors.toList())
        );
        
        updateMedicamentCount();
    }

    @FXML
    private void ajouterLigneImagerie(ActionEvent event) {
        createPrescriptionLine();
    }

    private VBox createPrescriptionLine() {
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

        int lineNumber = vboxLignesPrescription.getChildren().size() + 1;
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label numberLabel = new Label("Médicament #" + lineNumber);
        numberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #16a085;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button deleteBtn = new Button();
        FontIcon trashIcon = new FontIcon("fas-trash");
        trashIcon.setIconSize(14);
        deleteBtn.setGraphic(trashIcon);
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e -> {
            vboxLignesPrescription.getChildren().remove(lineContainer);
            renumberPrescriptions();
            updateMedicamentCount();
        });
        
        header.getChildren().addAll(numberLabel, spacer, deleteBtn);

        Label dateLabel = new Label("Date de prescription:");
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(200);

        Label medLabel = new Label("Médicament:");
        medLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        
        ComboBox<String> medComboBox = new ComboBox<>(FXCollections.observableArrayList(allMedicaments));
        medComboBox.setEditable(true);
        medComboBox.setPrefWidth(400);
        medComboBox.setPromptText("Rechercher ou saisir un médicament...");
        
        medComboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!medComboBox.isShowing()) {
                medComboBox.show();
            }
            String filter = newVal.toLowerCase();
            ObservableList<String> filtered = allMedicaments.stream()
                .filter(item -> item.toLowerCase().contains(filter))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            medComboBox.setItems(filtered);
            
            boolean isValid = !newVal.trim().isEmpty();
            medComboBox.setStyle(isValid 
                ? "-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;" 
                : "-fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-border-radius: 4px;");
        });

        Label instructionLabel = new Label("Posologie / Instructions:");
        instructionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        
        TextArea instructionArea = new TextArea();
        instructionArea.setPrefRowCount(4);
        instructionArea.setWrapText(true);
        instructionArea.setPromptText("Dosage, durée, fréquence et instructions d'utilisation...");
        
        Label charCountLabel = new Label("0/500");
        charCountLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");
        charCountLabel.setAlignment(Pos.CENTER_RIGHT);
        
        instructionArea.textProperty().addListener((obs, oldVal, newVal) -> {
            int length = newVal.length();
            if (length > 500) {
                instructionArea.setText(oldVal);
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
        
        Button matin = createSnippetButton("1 matin", "#3498db", instructionArea);
        Button soir = createSnippetButton("1 soir", "#9b59b6", instructionArea);
        Button repas = createSnippetButton("Après repas", "#e67e22", instructionArea);
        
        snippetBox.getChildren().addAll(snippetLabel, matin, soir, repas);

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
            medLabel, medComboBox,
            instructionLabel, instructionArea,
            charCountLabel,
            snippetBox,
            actionBox
        );

        lineContainer.setOpacity(0);
        vboxLignesPrescription.getChildren().add(lineContainer);
        updateMedicamentCount();

        FadeTransition fade = new FadeTransition(Duration.millis(300), lineContainer);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        return lineContainer;
    }

    private Button createSnippetButton(String text, String color, TextArea targetArea) {
        Button btn = new Button(text);
        
        String baseStyle = 
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 10px;" +
            "-fx-padding: 4 8 4 8;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;";
        
        btn.setStyle(baseStyle);
        
        // Effet hover - couleur plus claire
        btn.setOnMouseEntered(e -> {
            btn.setStyle(baseStyle + "-fx-opacity: 0.8; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(baseStyle);
        });
        
        // Effet pressed - couleur plus foncée
        btn.setOnMousePressed(e -> {
            btn.setStyle(baseStyle + "-fx-opacity: 0.6; -fx-scale-x: 0.95; -fx-scale-y: 0.95;");
        });
        
        btn.setOnMouseReleased(e -> {
            btn.setStyle(baseStyle + "-fx-opacity: 0.8; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        btn.setOnAction(e -> {
            String current = targetArea.getText();
            String snippet = text + " ";
            if (!current.isEmpty() && !current.endsWith("\n") && !current.endsWith(" ")) {
                snippet = " " + snippet;
            }
            
            // Sauvegarder le style original
            String originalStyle = targetArea.getStyle();
            
            // Ajouter le texte
            targetArea.appendText(snippet);
            targetArea.requestFocus();
            targetArea.positionCaret(targetArea.getLength());
            
            // Animation flash pour montrer que le texte a été ajouté
            targetArea.setStyle(originalStyle + "-fx-border-color: " + color + "; -fx-border-width: 2px;");
            
            // Retour au style original après 500ms
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(500));
            pause.setOnFinished(ev -> targetArea.setStyle(originalStyle));
            pause.play();
        });
        return btn;
    }

    @FXML
    private void addTemplateAntibiotiques() {
        VBox line = createPrescriptionLine();
        ComboBox<String> medCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea instructionArea = (TextArea) line.getChildren().get(7);
        
        medCombo.getEditor().setText("Amoxicilline 500mg");
        medCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        instructionArea.setText("1 comprimé 3 fois par jour pendant 7 jours\nÀ prendre après les repas");
    }

    @FXML
    private void addTemplateAntiInflammatoires() {
        VBox line = createPrescriptionLine();
        ComboBox<String> medCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea instructionArea = (TextArea) line.getChildren().get(7);
        
        medCombo.getEditor().setText("Ibuprofène 400mg");
        medCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        instructionArea.setText("1 comprimé 2 à 3 fois par jour si douleur\nÀ prendre avec de l'eau au cours des repas");
    }

    @FXML
    private void addTemplateAntalgiques() {
        VBox line = createPrescriptionLine();
        ComboBox<String> medCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea instructionArea = (TextArea) line.getChildren().get(7);
        
        medCombo.getEditor().setText("Paracétamol 1g");
        medCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        instructionArea.setText("1 comprimé 3 fois par jour si besoin\nNe pas dépasser 3g par jour");
    }

    @FXML
    private void addTemplateVitamines() {
        VBox line = createPrescriptionLine();
        ComboBox<String> medCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea instructionArea = (TextArea) line.getChildren().get(7);
        
        medCombo.getEditor().setText("Vitamine D3 1000 UI");
        medCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        instructionArea.setText("1 comprimé par jour le matin\nCure de 3 mois");
    }

    private void duplicateLine(VBox sourceLine) {
        DatePicker sourceDatePicker = (DatePicker) sourceLine.getChildren().get(3);
        ComboBox<String> sourceMedCombo = (ComboBox<String>) sourceLine.getChildren().get(5);
        TextArea sourceInstructionArea = (TextArea) sourceLine.getChildren().get(7);

        VBox newLine = createPrescriptionLine();
        DatePicker newDatePicker = (DatePicker) newLine.getChildren().get(3);
        ComboBox<String> newMedCombo = (ComboBox<String>) newLine.getChildren().get(5);
        TextArea newInstructionArea = (TextArea) newLine.getChildren().get(7);

        newDatePicker.setValue(sourceDatePicker.getValue());
        newMedCombo.getEditor().setText(sourceMedCombo.getEditor().getText());
        newInstructionArea.setText(sourceInstructionArea.getText());
    }

    @FXML
    private void clearAll() {
        if (vboxLignesPrescription.getChildren().isEmpty()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Effacer toutes les prescriptions?");
        alert.setContentText("Cette action supprimera toutes les lignes de prescription. Continuer?");
        alert.initOwner(rootPane.getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            vboxLignesPrescription.getChildren().clear();
            updateMedicamentCount();
        }
    }

    private void updateMedicamentCount() {
        int count = vboxLignesPrescription.getChildren().size();
        if (lblNombreMedicaments != null) {
            lblNombreMedicaments.setText(count + " médicament" + (count > 1 ? "s" : ""));
        }
    }

    private void renumberPrescriptions() {
        for (int i = 0; i < vboxLignesPrescription.getChildren().size(); i++) {
            VBox lineContainer = (VBox) vboxLignesPrescription.getChildren().get(i);
            HBox header = (HBox) lineContainer.getChildren().get(0);
            Label numberLabel = (Label) header.getChildren().get(0);
            numberLabel.setText("Médicament #" + (i + 1));
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void savePresciption(ActionEvent event) {
        boolean success = true;
        
        for (Node node : vboxLignesPrescription.getChildren()) {
            if (node instanceof VBox) {
                VBox lineContainer = (VBox) node;
                
                DatePicker datePicker = (DatePicker) lineContainer.getChildren().get(3);
                ComboBox<String> medComboBox = (ComboBox<String>) lineContainer.getChildren().get(5);
                TextArea instructionArea = (TextArea) lineContainer.getChildren().get(7);

                String medText = medComboBox.getEditor().getText().trim();
                if (medText.isEmpty()) {
                    continue;
                }

                Prescriptions prescription = new Prescriptions();
                
                // Check if this is an existing prescription (has an ID stored in user data)
                Object userData = lineContainer.getUserData();
                if (userData instanceof Integer) {
                    prescription.setPrescriptionID((Integer) userData);
                }
                
                prescription.setConsultationID(consultations.getConsultationID());
                
                Medicaments medicament = findMedicamentByName(medText);
                if (medicament != null) {
                    prescription.setMedicament(medicament);
                } else {
                    continue;
                }
                
                String instructions = instructionArea.getText();
                
                String[] lines = instructions.split("\n");
                String dose = "";
                String duree = "";
                String description = "";
                
                // Parse each line and extract values by removing prefixes
                boolean hasStructuredData = false;
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("Posologie:")) {
                        dose = line.substring("Posologie:".length()).trim();
                        hasStructuredData = true;
                    } else if (line.startsWith("Durée:")) {
                        duree = line.substring("Durée:".length()).trim();
                        hasStructuredData = true;
                    } else if (line.startsWith("Instructions:")) {
                        description = line.substring("Instructions:".length()).trim();
                        hasStructuredData = true;
                    }
                }
                
                // Si aucun préfixe n'a été trouvé (saisie libre), tout va dans description
                if (!hasStructuredData && !instructions.trim().isEmpty()) {
                    description = instructions.trim();
                }
                
                // Set cleaned values
                prescription.setDose(dose.length() > 100 ? dose.substring(0, 100) : dose);
                prescription.setDuree(duree.length() > 50 ? duree.substring(0, 50) : duree);
                prescription.setDescription(description);

                try {
                    boolean result;
                    // Check if this is an update (has prescriptionID) or a new prescription
                    if (prescription.getPrescriptionID() > 0) {
                        result = prescriptionService.updatePrescription(prescription);
                    } else {
                        result = prescriptionService.savePrescription(prescription);
                    }
                    
                    if (!result) {
                        success = false;
                    }
                } catch (Exception e) {
                    success = false;
                    e.printStackTrace();
                }
            }
        }

        if (success) {
            Platform.runLater(() -> {
                if (dossierController != null) {
                    dossierController.loadPrescriptionsForConsultation(consultations);
                    dossierController.loadConsultations();
                    dossierController.getConsultationSelected();
                }
            });
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Enregistrement réussi");
            alert.setHeaderText(null);
            alert.setContentText("Les prescriptions ont été enregistrées avec succès.");
            alert.initOwner(rootPane.getScene().getWindow());
            alert.showAndWait();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'enregistrement");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'enregistrement des prescriptions. Veuillez réessayer.");
            alert.initOwner(rootPane.getScene().getWindow());
            alert.showAndWait();
        }
    }

    private Medicaments findMedicamentByName(String medText) {
        List<Medicaments> allMeds = medicamentsService.findAllMedicaments();
        
        for (Medicaments med : allMeds) {
            String fullName = med.getNomMedicament() + " - " + med.getFormeDosage();
            if (fullName.equalsIgnoreCase(medText) || med.getNomMedicament().equalsIgnoreCase(medText)) {
                return med;
            }
        }
        
        String searchName = medText.split(" - ")[0].trim();
        for (Medicaments med : allMeds) {
            if (med.getNomMedicament().toLowerCase().contains(searchName.toLowerCase())) {
                return med;
            }
        }
        
        return null;
    }

    public void loadExistingPrescriptions() {
        if (consultations == null) {
            return;
        }
        
        List<Prescriptions> lst = prescriptionService.getPrescriptionByConsultation(consultations.getConsultationID());
        
        for (Prescriptions prescription : lst) {
            VBox line = createPrescriptionLine();
            
            // Store the prescription ID in the line container's user data for later update
            line.setUserData(prescription.getPrescriptionID());
            
            DatePicker datePicker = (DatePicker) line.getChildren().get(3);
            ComboBox<String> medCombo = (ComboBox<String>) line.getChildren().get(5);
            TextArea instructionArea = (TextArea) line.getChildren().get(7);
            
            if (prescription.getMedicament() != null) {
                String medName = prescription.getMedicament().getNomMedicament() + " - " + 
                                prescription.getMedicament().getFormeDosage();
                medCombo.getEditor().setText(medName);
                medCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
            }
            
            StringBuilder instructions = new StringBuilder();
            if (prescription.getDose() != null && !prescription.getDose().isEmpty()) {
                instructions.append(prescription.getDose());
            }
            if (prescription.getDuree() != null && !prescription.getDuree().isEmpty()) {
                if (instructions.length() > 0) {
                    instructions.append("\n");
                }
                instructions.append(prescription.getDuree());
            }
            if (prescription.getDescription() != null && !prescription.getDescription().isEmpty()) {
                if (instructions.length() == 0) {
                    instructions.append(prescription.getDescription());
                }
            }
            
            instructionArea.setText(instructions.toString());
        }
    }

    public void loadSelectedPrescriptions(List<Prescriptions> prescriptions) {
        if (prescriptions == null || prescriptions.isEmpty()) {
            return;
        }
        
        for (Prescriptions prescription : prescriptions) {
            loadSinglePrescription(prescription);
        }
    }

    public void loadSinglePrescription(Prescriptions prescription) {
        if (prescription == null) {
            return;
        }
        
        VBox line = createPrescriptionLine();
        
        // Store the prescription ID in the line container's user data for later update
        line.setUserData(prescription.getPrescriptionID());
        
        DatePicker datePicker = (DatePicker) line.getChildren().get(3);
        ComboBox<String> medCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea instructionArea = (TextArea) line.getChildren().get(7);
        
        if (prescription.getMedicament() != null) {
            String medName = prescription.getMedicament().getNomMedicament() + " - " + 
                            prescription.getMedicament().getFormeDosage();
            medCombo.getEditor().setText(medName);
            medCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        }
        
        StringBuilder instructions = new StringBuilder();
        if (prescription.getDose() != null && !prescription.getDose().isEmpty()) {
            instructions.append(prescription.getDose());
        }
        if (prescription.getDuree() != null && !prescription.getDuree().isEmpty()) {
            if (instructions.length() > 0) {
                instructions.append(" / ");
            }
            instructions.append(prescription.getDuree());
        }
        if (prescription.getDescription() != null && !prescription.getDescription().isEmpty()) {
            if (instructions.length() == 0) {
                instructions.append(prescription.getDescription());
            }
        }
        
        instructionArea.setText(instructions.toString());
    }

    @FXML
    private void openTemplateMode(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/com/azmicro/moms/view/prescription/template-selection-dialog.fxml")
            );
            
            javafx.scene.Parent root = loader.load();
            TemplateSelectionController controller = loader.getController();
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Choisir un Template");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            com.azmicro.moms.model.PrescriptionTemplate template = controller.getSelectedTemplate();
            if (template != null) {
                for (com.azmicro.moms.model.MedicamentTemplate medTemplate : template.getMedicaments()) {
                    createPrescriptionLineFromTemplate(medTemplate);
                }
                showInfo("Template appliqué avec succès! " + template.getMedicaments().size() + " médicaments ajoutés.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ouverture du mode template: " + e.getMessage());
        }
    }

    @FXML
    private void openBatchMode(ActionEvent event) {
        try {
            // Step 1: Select medications
            javafx.fxml.FXMLLoader loader1 = new javafx.fxml.FXMLLoader(
                getClass().getResource("/com/azmicro/moms/view/prescription/batch-selection-dialog.fxml")
            );
            
            javafx.scene.Parent root1 = loader1.load();
            BatchSelectionController controller1 = loader1.getController();
            
            javafx.stage.Stage stage1 = new javafx.stage.Stage();
            stage1.setTitle("Sélection Multiple");
            stage1.setScene(new javafx.scene.Scene(root1));
            stage1.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage1.showAndWait();
            
            List<String> selectedMeds = controller1.getSelectedMedicaments();
            if (selectedMeds == null || selectedMeds.isEmpty()) {
                return;
            }
            
            // Step 2: Fill posology for each
            javafx.fxml.FXMLLoader loader2 = new javafx.fxml.FXMLLoader(
                getClass().getResource("/com/azmicro/moms/view/prescription/posology-form-dialog.fxml")
            );
            
            javafx.scene.Parent root2 = loader2.load();
            PosologyFormController controller2 = loader2.getController();
            controller2.setMedicaments(selectedMeds);
            
            javafx.stage.Stage stage2 = new javafx.stage.Stage();
            stage2.setTitle("Posologie Rapide");
            stage2.setScene(new javafx.scene.Scene(root2));
            stage2.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage2.showAndWait();
            
            List<com.azmicro.moms.model.MedicamentTemplate> completed = controller2.getCompletedMedicaments();
            if (completed != null && !completed.isEmpty()) {
                for (com.azmicro.moms.model.MedicamentTemplate medTemplate : completed) {
                    createPrescriptionLineFromTemplate(medTemplate);
                }
                showInfo("Ajout réussi! " + completed.size() + " médicaments ajoutés avec posologie.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ouverture du mode batch: " + e.getMessage());
        }
    }

    private void createPrescriptionLineFromTemplate(com.azmicro.moms.model.MedicamentTemplate template) {
        VBox line = createPrescriptionLine();
        
        // Get controls
        DatePicker datePicker = (DatePicker) line.getChildren().get(3);
        ComboBox<String> medCombo = (ComboBox<String>) line.getChildren().get(5);
        TextArea instructionArea = (TextArea) line.getChildren().get(7);
        
        // Set medication name
        medCombo.getEditor().setText(template.getNomMedicament());
        medCombo.setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 4px;");
        
        // Build instruction text
        StringBuilder instructions = new StringBuilder();
        if (template.getPosologie() != null && !template.getPosologie().isEmpty()) {
            instructions.append("Posologie: ").append(template.getPosologie()).append("\n");
        }
        if (template.getDuree() != null && !template.getDuree().isEmpty()) {
            instructions.append("Durée: ").append(template.getDuree()).append("\n");
        }
        if (template.getInstructions() != null && !template.getInstructions().isEmpty()) {
            instructions.append("Instructions: ").append(template.getInstructions());
        }
        
        instructionArea.setText(instructions.toString());
        
        // Animate
        FadeTransition fade = new FadeTransition(Duration.millis(400), line);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
        
        updateMedicamentCount();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.showAndWait();
    }
}
