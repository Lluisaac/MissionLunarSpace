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

    public DAODenonciationActivity(DenonciationActivity choixClasseActivity) {
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
        }
        return tab;
    }


    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getNomEtudiantsGroupe":
                this.denonciationActivity.getNomEudiantsGroupe(this.listeNomEtudiant);
                break;
        }
    }

    private void getNomEtudiantsGroupe(String idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT nomEtudiant FROM Etudiants  WHERE groupe = ?  AND role > 2;");
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
}
