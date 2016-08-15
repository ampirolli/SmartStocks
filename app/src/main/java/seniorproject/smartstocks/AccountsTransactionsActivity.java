package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;

public class AccountsTransactionsActivity extends AppCompatActivity {

    Session currentSession;
    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    Spinner spAccounts;

    private getTransactionsTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_transactions);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0)); //loads current session into intent
        currentSession.getUser_id();
        accountsList = previousIntent.getParcelableArrayListExtra("AccountsList"); // pulls account list from previous intent for loading the spinner
        accountsNumberList = previousIntent.getStringArrayListExtra("AccountsNumberList"); //pulls account number from the previous intent to call sql statments regarding the account
        accountSelectionValue = previousIntent.getStringExtra("SelectedAccount"); // pulls selected account from previous intent
        accountSelectionIndex = previousIntent.getIntExtra("SelectedIndex",0); //pull selected accounts index from previous intent

        spAccounts = (Spinner) findViewById(R.id.spAccount);


        //load the spinner with a list of accounts
        List<String> accountsNickname= new ArrayList<String>();
        for (Account account: accountsList) {
            if(account.getNickname().equals(""))
                accountsNickname.add(account.getAccountNumber());
            else
                accountsNickname.add(account.getNickname() + "-" + account.getAccountNumber());
            accountsNumberList.add(account.getAccountNumber());

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountsNickname);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAccounts.setAdapter(arrayAdapter);
        spAccounts.setSelection(accountSelectionIndex);

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

    public class getTransactionsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }

        @Override
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
