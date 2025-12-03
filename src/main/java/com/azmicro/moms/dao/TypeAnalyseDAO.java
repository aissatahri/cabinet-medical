/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.TypeAnalyse;
import java.util.List;

public interface TypeAnalyseDAO {

    boolean save(TypeAnalyse typeAnalyse);

    TypeAnalyse findById(int id);

    List<TypeAnalyse> findAll();

    boolean update(TypeAnalyse typeAnalyse);

    boolean delete(TypeAnalyse typeAnalyse);
    
    List<TypeAnalyse> searchByName(String searchTerm);
    
    List<String> getAllTypeAnalyses();
     
    TypeAnalyse findByNomAnalyseFr(String nomAnalyseFr);

    List<TypeAnalyse> findByKeyword(String keyword);
}
