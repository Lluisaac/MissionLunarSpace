package com.acpi.mls.missionlunarspace.DAO.activity;

import com.acpi.mls.missionlunarspace.ChoixPersoActivity;
import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.ScoreFinalActivity;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOScoreFinal extends DAO {

    private ScoreFinalActivity activity;
    private ArrayList<String> classementPerso;
    private ArrayList<String> classementGroupe;
    private ArrayList<String> classementClasse;

    public DAOScoreFinal(ScoreFinalActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        getClassement("Classe", Integer.parseInt(strings[0]));
        getClassement("Groupe", Integer.parseInt(strings[1]));
        getClassement("Etudiant", Integer.parseInt(strings[2]));

        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        ArrayList<String>[] tab = new ArrayList[3];
        tab[0] = classementPerso;
        tab[1] = classementGroupe;
        tab[2] = classementClasse;
        activity.mettreScores(tab);
    }

    private void getClassement(String table, int idEtudiant) {
        try {
            ArrayList<String> classementTemp = new ArrayList<>();
            PreparedStatement pst = cn.prepareStatement("SELECT nomObjet, position FROM ?s tb JOIN Classement? cl ON tb.id? = cl.id? JOIN Objets ob ON cl.idObjet = ob.idObjet WHERE tb.id? = ? ORDER BY position ASC;");
            for (int i = 1; i < 6; i++) {
                pst.setString(i, table);
            }
            pst.setInt(6, idEtudiant);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                classementTemp.add(rs.getString(1));
            }

            mettreClassement(classementTemp, table);
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
        }
    }

    private void mettreClassement(ArrayList<String> classementTemp, String table) {
        switch (table) {
            case "Classe":
                classementClasse = classementTemp;
                break;
            case "Groupe":
                classementGroupe = classementTemp;
                break;
            case "Etudiant":
                classementPerso = classementTemp;
                break;
        }
    }
}
