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
import com.azmicro.moms.model.Specialites;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAOImpl implements MedecinDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseUtil.getConnection();
    }

    @Override
    public boolean createMedecin(Medecin medecin) {
        String sql = "INSERT INTO medecins (MedecinID, Nom, Prenom, SpecialiteID, Telephone, Email, Adresse, logo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medecin.getMedecinID());
            stmt.setString(2, medecin.getNom());
            stmt.setString(3, medecin.getPrenom());
            stmt.setInt(4, medecin.getSpecialite().getSpecialiteID());
            stmt.setString(5, medecin.getTelephone());
            stmt.setString(6, medecin.getEmail());
            stmt.setString(7, medecin.getAdresse());
            stmt.setBytes(8, medecin.getLogo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
public Medecin readMedecin(int medecinID) throws SQLException {
    String sql = "SELECT m.MedecinID, m.Nom, m.Prenom, s.SpecialiteID, s.NomSpecialite, m.Telephone, m.Email, m.Adresse, m.logo "
               + "FROM medecins m "
               + "LEFT JOIN specialites s ON m.SpecialiteID = s.SpecialiteID "
               + "WHERE m.MedecinID = ?";
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, medecinID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Specialites specialite = new Specialites();
            specialite.setSpecialiteID(rs.getInt("SpecialiteID"));
            specialite.setNomSpecialite(rs.getString("NomSpecialite"));
            return new Medecin(
                rs.getInt("MedecinID"),
                rs.getString("Nom"),
                rs.getString("Prenom"),
                specialite,
                rs.getString("Telephone"),
                rs.getString("Email"),
                rs.getString("Adresse"),
                rs.getBytes("logo")
            );
        }
    }
    return null;
}

    @Override
    public List<Medecin> readAllMedecins() throws SQLException {
    String query = "SELECT m.MedecinID, m.Nom, m.Prenom, s.nomSpecialite, m.Telephone, m.Email, m.Adresse, m.logo "
                 + "FROM medecins m "
                 + "LEFT JOIN specialites s ON m.SpecialiteID = s.specialiteID";
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        List<Medecin> medecins = new ArrayList<>();
        while (resultSet.next()) {
            Specialites specialite = new Specialites(); // Assuming you have a way to set this up
            specialite.setNomSpecialite(resultSet.getString("nomSpecialite"));

            Medecin medecin = new Medecin();
            medecin.setMedecinID(resultSet.getInt("MedecinID"));
            medecin.setNom(resultSet.getString("Nom"));
            medecin.setPrenom(resultSet.getString("Prenom"));
            medecin.setSpecialite(specialite);
            medecin.setTelephone(resultSet.getString("Telephone"));
            medecin.setEmail(resultSet.getString("Email"));
            medecin.setAdresse(resultSet.getString("Adresse"));
            medecin.setLogo(resultSet.getBytes("logo"));

            medecins.add(medecin);
        }
        return medecins;
    }
}

    @Override
    public boolean updateMedecin(Medecin medecin) {
        String sql = "UPDATE medecins SET Nom = ?, Prenom = ?, SpecialiteID = ?, Telephone = ?, Email = ?, Adresse = ?, logo = ? WHERE MedecinID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, medecin.getNom());
            stmt.setString(2, medecin.getPrenom());
            stmt.setInt(3, medecin.getSpecialite().getSpecialiteID());
            stmt.setString(4, medecin.getTelephone());
            stmt.setString(5, medecin.getEmail());
            stmt.setString(6, medecin.getAdresse());
            stmt.setBytes(7, medecin.getLogo());
            stmt.setInt(8, medecin.getMedecinID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMedecin(int medecinID) {
        String sql = "DELETE FROM medecins WHERE MedecinID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, medecinID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

