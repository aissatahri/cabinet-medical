package com.azmicro.moms.controller.prescription;

import com.azmicro.moms.model.MedicamentTemplate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PosologyFormController {

    @FXML private Label lblProgress;
    @FXML private Label lblCurrentMedicament;
    @FXML private TextField txtPosologie;
    @FXML private TextField txtFrequence;
    @FXML private TextField txtDuree;
    @FXML private TextArea txtInstructions;
    @FXML private Button btnPrevious;
    @FXML private Button btnNext;
    @FXML private Button btnFinish;
    @FXML private Button btnCancel;

    private List<String> medicamentNames;
    private List<MedicamentTemplate> completedMedicaments;
    private int currentIndex = 0;

    public void setMedicaments(List<String> names) {
        this.medicamentNames = names;
        this.completedMedicaments = new ArrayList<>();
        this.currentIndex = 0;
        showCurrentMedicament();
    }

    private void showCurrentMedicament() {
        if (medicamentNames == null || medicamentNames.isEmpty()) return;

        lblCurrentMedicament.setText(medicamentNames.get(currentIndex));
        lblProgress.setText("Médicament " + (currentIndex + 1) + " sur " + medicamentNames.size());

        // Clear fields
        txtPosologie.clear();
        txtFrequence.clear();
        txtDuree.clear();
        txtInstructions.clear();

        // Update buttons
        btnPrevious.setVisible(currentIndex > 0);
        btnNext.setVisible(currentIndex < medicamentNames.size() - 1);
        btnFinish.setVisible(currentIndex == medicamentNames.size() - 1);
    }

    @FXML
    private void setPosologie(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        txtPosologie.setText(btn.getText());
        
        // Effet visuel - flash du bouton
        String originalStyle = btn.getStyle();
        btn.setStyle(originalStyle + "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
        pause.setOnFinished(e -> btn.setStyle(originalStyle));
        pause.play();
    }

    @FXML
    private void setFrequence(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        txtFrequence.setText(btn.getText());
        
        // Effet visuel - flash du bouton
        String originalStyle = btn.getStyle();
        btn.setStyle(originalStyle + "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
        pause.setOnFinished(e -> btn.setStyle(originalStyle));
        pause.play();
    }

    @FXML
    private void setDuree(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        txtDuree.setText(btn.getText());
        
        // Effet visuel - flash du bouton
        String originalStyle = btn.getStyle();
        btn.setStyle(originalStyle + "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
        pause.setOnFinished(e -> btn.setStyle(originalStyle));
        pause.play();
    }

    @FXML
    private void setInstruction(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        String current = txtInstructions.getText();
        if (current.isEmpty()) {
            txtInstructions.setText(btn.getText());
        } else {
            txtInstructions.setText(current + ", " + btn.getText());
        }
        
        // Effet visuel - flash du bouton
        String originalStyle = btn.getStyle();
        btn.setStyle(originalStyle + "-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold;");
        
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
        pause.setOnFinished(e -> btn.setStyle(originalStyle));
        pause.play();
    }

    @FXML
    private void handlePrevious() {
        if (currentIndex > 0) {
            // Save current before going back
            saveCurrent();
            currentIndex--;
            
            // Load saved data
            if (currentIndex < completedMedicaments.size()) {
                MedicamentTemplate saved = completedMedicaments.get(currentIndex);
                txtPosologie.setText(saved.getPosologie());
                txtDuree.setText(saved.getDuree());
                txtInstructions.setText(saved.getInstructions());
            }
            
            showCurrentMedicament();
        }
    }

    @FXML
    private void handleNext() {
        if (!validateFields()) return;

        saveCurrent();
        currentIndex++;
        showCurrentMedicament();
    }

    @FXML
    private void handleFinish() {
        if (!validateFields()) return;

        saveCurrent();
        ((Stage) btnFinish.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        completedMedicaments.clear();
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private boolean validateFields() {
        if (txtPosologie.getText().trim().isEmpty()) {
            showWarning("Veuillez renseigner la posologie");
            return false;
        }
        if (txtFrequence.getText().trim().isEmpty()) {
            showWarning("Veuillez sélectionner la fréquence");
            return false;
        }
        if (txtDuree.getText().trim().isEmpty()) {
            showWarning("Veuillez renseigner la durée");
            return false;
        }
        return true;
    }

    private void saveCurrent() {
        String posologie = txtPosologie.getText() + " - " + txtFrequence.getText();
        
        MedicamentTemplate template = new MedicamentTemplate(
            medicamentNames.get(currentIndex),
            posologie,
            txtDuree.getText(),
            txtInstructions.getText()
        );

        // Update or add
        if (currentIndex < completedMedicaments.size()) {
            completedMedicaments.set(currentIndex, template);
        } else {
            completedMedicaments.add(template);
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<MedicamentTemplate> getCompletedMedicaments() {
        return completedMedicaments;
    }
}
