package com.acpi.mls.missionlunarspace.DAO.refresh;

import android.view.View;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.DenonciationActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAORefreshEnquete extends DAO {

    private DenonciationActivity denonciationActivity;
    private int idGroupe;

    /*
     * Donner en paramètre l'arrayList qui doit être modifié
     * Il faut conserver l'objet crée pour faire myDAO.arreter() quand l'utilisation est terminée
     * Il faut faire myDAO.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,monIdDeGroupe) pour un bon fonctionnement
     * /!\ Cela ne va pas modifier l'affichage d'une quelconque façon
     */
    public DAORefreshEnquete(DenonciationActivity denonciationActivity,String idGroupe) {
        this.denonciationActivity = denonciationActivity;
        this.idGroupe = Integer.parseInt(idGroupe);
    }

    private boolean isPartieDemaree(int idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT expertTrouve, saboteurTrouve FROM Groupes WHERE idGroupe = ?");
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();

            rs.next();
            rs.getString(2);
            if (rs.wasNull()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        while (!isPartieDemaree(idGroupe)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        denonciationActivity.afficherButtonFin();
    }
}