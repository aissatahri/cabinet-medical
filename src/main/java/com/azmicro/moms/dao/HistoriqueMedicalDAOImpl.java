/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.HistoriqueMedical;
import com.azmicro.moms.model.Type;
import java.util.Locale;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueMedicalDAOImpl implements HistoriqueMedicalDAO {

    private final PatientDAO patientDAO;

    // Constructeur acceptant une instance de PatientDAO
    public HistoriqueMedicalDAOImpl(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    @Override
    public void save(HistoriqueMedical historiqueMedical) {
        String query = "INSERT INTO HistoriqueMedical (PatientID, Type, Description, Date, Note) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, historiqueMedical.getPatient().getPatientID());
            statement.setString(2, historiqueMedical.getType().name());
            statement.setString(3, historiqueMedical.getDescription());
            statement.setDate(4, Date.valueOf(historiqueMedical.getDate()));
            statement.setString(5, historiqueMedical.getNote());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HistoriqueMedical findById(int historiqueID) {
        String query = "SELECT * FROM HistoriqueMedical WHERE HistoriqueID = ?";
        HistoriqueMedical historiqueMedical = null;
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, historiqueID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                historiqueMedical = new HistoriqueMedical();
                historiqueMedical.setHistoriqueID(resultSet.getInt("HistoriqueID"));
                historiqueMedical.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                // Normalise la casse pour éviter les valeurs incohérentes (ex: "ChIRURGICAL")
                String rawType = resultSet.getString("Type");
                historiqueMedical.setType(Type.valueOf(rawType.toUpperCase(Locale.ROOT)));
                historiqueMedical.setDescription(resultSet.getString("Description"));
                historiqueMedical.setDate(resultSet.getDate("Date").toLocalDate());
                historiqueMedical.setNote(resultSet.getString("Note"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiqueMedical;
    }

    @Override
    public List<HistoriqueMedical> findAll() {
        String query = "SELECT * FROM HistoriqueMedical";
        List<HistoriqueMedical> historiques = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                HistoriqueMedical historiqueMedical = new HistoriqueMedical();
                historiqueMedical.setHistoriqueID(resultSet.getInt("HistoriqueID"));
                historiqueMedical.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                historiqueMedical.setType(Type.valueOf(resultSet.getString("Type")));
                historiqueMedical.setDescription(resultSet.getString("Description"));
                historiqueMedical.setDate(resultSet.getDate("Date").toLocalDate());
                historiqueMedical.setNote(resultSet.getString("Note"));
                historiques.add(historiqueMedical);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiques;
    }

    @Override
    public List<HistoriqueMedical> findAllByPatientId(int patientId) {
        String query = "SELECT * FROM HistoriqueMedical WHERE PatientID = ?";
        List<HistoriqueMedical> historiques = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, patientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    HistoriqueMedical historiqueMedical = new HistoriqueMedical();
                    historiqueMedical.setHistoriqueID(resultSet.getInt("HistoriqueID"));
                    historiqueMedical.setPatient(patientDAO.findById(resultSet.getInt("PatientID")));
                    String rawType = resultSet.getString("Type");
                    historiqueMedical.setType(Type.valueOf(rawType.toUpperCase(Locale.ROOT)));
                    historiqueMedical.setDescription(resultSet.getString("Description"));
                    historiqueMedical.setDate(resultSet.getDate("Date").toLocalDate());
                    historiqueMedical.setNote(resultSet.getString("Note"));
                    historiques.add(historiqueMedical);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiques;
    }

    @Override
    public void update(HistoriqueMedical historiqueMedical) {
        String query = "UPDATE HistoriqueMedical SET PatientID = ?, Type = ?, Description = ?, Date = ?, Note = ? WHERE HistoriqueID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, historiqueMedical.getPatient().getPatientID());
            statement.setString(2, historiqueMedical.getType().name());
            statement.setString(3, historiqueMedical.getDescription());
            statement.setDate(4, Date.valueOf(historiqueMedical.getDate()));
            statement.setString(5, historiqueMedical.getNote());
            statement.setInt(6, historiqueMedical.getHistoriqueID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(HistoriqueMedical historique) {
        String query = "DELETE FROM HistoriqueMedical WHERE HistoriqueID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, historique.getHistoriqueID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
