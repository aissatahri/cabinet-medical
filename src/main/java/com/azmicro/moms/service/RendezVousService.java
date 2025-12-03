/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.RendezVousDAO;
import com.azmicro.moms.dao.RendezVousDAOImpl;
import com.azmicro.moms.model.RendezVous;

import java.time.LocalDate;
import java.util.List;

public class RendezVousService {
    private final RendezVousDAO rendezVousDAO;

    public RendezVousService() {
        this.rendezVousDAO = new RendezVousDAOImpl();
    }

    public boolean saveRendezVous(RendezVous rendezVous) {
        return rendezVousDAO.save(rendezVous);
    }

    public boolean updateRendezVous(RendezVous rendezVous) {
        return rendezVousDAO.update(rendezVous);
    }

    public RendezVous findRendezVousById(int rendezVousID) {
        return rendezVousDAO.findById(rendezVousID);
    }

    public List<RendezVous> findAllRendezVous() {
        return rendezVousDAO.findAll();
    }

    public List<RendezVous> findRendezVousByPatient(int patientID) {
        return rendezVousDAO.findByPatient(patientID);
    }

    public List<RendezVous> findRendezVousByDate(LocalDate date) {
        return rendezVousDAO.findByDate(date);
    }

    public boolean deleteRendezVous(int rendezVousID) {
        return rendezVousDAO.delete(rendezVousID);
    }
    
    public List<RendezVous> getRendezVousByDate(LocalDate date) {
        return rendezVousDAO.findRendezVousByDate(date);
    }

    public List<RendezVous> getRendezVousByWeek(LocalDate startOfWeek) {
        return rendezVousDAO.findRendezVousByWeek(startOfWeek);
    }

    public List<RendezVous> getRendezVousByMonth(LocalDate firstDayOfMonth) {
        return rendezVousDAO.findRendezVousByMonth(firstDayOfMonth);
    }

    public List<RendezVous> getRendezVousByMonth(int monthValue, int year) {
        return rendezVousDAO.getRendezVousByMonth(monthValue, year); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<RendezVous> getRendezVousByMonth(LocalDate startOfMonth, LocalDate endOfMonth) {
        return rendezVousDAO.getRendezVousByMonth(startOfMonth, endOfMonth); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public List<RendezVous> findRendezVousFromToday(){
        return rendezVousDAO.findRendezVousFromToday();
    }
    
}
