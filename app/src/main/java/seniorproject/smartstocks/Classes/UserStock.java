package seniorproject.smartstocks.Classes;

import java.math.BigDecimal;

import yahoofinance.Stock;

/**
 * Created by Ampirollli on 8/8/2016.
 */
public class UserStock {

    Stock StockSymbol;
    Integer Quantity;
    Integer TransactionID;

    public BigDecimal getPricePaid() {
        return PricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        PricePaid = pricePaid;
    }

    public String getStockSymbol() {
        return StockSymbol.getSymbol();
    }

    public Stock getStock() {
        return StockSymbol;
    }

    public void setStock(String symbol) {
        StockSymbol = new Stock(symbol);
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

    BigDecimal PricePaid;

    public UserStock() {

    }
}
