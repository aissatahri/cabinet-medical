/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.service;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.dao.FilesAttenteDAO;
import com.azmicro.moms.model.FilesAttente;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;

public class FilesAttenteService {
    private final FilesAttenteDAO filesAttenteDAO;

    // Constructeur acceptant une instance de FilesAttenteDAO
    public FilesAttenteService(FilesAttenteDAO filesAttenteDAO) {
        this.filesAttenteDAO = filesAttenteDAO;
    }

    public boolean save(FilesAttente filesAttente) {
        return filesAttenteDAO.save(filesAttente);
    }

    public FilesAttente findById(int fileAttenteID) {
        return filesAttenteDAO.findById(fileAttenteID);
    }

    public List<FilesAttente> findAll() {
        return filesAttenteDAO.findAll();
    }
    
     public List<FilesAttente> findAllByIdPatient(int IdPatient) {
        return filesAttenteDAO.findAllByPatientId(IdPatient);
    }

    public boolean update(FilesAttente filesAttente) {
        return filesAttenteDAO.update(filesAttente);
    }

    public boolean delete(int fileAttenteID) {
        return filesAttenteDAO.delete(fileAttenteID);
    }
    
    public List<FilesAttente> findAll(LocalDate date, LocalTime heureDebut, LocalTime HeureFin,String ...statut){
        return filesAttenteDAO.findAll(date, heureDebut, HeureFin, statut);
    }
    
    public List<FilesAttente> findAll(LocalDate date, LocalTime heureDebut, LocalTime HeureFin,String statut){
        return filesAttenteDAO.findAll(date, heureDebut, HeureFin, statut);
    }
}

