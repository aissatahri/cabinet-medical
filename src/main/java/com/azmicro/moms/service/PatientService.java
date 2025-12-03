/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */

import com.azmicro.moms.dao.PatientDAO;
import com.azmicro.moms.dao.PatientDAOImpl;
import com.azmicro.moms.model.Patient;
import com.azmicro.moms.util.DatabaseUtil;
import java.sql.SQLException;

import java.util.List;

public class PatientService {

    private PatientDAO patientDAO;

    public PatientService() throws SQLException {
        this.patientDAO = new PatientDAOImpl(DatabaseUtil.getConnection());
    }

    public boolean save(Patient patient) {
        // Ajouter toute logique métier nécessaire avant d'appeler le DAO
        // Par exemple : validation des données
        validatePatient(patient);
        return patientDAO.save(patient);
    }

    public Patient findById(int patientID) {
        // Ajouter toute logique métier nécessaire avant d'appeler le DAO
        return patientDAO.findById(patientID);
    }

    public List<Patient> findAll() {
        // Ajouter toute logique métier nécessaire avant d'appeler le DAO
        return patientDAO.findAll();
    }

    public boolean update(Patient patient) {
        // Ajouter toute logique métier nécessaire avant d'appeler le DAO
        validatePatient(patient);
        return patientDAO.update(patient);
    }

    public boolean delete(int patientID) {
        // Ajouter toute logique métier nécessaire avant d'appeler le DAO
        return patientDAO.delete(patientID);
    }

    private void validatePatient(Patient patient) {
        // Implémentez la logique de validation ici
        // Par exemple, vérifier que les champs ne sont pas vides, que la date de naissance est valide, etc.
        if (patient.getNom() == null || patient.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du patient ne peut pas être vide.");
        }
        // Ajoutez d'autres validations selon les besoins
    }

    public int getLastPatientId() {
        return patientDAO.getLastPatientId(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Patient findByNumDossier(String numDossier) {
        return patientDAO.findByNumDossier(numDossier); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
