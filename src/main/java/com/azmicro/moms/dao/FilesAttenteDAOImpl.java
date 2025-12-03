/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.EtatArrive;
import com.azmicro.moms.model.FilesAttente;
import com.azmicro.moms.model.Statut;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FilesAttenteDAOImpl implements FilesAttenteDAO {

    private final PatientDAO patientDAO;

    // Constructeur acceptant une instance de PatientDAO
    public FilesAttenteDAOImpl(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }
@Override
public boolean save(FilesAttente filesAttente) {
    String query = "INSERT INTO FilesAttente (PatientID, DateArrivee, HeureArrivee, Etat, Statut) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, filesAttente.getPatient().getPatientID());
        statement.setDate(2, Date.valueOf(filesAttente.getDateArrivee()));
        statement.setTime(3, Time.valueOf(filesAttente.getHeureArrive()));
        statement.setString(4, filesAttente.getEtat().name());
        statement.setString(5, filesAttente.getStatut().name());
        statement.executeUpdate();
        return true; // Succès
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Échec
    }
}

    @Override
    public FilesAttente findById(int fileAttenteID) {
        String query = "SELECT * FROM FilesAttente WHERE FileAttenteID = ?";
        FilesAttente filesAttente = null;
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, fileAttenteID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                filesAttente = new FilesAttente();
                filesAttente.setFileAttenteID(resultSet.getInt("FileAttenteID"));
                filesAttente.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                filesAttente.setDateArrivee(resultSet.getDate("DateArrivee").toLocalDate());
                filesAttente.setHeureArrive(resultSet.getTime("HeureArrivee").toLocalTime());
                filesAttente.setEtat(EtatArrive.valueOf(resultSet.getString("Etat")));
                filesAttente.setStatut(Statut.valueOf(resultSet.getString("Statut")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filesAttente;
    }

    @Override
    public List<FilesAttente> findAll() {
        String query = "SELECT * FROM FilesAttente";
        List<FilesAttente> filesAttentes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                FilesAttente filesAttente = new FilesAttente();
                filesAttente.setFileAttenteID(resultSet.getInt("FileAttenteID"));
                filesAttente.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                filesAttente.setDateArrivee(resultSet.getDate("DateArrivee").toLocalDate());
                filesAttente.setHeureArrive(resultSet.getTime("HeureArrivee").toLocalTime());
                filesAttente.setEtat(EtatArrive.valueOf(resultSet.getString("Etat")));
                filesAttente.setStatut(Statut.valueOf(resultSet.getString("Statut")));
                filesAttentes.add(filesAttente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filesAttentes;
    }

    @Override
    public List<FilesAttente> findAllByPatientId(int patientId) {
        String query = "SELECT * FROM FilesAttente WHERE PatientID = ?";
        List<FilesAttente> filesAttentes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, patientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    FilesAttente filesAttente = new FilesAttente();
                    filesAttente.setFileAttenteID(resultSet.getInt("FileAttenteID"));
                    filesAttente.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                    filesAttente.setDateArrivee(resultSet.getDate("DateArrivee").toLocalDate());
                    filesAttente.setHeureArrive(resultSet.getTime("HeureArrivee").toLocalTime());
                    filesAttente.setEtat(EtatArrive.valueOf(resultSet.getString("Etat")));
                    filesAttente.setStatut(Statut.valueOf(resultSet.getString("Statut")));
                    filesAttentes.add(filesAttente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filesAttentes;
    }

    @Override
public boolean update(FilesAttente filesAttente) {
    String query = "UPDATE FilesAttente SET PatientID = ?, DateArrivee = ?, HeureArrivee = ?, Etat = ?, Statut = ? WHERE FileAttenteID = ?";
    try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, filesAttente.getPatient().getPatientID());
        statement.setDate(2, Date.valueOf(filesAttente.getDateArrivee()));
        statement.setTime(3, Time.valueOf(filesAttente.getHeureArrive()));
        statement.setString(4, filesAttente.getEtat().name());
        statement.setString(5, filesAttente.getStatut().name());
        statement.setInt(6, filesAttente.getFileAttenteID());
        statement.executeUpdate();
        return true; // Succès
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Échec
    }
}


    @Override
    public List<FilesAttente> findAll(LocalDate dateJour, LocalTime startTime, LocalTime endTime, String... statuses) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM FilesAttente WHERE DateArrivee = ? AND HeureArrivee BETWEEN ? AND ?");

        if (statuses.length > 0) {
            queryBuilder.append(" AND Statut IN (");
            for (int i = 0; i < statuses.length; i++) {
                if (i > 0) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append("?");
            }
            queryBuilder.append(")");
        }

        String query = queryBuilder.toString();

        List<FilesAttente> filesAttentes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(dateJour)); // Convertir LocalDate en java.sql.Date
            statement.setTime(2, java.sql.Time.valueOf(startTime)); // Convertir LocalTime en java.sql.Time
            statement.setTime(3, java.sql.Time.valueOf(endTime)); // Convertir LocalTime en java.sql.Time

            // Définir les paramètres pour les statuts
            for (int i = 0; i < statuses.length; i++) {
                statement.setString(4 + i, statuses[i]);
            }

            // Exécuter la requête et traiter les résultats
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    FilesAttente filesAttente = new FilesAttente();
                    filesAttente.setFileAttenteID(resultSet.getInt("FileAttenteID"));
                    filesAttente.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                    filesAttente.setDateArrivee(resultSet.getDate("DateArrivee").toLocalDate());
                    filesAttente.setHeureArrive(resultSet.getTime("HeureArrivee").toLocalTime());
                    filesAttente.setEtat(EtatArrive.valueOf(resultSet.getString("Etat")));
                    filesAttente.setStatut(Statut.valueOf(resultSet.getString("Statut")));
                    filesAttentes.add(filesAttente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filesAttentes;
    }

    @Override
public boolean delete(int fileAttenteID) {
    String query = "DELETE FROM FilesAttente WHERE FileAttenteID = ?";
    try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, fileAttenteID);
        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0; // Succès si au moins une ligne a été supprimée
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Échec
    }
}

    @Override
    public List<FilesAttente> findAll(LocalDate dateJour, LocalTime heureDebut, LocalTime heureFin, String status) {
        String query = "SELECT * FROM FilesAttente WHERE DateArrivee = ? AND HeureArrivee BETWEEN ? AND ? AND Statut = ?";

        List<FilesAttente> filesAttentes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(dateJour)); // Convertir LocalDate en java.sql.Date
            statement.setTime(2, java.sql.Time.valueOf(heureDebut)); // Convertir LocalTime en java.sql.Time
            statement.setTime(3, java.sql.Time.valueOf(heureFin)); // Convertir LocalTime en java.sql.Time
            statement.setString(4, status); // Définir le statut

            // Exécuter la requête et traiter les résultats
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    FilesAttente filesAttente = new FilesAttente();
                    filesAttente.setFileAttenteID(resultSet.getInt("FileAttenteID"));
                    filesAttente.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                    filesAttente.setDateArrivee(resultSet.getDate("DateArrivee").toLocalDate());
                    filesAttente.setHeureArrive(resultSet.getTime("HeureArrivee").toLocalTime());
                    filesAttente.setEtat(EtatArrive.valueOf(resultSet.getString("Etat")));
                    filesAttente.setStatut(Statut.valueOf(resultSet.getString("Statut")));
                    filesAttentes.add(filesAttente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filesAttentes;
    }

}
