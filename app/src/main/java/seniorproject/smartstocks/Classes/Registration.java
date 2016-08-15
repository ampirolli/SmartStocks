package seniorproject.smartstocks.Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import seniorproject.smartstocks.LICS;

/**
 * Created by Ampirollli on 7/25/2016.
 */
public class Registration {

    private String FirstName;
    private String LastName;
    private String Email;
    private String Phone;
    private String BirthDate;
    private String UserType;

    private ArrayList<Account> Accounts = new ArrayList<Account>();

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

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public Registration() {
        
    }

    public Registration(String firstName, String lastName, String email, String phone, String birthDate, String userType) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Phone = phone;
        BirthDate = birthDate;
        UserType = userType;
    }

    public boolean isEmailAvailable(String email){

        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        //Declare email
        String dbEmail = null;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.
            //String SQL = "SELECT * WHERE Email = " + mEmail +" and Password = " + mPassword + "FROM dbo.LOGIN";
            String SQL = "SELECT * FROM [SE414_Group3].[dbo].[User] WHERE email_address = '"+ email.toLowerCase() + "';";
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
            if(email.equals(dbEmail)){ return false; } else{ return true; }
        }

    }

    public boolean attemptRegister(Registration registration, String password){

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
            //execute sp_user_registration 'Owner', 'Anthony', 'PieRoll', 'ampieroll1@gmail.com', '4015555555', '08-08-1700', 'password', NULL
            String SQL = "execute sp_user_registration '" + registration.getUserType() + "', '" + registration.getFirstName() + "', '" + registration.getLastName() + "', '" + registration.getEmail() + "', '" + registration.getPhone() + "', '" + registration.getBirthDate() + "', '" + password + "', NULL ;";
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
