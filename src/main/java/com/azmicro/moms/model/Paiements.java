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

public class Paiements {
    private int paiementID;
    private Consultations consultation;
    private double montant;
    private double versment; // Nouveau champ
    private double reste; // Nouveau champ
    private LocalDate datePaiement;
    private ModePaiement modePaiement; // Type changé en ModePaiement
    private boolean etatPayment;

    public Paiements() {}

    public Paiements(int paiementID, Consultations consultation, double montant, double versment, double reste, LocalDate datePaiement, ModePaiement modePaiement, boolean etatPayment) {
        this.paiementID = paiementID;
        this.consultation = consultation;
        this.montant = montant;
        this.versment = versment;
        this.reste = reste;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
        this.etatPayment = etatPayment;
    }

    public int getPaiementID() {
        return paiementID;
    }

    public void setPaiementID(int paiementID) {
        this.paiementID = paiementID;
    }

    public Consultations getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultations consultation) {
        this.consultation = consultation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getVersment() {
        return versment;
    }

    public void setVersment(double versment) {
        this.versment = versment;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public boolean isEtatPayment() {
        return etatPayment;
    }

    public void setEtatPayment(boolean etatPayment) {
        this.etatPayment = etatPayment;
    }

    @Override
    public String toString() {
        return "Paiements{" +
                "paiementID=" + paiementID +
                ", consultation=" + consultation +
                ", montant=" + montant +
                ", versment=" + versment +
                ", reste=" + reste +
                ", datePaiement=" + datePaiement +
                ", modePaiement=" + modePaiement +
                ", etatPayment=" + etatPayment +
                '}';
    }
}
