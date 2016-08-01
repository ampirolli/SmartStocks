package seniorproject.smartstocks;

import android.content.Intent;
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
                String pass = txtPassword.getText().toString();


                User newUser =  new User();
                if (newUser.isEmailTaken(email) == false){

                    if(pass.equals(txtPasswordMatch.getText().toString())){

                        if(pass.length() > 7){
                            Intent i = new Intent(RegistrationActivity.this, RegistrationPersonalInfoActivity.class); //creates intent that launches personal info page
                            startActivity(i);


                        }else{
                            txtPassword.setError("Password length is too short. ");
                            //password length is too short!
                        }

                    }else{
                        txtPassword.setError("Passwords do not match. ");
                    }


                }else{

                    txtEmail.setError("This Email is already taken.");//THIS EMAIL IS TAKEN!
                }



            }
        });


    }
}
