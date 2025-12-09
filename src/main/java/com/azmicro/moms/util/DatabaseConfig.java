/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util;

import java.io.*;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Classe pour gérer la configuration de la base de données
 * @author Aissa
 */
public class DatabaseConfig {
    
    private static final String CONFIG_FILENAME = "database.properties";
    private static final String RESOURCE_PATH = "/database.properties";
    private static final String APP_DIR = "CabinetMedical";
    private static Properties properties;
    
    static {
        loadConfig();
    }
    
    /**
     * Retourne le chemin du fichier de configuration dans AppData
     */
    private static File getConfigFile() {
        try {
            // Utiliser le dossier AppData de l'utilisateur
            String userHome = System.getProperty("user.home");
            File appDataDir = new File(userHome, "AppData" + File.separator + "Roaming" + File.separator + APP_DIR);
            
            // Créer le répertoire s'il n'existe pas
            if (!appDataDir.exists()) {
                boolean created = appDataDir.mkdirs();
                if (created) {
                    System.out.println("Répertoire de configuration créé: " + appDataDir.getAbsolutePath());
                } else {
                    System.err.println("Impossible de créer le répertoire: " + appDataDir.getAbsolutePath());
                }
            }
            
            File configFile = new File(appDataDir, CONFIG_FILENAME);
            System.out.println("Chemin du fichier de configuration: " + configFile.getAbsolutePath());
            return configFile;
        } catch (Exception e) {
            System.err.println("Erreur lors de la détermination du chemin de configuration: " + e.getMessage());
            e.printStackTrace();
            // Fallback vers le répertoire courant
            return new File(CONFIG_FILENAME);
        }
    }
    
    /**
     * Charge la configuration depuis le fichier properties
     */
    private static void loadConfig() {
        properties = new Properties();
        boolean configLoaded = false;
        
        try {
            // Essayer de charger depuis le fichier dans AppData d'abord
            File configFile = getConfigFile();
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    properties.load(fis);
                    System.out.println("Configuration chargée depuis: " + configFile.getAbsolutePath());
                    configLoaded = true;
                    return;
                } catch (IOException e) {
                    System.err.println("Erreur lors du chargement du fichier de configuration: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Fichier de configuration non trouvé: " + configFile.getAbsolutePath());
            }
            
            // Essayer le fichier dans le répertoire courant (pour compatibilité)
            File localFile = new File(CONFIG_FILENAME);
            if (localFile.exists()) {
                try (FileInputStream fis = new FileInputStream(localFile)) {
                    properties.load(fis);
                    System.out.println("Configuration chargée depuis: " + localFile.getAbsolutePath());
                    configLoaded = true;
                    return;
                } catch (IOException e) {
                    System.err.println("Erreur lors du chargement du fichier local: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Sinon, charger depuis les ressources
            try (InputStream input = DatabaseConfig.class.getResourceAsStream(RESOURCE_PATH)) {
                if (input != null) {
                    properties.load(input);
                    System.out.println("Configuration chargée depuis les ressources");
                    configLoaded = true;
                } else {
                    System.err.println("Impossible de trouver " + RESOURCE_PATH);
                }
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement de la configuration depuis les ressources: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Erreur critique lors du chargement de la configuration: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Si aucune configuration n'a été chargée, utiliser les valeurs par défaut
            if (!configLoaded || properties.isEmpty()) {
                System.out.println("Utilisation de la configuration par défaut");
                setDefaultConfig();
            }
        }
    }
    
    /**
     * Définit les valeurs par défaut si le fichier n'existe pas
     */
    private static void setDefaultConfig() {
        properties.setProperty("db.host", "localhost");
        properties.setProperty("db.port", "3306");
        properties.setProperty("db.name", "CabinetMedicalbis");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "");
    }
    
    /**
     * Sauvegarde la configuration dans un fichier externe
     */
    public static boolean saveConfig(String host, String port, String dbName, String username, String password) {
        properties.setProperty("db.host", host);
        properties.setProperty("db.port", port);
        properties.setProperty("db.name", dbName);
        properties.setProperty("db.username", username);
        properties.setProperty("db.password", password);
        
        File configFile = getConfigFile();
        
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            properties.store(fos, "Configuration de la base de données - Modifié le " + java.time.LocalDateTime.now());
            System.out.println("Configuration sauvegardée dans: " + configFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de la configuration: " + e.getMessage());
            e.printStackTrace();
            showError("Erreur de sauvegarde", 
                "Impossible de sauvegarder la configuration dans:\n" + 
                configFile.getAbsolutePath() + "\n\n" +
                "Erreur: " + e.getMessage() + "\n\n" +
                "Vérifiez que vous avez les droits d'écriture.");
            return false;
        }
    }
    
    /**
     * Retourne le chemin du fichier de configuration
     */
    public static String getConfigFilePath() {
        return getConfigFile().getAbsolutePath();
    }
    
    /**
     * Recharge la configuration depuis le fichier
     */
    public static void reloadConfig() {
        loadConfig();
    }
    
    /**
     * Retourne l'URL JDBC complète
     */
    public static String getJdbcUrl() {
        String host = properties.getProperty("db.host", "localhost");
        String port = properties.getProperty("db.port", "3306");
        return "jdbc:mysql://" + host + ":" + port + "/";
    }
    
    /**
     * Retourne l'URL JDBC complète avec la base de données
     */
    public static String getJdbcUrlWithDatabase() {
        return getJdbcUrl() + getDatabaseName();
    }
    
    public static String getHost() {
        return properties.getProperty("db.host", "localhost");
    }
    
    public static String getPort() {
        return properties.getProperty("db.port", "3306");
    }
    
    public static String getDatabaseName() {
        return properties.getProperty("db.name", "CabinetMedicalbis");
    }
    
    public static String getUsername() {
        return properties.getProperty("db.username", "root");
    }
    
    public static String getPassword() {
        return properties.getProperty("db.password", "");
    }
    
    /**
     * Teste la connexion avec les paramètres actuels
     */
    public static boolean testConnection() {
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                getJdbcUrl(), getUsername(), getPassword()
            );
            conn.close();
            return true;
        } catch (Exception e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Affiche un message d'erreur
     */
    private static void showError(String title, String message) {
        try {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } catch (Exception e) {
            // Si JavaFX n'est pas initialisé, afficher dans la console
            System.err.println(title + ": " + message);
        }
    }
}
