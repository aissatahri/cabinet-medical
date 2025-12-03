/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */

import com.azmicro.moms.model.Utilisateur;
import java.util.List;

public interface UtilisateurDAO {
    void save(Utilisateur utilisateur);
    Utilisateur findById(int utilisateurID);
    Utilisateur findByUsername(String username);
    List<Utilisateur> findAll();
    void update(Utilisateur utilisateur);
    void delete(int utilisateurID);
}
