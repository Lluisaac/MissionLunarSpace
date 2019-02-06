package com.acpi.mls.missionlunarspace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO extends AsyncTask<String, Void, String[]> {
	private static Connection cn;


	public DAO() {}

	private void faireCN() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://162.38.222.147:3306/lluisi";
			String login = "lluisi";
			String mdp = "123";

			cn = DriverManager.getConnection(url, login, mdp);

		} catch (Exception e) {
			deconnexion();
			e.printStackTrace();
		}
	}

	public String getMdpProf() {
		try {
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery("SELECT mdp FROM mdpProf");

			rs.next();
			String mdp = rs.getString(1) + "";
			return mdp;
		} catch (SQLException e) {
			deconnexion();
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected String[] doInBackground(String... strings) {
		if (cn == null) {
			faireCN();
		}
		String[] tab = {"", strings[1]};
		switch (strings[0]) {
			case "getMdpProf":
				tab[0] = getMdpProf();
				break;
		}
		return tab;
	}

	@Override
	protected void onPostExecute(String[] result) {
		switch (result[1]) {
			case "EnseignantActivity.validerMDP":
				EnseignantActivity.validerMDP(result[0]);
				break;
		}
	}

	public void deconnexion() {
		if (cn != null) {
			try {
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
