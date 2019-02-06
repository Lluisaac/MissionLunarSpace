package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.EnseignantActivity;

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

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getMdpProf":
                tab[0] = getMdpProf();
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
