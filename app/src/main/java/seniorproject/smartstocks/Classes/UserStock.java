package seniorproject.smartstocks.Classes;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import seniorproject.smartstocks.LICS;
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

    public BigDecimal getPricePaid(){
        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;

        BigDecimal pricePaid = new BigDecimal(0);

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.

            String SQL = "execute sp_get_price_paid '"+ TransactionID +"';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {

                pricePaid = result.getBigDecimal("price_paid");
            }



        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (result != null) try { result.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (conn != null) try { conn.close(); } catch(Exception e) {}
            return pricePaid;
        }
    }



    public UserStock() {

    }
}
