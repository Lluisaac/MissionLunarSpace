package com.acpi.mls.missionlunarspace.DAO;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.acpi.mls.missionlunarspace.EnseignantActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class DAO extends AsyncTask<String, String, String[]> {
	protected static Connection cn;

	public DAO() {}

	protected void faireCN() {
		if (cn == null) {
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
