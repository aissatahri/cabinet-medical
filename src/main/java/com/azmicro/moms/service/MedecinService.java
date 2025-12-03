/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.MedecinDAO;
import com.azmicro.moms.dao.MedecinDAOImpl;
import com.azmicro.moms.model.Medecin;
import java.sql.SQLException;

import java.util.List;

public class MedecinService {
    private MedecinDAO medecinDAO;

    public MedecinService() {
        this.medecinDAO = new MedecinDAOImpl();
    }

    public boolean createMedecin(Medecin medecin) {
        return medecinDAO.createMedecin(medecin);
    }

    public Medecin getMedecin(int medecinID) throws SQLException {
        return medecinDAO.readMedecin(medecinID);
    }

    public List<Medecin> getAllMedecins() throws SQLException {
        return medecinDAO.readAllMedecins();
    }

    public boolean updateMedecin(Medecin medecin) {
        return medecinDAO.updateMedecin(medecin);
    }

    public boolean deleteMedecin(int medecinID) {
        return medecinDAO.deleteMedecin(medecinID);
    }
}

