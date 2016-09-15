package seniorproject.smartstocks.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import seniorproject.smartstocks.LICS;

/**
 * Created by Ampirollli on 8/8/2016.
 */
public class Account implements Parcelable {

    Integer AccountNumber;
    String Type;
    String Balance;
    String Nickname;
    ArrayList<Order> Orders = new ArrayList<Order>();
    ArrayList<Transaction> Transcations = new ArrayList<Transaction>();
    ArrayList<UserStock> Holdings = new ArrayList<UserStock>();


    protected Account(Parcel in) {
        AccountNumber = in.readInt();
        Type = in.readString();
        Balance = in.readString();
        Nickname = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public Integer getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public ArrayList<Transaction> getTranscations() {
        return Transcations;
    }

    public void setTranscations(Integer AccountID, String StartDate) {
        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare TransactionList
        ArrayList<Transaction> transactionList = new ArrayList<>();

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.

            String SQL = "sp_get_transaction '" + AccountID +"', '" + StartDate  + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {

                Transaction transaction = new Transaction();
                transaction.setPricePaid(result.getBigDecimal("transaction_amount"));
                transaction.setTransactionType(result.getString("transaction_type"));
                transaction.setTransactionDate(result.getDate("transaction_time"));
                transaction.setStockSymbol(result.getString("stock_symbol"));
                transaction.setStockQuantity(result.getInt("stock_quantity"));
                transaction.setPricePaid(result.getBigDecimal("price_paid"));
                transactionList.add(transaction);
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
            this.Transcations = transactionList;
        }
    }

    public ArrayList<Order> getOrders() {
        return Orders;
    }

    public void setOrders(Integer AccountID) {
        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare TransactionList
        ArrayList<Order> orderList = new ArrayList<>();

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.

            String SQL = "sp_get_orders '" + AccountID  + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {

                Order order = new Order();
                order.setPricePaid(result.getBigDecimal("transaction_amount"));
                order.setOrderType(result.getString("transaction_type"));
                order.setOrderDate(result.getDate("transaction_time"));
                order.setStockSymbol(result.getString("stock_symbol"));
                order.setStockQuantity(result.getInt("stock_quantity"));
                order.setPricePaid(result.getBigDecimal("price_paid"));
                orderList.add(order);
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
            this.Orders = orderList;
        }
    }

    public ArrayList<UserStock> getHoldings() {
        return Holdings;
    }

    public void setHoldings(Integer AccountID) {
        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare TransactionList
        ArrayList<UserStock> holdingsList = new ArrayList<>();

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.

            String SQL = "sp_get_portfolio_for_account '" + AccountID  + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {

                UserStock userStock = new UserStock();
                userStock.setStock(result.getString("stock_symbol"));
                userStock.setQuantity(result.getInt("quantity"));
                userStock.setTransactionID(result.getInt("transaction_id"));
                userStock.setAccountID(AccountID);
                holdingsList.add(userStock);
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
            this.Holdings = holdingsList;
        }
    }

    public Integer requestOrder(String transactionType, String priceType, BigDecimal stockPrice, String transcationTime, String term, String stockSymbol, Integer quantity, BigDecimal transactionAmount){

        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        Integer order_id = 0;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.
            String SQL = "execute sp_request_order '" + getAccountNumber() + "', '" + priceType + "', '" + stockPrice + "', " + transactionType + ", '" + transcationTime + "', '" + term + "', '" + stockSymbol + "', '" +quantity + "', " +transactionAmount + ", null ;";
            stmt = conn.createStatement();

            result = stmt.executeQuery(SQL);
            while (result.next()) {


                order_id = (result.getInt("transaction_id"));
            }
            return order_id;

        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (result != null) try { result.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (conn != null) try { conn.close(); } catch(Exception e) {}
        }


    }

    public Integer requestAutoTrade(Integer user_id, String symbol, Integer quantity){

        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        Integer autoTrade_id = 0;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.
            String SQL = "execute sp_request_autotrade " + user_id + ", " + getAccountNumber() + ", '" + symbol + "', " + quantity +  ", null ;";
            stmt = conn.createStatement();

            result = stmt.executeQuery(SQL);
            while (result.next()) {


                autoTrade_id = (result.getInt("auto_trade_id"));
            }
            return autoTrade_id;

        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (result != null) try { result.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (conn != null) try { conn.close(); } catch(Exception e) {}
        }


    }


    public void deleteOrder(Integer order_id) {

        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.
            String SQL = "execute sp_delete_order" + order_id + " ;";
            stmt = conn.createStatement();

            stmt.executeQuery(SQL);

        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (conn != null) try { conn.close(); } catch(Exception e) {}
        }

    }

    public void deleteAutoTrade(Integer autoTrade_id) {

        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.
            String SQL = "execute sp_delete_autotrade" + autoTrade_id + " ;";
            stmt = conn.createStatement();

            stmt.executeQuery(SQL);

        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (conn != null) try { conn.close(); } catch(Exception e) {}
        }

    }

    public Account(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(AccountNumber);
        dest.writeString(Type);
        dest.writeString(Balance);
        dest.writeString(Nickname);
    }


    //EXECUTE sp_get_transaction 6






}
