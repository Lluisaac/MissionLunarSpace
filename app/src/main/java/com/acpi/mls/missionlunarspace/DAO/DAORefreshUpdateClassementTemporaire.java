package com.acpi.mls.missionlunarspace.DAO;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAORefreshUpdateClassementTemporaire extends DAO {

    private String precedent;
    private boolean continuer = true;
    private ChoixGroupeActivity choixGroupeActivity;

    public DAORefreshUpdateClassementTemporaire(ChoixGroupeActivity choixGroupeActivity) {
        this.precedent = "";
        this.choixGroupeActivity = choixGroupeActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        while(continuer) {
            faireCN();
            String str = getDifferences(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
            publishProgress(str);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... result) {
        if (!"".equals(result[0]) && !precedent.equals(result[0])) {
            precedent = result[0];
            choixGroupeActivity.afficherPopupTechnicien(result[0]);
        }
    }

    public void arreter() {
        continuer = false;
    }

    private String getDifferences(int idGroupe, int phase) {
        try {
            System.out.println("-------------JE PASSE---------------");
            PreparedStatement pst = cn.prepareStatement("SELECT position, nomObjet, idGroupe FROM ClassementGroupeTemp cgt JOIN Objets obj ON cgt.idObjet = obj.idObjet WHERE position > ? AND position <= ? AND idGroupe = ? ORDER BY position");
            pst.setInt(1, phase * 5);
            pst.setInt(2, phase * 5 + 5);
            pst.setInt(3, idGroupe);
            ResultSet rs = pst.executeQuery();

            String str = "";

            for (int i = 0; i < 5; i++) {
                rs.next();
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
