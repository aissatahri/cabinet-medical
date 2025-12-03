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
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Représente un enregistrement dans la table FilesAttente.
 */
public class FilesAttente {

    private int fileAttenteID;
    private Patient patient; // Association avec la classe Patient
    private LocalDate dateArrivee;
    private LocalTime heureArrive;
    private EtatArrive etat;
    private Statut statut; // Utilisation de l'enum Statut

    // Constructeur par défaut
    public FilesAttente() {
    }

    // Constructeur avec tous les attributs
    public FilesAttente(int fileAttenteID, Patient patient, LocalDate dateArrivee, LocalTime heureArrivee, EtatArrive etat, Statut statut) {
        this.fileAttenteID = fileAttenteID;
        this.patient = patient;
        this.dateArrivee = dateArrivee;
        this.heureArrive = heureArrivee;
        this.etat = etat;
        this.statut = statut;
    }

    // Getters et Setters
    public int getFileAttenteID() {
        return fileAttenteID;
    }

    public void setFileAttenteID(int fileAttenteID) {
        this.fileAttenteID = fileAttenteID;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public void setHeureArrive(LocalTime heureArrive) {
        this.heureArrive = heureArrive;
    }

    public LocalTime getHeureArrive() {
        return heureArrive;
    }

    public EtatArrive getEtat() {
        return etat;
    }

    public void setEtat(EtatArrive etat) {
        this.etat = etat;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "FilesAttente{"
                + "fileAttenteID=" + fileAttenteID
                + ", patient=" + patient.toString()
                + ", dateArrivee=" + dateArrivee
                + ", heureArrivee=" + heureArrive
                + ", etat='" + etat + '\''
                + ", statut=" + statut
                + '}';
    }
}
