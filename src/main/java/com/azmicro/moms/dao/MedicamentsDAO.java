/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Medicaments;
import java.util.List;

public interface MedicamentsDAO {

    boolean save(Medicaments medicament);

    boolean update(Medicaments medicament);

    boolean delete(Medicaments medicament);

    Medicaments findById(int medicamentID);

    List<Medicaments> findAll();
    
    Medicaments findByNameAndFormeDosage(String nomMedicament, String formeDosage);

    List<Medicaments> findByKeyword(String keyword);
}
