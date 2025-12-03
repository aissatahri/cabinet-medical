/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import java.sql.*;
import com.azmicro.moms.dao.RendezVousDAO;
import com.azmicro.moms.model.Medecin;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.model.RendezVous;
import com.azmicro.moms.util.DatabaseUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAOImpl implements RendezVousDAO {

    public RendezVousDAOImpl() {
    }

    @Override
    public boolean save(RendezVous rendezVous) {
        String query = "INSERT INTO rendezvous (PatientID, MedecinID, date, hourstart, hourend, titre, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rendezVous.getPatient().getPatientID());
            stmt.setInt(2, rendezVous.getMedecin().getMedecinID());
            stmt.setDate(3, Date.valueOf(rendezVous.getDate()));
            stmt.setTime(4, Time.valueOf(rendezVous.getHourStart()));
            stmt.setTime(5, Time.valueOf(rendezVous.getHourEnd()));
            stmt.setString(6, rendezVous.getTitre());
            stmt.setString(7, rendezVous.getDesc());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(RendezVous rendezVous) {
        String query = "UPDATE rendezvous SET PatientID = ?, MedecinID = ?, date = ?, hourstart = ?, hourend = ?, titre = ?, description = ? WHERE RendezVousID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rendezVous.getPatient().getPatientID());
            stmt.setInt(2, rendezVous.getMedecin().getMedecinID());
            stmt.setDate(3, Date.valueOf(rendezVous.getDate()));
            stmt.setTime(4, Time.valueOf(rendezVous.getHourStart()));
            stmt.setTime(5, Time.valueOf(rendezVous.getHourEnd()));
            stmt.setString(6, rendezVous.getTitre());
            stmt.setString(7, rendezVous.getDesc());
            stmt.setInt(8, rendezVous.getRendezVousID());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public RendezVous findById(int rendezVousID) {
        String query = "SELECT * FROM rendezvous WHERE RendezVousID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rendezVousID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRendezVous(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<RendezVous> findAll() {
        String query = "SELECT * FROM rendezvous";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> findByPatient(int patientID) {
        String query = "SELECT * FROM rendezvous WHERE PatientID = ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, patientID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> findByDate(LocalDate date) {
        String query = "SELECT * FROM rendezvous WHERE date = ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public boolean delete(int rendezVousID) {
        String query = "DELETE FROM rendezvous WHERE RendezVousID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, rendezVousID);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode utilitaire pour mapper un ResultSet en objet RendezVous
    private RendezVous mapResultSetToRendezVous(ResultSet rs) throws SQLException {
        int rendezVousID = rs.getInt("RendezVousID");

        // Récupérer l'ID du patient depuis le ResultSet
        int patientID = rs.getInt("PatientID");

        // Utiliser PatientDAOImpl pour récupérer le patient
        PatientDAO patientDAO = new PatientDAOImpl(DatabaseUtil.getConnection());
        Patient patient = patientDAO.findById(patientID);

        // Récupérer l'ID du médecin depuis le ResultSet
        int medecinID = rs.getInt("MedecinID");

        // Vous pouvez ajouter la logique pour récupérer l'objet Medecin si nécessaire
        Medecin medecin = new Medecin();
        medecin.setMedecinID(medecinID);

        // Récupérer les autres champs du rendez-vous
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime hourStart = rs.getTime("hourstart").toLocalTime();
        LocalTime hourEnd = rs.getTime("hourend").toLocalTime();
        String titre = rs.getString("titre");
        String description = rs.getString("description");

        // Retourner un nouvel objet RendezVous avec le patient et le médecin récupérés
        return new RendezVous(rendezVousID, patient, medecin, date, hourStart, hourEnd, titre, description);
    }

    @Override
    public List<RendezVous> findRendezVousByDate(LocalDate date) {
        String query = "SELECT * FROM rendezvous WHERE date = ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> findRendezVousByWeek(LocalDate startOfWeek) {
        // Calculer la fin de la semaine (dimanche de la même semaine)
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        String query = "SELECT * FROM rendezvous WHERE date BETWEEN ? AND ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startOfWeek));
            stmt.setDate(2, Date.valueOf(endOfWeek));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> findRendezVousByMonth(LocalDate firstDayOfMonth) {
        // Calculer le dernier jour du mois
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        String query = "SELECT * FROM rendezvous WHERE date BETWEEN ? AND ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(firstDayOfMonth));
            stmt.setDate(2, Date.valueOf(lastDayOfMonth));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> getRendezVousByMonth(int monthValue, int year) {
        // Calculer le premier et le dernier jour du mois spécifié
        LocalDate startOfMonth = LocalDate.of(year, monthValue, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String query = "SELECT * FROM rendezvous WHERE date BETWEEN ? AND ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startOfMonth));
            stmt.setDate(2, Date.valueOf(endOfMonth));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> getRendezVousByMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        String query = "SELECT * FROM rendezvous WHERE date BETWEEN ? AND ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(startOfMonth));
            stmt.setDate(2, Date.valueOf(endOfMonth));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

    @Override
    public List<RendezVous> findRendezVousFromToday() {
        String query = "SELECT * FROM rendezvous WHERE date >= ?";
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Utilisation de la date actuelle
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RendezVous rendezVous = mapResultSetToRendezVous(rs);
                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezVousList;
    }

}
