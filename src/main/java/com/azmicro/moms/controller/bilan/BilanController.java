package com.azmicro.moms.controller.bilan;

import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.dao.AnalyseDAOImpl;
import com.azmicro.moms.model.Analyse;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.service.AnalyseService;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import com.azmicro.moms.service.TypeAnalyseService;
import com.azmicro.moms.util.DatabaseUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BilanController implements Initializable {

    @FXML
    private VBox vboxLignesBilan; // Conteneur pour les lignes de bilan
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button btnAjouterLigneBilan;
    @FXML
    private Button btnAjouteBilan;
    @FXML
    private Button btnCancel;

    private ObservableList<String> allTypeAnalyses; // Liste de toutes les analyses

    private TypeAnalyseService typeAnalyseService;
    private AnalyseService analyseService;
    private Consultations consultation;

    private DossierController dossierController;

    public void setDossierController(DossierController dossierController) {
        this.dossierController = dossierController;
    }

    // Autres attributs et méthodes
    public void setConsultation(Consultations consultation) {
        this.consultation = consultation;
        // Vous pouvez utiliser l'objet consultation pour initialiser des champs ou effectuer des actions spécifiques
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            typeAnalyseService = new TypeAnalyseService();
            // Instanciation correcte de AnalyseService avec AnalyseDAOImpl
            AnalyseDAOImpl analyseDAO = new AnalyseDAOImpl(connection);
            this.analyseService = new AnalyseService(analyseDAO);
            // Initialiser la liste des types d'analyses
            allTypeAnalyses = FXCollections.observableArrayList(typeAnalyseService.getAllTypeAnalyses());

        } catch (SQLException ex) {
            Logger.getLogger(BilanController.class.getName()).log(Level.SEVERE, "Erreur lors de la connexion à la base de données", ex);
        }
    }

    @FXML
    private void ajouterLigneBilan(ActionEvent event) {
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
        Label labelComboBox = new Label("Type d'analyse");
        labelComboBox.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
        ComboBox<String> ligneComboBox = new ComboBox<>();
        ligneComboBox.setItems(FXCollections.observableArrayList(allTypeAnalyses));
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
        // Ajouter la VBox principale au VBox de lignes de bilan
        
        vboxLignesBilan.getChildren().add(vboxLigneComplete);

        // Ajouter l'écouteur de texte au champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterComboBox(ligneComboBox, newValue);
            // Forcer la mise à jour de l'affichage du ComboBox
            ligneComboBox.show();
        });
    }

    private void filterComboBox(ComboBox<String> comboBox, String searchText) {
        ObservableList<String> filteredItems = allTypeAnalyses.stream()
                .filter(item -> item.toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        comboBox.setItems(filteredItems);
        // Réinitialiser la sélection pour éviter des éléments cachés
        comboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void annuler(ActionEvent event) {
        // Clear all fields in the VBox
        for (Node node : vboxLignesBilan.getChildren()) {
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
@FXML
private void ajouterBilan(ActionEvent event) {
    // Parcourir chaque ligne du VBox
    for (Node node : vboxLignesBilan.getChildren()) {
        if (node instanceof VBox) {
            VBox vboxLigneComplete = (VBox) node;
            // Récupérer les HBox et VBox dans chaque VBox (assurez-vous de l'ordre des contrôles)
            HBox hboxRechercheComboBox = (HBox) vboxLigneComplete.getChildren().get(0);
            VBox vboxDescription = (VBox) vboxLigneComplete.getChildren().get(1);

            // Extraire les TextField et ComboBox
            TextField searchField = (TextField) ((VBox) hboxRechercheComboBox.getChildren().get(0)).getChildren().get(1);
            ComboBox<String> ligneComboBox = (ComboBox<String>) ((VBox) hboxRechercheComboBox.getChildren().get(1)).getChildren().get(1);
            TextField textField2 = (TextField) vboxDescription.getChildren().get(1);

            // Créer un objet Analyse avec les données
            Analyse analyse = new Analyse();
            String selectedItem = ligneComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Diviser la chaîne en utilisant ':' comme séparateur
                String[] parts = selectedItem.split(":");

                if (parts.length == 2) {
                    String nomAnalyse = parts[0];
                    String codeAnalyse = parts[1];
                    analyse.setTypeAnalyse(typeAnalyseService.findByNomAnalyseFr(nomAnalyse));
                    // Utiliser les valeurs extraites
                    System.out.println("Nom Analyse : " + nomAnalyse);
                    System.out.println("Code Analyse : " + codeAnalyse);
                } else {
                    System.out.println("La chaîne ne contient pas le séparateur ':' ou est mal formée.");
                }
            }
            // Ajouter la description et d'autres champs
            analyse.setDescription(textField2.getText());
            analyse.setDateAnalyse(LocalDate.now()); // Par exemple, la date actuelle
            analyse.setConsultationID(this.consultation.getConsultationID());
            
            // Sauvegarder l'analyse dans la base de données
            analyseService.saveAnalyse(analyse);
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

}
