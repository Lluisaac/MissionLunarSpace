package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOEnseignant extends DAO {


    private final EnseignantActivity monEnseignant;

    public DAOEnseignant(EnseignantActivity ea) {
        this.monEnseignant = ea;
    }

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

    public void createClasse(String nom, int annee) {
        try {
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT id, nomClasse, anneeClasse FROM Classes");
            rs.last();
            int nb = rs.getInt(1);
            rs.moveToInsertRow();
            rs.updateInt(1, nb + 1);
            rs.updateString(2, nom);
            rs.updateInt(3, annee);
            rs.insertRow();
            //createGroupes();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
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
                monEnseignant.validerMDP(result[0]);
                break;
        }
    }
}
