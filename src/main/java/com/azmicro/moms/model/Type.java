/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public enum Type {
    MEDICAL("Médical"),
    CHIRURGICAL("Chirurgical"),
    FAMILIAL("Familial"),
    SOCIAL("Social"),
    AUTRE("Autre");

    private final String description;

    Type(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    public String getDescription() {
        return description;
    }

}
