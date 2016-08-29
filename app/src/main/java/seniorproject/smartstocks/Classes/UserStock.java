package seniorproject.smartstocks.Classes;

import java.io.IOException;
import java.math.BigDecimal;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Created by Ampirollli on 8/8/2016.
 */
public class UserStock {

    Stock StockSymbol;
    Integer Quantity;
    Integer TransactionID;
    Integer AccountID;

    public String getStockSymbol() {
        return StockSymbol.getSymbol();
    }

    public Stock getStock() {
        return StockSymbol;
    }

    public void setStock(String symbol) throws IOException {
        StockSymbol = YahooFinance.get(symbol);
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public Integer getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(Integer transactionID) {
        TransactionID = transactionID;
    }

    public Integer getAccountID() {
        return AccountID;
    }

    public void setAccountID(Integer accountID) {
        AccountID = accountID;
    }



    public UserStock() {

    }
}
