package com.acpi.mls.missionlunarspace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {
	private static DAO dao;
	private Connection cn;
	
	private DAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@162.38.222.149:1521:iut";
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
			PreparedStatement pst = cn
					.prepareCall("SELECT mdp FROM mdpProf");
			ResultSet rs = pst.executeQuery();

			rs.first();

			String mdp = rs.getString(1);

			return mdp;
		} catch (SQLException e) {
			deconnexion();
			return null;
		}
	}

	public static synchronized DAO getInstance() {
		if (dao == null) {
			dao = new DAO();
		}
		return dao;
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
