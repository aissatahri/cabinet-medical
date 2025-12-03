/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.ActeDAO;
import com.azmicro.moms.model.Acte;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActeDAOImpl implements ActeDAO {

    @Override
    public boolean save(Acte acte) {
        String query = "INSERT INTO acte (idActe, nomActe, prix) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, acte.getIdActe());
            statement.setString(2, acte.getNomActe());
            statement.setDouble(3, acte.getPrix());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Acte findById(int idActe) {
        String query = "SELECT * FROM acte WHERE idActe = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idActe);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Acte acte = new Acte();
                    acte.setIdActe(resultSet.getInt("idActe"));
                    acte.setNomActe(resultSet.getString("nomActe"));
                    acte.setPrix(resultSet.getDouble("prix"));
                    return acte;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Acte> findAll() {
        String query = "SELECT * FROM acte";
        List<Acte> actes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Acte acte = new Acte();
                acte.setIdActe(resultSet.getInt("idActe"));
                acte.setNomActe(resultSet.getString("nomActe"));
                acte.setPrix(resultSet.getDouble("prix"));
                actes.add(acte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actes;
    }

    @Override
    public boolean update(Acte acte) {
        String query = "UPDATE acte SET nomActe = ?, prix = ? WHERE idActe = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, acte.getNomActe());
            statement.setDouble(2, acte.getPrix());
            statement.setInt(3, acte.getIdActe());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Acte acte) {
        String query = "DELETE FROM acte WHERE idActe = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, acte.getIdActe());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
