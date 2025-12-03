/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Imagerie;
import java.util.List;

public interface ImagerieDAO {

    void saveImagerie(Imagerie imagerie);

    void updateImagerie(Imagerie imagerie);

    void deleteImagerie(int imagerieID);

    Imagerie findById(int imagerieID);

    List<Imagerie> findAll();

    List<Imagerie> findByConsultationId(int consultationID);

    List<Imagerie> searchByName(String searchTerm); // Nouvelle méthode

    List<String> getAllTypeImagerie(); // Nouvelle méthode

    Imagerie findByNomImagerieFr(String nomImagerieFr); // Nouvelle méthode
}
