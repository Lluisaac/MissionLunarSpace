package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.ChoixClasseActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOChoixClasseActivity extends DAO {
    private ChoixClasseActivity choixClasseActivity;
    private ArrayList<String> classementGroup;

    public DAOChoixClasseActivity(ChoixClasseActivity choixClasseActivity) {
        this.choixClasseActivity = choixClasseActivity;
        this.classementGroup = new ArrayList<>();
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getAllClassementGroupe":
                getAllClassementGroupe(strings[2]);
                break;
            case "saveClassementClasse":
                saveClassementClasse(strings[2], strings[3], strings[4]);
                break;
            case "deleteClassementClasse":
                deleteClassementClasse(strings[2]);
                break;
            case "getIdClasse":
                tab[0] = getIdClasse(strings[2]);
                break;
        }
        return tab;
    }


    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getAllClassementGroupe":
                choixClasseActivity.initClasssementGroupe(this.classementGroup);
                break;
            case "getIdClasse":
                choixClasseActivity.setIdClasse(result[0]);
                break;
        }
    }


    private void getAllClassementGroupe(String idGroupe) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT nomObjet FROM ClassementGroupe cgt JOIN Objets obj ON cgt.idObjet = obj.idObjet WHERE idGroupe = ? ORDER BY position");
            pst.setString(1, idGroupe);
            ResultSet rs = pst.executeQuery();

            while (rs.next())
                this.classementGroup.add(rs.getString(1));


        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    private String getIdClasse(String idGroupe) {
        try {
            PreparedStatement preparedStatement = cn.prepareStatement("SELECT classe FROM Groupes WHERE idGroupe = ?");
            preparedStatement.setString(1, idGroupe);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private void deleteClassementClasse(String idClasse) {
        try {
            PreparedStatement preparedStatement = cn.prepareStatement("DELETE FROM ClassementClasse WHERE idClasse = ? ");
            preparedStatement.setString(1, idClasse);
            preparedStatement.execute();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }


    private void saveClassementClasse(String idClasse, String idObjet, String position) {
        try {
            PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO ClassementClasse (idClasse,idObjet,position) VALUES (?,?,?)");
            preparedStatement.setString(1, idClasse);
            preparedStatement.setString(2, idObjet);
            preparedStatement.setString(3, position);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }
}


