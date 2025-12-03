/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.model;

/**
 *
 * @author Aissa
 */
public class Acte {
    private int idActe;
    private String nomActe;
    private double prix;

    // Constructeurs
    public Acte() {}

    public Acte(int idActe, String nomActe, double prix) {
        this.idActe = idActe;
        this.nomActe = nomActe;
        this.prix = prix;
    }

    // Getters et Setters
    public int getIdActe() {
        return idActe;
    }

    public void setIdActe(int idActe) {
        this.idActe = idActe;
    }

    public String getNomActe() {
        return nomActe;
    }

    public void setNomActe(String nomActe) {
        this.nomActe = nomActe;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Acte{" +
                "idActe=" + idActe +
                ", nomActe='" + nomActe + '\'' +
                ", prix=" + prix +
                '}';
    }
}

