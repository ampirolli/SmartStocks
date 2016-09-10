package seniorproject.smartstocks.Classes;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import seniorproject.smartstocks.LICS;

/**
 * Created by Ampirollli on 8/14/2016.
 */
public class User {

    Integer UserID;

    String Username;
    String UserType;
    ArrayList<Account> Accounts = new ArrayList<Account>();
    ArrayList<StockAnalyzer> Analyzer = new ArrayList<StockAnalyzer>();
    ArrayList<StockAutoTrade> AutoTrades = new ArrayList<StockAutoTrade>();


    ArrayList<UserStock> Holdings = new ArrayList<UserStock>();
    ArrayList<String> Favorites = new ArrayList<String>();

    public ArrayList<StockAnalyzer> getAnalyzer() {
        return Analyzer;
    }

    public Integer getUserID() {
        return UserID;
    }

    public String getUsername() {
        return Username;
    }

    public String getUserType() {
        return UserType;
    }

    public ArrayList<Account> getAccounts() {
        return Accounts;
    }

    public void setAccounts(){
        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare email+password
        ArrayList<Account> accountList = new ArrayList<>();

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.

            String SQL = "sp_get_accounts '" + this.UserID + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {
                Account account = new Account();
                account.setAccountNumber(Integer.valueOf(result.getString("account_id")));
                account.setBalance(result.getString("account_balance"));
                account.setType(result.getString("account_type"));
                account.setNickname(result.getString("account_nickname"));
                accountList.add(account);
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
            this.Accounts = accountList;
        }
    }

    public ArrayList<StockAutoTrade> getAutoTrades() {
        return AutoTrades;
    }

    public ArrayList<String> getFavorites() {
        return Favorites;
    }

    public ArrayList<String> setFavorites() {
        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare email+password
        ArrayList<String> favorites = new ArrayList<>();

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.

            String SQL = "sp_get_favorites '" + this.UserID + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {
                String favorite = new String();
                favorite = (result.getString("stock_symbol"));
                favorites.add(favorite);
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
             return favorites;
        }
    }


    public User(Integer ID){

        UserID = ID;

    }


}
