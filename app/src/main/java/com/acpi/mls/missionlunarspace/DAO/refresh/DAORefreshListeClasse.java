package com.acpi.mls.missionlunarspace.DAO.refresh;


import com.acpi.mls.missionlunarspace.ChoixClasseActivity;
import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAORefreshListeClasse extends DAO {

    private ArrayList<String> liste;
    private boolean continuer = true;
    private ChoixClasseActivity choixClasseActivity;

    /*
     * Donner en paramètre l'arrayList qui doit être modifié
     * Il faut conserver l'objet crée pour faire myDAO.arreter() quand l'utilisation est terminée
     * Il faut faire myDAO.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,monIdDeGroupe) pour un bon fonctionnement
     * /!\ Cela ne va pas modifier l'affichage d'une quelconque façon
     */
    public DAORefreshListeClasse(ChoixClasseActivity choixClasseActivity) {
        this.choixClasseActivity = choixClasseActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        while(continuer) {
            faireCN();
            setClassementClasse(Integer.parseInt(strings[0]));
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
        choixClasseActivity.refreshClassementClasse(liste);
    }

    public void arreter() {
        continuer = false;
    }

    private void setClassementClasse(int idClasse) {
        try {
            liste = new ArrayList<>();
            PreparedStatement pst = cn.prepareStatement("SELECT idClasse, nomObjet, position FROM Objets ob JOIN ClassementClasse cc ON ob.idObjet = cc.idObjet WHERE idClasse = ? ORDER BY position");
            pst.setInt(1, idClasse);
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
