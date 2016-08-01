package seniorproject.smartstocks.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import seniorproject.smartstocks.LICS;

/**
 * Created by Ampirollli on 7/25/2016.
 */
public class User {
    private String FirstName;
    private String LastName;
    private String Email;
    private String Phone;
    private String BirthDate;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
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
        String dbPassword = null;
        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
//woah
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

    //execute sp_user_registration 'Owner', 'Anthony', 'Pirolli', 'ampirolli831@gmail.com', '4015555555', '08-31-1995', 'password', NULL


}
