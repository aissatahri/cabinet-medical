/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.ModePaiement;
import com.azmicro.moms.model.Paiements;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaiementsDAOImpl implements PaiementsDAO {

    private static final Logger LOGGER = Logger.getLogger(PaiementsDAOImpl.class.getName());

    @Override
    public boolean save(Paiements paiement) {
        String query = paiement.getPaiementID() == 0
                ? "INSERT INTO paiements (ConsultationID, Montant, versment, reste, DatePaiement, ModePaiement, etatPayment) VALUES (?, ?, ?, ?, ?, ?, ?)"
                : "UPDATE paiements SET ConsultationID = ?, Montant = ?, versment = ?, reste = ?, DatePaiement = ?, ModePaiement = ?, etatPayment = ? WHERE PaiementID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, paiement.getConsultation().getConsultationID());
            statement.setDouble(2, paiement.getMontant());
            statement.setDouble(3, paiement.getVersment());
            statement.setDouble(4, paiement.getReste());
            statement.setDate(5, Date.valueOf(paiement.getDatePaiement()));
            statement.setString(6, paiement.getModePaiement().name()); // Use name() for consistency
            statement.setBoolean(7, paiement.isEtatPayment());

            if (paiement.getPaiementID() != 0) {
                statement.setInt(8, paiement.getPaiementID());
            }

            int rowsAffected = statement.executeUpdate();

            if (paiement.getPaiementID() == 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    paiement.setPaiementID(generatedKeys.getInt(1));
                }
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving paiement", e);
            return false;
        }
    }

    @Override
    public Paiements findById(int paiementID) {
        String query = "SELECT * FROM paiements WHERE PaiementID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, paiementID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Paiements paiement = new Paiements();
                paiement.setPaiementID(resultSet.getInt("PaiementID"));
                // Fetch and set the Consultation object if necessary
                // paiement.setConsultation(consultationService.findById(resultSet.getInt("ConsultationID")));
                paiement.setMontant(resultSet.getDouble("Montant"));
                paiement.setVersment(resultSet.getDouble("versment"));
                paiement.setReste(resultSet.getDouble("reste"));
                paiement.setDatePaiement(resultSet.getDate("DatePaiement").toLocalDate());
                paiement.setModePaiement(ModePaiement.valueOf(resultSet.getString("ModePaiement"))); // Use valueOf()
                paiement.setEtatPayment(resultSet.getBoolean("etatPayment"));
                return paiement;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding paiement by ID", e);
        }
        return null;
    }

    @Override
    public List<Paiements> findAll() {
        List<Paiements> paiementsList = new ArrayList<>();
        String query = "SELECT * FROM paiements";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Paiements paiement = new Paiements();
                paiement.setPaiementID(resultSet.getInt("PaiementID"));
                // Fetch and set the Consultation object if necessary
                // paiement.setConsultation(consultationService.findById(resultSet.getInt("ConsultationID")));
                paiement.setMontant(resultSet.getDouble("Montant"));
                paiement.setVersment(resultSet.getDouble("versment"));
                paiement.setReste(resultSet.getDouble("reste"));
                paiement.setDatePaiement(resultSet.getDate("DatePaiement").toLocalDate());
                paiement.setModePaiement(ModePaiement.valueOf(resultSet.getString("ModePaiement"))); // Use valueOf()
                paiement.setEtatPayment(resultSet.getBoolean("etatPayment"));
                paiementsList.add(paiement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding all paiements", e);
        }
        return paiementsList;
    }

    @Override
    public boolean delete(int paiementID) {
        String query = "DELETE FROM paiements WHERE PaiementID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, paiementID);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting paiement", e);
            return false;
        }
    }

    @Override
    public boolean update(Paiements paiement) {
        return save(paiement); // Use save method for updating to avoid redundancy
    }

    @Override
    public List<Paiements> getPaiementsByConsultation(Consultations consultation) {
        String query = "SELECT * FROM paiements WHERE ConsultationID = ?";
        List<Paiements> paiementsList = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, consultation.getConsultationID());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Paiements paiement = new Paiements();
                paiement.setPaiementID(resultSet.getInt("PaiementID"));
                paiement.setMontant(resultSet.getDouble("Montant"));
                paiement.setVersment(resultSet.getDouble("versment"));
                paiement.setReste(resultSet.getDouble("reste"));
                paiement.setDatePaiement(resultSet.getDate("DatePaiement").toLocalDate());
                paiement.setModePaiement(ModePaiement.valueOf(resultSet.getString("ModePaiement"))); // Use valueOf()
                paiement.setConsultation(consultation);  // Set the Consultation object here
                paiement.setEtatPayment(resultSet.getBoolean("etatPayment")); // Add etatPayment field

                paiementsList.add(paiement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving paiements by consultation", e);
        }

        return paiementsList;
    }

    @Override
    public List<Paiements> getPaiementsByConsultation(Patient patient) {
        List<Paiements> paiementsList = new ArrayList<>();

        try {
            ConsultationDAO consultationDAO = new ConsultationDAOImpl(DatabaseUtil.getConnection());

            // Récupérer toutes les consultations pour ce patient
            List<Consultations> consultations = consultationDAO.findAllByIdPatient(patient.getPatientID());

            // Pour chaque consultation, récupérer les paiements associés
            for (Consultations consultation : consultations) {
                paiementsList.addAll(getPaiementsByConsultation(consultation));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving paiements by patient", e);
        }

        return paiementsList;
    }

}
