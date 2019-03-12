package com.acpi.mls.missionlunarspace.DAO.autre;

import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOClassemenCapitaineSecure extends DAO {


    private List<String> classementTemp;
    private ChoixGroupeActivity activity;

    public DAOClassemenCapitaineSecure(ArrayList<String> liste, ChoixGroupeActivity activity) {
        this.classementTemp = liste;
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        int idGroupe = Integer.parseInt(strings[0]);
        int phase = Integer.parseInt(strings[1]);
        classementTemp = getAllClassementGroupe(idGroupe, phase);
        removeAllClassementTemporaire(idGroupe);
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        activity.setClassementRestantCapitaine(classementTemp);
    }

    private List<String> getAllClassementGroupe(int idGroupe, int phase) {
        try {
            int pos = phase + 6;
            PreparedStatement pst = cn.prepareStatement("SELECT nomObjet FROM ClassementGroupe cgt JOIN Objets obj ON cgt.idObjet = obj.idObjet WHERE idGroupe = ? AND position < ? ORDER BY position");
            pst.setInt(1, idGroupe);
            pst.setInt(2, pos);
            ResultSet rs = pst.executeQuery();
            List<String> temp = new ArrayList<>();
            while (rs.next())
                temp.add(rs.getString(1));

            return temp;

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private void removeAllClassementTemporaire(int idGroupe) {
        try {
            PreparedStatement st2 = cn.prepareStatement("DELETE FROM ClassementGroupeTemp WHERE idGroupe = ?");
            st2.setInt(1, idGroupe);
            st2.execute();

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

}
