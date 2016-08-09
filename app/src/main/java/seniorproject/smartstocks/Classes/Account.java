package seniorproject.smartstocks.Classes;

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
public class Account {

    BigDecimal Balance;
    String AccountNumber;
    String Type;
    String Nickname;

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

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

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public Account(){

    }

    public ArrayList<Account> getAccounts(String user_id){
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

            String SQL = "sp_get_transaction '" + user_id + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {
                Account account = new Account();
                account.setAccountNumber(result.getString("account_number"));
                account.setBalance(result.getBigDecimal("account_balance"));
                account.setType(result.getString("account_type"));
                account.setNickname(result.getString("account_nickname"));
                accountList.add(account);
            }

            return  accountList;

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




    //EXECUTE sp_get_transaction 6






}
