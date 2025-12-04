package com.azmicro.moms.controller.imagerie;

import com.azmicro.moms.model.ImagerieTemplate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DescriptionImagerieController {

    @FXML private Label lblProgress;
    @FXML private Label lblCurrentImagerie;
    @FXML private TextArea txtDescription;
    @FXML private Button btnPrevious;
    @FXML private Button btnNext;
    @FXML private Button btnFinish;
    @FXML private Button btnCancel;

    private List<String> imagerieTypes;
    private List<ImagerieTemplate> completedImageries;
    private int currentIndex = 0;

    public void setImageries(List<String> types) {
        this.imagerieTypes = types;
        this.completedImageries = new ArrayList<>();
        this.currentIndex = 0;
        showCurrentImagerie();
    }

    private void showCurrentImagerie() {
        if (imagerieTypes == null || imagerieTypes.isEmpty()) return;

        lblCurrentImagerie.setText(imagerieTypes.get(currentIndex));
        lblProgress.setText("Imagerie " + (currentIndex + 1) + " sur " + imagerieTypes.size());

        txtDescription.clear();

        btnPrevious.setVisible(currentIndex > 0);
        btnNext.setVisible(currentIndex < imagerieTypes.size() - 1);
        btnFinish.setVisible(currentIndex == imagerieTypes.size() - 1);
    }

    @FXML
    private void setDescription(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        String current = txtDescription.getText();
        if (current.isEmpty()) {
            txtDescription.setText(btn.getText());
        } else {
            txtDescription.setText(current + ", " + btn.getText());
        }
    }

    @FXML
    private void handlePrevious() {
        if (currentIndex > 0) {
            saveCurrent();
            currentIndex--;
            
            if (currentIndex < completedImageries.size()) {
                ImagerieTemplate saved = completedImageries.get(currentIndex);
                txtDescription.setText(saved.getDescription());
            }
            
            showCurrentImagerie();
        }
    }

    @FXML
    private void handleNext() {
        saveCurrent();
        currentIndex++;
        showCurrentImagerie();
    }

    @FXML
    private void handleFinish() {
        saveCurrent();
        ((Stage) btnFinish.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        completedImageries.clear();
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private void saveCurrent() {
        ImagerieTemplate template = new ImagerieTemplate(
            imagerieTypes.get(currentIndex),
            txtDescription.getText()
        );

        if (currentIndex < completedImageries.size()) {
            completedImageries.set(currentIndex, template);
        } else {
            completedImageries.add(template);
        }
    }

    public List<ImagerieTemplate> getCompletedImageries() {
        return completedImageries;
    }
}
