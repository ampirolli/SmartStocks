package seniorproject.smartstocks.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import seniorproject.smartstocks.LICS;

/**
 * Created by Ampirollli on 8/16/2016.
 */
public class AccountSmartStocks extends Account {

    public AccountSmartStocks(String balance, String nickname){

        this.setBalance(balance);
        this.setType("SmartStocks");
        this.setNickname(nickname);

    }

    public boolean openAccount(Integer user_id){

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
            String SQL = "execute sp_open_account '"+ user_id + "', '"  + this.getBalance() + "', '" + this.getType() + "', '" + this.getNickname() + "' ;";
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            return true;

        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (conn != null) try { conn.close(); } catch(Exception e) {}
        }

    }

}
