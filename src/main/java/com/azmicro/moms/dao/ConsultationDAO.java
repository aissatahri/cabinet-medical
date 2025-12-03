/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Consultations;
import java.util.List;

public interface ConsultationDAO {
    boolean save(Consultations consultation);
    Consultations findById(int id);
    List<Consultations> findAll();
    boolean update(Consultations consultation);
    boolean delete(int id);
    List<Consultations> findAllByIdPatient(int patientId);
}

