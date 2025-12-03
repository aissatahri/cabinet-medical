/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.TypeImagerieDAO;
import com.azmicro.moms.model.TypeImagerie;
import com.azmicro.moms.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeImagerieDAOImpl implements TypeImagerieDAO {

    @Override
    public boolean saveTypeImagerie(TypeImagerie typeImagerie) {
        String sql = "INSERT INTO types_imagerie (nom_imagerie_fr, nom_imagerie_en, description, code_imagerie_fr, code_imagerie_en) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, typeImagerie.getNomImagerieFr());
            stmt.setString(2, typeImagerie.getNomImagerieEn());
            stmt.setString(3, typeImagerie.getDescription());
            stmt.setString(4, typeImagerie.getCodeImagerieFr());
            stmt.setString(5, typeImagerie.getCodeImagerieEn());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTypeImagerie(TypeImagerie typeImagerie) {
        String sql = "UPDATE types_imagerie SET nom_imagerie_fr = ?, nom_imagerie_en = ?, description = ?, code_imagerie_fr = ?, code_imagerie_en = ? WHERE idTypeImagerie = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, typeImagerie.getNomImagerieFr());
            stmt.setString(2, typeImagerie.getNomImagerieEn());
            stmt.setString(3, typeImagerie.getDescription());
            stmt.setString(4, typeImagerie.getCodeImagerieFr());
            stmt.setString(5, typeImagerie.getCodeImagerieEn());
            stmt.setInt(6, typeImagerie.getIdTypeImagerie());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTypeImagerie(TypeImagerie typeImagerie) {
        String sql = "DELETE FROM types_imagerie WHERE idTypeImagerie = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, typeImagerie.getIdTypeImagerie());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TypeImagerie findById(int idTypeImagerie) {
        String sql = "SELECT * FROM types_imagerie WHERE idTypeImagerie = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTypeImagerie);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTypeImagerie(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TypeImagerie> findAll() {
        List<TypeImagerie> types = new ArrayList<>();
        String sql = "SELECT * FROM types_imagerie";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                types.add(mapResultSetToTypeImagerie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    @Override
    public List<TypeImagerie> searchByName(String searchTerm) {
        List<TypeImagerie> types = new ArrayList<>();
        String sql = "SELECT * FROM types_imagerie WHERE nom_imagerie_fr LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                types.add(mapResultSetToTypeImagerie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    @Override
    public TypeImagerie findByNomImagerieFr(String nomImagerieFr) {
        String sql = "SELECT * FROM types_imagerie WHERE nom_imagerie_fr = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomImagerieFr);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTypeImagerie(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private TypeImagerie mapResultSetToTypeImagerie(ResultSet rs) throws SQLException {
        TypeImagerie typeImagerie = new TypeImagerie();
        typeImagerie.setIdTypeImagerie(rs.getInt("idTypeImagerie"));
        typeImagerie.setNomImagerieFr(rs.getString("nom_imagerie_fr"));
        typeImagerie.setNomImagerieEn(rs.getString("nom_imagerie_en"));
        typeImagerie.setDescription(rs.getString("description"));
        typeImagerie.setCodeImagerieFr(rs.getString("code_imagerie_fr"));
        typeImagerie.setCodeImagerieEn(rs.getString("code_imagerie_en"));
        return typeImagerie;
    }

    @Override
    public List<String> getAllTypeImagerie() {
        List<String> typeImageries = new ArrayList<>();
        String sql = "SELECT nom_imagerie_fr, code_imagerie_fr FROM types_imagerie";

        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Concaténer nom_imagerie_fr et code_imagerie_fr avec ':' comme séparateur
                String nomImagerie = rs.getString("nom_imagerie_fr");
                String codeImagerie = rs.getString("code_imagerie_fr");
                typeImageries.add(nomImagerie + ":" + codeImagerie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return typeImageries;
    }

    @Override
    public List<TypeImagerie> findByKeyword(String searchTerm) {
        List<TypeImagerie> types = new ArrayList<>();
        String sql = "SELECT * FROM types_imagerie WHERE nom_imagerie_fr LIKE ? OR code_imagerie_fr LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                types.add(mapResultSetToTypeImagerie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

}
