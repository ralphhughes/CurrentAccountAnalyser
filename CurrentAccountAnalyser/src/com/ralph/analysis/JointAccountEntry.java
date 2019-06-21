/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ralph.analysis;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jopendocument.dom.spreadsheet.Cell;
import org.jopendocument.dom.spreadsheet.Sheet;

/**
 *
 * @author Ralph
 */
public class JointAccountEntry {


    // Define the current layout of the spreadsheet
    private Integer rowNum;     // Primary key comes from source spreadsheet
    private static final Integer dateColIndex = 0;              private Date date;
    private static final Integer bankDescriptionColIndex = 2;   private String bankDescription;
    private static final Integer moneyInOutColIndex = 3;        private BigDecimal moneyIn;
                                                                private BigDecimal moneyOut;
    
    private static final Integer balanceColIndex = 4;           private BigDecimal balance;
    private static final Integer myDescriptionColIndex = 8;     private String myDescription;

    
    public static final String equivalentDDLSQL = "create table if not exists Natwest ("
            + "rowNum integer primary key, "
            + "date text, "
            + "bankDescription text, "
            + "moneyIn numeric, "
            + "moneyOut numeric, "
            + "balance numeric, "
            + "myDescription text"
            + ");";
    
    public static JointAccountEntry rowToAccountEntry(Sheet sheet, Integer rowNum) {
        JointAccountEntry entry = new JointAccountEntry();
        
        entry.setRowNum(rowNum);
        
        Cell cell;
        
        cell = sheet.getCellAt(dateColIndex, rowNum);
        if (cell.isEmpty() || cell.getTextValue().length() == 0) {
            entry.setDate(null);
        } else {
            Object obj = cell.getValue();
            entry.setDate((Date) obj);
        }
        
        
        Object bankDescription = sheet.getValueAt(bankDescriptionColIndex, rowNum);
        entry.setBankDescription(bankDescription.toString());
        
        cell = sheet.getCellAt(moneyInOutColIndex, rowNum);
        if (cell.isEmpty() || cell.getTextValue().length() ==0) {
            entry.setMoneyIn(null);
        } else {
            String str = cell.getTextValue();
            str = removeCurrencyNotation(str);
            BigDecimal moneyInOut = new BigDecimal(str);
            if (moneyInOut.floatValue() < 0) {
                entry.setMoneyOut(moneyInOut);
            } else {
                entry.setMoneyIn(moneyInOut);
            }
        }
        
        
        
        
        cell = sheet.getCellAt(balanceColIndex, rowNum);
        if (cell.isEmpty() || cell.getTextValue().length() ==0) {
            entry.setBalance(null);
        } else {
            String str = cell.getTextValue();
            str = removeCurrencyNotation(str);
            entry.setBalance(new BigDecimal(str));
        }
        
        cell = sheet.getCellAt(myDescriptionColIndex, rowNum);
        if (cell.isEmpty() || cell.getTextValue().length() == 0) {
            entry.setMyDescription(null);
        } else {
            entry.setMyDescription(cell.getTextValue());
        }
        
        
        return entry;
    }
    
    private static String removeCurrencyNotation(String str) {
        str = str.replace("£", "");
        str = str.replace(",", "");
        return str;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return rowNum + "\t" + sdf.format(date) + "\t" + bankDescription + "\t" + moneyIn + "\t" + moneyOut + "\t" + myDescription;
    }
    // Simple getters and setters only below here
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(BigDecimal moneyIn) {
        this.moneyIn = moneyIn;
    }

    public BigDecimal getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(BigDecimal moneyOut) {
        this.moneyOut = moneyOut;
    }

    public String getMyDescription() {
        return myDescription;
    }

    public void setMyDescription(String myDescription) {
        this.myDescription = myDescription;
    }

    /**
     * @return the bankDescription
     */
    public String getBankDescription() {
        return bankDescription;
    }

    /**
     * @param bankDescription the bankDescription to set
     */
    public void setBankDescription(String bankDescription) {
        this.bankDescription = bankDescription;
    }
    /**
     * @return the bankDescriptionColIndex
     */
    public static Integer getBankDescriptionColIndex() {
        return bankDescriptionColIndex;
    }

    /**
     * @return the myDescriptionColIndex
     */
    public static Integer getMyDescriptionColIndex() {
        return myDescriptionColIndex;
    }

    
    /**
     * @return the dateColIndex
     */
    public static Integer getDateColIndex() {
        return dateColIndex;
    }

    /**
     * @return the balanceColIndex
     */
    public static Integer getBalanceColIndex() {
        return balanceColIndex;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getRowNum() {
        return rowNum;
    }
    
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
}
