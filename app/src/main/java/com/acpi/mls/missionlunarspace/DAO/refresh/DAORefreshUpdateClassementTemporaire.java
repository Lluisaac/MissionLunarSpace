package com.acpi.mls.missionlunarspace.DAO.refresh;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAORefreshUpdateClassementTemporaire extends DAO {

    private String precedent;
    private boolean continuer = true;
    private ChoixGroupeActivity choixGroupeActivity;
    private int phase;

    public DAORefreshUpdateClassementTemporaire(ChoixGroupeActivity choixGroupeActivity) {
        this.precedent = "";
        this.choixGroupeActivity = choixGroupeActivity;
        this.phase = 0;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        while (continuer) {
            faireCN();
            String str = getDifferences(Integer.parseInt(strings[0]));

            if (!"".equals(str) && !precedent.equals(str)) {
                if (str.split("\n").length == 5) {
                    precedent = str;
                    publishProgress(str);
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... result) {
            choixGroupeActivity.afficherPopupTechnicien(result[0]);
    }

    public void arreter() {
        continuer = false;
    }

    public void incrementPhase(){
        this.phase ++;
    }

    private String getDifferences(int idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT position, nomObjet, idGroupe FROM ClassementGroupeTemp cgt JOIN Objets obj ON cgt.idObjet = obj.idObjet WHERE position > ? AND position <= ? AND idGroupe = ? ORDER BY position");
            pst.setInt(1, this.phase * 5);
            pst.setInt(2, this.phase * 5 + 5);
            pst.setInt(3, idGroupe);
            ResultSet rs = pst.executeQuery();

            String str = "";

            for (int i = 0; i < 5; i++) {
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
}
