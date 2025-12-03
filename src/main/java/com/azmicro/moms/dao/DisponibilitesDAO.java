/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Disponibilites;
import com.azmicro.moms.model.Jours;
import com.azmicro.moms.model.Medecin;
import java.time.DayOfWeek;
import java.util.List;

public interface DisponibilitesDAO {

    boolean save(Disponibilites disponibilite);

    Disponibilites findById(int disponibiliteID);

    List<Disponibilites> findAll();
    
    List<Disponibilites> findAllByMedecin(Medecin medecin);

    boolean update(Disponibilites disponibilite);

    boolean delete(int disponibiliteID);
    
    Disponibilites findByMedecinAndJour(Medecin medecin, Jours jour);
}
