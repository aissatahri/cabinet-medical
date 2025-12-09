package com.azmicro.moms.controller.prescription;

import com.azmicro.moms.model.Medicaments;
import com.azmicro.moms.service.MedicamentsService;
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
import java.util.List;
import java.util.stream.Collectors;

public class BatchSelectionController {

    @FXML private TextField txtSearch;
    @FXML private TableView<MedicamentRow> tableMedicaments;
    @FXML private TableColumn<MedicamentRow, Boolean> colSelect;
    @FXML private TableColumn<MedicamentRow, String> colNom;
    @FXML private TableColumn<MedicamentRow, String> colDosage;
    @FXML private Label lblSelectionCount;
    @FXML private Button btnNext;
    @FXML private Button btnCancel;

    private final MedicamentsService medicamentsService = new MedicamentsService();
    private ObservableList<MedicamentRow> allMedicaments = FXCollections.observableArrayList();
    private List<String> selectedMedicaments = new ArrayList<>();

    @FXML
    public void initialize() {
        // Configure table
        tableMedicaments.setEditable(true);
        
        colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
        
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));

        // Load medicaments
        loadMedicaments();

        // Search filter
        txtSearch.textProperty().addListener((obs, old, newVal) -> filterMedicaments(newVal));

        // Selection counter
        tableMedicaments.getItems().forEach(row -> 
            row.selectedProperty().addListener((obs, old, newVal) -> updateSelectionCount())
        );

        updateSelectionCount();
    }

    private void loadMedicaments() {
        try {
            List<Medicaments> meds = medicamentsService.findAllMedicaments();
            for (Medicaments med : meds) {
                MedicamentRow row = new MedicamentRow(
                    med.getNomMedicament(),
                    med.getFormeDosage() != null ? med.getFormeDosage() : ""
                );
                row.selectedProperty().addListener((obs, old, newVal) -> updateSelectionCount());
                allMedicaments.add(row);
            }
            tableMedicaments.setItems(allMedicaments);
        } catch (Exception e) {
            showError("Erreur lors du chargement des médicaments: " + e.getMessage());
        }
    }

    private void filterMedicaments(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableMedicaments.setItems(allMedicaments);
        } else {
            ObservableList<MedicamentRow> filtered = allMedicaments.stream()
                .filter(med -> med.getNom().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            tableMedicaments.setItems(filtered);
        }
    }

    private void updateSelectionCount() {
        long count = allMedicaments.stream().filter(MedicamentRow::isSelected).count();
        lblSelectionCount.setText(count + " médicament(s) sélectionné(s)");
        btnNext.setDisable(count == 0);
    }

    @FXML
    private void handleNext() {
        selectedMedicaments = allMedicaments.stream()
            .filter(MedicamentRow::isSelected)
            .map(MedicamentRow::getNom)
            .collect(Collectors.toList());

        if (!selectedMedicaments.isEmpty()) {
            ((Stage) btnNext.getScene().getWindow()).close();
        }
    }

    @FXML
    private void handleCancel() {
        selectedMedicaments.clear();
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    public List<String> getSelectedMedicaments() {
        return selectedMedicaments;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for table row
    public static class MedicamentRow {
        private final String nom;
        private final String dosage;
        private final BooleanProperty selected;

        public MedicamentRow(String nom, String dosage) {
            this.nom = nom;
            this.dosage = dosage;
            this.selected = new SimpleBooleanProperty(false);
        }

        public String getNom() { return nom; }
        public String getDosage() { return dosage; }
        public boolean isSelected() { return selected.get(); }
        public BooleanProperty selectedProperty() { return selected; }
        public void setSelected(boolean value) { selected.set(value); }
    }
}
