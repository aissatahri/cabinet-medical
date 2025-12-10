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
import com.azmicro.moms.model.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAOImpl implements ConsultationDAO {

    private Connection connection;
    private RendezVousDAO rendezVousDAO;
    private PatientDAO patientDAO;

    private String getStringSafe(ResultSet rs, String column) {
        try {
            return rs.getString(column);
        } catch (SQLException e) {
            return null;
        }
    }

    public ConsultationDAOImpl(Connection connection) {
        this.connection = connection;
        this.rendezVousDAO = new RendezVousDAOImpl();
        this.patientDAO = new PatientDAOImpl(connection);
    }

    @Override
    public boolean save(Consultations consultation) {
        String sql = "INSERT INTO consultations (RendezVousID, DateConsultation, symptome, diagnostique, cat, poids, taille, imc, frequencequardiaque, pression, pression_droite, frequencerespiratoire, glycimie, temperature, saO, idPatient, examenClinique, ecg, ett) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Remplir les paramètres
            stmt.setObject(1, consultation.getRendezVous() != null ? consultation.getRendezVous().getRendezVousID() : null);
            stmt.setObject(2, consultation.getDateConsultation());
            stmt.setString(3, consultation.getSymptome());
            stmt.setString(4, consultation.getDiagnostique());
            stmt.setString(5, consultation.getCat());
            stmt.setDouble(6, consultation.getPoids());
            stmt.setDouble(7, consultation.getTaille());
            stmt.setDouble(8, consultation.getImc());
            stmt.setInt(9, consultation.getFrequencequardiaque());
            stmt.setString(10, consultation.getPression());
            System.out.println("DEBUG DAO save: pression = " + consultation.getPression());
            System.out.println("DEBUG DAO save: pression_droite = " + consultation.getPressionDroite());
            stmt.setString(11, consultation.getPressionDroite());
            stmt.setInt(12, consultation.getFrequencerespiratoire());
            stmt.setDouble(13, consultation.getGlycimie());
            stmt.setDouble(14, consultation.getTemperature());
            stmt.setDouble(15, consultation.getSaO());
            stmt.setInt(16, consultation.getPatient().getPatientID()); // Associer le patient
            stmt.setString(17, consultation.getExamenClinique()); // Ajouter examenClinique
            stmt.setString(18, consultation.getEcg()); // Ajouter ecg
            stmt.setString(19, consultation.getEtt()); // Ajouter ett

            int rowsAffected = stmt.executeUpdate();

            // Récupérer l'ID généré automatiquement
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        consultation.setConsultationID(generatedKeys.getInt(1)); // Mettre à jour l'ID de la consultation
                    } else {
                        return false; // Échec de la récupération de l'ID
                    }
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour mettre à jour une consultation
    @Override
    public boolean update(Consultations consultation) {
        String sql = "UPDATE consultations SET RendezVousID = ?, DateConsultation = ?, symptome = ?, diagnostique = ?, cat = ?, poids = ?, taille = ?, imc = ?, frequencequardiaque = ?, pression = ?, pression_droite = ?, frequencerespiratoire = ?, glycimie = ?, temperature = ?, saO = ?, idPatient = ?, examenClinique = ?, ecg = ?, ett = ? WHERE ConsultationID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.println("DEBUG DAO update: ConsultationID = " + consultation.getConsultationID());
            System.out.println("DEBUG DAO update: pression (TA gauche) = " + consultation.getPression());
            System.out.println("DEBUG DAO update: pression_droite (TA droit) = " + consultation.getPressionDroite());
            
            stmt.setObject(1, consultation.getRendezVous() != null ? consultation.getRendezVous().getRendezVousID() : null);
            stmt.setObject(2, consultation.getDateConsultation());
            stmt.setString(3, consultation.getSymptome());
            stmt.setString(4, consultation.getDiagnostique());
            stmt.setString(5, consultation.getCat());
            stmt.setDouble(6, consultation.getPoids());
            stmt.setDouble(7, consultation.getTaille());
            stmt.setDouble(8, consultation.getImc());
            stmt.setInt(9, consultation.getFrequencequardiaque());
            stmt.setString(10, consultation.getPression());
            stmt.setString(11, consultation.getPressionDroite());
            stmt.setInt(12, consultation.getFrequencerespiratoire());
            stmt.setDouble(13, consultation.getGlycimie());
            stmt.setDouble(14, consultation.getTemperature());
            stmt.setDouble(15, consultation.getSaO());
            stmt.setInt(16, consultation.getPatient().getPatientID()); // Associer le patient
            stmt.setString(17, consultation.getExamenClinique()); // Ajouter examenClinique
            stmt.setString(18, consultation.getEcg()); // Ajouter ecg
            stmt.setString(19, consultation.getEtt()); // Ajouter ett
            stmt.setInt(20, consultation.getConsultationID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour trouver une consultation par son ID
    @Override
    public Consultations findById(int id) {
        String sql = "SELECT * FROM consultations WHERE ConsultationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Consultations consultation = new Consultations();
                    consultation.setConsultationID(rs.getInt("ConsultationID"));
                    consultation.setRendezVous(rs.getObject("RendezVousID") != null ? rendezVousDAO.findById(rs.getInt("RendezVousID")) : null);
                    consultation.setPatient(patientDAO.findById(rs.getInt("idPatient")));
                    java.sql.Date dateConsultation = rs.getDate("DateConsultation");
                    consultation.setDateConsultation(dateConsultation != null ? dateConsultation.toLocalDate() : null);
                    consultation.setSymptome(rs.getString("symptome"));
                    consultation.setDiagnostique(rs.getString("diagnostique"));
                    consultation.setCat(rs.getString("cat"));
                    consultation.setPoids(rs.getDouble("poids"));
                    consultation.setTaille(rs.getDouble("taille"));
                    consultation.setImc(rs.getDouble("imc"));
                    consultation.setFrequencequardiaque(rs.getInt("frequencequardiaque"));
                    consultation.setPression(rs.getString("pression"));
                    consultation.setPressionDroite(getStringSafe(rs, "pression_droite"));
                    consultation.setFrequencerespiratoire(rs.getInt("frequencerespiratoire"));
                    consultation.setGlycimie(rs.getDouble("glycimie"));
                    consultation.setTemperature(rs.getDouble("temperature"));
                    consultation.setSaO(rs.getInt("saO"));
                    consultation.setExamenClinique(rs.getString("examenClinique")); // Ajouter examenClinique
                    consultation.setEcg(rs.getString("ecg")); // Ajouter ecg
                    consultation.setEtt(rs.getString("ett")); // Ajouter ett
                    return consultation;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode pour trouver toutes les consultations
    @Override
    public List<Consultations> findAll() {
        List<Consultations> consultations = new ArrayList<>();
        String sql = "SELECT * FROM consultations";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Consultations consultation = new Consultations();
                consultation.setConsultationID(rs.getInt("ConsultationID"));
                consultation.setRendezVous(rs.getObject("RendezVousID") != null ? rendezVousDAO.findById(rs.getInt("RendezVousID")) : null);
                consultation.setPatient(patientDAO.findById(rs.getInt("idPatient")));
                java.sql.Date dateConsultation = rs.getDate("DateConsultation");
                consultation.setDateConsultation(dateConsultation != null ? dateConsultation.toLocalDate() : null);
                consultation.setSymptome(rs.getString("symptome"));
                consultation.setDiagnostique(rs.getString("diagnostique"));
                consultation.setCat(rs.getString("cat"));
                consultation.setPoids(rs.getDouble("poids"));
                consultation.setTaille(rs.getDouble("taille"));
                consultation.setImc(rs.getDouble("imc"));
                consultation.setFrequencequardiaque(rs.getInt("frequencequardiaque"));
                consultation.setPression(rs.getString("pression"));
                consultation.setPressionDroite(getStringSafe(rs, "pression_droite"));
                consultation.setFrequencerespiratoire(rs.getInt("frequencerespiratoire"));
                consultation.setGlycimie(rs.getDouble("glycimie"));
                consultation.setTemperature(rs.getDouble("temperature"));
                consultation.setSaO(rs.getInt("saO"));
                consultation.setExamenClinique(rs.getString("examenClinique")); // Ajouter examenClinique
                consultation.setEcg(rs.getString("ecg")); // Ajouter ecg
                consultation.setEtt(rs.getString("ett")); // Ajouter ett
                consultations.add(consultation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    // Méthode pour supprimer une consultation
    public boolean delete(int id) {
        String sql = "DELETE FROM consultations WHERE ConsultationID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Consultations> findAllByIdPatient(int patientId) {
        List<Consultations> consultations = new ArrayList<>();
        String sql = "SELECT * FROM consultations WHERE idPatient = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Consultations consultation = new Consultations();
                    consultation.setConsultationID(rs.getInt("ConsultationID"));
                    consultation.setRendezVous(rs.getObject("RendezVousID") != null ? rendezVousDAO.findById(rs.getInt("RendezVousID")) : null);
                    consultation.setPatient(patientDAO.findById(rs.getInt("idPatient")));
                    java.sql.Date dateConsultation = rs.getDate("DateConsultation");
                    consultation.setDateConsultation(dateConsultation != null ? dateConsultation.toLocalDate() : null);
                    consultation.setSymptome(rs.getString("symptome"));
                    consultation.setDiagnostique(rs.getString("diagnostique"));
                    consultation.setCat(rs.getString("cat"));
                    consultation.setPoids(rs.getDouble("poids"));
                    consultation.setTaille(rs.getDouble("taille"));
                    consultation.setImc(rs.getDouble("imc"));
                    consultation.setFrequencequardiaque(rs.getInt("frequencequardiaque"));
                    consultation.setPression(rs.getString("pression"));
                    consultation.setPressionDroite(getStringSafe(rs, "pression_droite"));
                    consultation.setFrequencerespiratoire(rs.getInt("frequencerespiratoire"));
                    consultation.setGlycimie(rs.getDouble("glycimie"));
                    consultation.setTemperature(rs.getDouble("temperature"));
                    consultation.setSaO(rs.getInt("saO"));
                    consultation.setExamenClinique(rs.getString("examenClinique")); // Ajouter examenClinique
                    consultation.setEcg(rs.getString("ecg")); // Ajouter ecg
                    consultation.setEtt(rs.getString("ett")); // Ajouter ett
                    consultations.add(consultation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

}
