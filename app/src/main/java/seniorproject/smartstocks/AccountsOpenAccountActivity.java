package seniorproject.smartstocks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.AccountSmartStocks;
import seniorproject.smartstocks.Classes.Registration;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;

public class AccountsOpenAccountActivity extends AppCompatActivity {

    Session currentSession;
    EditText txtNickname;
    EditText txtBalance;
    Button btnCreate;

    private createSmartStocksAccountTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_open_account);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        txtNickname = (EditText) findViewById(R.id.txtAccountNickname);
        txtBalance = (EditText) findViewById(R.id.txtAccountBalance);
        btnCreate = (Button) findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attempAccountCreation();
            }
        });



    }

    public void attempAccountCreation(){
        boolean cancel = false;
        View focusView = null;


        String nickname = txtNickname.getText().toString();
        String balance = txtBalance.getText().toString();                //Stores values at attempt


        if (TextUtils.isEmpty(nickname)) {
            txtNickname.setError(getString(R.string.error_field_required));
            focusView = txtNickname;
            cancel = true;
        } else if (!isNameValid(nickname)) {
            txtNickname.setError("Invalid nickname");
            focusView = txtNickname;
            cancel = true;
        }

        if (TextUtils.isEmpty(balance)) {
            txtBalance.setError(getString(R.string.error_field_required));
            focusView = txtBalance;
            cancel = true;
        }


        if(cancel){
            //will not execute registration
            focusView.requestFocus();
        }else {
            AuthTask = new createSmartStocksAccountTask(currentSession.getUser_id(), balance , nickname);
            AuthTask.execute();
        }
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return (!name.matches("\\d+") && !name.matches("\\W+")); // if name contains numbers or special characters, return false
    }

    public class createSmartStocksAccountTask extends AsyncTask<Void, Void, Boolean> {

        Integer User_id;
        String Balance;
        String Nickname;

        public createSmartStocksAccountTask(Integer user_id, String balance, String nickname) {
            this.User_id = user_id;
            this.Balance = balance;
            this.Nickname = nickname;
        }

        @Override
        protected Boolean doInBackground(Void... params) {



            AccountSmartStocks smartStockAccount = new AccountSmartStocks(Balance, Nickname);
            return smartStockAccount.openAccount(User_id);
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){

                Context context = getApplicationContext();
                CharSequence text = "Account opened!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }else{
                Context context = getApplicationContext();
                CharSequence text = "An error occured!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            AuthTask = null;

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AccountsOpenAccountActivity.this, AccountsActivity.class);
        i.putExtra("Session", currentSession.getUser_id());
        startActivity(i);
        this.finish();
    }

}
