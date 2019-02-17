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
        }
        return tab;
    }


    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getAllClassementGroupe":
                choixClasseActivity.initClasssementGroupe(this.classementGroup);
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
}
