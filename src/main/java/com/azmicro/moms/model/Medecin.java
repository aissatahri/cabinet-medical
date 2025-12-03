/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public class Medecin {
    private int medecinID;
    private String nom;
    private String prenom;
    private Specialites specialite; // Utilisation de la classe Specialites
    private String telephone;
    private String email;
    private String adresse;
    private byte[] logo; // Stocke les données BLOB

    // Constructeur par défaut
    public Medecin() {}

    // Constructeur complet
    public Medecin(int medecinID, String nom, String prenom, Specialites specialite, String telephone, String email, String adresse, byte[] logo) {
        this.medecinID = medecinID;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.logo = logo;
    }

    // Getters et Setters
    public int getMedecinID() {
        return medecinID;
    }

    public void setMedecinID(int medecinID) {
        this.medecinID = medecinID;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Specialites getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialites specialite) {
        this.specialite = specialite;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Medecin{" +
                "medecinID=" + medecinID +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", specialite=" + specialite +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
