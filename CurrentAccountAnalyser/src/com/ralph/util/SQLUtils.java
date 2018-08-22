/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralph.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Ralph
 */
public class SQLUtils {
        public static ArrayList<String> getColumnNames(Statement stmt, String table) throws SQLException {
        String query = "select * from " + table;
        ResultSet rs = stmt.executeQuery(query);
        if (rs == null) {
            return new ArrayList<String>();
        }
        ArrayList<String> cols = new ArrayList<String>();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();

        // get the column names; column indexes start from 1
        for (int i = 1; i < numberOfColumns + 1; i++) {
            String columnName = rsMetaData.getColumnName(i);
            cols.add(columnName);
        }
        rs.close();
        return cols;
    }

    public static void printResultsFromQuery(ResultSet rs, boolean showColumnNames) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            Integer numColumns = md.getColumnCount();

            if (showColumnNames) {
                // get the column names; column indexes start from 1
                String cols = "";
                for (int i = 1; i <= numColumns; i++) {
                    cols = cols + md.getColumnName(i) + "\t";
                }
                System.out.println("\t" + cols);
            }
            while (rs.next()) {
                String vals = "";
                for (int i = 1; i <= numColumns; i++) {
                    if (rs.getObject(i) != null) {
                        vals = vals + rs.getObject(i).toString() + "\t";
                    } else {
                        vals = vals + "\t";
                    }
                }
                System.out.println("\t" + vals);
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }
    
    
    
}
