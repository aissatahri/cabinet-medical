/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Patient;
import java.util.List;

public interface PatientDAO {
    boolean save(Patient patient);
    Patient findById(int patientID);
    List<Patient> findAll();
    boolean update(Patient patient);
    boolean delete(int patientID);
    public int getLastPatientId();

    public Patient findByNumDossier(String numDossier);
}
