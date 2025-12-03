/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.SpecialitesDAO;
import com.azmicro.moms.model.Specialites;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialitesDAOImpl implements SpecialitesDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseUtil.getConnection();
    }

    @Override
    public boolean save(Specialites specialite) {
        String sql = "INSERT INTO specialites (SpecialiteID, NomSpecialite) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, specialite.getSpecialiteID());
            stmt.setString(2, specialite.getNomSpecialite());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Specialites specialite) {
        String sql = "UPDATE specialites SET NomSpecialite = ? WHERE SpecialiteID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, specialite.getNomSpecialite());
            stmt.setInt(2, specialite.getSpecialiteID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int specialiteID) {
        String sql = "DELETE FROM specialites WHERE SpecialiteID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, specialiteID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Specialites findById(int specialiteID) {
        String sql = "SELECT * FROM specialites WHERE SpecialiteID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, specialiteID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Specialites(
                        rs.getInt("SpecialiteID"),
                        rs.getString("NomSpecialite")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Specialites> findAll() {
        List<Specialites> specialitesList = new ArrayList<>();
        String sql = "SELECT * FROM specialites";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                specialitesList.add(new Specialites(
                        rs.getInt("SpecialiteID"),
                        rs.getString("NomSpecialite")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialitesList;
    }

    @Override
    public Specialites finByName(String specialites) {
        String sql = "SELECT * FROM specialites WHERE NomSpecialite = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, specialites);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Specialites(
                        rs.getInt("SpecialiteID"),
                        rs.getString("NomSpecialite")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
