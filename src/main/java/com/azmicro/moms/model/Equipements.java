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

public class Equipements {
    private int equipementID;
    private String nomEquipement;
    private String description;
    private LocalDate dateAcquisition;

    public Equipements() {}

    public Equipements(int equipementID, String nomEquipement, String description, LocalDate dateAcquisition) {
        this.equipementID = equipementID;
        this.nomEquipement = nomEquipement;
        this.description = description;
        this.dateAcquisition = dateAcquisition;
    }

    public int getEquipementID() {
        return equipementID;
    }

    public void setEquipementID(int equipementID) {
        this.equipementID = equipementID;
    }

    public String getNomEquipement() {
        return nomEquipement;
    }

    public void setNomEquipement(String nomEquipement) {
        this.nomEquipement = nomEquipement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    @Override
    public String toString() {
        return "Equipements{" +
                "equipementID=" + equipementID +
                ", nomEquipement='" + nomEquipement + '\'' +
                ", description='" + description + '\'' +
                ", dateAcquisition=" + dateAcquisition +
                '}';
    }
}

