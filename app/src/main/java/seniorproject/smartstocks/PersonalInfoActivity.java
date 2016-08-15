package seniorproject.smartstocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import seniorproject.smartstocks.Classes.Session;

public class PersonalInfoActivity extends AppCompatActivity {

    Session currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
