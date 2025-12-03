/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */

import com.azmicro.moms.model.RendezVous;
import java.time.LocalDate;

import java.util.List;

public interface RendezVousDAO {
    boolean save(RendezVous rendezVous);
    boolean update(RendezVous rendezVous);
    RendezVous findById(int rendezVousID);
    List<RendezVous> findAll();
    List<RendezVous> findByPatient(int patientID);
    List<RendezVous> findByDate(LocalDate date);
    boolean delete(int rendezVousID);

    public List<RendezVous> findRendezVousByDate(LocalDate date);

    public List<RendezVous> findRendezVousByWeek(LocalDate startOfWeek);

    public List<RendezVous> findRendezVousByMonth(LocalDate firstDayOfMonth);
    
    List<RendezVous> getRendezVousByMonth(int monthValue, int year);
    
    List<RendezVous> getRendezVousByMonth(LocalDate startOfMonth, LocalDate endOfMonth);
    
    List<RendezVous> findRendezVousFromToday();
}
