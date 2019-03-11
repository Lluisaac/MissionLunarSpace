package com.acpi.mls.missionlunarspace.DAO.refresh;

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

    private int etape = 0;

    private boolean sf0 = false;
    private boolean sf1 = false;
    private boolean sf2 = false;
    private boolean sf3 = false;

    public DAOScoreFinal(ScoreFinalActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();

        getClassement("Classe", Integer.parseInt(strings[0]));
        getClassement("Groupe", Integer.parseInt(strings[1]));
        getClassement("Etudiant", Integer.parseInt(strings[2]));

        int etapePrec = -1;

        while (etape != 4) {
            int etapeAct = getEtape(Integer.parseInt(strings[0]));
            if (etapeAct != etapePrec) {
                etapePrec = etapeAct;
                etape = etapeAct;
                publishProgress();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... result) {
        switch (etape) {
            case 0:
                faireSF0();
                break;
            case 1:
                faireSF1();
                break;
            case 2:
                faireSF2();
                break;
            case 3:
                faireSF3();
                break;
            case 4:
                faireSF4();
                break;
        }
    }

    private void recupClassements() {
        ArrayList<String>[] tab = new ArrayList[3];
        tab[0] = classementPerso;
        tab[1] = classementGroupe;
        tab[2] = classementClasse;
        activity.mettreScores(tab);
    }

    private void faireSF0() {
        recupClassements();
        sf0 = true;
    }

    private void faireSF1() {
        if (!sf0) {
            faireSF0();
        }
        activity.afficherPerso();
        sf1 = true;
    }

    private void faireSF2() {
        if (!sf1) {
            faireSF1();
        }
        activity.afficherGroupe();
        sf2 = true;
    }

    private void faireSF3() {
        if (!sf2) {
            faireSF2();
        }
        activity.afficherClasse();
        sf3 = true;
    }

    private void faireSF4() {
        if (!sf3) {
            faireSF3();
        }
        activity.afficherAGEST();
    }

    private int getEtape(int idClasse) {
        try {
            ArrayList<String> classementTemp = new ArrayList<>();
            PreparedStatement pst = cn.prepareStatement("SELECT etapeSF, idClasse FROM Classes WHERE idClasse = ?");
            pst.setInt(1, idClasse);
            ResultSet rs = pst.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (
                SQLException e) {
            deconnexion();
            e.printStackTrace();
            return -1;
        }
    }

    private void getClassement(String table, int id) {
        try {
            ArrayList<String> classementTemp = new ArrayList<>();
            PreparedStatement pst = cn.prepareStatement(("SELECT nomObjet, position FROM ?s tb JOIN Classement? cl ON tb.id? = cl.id? JOIN Objets ob ON cl.idObjet = ob.idObjet WHERE tb.id? = " + id + " ORDER BY position ASC\n").replace("?", table));
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
