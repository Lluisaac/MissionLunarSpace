package com.acpi.mls.missionlunarspace.DAO.autre;

import com.acpi.mls.missionlunarspace.ChoixGroupeActivity;
import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAOAugmenterEtapes extends DAO {

    private EnseignantActivity activity;


    public DAOAugmenterEtapes(EnseignantActivity activity) {
        this.activity = activity;
    }


    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        resetTimerDepart(Integer.parseInt(strings[0]));
        incrementerEtape(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
        String[] tab = {strings[1]};
        return tab;
    }

    @Override
    protected void onPostExecute(String[] result) {
        switch (result[0]) {
            case "0":
                //On vient d'augmenter l'étape de la Classe

                break;
            case "1":
                //On vient d'augmenter l'étape du score final

                break;
        }
    }

    private void incrementerEtape(int idClasse, int sf) {
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT etape, etapeSF, idClasse FROM Classes WHERE idClasse = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, idClasse);
            ResultSet rs = pst.executeQuery();
            rs.next();
            rs.updateInt(1 + sf, rs.getInt(1 + sf) + 1);
            rs.updateRow();

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
        }

    }

    private String resetTimerDepart(int classe) {
        try {

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT NOW()");
            rs.last();
            String date = rs.getString(1);

            st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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


}
