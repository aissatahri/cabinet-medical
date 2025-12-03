/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.TypeAnalyseDAO;
import com.azmicro.moms.dao.TypeAnalyseDAOImpl;
import com.azmicro.moms.model.TypeAnalyse;
import java.sql.Connection;
import java.util.List;

public class TypeAnalyseService {
    private TypeAnalyseDAO typeAnalyseDAO = new TypeAnalyseDAOImpl();

    

    public boolean save(TypeAnalyse typeAnalyse) {
        return typeAnalyseDAO.save(typeAnalyse);
    }

    public TypeAnalyse findById(int id) {
        return typeAnalyseDAO.findById(id);
    }

    public List<TypeAnalyse> findAll() {
        return typeAnalyseDAO.findAll();
    }

    public boolean update(TypeAnalyse typeAnalyse) {
        return typeAnalyseDAO.update(typeAnalyse);
    }

    public boolean delete(TypeAnalyse typeAnalyse) {
        return typeAnalyseDAO.delete(typeAnalyse);
    }
    
    public List<TypeAnalyse> searchByName(String searchTerm) {
        // Implement this method in your DAO and service to filter results based on the search term
        return typeAnalyseDAO.searchByName(searchTerm);
    }
    
     public List<String> getAllTypeAnalyses() {
         return typeAnalyseDAO.getAllTypeAnalyses();
     }
     
     public TypeAnalyse findByNomAnalyseFr(String nomAnalyseFr){
         return typeAnalyseDAO.findByNomAnalyseFr(nomAnalyseFr);
     }

    public List<TypeAnalyse> findByKeyword(String keyword) {
        return typeAnalyseDAO.findByKeyword(keyword); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
