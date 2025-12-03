/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.TypeAnalyse;
import com.azmicro.moms.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TypeAnalyseDAOImpl implements TypeAnalyseDAO {

    // Utilise DatabaseUtil pour obtenir la connexion, donc pas besoin de la stocker ici
    // private final Connection connection; // Retiré car non utilisé
    @Override
    public boolean save(TypeAnalyse typeAnalyse) {
        String sql = "INSERT INTO types_analyse (idTypeAnalyse, nom_analyse_fr, nom_analyse_en, description, code_analyse_fr, code_analyse_en) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeAnalyse.getIdTypeAnalyse());
            stmt.setString(2, typeAnalyse.getNomAnalyseFr());
            stmt.setString(3, typeAnalyse.getNomAnalyseEn());
            stmt.setString(4, typeAnalyse.getDescription());
            stmt.setString(5, typeAnalyse.getCodeAnalyseFr());
            stmt.setString(6, typeAnalyse.getCodeAnalyseEn());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error saving TypeAnalyse", e);
            return false;
        }
    }

    @Override
    public TypeAnalyse findById(int id) {
        String sql = "SELECT * FROM types_analyse WHERE idTypeAnalyse = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TypeAnalyse(
                            rs.getInt("idTypeAnalyse"),
                            rs.getString("nom_analyse_fr"),
                            rs.getString("nom_analyse_en"),
                            rs.getString("description"),
                            rs.getString("code_analyse_fr"),
                            rs.getString("code_analyse_en")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error finding TypeAnalyse by ID", e);
        }
        return null;
    }

    @Override
    public List<TypeAnalyse> findAll() {
        List<TypeAnalyse> typeAnalyseList = new ArrayList<>();
        String sql = "SELECT * FROM types_analyse";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                typeAnalyseList.add(new TypeAnalyse(
                        rs.getInt("idTypeAnalyse"),
                        rs.getString("nom_analyse_fr"),
                        rs.getString("nom_analyse_en"),
                        rs.getString("description"),
                        rs.getString("code_analyse_fr"),
                        rs.getString("code_analyse_en")
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error finding all TypeAnalyses", e);
        }
        return typeAnalyseList;
    }

    @Override
    public boolean update(TypeAnalyse typeAnalyse) {
        String sql = "UPDATE types_analyse SET nom_analyse_fr = ?, nom_analyse_en = ?, description = ?, code_analyse_fr = ?, code_analyse_en = ? WHERE idTypeAnalyse = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, typeAnalyse.getNomAnalyseFr());
            stmt.setString(2, typeAnalyse.getNomAnalyseEn());
            stmt.setString(3, typeAnalyse.getDescription());
            stmt.setString(4, typeAnalyse.getCodeAnalyseFr());
            stmt.setString(5, typeAnalyse.getCodeAnalyseEn());
            stmt.setInt(6, typeAnalyse.getIdTypeAnalyse());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error updating TypeAnalyse", e);
            return false;
        }
    }

    @Override
    public boolean delete(TypeAnalyse typeAnalyse) {
        String sql = "DELETE FROM types_analyse WHERE idTypeAnalyse = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeAnalyse.getIdTypeAnalyse());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error deleting TypeAnalyse", e);
            return false;
        }
    }

    @Override
    public List<TypeAnalyse> searchByName(String searchTerm) {
        List<TypeAnalyse> typeAnalyseList = new ArrayList<>();
        String sql = "SELECT * FROM types_analyse WHERE nom_analyse_fr LIKE ? OR nom_analyse_en LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    typeAnalyseList.add(new TypeAnalyse(
                            rs.getInt("idTypeAnalyse"),
                            rs.getString("nom_analyse_fr"),
                            rs.getString("nom_analyse_en"),
                            rs.getString("description"),
                            rs.getString("code_analyse_fr"),
                            rs.getString("code_analyse_en")
                    ));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error searching TypeAnalyse by name", e);
        }
        return typeAnalyseList;
    }

    @Override
    public List<String> getAllTypeAnalyses() {
        List<String> typeAnalyses = new ArrayList<>();
        String query = "SELECT nom_analyse_fr, code_analyse_fr FROM types_analyse";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement statement = conn.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String nomAnalyse = resultSet.getString("nom_analyse_fr");
                String codeAnalyse = resultSet.getString("code_analyse_fr");
                String analyseDisplay = nomAnalyse + ": (" + codeAnalyse + ")";
                typeAnalyses.add(analyseDisplay);
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error retrieving all type analyses", e);
        }
        return typeAnalyses;
    }

    @Override
    public TypeAnalyse findByNomAnalyseFr(String nomAnalyseFr) {
        String sql = "SELECT * FROM types_analyse WHERE nom_analyse_fr = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomAnalyseFr);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new TypeAnalyse(
                            rs.getInt("idTypeAnalyse"),
                            rs.getString("nom_analyse_fr"),
                            rs.getString("nom_analyse_en"),
                            rs.getString("description"),
                            rs.getString("code_analyse_fr"),
                            rs.getString("code_analyse_en")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error finding TypeAnalyse by nomAnalyseFr", e);
        }
        return null;
    }

    @Override
    public List<TypeAnalyse> findByKeyword(String keyword) {
        List<TypeAnalyse> typeAnalyseList = new ArrayList<>();
        String sql = "SELECT * FROM types_analyse WHERE nom_analyse_fr LIKE ? OR nom_analyse_en LIKE ? OR code_analyse_fr LIKE ? OR code_analyse_en LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Utilisation du pattern pour la recherche avec les wildcards
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Ajouter chaque TypeAnalyse trouvé dans la liste
                    typeAnalyseList.add(new TypeAnalyse(
                            rs.getInt("idTypeAnalyse"),
                            rs.getString("nom_analyse_fr"),
                            rs.getString("nom_analyse_en"),
                            rs.getString("description"),
                            rs.getString("code_analyse_fr"),
                            rs.getString("code_analyse_en")
                    ));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TypeAnalyseDAOImpl.class.getName()).log(Level.SEVERE, "Error searching TypeAnalyse by keyword", e);
        }

        return typeAnalyseList;
    }

}
