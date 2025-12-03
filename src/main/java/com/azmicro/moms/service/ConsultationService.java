/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.ConsultationDAO;
import com.azmicro.moms.dao.ConsultationDAOImpl;
import com.azmicro.moms.model.Consultations;
import java.sql.Connection;
import java.util.List;

public class ConsultationService {
    private final ConsultationDAO consultationDAO;

    // Constructor with Dependency Injection
    public ConsultationService(Connection connection) {
        this.consultationDAO = new ConsultationDAOImpl(connection);
    }

    public boolean save(Consultations consultation) {
        return consultationDAO.save(consultation);
    }

    public Consultations findById(int id) {
        return consultationDAO.findById(id);
    }

    public List<Consultations> findAll() {
        return consultationDAO.findAll();
    }
    
    public List<Consultations> findAllByIdPatient(int idPatient) {
        return consultationDAO.findAllByIdPatient(idPatient);
    }

    public boolean update(Consultations consultation) {
        return consultationDAO.update(consultation);
    }

    public boolean delete(int id) {
        return consultationDAO.delete(id);
    }
}
