/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.ConsultationActeDAOImpl;
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.Connection;
import java.util.List;

public class ConsultationActeService {

    private ConsultationActeDAOImpl consultationActeDAO;

    public ConsultationActeService() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            this.consultationActeDAO = new ConsultationActeDAOImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean save(ConsultationActe consultationActe) {
        return consultationActeDAO.save(consultationActe);
    }

    public boolean update(ConsultationActe consultationActe) {
        return consultationActeDAO.update(consultationActe);
    }

    public boolean delete(int idConsultationActe) {
        return consultationActeDAO.delete(idConsultationActe);
    }

    public ConsultationActe findById(int idConsultationActe) {
        return consultationActeDAO.findById(idConsultationActe);
    }

    public List<ConsultationActe> findAll() {
        return consultationActeDAO.findAll();
    }

    public List<ConsultationActe> findByConsultation(Consultations consultation) {
        return consultationActeDAO.findByConsultation(consultation); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

