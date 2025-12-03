/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.ConsultationActe;
import com.azmicro.moms.model.Consultations;

import java.util.List;

public interface ConsultationActeDAO {

    boolean save(ConsultationActe consultationActe);

    boolean update(ConsultationActe consultationActe);

    boolean delete(int idConsultationActe);

    ConsultationActe findById(int idConsultationActe);

    List<ConsultationActe> findAll();
    
    List<ConsultationActe> findByConsultation(Consultations consultation);
}
