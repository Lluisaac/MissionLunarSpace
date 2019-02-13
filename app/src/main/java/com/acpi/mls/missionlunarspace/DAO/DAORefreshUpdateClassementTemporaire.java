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

    /*
     * Donner en paramètre l'arrayList qui doit être modifié
     * Il faut conserver l'objet crée pour faire myDAO.arreter() quand l'utilisation est terminée
     * Il faut faire myDAO.execute(monIdDeGroupe) pour un bon fonctionnement
     * /!\ Cela ne va pas modifier l'affichage d'une quelconque façon
     */
    public DAORefreshUpdateClassementTemporaire(ChoixGroupeActivity choixGroupeActivity) {
        this.precedent = "";
        this.choixGroupeActivity = choixGroupeActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        while(continuer) {
            faireCN();
            publishProgress(getDifferences(Integer.parseInt(strings[0])));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... result) {
        //choixGroupeActivity.afficherPopup(result[0]);
    }

    public void arreter() {
        continuer = false;
    }

    private String getDifferences(int phase) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT ");
            //pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                //classement.add(rs.getString(2));
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
        return "";
    }
}
