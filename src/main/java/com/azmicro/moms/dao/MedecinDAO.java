/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Medecin;
import java.sql.SQLException;
import java.util.List;

public interface MedecinDAO {

    boolean createMedecin(Medecin medecin);

    Medecin readMedecin(int medecinID) throws SQLException;

    List<Medecin> readAllMedecins()  throws SQLException;

    boolean updateMedecin(Medecin medecin);

    boolean deleteMedecin(int medecinID);
}
