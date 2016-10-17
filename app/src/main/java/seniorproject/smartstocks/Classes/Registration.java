package seniorproject.smartstocks.Classes;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

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

        try {

            String data = URLEncoder.encode("account_type", "UTF-8") + "=" + URLEncoder.encode(registration.getUserType(), "UTF-8");
            data += "&" + URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(registration.getFirstName(), "UTF-8");
            data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(registration.getLastName(), "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(registration.getEmail(), "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(registration.getPhone(), "UTF-8");
            data += "&" + URLEncoder.encode("DOB", "UTF-8") + "=" + URLEncoder.encode(registration.getBirthDate(), "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


            URL url = new URL("http://192.168.0.36:8080/WebService/user_registration.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            String response = new String();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

                Log.i("Registration", responseCode + "");
            }

            return true;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }



}
