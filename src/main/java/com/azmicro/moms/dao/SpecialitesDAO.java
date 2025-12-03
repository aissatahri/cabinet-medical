/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

import com.azmicro.moms.model.Specialites;
import java.util.List;

/**
 *
 * @author Aissa
 */
public interface SpecialitesDAO {

    boolean save(Specialites specialite);

    boolean update(Specialites specialite);

    boolean delete(int specialiteID);

    Specialites findById(int specialiteID);

    List<Specialites> findAll();
    
    public Specialites finByName(String specialites);
}
