package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOEnseignant extends DAO {


    private EnseignantActivity monEnseignant;
    private static int[][] tabRoles = {{1, 2, 3, 4, 5, 5, 5, 5}, {1, 2, 4, 5, 5, 5, 5, 5}, {1, 2, 5, 5, 5, 5, 5, 5}, {1, 2, 3, 5, 5, 5, 5, 5}};
    private static String strInfoGroupes;

    public DAOEnseignant(EnseignantActivity ea) {
        this.monEnseignant = ea;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1], ""};
        switch (strings[0]) {
            case "DemarrerPartie":
                tab[2] = demarrerPartie(strings[2]);
                int id = Integer.parseInt(strInfoGroupes.split(":")[0]);
                int nbE = Integer.parseInt(strInfoGroupes.split(":")[1]);
                int nbG = Integer.parseInt(strInfoGroupes.split(":")[2]);
                assignerJoueur(id, nbE, nbG);
                break;
            case "getMdpProf":
                tab[0] = getMdpProf();
                break;
            case "createClasse":
                int num = createClasse(strings[2], Integer.parseInt(strings[3]));
                if (num != -1) {
                    strInfoGroupes = createGroupes(num, Integer.parseInt(strings[4]), Integer.parseInt(strings[5]));
                    if (strInfoGroupes == null) {
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
            case "indicationPartieDemarree":
                monEnseignant.indiquerPartieDemaree(result[2]);
                break;
        }
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
            ResultSet rs = st.executeQuery("SELECT idClasse, nomClasse, anneeClasse FROM Classes ORDER BY idClasse");
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

    private String createGroupes(int classe, int nbEleves, int nbGroupes) {
        try {
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT idGroupe, typeGroupe, classe FROM Groupes");
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
            return (der + 1) + ":" + nbEleves + ":" + nbGroupes;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private String demarrerPartie(String classe) {
        try {

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT NOW()");
            rs.last();
            String date = rs.getString(1);

            st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //TODO Refactorer de façon a ce que heureDepart ne soit pas utilisée
            rs = st.executeQuery("SELECT idClasse, heureDepart FROM Classes WHERE idClasse = " + classe);
            rs.last();
            rs.updateString(2, date);
            rs.updateRow();
            return date;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return "";
        }
    }

    private void assignerJoueur(int premierIdClasse, int nbEleves, int nbGroupes) {
        try {
            Statement st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT idEtudiant, groupe, role FROM Etudiants");
            int i = 0;
            while (i < nbEleves && rs.next()) {
                rs.getInt(2);
                if (rs.wasNull()) {
                    rs.updateInt(2, premierIdClasse + (i % nbGroupes));
                    rs.updateInt(3, tabRoles[i % nbGroupes][i / nbGroupes]);
                    rs.updateRow();
                    i++;
                }
            }
        } catch (SQLException e) {

            deconnexion();
            e.printStackTrace();
        }
    }
}
