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
import java.util.List;

public class Consultations {

    private int consultationID;
    private RendezVous rendezVous; // Changement ici pour utiliser RendezVous
    private Patient patient; // Ajout du Patient
    private LocalDate dateConsultation;
    private String symptome;
    private String diagnostique;
    private String cat;
    private double poids;
    private double taille;
    private double imc;
    private int frequencequardiaque;
    // pression artérielle bras gauche (historic field)
    private String pression;
    // pression artérielle bras droit
    private String pressionDroite;
    private int frequencerespiratoire;
    private double glycimie;
    private double temperature;
    private int saO;
    private String examenClinique;
    private List<Prescriptions> prescriptions;


    // Getters and Setters
    public int getConsultationID() {
        return consultationID;
    }

    public void setConsultationID(int consultationID) {
        this.consultationID = consultationID;
    }

    public RendezVous getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(RendezVous rendezVous) {
        this.rendezVous = rendezVous;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDate dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getSymptome() {
        return symptome;
    }

    public void setSymptome(String symptome) {
        this.symptome = symptome;
    }

    public String getDiagnostique() {
        return diagnostique;
    }

    public void setDiagnostique(String diagnostique) {
        this.diagnostique = diagnostique;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public double getTaille() {
        return taille;
    }

    public void setTaille(double taille) {
        this.taille = taille;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public int getFrequencequardiaque() {
        return frequencequardiaque;
    }

    public void setFrequencequardiaque(int frequencequardiaque) {
        this.frequencequardiaque = frequencequardiaque;
    }

    public String getPression() {
        return pression;
    }

    public void setPression(String pression) {
        this.pression = pression;
    }

    public String getPressionDroite() {
        return pressionDroite;
    }

    public void setPressionDroite(String pressionDroite) {
        this.pressionDroite = pressionDroite;
    }

    public int getFrequencerespiratoire() {
        return frequencerespiratoire;
    }

    public void setFrequencerespiratoire(int frequencerespiratoire) {
        this.frequencerespiratoire = frequencerespiratoire;
    }

    public double getGlycimie() {
        return glycimie;
    }

    public void setGlycimie(double glycimie) {
        this.glycimie = glycimie;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getSaO() {
        return saO;
    }

    public void setSaO(int saO) {
        this.saO = saO;
    }

    public String getExamenClinique() {
        return examenClinique;
    }

    public void setExamenClinique(String examenClinique) {
        this.examenClinique = examenClinique;
    }
   
    public List<Prescriptions> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescriptions> prescriptions) {
        this.prescriptions = prescriptions;
    }

    @Override
    public String toString() {
        return "Consultation{"
                + "consultationID=" + consultationID
                + ", rendezVous=" + rendezVous
                + ", patient=" + patient
                + ", dateConsultation=" + dateConsultation
                + ", symptome='" + symptome + '\''
                + ", diagnostique='" + diagnostique + '\''
                + ", cat='" + cat + '\''
                + ", poids=" + poids
                + ", taille=" + taille
                + ", imc=" + imc
                + ", frequencequardiaque=" + frequencequardiaque
                + ", pressionGauche=" + pression
                + ", pressionDroite=" + pressionDroite
                + ", frequencerespiratoire=" + frequencerespiratoire
                + ", glycimie=" + glycimie
                + ", temperature=" + temperature
                + ", saO=" + saO
                + '}';
    }
}
