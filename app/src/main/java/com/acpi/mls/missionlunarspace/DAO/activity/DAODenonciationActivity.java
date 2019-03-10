package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DenonciationActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAODenonciationActivity extends DAO {

    private DenonciationActivity denonciationActivity;
    private ArrayList<String> listeNomEtudiant;

    public DAODenonciationActivity(DenonciationActivity denonciationActivity) {
        this.denonciationActivity = denonciationActivity;
        this.listeNomEtudiant = new ArrayList<>();
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getNomEtudiantsGroupe":
                getNomEtudiantsGroupe(strings[2]);
                break;
            case "getSaboteur":
                tab[0] = getSaboteur(strings[2]);
                break;
            case "getExpert":
                tab[0] = getExpert(strings[2]);
                break;
            case "saveEnquete":
                saveEnquete(strings[2],strings[3],strings[4]);
                break;
        }
        return tab;
    }


    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getNomEtudiantsGroupe":
                this.denonciationActivity.getNomEudiantsGroupe(this.listeNomEtudiant);
                break;
            case "getSaboteur":
                this.denonciationActivity.getSaboteur(result[0]);
                break;
            case "getExpert":
                this.denonciationActivity.getExpert(result[0]);
                break;
        }
    }

    private void getNomEtudiantsGroupe(String idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT nomEtudiant FROM Etudiants  WHERE groupe = ?  AND role > 2 ORDER BY nomEtudiant;");
            pst.setString(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.listeNomEtudiant.add(rs.getString(1));
            }
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    private String getSaboteur(String idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT nomEtudiant FROM Etudiants  WHERE groupe = ?  AND role = 4;");
            pst.setString(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
                return rs.getString(1);
            return null;
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private String getExpert(String idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT nomEtudiant FROM Etudiants  WHERE groupe = ?  AND role = 3;");
            pst.setString(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
                return rs.getString(1);
            return null;
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private void saveEnquete(String idGroupe, String resultatSaboteur, String resultatExpert){
        try {
            PreparedStatement pst = cn.prepareStatement("UPDATE Groupes SET expertTrouve = ? , saboteurTrouve = ? WHERE idGroupe = ?");
            pst.setInt(1, Integer.parseInt(resultatExpert));
            pst.setInt(2, Integer.parseInt(resultatSaboteur));
            pst.setInt(3, Integer.parseInt(idGroupe));
            pst.executeUpdate();
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }
}
