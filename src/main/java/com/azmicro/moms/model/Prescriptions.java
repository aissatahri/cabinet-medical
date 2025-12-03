/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public class Prescriptions {
    private int prescriptionID;
    private int consultationID;
    private Medicaments medicament;
    private String dose;
    private String duree;
    private String description;

    public Prescriptions() {}

    public Prescriptions(int prescriptionID, int consultationID, Medicaments medicament, String dose, String duree, String description) {
        this.prescriptionID = prescriptionID;
        this.consultationID = consultationID;
        this.medicament = medicament;
        this.dose = dose;
        this.duree = duree;
        this.description = description;
    }

    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public int getConsultationID() {
        return consultationID;
    }

    public void setConsultationID(int consultationID) {
        this.consultationID = consultationID;
    }

    public Medicaments getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicaments medicament) {
        this.medicament = medicament;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Prescriptions{" + "prescriptionID=" + prescriptionID + ", consultationID=" + consultationID + ", medicament=" + medicament + ", dose=" + dose + ", duree=" + duree + ", description=" + description + '}';
    }

    
    
    
}
