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

public class ToolsDefinitionsActivity extends AppCompatActivity {

    Session currentSession;
    ListView lvDefinitions;

    List<String> DefinitionsTitleList= new ArrayList<String>();
    List<Integer> DefinitionsID = new ArrayList<Integer>();

    private getDefinitionsListTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_definitions);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(0);

        lvDefinitions = (ListView) findViewById(R.id.lvDefinitionsList);

        AuthTask = new getDefinitionsListTask();
        AuthTask.execute();



    }

    public class getDefinitionsListTask extends AsyncTask<Void, Void, Boolean> {



        public getDefinitionsListTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //CONNECT TO SQL DATABASE ON SEPERATE THREAD FROM THE MAIN THREAD
            //Load into definitions list and definition id list

            getDefinitions();

            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){
                //Load the definitions data
                List<String> definitionsList = DefinitionsTitleList;

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ToolsDefinitionsActivity.this, android.R.layout.simple_spinner_item, definitionsList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvDefinitions.setAdapter(arrayAdapter);
                lvDefinitions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                        Intent i = new Intent(ToolsDefinitionsActivity.this, ToolsDefinitionsSelectedActivity.class); //creates intent that launches Definitions
                        i.putExtra("DefinitionID", DefinitionsID.get(index));

                        startActivity(i);

                        //launch new event

                    }
                });

            }
        }

        @Override
        protected void onCancelled() {
            AuthTask = null;

        }

        protected  void getDefinitions(){
            //SQL THAT SETS INFO
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

                String SQL = "execute sp_get_all_definitions;";
                stmt = conn.createStatement();
                result = stmt.executeQuery(SQL);
                int counter = 0;
                while (result.next()) {
                    DefinitionsTitleList.add(result.getString("title"));
                    DefinitionsID.add(result.getInt("definition_id"));
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
