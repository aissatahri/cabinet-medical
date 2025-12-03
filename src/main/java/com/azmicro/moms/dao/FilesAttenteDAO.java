/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.FilesAttente;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface FilesAttenteDAO {

    // Renvoie true si l'enregistrement a réussi
    boolean save(FilesAttente filesAttente);

    // Trouve par ID, peut être laissé tel quel
    FilesAttente findById(int fileAttenteID);

    // Renvoie la liste complète des files d'attente
    List<FilesAttente> findAll();

    // Renvoie true si la mise à jour a réussi
    boolean update(FilesAttente filesAttente);

    // Renvoie true si la suppression a réussi
    boolean delete(int fileAttenteID);

    // Recherche toutes les files d'attente pour un patient donné
    List<FilesAttente> findAllByPatientId(int patientId);

    // Recherche avec filtres de date et heure, plusieurs statuts
    List<FilesAttente> findAll(LocalDate dateJour, LocalTime heureDebut, LocalTime heureFin, String... statuses);

    // Recherche avec un seul statut
    List<FilesAttente> findAll(LocalDate dateJour, LocalTime heureDebut, LocalTime heureFin, String status);
}
