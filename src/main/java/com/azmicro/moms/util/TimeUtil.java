/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Aissa
 */
public class TimeUtil {
    
    // Format pour l'heure (HH:mm:ss)
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Format pour la date (dd-MM-yyyy)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Méthode pour obtenir l'heure actuelle formatée
    public static String getCurrentTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    // Méthode pour obtenir la date actuelle formatée
    public static String getCurrentDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }
    
}
