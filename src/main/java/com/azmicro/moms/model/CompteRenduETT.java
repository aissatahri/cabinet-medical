package com.azmicro.moms.model;

import java.time.LocalDate;

/**
 * Modèle représentant un compte rendu d'Échocardiographie Transthoracique (ETT)
 */
public class CompteRenduETT {
    private int id;
    private Consultations consultation;
    private Patient patient;
    private String contenu;
    private LocalDate dateCreation;

    public CompteRenduETT() {
        this.dateCreation = LocalDate.now();
    }

    public CompteRenduETT(Consultations consultation, Patient patient, String contenu) {
        this.consultation = consultation;
        this.patient = patient;
        this.contenu = contenu;
        this.dateCreation = LocalDate.now();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Consultations getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultations consultation) {
        this.consultation = consultation;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "CompteRenduETT{" +
                "id=" + id +
                ", consultation=" + consultation +
                ", patient=" + patient +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
