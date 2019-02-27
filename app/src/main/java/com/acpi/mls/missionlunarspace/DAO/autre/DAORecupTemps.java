package com.acpi.mls.missionlunarspace.DAO.autre;

import com.acpi.mls.missionlunarspace.DAO.DAO;
import com.acpi.mls.missionlunarspace.EtudiantActivity;
import com.acpi.mls.missionlunarspace.Timer;
import com.acpi.mls.missionlunarspace.TimerProf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAORecupTemps extends DAO {
    private EtudiantActivity activity;
    private Timer timer;
    private TimerProf timerProf;

    public DAORecupTemps(EtudiantActivity activity) {
        this.activity = activity;
    }

    public DAORecupTemps(Timer timer) {
        this.timer = timer;
    }

    public DAORecupTemps(TimerProf timerProf) {
        this.timerProf = timerProf;
    }


    @Override
    protected String[] doInBackground(String... strings) {
        faireCN();
        if (!strings[0].equals("simple")) {
            String[] tab = {"NULL"};
            while (tab[0].equals("NULL")) {
                tab[0] = strings[0].equals("groupe") ? getTempsDebutAvecGroupe(strings[1]) : getTempsDebutAvecClasse(strings[1]);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
            return tab;
        } else {
            String[] tab = {"simple", getLastTemps(), strings[1], getTempsDebutAvecClasse(strings[2])};
            return tab;
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result.length == 1) {
            activity.faireTimer(result[0]);
        } else {
            String[] tempsD = result[1].split(" ")[1].split(":");
            String[] temps = result[3].split(" ")[1].split(":");
            if (timerProf == null) {
                timer.postTimeLeft(Integer.parseInt(result[2]), tempsD, temps);
            } else {
                timerProf.postTimeLeft(Integer.parseInt(result[2]), tempsD, temps);
            }
        }
    }

    private String getTempsDebutAvecGroupe(String idGroupe) {
        try {
            PreparedStatement pst1 = cn.prepareStatement("SELECT heureDepart, idClasse FROM Classes cl JOIN Groupes gp ON cl.idClasse = gp.idClasse WHERE idGroupe = ?");
            pst1.setInt(1, Integer.parseInt(idGroupe));
            ResultSet rs = pst1.executeQuery();

            rs.last();
            return rs.getString(1);

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return "";
        }
    }

    private String getLastTemps() {
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT NOW()");
            rs.last();
            String date = rs.getString(1);
            return date;
        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return "";
        }
    }

    private String getTempsDebutAvecClasse(String idClasse) {
        try {
            PreparedStatement pst1 = cn.prepareStatement("SELECT heureDepart, idClasse FROM Classes WHERE idClasse = ?");
            pst1.setInt(1, Integer.parseInt(idClasse));
            ResultSet rs = pst1.executeQuery();

            rs.last();
            return rs.getString(1);

        } catch (SQLException e) {
            deconnexion();
            e.printStackTrace();
            return "";
        }
    }

}
