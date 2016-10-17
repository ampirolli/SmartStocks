package seniorproject.smartstocks.Classes;

import android.util.Log;

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

import javax.net.ssl.HttpsURLConnection;

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

        String dbEmail = "";
        String dbPassword = "";

        try {

            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(getEmail(), "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(getHashedPassword(), "UTF-8");

            URL url = new URL("http://192.168.0.36:8080/WebService/login.php");
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

                Log.i("Login", responseCode + "");
            }

            return true;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        finally {

           if(getEmail().equals(dbEmail) && getHashedPassword().equals(dbPassword)){ return true; } else{ return false; }
        }
    }


    public Integer getUserID(){

        LICS loginConnectionString = new LICS();
        String connectionUrl = loginConnectionString.LoginConnectionString();

        // Declare the JDBC objects.
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(connectionUrl);
            // Create and execute an SQL statement that returns some data.

            String SQL = "execute sp_get_user_id '" +this.getEmail() + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            Integer user_id = null;
            while (result.next()) {
                user_id = result.getInt("user_id");
            }

            return  user_id;

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




}
