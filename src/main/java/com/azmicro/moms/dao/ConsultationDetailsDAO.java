/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.ConsultationDetails;
import java.util.List;

public interface ConsultationDetailsDAO {
    List<ConsultationDetails> getConsultationDetailsForToday();
    
    List<ConsultationDetails> getConsultationDetailsForLastWeek();
    
    public List<ConsultationDetails> getConsultationDetailsForLastMonth();
    
    public List<ConsultationDetails> getAllConsultationDetails();
}
