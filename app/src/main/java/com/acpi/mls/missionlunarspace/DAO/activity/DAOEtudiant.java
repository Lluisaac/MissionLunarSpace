package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.Constantes;
import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.EtudiantActivity;
import com.acpi.mls.missionlunarspace.BtnRoles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Result;


public class DAOEtudiant extends DAO {

    private EtudiantActivity monEtudiant;

    public DAOEtudiant(EtudiantActivity etudiantActivity) {
        this.monEtudiant = etudiantActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getIdClasse":
                tab[0] = getIdClasse(strings[2], strings[3]);
                break;
            case "createEtudiant":
                tab[0] = createEtudiant(strings[2]) + "";
                break;
        }
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        switch (result[1]) {
            case "existClasse":
                monEtudiant.existeClasse(result[0]);
                break;
            case "setIdEtu":
                monEtudiant.faireNouvelEtudiant(result[0]);
                break;
        }
    }

    private String getIdClasse(String nomClasse, String anneeClasse) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT idClasse AS 'NbId' FROM Classes WHERE nomClasse = ? AND anneeClasse = ?;");
            pst.setString(1, nomClasse);
            pst.setString(2, anneeClasse);
            ResultSet rs = pst.executeQuery();

            String id = "";
            if (rs.next()) {
                id = rs.getInt("NbId") + "";
            }
            return id;

        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private int createEtudiant(String nom) {
        try {
            int id;
            PreparedStatement preparedStatement = cn.prepareStatement("SELECT createEtudiant(?)");
            preparedStatement.setString(1, nom);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            id = rs.getInt(1);
            return id;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }
}
