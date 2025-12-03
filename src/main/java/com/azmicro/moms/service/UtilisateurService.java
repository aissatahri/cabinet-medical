/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.UtilisateurDAO;
import com.azmicro.moms.dao.UtilisateurDAOImpl;
import com.azmicro.moms.model.Utilisateur;
import java.util.List;

public class UtilisateurService {

    private UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAOImpl();
    }

    public void saveUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.save(utilisateur);
    }

    public Utilisateur getUtilisateurById(int id) {
        return utilisateurDAO.findById(id);
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurDAO.findAll();
    }

    public void updateUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.update(utilisateur);
    }

    public void deleteUtilisateur(int id) {
        utilisateurDAO.delete(id);
    }

    public boolean doesUserExist(String username) {
        return utilisateurDAO.findByUsername(username) != null;
    }

    // Authentifie un utilisateur en vérifiant le nom d'utilisateur et le mot de passe
    public Utilisateur authenticateUser(String username, String password) {
        // Récupérer l'utilisateur basé sur le nom d'utilisateur
        Utilisateur utilisateur = utilisateurDAO.findByUsername(username);

        // Vérifier si l'utilisateur existe et si le mot de passe est correct
        if (utilisateur != null && utilisateur.getMotDePasse().equals(password)) {
            return utilisateur;
        } else {
            return null;
        }
    }
}
