/*
 *  Copyright (C) CGG Services (UK) Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Robertson Web Team
 */
package com.ralph.analysis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rhughes
 */
public class ThinSQLLiteWrapper {
    Connection connection = null;
    Statement statement = null;
    
    public ThinSQLLiteWrapper(String SQLLiteDB) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + SQLLiteDB);
            statement = connection.createStatement();
            System.out.println("Connection created");
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
    public Connection getConnection() {
        return connection;
    }
    public ResultSet executeQuery(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void closeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int executeUpdate( String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean execute (String sql) {
        try {
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void closeConnection() {
        try {
            statement.close();
            connection.close();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
