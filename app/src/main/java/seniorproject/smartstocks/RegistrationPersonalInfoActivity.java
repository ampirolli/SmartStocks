package seniorproject.smartstocks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import seniorproject.smartstocks.Classes.User;

public class RegistrationPersonalInfoActivity extends AppCompatActivity {

    EditText txtFistName;
    EditText txtLastName;
    Spinner spAccountType;
    EditText txtPhone;
    DatePicker dpDOB;
    Button btnRegister;
    String email;
    String password;

    private UserRegistrationSubmit AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_personal_info);


        txtFistName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        spAccountType = (Spinner) findViewById(R.id.spAccountType);
        String[] arraySpinner = new String[] {
                "Personal", "Business"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spAccountType.setAdapter(adapter); // sets items in spinner

        txtPhone = (EditText) findViewById(R.id.txtPhone);
        dpDOB = (DatePicker) findViewById(R.id.dpDOB);
        dpDOB.setMaxDate(System.currentTimeMillis()); //sets maximum birthday dat

        Intent previousIntent = getIntent();
        email = previousIntent.getStringExtra("Email");
        password = previousIntent.getStringExtra("Password"); //will eventually hash

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



        String firstname = txtFistName.getText().toString();                //Stores values at registration attempt
        String lastname = txtLastName.getText().toString();
        String accountType = spAccountType.getSelectedItem().toString();
        String phone = txtPhone.getText().toString();
        String dateOfBirth = dpDOB.getMonth() + "/" + dpDOB.getDayOfMonth() + "/" + dpDOB.getYear();

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

        if (TextUtils.isEmpty(phone)) {
            txtPhone.setError(getString(R.string.error_field_required));
            focusView = txtPhone;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            txtPhone.setError("Invalid phone number");
            focusView = txtPhone;
            cancel = true;
        }



        if(cancel){
            //will not execute registration
            focusView.requestFocus();
        }else {
            User user = new User(firstname, lastname, email, phone, dateOfBirth,accountType);
            AuthTask = new UserRegistrationSubmit(user, password);
            AuthTask.execute((Void) null);
        }
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return (!name.matches("\\d+") && !name.matches("\\W+")); // if name contains numbers or special characters, return false
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return (phone.matches("[0-9]+") && (phone.length() == 10) ); //if phone number is numeric and 10 characters long, return true
    }




    public class UserRegistrationSubmit extends AsyncTask<Void, Void, Boolean>{

        User newUser;
        String password;
        public UserRegistrationSubmit(User user, String password){
            newUser = user;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return newUser.attemptRegister(newUser, password);

        }

        protected void onPostExecute(final Boolean success) {

            if(success){
                Context context = getApplicationContext();
                CharSequence text = "Registration Successful!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
            }
            else{
                Context context = getApplicationContext();
                CharSequence text = "Registration Failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }

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
