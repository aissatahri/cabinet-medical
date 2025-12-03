/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public enum Statut {
    EN_ATTENTE("En attente"),
    EN_CONSULTATION("En Consultation"),
    TERMINE("Terminé");

    private final String displayName;

    Statut(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
