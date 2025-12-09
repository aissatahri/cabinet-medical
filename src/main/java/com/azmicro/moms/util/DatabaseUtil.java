/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util;

/**
 *
 * @author Aissa
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    /**
     * Obtient une connexion à la base de données en utilisant la configuration
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DatabaseConfig.getJdbcUrlWithDatabase(), 
            DatabaseConfig.getUsername(), 
            DatabaseConfig.getPassword()
        );
    }

    /**
     * Obtient une connexion sans spécifier de base de données
     */
    public static Connection getConnectionWithoutDB() throws SQLException {
        return DriverManager.getConnection(
            DatabaseConfig.getJdbcUrl(), 
            DatabaseConfig.getUsername(), 
            DatabaseConfig.getPassword()
        );
    }

    /**
     * Vérifie si la base de données existe
     */
    public static boolean databaseExists() {
        try (Connection connection = getConnectionWithoutDB();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + DatabaseConfig.getDatabaseName() + "'")) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
