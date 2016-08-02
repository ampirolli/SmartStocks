package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import seniorproject.smartstocks.Classes.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtPasswordMatch;
    private Button btnSubmit;
    private UserRegistrationEmailTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPasswordMatch = (EditText) findViewById(R.id.txtPasswordMatch);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString().toLowerCase();
                String password = txtPassword.getText().toString();

                AuthTask = new UserRegistrationEmailTask(email, password);
                AuthTask.execute((Void) null);

            }
        });


    }

    public class UserRegistrationEmailTask extends AsyncTask<Void, Void, Boolean> {

        private final String Email;
        private final String Password;

        UserRegistrationEmailTask(String email, String password) {
            Email = email;
            Password = password;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            AuthTask = null;


            if (success) {


                User newUser = new User();
                if (!newUser.isEmailTaken(Email)) {

                    if (Password.equals(txtPasswordMatch.getText().toString())) {

                        if (Password.length() > 7) {
                            Intent i = new Intent(RegistrationActivity.this, RegistrationPersonalInfoActivity.class); //creates intent that launches personal info page
                            startActivity(i);


                        } else {
                            txtPassword.setError("Password length is too short. ");
                            txtPassword.requestFocus();
                            //password length is too short!
                        }

                    } else {
                        txtPassword.setError("Passwords do not match. ");
                        txtPassword.requestFocus();
                    }


                } else {

                    txtEmail.setError("This Email is already taken.");//THIS EMAIL IS TAKEN!
                    txtEmail.requestFocus();
                }
            }
        }


        @Override
        protected void onCancelled() {
            AuthTask = null;

        }


    }
}
