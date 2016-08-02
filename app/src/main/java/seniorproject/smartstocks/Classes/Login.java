package seniorproject.smartstocks.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import seniorproject.smartstocks.LICS;

/**
 * Created by Ampirollli on 7/25/2016.
 */
public class Login {

    private String Email;
    private String HashedPassword; //password does not need to be set to lowercase. case sensitive

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHashedPassword() {
        return HashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        HashedPassword = hashedPassword;//WE ARE GOING TO ENCORPERATE AN ENCRYPTION METHOD FOR THE PASSWORD
    }

    public Login(String email, String Password) {
        setEmail(email.toLowerCase());
        setHashedPassword(Password); //password does not need to be set to lowercase. case sensitive
    }

    public Login() {
        setEmail(null);
        setHashedPassword(null);
    }

    public boolean attemptLogin(){
        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare email+password
        String dbEmail = null;
        String dbPassword = null;
        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);

            // Create and execute an SQL statement that returns some data.
            String SQL = "execute sp_login '" + getEmail() +"', '" + getHashedPassword() +  "',NULL ;";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (result.next()) {
                String dbUserID = result.getString("user_id");
                String SQL1 = "SELECT email_address FROM DBO.[USER] WHERE user_id = '"+ dbUserID.toLowerCase() + "'; ";
                Statement stmt1 = conn.createStatement();
                ResultSet result1 = stmt1.executeQuery(SQL1);
                while (result1.next()) {
                    dbEmail = result1.getString("email_address");
                }


                dbPassword = result.getString("password");
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
            if(getEmail().equals(dbEmail) && getHashedPassword().equals(dbPassword)){ return true; } else{ return false; }
        }
    }

    public boolean isEmailTaken(String email){

        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare email+password
        String dbEmail = null;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.
            //String SQL = "SELECT * WHERE Email = " + mEmail +" and Password = " + mPassword + "FROM dbo.LOGIN";
            String SQL = "SELECT * FROM [SE414_Group3].[dbo].[User] WHERE email_address = '"+ email.toLowerCase() + ";";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (result.next()) {
                dbEmail = result.getString("email_address");
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
            if(email.equals(dbEmail)){ return true; } else{ return false; }
        }

    }



}
