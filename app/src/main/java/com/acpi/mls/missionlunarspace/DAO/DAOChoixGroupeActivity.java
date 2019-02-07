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
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getIdEtudiant":
                tab[0] = getIdEtudiant(strings[2], strings[3], strings[4]);
                break;
            case "getGroupeEtudiant":
                tab[0] = getGroupeEtudiant(strings[2]);
                break;
            case "getRoleEtudiant":
                tab[0] = getRoleEtudiant(strings[2]);
                break;
        }
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getIdEtudiant":
                monChoixGroupeActivity.setIdEtudiant(result[0]);
                break;
            case "getGroupeEtudiant":
                monChoixGroupeActivity.setGroup(result[0]);
                break;
            case "getRoleEtudiant":
                monChoixGroupeActivity.setRole(result[0]);
                break;
        }
    }

    private String getIdEtudiant(String nom, String classe, String annee) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT E.idEtudiant FROM Groupes G JOIN Etudiants E ON E.groupe = G.idGroupe JOIN Classes C ON C.idClasse = G.classe WHERE E.nomEtudiant = ? AND C.nomClasse = ? AND C.anneeClasse = ?;");
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

    private String getGroupeEtudiant(String idEtudiant) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT typeGroupe FROM Groupes G JOIN Etudiants E ON E.groupe = G.idGroupe WHERE E.idEtudiant = ?;");
            pst.setString(1, idEtudiant);
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

    private String getRoleEtudiant(String idEtudiant) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT nomRole FROM Roles R JOIN Etudiants E ON E.role = R.idRole WHERE E.idEtudiant = ?;");
            pst.setString(1, idEtudiant);
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
