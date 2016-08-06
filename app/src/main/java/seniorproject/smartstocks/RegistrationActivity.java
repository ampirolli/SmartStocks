package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


               attempCreation();

            }
        });


    }
    private void attempCreation(){
        if (AuthTask != null) {
            return;
        }

        // Reset errors.
        txtEmail.setError(null);
        txtPassword.setError(null);
        txtPasswordMatch.setError(null);

        // Store values at the time of the login attempt.
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String passwordMatch = txtPasswordMatch.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            txtEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtEmail;
            cancel = true;
        }


        if (TextUtils.isEmpty(passwordMatch)) {
            txtPasswordMatch.setError(getString(R.string.error_field_required));
            focusView = txtPasswordMatch;
            cancel = true;
        }

        if (!doPasswordsMatch(password, passwordMatch)) {
            txtPasswordMatch.setError("These passwords do not match");
            focusView = txtPasswordMatch;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            AuthTask = new UserRegistrationEmailTask(email, password);
            AuthTask.execute((Void) null);

        }

    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    private boolean doPasswordsMatch(String password, String passwordMatch){
        return password.equals(passwordMatch);
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

            User newUser = new User();

            return newUser.isEmailAvailable(Email);
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            AuthTask = null;


            if (success) {

                        Intent i = new Intent(RegistrationActivity.this, RegistrationPersonalInfoActivity.class);//creates intent that launches personal info page
                i.putExtra("Email", Email);
                i.putExtra("Password", Password);
                startActivity(i);

            } else {

                txtEmail.setError("This email is already taken");//THIS EMAIL IS TAKEN!
                txtEmail.requestFocus();
            }
        }


        @Override
        protected void onCancelled() {
            AuthTask = null;

        }


    }
}
