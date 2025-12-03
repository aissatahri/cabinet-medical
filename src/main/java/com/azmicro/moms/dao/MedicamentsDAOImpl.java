/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Medicaments;
import com.azmicro.moms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentsDAOImpl implements MedicamentsDAO {

    @Override
    public boolean save(Medicaments medicament) {
        String query = "INSERT INTO medicament (MedicamentID, CODE_EAN_13, nomMedicament, denominationCommuneInternationale, formeDosage, Presentation, prixPublicVentePPV, prixBaseRemboursementPPV, prixHospitalierPH, prixBaseRemboursementPH, classeTherapeutique, princepsGenerique) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, medicament.getMedicamentID());
            ps.setInt(2, medicament.getCodeEAN13());
            ps.setString(3, medicament.getNomMedicament());
            ps.setString(4, medicament.getDenominationCommuneInternationale());
            ps.setString(5, medicament.getFormeDosage());
            ps.setString(6, medicament.getPresentation());
            ps.setDouble(7, medicament.getPrixPublicVentePPV());
            ps.setDouble(8, medicament.getPrixBaseRemboursementPPV());
            ps.setDouble(9, medicament.getPrixHospitalierPH());
            ps.setDouble(10, medicament.getPrixBaseRemboursementPH());
            ps.setString(11, medicament.getClasseTherapeutique());
            ps.setString(12, medicament.getPrincepsGenerique());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Medicaments medicament) {
        String query = "UPDATE medicament SET CODE_EAN_13 = ?, nomMedicament = ?, denominationCommuneInternationale = ?, formeDosage = ?, Presentation = ?, prixPublicVentePPV = ?, prixBaseRemboursementPPV = ?, prixHospitalierPH = ?, prixBaseRemboursementPH = ?, classeTherapeutique = ?, princepsGenerique = ? WHERE MedicamentID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, medicament.getCodeEAN13());
            ps.setString(2, medicament.getNomMedicament());
            ps.setString(3, medicament.getDenominationCommuneInternationale());
            ps.setString(4, medicament.getFormeDosage());
            ps.setString(5, medicament.getPresentation());
            ps.setDouble(6, medicament.getPrixPublicVentePPV());
            ps.setDouble(7, medicament.getPrixBaseRemboursementPPV());
            ps.setDouble(8, medicament.getPrixHospitalierPH());
            ps.setDouble(9, medicament.getPrixBaseRemboursementPH());
            ps.setString(10, medicament.getClasseTherapeutique());
            ps.setString(11, medicament.getPrincepsGenerique());
            ps.setInt(12, medicament.getMedicamentID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Medicaments medicament) {
        String query = "DELETE FROM medicament WHERE MedicamentID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, medicament.getMedicamentID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Medicaments findById(int medicamentID) {
        String query = "SELECT * FROM medicament WHERE MedicamentID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, medicamentID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicaments(
                            rs.getInt("MedicamentID"),
                            rs.getInt("CODE_EAN_13"),
                            rs.getString("nomMedicament"),
                            rs.getString("denominationCommuneInternationale"),
                            rs.getString("formeDosage"),
                            rs.getString("Presentation"),
                            rs.getDouble("prixPublicVentePPV"),
                            rs.getDouble("prixBaseRemboursementPPV"),
                            rs.getDouble("prixHospitalierPH"),
                            rs.getDouble("prixBaseRemboursementPH"),
                            rs.getString("classeTherapeutique"),
                            rs.getString("princepsGenerique")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Medicaments> findAll() {
        List<Medicaments> medicamentsList = new ArrayList<>();
        String query = "SELECT * FROM medicament";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicaments medicament = new Medicaments(
                        rs.getInt("MedicamentID"),
                        rs.getInt("CODE_EAN_13"),
                        rs.getString("nomMedicament"),
                        rs.getString("denominationCommuneInternationale"),
                        rs.getString("formeDosage"),
                        rs.getString("Presentation"),
                        rs.getDouble("prixPublicVentePPV"),
                        rs.getDouble("prixBaseRemboursementPPV"),
                        rs.getDouble("prixHospitalierPH"),
                        rs.getDouble("prixBaseRemboursementPH"),
                        rs.getString("classeTherapeutique"),
                        rs.getString("princepsGenerique")
                );
                medicamentsList.add(medicament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicamentsList;
    }

    @Override
    public Medicaments findByNameAndFormeDosage(String nomMedicament, String formeDosage) {
        String query = "SELECT * FROM medicament WHERE nomMedicament = ? AND formeDosage = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomMedicament);
            ps.setString(2, formeDosage);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicaments(
                            rs.getInt("MedicamentID"),
                            rs.getInt("CODE_EAN_13"),
                            rs.getString("nomMedicament"),
                            rs.getString("denominationCommuneInternationale"),
                            rs.getString("formeDosage"),
                            rs.getString("Presentation"),
                            rs.getDouble("prixPublicVentePPV"),
                            rs.getDouble("prixBaseRemboursementPPV"),
                            rs.getDouble("prixHospitalierPH"),
                            rs.getDouble("prixBaseRemboursementPH"),
                            rs.getString("classeTherapeutique"),
                            rs.getString("princepsGenerique")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
public List<Medicaments> findByKeyword(String keyword) {
    List<Medicaments> medicamentsList = new ArrayList<>();
    String query = "SELECT * FROM medicament WHERE nomMedicament LIKE ? OR denominationCommuneInternationale LIKE ?";
    
    try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
        // Utiliser le mot-clé pour rechercher des correspondances partielles
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicaments medicament = new Medicaments(
                    rs.getInt("MedicamentID"),
                    rs.getInt("CODE_EAN_13"),
                    rs.getString("nomMedicament"),
                    rs.getString("denominationCommuneInternationale"),
                    rs.getString("formeDosage"),
                    rs.getString("Presentation"),
                    rs.getDouble("prixPublicVentePPV"),
                    rs.getDouble("prixBaseRemboursementPPV"),
                    rs.getDouble("prixHospitalierPH"),
                    rs.getDouble("prixBaseRemboursementPH"),
                    rs.getString("classeTherapeutique"),
                    rs.getString("princepsGenerique")
                );
                medicamentsList.add(medicament);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return medicamentsList;
}


}
