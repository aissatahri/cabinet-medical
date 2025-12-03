/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.HistoriqueMedical;
import java.util.List;

public interface HistoriqueMedicalDAO {
    void save(HistoriqueMedical historiqueMedical);
    HistoriqueMedical findById(int historiqueID);
    List<HistoriqueMedical> findAll();
    void update(HistoriqueMedical historiqueMedical);
    void delete(HistoriqueMedical historique);
    List<HistoriqueMedical> findAllByPatientId(int patientId);
}
