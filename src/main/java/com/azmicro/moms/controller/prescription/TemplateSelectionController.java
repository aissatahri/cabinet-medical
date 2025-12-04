package com.azmicro.moms.controller.prescription;

import com.azmicro.moms.model.MedicamentTemplate;
import com.azmicro.moms.model.PrescriptionTemplate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class TemplateSelectionController {

    @FXML private ListView<PrescriptionTemplate> listViewTemplates;
    @FXML private VBox detailsPanel;
    @FXML private Label lblTemplateName;
    @FXML private Label lblTemplateDescription;
    @FXML private VBox vboxMedicaments;
    @FXML private Button btnUseTemplate;
    @FXML private Button btnCancel;

    private PrescriptionTemplate selectedTemplate;

    @FXML
    public void initialize() {
        // Load templates
        List<PrescriptionTemplate> templates = PrescriptionTemplate.getDefaultTemplates();
        listViewTemplates.getItems().addAll(templates);

        // Configure cell factory with icon
        listViewTemplates.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PrescriptionTemplate template, boolean empty) {
                super.updateItem(template, empty);
                if (empty || template == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(template.getNom() + " (" + template.getMedicaments().size() + " médicaments)");
                    FontIcon icon = new FontIcon(template.getIcone());
                    icon.setIconSize(16);
                    setGraphic(icon);
                    setStyle("-fx-padding: 8; -fx-font-size: 13px;");
                }
            }
        });

        // Selection listener
        listViewTemplates.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                showTemplateDetails(newVal);
                selectedTemplate = newVal;
                btnUseTemplate.setDisable(false);
            }
        });
    }

    private void showTemplateDetails(PrescriptionTemplate template) {
        lblTemplateName.setText(template.getNom());
        lblTemplateDescription.setText(template.getDescription());

        vboxMedicaments.getChildren().clear();
        for (MedicamentTemplate med : template.getMedicaments()) {
            Label label = new Label("• " + med.getFullDescription());
            label.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");
            label.setWrapText(true);
            vboxMedicaments.getChildren().add(label);
        }

        detailsPanel.setVisible(true);
    }

    @FXML
    private void handleUseTemplate() {
        if (selectedTemplate != null) {
            ((Stage) btnUseTemplate.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        selectedTemplate = null;
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public PrescriptionTemplate getSelectedTemplate() {
        return selectedTemplate;
    }
}
