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
import java.time.LocalTime;

public class Disponibilites {
    private int disponibiliteID;
    private Medecin medecin;
    private Jours jours;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public Disponibilites() {}

    public Disponibilites(int disponibiliteID, Medecin medecin, Jours jours, LocalTime heureDebut, LocalTime heureFin) {
        this.disponibiliteID = disponibiliteID;
        this.medecin = medecin;
        this.jours = jours;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public int getDisponibiliteID() {
        return disponibiliteID;
    }

    public void setDisponibiliteID(int disponibiliteID) {
        this.disponibiliteID = disponibiliteID;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Jours getJours() {
        return jours;
    }

    public void setJours(Jours jours) {
        this.jours = jours;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    @Override
    public String toString() {
        return "Disponibilites{" + "disponibiliteID=" + disponibiliteID + ", medecin=" + medecin + ", jours=" + jours + ", heureDebut=" + heureDebut + ", heureFin=" + heureFin + '}';
    }

   

    
}

