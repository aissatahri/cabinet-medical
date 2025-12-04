package com.azmicro.moms.controller.bilan;

import com.azmicro.moms.model.AnalyseTemplate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DescriptionFormController {

    @FXML private Label lblProgress;
    @FXML private Label lblCurrentAnalyse;
    @FXML private TextArea txtDescription;
    @FXML private Button btnPrevious;
    @FXML private Button btnNext;
    @FXML private Button btnFinish;
    @FXML private Button btnCancel;

    private List<String> analyseTypes;
    private List<AnalyseTemplate> completedAnalyses;
    private int currentIndex = 0;

    public void setAnalyses(List<String> types) {
        this.analyseTypes = types;
        this.completedAnalyses = new ArrayList<>();
        this.currentIndex = 0;
        showCurrentAnalyse();
    }

    private void showCurrentAnalyse() {
        if (analyseTypes == null || analyseTypes.isEmpty()) return;

        lblCurrentAnalyse.setText(analyseTypes.get(currentIndex));
        lblProgress.setText("Analyse " + (currentIndex + 1) + " sur " + analyseTypes.size());

        txtDescription.clear();

        btnPrevious.setVisible(currentIndex > 0);
        btnNext.setVisible(currentIndex < analyseTypes.size() - 1);
        btnFinish.setVisible(currentIndex == analyseTypes.size() - 1);
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
            
            if (currentIndex < completedAnalyses.size()) {
                AnalyseTemplate saved = completedAnalyses.get(currentIndex);
                txtDescription.setText(saved.getDescription());
            }
            
            showCurrentAnalyse();
        }
    }

    @FXML
    private void handleNext() {
        saveCurrent();
        currentIndex++;
        showCurrentAnalyse();
    }

    @FXML
    private void handleFinish() {
        saveCurrent();
        ((Stage) btnFinish.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        completedAnalyses.clear();
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private void saveCurrent() {
        AnalyseTemplate template = new AnalyseTemplate(
            analyseTypes.get(currentIndex),
            txtDescription.getText()
        );

        if (currentIndex < completedAnalyses.size()) {
            completedAnalyses.set(currentIndex, template);
        } else {
            completedAnalyses.add(template);
        }
    }

    public List<AnalyseTemplate> getCompletedAnalyses() {
        return completedAnalyses;
    }
}
