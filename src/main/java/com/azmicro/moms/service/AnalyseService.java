/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.AnalyseDAO;
import com.azmicro.moms.model.Analyse;
import java.util.List;

public class AnalyseService {
    private final AnalyseDAO analyseDAO;

    public AnalyseService(AnalyseDAO analyseDAO) {
        this.analyseDAO = analyseDAO;
    }

    public boolean saveAnalyse(Analyse analyse) {
        return analyseDAO.save(analyse);
    }

    public Analyse getAnalyseById(int id) {
        return analyseDAO.findById(id);
    }

    public List<Analyse> getAllAnalyses() {
        return analyseDAO.findAll();
    }

    public boolean updateAnalyse(Analyse analyse) {
        return analyseDAO.update(analyse);
    }

    public boolean deleteAnalyse(int id) {
        return analyseDAO.delete(id);
    }
    
     // Nouvelle méthode pour récupérer les analyses par ID de consultation
    public List<Analyse> getAnalysesByConsultationId(int consultationId) {
        return analyseDAO.findByConsultationId(consultationId);
    }
}

