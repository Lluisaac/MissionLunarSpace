package com.acpi.mls.missionlunarspace.DAO;

import com.acpi.mls.missionlunarspace.Constantes;
import com.acpi.mls.missionlunarspace.EtudiantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DAOEtudiant extends DAO {

    private EtudiantActivity monEtudiant;
    private BtnRoles monBoutonRoles;

    public DAOEtudiant(EtudiantActivity etudiantActivity) {
        this.monEtudiant = etudiantActivity;
    }

    public DAOEtudiant(BtnRoles bouton) {
        this.monBoutonRoles = bouton;
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
            case "getTypeEtudiant":
                tab[0] = getTypeEtudiant(Integer.parseInt(strings[2])) + "";
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
                Constantes.idEtudiant = Integer.parseInt(result[0]);
                break;
            case "setRole":
                monBoutonRole.setRole(Integer.parseInt(result[0]));
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

    private int getTypeEtudiant(int id) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT idEtudiant, role FROM Etudiants WHERE idEtudiant = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            int role = -1;
            if (rs.next()) {
                id = rs.getInt("role");
            }
            return role;

        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }

    private int createEtudiant(String nom) {
        try {
            int id = getNextIdEtudiant();
            PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO Etudiants (nomEtudiant) VALUES (?)");
            preparedStatement.setString(1, nom);
            preparedStatement.executeUpdate();
            return id;
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }

    private int getLastIdEtudiant() {
        return getNextIdEtudiant() - 1;
    }

    private int getNextIdEtudiant() {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'lluisi' AND TABLE_NAME = 'Etudiants'");
            ResultSet rs = pst.executeQuery();
            return rs.getInt(1);
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }
}
