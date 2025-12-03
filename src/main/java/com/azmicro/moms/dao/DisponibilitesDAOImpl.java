/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.Jours;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DisponibilitesDAOImpl implements DisponibilitesDAO {

    @Override
    public boolean save(Disponibilites disponibilite) {
        String query = "INSERT INTO disponibilites (MedecinID, Jours, HeureDebut, HeureFin) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, disponibilite.getMedecin().getMedecinID());
            statement.setString(2, disponibilite.getJours().name());
            statement.setTime(3, Time.valueOf(disponibilite.getHeureDebut()));
            statement.setTime(4, Time.valueOf(disponibilite.getHeureFin()));

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Retourne true si l'insertion a réussi

        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
            return false; // Retourne false en cas d'échec
        }
    }

    @Override
    public Disponibilites findById(int disponibiliteID) {
        Disponibilites disponibilite = null;
        String query = "SELECT * FROM disponibilites WHERE DisponibiliteID = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, disponibiliteID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                disponibilite = mapResultSetToDisponibilites(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disponibilite;
    }

    @Override
    public List<Disponibilites> findAll() {
        List<Disponibilites> disponibilitesList = new ArrayList<>();
        String query = "SELECT * FROM disponibilites";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Disponibilites disponibilite = mapResultSetToDisponibilites(resultSet);
                disponibilitesList.add(disponibilite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disponibilitesList;
    }

    @Override
    public boolean update(Disponibilites disponibilite) {
        String query = "UPDATE disponibilites SET MedecinID = ?, Jours = ?, HeureDebut = ?, HeureFin = ? WHERE DisponibiliteID = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, disponibilite.getMedecin().getMedecinID());
            statement.setString(2, disponibilite.getJours().name());
            statement.setTime(3, Time.valueOf(disponibilite.getHeureDebut()));
            statement.setTime(4, Time.valueOf(disponibilite.getHeureFin()));
            statement.setInt(5, disponibilite.getDisponibiliteID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Retourne true si la mise à jour a réussi

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false en cas d'échec
        }
    }

    @Override
    public boolean delete(int disponibiliteID) {
        String query = "DELETE FROM disponibilites WHERE DisponibiliteID = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, disponibiliteID);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Retourne true si la suppression a réussi

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false en cas d'échec
        }
    }

    public List<Disponibilites> findAllByMedecin(Medecin medecin) {
        List<Disponibilites> disponibilitesList = new ArrayList<>();
        String query = "SELECT * FROM disponibilites WHERE MedecinID = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, medecin.getMedecinID());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Disponibilites disponibilite = mapResultSetToDisponibilites(resultSet);
                disponibilitesList.add(disponibilite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disponibilitesList;
    }

    private Disponibilites mapResultSetToDisponibilites(ResultSet resultSet) throws SQLException {
        Disponibilites disponibilite = new Disponibilites();

        disponibilite.setDisponibiliteID(resultSet.getInt("DisponibiliteID"));

        // Récupération du MedecinID depuis le ResultSet
        int medecinID = resultSet.getInt("MedecinID");

        // Réutilisation d'une instance de MedecinDAO
        Medecin medecin = null;
        try {
            MedecinDAO medecinDAO = new MedecinDAOImpl(); // Idéalement, injecter cette instance pour éviter une création répétée
            medecin = medecinDAO.readMedecin(medecinID);
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception as needed
        }

        disponibilite.setMedecin(medecin);
        disponibilite.setJours(Jours.valueOf(resultSet.getString("Jours")));
        disponibilite.setHeureDebut(resultSet.getTime("HeureDebut").toLocalTime());
        disponibilite.setHeureFin(resultSet.getTime("HeureFin").toLocalTime());

        return disponibilite;
    }

    @Override
    public Disponibilites findByMedecinAndJour(Medecin medecin, Jours dayOfWeek) {
        Disponibilites disponibilite = null;
        String query = "SELECT * FROM disponibilites WHERE MedecinID = ? AND Jours = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, medecin.getMedecinID());
            statement.setString(2, Jours.valueOf(dayOfWeek.name()).name());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                disponibilite = mapResultSetToDisponibilites(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ideally log this instead of printStackTrace
        }

        return disponibilite;
    }

}
