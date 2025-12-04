package com.azmicro.moms.controller.bilan;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BatchAnalyseController {

    @FXML private TextField txtSearch;
    @FXML private TableView<AnalyseRow> tableAnalyses;
    @FXML private TableColumn<AnalyseRow, Boolean> colSelect;
    @FXML private TableColumn<AnalyseRow, String> colType;
    @FXML private Label lblSelectionCount;
    @FXML private Button btnNext;
    @FXML private Button btnCancel;

    private ObservableList<AnalyseRow> allAnalyses = FXCollections.observableArrayList();
    private List<String> selectedAnalyses = new ArrayList<>();

    // Liste des types d'analyses courants
    private static final List<String> ANALYSE_TYPES = Arrays.asList(
        "Numération Formule Sanguine (NFS)",
        "Glycémie à jeun",
        "Hémoglobine glyquée (HbA1c)",
        "Bilan lipidique (Cholestérol)",
        "Créatinine / Urée",
        "Transaminases (ASAT, ALAT)",
        "TSH (Thyréostimuline)",
        "Vitamine D",
        "Ferritine",
        "CRP (Protéine C Réactive)",
        "VS (Vitesse de Sédimentation)",
        "Ionogramme sanguin",
        "Calcium / Phosphore",
        "Acide urique",
        "Protéinurie 24h",
        "ECBU (Examen Cytobactériologique des Urines)",
        "Sérologie hépatite B",
        "Sérologie hépatite C",
        "Sérologie VIH",
        "Bêta-HCG (Test de grossesse)",
        "PSA (Antigène Prostatique)",
        "Coagulation (TP, TCA)",
        "Fibrinogène",
        "D-Dimères",
        "Troponine",
        "BNP (Peptide Natriurétique)",
        "CPK (Créatine Phosphokinase)",
        "LDH (Lactate Déshydrogénase)",
        "Électrophorèse des protéines",
        "Albumine"
    );

    @FXML
    public void initialize() {
        tableAnalyses.setEditable(true);
        
        colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
        
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        loadAnalyses();

        txtSearch.textProperty().addListener((obs, old, newVal) -> filterAnalyses(newVal));

        tableAnalyses.getItems().forEach(row -> 
            row.selectedProperty().addListener((obs, old, newVal) -> updateSelectionCount())
        );

        updateSelectionCount();
    }

    private void loadAnalyses() {
        for (String type : ANALYSE_TYPES) {
            AnalyseRow row = new AnalyseRow(type);
            row.selectedProperty().addListener((obs, old, newVal) -> updateSelectionCount());
            allAnalyses.add(row);
        }
        tableAnalyses.setItems(allAnalyses);
    }

    private void filterAnalyses(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableAnalyses.setItems(allAnalyses);
        } else {
            ObservableList<AnalyseRow> filtered = allAnalyses.stream()
                .filter(analyse -> analyse.getType().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableAnalyses.setItems(filtered);
        }
    }

    private void updateSelectionCount() {
        long count = allAnalyses.stream().filter(AnalyseRow::isSelected).count();
        lblSelectionCount.setText(count + " analyse(s) sélectionnée(s)");
        btnNext.setDisable(count == 0);
    }

    @FXML
    private void handleNext() {
        selectedAnalyses = allAnalyses.stream()
            .filter(AnalyseRow::isSelected)
            .map(AnalyseRow::getType)
            .collect(Collectors.toList());

        if (!selectedAnalyses.isEmpty()) {
            ((Stage) btnNext.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        selectedAnalyses.clear();
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public List<String> getSelectedAnalyses() {
        return selectedAnalyses;
    }

    public static class AnalyseRow {
        private final String type;
        private final BooleanProperty selected;

        public AnalyseRow(String type) {
            this.type = type;
            this.selected = new SimpleBooleanProperty(false);
        }

        public String getType() { return type; }
        public boolean isSelected() { return selected.get(); }
        public BooleanProperty selectedProperty() { return selected; }
        public void setSelected(boolean value) { selected.set(value); }
    }
}
