package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOEnseignant extends DAO {


    private EnseignantActivity monEnseignant;

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

    public int createClasse(String nom, int annee) {
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
            return nb + 1;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }

    private int createGroupes(int classe, int nbEleves, int nbGroupes) {
        try {
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT id, type, classe FROM Groupes");
            int der;
            if (!rs.last()) {
                der = 0;
            } else {
                der = rs.getInt(1);
            }
            for (int i = 0; i < nbGroupes; i++) {
                rs.moveToInsertRow();
                rs.updateInt(1, der + 1 + i);
                rs.updateInt(2, i + 1);
                rs.updateInt(3, classe);
                rs.insertRow();
            }
            return 0;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }

    private void demarrerPartie(String classe) {
        try {

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT NOW()");
            rs.last();
            String date = rs.getString(1);

            st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT id, heureDepart FROM Classes WHERE id = " + classe);
            rs.last();
            rs.updateString(2, date);
            rs.updateRow();
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
            case "DemarrerPartie":
                demarrerPartie(strings[2]);
                break;
            case "getMdpProf":
                tab[0] = getMdpProf();
                break;
            case "createClasse":
                int num = createClasse(strings[2], Integer.parseInt(strings[3]));
                if (num != -1) {
                    int num2 = createGroupes(num, Integer.parseInt(strings[4]), Integer.parseInt(strings[5]));
                    if (num2 == -1) {
                        tab[1] = "Erreur a la création";
                    } else {
                        tab[0] = num + "";
                        tab[1] = "Aller a lancer partie";
                    }
                } else {
                    tab[1] = "Erreur a la création";
                }
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
            case "Erreur a la création":
                monEnseignant.erreurALaCreation();
                break;
            case "Aller a lancer partie":
                monEnseignant.allerALancerPartie(result[0]);
                break;
        }
    }
}
