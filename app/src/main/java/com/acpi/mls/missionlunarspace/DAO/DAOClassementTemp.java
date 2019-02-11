package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;

import java.sql.PreparedStatement;
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
                saveClassementTemp(strings[2]);
                break;

        }

        return tab;
    }

    private String finIdObjet(String nomObjet){
        String ret = "";
        int i = 0;
        boolean trouve = false;
        while( i < ChoixPersoActivity.listObjets.length && !trouve)
        {
            if(nomObjet.equals(ChoixPersoActivity.listObjets[i]))
            {
                trouve = true;
                ret = (i+1) + "";
            }
            i++;
        }
        return  ret;
    }

    private void saveClassementTemp(String idGroupe) {
        int pos;
        try {
            for (int i = 0; i < 5; i++) {

                pos = 5 * this.phase + (i + 1);

                PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO ClassementGroupeTemp (idGroupe,idObjet,position) VALUES (?,?,?)");
                preparedStatement.setString(1, idGroupe);
                preparedStatement.setString(2, finIdObjet(this.classementTemp.get(i)));
                preparedStatement.setString(3, pos + "");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }

    }
}
