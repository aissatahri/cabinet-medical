/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Analyse;
import java.util.List;

public interface AnalyseDAO {
    boolean save(Analyse analyse);
    Analyse findById(int id);
    List<Analyse> findAll();
    boolean update(Analyse analyse);
    boolean delete(int id);
    public List<Analyse> findByConsultationId(int consultationId);
}

