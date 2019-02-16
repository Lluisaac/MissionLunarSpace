package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOChoixPersoActivity extends DAO {

    private ChoixPersoActivity monChoixPerso;

    public DAOChoixPersoActivity(ChoixPersoActivity monChoixPerso) {
        this.monChoixPerso = monChoixPerso;
    }


    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "saveClassementObjet":
                saveClassementObjet(strings[2], strings[3], strings[4]);
                break;
            case "getIdEtudiant":
                tab[0] = getIdEtudiant(strings[2], strings[3], strings[4]);
                break;
        }
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "getIdEtudiant":
                monChoixPerso.setIdEtudiant(result[0]);
                break;
        }
    }

    private String getIdEtudiant(String nom, String classe, String annee) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT E.idEtudiant FROM Groupes G JOIN Etudiants E ON E.groupe = G.idGroupe JOIN Classes C ON C.idClasse = G.classe WHERE E.nomEtudiant = ? AND C.nomClasse = ? AND C.anneeClasse = ?;");
            pst.setString(1, nom);
            pst.setString(2, classe);
            pst.setString(3, annee);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }


    private void saveClassementObjet(String idEtudiant, String idObjet, String position) {
        try {
            PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO ClassementEtudiant (idEtudiant,idObjet,position) VALUES (?,?,?)");
            preparedStatement.setString(1, idEtudiant);
            preparedStatement.setString(2, idObjet);
            preparedStatement.setString(3, position);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }
}
