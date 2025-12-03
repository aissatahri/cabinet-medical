package com.azmicro.moms.util;

import com.azmicro.moms.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utilitaire pour configurer les TableView
 */
public class TableViewConfigurator {

    /**
     * Configure les colonnes de la TableView des antécédents
     */
    public static void configureAntecedentsTable(TableView<HistoriqueMedical> tvAntecedent,
                                                  TableColumn<HistoriqueMedical, LocalDate> clmDate,
                                                  TableColumn<HistoriqueMedical, String> clmType,
                                                  TableColumn<HistoriqueMedical, String> clmDescription) {
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmType.setCellValueFactory(new PropertyValueFactory<>("type"));
        clmDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    /**
     * Configure les colonnes de la TableView des consultations
     */
    public static void configureConsultationsTable(TableView<Consultations> tvConsultation,
                                                    TableColumn<Consultations, String> clmDetails,
                                                    TableColumn<Consultations, LocalDate> clmDate) {
        clmDetails.setCellValueFactory(new PropertyValueFactory<>("symptome"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
    }

    /**
     * Configure la TableView des bilans avec formatter personnalisé
     */
    public static void configureBilansTable(TableView<Analyse> tvBilan,
                                            TableColumn<Analyse, TypeAnalyse> clmLigneBilan) {
        clmLigneBilan.setCellValueFactory(new PropertyValueFactory<>("typeAnalyse"));
        clmLigneBilan.setCellFactory(column -> new TableCell<Analyse, TypeAnalyse>() {
            @Override
            protected void updateItem(TypeAnalyse item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCodeAnalyseFr());
                }
            }
        });
    }

    /**
     * Configure la TableView des imageries avec formatter personnalisé
     */
    public static void configureImagerieTable(TableView<Imagerie> tvRadio,
                                              TableColumn<Imagerie, TypeImagerie> clmLigneImagerie) {
        clmLigneImagerie.setCellValueFactory(new PropertyValueFactory<>("TypeImagerie"));
        clmLigneImagerie.setCellFactory(column -> new TableCell<Imagerie, TypeImagerie>() {
            @Override
            protected void updateItem(TypeImagerie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomImagerieFr());
                }
            }
        });
    }

    /**
     * Configure la TableView des consultations pour les ordonnances
     */
    public static void configureConsultationOrdonnanceTable(TableView<Consultations> tvConsultationOrd,
                                                             TableColumn<Consultations, LocalDate> clmDate,
                                                             TableColumn<Consultations, String> clmMotif) {
        clmDate.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        clmMotif.setCellValueFactory(new PropertyValueFactory<>("symptome"));

        clmDate.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                }
            }
        });
    }

    /**
     * Configure la TableView des prescriptions
     */
    public static void configurePrescriptionsTable(TableView<Prescriptions> tvOrdonnance,
                                                    TableColumn<Prescriptions, Medicaments> clmMedicament,
                                                    TableColumn<Prescriptions, String> clmDetails) {
        clmMedicament.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getMedicament())
        );
        clmMedicament.setCellFactory(column -> new TableCell<Prescriptions, Medicaments>() {
            @Override
            protected void updateItem(Medicaments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNomMedicament());
                }
            }
        });

        clmDetails.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDose() + " / " + cellData.getValue().getDuree())
        );
    }

    /**
     * Configure la TableView des rendez-vous
     */
    public static void configureRendezVousTable(TableView<RendezVous> tvRendezVous,
                                                 TableColumn<RendezVous, String> clmTitre,
                                                 TableColumn<RendezVous, LocalDate> clmDate,
                                                 TableColumn<RendezVous, String> clmDesc,
                                                 TableColumn<RendezVous, ?> clmHourStart,
                                                 TableColumn<RendezVous, ?> clmHourEnd) {
        clmTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        clmHourStart.setCellValueFactory(new PropertyValueFactory<>("hourStart"));
        clmHourEnd.setCellValueFactory(new PropertyValueFactory<>("hourEnd"));
    }

    /**
     * Configure la TableView des paiements
     */
    public static void configurePaiementsTable(TableView<Paiements> tvPaiement,
                                               TableColumn<Paiements, LocalDate> clmDate,
                                               TableColumn<Paiements, Double> clmMontant,
                                               TableColumn<Paiements, Double> clmVersement,
                                               TableColumn<Paiements, Double> clmReste) {
        clmDate.setCellValueFactory(new PropertyValueFactory<>("datePaiement"));
        clmMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        clmVersement.setCellValueFactory(new PropertyValueFactory<>("versment"));
        clmReste.setCellValueFactory(new PropertyValueFactory<>("reste"));
    }

    /**
     * Configure la TableView des actes avec colonne de sélection
     */
    public static void configureActesTable(TableView<ConsultationActe> tvActes,
                                           TableColumn<ConsultationActe, String> clmActe,
                                           TableColumn<ConsultationActe, Double> clmMontant) {
        clmActe.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getActe().getNomActe())
        );
        clmMontant.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getActe().getPrix()).asObject()
        );

        // Colonne pour les cases à cocher
        TableColumn<ConsultationActe, Boolean> selectionClm = new TableColumn<>("Sélectionner");
        selectionClm.setCellFactory(column -> new TableCell<ConsultationActe, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            private final HBox hbox = new HBox(checkBox);

            {
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(5));
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ConsultationActe acte = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(acte.isSelected());
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        acte.setSelected(newValue);
                    });
                    setGraphic(hbox);
                }
            }
        });

        tvActes.getColumns().add(selectionClm);
    }
}
