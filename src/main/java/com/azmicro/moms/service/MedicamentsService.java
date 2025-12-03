/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.MedicamentsDAO;
import com.azmicro.moms.dao.MedicamentsDAOImpl;
import com.azmicro.moms.model.Medicaments;

import java.util.List;

public class MedicamentsService {
    private MedicamentsDAO medicamentsDAO = new MedicamentsDAOImpl();

    public boolean saveMedicaments(Medicaments medicament) {
        return medicamentsDAO.save(medicament);
    }

    public boolean updateMedicaments(Medicaments medicament) {
        return medicamentsDAO.update(medicament);
    }

    public boolean deleteMedicaments(Medicaments medicament) {
        return medicamentsDAO.delete(medicament);
    }

    public Medicaments findMedicamentsById(int medicamentID) {
        return medicamentsDAO.findById(medicamentID);
    }

    public List<Medicaments> findAllMedicaments() {
        return medicamentsDAO.findAll();
    }
    
    public Medicaments findByNameAndFormeDosage(String nomMedicament, String formeDosage){
        return medicamentsDAO.findByNameAndFormeDosage(nomMedicament, formeDosage);
    }

    public List<Medicaments> findByKeyword(String keyword) {
        return medicamentsDAO.findByKeyword(keyword); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

