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
/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting application...");
        if (!DatabaseUtil.databaseExists()) {
            logger.info("Database does not exist. Initializing...");
            DatabaseInitializer.initializeDatabase();
        }

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/azmicro/moms/images/cardiology.png")));
        stage.getIcons().add(icon);

        Scene scene = new Scene(loadFXML("view/login-view"), 640, 520);
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
        logger.info("Application started.");
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
