package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;

public class PersonalInfoActivity extends AppCompatActivity {

    Session currentSession;
    String test;
    TextView txtEmailAddress;
    TextView txtName;
    TextView txtPhone;
    TextView txtDOB;


    private getPersonalInfoTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        txtEmailAddress= (TextView)findViewById(R.id.txtEmailAddress);
        txtName= (TextView)findViewById(R.id.txtName);
        txtPhone = (TextView)findViewById(R.id.txtPhoneNumber);
        txtDOB = (TextView)findViewById(R.id.txtDateOfBirth);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();
        AuthTask = new getPersonalInfoTask(currentSession.getUser_id());
        AuthTask.execute();
        
    }

    public class getPersonalInfoTask extends AsyncTask<Void, Void, Boolean> {

        Integer user_id;

        public getPersonalInfoTask(Integer user_id) {
            this.user_id = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            getPersonalInformation(user_id);
            return true;
        }

        @Override
        protected void onCancelled() {
            AuthTask = null;

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void getPersonalInformation(Integer user_id){

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

            String SQL = "sp_get_personal '" + user_id + "';";
            stmt = conn.createStatement();
            result = stmt.executeQuery(SQL);
            int counter = 0;
            while (result.next()) {
                txtEmailAddress.setText(result.getString("email_address"));
                txtName.setText((result.getString("first_name")) + " " + (result.getString("last_name")));
                txtPhone.setText((result.getString("phone_number")));
                txtDOB.setText((result.getString("date_of_birth")));

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
        }

    }
}
