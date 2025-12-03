/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.ImagerieDAO;
import com.azmicro.moms.dao.ImagerieDAOImpl;
import com.azmicro.moms.model.Imagerie;
import java.util.List;

public class ImagerieService {
    private final ImagerieDAO imagerieDAO = new ImagerieDAOImpl();

    public void saveImagerie(Imagerie imagerie) {
        imagerieDAO.saveImagerie(imagerie);
    }

    public void updateImagerie(Imagerie imagerie) {
        imagerieDAO.updateImagerie(imagerie);
    }

    public void deleteImagerie(int imagerieID) {
        imagerieDAO.deleteImagerie(imagerieID);
    }

    public Imagerie findById(int imagerieID) {
        return imagerieDAO.findById(imagerieID);
    }

    public List<Imagerie> findAll() {
        return imagerieDAO.findAll();
    }

    public List<Imagerie> findByConsultationId(int consultationID) {
        return imagerieDAO.findByConsultationId(consultationID);
    }
    
    public List<Imagerie> searchByName(String searchTerm) {
        return imagerieDAO.searchByName(searchTerm);
    }

    public List<String> getAllTypeImagerie() {
        return imagerieDAO.getAllTypeImagerie();
    }

    public Imagerie findByNomImagerieFr(String nomImagerieFr) {
        return imagerieDAO.findByNomImagerieFr(nomImagerieFr);
    }
}

