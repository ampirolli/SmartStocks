package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import seniorproject.smartstocks.Classes.User;

public class RegistrationPersonalInfoActivity extends AppCompatActivity {

    private UserRegistrationSubmit AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_personal_info);
        final Intent previousIntent = getIntent();

        EditText txtFistName = (EditText) findViewById(R.id.txtFirstName);
        EditText txtLastName = (EditText) findViewById(R.id.txtLastName);
        Spinner txtAccountType = (Spinner) findViewById(R.id.ddAccountType);
        EditText txtPhone = (EditText) findViewById(R.id.txtPhone);
        EditText txtDOB = (EditText) findViewById(R.id.txtDOB);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User newUser =  new User();

                String email = previousIntent.getStringExtra("Email");
                String password = previousIntent.getStringExtra("Password");


                AuthTask = new UserRegistrationSubmit(newUser);
                AuthTask.execute((Void) null);

            }
        });

    }

    private boolean attemptUserCreation(){


        return true;
    }

    public class UserRegistrationSubmit extends AsyncTask<Void, Void, Boolean>{

        public UserRegistrationSubmit(User user){

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }

        protected void onPostExecute(final Boolean success) {}

        protected void onCancelled() {
            AuthTask = null;

        }
    }
}
