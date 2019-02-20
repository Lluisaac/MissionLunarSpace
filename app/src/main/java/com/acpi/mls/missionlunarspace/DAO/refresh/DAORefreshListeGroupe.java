package com.acpi.mls.missionlunarspace.DAO.refresh;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAORefreshListeGroupe extends DAO {

    private ArrayList<String> liste;
    private boolean continuer = true;
    private ChoixGroupeActivity choixGroupeActivity;

    /*
     * Donner en paramètre l'arrayList qui doit être modifié
     * Il faut conserver l'objet crée pour faire myDAO.arreter() quand l'utilisation est terminée
     * Il faut faire myDAO.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,monIdDeGroupe) pour un bon fonctionnement
     * /!\ Cela ne va pas modifier l'affichage d'une quelconque façon
     */
    public DAORefreshListeGroupe(ChoixGroupeActivity choixGroupeActivity) {
        this.choixGroupeActivity = choixGroupeActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        while(continuer) {
            faireCN();
            setClassementGroupe(Integer.parseInt(strings[0]));
            publishProgress(strings[0]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... result) {
        choixGroupeActivity.refreshClassementGroupe(liste);
    }

    public void arreter() {
        continuer = false;
    }

    private void setClassementGroupe(int idGroupe) {
        try {
            liste = new ArrayList<>();
            PreparedStatement pst = cn.prepareStatement("SELECT idGroupe, nomObjet, position FROM Objets ob JOIN ClassementGroupe cg ON ob.idObjet = cg.idObjet WHERE idGroupe = ? ORDER BY position");
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                liste.add(rs.getString(2));
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }
}
