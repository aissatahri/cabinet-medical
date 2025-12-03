/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Prescriptions;
import java.util.List;

public interface PrescriptionDAO {

    boolean savePrescription(Prescriptions prescription);

    boolean updatePrescription(Prescriptions prescription);

    boolean deletePrescription(int prescriptionID);

    Prescriptions getPrescriptionById(int prescriptionID);

    List<Prescriptions> getAllPrescriptions();
    
    public List<Prescriptions> getPrescriptionByConsultation(int consultationID);
}
