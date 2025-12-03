/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

import com.azmicro.moms.model.TypeImagerie;
import java.util.List;

/**
 *
 * @author Aissa
 */
public interface TypeImagerieDAO {

    boolean saveTypeImagerie(TypeImagerie typeImagerie);

    boolean updateTypeImagerie(TypeImagerie typeImagerie);

    boolean deleteTypeImagerie(TypeImagerie typeImagerie);

    TypeImagerie findById(int idTypeImagerie);

    List<TypeImagerie> findAll();

    List<TypeImagerie> searchByName(String searchTerm);

    TypeImagerie findByNomImagerieFr(String nomImagerieFr);

    List<String> getAllTypeImagerie();

    List<TypeImagerie> findByKeyword(String searchTerm);
}
