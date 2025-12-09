/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.azmicro.moms.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * FXML Controller class
 *
 * @author Aissa
 */
public class ParametreController implements Initializable {
    @FXML private ComboBox<String> fontFamilyCombo;
    @FXML private Spinner<Integer> fontSizeSpinner;
    @FXML private Spinner<Integer> titleSizeSpinner;
    @FXML private Button saveFontSettingsBtn;
    @FXML private Button configureDatabaseBtn;
    @FXML private Label saveStatusLabel;

    private static final List<String> FONT_FAMILIES = Arrays.asList(
        "Calibri", "Cambria", "Book Antiqua", "Times New Roman", "Arial", "Verdana"
    );

    private Properties config;
    private File configFile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupConfig();
        setupControls();
        loadSettings();
        saveFontSettingsBtn.setOnAction(e -> saveSettings());
        if (configureDatabaseBtn != null) {
            configureDatabaseBtn.setOnAction(e -> openDatabaseConfig());
        }
    }

    private void setupConfig() {
        config = new Properties();
        String configFilePath = System.getProperty("user.home") + "/app-config/config.properties";
        configFile = new File(configFilePath);
        if (configFile.exists()) {
            try (InputStream in = new FileInputStream(configFile)) {
                config.load(in);
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    private void setupControls() {
        fontFamilyCombo.getItems().setAll(FONT_FAMILIES);
        fontFamilyCombo.setEditable(false);
        fontSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 24, 15));
        titleSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(12, 28, 16));
    }

    private void loadSettings() {
        String font = config.getProperty("pdf.font.family", "Calibri");
        int bodySize = parseInt(config.getProperty("pdf.font.size.body", "15"), 15);
        int titleSize = parseInt(config.getProperty("pdf.font.size.title", "16"), 16);
        fontFamilyCombo.getSelectionModel().select(font);
        fontSizeSpinner.getValueFactory().setValue(bodySize);
        titleSizeSpinner.getValueFactory().setValue(titleSize);
    }

    private void saveSettings() {
        String font = fontFamilyCombo.getSelectionModel().getSelectedItem();
        Integer bodySize = fontSizeSpinner.getValue();
        Integer titleSize = titleSizeSpinner.getValue();
        if (font == null || bodySize == null || titleSize == null) {
            saveStatusLabel.setText("Veuillez remplir tous les champs.");
            return;
        }
        config.setProperty("pdf.font.family", font);
        config.setProperty("pdf.font.size.body", String.valueOf(bodySize));
        config.setProperty("pdf.font.size.title", String.valueOf(titleSize));
        try {
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }
            try (FileOutputStream out = new FileOutputStream(configFile)) {
                config.store(out, "Font settings for PDFs");
            }
            saveStatusLabel.setText("Paramètres enregistrés.");
        } catch (IOException ex) {
            saveStatusLabel.setText("Erreur lors de l'enregistrement.");
        }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
    
    /**
     * Ouvre la fenêtre de configuration de la base de données
     */
    @FXML
    private void openDatabaseConfig() {
        com.azmicro.moms.util.DatabaseConfigDialog.show();
    }
    
}
