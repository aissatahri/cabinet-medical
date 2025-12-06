package com.azmicro.moms;

import com.azmicro.moms.util.DatabaseInitializer;
import com.azmicro.moms.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Objects;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

// Import AtlantaFX
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.NordDark;

/**
 * JavaFX App - Version avec AtlantaFX
 * 
 * Cette version montre comment appliquer les thèmes AtlantaFX à l'application.
 * Pour utiliser cette version, renommez App.java en App_backup.java
 * et renommez ce fichier en App.java
 */
public class App_AtlantaFX extends Application {

    private static final Logger logger = LogManager.getLogger(App_AtlantaFX.class);
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting application with AtlantaFX theme...");
        
        // Appliquer le thème AtlantaFX AVANT de créer la scène
        // Choisissez un des thèmes suivants :
        
        // Thème clair moderne (recommandé pour une application médicale)
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        
        // Autres options de thèmes :
        // Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        // Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
        // Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        
        // Always run initializer so schema migrations apply even when DB already exists
        DatabaseInitializer.initializeDatabase();

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);

        scene = new Scene(loadFXML("view/login-view"), 640, 520);
        
        // Vous pouvez ajouter des feuilles de style CSS supplémentaires
        // scene.getStylesheets().add(getClass().getResource("/com/azmicro/moms/css/custom.css").toExternalForm());
        
        stage.setScene(scene);
        stage.setTitle("SGM"); // Set the window title

        // Center the window on the screen
        stage.setWidth(800);
        stage.setHeight(600);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double x = (bounds.getWidth() - stage.getWidth()) / 2;
        double y = (bounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);

        stage.show();
        logger.info("Application started with AtlantaFX theme.");
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App_AtlantaFX.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "resources/log4j2.xml");
        launch();
    }
}
