/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */

import com.azmicro.moms.dao.HistoriqueMedicalDAO;
import com.azmicro.moms.dao.HistoriqueMedicalDAOImpl;
import com.azmicro.moms.model.HistoriqueMedical;
import java.util.List;

public class HistoriqueMedicalService {

    private HistoriqueMedicalDAO historiqueMedicalDAO;

    // Constructor accepting an instance of HistoriqueMedicalDAO
    public HistoriqueMedicalService(HistoriqueMedicalDAO historiqueMedicalDAO) {
        this.historiqueMedicalDAO = historiqueMedicalDAO;
    }

    public void saveHistoriqueMedical(HistoriqueMedical historiqueMedical) {
        if (historiqueMedical != null && historiqueMedical.getPatient() != null) {
            historiqueMedicalDAO.save(historiqueMedical);
        } else {
            throw new IllegalArgumentException("HistoriqueMedical or Patient cannot be null");
        }
    }

    public HistoriqueMedical findHistoriqueMedicalById(int historiqueID) {
        if (historiqueID > 0) {
            return historiqueMedicalDAO.findById(historiqueID);
        } else {
            throw new IllegalArgumentException("HistoriqueID must be greater than 0");
        }
    }

    public List<HistoriqueMedical> findAllHistoriquesMedical() {
        return historiqueMedicalDAO.findAll();
    }
    
    public List<HistoriqueMedical> findAllHistoriquesMedicalByIdPatient(int IdPatient) {
        return historiqueMedicalDAO.findAllByPatientId(IdPatient);
    }

    public void updateHistoriqueMedical(HistoriqueMedical historiqueMedical) {
        if (historiqueMedical != null && historiqueMedical.getHistoriqueID() > 0) {
            historiqueMedicalDAO.update(historiqueMedical);
        } else {
            throw new IllegalArgumentException("HistoriqueMedical or HistoriqueID cannot be null or zero");
        }
    }

    public void deleteHistoriqueMedical(HistoriqueMedical historiqueMedical) {
        if (historiqueMedical.getHistoriqueID() > 0) {
            historiqueMedicalDAO.delete(historiqueMedical);
        } else {
            throw new IllegalArgumentException("HistoriqueID must be greater than 0");
        }
    }
}

