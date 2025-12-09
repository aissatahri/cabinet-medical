package com.azmicro.moms;

import com.azmicro.moms.util.DatabaseInitializer;
import com.azmicro.moms.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting application...");
        
        // Vérifier et configurer la base de données
        boolean dbConfigured = false;
        int retryCount = 0;
        final int MAX_RETRIES = 3;
        
        while (!dbConfigured && retryCount < MAX_RETRIES) {
            // Vérifier si la configuration existe
            if (!checkDatabaseConfig()) {
                logger.warn("Database configuration not found.");
                if (!showDatabaseConfigDialog(stage)) {
                    logger.info("Database configuration cancelled. Exiting application.");
                    System.exit(0);
                    return;
                }
            } else {
                // Charger la configuration existante
                loadDatabaseConfig();
            }
            
            // Tester la connexion
            if (testDatabaseConnection()) {
                dbConfigured = true;
                logger.info("Database connection successful.");
            } else {
                retryCount++;
                logger.error("Database connection failed. Attempt " + retryCount + " of " + MAX_RETRIES);
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de Connexion");
                alert.setHeaderText("Impossible de se connecter à la base de données");
                alert.setContentText("Vérifiez que :\n" +
                    "1. Le serveur MySQL est démarré\n" +
                    "2. L'adresse IP/hôte est correcte\n" +
                    "3. Le port est correct (défaut: 3306)\n\n" +
                    "Voulez-vous reconfigurer la connexion ?");
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                
                if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                    if (!showDatabaseConfigDialog(stage)) {
                        logger.info("Database configuration cancelled. Exiting application.");
                        System.exit(0);
                        return;
                    }
                } else {
                    logger.info("User chose not to reconfigure. Exiting application.");
                    System.exit(0);
                    return;
                }
            }
        }
        
        if (!dbConfigured) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur Fatale");
            alert.setHeaderText("Impossible de se connecter à la base de données");
            alert.setContentText("L'application ne peut pas démarrer sans connexion à la base de données.");
            alert.showAndWait();
            System.exit(1);
            return;
        }
        
        // Always run initializer to apply schema migrations on existing DBs
        DatabaseInitializer.initializeDatabase();

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);

        Scene scene = new Scene(loadFXML("view/login-view"), 640, 520);
        stage.setScene(scene);
        stage.setTitle("SGM"); // Set the window title
        
        // Supprimer la barre de titre (pas de boutons fermer/agrandir/réduire)
        stage.initStyle(StageStyle.UNDECORATED);

        // Rendre la fenêtre non redimensionnable
        stage.setResizable(false);

        // Center the window on the screen
        stage.setWidth(900);
        stage.setHeight(600);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double x = (bounds.getWidth() - stage.getWidth()) / 2;
        double y = (bounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);

        stage.show();
        logger.info("Application started.");
    }
    
    private boolean checkDatabaseConfig() {
        // Vérifier via DatabaseConfig
        File configFile = new File(com.azmicro.moms.util.DatabaseConfig.getConfigFilePath());
        return configFile.exists();
    }
    
    private void loadDatabaseConfig() {
        // La configuration est déjà chargée automatiquement par DatabaseConfig
        logger.info("Database configuration loaded: {}:{}", 
            com.azmicro.moms.util.DatabaseConfig.getHost(), 
            com.azmicro.moms.util.DatabaseConfig.getPort());
    }
    
    private boolean testDatabaseConnection() {
        try {
            DatabaseUtil.getConnection().close();
            return true;
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
    
    private boolean showDatabaseConfigDialog(Stage owner) {
        try {
            // Utiliser le DatabaseConfigDialog existant
            com.azmicro.moms.util.DatabaseConfigDialog.show();
            return true;
        } catch (Exception e) {
            logger.error("Error showing database config dialog", e);
            return false;
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "resources/log4j2.xml");
        launch();
    }
}
