/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.SpecialitesDAO;
import com.azmicro.moms.dao.SpecialitesDAOImpl;
import com.azmicro.moms.model.Specialites;

import java.util.List;

public class SpecialitesService {

    private SpecialitesDAO specialitesDAO;

    public SpecialitesService() {
        this.specialitesDAO = new SpecialitesDAOImpl();
    }

    public boolean addSpecialite(Specialites specialite) {
        return specialitesDAO.save(specialite);
    }

    public boolean updateSpecialite(Specialites specialite) {
        return specialitesDAO.update(specialite);
    }

    public boolean removeSpecialite(int specialiteID) {
        return specialitesDAO.delete(specialiteID);
    }

    public Specialites getSpecialiteById(int specialiteID) {
        return specialitesDAO.findById(specialiteID);
    }

    public List<Specialites> getAllSpecialites() {
        return specialitesDAO.findAll();
    }

    public Specialites finByName(String specialites) {
        return specialitesDAO.finByName(specialites);
    }
}

