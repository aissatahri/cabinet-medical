/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.azmicro.moms.model;

import java.time.DayOfWeek;

/**
 *
 * @author Aissa
 */
public enum Jours {
    LUNDI("Lundi"),
    MARDI("Mardi"),
    MERCREDI("Mercredi"),
    JEUDI("Jeudi"),
    VENDREDI("Vendredi"),
    SAMEDI("Samedi"),
    DIMANCHE("Dimanche");

    private final String jour;

    // Constructeur
    Jours(String jour) {
        this.jour = jour;
    }

    // Getter
    public String getJour() {
        return jour;
    }

    // Méthode toString pour obtenir la représentation textuelle
    @Override
    public String toString() {
        return jour;
    }
    public static String convertDayOfWeekToJours(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
        case MONDAY:
            return "LUNDI";
        case TUESDAY:
            return "MARDI";
        case WEDNESDAY:
            return "MERCREDI";
        case THURSDAY:
            return "JEUDI";
        case FRIDAY:
            return "VENDREDI";
        case SATURDAY:
            return "SAMEDI";
        case SUNDAY:
            return "DIMANCHE";
        default:
            throw new IllegalArgumentException("Jour inconnu: " + dayOfWeek);
    }
}
}

