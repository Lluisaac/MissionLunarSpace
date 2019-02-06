package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOEnseignant extends DAO {

    public String getMdpProf() {
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT mdp FROM mdpProf");

            rs.next();
            String mdp = rs.getString(1) + "";
            return mdp;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    public int createClasse(String nom, int annee) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT id, nomClasse, anneeClasse FROM Classes");
            ResultSet rs = pst.executeQuery();
            rs.last();
            System.out.println(rs.getInt(1));
            return 0;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getMdpProf":
                tab[0] = getMdpProf();
                break;
            case "createClasse":
                createClasse(strings[2], Integer.parseInt(strings[3]));
                break;
        }
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "EnseignantActivity.validerMDP":
                EnseignantActivity.validerMDP(result[0]);
                break;
        }
    }
}
