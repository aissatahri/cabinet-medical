/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public class Medicaments {
    private int medicamentID;
    private int codeEAN13;
    private String nomMedicament;
    private String denominationCommuneInternationale;
    private String formeDosage;
    private String presentation;
    private double prixPublicVentePPV;
    private double prixBaseRemboursementPPV;
    private double prixHospitalierPH;
    private double prixBaseRemboursementPH;
    private String classeTherapeutique;
    private String princepsGenerique;

    public Medicaments() {}

    public Medicaments(int medicamentID, int codeEAN13, String nomMedicament, String denominationCommuneInternationale, String formeDosage, String presentation, double prixPublicVentePPV, double prixBaseRemboursementPPV, double prixHospitalierPH, double prixBaseRemboursementPH, String classeTherapeutique, String princepsGenerique) {
        this.medicamentID = medicamentID;
        this.codeEAN13 = codeEAN13;
        this.nomMedicament = nomMedicament;
        this.denominationCommuneInternationale = denominationCommuneInternationale;
        this.formeDosage = formeDosage;
        this.presentation = presentation;
        this.prixPublicVentePPV = prixPublicVentePPV;
        this.prixBaseRemboursementPPV = prixBaseRemboursementPPV;
        this.prixHospitalierPH = prixHospitalierPH;
        this.prixBaseRemboursementPH = prixBaseRemboursementPH;
        this.classeTherapeutique = classeTherapeutique;
        this.princepsGenerique = princepsGenerique;
    }

    // Getters and Setters

    public int getMedicamentID() {
        return medicamentID;
    }

    public void setMedicamentID(int medicamentID) {
        this.medicamentID = medicamentID;
    }

    public int getCodeEAN13() {
        return codeEAN13;
    }

    public void setCodeEAN13(int codeEAN13) {
        this.codeEAN13 = codeEAN13;
    }

    public String getNomMedicament() {
        return nomMedicament;
    }

    public void setNomMedicament(String nomMedicament) {
        this.nomMedicament = nomMedicament;
    }

    public String getDenominationCommuneInternationale() {
        return denominationCommuneInternationale;
    }

    public void setDenominationCommuneInternationale(String denominationCommuneInternationale) {
        this.denominationCommuneInternationale = denominationCommuneInternationale;
    }

    public String getFormeDosage() {
        return formeDosage;
    }

    public void setFormeDosage(String formeDosage) {
        this.formeDosage = formeDosage;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public double getPrixPublicVentePPV() {
        return prixPublicVentePPV;
    }

    public void setPrixPublicVentePPV(double prixPublicVentePPV) {
        this.prixPublicVentePPV = prixPublicVentePPV;
    }

    public double getPrixBaseRemboursementPPV() {
        return prixBaseRemboursementPPV;
    }

    public void setPrixBaseRemboursementPPV(double prixBaseRemboursementPPV) {
        this.prixBaseRemboursementPPV = prixBaseRemboursementPPV;
    }

    public double getPrixHospitalierPH() {
        return prixHospitalierPH;
    }

    public void setPrixHospitalierPH(double prixHospitalierPH) {
        this.prixHospitalierPH = prixHospitalierPH;
    }

    public double getPrixBaseRemboursementPH() {
        return prixBaseRemboursementPH;
    }

    public void setPrixBaseRemboursementPH(double prixBaseRemboursementPH) {
        this.prixBaseRemboursementPH = prixBaseRemboursementPH;
    }

    public String getClasseTherapeutique() {
        return classeTherapeutique;
    }

    public void setClasseTherapeutique(String classeTherapeutique) {
        this.classeTherapeutique = classeTherapeutique;
    }

    public String getPrincepsGenerique() {
        return princepsGenerique;
    }

    public void setPrincepsGenerique(String princepsGenerique) {
        this.princepsGenerique = princepsGenerique;
    }

    @Override
    public String toString() {
        return "Medicaments{" +
                "medicamentID=" + medicamentID +
                ", codeEAN13=" + codeEAN13 +
                ", nomMedicament='" + nomMedicament + '\'' +
                ", denominationCommuneInternationale='" + denominationCommuneInternationale + '\'' +
                ", formeDosage='" + formeDosage + '\'' +
                ", presentation='" + presentation + '\'' +
                ", prixPublicVentePPV=" + prixPublicVentePPV +
                ", prixBaseRemboursementPPV=" + prixBaseRemboursementPPV +
                ", prixHospitalierPH=" + prixHospitalierPH +
                ", prixBaseRemboursementPH=" + prixBaseRemboursementPH +
                ", classeTherapeutique='" + classeTherapeutique + '\'' +
                ", princepsGenerique='" + princepsGenerique + '\'' +
                '}';
    }
}

