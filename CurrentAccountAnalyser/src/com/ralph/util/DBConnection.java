package com.ralph.util;

import java.sql.Connection;
import java.sql.DriverManager;
import com.ralph.util.DBType;

public class DBConnection {

    public DBConnection() {
        throw new UnsupportedOperationException();
    }

    public static Connection getConnection(DBType dbType, String server, String dbName, String username, String password) {
        try {
            String driver = "", url = "";
            switch (dbType) {
                case SQLSERVER:
                    driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                    // driver="net.sourceforge.jtds.jdbc.Driver";
                    url = "jdbc:jtds:sqlserver://" + server + ":1433/" + dbName;
                    break;
                case POSTGRESQL:
                    driver = "org.postgresql.Driver";
                    url = "jdbc:postgresql://" + server + ":5432/" + dbName;
                    break;
                case ORACLE:
                    driver = "oracle.jdbc.driver.OracleDriver";
                    url = "jdbc:oracle:thin:@" + server + ":1521:" + dbName;
                    break;
                case MYSQL:
                    driver = "com.mysql.jdbc.Driver";
                    url = "jdbc:mysql://" + server + ":3306/" + dbName;
                    break;
                case ACCESS:
                    driver = "sun.jdbc.odbc.JdbcOdbcDriver";
                    url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + dbName; // dbName should be full file path
                    break;
                case ORACLE_RAC:
                    driver = "oracle.jdbc.driver.OracleDriver";
                    url = "jdbc:oracle:thin:@//" +server + ":1521/" + dbName;
                    break;
                case H2:
                    driver = "org.h2.Driver";
                    url = "jdbc:h2:" + dbName;
                    break;

            }
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
