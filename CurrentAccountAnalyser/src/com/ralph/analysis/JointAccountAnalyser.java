/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralph.analysis;

import com.ralph.util.SQLUtils;
import java.awt.Point;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 *
 * @author Ralph
 */
public class JointAccountAnalyser {
    
    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "\tProgram started.");
        final String sourceFilename = "C:\\users\\rhughes\\Google Drive\\Finance\\Joint Account.ods";
        final String SQLLiteFilename = "C:\\users\\rhughes\\Google Drive\\Finance\\Joint Account.sqlite";
        final String categoryRulesFilename = "C:\\users\\rhughes\\Google Drive\\Finance\\categoryRules.sql";
        JointAccountAnalyser ca = new JointAccountAnalyser();
        
        // Import any new rows in Joint Account.ods into the SQLLite DB (wipe the DB first if last parameter is true)
        ca.importSpreadsheetToSql(sourceFilename, SQLLiteFilename, false);
        
        
        ca.categoriseRecurringPayments(SQLLiteFilename, categoryRulesFilename);
        
        //ca.displayUnknownPayments(SQLLiteFilename);
        
        //ca.displayCategoryStatistics(SQLLiteFilename);
        
        System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "\tProgram ended.");
    }
    
    public void displayCategoryStatistics(String SQLLiteDB) {
        ThinSQLLiteWrapper myDB = new ThinSQLLiteWrapper(SQLLiteDB);
        
        myDB.closeConnection();
    }
    public void displayUnknownPayments(String SQLiteDB) {
        String sql = "select * from Santander where category is null";
    }
    
    
    public void categoriseRecurringPayments(String SQLLiteDB, String categoryRulesFilename) {
        try {
            Scanner sc = new Scanner(new File(categoryRulesFilename));
            List<String> lines = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.startsWith("--")) { // Not a comment
                    if (line.length() > 0) { // Not a blank line
                        lines.add(line);
                    }
                }
            }

            String[] categoriseSQL = lines.toArray(new String[0]);
            ThinSQLLiteWrapper myDB = new ThinSQLLiteWrapper(SQLLiteDB);
            for (String currentSQL: categoriseSQL) {
                int rowsUpdated = myDB.executeUpdate(currentSQL);
                System.out.println(currentSQL + "\nRows Updated: " + rowsUpdated + "\n");
            }
            myDB.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Boolean importSpreadsheetToSql(String sourceFilename, String destinationSQLiteDB, boolean eraseDestinationTables) {
        ThinSQLLiteWrapper myDB = new ThinSQLLiteWrapper(destinationSQLiteDB);
        try {
            
            if (eraseDestinationTables) {
                myDB.execute("DROP TABLE IF EXISTS Natwest");
                myDB.execute(JointAccountEntry.equivalentDDLSQL);
                System.out.println("Table dropped and empty table re-created.");
            }
            
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "\tOpening spreadsheet...");
            Sheet sheet = SpreadSheet.createFromFile(new File(sourceFilename)).getSheet("Downloaded Transactions");
            Integer firstRow = 4;
            Integer lastRow = getLastPopulatedRow(sheet);
            
            String insertSQL = "insert or ignore into Natwest values (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement prepStmt = myDB.getConnection().prepareStatement(insertSQL);
                    
            int totalUpdated=0;
            for (Integer rowNum = firstRow; rowNum < lastRow; rowNum++) {
                JointAccountEntry ae = JointAccountEntry.rowToAccountEntry(sheet, rowNum);
                //System.out.println(ae);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                prepStmt.setInt(1, ae.getRowNum());
                prepStmt.setString(2, sdf.format(ae.getDate()));
                prepStmt.setString(3, ae.getBankDescription());
                prepStmt.setBigDecimal(4, ae.getMoneyIn());
                prepStmt.setBigDecimal(5, ae.getMoneyOut());
                prepStmt.setBigDecimal(6, ae.getBalance());
                prepStmt.setString(7, ae.getMyDescription());
                
                prepStmt.addBatch();
              
                // Get the database to run the inserts every 1000 SQL statements
                if (rowNum % 1000 == 0) {
                    int[] updatedRows = prepStmt.executeBatch();
                    for (int current: updatedRows) {
                       totalUpdated += current;
                    }
                }
            }
            // Execute any outstanding inserts (less than a thousand of them)
            int[] updatedRows = prepStmt.executeBatch();
            for (int current: updatedRows) {
                totalUpdated += current;
            }
            
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "\tInserted " + totalUpdated + " rows into database.");
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        myDB.closeConnection();
        return true;
    }

    private Integer getLastPopulatedRow(Sheet sheet) {
        Point endPoint = sheet.getUsedRange().getEndPoint();
        Integer currentRowNum = endPoint.y;
        Boolean populated;
        do {
            populated = false;
            for (int colIndex = 0; colIndex < endPoint.x; colIndex++) {
                Object val = sheet.getCellAt(colIndex, currentRowNum).getValue();
                if (val != null && val.toString().trim().length() > 0) {
                    populated = true;
                    break;
                }
            }
            currentRowNum--;
        } while (populated == false);
        return currentRowNum;
    }

}
