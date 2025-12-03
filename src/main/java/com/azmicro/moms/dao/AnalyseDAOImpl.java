/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.dao;

/**
 *
 * @author Aissa
 */
import com.azmicro.moms.model.Analyse;
import com.azmicro.moms.model.TypeAnalyse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnalyseDAOImpl implements AnalyseDAO {

    private final Connection connection;

    public AnalyseDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Analyse analyse) {
        String sql = "INSERT INTO analyses (AnalyseID, ConsultationID, IdTypeAnalyse, description, DateAnalyse, Resultat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, analyse.getAnalyseID());
            stmt.setInt(2, analyse.getConsultationID());
            stmt.setInt(3, analyse.getTypeAnalyse().getIdTypeAnalyse()); // Updated to get ID from TypeAnalyse
            stmt.setString(4, analyse.getDescription());
            stmt.setDate(5, Date.valueOf(analyse.getDateAnalyse()));
            stmt.setString(6, analyse.getResultat());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Other methods are similar, using analyse.getTypeAnalyse().getIdTypeAnalyse() where needed.
    @Override
    public Analyse findById(int id) {
        String sql = "SELECT a.*, ta.* FROM analyses a JOIN typeanalyse ta ON a.IdTypeAnalyse = ta.idTypeAnalyse WHERE a.AnalyseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TypeAnalyse typeAnalyse = new TypeAnalyse(
                        rs.getInt("idTypeAnalyse"),
                        rs.getString("nomAnalyseFr"),
                        rs.getString("nomAnalyseEn"),
                        rs.getString("description"),
                        rs.getString("codeAnalyseFr"),
                        rs.getString("codeAnalyseEn")
                );
                return new Analyse(
                        rs.getInt("AnalyseID"),
                        rs.getInt("ConsultationID"),
                        typeAnalyse,
                        rs.getString("description"),
                        rs.getDate("DateAnalyse").toLocalDate(),
                        rs.getString("Resultat")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Analyse> findAll() {
        List<Analyse> analyseList = new ArrayList<>();
        String sql = "SELECT a.*, ta.* FROM analyses a JOIN typeanalyse ta ON a.IdTypeAnalyse = ta.idTypeAnalyse";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TypeAnalyse typeAnalyse = new TypeAnalyse(
                        rs.getInt("idTypeAnalyse"),
                        rs.getString("nomAnalyseFr"),
                        rs.getString("nomAnalyseEn"),
                        rs.getString("description"),
                        rs.getString("codeAnalyseFr"),
                        rs.getString("codeAnalyseEn")
                );
                analyseList.add(new Analyse(
                        rs.getInt("AnalyseID"),
                        rs.getInt("ConsultationID"),
                        typeAnalyse,
                        rs.getString("description"),
                        rs.getDate("DateAnalyse").toLocalDate(),
                        rs.getString("Resultat")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analyseList;
    }

    @Override
    public boolean update(Analyse analyse) {
        String sql = "UPDATE analyses SET ConsultationID = ?, IdTypeAnalyse = ?, description = ?, DateAnalyse = ?, Resultat = ? WHERE AnalyseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, analyse.getConsultationID());
            stmt.setInt(2, analyse.getTypeAnalyse().getIdTypeAnalyse()); // Extract idTypeAnalyse
            stmt.setString(3, analyse.getDescription());
            stmt.setDate(4, Date.valueOf(analyse.getDateAnalyse()));
            stmt.setString(5, analyse.getResultat());
            stmt.setInt(6, analyse.getAnalyseID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM analyses WHERE AnalyseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Analyse> findByConsultationId(int consultationId) {
        List<Analyse> analyses = new ArrayList<>();
        String sql = "SELECT a.*, ta.* FROM analyses a JOIN types_analyse ta ON a.IdTypeAnalyse = ta.idTypeAnalyse WHERE a.ConsultationID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, consultationId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TypeAnalyse typeAnalyse = new TypeAnalyse(
                        rs.getInt("idTypeAnalyse"),
                        rs.getString("nom_Analyse_Fr"),
                        rs.getString("nom_Analyse_En"),
                        rs.getString("description"),
                        rs.getString("code_Analyse_Fr"),
                        rs.getString("code_Analyse_En")
                );
                Analyse analyse = new Analyse(
                        rs.getInt("AnalyseID"),
                        rs.getInt("ConsultationID"),
                        typeAnalyse,
                        rs.getString("description"),
                        rs.getDate("DateAnalyse").toLocalDate(),
                        rs.getString("Resultat")
                );

                analyses.add(analyse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return analyses;
    }

}
