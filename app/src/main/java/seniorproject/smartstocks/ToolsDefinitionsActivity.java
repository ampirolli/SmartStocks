package seniorproject.smartstocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ToolsDefinitionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definitions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
