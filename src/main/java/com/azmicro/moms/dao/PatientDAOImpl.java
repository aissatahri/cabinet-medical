/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.Sexe;
import com.azmicro.moms.model.SituationFamiliale; // Importer l'énumération
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    private Connection connection;

    public PatientDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
public boolean save(Patient patient) {
    String query = "INSERT INTO Patients (numDossier, Nom, Prenom, DateNaissance, age, Sexe, SituationFamiliale, Telephone, Email, Adresse, Profession, CouvertureSanitaire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, patient.getNumDossier());
        statement.setString(2, patient.getNom());
        statement.setString(3, patient.getPrenom());
        statement.setDate(4, Date.valueOf(patient.getDateNaissance()));
        statement.setInt(5, patient.getAge());
        statement.setString(6, patient.getSexe().name());
        statement.setString(7, patient.getSituationFamiliale().name());
        statement.setString(8, patient.getTelephone());
        statement.setString(9, patient.getEmail());
        statement.setString(10, patient.getAdresse());
        statement.setString(11, patient.getProfession());
        statement.setString(12, patient.getCouvertureSanitaire());
        statement.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    @Override
    public Patient findById(int patientID) {
        String query = "SELECT * FROM Patients WHERE PatientID = ?";
        Patient patient = null;
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patient = new Patient();
                patient.setPatientID(resultSet.getInt("PatientID"));
                patient.setNom(resultSet.getString("Nom"));
                patient.setPrenom(resultSet.getString("Prenom"));
                patient.setNumDossier(resultSet.getString("numDossier"));
                patient.setDateNaissance(resultSet.getDate("DateNaissance").toLocalDate());
                patient.setAge(resultSet.getInt("age"));
                patient.setSexe(Sexe.valueOf(resultSet.getString("Sexe")));
                patient.setSituationFamiliale(SituationFamiliale.valueOf(resultSet.getString("SituationFamiliale")));
                patient.setTelephone(resultSet.getString("Telephone"));
                patient.setEmail(resultSet.getString("Email"));
                patient.setAdresse(resultSet.getString("Adresse"));
                patient.setProfession(resultSet.getString("Profession"));
                patient.setCouvertureSanitaire(resultSet.getString("CouvertureSanitaire"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    public List<Patient> findAll() {
        String query = "SELECT * FROM Patients";
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setPatientID(resultSet.getInt("PatientID"));
                patient.setNom(resultSet.getString("Nom"));
                patient.setPrenom(resultSet.getString("Prenom"));
                patient.setNumDossier(resultSet.getString("numDossier"));
                patient.setDateNaissance(resultSet.getDate("DateNaissance").toLocalDate());
                patient.setAge(resultSet.getInt("age"));
                patient.setSexe(Sexe.valueOf(resultSet.getString("Sexe")));
                patient.setSituationFamiliale(SituationFamiliale.valueOf(resultSet.getString("SituationFamiliale")));
                patient.setTelephone(resultSet.getString("Telephone"));
                patient.setEmail(resultSet.getString("Email"));
                patient.setAdresse(resultSet.getString("Adresse"));
                patient.setProfession(resultSet.getString("Profession"));
                patient.setCouvertureSanitaire(resultSet.getString("CouvertureSanitaire"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    @Override
    public boolean update(Patient patient) {
        String query = "UPDATE Patients SET numDossier = ?, Nom = ?, Prenom = ?, DateNaissance = ?, age = ?, Sexe = ?, SituationFamiliale = ?, Telephone = ?, Email = ?, Adresse = ?, Profession = ?, CouvertureSanitaire = ? WHERE PatientID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patient.getNumDossier());
            statement.setString(2, patient.getNom());
            statement.setString(3, patient.getPrenom());
            statement.setDate(4, Date.valueOf(patient.getDateNaissance()));
            statement.setInt(5, patient.getAge());
            statement.setString(6, patient.getSexe().name());
            statement.setString(7, patient.getSituationFamiliale().toString());
            statement.setString(8, patient.getTelephone());
            statement.setString(9, patient.getEmail());
            statement.setString(10, patient.getAdresse());
            statement.setString(11, patient.getProfession());
            statement.setString(12, patient.getCouvertureSanitaire());
            statement.setInt(13, patient.getPatientID());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int patientID) {
        String query = "DELETE FROM Patients WHERE PatientID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, patientID);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getLastPatientId() {
        String query = "SELECT MAX(PatientID) FROM Patients";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les exceptions correctement dans votre application
        }
        return -1; // Retourne -1 en cas d'échec
    }

    @Override
    public Patient findByNumDossier(String numDossier) {
        String query = "SELECT * FROM Patients WHERE numDossier = ?";
        Patient patient = null;
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, numDossier);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patient = new Patient();
                patient.setPatientID(resultSet.getInt("PatientID"));
                patient.setNom(resultSet.getString("Nom"));
                patient.setPrenom(resultSet.getString("Prenom"));
                patient.setNumDossier(resultSet.getString("numDossier"));
                patient.setDateNaissance(resultSet.getDate("DateNaissance").toLocalDate());
                patient.setAge(resultSet.getInt("age"));
                patient.setSexe(Sexe.valueOf(resultSet.getString("Sexe")));
                patient.setSituationFamiliale(SituationFamiliale.valueOf(resultSet.getString("SituationFamiliale").toUpperCase()));
                patient.setTelephone(resultSet.getString("Telephone"));
                patient.setEmail(resultSet.getString("Email"));
                patient.setAdresse(resultSet.getString("Adresse"));
                patient.setProfession(resultSet.getString("Profession"));
                patient.setCouvertureSanitaire(resultSet.getString("CouvertureSanitaire"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }
}
