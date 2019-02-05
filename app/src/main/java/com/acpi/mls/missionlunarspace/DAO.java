package com.acpi.mls.missionlunarspace;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
	protected Connection cn;
	
	public DAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@gloin:1521:iut";
			String login = "lluisi";
			String mdp = "123";

			cn = DriverManager.getConnection(url, login, mdp);

		} catch (Exception e) {
			deconnexion();
			e.printStackTrace();
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
