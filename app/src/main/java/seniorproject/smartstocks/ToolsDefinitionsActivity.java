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

import seniorproject.smartstocks.Classes.Session;

public class ToolsDefinitionsActivity extends AppCompatActivity {

    Session currentSession;
    ListView lvDefinitions;

    private getDefinitionsListTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_definitions_selected);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        lvDefinitions = (ListView) findViewById(R.id.lvDefinitionsList);
    }

    public class getDefinitionsListTask extends AsyncTask<Void, Void, Boolean> {

        List<String> DefinitionsList= new ArrayList<String>();

        public getDefinitionsListTask() {

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
                //Load the definitions data


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
