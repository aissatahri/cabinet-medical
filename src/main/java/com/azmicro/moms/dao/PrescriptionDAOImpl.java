/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Medicaments;
import com.azmicro.moms.model.Prescriptions;
import com.azmicro.moms.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAOImpl implements PrescriptionDAO {

    @Override
    public boolean savePrescription(Prescriptions prescription) {
        String query = "INSERT INTO Prescriptions (consultationID, medicamentID, dose, duree, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, prescription.getConsultationID());
            preparedStatement.setInt(2, prescription.getMedicament().getMedicamentID());
            preparedStatement.setString(3, prescription.getDose());
            preparedStatement.setString(4, prescription.getDuree());
            preparedStatement.setString(5, prescription.getDescription()); // Ajouter la description

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePrescription(Prescriptions prescription) {
        String query = "UPDATE Prescriptions SET consultationID = ?, medicamentID = ?, dose = ?, duree = ?, description = ? WHERE prescriptionID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, prescription.getConsultationID());
            preparedStatement.setInt(2, prescription.getMedicament().getMedicamentID());
            preparedStatement.setString(3, prescription.getDose());
            preparedStatement.setString(4, prescription.getDuree());
            preparedStatement.setString(5, prescription.getDescription()); // Ajouter la description
            preparedStatement.setInt(6, prescription.getPrescriptionID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePrescription(int prescriptionID) {
        String query = "DELETE FROM Prescriptions WHERE prescriptionID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, prescriptionID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Prescriptions getPrescriptionById(int prescriptionID) {
        String query = "SELECT * FROM Prescriptions WHERE prescriptionID = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, prescriptionID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                MedicamentsDAO medicamentsDAO = new MedicamentsDAOImpl();
                Medicaments medicament = medicamentsDAO.findById(resultSet.getInt("MedicamentID"));
                return new Prescriptions(
                        resultSet.getInt("prescriptionID"),
                        resultSet.getInt("consultationID"),
                        medicament,
                        resultSet.getString("dose"),
                        resultSet.getString("duree"),
                        resultSet.getString("description") // Added description
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Prescriptions> getAllPrescriptions() {
        List<Prescriptions> prescriptionsList = new ArrayList<>();
        String query = "SELECT * FROM Prescriptions";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            MedicamentsDAO medicamentsDAO = new MedicamentsDAOImpl();
            while (resultSet.next()) {
                Medicaments medicament = medicamentsDAO.findById(resultSet.getInt("MedicamentID"));
                Prescriptions prescription = new Prescriptions(
                        resultSet.getInt("prescriptionID"),
                        resultSet.getInt("consultationID"),
                        medicament,
                        resultSet.getString("dose"),
                        resultSet.getString("duree"),
                        resultSet.getString("description") // Added description
                );
                prescriptionsList.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptionsList;
    }

    @Override
    public List<Prescriptions> getPrescriptionByConsultation(int consultationID) {
        List<Prescriptions> prescriptionsList = new ArrayList<>();
        String query = "SELECT * FROM prescriptions WHERE ConsultationID = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the consultationID parameter in the query
            preparedStatement.setInt(1, consultationID);

            // Execute the query and get the result set
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                MedicamentsDAO medicamentsDAO = new MedicamentsDAOImpl();

                // Process each row in the result set
                while (resultSet.next()) {
                    // Retrieve medicament details using medicamentID
                    Medicaments medicament = medicamentsDAO.findById(resultSet.getInt("MedicamentID"));
                    // Create a Prescriptions object from the result set data
                    Prescriptions prescription = new Prescriptions(
                            resultSet.getInt("PrescriptionID"),
                            resultSet.getInt("ConsultationID"),
                            medicament,
                            resultSet.getString("Dose"),
                            resultSet.getString("Duree"),
                            resultSet.getString("description") // Added description
                    );
                    // Add the prescription to the list
                    prescriptionsList.add(prescription);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions appropriately (e.g., log them or show an alert)
        }
        return prescriptionsList;
    }

}
