package seniorproject.smartstocks.Classes;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Ampirollli on 8/8/2016.
 */
public class Order {
    BigDecimal Profit;
    BigDecimal PricePaid;
    String OrderType;
    Date OrderDate;
    UserStock StockSymbol;
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

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    public String getStockSymbol() {
        return StockSymbol.getStockSymbol();
    }

    public void setStockSymbol(String stockSymbol) throws IOException {
        StockSymbol = new UserStock();
        StockSymbol.setStock(stockSymbol);
    }

    public Integer getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        StockQuantity = stockQuantity;
    }

}
