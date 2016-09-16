package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

public class ToolsDefinitionsSelectedActivity extends AppCompatActivity {
    Session currentSession;
    TextView txtTitle;
    TextView txtDefinitions;
    Integer DefinitionID;

    private getDefinitionsTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_definitions_selected);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(0);

        DefinitionID = previousIntent.getIntExtra("DefinitionID", 0); //pulls the definition id from previos intent to be loaded into our SP

        txtTitle= (TextView)findViewById(R.id.txtTitle);
        txtDefinitions =(TextView)findViewById(R.id.txtDefinitions);
        AuthTask = new getDefinitionsTask();
        AuthTask.execute();

    }


    public class getDefinitionsTask extends AsyncTask<Void, Void, Boolean> {
        String Title;
        String Definition;

        public getDefinitionsTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //CONNECT TO SQL DATABASE ON SEPERATE THREAD FROM THE MAIN THREAD
            getDefinition();

            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){

                txtTitle.setText(Title);
                txtDefinitions.setText(Definition);



            }
        }

        @Override
        protected void onCancelled() {
            AuthTask = null;

        }

        protected void getDefinition(){
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

                String SQL = "execute sp_get_definitions_by_id'"+ DefinitionID +"';";
                stmt = conn.createStatement();
                result = stmt.executeQuery(SQL);
                int counter = 0;
                while (result.next()) {
                    Title = (result.getString("title"));
                    Definition = (result.getString("definition"));


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
