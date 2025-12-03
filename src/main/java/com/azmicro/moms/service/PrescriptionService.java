/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.PrescriptionDAO;
import com.azmicro.moms.dao.PrescriptionDAOImpl;
import com.azmicro.moms.model.Prescriptions;
import java.util.List;

public class PrescriptionService {

    private final PrescriptionDAO prescriptionDAO;

    public PrescriptionService() {
        this.prescriptionDAO = new PrescriptionDAOImpl();
    }

    public boolean savePrescription(Prescriptions prescription) {
        return prescriptionDAO.savePrescription(prescription);
    }

    public boolean updatePrescription(Prescriptions prescription) {
        return prescriptionDAO.updatePrescription(prescription);
    }

    public boolean deletePrescription(int prescriptionID) {
        return prescriptionDAO.deletePrescription(prescriptionID);
    }

    public Prescriptions getPrescriptionById(int prescriptionID) {
        return prescriptionDAO.getPrescriptionById(prescriptionID);
    }

    public List<Prescriptions> getAllPrescriptions() {
        return prescriptionDAO.getAllPrescriptions();
    }
    
    public List<Prescriptions> getPrescriptionByConsultation(int consultationID){
        return prescriptionDAO.getPrescriptionByConsultation(consultationID);
    }
}

