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
    private String HashedPassword;

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
        setEmail(email);
        setHashedPassword(Password);
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
            //String SQL = "SELECT * WHERE Email = " + mEmail +" and Password = " + mPassword + "FROM dbo.LOGIN";
            String SQL = "SELECT * FROM [SE414_Group3].[dbo].[Login] WHERE email_address = '"+ getEmail().toLowerCase() +"' and password = '" + getHashedPassword().toLowerCase() +"';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (result.next()) {
                dbEmail = result.getString("email_address");
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



}
