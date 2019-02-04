package com.acpi.mls.missionlunarspace;
import java.sql.*;

public class ConnexionBD {
        private Connection cn;
        private Statement st;
        private ResultSet rs;


        public Connection getCn() {
            return cn;
        }


        public void setCn(Connection cn) {
            this.cn = cn;
        }


        public Statement getSt() {
            return st;
        }

        public void setSt(Statement st) {
            this.st = st;
        }

        public ResultSet getRs() {
            return rs;
        }


        public void setRs(ResultSet rs) {
            this.rs = rs;
        }


        public ConnexionBD() {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                cn = DriverManager.getConnection("jdbc:mysql://localhost/missionLunarSpace","root","");
                st = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = st.executeQuery("");
                //si avertissement SQL
                if (rs.getWarnings() != null) System.out.println(rs.getWarnings().getMessage());
            }
            //si la classe n'existe pas
            catch(ClassNotFoundException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            //si erreur dans BDD
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }
