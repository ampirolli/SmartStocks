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

    String AccountNumber;
    String Type;
    BigDecimal Balance;
    ArrayList<Order> Orders = new ArrayList<Order>();
    ArrayList<Transaction> Transcations = new ArrayList<Transaction>();

    String Nickname;


    protected Account(Parcel in) {
        AccountNumber = in.readString();
        Type = in.readString();
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


    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    public ArrayList<Transaction> getTranscations(Integer AccountID, String StartDate, String EndDate) {
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

            String SQL = "sp_get_transaction '" + AccountID +", " + StartDate + ", " + EndDate + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {




                Transaction transaction = new Transaction();
                transaction.setPricePaid(result.getBigDecimal("transaction_ammount"));
                transaction.setTransactionType(result.getString("transaction_type"));
                transaction.setTransactionDate(result.getDate("transaction_time"));
                transaction.setStockSymbol(result.getString("stock_symbol"));
                transaction.setStockQuantity(result.getInt("stock_quantity"));
                transactionList.add(transaction);
            }



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
            return transactionList;
        }
    }

    public ArrayList<Order> getOrders() {
        return Orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        Orders = orders;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public Account(){

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AccountNumber);
        dest.writeString(Type);
        dest.writeString(Nickname);
    }


    //EXECUTE sp_get_transaction 6






}
