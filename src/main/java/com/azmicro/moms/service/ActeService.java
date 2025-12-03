/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.ActeDAO;
import com.azmicro.moms.dao.ActeDAOImpl;
import com.azmicro.moms.model.Acte;

import java.util.List;

public class ActeService {

    private final ActeDAO acteDAO = new ActeDAOImpl();

    public boolean addActe(Acte acte) {
        return acteDAO.save(acte);
    }

    public Acte getActeById(int idActe) {
        return acteDAO.findById(idActe);
    }

    public List<Acte> getAllActes() {
        return acteDAO.findAll();
    }

    public boolean updateActe(Acte acte) {
        return acteDAO.update(acte);
    }

    public boolean deleteActe(Acte acte) {
        return acteDAO.delete(acte);
    }
}

