package seniorproject.smartstocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ToolsDefinitionsSelectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_definitions_selected);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
