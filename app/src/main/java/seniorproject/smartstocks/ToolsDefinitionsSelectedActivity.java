package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    ListView lvDefinitions;

    private getDefinitionsTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_definitions_selected);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        lvDefinitions = (ListView) findViewById(R.id.lvDefinitionsList);

    }


    public class getDefinitionsTask extends AsyncTask<Void, Void, Boolean> {

        List<String> DefinitionsList= new ArrayList<String>();

        public getDefinitionsTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //CONNECT TO SQL DATABASE ON SEPERATE THREAD FROM THE MAIN THREAD


            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){

                //load the text views


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

                String SQL = "execute sp_get_definitions_by_id;";
                stmt = conn.createStatement();
                result = stmt.executeQuery(SQL);
                int counter = 0;
                while (result.next()) {
                    //DefinitionsTitleList.add(result.getString("title"));
                    //DefinitionsID.add(result.getInt("definition_id"));
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
