package com.acpi.mls.missionlunarspace.DAO.autre;

import android.support.v7.app.AppCompatActivity;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.EtudiantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAORecupTemps extends DAO {
    private EtudiantActivity activity;

    public DAORecupTemps(EtudiantActivity activity) {
        this.activity = activity;
    }


    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {strings[0].equals("groupe") ? getTempsDebutAvecGroupe(strings[1]) : getTempsDebutAvecClasse(strings[1])};
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        activity.createTimer(result[0]);
    }

    private String getTempsDebutAvecGroupe(String idGroupe) {
        try {
            PreparedStatement pst1 = cn.prepareStatement("SELECT heureDepart, idClasse FROM Classes cl JOIN Groupes gp ON cl.idClasse = gp.idClasse WHERE idGroupe = ?");
            pst1.setInt(1, Integer.parseInt(idGroupe));
            ResultSet rs = pst1.executeQuery();

            rs.last();
            return rs.getString(1);

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return "";
        }
    }

    private String getTempsDebutAvecClasse(String idClasse) {
        try {
            PreparedStatement pst1 = cn.prepareStatement("SELECT heureDepart, idClasse FROM Classes WHERE idClasse = ?");
            pst1.setInt(1, Integer.parseInt(idClasse));
            ResultSet rs = pst1.executeQuery();

            rs.last();
            return rs.getString(1);

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return "";
        }
    }

}
