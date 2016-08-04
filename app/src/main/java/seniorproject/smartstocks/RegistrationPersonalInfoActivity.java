package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import seniorproject.smartstocks.Classes.User;

public class RegistrationPersonalInfoActivity extends AppCompatActivity {

    EditText txtFistName;
    EditText txtLastName;
    Spinner txtAccountType;
    EditText txtPhone;
    EditText txtDOB;
    Button btnRegister;
    private UserRegistrationSubmit AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_personal_info);


        txtFistName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtAccountType = (Spinner) findViewById(R.id.ddAccountType);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtDOB = (EditText) findViewById(R.id.txtDOB);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptUserCreation();

            }
        });

    }

    private void attemptUserCreation(){

        boolean cancel = false;
        View focusView = null;

        Intent previousIntent = getIntent();
        String email = previousIntent.getStringExtra("Email");
        String password = previousIntent.getStringExtra("Password");

        String firstname = txtFistName.getText().toString();                //Stores values at registration attempt
        String lastname = txtLastName.getText().toString();
       // String accountType = txtAccountType.getSelectedItem().toString();
        //String phone = txtPhone.getText().toString();
       // String dateOfBirth = txtDOB.getText().toString();

        if (TextUtils.isEmpty(firstname)) {
            txtFistName.setError(getString(R.string.error_field_required));
            focusView = txtFistName;
            cancel = true;
        } else if (!isNameValid(firstname)) {
            txtFistName.setError("Invalid fist name");
            focusView = txtFistName;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastname)) {
            txtLastName.setError(getString(R.string.error_field_required));
            focusView = txtLastName;
            cancel = true;
        } else if (!isNameValid(lastname)) {
            txtLastName.setError("Invalid last name");
            focusView = txtLastName;
            cancel = true;
        }



        if(cancel){
            //will not execute registration
        }else {
            User user = new User();
            AuthTask = new UserRegistrationSubmit(user);
            AuthTask.execute((Void) null);
        }
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return (!name.contains("\\d+") || !name.contains("\\W+"));
    }




    public class UserRegistrationSubmit extends AsyncTask<Void, Void, Boolean>{

        User newUser;
        public UserRegistrationSubmit(User user){
            newUser = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        protected void onPostExecute(final Boolean success) {

            if(success){

            }

        }

        protected void onCancelled() {
            AuthTask = null;

        }
    }
}
