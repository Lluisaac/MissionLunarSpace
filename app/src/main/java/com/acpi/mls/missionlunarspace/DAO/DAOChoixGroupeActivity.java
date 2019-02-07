package com.acpi.mls.missionlunarspace.DAO;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOChoixGroupeActivity extends DAO {
    private ChoixGroupeActivity monChoixGroupeActivity;

    public DAOChoixGroupeActivity(ChoixGroupeActivity choixGroupeActivity) {
        this.monChoixGroupeActivity = choixGroupeActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"",strings[1]};
        switch (strings[0]) {

        }
        return tab;
    }

    private String getGroupeEtudiant(String nom, String classe, String annee){
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT G.typeGroupe FROM Groupes G " +
                    "JOIN Etudiants E ON E.idEtudiant = G.idGroupe" +
                    "JOIN Classes C ON C.idClasse = G.idGroupe" +
                    "WHERE E.nomEtudiant = ?" +
                    "AND C.nomClasse = ?" +
                    "AND C.anneeClasse = ?;");
            pst.setString(1, nom);
            pst.setString(2, classe);
            pst.setString(3, annee);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getString(1);

        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }

    }
}
