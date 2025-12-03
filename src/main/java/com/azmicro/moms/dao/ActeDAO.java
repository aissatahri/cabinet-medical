/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Acte;

import java.util.List;

public interface ActeDAO {
    boolean save(Acte acte);
    Acte findById(int idActe);
    List<Acte> findAll();
    boolean update(Acte acte);
    boolean delete(Acte acte);
}

