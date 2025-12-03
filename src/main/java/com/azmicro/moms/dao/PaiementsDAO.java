/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Consultations;
import com.azmicro.moms.model.Paiements;
import com.azmicro.moms.model.Patient;
import java.util.List;

public interface PaiementsDAO {
    boolean save(Paiements paiement); // Create
    boolean update(Paiements paiement);
    Paiements findById(int paiementID); // Read
    List<Paiements> findAll(); // Read all
    boolean delete(int paiementID); // Delete
    List<Paiements> getPaiementsByConsultation(Consultations consultation);
    List<Paiements> getPaiementsByConsultation(Patient patient);
}

