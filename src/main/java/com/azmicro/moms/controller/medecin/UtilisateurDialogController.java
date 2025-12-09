package com.azmicro.moms.controller.medecin;

import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Role;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.MedecinService;
import com.azmicro.moms.service.UtilisateurService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UtilisateurDialogController implements Initializable {

    @FXML private Label lblTitre;
    @FXML private TextField txtNomUtilisateur;
    @FXML private PasswordField txtMotDePasse;
    @FXML private PasswordField txtConfirmerMotDePasse;
    @FXML private ComboBox<Role> cbRole;
    @FXML private Label lblMedecin;
    @FXML private ComboBox<Medecin> cbMedecin;
    @FXML private Label lblMessage;
    @FXML private Button btnEnregistrer;
    @FXML private Button btnAnnuler;

    private UtilisateurService utilisateurService;
    private MedecinService medecinService;
    private Utilisateur utilisateurActuel;
    private boolean modeEdition = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilisateurService = new UtilisateurService();
        medecinService = new MedecinService();

        // Initialiser ComboBox des rôles
        cbRole.setItems(FXCollections.observableArrayList(Role.values()));
        
        // Listener pour afficher/masquer le champ médecin selon le rôle
        cbRole.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isMedecin = newVal == Role.MEDECIN;
            lblMedecin.setVisible(isMedecin);
            lblMedecin.setManaged(isMedecin);
            cbMedecin.setVisible(isMedecin);
            cbMedecin.setManaged(isMedecin);
        });

        // Charger la liste des médecins
        chargerMedecins();

        // Configurer le convertisseur pour le ComboBox médecin
        cbMedecin.setConverter(new StringConverter<Medecin>() {
            @Override
            public String toString(Medecin medecin) {
                return medecin == null ? "" : "Dr. " + medecin.getNom() + " " + medecin.getPrenom();
            }

            @Override
            public Medecin fromString(String string) {
                return null;
            }
        });
    }

    private void chargerMedecins() {
        try {
            List<Medecin> medecins = medecinService.getAllMedecins();
            cbMedecin.setItems(FXCollections.observableArrayList(medecins));
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement des médecins : " + e.getMessage());
        }
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurActuel = utilisateur;
        this.modeEdition = true;
        lblTitre.setText("Modifier l'Utilisateur");

        // Remplir les champs
        txtNomUtilisateur.setText(utilisateur.getNomUtilisateur());
        txtNomUtilisateur.setEditable(false); // Ne pas permettre de modifier le nom d'utilisateur
        cbRole.setValue(utilisateur.getRole());
        
        if (utilisateur.getRole() == Role.MEDECIN && utilisateur.getMedecin() != null) {
            cbMedecin.setValue(utilisateur.getMedecin());
        }

        // En mode édition, les champs de mot de passe sont optionnels
        txtMotDePasse.setPromptText("Laisser vide pour ne pas modifier");
        txtConfirmerMotDePasse.setPromptText("Laisser vide pour ne pas modifier");
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        // Validation
        if (!validerFormulaire()) {
            return;
        }

        try {
            if (modeEdition) {
                // Mise à jour
                if (!txtMotDePasse.getText().isEmpty()) {
                    utilisateurActuel.setMotDePasse(txtMotDePasse.getText());
                }
                utilisateurActuel.setRole(cbRole.getValue());
                
                if (cbRole.getValue() == Role.MEDECIN) {
                    utilisateurActuel.setMedecin(cbMedecin.getValue());
                } else {
                    utilisateurActuel.setMedecin(null);
                }

                utilisateurService.updateUtilisateur(utilisateurActuel);
                afficherSucces("Utilisateur modifié avec succès");
            } else {
                // Création
                Utilisateur nouvelUtilisateur = new Utilisateur();
                nouvelUtilisateur.setNomUtilisateur(txtNomUtilisateur.getText().trim());
                nouvelUtilisateur.setMotDePasse(txtMotDePasse.getText());
                nouvelUtilisateur.setRole(cbRole.getValue());
                nouvelUtilisateur.setDateCreation(LocalDate.now());

                if (cbRole.getValue() == Role.MEDECIN) {
                    nouvelUtilisateur.setMedecin(cbMedecin.getValue());
                }

                utilisateurService.saveUtilisateur(nouvelUtilisateur);
                afficherSucces("Utilisateur créé avec succès");
            }

            // Fermer la fenêtre après un délai
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() -> fermerDialogue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            afficherErreur("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler(ActionEvent event) {
        fermerDialogue();
    }

    private boolean validerFormulaire() {
        // Vérifier nom d'utilisateur
        if (txtNomUtilisateur.getText() == null || txtNomUtilisateur.getText().trim().isEmpty()) {
            afficherErreur("Le nom d'utilisateur est obligatoire");
            return false;
        }

        // Vérifier unicité du nom d'utilisateur (en mode création uniquement)
        if (!modeEdition && utilisateurService.doesUserExist(txtNomUtilisateur.getText().trim())) {
            afficherErreur("Ce nom d'utilisateur existe déjà");
            return false;
        }

        // Vérifier mot de passe (obligatoire en création, optionnel en édition)
        if (!modeEdition) {
            if (txtMotDePasse.getText() == null || txtMotDePasse.getText().isEmpty()) {
                afficherErreur("Le mot de passe est obligatoire");
                return false;
            }
            if (txtMotDePasse.getText().length() < 3) {
                afficherErreur("Le mot de passe doit contenir au moins 3 caractères");
                return false;
            }
        }

        // Vérifier confirmation mot de passe si un mot de passe est saisi
        if (!txtMotDePasse.getText().isEmpty()) {
            if (!txtMotDePasse.getText().equals(txtConfirmerMotDePasse.getText())) {
                afficherErreur("Les mots de passe ne correspondent pas");
                return false;
            }
        }

        // Vérifier rôle
        if (cbRole.getValue() == null) {
            afficherErreur("Veuillez sélectionner un rôle");
            return false;
        }

        // Vérifier médecin si rôle MEDECIN
        if (cbRole.getValue() == Role.MEDECIN && cbMedecin.getValue() == null) {
            afficherErreur("Veuillez sélectionner un médecin");
            return false;
        }

        return true;
    }

    private void afficherErreur(String message) {
        lblMessage.setText("❌ " + message);
        lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
    }

    private void afficherSucces(String message) {
        lblMessage.setText("✓ " + message);
        lblMessage.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");
    }

    private void fermerDialogue() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }
}
