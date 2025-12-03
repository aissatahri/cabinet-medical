/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller.imagerie;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.service.ImagerieService;
import com.azmicro.moms.service.TypeImagerieService;
import com.azmicro.moms.util.DatabaseUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class ImagerieController implements Initializable {

    @FXML
    private Button btnAjouterLigneImagerie;
    @FXML
    private Button btnSaveImagerie;
    @FXML
    private Button btnCancel;
    @FXML
    private VBox vboxLignesImagerie;

    private ObservableList<String> allTypeImageries; // Liste de toutes les analyses

    private TypeImagerieService typeImagerieService;
    private ImagerieService imagerieService;
    private Consultations consultation;

    private DossierController dossierController;

    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController;
    }

    public void setConsultation(Consultations selectedConsultation) {
        this.consultation = selectedConsultation; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            // Obtention de la connexion à la base de données
            Connection connection = DatabaseUtil.getConnection();

            // Instanciation du DAO pour
            // Instanciation du service TypeImagerie avec le DAO
            this.typeImagerieService = new TypeImagerieService();

            this.imagerieService = new ImagerieService();

            // Récupérer et initialiser la liste des types d'imagerie
            allTypeImageries = FXCollections.observableArrayList(typeImagerieService.getAllTypeImagerie());

            // Vous pouvez maintenant utiliser 'allTypeImageries' pour peupler une vue (TableView, ComboBox, etc.)
        } catch (SQLException ex) {
            Logger.getLogger(ImagerieController.class.getName()).log(Level.SEVERE, "Erreur lors de la connexion à la base de données", ex);
        }
    }

    @FXML
    private void ajouterLigneImagerie(ActionEvent event) {
        // Créer la HBox pour les champs Recherche et ComboBox
        HBox hboxRechercheComboBox = new HBox(5.0);
        hboxRechercheComboBox.setPadding(new Insets(5));
        // Définir un style de fond et une bordure pour la HBox
        hboxRechercheComboBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #dcdcdc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // Créer les champs avec leurs labels
        VBox vboxRecherche = new VBox(5);
        // Définir un style pour le VBox
        vboxRecherche.setStyle("-fx-spacing: 5px; -fx-padding: 5px;");
        Label labelRecherche = new Label("Recherche");
        labelRecherche.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
        TextField searchField = new TextField();
        searchField.setPromptText("Recherche");
        vboxRecherche.getChildren().addAll(labelRecherche, searchField);

        VBox vboxComboBox = new VBox(5);
        // Définir un style pour le VBox
        vboxComboBox.setStyle("-fx-spacing: 5px; -fx-padding: 5px;");
        Label labelComboBox = new Label("Type d'imagerie");
        labelComboBox.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
        ComboBox<String> ligneComboBox = new ComboBox<>();
        ligneComboBox.setItems(FXCollections.observableArrayList(allTypeImageries));
        vboxComboBox.getChildren().addAll(labelComboBox, ligneComboBox);

        // Ajouter les VBox contenant les labels et les contrôles à la HBox principale
        hboxRechercheComboBox.getChildren().addAll(vboxRecherche, vboxComboBox);

        // Créer le VBox pour la description
        VBox vboxDescription = new VBox(5);
        // Définir un style pour le VBox
        vboxDescription.setStyle("-fx-spacing: 5px; -fx-padding: 5px;");
        Label labelDescription = new Label("Description");
        labelDescription.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
        TextField textField2 = new TextField();
        textField2.setPromptText("Entrez un autre texte");
        vboxDescription.getChildren().addAll(labelDescription, textField2);

        // Créer la VBox principale pour la disposition verticale
        VBox vboxLigneComplete = new VBox(5.0);
        vboxLigneComplete.setPadding(new Insets(10));
        vboxLigneComplete.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dcdcdc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // Ajouter la HBox avec Recherche et ComboBox, et la VBox avec Description à la VBox principale
        vboxLigneComplete.getChildren().addAll(hboxRechercheComboBox, vboxDescription);

        VBox.setMargin(vboxLigneComplete, new Insets(10));
        // Ajouter la VBox principale au VBox de lignes d'imagerie

        vboxLignesImagerie.getChildren().add(vboxLigneComplete);

        // Ajouter l'écouteur de texte au champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterComboBox(ligneComboBox, newValue);
            // Forcer la mise à jour de l'affichage du ComboBox
            ligneComboBox.show();
        });
    }

    private void filterComboBox(ComboBox<String> comboBox, String searchText) {
        ObservableList<String> filteredItems = allTypeImageries.stream()
                .filter(item -> item.toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        comboBox.setItems(filteredItems);
        // Réinitialiser la sélection pour éviter des éléments cachés
        comboBox.getSelectionModel().clearSelection();
    }
@FXML
private void saveBilan(ActionEvent event) {
    // Parcourir chaque ligne (VBox) dans vboxLignesImagerie
    for (Node node : vboxLignesImagerie.getChildren()) {
        if (node instanceof VBox) {
            VBox ligneImagerieVBox = (VBox) node;

            // Extraction des champs à partir des VBox internes
            HBox hboxRechercheComboBox = (HBox) ligneImagerieVBox.getChildren().get(0);
            VBox vboxRecherche = (VBox) hboxRechercheComboBox.getChildren().get(0);
            VBox vboxComboBox = (VBox) hboxRechercheComboBox.getChildren().get(1);

            // Récupérer les champs Recherche et ComboBox
            TextField searchField = (TextField) vboxRecherche.getChildren().get(1);
            ComboBox<String> ligneComboBox = (ComboBox<String>) vboxComboBox.getChildren().get(1);

            // Récupérer le champ Description
            VBox vboxDescription = (VBox) ligneImagerieVBox.getChildren().get(1);
            TextField descriptionField = (TextField) vboxDescription.getChildren().get(1);

            // Créer un objet Imagerie avec les données extraites
            Imagerie imagerie = new Imagerie();
            String selectedItem = ligneComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Diviser la chaîne en utilisant ':' comme séparateur
                String[] parts = selectedItem.split(":");
                if (parts.length == 2) {
                    String nomImagerie = parts[0];
                    String codeImagerie = parts[1];
                    imagerie.setTypeImagerie(typeImagerieService.findByNomImagerieFr(nomImagerie));
                    // Utiliser les valeurs extraites
                    System.out.println("Nom imagerie : " + nomImagerie);
                    System.out.println("Code imagerie : " + codeImagerie);
                } else {
                    System.out.println("La chaîne ne contient pas le séparateur ':' ou est mal formée.");
                }
            }

            // Assigner les autres champs à l'objet Imagerie
            imagerie.setDescription(descriptionField.getText());
            imagerie.setDateImagerie(LocalDate.now()); // Date actuelle
            imagerie.setConsultationID(this.consultation.getConsultationID());

            // Sauvegarder l'imagerie dans la base de données
            imagerieService.saveImagerie(imagerie);
        }
    }
    // Fermer la scène actuelle
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();

    // Rafraîchir la table dans DossierController
    Platform.runLater(() -> {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/medecin/dossier-view.fxml"));
            Parent root = fxmlLoader.load();
            DossierController dossierController = fxmlLoader.getController();
            // Passer l'instance de consultation à DossierController
            dossierController.refreshBilan();
            dossierController.initializeConsultationsDates(consultation.getPatient().getPatientID());
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur
        }
    });
}

    @FXML
    private void annuler(ActionEvent event) {
        // Parcourir chaque ligne du VBox
        // Clear all fields in the VBox
        for (Node node : vboxLignesImagerie.getChildren()) {
            if (node instanceof HBox) {
                HBox ligne = (HBox) node;
                TextField searchField = (TextField) ligne.getChildren().get(0);
                ComboBox<String> ligneComboBox = (ComboBox<String>) ligne.getChildren().get(1);
                TextField textField2 = (TextField) ligne.getChildren().get(2);

                // Clear the fields
                searchField.clear();
                ligneComboBox.getSelectionModel().clearSelection();
                textField2.clear();
            }
        }

        // Then close the current window without saving any data
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

}
