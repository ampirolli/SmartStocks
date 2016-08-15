package seniorproject.smartstocks.Classes;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;

/**
 * Created by Ampirollli on 8/8/2016.
 */
public class Transaction {

    BigDecimal Profit;
    BigDecimal PricePaid;
    String TransactionType;
    Date TransactionDate;
    Stock StockSymbol;
    Integer StockQuantity;

    public BigDecimal getProfit() {
        return Profit;
    }

    public void setProfit(BigDecimal profit) {
        Profit = profit;
    }

    public BigDecimal getPricePaid() {
        return PricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        PricePaid = pricePaid;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public Date getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        TransactionDate = transactionDate;
    }

    public Stock getStockSymbol() {
        return StockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        StockSymbol = new Stock(stockSymbol);
    }

    public Integer getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        StockQuantity = stockQuantity;
    }



}
