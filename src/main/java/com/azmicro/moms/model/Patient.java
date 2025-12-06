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
import java.time.Period;
import java.util.List;

/**
 * Représente un patient dans le système.
 */
public class Patient {

    private int patientID;
    private String nom;
    private String prenom;
    private String numDossier;
    private LocalDate dateNaissance;
    private int age;
    private Sexe sexe; // Utilisation de l'enum Sexe
    private SituationFamiliale situationFamiliale; // Nouveau champ
    private String profession;
    private String couvertureSanitaire;
    private String telephone;
    private String email;
    private String adresse;
    private List<FilesAttente> fileAttentes;

    // Constructeur par défaut
    public Patient() {}

    // Constructeur avec tous les attributs
    public Patient(int patientID, String nom, String prenom, String numDossier, LocalDate dateNaissance, int age, Sexe sexe,
                     SituationFamiliale situationFamiliale, String profession, String couvertureSanitaire, String telephone, String email, String adresse,
            List<FilesAttente> filesAttentes) {
        this.patientID = patientID;
        this.nom = nom;
        this.prenom = prenom;
        this.numDossier = numDossier;
        this.dateNaissance = dateNaissance;
        this.age = age;
        this.sexe = sexe;
        this.situationFamiliale = situationFamiliale; // Initialisation du nouveau champ
        this.profession = profession;
        this.couvertureSanitaire = couvertureSanitaire;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.fileAttentes = filesAttentes;
    }

    // Getters et Setters
    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
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

    public String getNumDossier() {
        return numDossier;
    }

    public void setNumDossier(String numDossier) {
        this.numDossier = numDossier;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public int getAgeInMonths() {
        LocalDate today = LocalDate.now();
        return Period.between(dateNaissance, today).getMonths() + Period.between(dateNaissance, today).getYears() * 12;
    }

    public int getAgeInYears() {
        LocalDate today = LocalDate.now();
        return Period.between(dateNaissance, today).getYears();
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale; // Getter pour le nouveau champ
    }

    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale; // Setter pour le nouveau champ
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCouvertureSanitaire() {
        return couvertureSanitaire;
    }

    public void setCouvertureSanitaire(String couvertureSanitaire) {
        this.couvertureSanitaire = couvertureSanitaire;
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

    public List<FilesAttente> getFileAttentes() {
        return fileAttentes;
    }

    public void setFileAttentes(List<FilesAttente> fileAttentes) {
        this.fileAttentes = fileAttentes;
    }
    
    

    @Override
    public String toString() {
        return "Patient{" +
                "patientID=" + patientID +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numDossier='" + numDossier + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", sexe=" + sexe +
                ", situationFamiliale=" + situationFamiliale + // Inclure le nouveau champ dans toString
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", couvertureSanitaire='" + couvertureSanitaire + '\'' +
                '}';
    }
}
