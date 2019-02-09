package com.acpi.mls.missionlunarspace.DAO;


import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            case "getGroupeEtudiant":
                tab[0] = getGroupeEtudiant(strings[2]);
                break;
            case "getRoleEtudiant":
                tab[0] = getRoleEtudiant(strings[2]);
                break;
            case "getIdGroupeEtudiant":
                tab[0] = getIdGroupeEtudiant(strings[2]);
                break;
            /*case "getClassementEtudiant":
                tab[0] = getClassementEtudiant(strings[2], strings[3]);
                break;*/
        }
        return tab;
    }


    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getGroupeEtudiant":
                monChoixGroupeActivity.setGroup(result[0]);
                break;
            case "getRoleEtudiant":
                monChoixGroupeActivity.setRole(result[0]);
                break;
            case "getIdGroupeEtudiant":
                monChoixGroupeActivity.setIdGroupe(result[0]);
                break;

            //case "getClassementEtudiant":
               // monChoixGroupeActivity.setClassement(result[0]);
                //break;
        }
    }


    private String getIdGroupeEtudiant(String idEtudiant) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT groupe FROM Etudiants  WHERE idEtudiant = ?;");
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

    private String getClassementEtudiant(String idEtudiant, String position) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT nomObjet FROM Objets O JOIN ClassementEtudiant CE ON CE.idObjet = O.idObjet WHERE CE.idEtudiant = ? AND CE.position = ?;");
            pst.setString(1, idEtudiant);
            pst.setString(2, position);
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


    //------------------------------------------------------PREMIERE  PHASE------------------------------------------------------//
                                                    /*Choix avec tous les objets */
                                             /*On enleve les 5 premier objets du classement*/
    //------------------------------------------------------DEUXIEME  PHASE------------------------------------------------------//
                                                     /*On enleve les 5 suivants*/
    //------------------------------------------------------TROISIEME PHASE------------------------------------------------------//
                                                    /*On enleve les 5 suivants*/
    //------------------------------------------------------QUATRIEME PHASE------------------------------------------------------//
            /*Affichage de tous les objets dans l'ordre enregistré et possibilité de faire maximum trois changement*/
}
