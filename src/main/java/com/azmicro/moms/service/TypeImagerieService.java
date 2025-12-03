/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.TypeImagerieDAO;
import com.azmicro.moms.dao.TypeImagerieDAOImpl;
import com.azmicro.moms.model.TypeImagerie;
import java.util.List;

public class TypeImagerieService {
    private TypeImagerieDAO typeImagerieDAO = new TypeImagerieDAOImpl();

    public boolean saveTypeImagerie(TypeImagerie typeImagerie) {
        return typeImagerieDAO.saveTypeImagerie(typeImagerie);
    }

    public boolean updateTypeImagerie(TypeImagerie typeImagerie) {
        return typeImagerieDAO.updateTypeImagerie(typeImagerie);
    }

    public boolean deleteTypeImagerie(TypeImagerie typeImagerie) {
        return typeImagerieDAO.deleteTypeImagerie(typeImagerie);
    }

    public TypeImagerie findById(int idTypeImagerie) {
        return typeImagerieDAO.findById(idTypeImagerie);
    }

    public List<TypeImagerie> findAll() {
        return typeImagerieDAO.findAll();
    }

    public List<TypeImagerie> searchByName(String searchTerm) {
        return typeImagerieDAO.searchByName(searchTerm);
    }

    public TypeImagerie findByNomImagerieFr(String nomImagerieFr) {
        return typeImagerieDAO.findByNomImagerieFr(nomImagerieFr);
    }
    
    public List<String> getAllTypeImagerie() {
        return typeImagerieDAO.getAllTypeImagerie();
    }

    public List<TypeImagerie> findByKeyword(String keyword) {
        return typeImagerieDAO.findByKeyword(keyword); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
