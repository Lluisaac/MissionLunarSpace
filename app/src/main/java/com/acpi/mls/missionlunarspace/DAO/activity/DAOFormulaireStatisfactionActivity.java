package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.FormulaireSatisfactionActivity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOFormulaireStatisfactionActivity extends DAO {
    private FormulaireSatisfactionActivity formulaireSatisfactionActivity;

    public DAOFormulaireStatisfactionActivity(FormulaireSatisfactionActivity formulaireSatisfactionActivity) {
        this.formulaireSatisfactionActivity = formulaireSatisfactionActivity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        String[] tab = {"", strings[1]};
        switch (strings[0]) {
            case "getIdClasse":
                saveForm(strings[2]);
                break;
        }
        return tab;
    }

    private void saveForm(String idEtudiant)
    {
        try {
            PreparedStatement preparedStatement = cn.prepareStatement("INSERT INTO FormulaireEtudiant (idEtudiant,idObjet,position) VALUES (?,?,?)");
            preparedStatement.setString(1, idEtudiant);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }





}
