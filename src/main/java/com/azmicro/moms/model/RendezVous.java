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

public class RendezVous {
    private int rendezVousID;
    private Patient patient; // Utilisation de l'objet Patient
    private Medecin medecin; // Utilisation de l'objet Medecin
    private LocalDate date;
    private LocalTime hourStart;
    private LocalTime hourEnd;
    private String titre;
    private String desc;

    public RendezVous() {}

    public RendezVous(int rendezVousID, Patient patient, Medecin medecin, LocalDate date, LocalTime hourStart, LocalTime hourEnd, String titre, String desc) {
        this.rendezVousID = rendezVousID;
        this.patient = patient;
        this.medecin = medecin;
        this.date = date;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.titre = titre;
        this.desc = desc;
    }

    public int getRendezVousID() {
        return rendezVousID;
    }

    public Patient getPatient() {
        return patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHourStart() {
        return hourStart;
    }

    public LocalTime getHourEnd() {
        return hourEnd;
    }

    public String getTitre() {
        return titre;
    }

    public String getDesc() {
        return desc;
    }

    public void setRendezVousID(int rendezVousID) {
        this.rendezVousID = rendezVousID;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setHourStart(LocalTime hourStart) {
        this.hourStart = hourStart;
    }

    public void setHourEnd(LocalTime hourEnd) {
        this.hourEnd = hourEnd;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RendezVous{" + "rendezVousID=" + rendezVousID + ", patient=" + patient + ", medecin=" + medecin + ", date=" + date + ", hourStart=" + hourStart + ", hourEnd=" + hourEnd + ", titre=" + titre + ", desc=" + desc + '}';
    }

    
}
