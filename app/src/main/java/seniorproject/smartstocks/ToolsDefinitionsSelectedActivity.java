package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
            //Load into definitions list

            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){
                //load the spinner with a list of accounts


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ToolsDefinitionsSelectedActivity.this, android.R.layout.simple_spinner_item, DefinitionsList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvDefinitions.setAdapter(arrayAdapter);

                lvDefinitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {

                        Intent i = new Intent(ToolsDefinitionsSelectedActivity.this, ToolsDefinitionsActivity.class); //creates intent that launches Definitions
                        i.putExtra("Session", currentSession.getUser_id());
                        startActivity(i);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // do nothing
                    }
                });



            }
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
}
