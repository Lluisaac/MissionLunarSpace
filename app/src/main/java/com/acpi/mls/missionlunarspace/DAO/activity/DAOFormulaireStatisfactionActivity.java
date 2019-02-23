package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.FormulaireSatisfactionActivity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOFormulaireStatisfactionActivity extends DAO {
    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "saveForm":
                saveForm(strings[2],strings[3],strings[4],strings[5],strings[6],strings[7],strings[8]);
                break;
        }
        return tab;
    }

    private void saveForm(String idEtudiant,String motCap,String motTech,String nD, String nL,String nO,String nC)
    {
        try {
            PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO FormSatisfaction (motGestionCapitaine,motGestionTechnicien,noteDecision,noteLeadership,noteOrganisation,noteCommunication,idEtudiant) VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, motCap);
            preparedStatement.setString(2, motTech);
            preparedStatement.setInt(3, Integer.parseInt(nD));
            preparedStatement.setInt(4, Integer.parseInt(nL));
            preparedStatement.setInt(5, Integer.parseInt(nO));
            preparedStatement.setInt(6, Integer.parseInt(nC));
            preparedStatement.setString(7, idEtudiant);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }





}
