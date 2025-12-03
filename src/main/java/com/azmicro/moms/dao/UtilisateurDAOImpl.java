/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Role;
import com.azmicro.moms.model.Specialites;
import com.azmicro.moms.model.Utilisateur;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl implements UtilisateurDAO {

    @Override
    public void save(Utilisateur utilisateur) {
        String query = "INSERT INTO Utilisateurs (NomUtilisateur, MotDePasse, Role, DateCreation, MedecinID) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, utilisateur.getNomUtilisateur());
            statement.setString(2, utilisateur.getMotDePasse());
            statement.setString(3, utilisateur.getRole().name()); // Convert Enum to String
            statement.setDate(4, Date.valueOf(utilisateur.getDateCreation()));
            if (utilisateur.getRole() == Role.MEDECIN && utilisateur.getMedecin() != null) {
                statement.setInt(5, utilisateur.getMedecin().getMedecinID());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this exception
        }
    }

    @Override
    public Utilisateur findById(int id) {
        String query = "SELECT * FROM Utilisateurs LEFT JOIN Medecins ON Utilisateurs.MedecinID = Medecins.MedecinID LEFT JOIN Specialites ON Medecins.SpecialiteID = Specialites.SpecialiteID WHERE UtilisateurID = ?";
        Utilisateur utilisateur = null;

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    utilisateur = new Utilisateur(
                            resultSet.getInt("UtilisateurID"),
                            resultSet.getString("NomUtilisateur"),
                            resultSet.getString("MotDePasse"),
                            Role.valueOf(resultSet.getString("Role")),
                            resultSet.getDate("DateCreation").toLocalDate()
                    );

                    if (utilisateur.getRole() == Role.MEDECIN) {
                        Specialites specialite = new Specialites(
                                resultSet.getInt("SpecialiteID"),
                                resultSet.getString("NomSpecialite") // Assurez-vous que cette colonne est dans le résultat
                        );

                        Medecin medecin = new Medecin(
                                resultSet.getInt("MedecinID"),
                                resultSet.getString("Nom"),
                                resultSet.getString("Prenom"),
                                specialite, // Utilisez l'objet Specialite ici
                                resultSet.getString("Telephone"),
                                resultSet.getString("Email"),
                                resultSet.getString("Adresse"),
                                resultSet.getBytes("logo")
                        );
                        utilisateur.setMedecin(medecin);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this exception
        }

        return utilisateur;
    }

    @Override
    public List<Utilisateur> findAll() {
        String query = "SELECT * FROM Utilisateurs LEFT JOIN Medecins ON Utilisateurs.MedecinID = Medecins.MedecinID LEFT JOIN Specialites ON Medecins.SpecialiteID = Specialites.SpecialiteID";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        resultSet.getInt("UtilisateurID"),
                        resultSet.getString("NomUtilisateur"),
                        resultSet.getString("MotDePasse"),
                        Role.valueOf(resultSet.getString("Role")),
                        resultSet.getDate("DateCreation").toLocalDate()
                );

                if (utilisateur.getRole() == Role.MEDECIN) {
                    Specialites specialite = new Specialites(
                            resultSet.getInt("SpecialiteID"),
                            resultSet.getString("NomSpecialite") // Assurez-vous que cette colonne est dans le résultat
                    );

                    Medecin medecin = new Medecin(
                            resultSet.getInt("MedecinID"),
                            resultSet.getString("Nom"),
                            resultSet.getString("Prenom"),
                            specialite, // Utilisez l'objet Specialite ici
                            resultSet.getString("Telephone"),
                            resultSet.getString("Email"),
                            resultSet.getString("Adresse"),
                            resultSet.getBytes("logo")
                    );
                    utilisateur.setMedecin(medecin);
                }

                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this exception
        }

        return utilisateurs;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String query = "UPDATE Utilisateurs SET NomUtilisateur = ?, MotDePasse = ?, Role = ?, DateCreation = ?, MedecinID = ? WHERE UtilisateurID = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, utilisateur.getNomUtilisateur());
            statement.setString(2, utilisateur.getMotDePasse());
            statement.setString(3, utilisateur.getRole().name()); // Convert Enum to String
            statement.setDate(4, Date.valueOf(utilisateur.getDateCreation()));
            if (utilisateur.getRole() == Role.MEDECIN && utilisateur.getMedecin() != null) {
                statement.setInt(5, utilisateur.getMedecin().getMedecinID());
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            statement.setInt(6, utilisateur.getUtilisateurID());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this exception
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Utilisateurs WHERE UtilisateurID = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this exception
        }
    }

    @Override
    public Utilisateur findByUsername(String username) {
        String query = "SELECT * FROM Utilisateurs "
                + "LEFT JOIN Medecins ON Utilisateurs.MedecinID = Medecins.MedecinID "
                + "LEFT JOIN Specialites ON Medecins.SpecialiteID = Specialites.SpecialiteID "
                + "WHERE NomUtilisateur = ?";
        Utilisateur utilisateur = null;

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    utilisateur = new Utilisateur(
                            resultSet.getInt("UtilisateurID"),
                            resultSet.getString("NomUtilisateur"),
                            resultSet.getString("MotDePasse"),
                            Role.valueOf(resultSet.getString("Role")), // Convert String to Enum
                            resultSet.getDate("DateCreation").toLocalDate()
                    );

                    if (utilisateur.getRole() == Role.MEDECIN) {
                        Specialites specialite = new Specialites(
                                resultSet.getInt("SpecialiteID"),
                                resultSet.getString("NomSpecialite") // Assurez-vous que cette colonne est dans le résultat
                        );

                        Medecin medecin = new Medecin(
                                resultSet.getInt("MedecinID"),
                                resultSet.getString("Nom"),
                                resultSet.getString("Prenom"),
                                specialite, // Utilisez l'objet Specialite ici
                                resultSet.getString("Telephone"),
                                resultSet.getString("Email"),
                                resultSet.getString("Adresse"),
                                resultSet.getBytes("logo")
                        );
                        utilisateur.setMedecin(medecin);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this exception
        }

        return utilisateur;
    }

}
