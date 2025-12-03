/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
import java.time.LocalDate;

public class Utilisateur {

    private int utilisateurID;
    private String nomUtilisateur;
    private String motDePasse;
    private Role role;
    private LocalDate dateCreation;
    private Medecin medecin;

    public Utilisateur() {
    }

    public Utilisateur(int utilisateurID, String nomUtilisateur, String motDePasse, Role role, LocalDate dateCreation, Medecin medecin) {
        this.utilisateurID = utilisateurID;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
        this.medecin = medecin;
    }

    public Utilisateur(int utilisateurID, String nomUtilisateur, String motDePasse, Role role, LocalDate dateCreation) {
        this.utilisateurID = utilisateurID;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateCreation = dateCreation;
    }

    public int getUtilisateurID() {
        return utilisateurID;
    }

    public void setUtilisateurID(int utilisateurID) {
        this.utilisateurID = utilisateurID;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "utilisateurID=" + utilisateurID + ", nomUtilisateur=" + nomUtilisateur + ", motDePasse=" + motDePasse + ", role=" + role + ", dateCreation=" + dateCreation + ", medecin=" + medecin + '}';
    }

}
