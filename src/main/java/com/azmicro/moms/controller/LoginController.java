package com.azmicro.moms.controller;

import com.azmicro.moms.controller.assistante.DashboardAssistanteController;
import com.azmicro.moms.controller.medecin.DashboardMedecinController;
import com.azmicro.moms.controller.medecin.DossierController;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.service.UtilisateurService;
import com.azmicro.moms.util.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javafx.application.Platform;

import javafx.scene.control.Alert;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.prefs.Preferences;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button togglePasswordVisibility;

    @FXML
    private FontIcon togglePasswordIcon;

    @FXML
    private AnchorPane passwordContainer;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCreateUser;

    @FXML
    private Label statusMessage;

    @FXML
    private Button btnClose;

    @FXML
    private CheckBox rememberMeCheckBox;

    private UtilisateurService utilisateurService;
    private boolean isPasswordVisible = false;
    private static final String PREF_USERNAME = "username";
    private static final String PREF_REMEMBER_ME = "rememberMe";

    @FXML
    private void initialize() {
        this.utilisateurService = new UtilisateurService();
        passwordField.setVisible(true);
        passwordTextField.setVisible(false);
        checkDatabaseConnection();

        // Ajoutez le gestionnaire d'événements pour les touches
        usernameField.setOnKeyPressed(this::handleKeyPress);
        passwordField.setOnKeyPressed(this::handleKeyPress);
        passwordTextField.setOnKeyPressed(this::handleKeyPress);

        // Charger les préférences
        loadPreferences();

        // Mettre le focus sur le champ usernameField
        Platform.runLater(() -> {
            if (usernameField.getText().isEmpty()) {
                usernameField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
        });
    }

    private void checkDatabaseConnection() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                statusMessage.setText("Connecté à la base de données.");
                statusMessage.setStyle("-fx-text-fill: green;");
            } else {
                displayConnectionError();
            }
        } catch (SQLException e) {
            displayConnectionError();
        }
    }

    private void loadPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        boolean rememberMe = prefs.getBoolean(PREF_REMEMBER_ME, false);
        
        if (rememberMe) {
            String savedUsername = prefs.get(PREF_USERNAME, "");
            usernameField.setText(savedUsername);
            rememberMeCheckBox.setSelected(true);
        } else {
            // Forcer la checkbox à être décochée si pas de préférence
            usernameField.clear();
            rememberMeCheckBox.setSelected(false);
        }
        
        // Toujours vider le champ mot de passe pour la sécurité
        passwordField.clear();
        passwordTextField.clear();
    }

    private void savePreferences() {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        
        if (rememberMeCheckBox.isSelected()) {
            prefs.put(PREF_USERNAME, usernameField.getText());
            prefs.putBoolean(PREF_REMEMBER_ME, true);
        } else {
            prefs.remove(PREF_USERNAME);
            prefs.putBoolean(PREF_REMEMBER_ME, false);
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login(null);  // Appelle la méthode login sans événement
        }
    }

    private void displayConnectionError() {
        statusMessage.setText("Échec de la connexion à la base de données.");
        statusMessage.setStyle("-fx-text-fill: red;");
        
        // Créer une alerte avec des boutons personnalisés
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de Connexion");
        alert.setHeaderText("Impossible de se connecter à la base de données");
        alert.setContentText("La connexion à la base de données a échoué.\n\n" +
                            "Vérifiez que :\n" +
                            "- Le serveur MySQL est démarré\n" +
                            "- Les paramètres de connexion sont corrects\n" +
                            "- Le pare-feu autorise la connexion\n\n" +
                            "Voulez-vous configurer les paramètres de connexion ?");
        
        // Ajouter des boutons personnalisés
        ButtonType configButton = new ButtonType("⚙ Configurer");
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(configButton, cancelButton);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == configButton) {
            // Ouvrir la fenêtre de configuration
            com.azmicro.moms.util.DatabaseConfigDialog.show();
            // Re-vérifier la connexion après la configuration
            checkDatabaseConnection();
        }
    }

    private void loadDashboard(Utilisateur utilisateur) throws IOException {
        String fxmlFile;
        FXMLLoader loader = new FXMLLoader();
        switch (utilisateur.getRole()) {
            case SECRETAIRE:
                fxmlFile = "/com/azmicro/moms/view/assistante/dashboardAssistante-view.fxml";
                loader.setLocation(getClass().getResource(fxmlFile));
                loader.load();
                DashboardAssistanteController assistanteController = loader.getController();
                assistanteController.setUtilisateur(utilisateur);
                break;
            case MEDECIN:
            case ADMIN:
                fxmlFile = "/com/azmicro/moms/view/medecin/dashboardMedecin-view.fxml";
                loader.setLocation(getClass().getResource(fxmlFile));
                loader.load();
                DashboardMedecinController medecinController = loader.getController();
                medecinController.setUtilisateur(utilisateur);

                break;
            default:
                throw new IllegalArgumentException("Rôle non reconnu: " + utilisateur.getRole());
        }

        AnchorPane root = loader.getRoot();
        Stage stage = new Stage();
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        Scene dashboardScene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());
        stage.setScene(dashboardScene);
        stage.setX(visualBounds.getMinX());
        stage.setY(visualBounds.getMinY());
        stage.setWidth(visualBounds.getWidth());
        stage.setHeight(visualBounds.getHeight());
        Image icon = new Image(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png"));
        stage.getIcons().add(icon);
        stage.setMaximized(true);
        stage.setTitle("SGM");
        stage.show();
    }

    @FXML
    private void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Informations manquantes", "Veuillez entrer votre nom d'utilisateur et mot de passe.");
            return;
        }

        Utilisateur utilisateur = utilisateurService.authenticateUser(username, password);

        if (utilisateur != null) {
            // Sauvegarder les préférences si Se souvenir de moi est coché
            savePreferences();
            
            try {
                loadDashboard(utilisateur);
                Stage primaryStage = (Stage) btnLogin.getScene().getWindow();
                primaryStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Échec de l'authentification", "Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createNewUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/azmicro/moms/view/createUser-view.fxml"));
            Parent root = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Créer un nouveau utilisateur");
            dialog.setHeaderText(null);
            dialog.getDialogPane().setContent(root);

            Image icon = new Image(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png"));
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(icon);

            ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, cancelButtonType);

            dialog.getDialogPane().setStyle(
                    "-fx-border-color: #cccccc; "
                    + "-fx-border-width: 2px; "
                    + "-fx-border-radius: 0 0 5px 5px; "
                    + "-fx-padding: 10px;"
            );

            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(((Node) event.getSource()).getScene().getWindow());

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == createButtonType) {
                CreateUserController controller = loader.getController();
                Utilisateur utilisateur = controller.getUserInput();
                if (utilisateur != null) {
                    if (utilisateurService.doesUserExist(utilisateur.getNomUtilisateur())) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Un utilisateur avec ce nom d'utilisateur existe déjà.");
                    } else {
                        utilisateurService.saveUtilisateur(utilisateur);
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur créé avec succès.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des informations utilisateur.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        // Basculer la visibilité du mot de passe
        if (isPasswordVisible) {
            // Si le mot de passe est visible, le cacher
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
            togglePasswordIcon.setIconLiteral("fas-eye");
        } else {
            // Si le mot de passe est caché, l'afficher
            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
            togglePasswordIcon.setIconLiteral("fas-eye-slash");
        }

        // Basculer l'état de visibilité du mot de passe
        isPasswordVisible = !isPasswordVisible;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
