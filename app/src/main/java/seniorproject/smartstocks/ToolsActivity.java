package seniorproject.smartstocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Session;

public class ToolsActivity extends AppCompatActivity {

    Session currentSession;
    ListView lvToolsActivitiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        lvToolsActivitiesList = (ListView) findViewById(R.id.lvToolPages);

        List<String> toolsActivities = new ArrayList<String>();
        toolsActivities.add("Analyzer");
        toolsActivities.add("AutoTrading");
        toolsActivities.add("Definitions");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                toolsActivities );

        lvToolsActivitiesList.setAdapter(arrayAdapter1);

        lvToolsActivitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if(id == 0){
                    Intent i = new Intent(ToolsActivity.this, ToolsStockAnalyzerActivity.class); //creates intent that launches Portfolio
                    i.putExtra("Session", currentSession.getUser_id());
                    startActivity(i);

                }else if(id == 1){
                    Intent i = new Intent(ToolsActivity.this, ToolsAutoTradeActivity.class); //creates intent that launches Portfolio
                    i.putExtra("Session", currentSession.getUser_id());
                    startActivity(i);

                }else if(id == 2){
                    Intent i = new Intent(ToolsActivity.this, ToolsDefinitionsActivity.class); //creates intent that launches Portfolio
                    i.putExtra("Session", currentSession.getUser_id());
                    startActivity(i);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ToolsActivity.this, MainActivity.class);
        i.putExtra("Session", currentSession.getUser_id());
        startActivity(i);
        this.finish();
    }
}
