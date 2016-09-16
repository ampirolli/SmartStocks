package seniorproject.smartstocks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;

public class ToolsAutoTradeRunActivity extends AppCompatActivity {

    Session currentSession;
    EditText txtSymbol;
    EditText txtQuantity;
    Button btnAutoTrade;

    Spinner spAccounts;

    ArrayList<Account> accountsList = new ArrayList<Account>(); //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new ArrayList<String>(); //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    requestAutoTradeTask AuthTask = null;
    getAccountsTask AuthTask2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_auto_trade_run);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(0);

        txtSymbol = (EditText) findViewById(R.id.txtBalance);
        txtQuantity = (EditText) findViewById(R.id.txtQuantity);
        btnAutoTrade = (Button) findViewById(R.id.btnAutoTrade);

        spAccounts = (Spinner) findViewById(R.id.spAccounts);
        AuthTask2 = new getAccountsTask(currentSession.getUser_id());
        AuthTask2.execute();

        btnAutoTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AuthTask = new requestAutoTradeTask(currentSession.getUser_id(), Integer.valueOf(accountSelectionValue), txtSymbol.getText().toString(), Integer.valueOf(txtQuantity.getText().toString()));
                AuthTask.execute();

            }
        });


    }

    public class getAccountsTask extends AsyncTask<Void, Void, Boolean> {

        Integer user_id;

        public getAccountsTask(Integer user_id) {
            this.user_id = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User user = new User(user_id);
            user.setAccounts();
            accountsList = user.getAccounts();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask2 = null;

            if (success) {
                //load the spinner with a list of accounts
                List<String> accountsNickname = new ArrayList<String>();
                for (Account account : accountsList) {
                    if (account.getNickname().equals(""))
                        accountsNickname.add(account.getAccountNumber().toString());
                    else
                        accountsNickname.add(account.getNickname() + "-" + account.getAccountNumber());
                    accountsNumberList.add(account.getAccountNumber().toString());

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ToolsAutoTradeRunActivity.this, android.R.layout.simple_spinner_item, accountsNickname);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spAccounts.setAdapter(arrayAdapter);

                spAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        accountSelectionValue = accountsNumberList.get(i);
                        accountSelectionIndex = i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // do nothing
                    }
                });


            }
        }

        @Override
        protected void onCancelled() {
            AuthTask2 = null;

        }


    }


    public class requestAutoTradeTask extends AsyncTask<Void, Void, Boolean> {

        Integer user_id;
        Integer account_id;
        String Symbol;
        Integer Quantity;

        public requestAutoTradeTask(Integer user_id, Integer account_id, String symbol, Integer quantity) {
            this.user_id = user_id;
            this.account_id = account_id;
            Symbol = symbol;
            Quantity = quantity;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Account account = new Account();
            account.setAccountNumber(account_id);
            Integer AutoTrade_id = account.requestAutoTrade(user_id, Symbol, Quantity);

            if (AutoTrade_id != null) {
                try {
                    Socket client;
                    PrintWriter printWriter;

                    client = new Socket("10.0.3.2", 4445);

                    printWriter = new PrintWriter(client.getOutputStream()); // notifys the server of the order that has just been placed
                    printWriter.write(String.valueOf(AutoTrade_id));
                    printWriter.flush();
                    printWriter.close();
                    return true;
                } catch (Exception e) {

                    account.deleteAutoTrade(AutoTrade_id);
                    return false;
                }

            }
            else
                return false;
        }

        @Override
        protected void onPostExecute ( final Boolean success){

            AuthTask = null;

            if(success){
                Context context = getApplicationContext();
                CharSequence text = "AutoTrade Request Successful!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();


            }
            else{
                Context context = getApplicationContext();
                CharSequence text = "AutoTrade Request Failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
            }
        }

        @Override
        protected void onCancelled () {
            AuthTask = null;

        }



    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
