/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

import com.azmicro.moms.service.MedicamentsService;
import com.azmicro.moms.service.PrescriptionService;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 *
 * @author Aissa
 */
public class LignePrescription {

    private HBox hbox;
    private HBox hboxRechercheMedicament;
    private VBox vbox;
    private HBox hboxDoseDuree;
    private TextField txtRecherche;
    private ComboBox<String> cmbMedicament;
    private TextField txtDose;
    private TextField txtDuree;
    private TextField txtDescription; // Nouveau TextField pour la description
    private Button btnClear;
    private MedicamentsService medicamentsService; // Ajoutez une référence à MedicamentsService
    private PrescriptionService prescriptionService; // Service pour manipuler les prescriptions
    private Prescriptions prescription; // Objet Prescription associé

    public LignePrescription(MedicamentsService medicamentsService, PrescriptionService prescriptionService, Prescriptions prescription, boolean isOdd) {
        this.medicamentsService = medicamentsService;
        this.prescriptionService = prescriptionService;
        this.prescription = prescription;
        this.vbox = new VBox(5);

        this.vbox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dcdcdc; -fx-border-width: 1; -fx-border-radius: 10;");

        this.vbox.setPadding(new Insets(10, 20, 10, 20));

        VBox.setMargin(vbox, new Insets(10));

        this.hboxRechercheMedicament = new HBox(10);
        this.hboxDoseDuree = new HBox(10);

        this.txtRecherche = new TextField();
        this.txtRecherche.setPromptText("Rechercher médicament");
        this.txtRecherche.setPrefWidth(200);

        this.cmbMedicament = new ComboBox<>();
        this.cmbMedicament.setPromptText("Médicament");
        this.cmbMedicament.setPrefWidth(300);

        ObservableList<String> medicamentsList = FXCollections.observableArrayList(
                medicamentsService.findAllMedicaments().stream()
                        .map(medicament -> medicament.getNomMedicament() + "-" + medicament.getFormeDosage())
                        .collect(Collectors.toList())
        );
        this.cmbMedicament.setItems(medicamentsList);
        cmbMedicament.setEditable(true);

        txtRecherche.textProperty().addListener((obs, oldText, newText) -> {
            filterComboBox(newText, medicamentsList);
        });

        this.txtDescription = new TextField();

        this.txtDose = new TextField();
        this.txtDose.setPromptText("Dose");
        this.txtDose.setPrefWidth(100);

        this.txtDuree = new TextField();
        this.txtDuree.setPromptText("Durée");
        this.txtDuree.setPrefWidth(100);

        this.txtDescription.setPromptText("Description");
        this.txtDescription.setPrefWidth(300);

        this.btnClear = new Button();
        FontIcon icon = new FontIcon(FontAwesomeSolid.TRASH);
        icon.setIconSize(24);
        icon.setIconColor(Color.RED);
        this.btnClear.setGraphic(icon);
        this.btnClear.setBackground(Background.EMPTY);
        this.btnClear.setOnAction(event -> clearLine());

        // Ajouter les éléments aux HBox
        hboxRechercheMedicament.getChildren().addAll(txtRecherche, cmbMedicament);
        hboxDoseDuree.getChildren().addAll(txtDose, txtDuree, txtDescription);
// Créez un HBox pour contenir les éléments de hboxDoseDuree et le bouton de suppression
        HBox hboxDoseDureeWithButton = new HBox(10);

// Créez un autre HBox pour contenir les champs de dose, durée et description
        HBox hboxFields = new HBox(10);
        hboxFields.getChildren().addAll(txtDose, txtDuree, txtDescription);

// Ajoutez les éléments à hboxDoseDureeWithButton
        hboxDoseDureeWithButton.getChildren().addAll(hboxFields, btnClear);

// Alignez le bouton de suppression à droite dans hboxDoseDureeWithButton
        HBox.setHgrow(hboxFields, Priority.ALWAYS);
        hboxDoseDureeWithButton.setAlignment(Pos.CENTER_LEFT);

// Appliquer HGrow pour permettre aux champs de s'étendre
        HBox.setHgrow(txtDose, Priority.ALWAYS);
        HBox.setHgrow(txtDuree, Priority.ALWAYS);
        HBox.setHgrow(txtDescription, Priority.ALWAYS);

// Appliquer des styles CSS alternés
        if (isOdd) {
            hboxRechercheMedicament.setStyle("-fx-background-color: #f2f2f2;");
            hboxDoseDureeWithButton.setStyle("-fx-background-color: #f2f2f2;");
        } else {
            hboxRechercheMedicament.setStyle("-fx-background-color: #ffffff;");
            hboxDoseDureeWithButton.setStyle("-fx-background-color: #ffffff;");
        }

// Ajouter les éléments à la VBox
        vbox.getChildren().addAll(hboxRechercheMedicament, hboxDoseDureeWithButton);

// Définir les marges pour la VBox
        vbox.setPadding(new Insets(10, 20, 10, 20));

        this.vbox.setUserData(this);
    }

    private void filterComboBox(String searchText, ObservableList<String> originalList) {
        if (searchText == null || searchText.isEmpty()) {
            cmbMedicament.setItems(originalList);
        } else {
            ObservableList<String> filteredList = originalList.stream()
                    .filter(item -> item.toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            cmbMedicament.setItems(filteredList);
        }
        cmbMedicament.show();
    }

    public HBox getHBox() {
        return hbox;
    }

    public String getMedicament() {
        return cmbMedicament.getValue();
    }

    // Nouvelle méthode pour obtenir l'objet Medicaments
    public Medicaments getMedicamentObject() {
        String selectedMedicament = cmbMedicament.getValue();
        if (selectedMedicament != null) {
            String[] parts = selectedMedicament.split("-");
            if (parts.length == 2) {
                String medicamentName = parts[0];
                String formeDosage = parts[1];
                Medicaments medicaments = medicamentsService.findByNameAndFormeDosage(medicamentName, formeDosage);
                return medicaments;
            }
        }
        return null;
    }

    public String getDose() {
        return txtDose.getText();
    }

    public String getDuree() {
        return txtDuree.getText();
    }

    public ComboBox<String> getCmbMedicament() {
        return cmbMedicament;
    }

    public TextField getTxtDose() {
        return txtDose;
    }

    public TextField getTxtDuree() {
        return txtDuree;
    }

    private void clearLine() {
        // Vérifiez si les champs sont vides (pas de médicament sélectionné, dose et durée vides)
        boolean isEmpty = (cmbMedicament.getValue() == null || cmbMedicament.getValue().trim().isEmpty())
                && (txtDose.getText() == null || txtDose.getText().trim().isEmpty())
                && (txtDuree.getText() == null || txtDuree.getText().trim().isEmpty());

        if (isEmpty) {
            // Supprimer directement l'HBox de la VBox sans confirmation
            Node current = vbox;
            while (current != null) {
                Parent parent = current.getParent();
                if (parent instanceof VBox) {
                    VBox vBoxParent = (VBox) parent;
                    vBoxParent.getChildren().remove(current);
                    return;
                }
                current = parent;
            }
            System.err.println("VBox not found in parent chain.");
        } else {
            // Créer une boîte de dialogue de confirmation si la ligne n'est pas vide
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer la prescription");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette prescription ?");

            // Ajouter des boutons pour les options Oui et Non
            ButtonType buttonYes = new ButtonType("Oui");
            ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonYes, buttonNo);

            // Attendre la réponse de l'utilisateur
            alert.showAndWait().ifPresent(response -> {
                if (response == buttonYes) {
                    // Supprimer la prescription de la base de données
                    if (prescription != null) {
                        prescriptionService.deletePrescription(prescription.getPrescriptionID());
                    }

                    // Supprimer l'HBox de la VBox
                    Node current = vbox;
                    while (current != null) {
                        Parent parent = current.getParent();
                        if (parent instanceof VBox) {
                            VBox vBoxParent = (VBox) parent;
                            vBoxParent.getChildren().remove(current);
                            return;
                        }
                        current = parent;
                    }
                    System.err.println("VBox not found in parent chain.");
                } else {
                    // Si l'utilisateur choisit "Non", ne rien faire
                    System.out.println("Suppression annulée.");
                }
            });
        }
    }

    public String getDetails() {
        return String.format("Médicament: %s, Dose: %s, Durée: %s",
                cmbMedicament.getValue(), txtDose.getText(), txtDuree.getText());
    }

    public VBox getVBox() {
        return vbox;
    }

    // Nouvelle méthode pour obtenir la description
    public String getDescription() {
        return txtDescription.getText();
    }

    public TextField getTxtDescription() {
        return txtDescription;
    }
}
