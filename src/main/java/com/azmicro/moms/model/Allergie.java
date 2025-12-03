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

public class Allergie {
    private int allergieID;
    private int patientID;
    private String nomAllergie;
    private LocalDate dateDetection;

    public Allergie() {}

    public Allergie(int allergieID, int patientID, String nomAllergie, LocalDate dateDetection) {
        this.allergieID = allergieID;
        this.patientID = patientID;
        this.nomAllergie = nomAllergie;
        this.dateDetection = dateDetection;
    }

    public int getAllergieID() {
        return allergieID;
    }

    public void setAllergieID(int allergieID) {
        this.allergieID = allergieID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getNomAllergie() {
        return nomAllergie;
    }

    public void setNomAllergie(String nomAllergie) {
        this.nomAllergie = nomAllergie;
    }

    public LocalDate getDateDetection() {
        return dateDetection;
    }

    public void setDateDetection(LocalDate dateDetection) {
        this.dateDetection = dateDetection;
    }

    @Override
    public String toString() {
        return "Allergies{" +
                "allergieID=" + allergieID +
                ", patientID=" + patientID +
                ", nomAllergie='" + nomAllergie + '\'' +
                ", dateDetection=" + dateDetection +
                '}';
    }
}

