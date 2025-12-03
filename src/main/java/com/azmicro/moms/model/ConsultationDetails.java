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

public class ConsultationDetails {
    private String nomPatient;
    private String prenomPatient;
    private LocalDate dateConsultation;
    private String statut;
    private int idPaiement;
    private Double montant;
    private Double montantReste;
    private Double montantVersement;
    private ModePaiement modePaiement;
    private Boolean etatPaiement;
    private int consultationID;  // Ajouté ici

    // Getters and Setters
    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public String getPrenomPatient() {
        return prenomPatient;
    }

    public void setPrenomPatient(String prenomPatient) {
        this.prenomPatient = prenomPatient;
    }

    public LocalDate getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDate dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getMontantReste() {
        return montantReste;
    }

    public void setMontantReste(Double montantReste) {
        this.montantReste = montantReste;
    }

    public Double getMontantVersement() {
        return montantVersement;
    }

    public void setMontantVersement(Double montantVersement) {
        this.montantVersement = montantVersement;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Boolean getEtatPaiement() {
        return etatPaiement;
    }

    public void setEtatPaiement(Boolean etatPaiement) {
        this.etatPaiement = etatPaiement;
    }

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    @Override
    public String toString() {
        return "ConsultationDetails{" + "nomPatient=" + nomPatient + ", prenomPatient=" + prenomPatient + ", dateConsultation=" + dateConsultation + ", statut=" + statut + ", idPaiement=" + idPaiement + ", montant=" + montant + ", montantReste=" + montantReste + ", montantVersement=" + montantVersement + ", modePaiement=" + modePaiement + ", etatPaiement=" + etatPaiement + '}';
    }

    public int getConsultationID() {
        return consultationID;
    }

    public void setConsultationID(int consultationID) {
        this.consultationID = consultationID;
    }
 
    
}

