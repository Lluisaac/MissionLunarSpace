package com.acpi.mls.missionlunarspace.DAO;
import com.acpi.mls.missionlunarspace.EtudiantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
            case "createEtudiant" :
                createEtudiant(strings[2]);
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
        }
    }

    private String getIdClasse(String nomClasse, String anneeClasse) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT COUNT(idClasse) AS 'NbId' FROM Classes WHERE nomClasse = ? AND anneeClasse = ?;");
            pst.setString(1, nomClasse);
            pst.setString(2, anneeClasse);
            ResultSet rs = pst.executeQuery();

            String id = "";
            rs.next();
            if ( rs.getInt("NbId") > 0)
                id = "exist";

            return id;

        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return null;
        }
    }

    private void createEtudiant(String nom){
        try {
            PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO Etudiants (nomEtudiant) VALUES (?)");
            preparedStatement.setString(1,nom);
            preparedStatement.executeUpdate();
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }
}
