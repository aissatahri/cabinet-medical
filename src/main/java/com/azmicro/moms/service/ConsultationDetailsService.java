/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.ConsultationDetailsDAO;
import com.azmicro.moms.dao.ConsultationDetailsDAOImpl;
import com.azmicro.moms.model.ConsultationDetails;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultationDetailsService {

    private final ConsultationDetailsDAO consultationDetailsDAO;

    public ConsultationDetailsService() {
        this.consultationDetailsDAO = new ConsultationDetailsDAOImpl();
    }

    // Récupérer toutes les consultations
    public List<ConsultationDetails> getAllConsultationDetails() {
        return consultationDetailsDAO.getAllConsultationDetails();
    }

    // Récupérer les consultations du jour
    public List<ConsultationDetails> getConsultationDetailsForToday() {
        LocalDate today = LocalDate.now();
        return consultationDetailsDAO.getAllConsultationDetails().stream()
                .filter(consultation -> consultation.getDateConsultation().equals(today))
                .collect(Collectors.toList());
    }

    // Récupérer les consultations de la semaine dernière
    public List<ConsultationDetails> getConsultationDetailsForLastWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);  // Premier jour de la semaine
        return consultationDetailsDAO.getAllConsultationDetails().stream()
                .filter(consultation -> !consultation.getDateConsultation().isBefore(startOfWeek))
                .collect(Collectors.toList());
    }

    // Récupérer les consultations du dernier mois
    public List<ConsultationDetails> getConsultationDetailsForLastMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);  // Premier jour du mois
        return consultationDetailsDAO.getAllConsultationDetails().stream()
                .filter(consultation -> !consultation.getDateConsultation().isBefore(startOfMonth))
                .collect(Collectors.toList());
    }

}
