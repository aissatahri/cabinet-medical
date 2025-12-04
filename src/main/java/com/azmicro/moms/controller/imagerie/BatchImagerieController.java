package com.azmicro.moms.controller.imagerie;

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

public class BatchImagerieController {

    @FXML private TextField txtSearch;
    @FXML private TableView<ImagerieRow> tableImageries;
    @FXML private TableColumn<ImagerieRow, Boolean> colSelect;
    @FXML private TableColumn<ImagerieRow, String> colType;
    @FXML private Label lblSelectionCount;
    @FXML private Button btnNext;
    @FXML private Button btnCancel;

    private ObservableList<ImagerieRow> allImageries = FXCollections.observableArrayList();
    private List<String> selectedImageries = new ArrayList<>();

    // Liste des types d'imagerie courants
    private static final List<String> IMAGERIE_TYPES = Arrays.asList(
        "Radiographie thoracique",
        "Radiographie abdominale",
        "Radiographie du genou",
        "Radiographie de la cheville",
        "Radiographie du poignet",
        "Radiographie de l'épaule",
        "Radiographie du rachis cervical",
        "Radiographie du rachis lombaire",
        "Radiographie du bassin",
        "Échographie abdominale",
        "Échographie pelvienne",
        "Échographie rénale",
        "Échographie thyroïdienne",
        "Échographie mammaire",
        "Échographie cardiaque (ETT)",
        "Échographie obstétricale",
        "Échographie doppler veineux",
        "Scanner cérébral",
        "Scanner thoracique",
        "Scanner abdomino-pelvien",
        "Scanner du rachis",
        "IRM cérébrale",
        "IRM du rachis",
        "IRM du genou",
        "IRM de l'épaule",
        "Mammographie",
        "Panoramique dentaire",
        "Scintigraphie osseuse",
        "Scintigraphie thyroïdienne",
        "Angiographie"
    );

    @FXML
    public void initialize() {
        tableImageries.setEditable(true);
        
        colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
        
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        loadImageries();

        txtSearch.textProperty().addListener((obs, old, newVal) -> filterImageries(newVal));

        tableImageries.getItems().forEach(row -> 
            row.selectedProperty().addListener((obs, old, newVal) -> updateSelectionCount())
        );

        updateSelectionCount();
    }

    private void loadImageries() {
        for (String type : IMAGERIE_TYPES) {
            ImagerieRow row = new ImagerieRow(type);
            row.selectedProperty().addListener((obs, old, newVal) -> updateSelectionCount());
            allImageries.add(row);
        }
        tableImageries.setItems(allImageries);
    }

    private void filterImageries(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableImageries.setItems(allImageries);
        } else {
            ObservableList<ImagerieRow> filtered = allImageries.stream()
                .filter(img -> img.getType().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableImageries.setItems(filtered);
        }
    }

    private void updateSelectionCount() {
        long count = allImageries.stream().filter(ImagerieRow::isSelected).count();
        lblSelectionCount.setText(count + " imagerie(s) sélectionnée(s)");
        btnNext.setDisable(count == 0);
    }

    @FXML
    private void handleNext() {
        selectedImageries = allImageries.stream()
            .filter(ImagerieRow::isSelected)
            .map(ImagerieRow::getType)
            .collect(Collectors.toList());

        if (!selectedImageries.isEmpty()) {
            ((Stage) btnNext.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        selectedImageries.clear();
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public List<String> getSelectedImageries() {
        return selectedImageries;
    }

    public static class ImagerieRow {
        private final String type;
        private final BooleanProperty selected;

        public ImagerieRow(String type) {
            this.type = type;
            this.selected = new SimpleBooleanProperty(false);
        }

        public String getType() { return type; }
        public boolean isSelected() { return selected.get(); }
        public BooleanProperty selectedProperty() { return selected; }
        public void setSelected(boolean value) { selected.set(value); }
    }
}
