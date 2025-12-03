/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.PaiementsDAOImpl;
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.Paiements;
import com.azmicro.moms.model.Patient;

import java.util.List;

public class PaiementsService {

    private final PaiementsDAOImpl paiementsDAO;

    public PaiementsService() {
        this.paiementsDAO = new PaiementsDAOImpl();
    }

    // Sauvegarder un paiement
    public boolean savePaiement(Paiements paiement) {
        return paiementsDAO.save(paiement);
    }
    // update un paiement

    public boolean updatePaiement(Paiements paiement) {
        return paiementsDAO.update(paiement);
    }

    // Trouver un paiement par ID
    public Paiements findPaiementById(int paiementID) {
        return paiementsDAO.findById(paiementID);
    }

    // Récupérer tous les paiements
    public List<Paiements> getAllPaiements() {
        return paiementsDAO.findAll();
    }

    // Supprimer un paiement par ID
    public boolean deletePaiement(int paiementID) {
        return paiementsDAO.delete(paiementID);
    }

    public List<Paiements> getPaiementsByConsultation(Consultations consultation) {
        return paiementsDAO.getPaiementsByConsultation(consultation);
    }
    
    public List<Paiements> getPaiementsByConsultation(Patient patient){
        return paiementsDAO.getPaiementsByConsultation(patient);
    }
}
