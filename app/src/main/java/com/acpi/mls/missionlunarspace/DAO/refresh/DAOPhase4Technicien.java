package com.acpi.mls.missionlunarspace.DAO.refresh;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOPhase4Technicien extends DAO {

    private ArrayList<String> liste;
    private boolean continuer = true;
    private ChoixGroupeActivity choixGroupeActivity;

    public DAOPhase4Technicien(ChoixGroupeActivity choixGroupeActivity) {
        this.choixGroupeActivity = choixGroupeActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {

        while(continuer) {
            faireCN();
            if (verifierPhase4(Integer.parseInt(strings[0]))) {
                arreter();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        String[] tab = {classementTemp(Integer.parseInt(strings[0]))};
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        choixGroupeActivity.afficherPopupTechnicienPhase4(result[0]);
    }

    private boolean verifierPhase4(int idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT COUNT(idGroupe) FROM MouvementPhase4 WHERE idGroupe = ? AND enAttenteReponse = 1");
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1) == 1;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return false;
        }
    }

    private String classementTemp(int idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT position, nomObjet, idGroupe FROM ClassementGroupeTemp cgt JOIN Objets obj ON cgt.idObjet = obj.idObjet WHERE idGroupe = ? ORDER BY position");
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();

            String str = "";

            for (int i = 0; i < 15; i++) {
                if (rs.next())
                    str += rs.getString(1) + ": " + rs.getString(2) + "\n";
            }

            return str;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return "";
        }
    }

    public void arreter() {
        continuer = false;
    }
}
