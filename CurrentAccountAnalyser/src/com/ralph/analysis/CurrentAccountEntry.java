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
public class CurrentAccountEntry {


    // Define the current layout of the spreadsheet
    private Integer rowNum;     // Primary key comes from source spreadsheet
    private static final Integer dateColIndex = 0;              private Date date;
    private static final Integer bankDescriptionColIndex = 2;   private String bankDescription;
    private static final Integer moneyInColIndex = 4;           private BigDecimal moneyIn;
    private static final Integer moneyOutColIndex = 5;          private BigDecimal moneyOut;
    private static final Integer balanceColIndex = 6;           private BigDecimal balance;
    private static final Integer myDescriptionColIndex = 7;     private String myDescription;
    private static final Integer myCategoryColIndex = 8;        private String myCategory;
    
    public static final String equivalentDDLSQL = "create table if not exists Santander ("
            + "rowNum integer primary key, "
            + "date text, "
            + "bankDescription text, "
            + "moneyIn numeric, "
            + "moneyOut numeric, "
            + "balance numeric, "
            + "myDescription text, "
            + "category text"
            + ");";
    
    public static CurrentAccountEntry rowToAccountEntry(Sheet sheet, Integer rowNum) {
        CurrentAccountEntry entry = new CurrentAccountEntry();
        
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
        
        cell = sheet.getCellAt(moneyInColIndex, rowNum);
        if (cell.isEmpty() || cell.getTextValue().length() ==0) {
            entry.setMoneyIn(null);
        } else {
            String str = cell.getTextValue();
            str = removeCurrencyNotation(str);
            entry.setMoneyIn(new BigDecimal(str));
        }
        
        
        cell = sheet.getCellAt(moneyOutColIndex, rowNum);
        if (cell.isEmpty() || cell.getTextValue().length() ==0) {
            entry.setMoneyOut(null);
        } else {
            String str = cell.getTextValue();
            str = removeCurrencyNotation(str);
            entry.setMoneyOut(new BigDecimal(str));
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
        
        cell = sheet.getCellAt(myCategoryColIndex, rowNum);
        if (cell.isEmpty() || cell.getTextValue().length() == 0) {
            entry.setMyCategory(null);
        } else {
            entry.setMyCategory(cell.getTextValue());
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
        return rowNum + "\t" + sdf.format(date) + "\t" + bankDescription + "\t" + moneyIn + "\t" + moneyOut + "\t" + myDescription + "\t" + myCategory;
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
     * @return the myCategory
     */
    public String getMyCategory() {
        return myCategory;
    }

    /**
     * @param myCategory the myCategory to set
     */
    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }

    /**
     * @return the moneyOutColIndex
     */
    public static Integer getMoneyOutColIndex() {
        return moneyOutColIndex;
    }

    /**
     * @return the moneyInColIndex
     */
    public static Integer getMoneyInColIndex() {
        return moneyInColIndex;
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
     * @return the myCategoryColIndex
     */
    public static Integer getMyCategoryColIndex() {
        return myCategoryColIndex;
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
