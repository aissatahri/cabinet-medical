/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.DisponibilitesDAOImpl;
import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.Jours;
import com.azmicro.moms.model.Medecin;
import java.time.DayOfWeek;

import java.util.List;

public class DisponibilitesService {

    private final DisponibilitesDAOImpl disponibilitesDAO;

    public DisponibilitesService() {
        this.disponibilitesDAO = new DisponibilitesDAOImpl();
    }

    public boolean saveDisponibilite(Disponibilites disponibilite) {
        return disponibilitesDAO.save(disponibilite);
    }

    public Disponibilites getDisponibiliteById(int disponibiliteID) {
        return disponibilitesDAO.findById(disponibiliteID);
    }

    public List<Disponibilites> getAllDisponibilites() {
        return disponibilitesDAO.findAll();
    }

    public boolean updateDisponibilite(Disponibilites disponibilite) {
        return disponibilitesDAO.update(disponibilite);
    }

    public boolean deleteDisponibilite(int disponibiliteID) {
        return disponibilitesDAO.delete(disponibiliteID);
    }
    
    public List<Disponibilites> findAllByMedecin(Medecin medecin){
        return disponibilitesDAO.findAllByMedecin(medecin);
    }

    public Disponibilites findByMedecinAndJour(Medecin medecin, Jours jour) {
        return disponibilitesDAO.findByMedecinAndJour(medecin, jour); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
