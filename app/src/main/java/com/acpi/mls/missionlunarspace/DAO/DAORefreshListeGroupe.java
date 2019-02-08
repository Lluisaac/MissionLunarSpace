package com.acpi.mls.missionlunarspace.DAO;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAORefreshListeGroupe extends DAO {

    private ArrayList<String> liste;
    private boolean continuer = true;
    /*
    * Donner en paramètre l'arrayList qui doit être modifié
    * Il faut conserver l'objet crée pour faire myDAO.arreter() quand l'utilisation est terminée
    * Il faut faire myDAO.execute(monIdDeGroupe) pour un bon fonctionnement
    * /!\ Cela ne va pas modifier l'affichage d'une quelconque façon
     */
    public DAORefreshListeGroupe(ArrayList<String> liste) {
        this.liste = liste;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        while (continuer) {
            liste.clear();
            setClassementGroupe(Integer.parseInt(strings[0]), liste);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }
        return null;
    }

    public void arreter() {
        continuer = false;
    }

    private void setClassementGroupe(int idGroupe, ArrayList<String> classement) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT idGroupe, nomObjet, position FROM Objets ob JOIN ClassementGroupe cg ON ob.idObjet = cg.idObjet WHERE idGroupe = ? ORDER BY position");
            pst.setInt(1, idGroupe);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                classement.add(rs.getString(2));
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }


}
