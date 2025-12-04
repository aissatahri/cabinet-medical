/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.ConsultationDetails;
import com.azmicro.moms.model.ModePaiement;
import com.azmicro.moms.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDetailsDAOImpl implements ConsultationDetailsDAO {

     private static final String QUERY = "SELECT DISTINCT "
            + "p.Nom AS NomPatient, "
            + "p.Prenom AS PrenomPatient, "
            + "c.DateConsultation, "
            + "c.ConsultationID AS ConsultationID, "
            + "f.Statut AS Statut, "
            + "pa.PaiementID AS PaiementID, "
            + "pa.Montant, "
            + "pa.reste AS MontantReste, "
            + "pa.versment AS MontantVersement, "
            + "pa.ModePaiement AS ModePaiement, "
            + "pa.etatPayment AS EtatPaiement "
            + "FROM patients p "
            + "JOIN consultations c ON p.PatientID = c.idPatient "
            + "LEFT JOIN paiements pa ON c.ConsultationID = pa.ConsultationID "
            + "JOIN filesattente f ON f.PatientID = p.PatientID AND DATE(f.DateArrivee) = DATE(c.DateConsultation) "
            + "WHERE f.Statut = 'TERMINE'";

    
    @Override
    public List<ConsultationDetails> getConsultationDetailsForToday() {
        String todayQuery = QUERY + " AND f.DateArrivee = CURDATE()"; // Assurez-vous que CURDATE() est appliqué ici aussi
        return getConsultationDetails(todayQuery);
    }

    @Override
    public List<ConsultationDetails> getConsultationDetailsForLastWeek() {
        String lastWeekQuery = QUERY + " AND c.DateConsultation >= DATE_SUB(CURDATE(), INTERVAL 1 WEEK)";
        return getConsultationDetails(lastWeekQuery);
    }

    @Override
    public List<ConsultationDetails> getConsultationDetailsForLastMonth() {
        String lastMonthQuery = QUERY + " AND c.DateConsultation >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
        return getConsultationDetails(lastMonthQuery);
    }

    @Override
    public List<ConsultationDetails> getAllConsultationDetails() {
        String allPaymentsQuery = QUERY; // Utiliser la requête de base sans condition de temps
        return getConsultationDetails(allPaymentsQuery);
    }

    private List<ConsultationDetails> getConsultationDetails(String query) {
        List<ConsultationDetails> detailsList = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ConsultationDetails details = new ConsultationDetails();
                details.setNomPatient(rs.getString("NomPatient"));
                details.setPrenomPatient(rs.getString("PrenomPatient"));
                details.setDateConsultation(rs.getDate("DateConsultation").toLocalDate());
                details.setConsultationID(rs.getInt("ConsultationID"));
                details.setStatut(rs.getString("Statut"));
                details.setIdPaiement(rs.getInt("PaiementID"));
                details.setMontant(rs.getDouble("Montant"));
                details.setMontantReste(rs.getDouble("MontantReste"));
                details.setMontantVersement(rs.getDouble("MontantVersement"));

                String modePaiementString = rs.getString("ModePaiement");
                if (modePaiementString != null) {
                    details.setModePaiement(ModePaiement.valueOf(modePaiementString));
                } else {
                    details.setModePaiement(null);
                }

                details.setEtatPaiement(rs.getBoolean("EtatPaiement"));
                detailsList.add(details);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detailsList;
    }

}
