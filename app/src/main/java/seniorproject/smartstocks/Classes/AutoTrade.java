package seniorproject.smartstocks.Classes;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ampirollli on 8/14/2016.
 */
public class AutoTrade {
    
    Integer User_id;
    Integer Account_id;
    String StockSymbol;
    Integer StockQuantity;
    Date AutoTradeTime;
    
    public AutoTrade(){
        
    }

    public AutoTrade(Integer user_id, Integer account_id, String stockSymbol, Integer stockQuantity, Date autoTradeTime) {
        User_id = user_id;
        Account_id = account_id;
        StockSymbol = stockSymbol;
        StockQuantity = stockQuantity;
        AutoTradeTime = autoTradeTime;
    }


    public Integer getUser_id() {
        return User_id;
    }

    public void setUser_id(Integer user_id) {
        User_id = user_id;
    }

    public Integer getAccount_id() {
        return Account_id;
    }

    public void setAccount_id(Integer account_id) {
        Account_id = account_id;
    }

    public String getStockSymbol() {
        return StockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        StockSymbol = stockSymbol;
    }

    public Integer getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        StockQuantity = stockQuantity;
    }

    public Date getAutoTradeTime() {
        return AutoTradeTime;
    }

    public void setAutoTradeTime(Date autoTradeTime) {
        AutoTradeTime = autoTradeTime;
    }
    
}
