package com.acpi.mls.missionlunarspace.DAO.autre;

import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOClassementTemp extends DAO {

    private List<String> classementTemp;
    private ChoixGroupeActivity choixGroupeActivity;
    private int phase;


    public DAOClassementTemp(ChoixGroupeActivity choixGroupeActivity, ArrayList<String> liste, int phase) {
        this.classementTemp = liste;
        this.choixGroupeActivity = choixGroupeActivity;
        this.phase = phase;
    }


    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "saveClassementTemp":
                if (classementDispo(strings[2])) {
                    saveClassementTemp(strings[2]);
                }
                break;

        }

        return tab;
    }

    private boolean classementDispo(String idGroupe) {
        try {
            PreparedStatement pst1 = cn.prepareStatement("SELECT COUNT(idObjet) FROM ClassementGroupeTemp WHERE idGroupe = ?");
            pst1.setString(1, idGroupe);
            ResultSet rs1 = pst1.executeQuery();
            rs1.next();
            return rs1.getInt(1) == 0;

        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return false;
        }
    }

    private void saveClassementTemp(String idGroupe) {
        int pos;
        try {
            for (int i = 0; i < 5; i++) {

                pos = 5 * this.phase + (i + 1);

                PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO ClassementGroupeTemp (idGroupe,idObjet,position) VALUES (?,?,?)");
                preparedStatement.setString(1, idGroupe);
                preparedStatement.setString(2, ChoixPersoActivity.findIdObjet(this.classementTemp.get(i)));
                preparedStatement.setString(3, pos + "");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }

    }


}
