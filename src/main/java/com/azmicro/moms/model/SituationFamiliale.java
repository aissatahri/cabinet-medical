/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public enum SituationFamiliale {
    CELIBATAIRE("Célibataire"),
    MARIE("Marié"),
    DIVORCE("Divorcé"),
    VEUF("Veuf");

    private final String description;

    private SituationFamiliale(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}

