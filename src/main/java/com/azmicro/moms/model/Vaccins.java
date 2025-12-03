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

public class Vaccins {
    private int vaccinID;
    private int patientID;
    private String nomVaccin;
    private LocalDate dateVaccination;
    private LocalDate prochainRappel;

    public Vaccins() {}

    public Vaccins(int vaccinID, int patientID, String nomVaccin, LocalDate dateVaccination, LocalDate prochainRappel) {
        this.vaccinID = vaccinID;
        this.patientID = patientID;
        this.nomVaccin = nomVaccin;
        this.dateVaccination = dateVaccination;
        this.prochainRappel = prochainRappel;
    }

    public int getVaccinID() {
        return vaccinID;
    }

    public void setVaccinID(int vaccinID) {
        this.vaccinID = vaccinID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getNomVaccin() {
        return nomVaccin;
    }

    public void setNomVaccin(String nomVaccin) {
        this.nomVaccin = nomVaccin;
    }

    public LocalDate getDateVaccination() {
        return dateVaccination;
    }

    public void setDateVaccination(LocalDate dateVaccination) {
        this.dateVaccination = dateVaccination;
    }

    public LocalDate getProchainRappel() {
        return prochainRappel;
    }

    public void setProchainRappel(LocalDate prochainRappel) {
        this.prochainRappel = prochainRappel;
    }

    @Override
    public String toString() {
        return "Vaccins{" +
                "vaccinID=" + vaccinID +
                ", patientID=" + patientID +
                ", nomVaccin='" + nomVaccin + '\'' +
                ", dateVaccination=" + dateVaccination +
                ", prochainRappel=" + prochainRappel +
                '}';
    }
}

