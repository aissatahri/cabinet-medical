/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Imagerie;
import com.azmicro.moms.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImagerieDAOImpl implements ImagerieDAO {

    private final TypeImagerieDAO typeImagerieDAO = new TypeImagerieDAOImpl();

    @Override
    public void saveImagerie(Imagerie imagerie) {
        String sql = "INSERT INTO radiographies (ConsultationID, idTypeImagerie, description, DateImagerie, Resultat) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imagerie.getConsultationID());
            stmt.setInt(2, imagerie.getTypeImagerie().getIdTypeImagerie());
            stmt.setString(3, imagerie.getDescription());
            stmt.setObject(4, imagerie.getDateImagerie());
            stmt.setString(5, imagerie.getResultat());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateImagerie(Imagerie imagerie) {
        String sql = "UPDATE radiographies SET ConsultationID = ?, idTypeImagerie = ?, description = ?, DateImagerie = ?, Resultat = ? WHERE imagerieID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imagerie.getConsultationID());
            stmt.setInt(2, imagerie.getTypeImagerie().getIdTypeImagerie());
            stmt.setString(3, imagerie.getDescription());
            stmt.setObject(4, imagerie.getDateImagerie());
            stmt.setString(5, imagerie.getResultat());
            stmt.setInt(6, imagerie.getImagerieID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteImagerie(int imagerieID) {
        String sql = "DELETE FROM radiographies WHERE imagerieID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imagerieID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Imagerie findById(int imagerieID) {
        String sql = "SELECT * FROM radiographies WHERE imagerieID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imagerieID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToImagerie(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Imagerie> findAll() {
        List<Imagerie> imageries = new ArrayList<>();
        String sql = "SELECT * FROM radiographies";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                imageries.add(mapResultSetToImagerie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imageries;
    }

    @Override
    public List<Imagerie> findByConsultationId(int consultationID) {
        List<Imagerie> imageries = new ArrayList<>();
        String sql = "SELECT * FROM radiographies WHERE ConsultationID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consultationID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                imageries.add(mapResultSetToImagerie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imageries;
    }

    @Override
    public List<Imagerie> searchByName(String searchTerm) {
        List<Imagerie> imageries = new ArrayList<>();
        String sql = "SELECT * FROM radiographies WHERE idTypeImagerie LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                imageries.add(mapResultSetToImagerie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imageries;
    }

    @Override
    public List<String> getAllTypeImagerie() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT idTypeImagerie FROM radiographies";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                types.add(rs.getString("idTypeImagerie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    @Override
    public Imagerie findByNomImagerieFr(String nomImagerieFr) {
        String sql = "SELECT * FROM radiographies WHERE idTypeImagerie = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomImagerieFr);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToImagerie(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode privée pour mapper les résultats de la base de données vers l'objet Imagerie
    private Imagerie mapResultSetToImagerie(ResultSet rs) throws SQLException {
        Imagerie imagerie = new Imagerie();
        imagerie.setImagerieID(rs.getInt("imagerieID"));
        imagerie.setConsultationID(rs.getInt("ConsultationID"));
        imagerie.setTypeImagerie(typeImagerieDAO.findById(rs.getInt("idTypeImagerie")));
        imagerie.setDateImagerie(rs.getObject("DateImagerie", LocalDate.class));
        imagerie.setResultat(rs.getString("Resultat"));
        imagerie.setDescription(rs.getString("description"));
        return imagerie;
    }
}
