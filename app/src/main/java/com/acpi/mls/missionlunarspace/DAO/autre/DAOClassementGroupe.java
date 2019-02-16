package com.acpi.mls.missionlunarspace.DAO.autre;

import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOClassementGroupe extends DAO {
    private ChoixGroupeActivity choixGroupeActivity;
    private ArrayList<String> classementGroup;

    public DAOClassementGroupe(ChoixGroupeActivity choixGroupeActivity) {
        this.choixGroupeActivity = choixGroupeActivity;
        this.classementGroup = new ArrayList<>();
    }


    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getClassementGroupe":
                tab[0] = getClassementGroupe(strings[2], strings[3]);
                break;
        }
        return tab;
    }


    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getClassementGroupe":
                choixGroupeActivity.classementEgal(this.classementGroup);
                break;
        }
    }

    private String getClassementGroupe(String idGroupe, String phaseStr){
        try {

            int phase = Integer.parseInt(phaseStr);
            PreparedStatement pst = cn.prepareStatement("SELECT nomObjet FROM ClassementGroupe cgt JOIN Objets obj ON cgt.idObjet = obj.idObjet WHERE position > ? AND position <= ? AND idGroupe = ? ORDER BY position");
            pst.setInt(1, phase * 5);
            pst.setInt(2, phase * 5 + 5);
            pst.setString(3, idGroupe);
            ResultSet rs = pst.executeQuery();

            for (int i = 0; i < 5; i++) {
                if (rs.next())
                    this.classementGroup.add(rs.getString(1));
            }

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
        return "getClassementGroupe";
    }


}
