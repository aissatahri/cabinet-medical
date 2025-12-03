/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.prescription;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.LignePrescription;
import com.azmicro.moms.model.Medicaments;
import com.azmicro.moms.model.Prescriptions;
import com.azmicro.moms.service.MedicamentsService;
import com.azmicro.moms.service.PrescriptionService;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class PrescriptionController implements Initializable {

    private Consultations consultations;
    @FXML
    private Button btnAjouterLignePrescription;
    @FXML
    private Button btnSavePresciption;
    @FXML
    private Button btnCancel;

    MedicamentsService medicamentsService;
    PrescriptionService prescriptionService;
    @FXML
    private VBox vboxLignesPrescription;

    private DossierController dossierController; // Référence au DossierController

    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.medicamentsService = new MedicamentsService();
        this.prescriptionService = new PrescriptionService();
    }

    public void setConsultation(Consultations selectedConsultation) {
        this.consultations = selectedConsultation; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @FXML
    private void ajouterLigneImagerie(ActionEvent event) {
        // Créez une nouvelle prescription vide
        Prescriptions prescription = new Prescriptions();

        // Déterminez si la nouvelle ligne est impaire ou paire en fonction du nombre actuel de lignes
        boolean isOdd = (vboxLignesPrescription.getChildren().size() % 2 == 0);

        // Créez une nouvelle ligne de prescription avec les services et la prescription
        LignePrescription lignePrescription = new LignePrescription(medicamentsService, prescriptionService, prescription, isOdd);

        // Ajoutez la ligne au VBox
        vboxLignesPrescription.getChildren().add(lignePrescription.getVBox());
    }

    private LignePrescription findLignePrescriptionByVBox(VBox vbox) {
        // Parcourez tous les enfants de vboxLignesPrescription pour trouver la VBox correspondante
        for (Node node : vboxLignesPrescription.getChildren()) {
            if (node instanceof VBox) {
                VBox currentVBox = (VBox) node;
                if (currentVBox.equals(vbox)) {
                    // Ici, on suppose que vous pouvez retrouver l'objet LignePrescription associé
                    // Vous devrez peut-être adapter cette méthode selon votre implémentation spécifique
                    return ((LignePrescription) currentVBox.getUserData());
                }
            }
        }
        return null;
    }

    @FXML
    private void annuler(ActionEvent event) {
        // Afficher une alerte de confirmation avant d'annuler
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir annuler les modifications ?");

        // Si l'utilisateur confirme l'annulation
        if (alert.showAndWait().get() == ButtonType.OK) {
            // Fermer la scène
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    private void clearLine(ActionEvent event) {
        // Assurez-vous que vous avez un moyen d'identifier la ligne à supprimer.
        // Par exemple, vous pouvez avoir un bouton "Supprimer" dans chaque ligne.
        Button btnClear = (Button) event.getSource();
        HBox lineToRemove = (HBox) btnClear.getParent(); // Récupérer le parent du bouton, supposé être un HBox
        // Supprimer la ligne du VBox
        vboxLignesPrescription.getChildren().remove(lineToRemove);
    }

    public void loadExistingPrescriptions() {

        System.out.println(consultations.toString());
        List<Prescriptions> lst = prescriptionService.getPrescriptionByConsultation(consultations.getConsultationID());
        System.out.println("size === " + lst.size());

        for (Prescriptions prescription : lst) {
            // Déterminez si la nouvelle ligne est impaire ou paire en fonction du nombre actuel de lignes
            boolean isOdd = (vboxLignesPrescription.getChildren().size() % 2 == 0);

            // Créez une nouvelle ligne de prescription avec les services et la prescription
            LignePrescription lignePrescription = new LignePrescription(medicamentsService, prescriptionService, prescription, isOdd);

            // Initialiser les champs avec les données de la prescription
            lignePrescription.getCmbMedicament().setValue(prescription.getMedicament().getNomMedicament() + "-" + prescription.getMedicament().getFormeDosage());
            lignePrescription.getTxtDose().setText(prescription.getDose());
            lignePrescription.getTxtDuree().setText(prescription.getDuree());
            lignePrescription.getTxtDescription().setText(prescription.getDescription());

            // Ajouter la ligne au VBox
            vboxLignesPrescription.getChildren().add(lignePrescription.getVBox());
        }
    }

    @FXML
    private void savePresciption(ActionEvent event) {
        boolean success = true;
        // Récupérer toutes les prescriptions existantes pour la consultation actuelle
        List<Prescriptions> existingPrescriptions = prescriptionService.getPrescriptionByConsultation(consultations.getConsultationID());
        for (Node node : vboxLignesPrescription.getChildren()) {
            if (node instanceof VBox) { // Updated from HBox to VBox
                VBox vbox = (VBox) node;

                // Trouver l'objet LignePrescription associé à cet VBox
                LignePrescription lignePrescription = findLignePrescriptionByVBox(vbox);
                if (lignePrescription != null) {
                    // Récupérer l'objet Medicaments à partir de la ligne de prescription
                    Medicaments medicament = lignePrescription.getMedicamentObject();

                    // Rechercher une prescription existante pour ce médicament
                    Prescriptions existingPrescription = existingPrescriptions.stream()
                            .filter(p -> p.getMedicament().getMedicamentID() == medicament.getMedicamentID())
                            .findFirst()
                            .orElse(null);

                    Prescriptions prescription;
                    if (existingPrescription != null) {
                        // Si la prescription existe, la mettre à jour
                        prescription = existingPrescription;
                    } else {
                        // Si elle n'existe pas, créer une nouvelle prescription
                        prescription = new Prescriptions();
                        prescription.setConsultationID(consultations.getConsultationID());
                        prescription.setMedicament(medicament);
                    }

                    // Remplir ou mettre à jour les autres détails de la prescription
                    prescription.setDose(lignePrescription.getDose());
                    prescription.setDuree(lignePrescription.getDuree());

                    try {
                        // Enregistrer ou mettre à jour l'objet Prescription
                        boolean result;
                        if (existingPrescription != null) {
                            result = prescriptionService.updatePrescription(prescription);
                        } else {
                            result = prescriptionService.savePrescription(prescription);
                        }

                        if (!result) {
                            success = false;
                        }
                    } catch (Exception e) {
                        // En cas d'erreur d'enregistrement, marquer l'opération comme un échec
                        success = false;
                        e.printStackTrace();
                    }
                }
            }
        }

        // Afficher une alerte de confirmation si l'enregistrement a réussi
        if (success) {
            if (dossierController != null) {
                dossierController.loadPrescriptionsForConsultation(consultations);
                dossierController.loadConsultations();
                dossierController.getConsultationSelected(); // Recharger les consultations dans DossierController
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Enregistrement réussi");
            alert.setHeaderText(null);
            alert.setContentText("La prescription a été enregistrée avec succès.");
            alert.showAndWait();
            // Fermer la scène après l'enregistrement
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else {
            // Afficher une alerte d'erreur si l'enregistrement a échoué
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'enregistrement");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'enregistrement des prescriptions. Veuillez réessayer.");
            alert.showAndWait();
        }
    }
}
