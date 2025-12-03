/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.azmicro.moms.model.Acte;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationActeDAOImpl implements ConsultationActeDAO {

    @Override
    public boolean save(ConsultationActe consultationActe) {
        String query = "INSERT INTO consultationacte (idActe, idConsultation) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, consultationActe.getActe().getIdActe());
            ps.setInt(2, consultationActe.getConsultation().getConsultationID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ConsultationActe consultationActe) {
        String query = "UPDATE consultationacte SET idActe = ?, idConsultation = ? WHERE idConsultationActe = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, consultationActe.getActe().getIdActe());
            ps.setInt(2, consultationActe.getConsultation().getConsultationID());
            ps.setInt(3, consultationActe.getIdConsultationActe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int idConsultationActe) {
        String query = "DELETE FROM consultationacte WHERE idConsultationActe = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idConsultationActe);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
@Override
public ConsultationActe findById(int idConsultationActe) {
    String query = "SELECT * FROM consultationacte WHERE idConsultationActe = ?";
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setInt(1, idConsultationActe);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Acte acte = new ActeDAOImpl().findById(rs.getInt("idActe"));
            Consultations consultation = new ConsultationDAOImpl(connection).findById(rs.getInt("ConsultationID")); // Passer l'objet Connection ici
            return new ConsultationActe(rs.getInt("idConsultationActe"), acte, consultation);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    @Override
    public List<ConsultationActe> findAll() {
        List<ConsultationActe> consultationActeList = new ArrayList<>();
        String query = "SELECT * FROM consultationacte";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Acte acte = new ActeDAOImpl().findById(rs.getInt("idActe"));
                Consultations consultation = new ConsultationDAOImpl(connection).findById(rs.getInt("ConsultationID")); // Passer l'objet Connection ici
                ConsultationActe consultationActe = new ConsultationActe(rs.getInt("idConsultationActe"), acte, consultation);
                consultationActeList.add(consultationActe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultationActeList;
    }

    @Override
public List<ConsultationActe> findByConsultation(Consultations consultation) {
    List<ConsultationActe> consultationActeList = new ArrayList<>();
    String query = "SELECT * FROM consultationacte WHERE idConsultation = ?";
    
    try (Connection connection = DatabaseUtil.getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {
        
        ps.setInt(1, consultation.getConsultationID());
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Acte acte = new ActeDAOImpl().findById(rs.getInt("idActe"));
                ConsultationActe consultationActe = new ConsultationActe(
                        rs.getInt("idConsultationActe"), 
                        acte, 
                        consultation
                );
                consultationActeList.add(consultationActe);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return consultationActeList;
}


    
}
