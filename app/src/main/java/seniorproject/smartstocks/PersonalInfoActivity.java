package seniorproject.smartstocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import seniorproject.smartstocks.Classes.Session;

public class PersonalInfoActivity extends AppCompatActivity {
   TextView txtEmailAddress;
    TextView txtFistName;
    TextView txtLastName;
    TextView txtPhone;
    TextView txtDOB;
    Session currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        txtEmailAddress= (TextView)findViewById(R.id.txtEmailAddress);
        txtFistName= (TextView)findViewById(R.id.txtFirstName);
        txtLastName =(TextView)findViewById(R.id.txtLastName);
        txtPhone = (TextView)findViewById(R.id.txtPhoneNumber);
        txtDOB = (TextView)findViewById(R.id.txtDateOfBirth);
        
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
