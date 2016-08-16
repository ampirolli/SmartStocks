package seniorproject.smartstocks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.Transaction;
import seniorproject.smartstocks.Classes.User;

public class AccountsTransactionsActivity extends AppCompatActivity {

    Session currentSession;
    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    Spinner spAccounts;
    DatePicker dpStartDate;
    ListView lvTransactions;
    Button btnSubmit;

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
        dpStartDate = (DatePicker) findViewById(R.id.dpStart);
        dpStartDate.setMaxDate(System.currentTimeMillis()); //sets maximum to today
        lvTransactions = (ListView) findViewById(R.id.lvTransactions);

        btnSubmit = (Button) findViewById(R.id.btnSearch);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnSubmit.getText().equals("Search")){
                    String startDate = dpStartDate.getYear() + "-" + dpStartDate.getMonth() + "-" + dpStartDate.getDayOfMonth();
                    AuthTask = new getTransactionsTask( currentSession.getUser_id() ,startDate);
                    AuthTask.execute();

                    btnSubmit.setText("Show");
                    dpStartDate.setVisibility(View.GONE);
                }
                if(btnSubmit.getText().equals("Show")){
                    btnSubmit.setText("Search");
                    dpStartDate.setVisibility(View.VISIBLE);
                }



            }
        });

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

        Integer User_ID;
        String StartDate;
        ArrayList<String> transactionsList = new ArrayList<String>();

        public getTransactionsTask(Integer user_id, String startDate){
            User_ID = user_id;
            StartDate = startDate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            User user = new User(User_ID);
            user.setAccounts();
            Account account = user.getAccounts().get(accountSelectionIndex);
            account.setTranscations(Integer.valueOf(account.getAccountNumber()), StartDate);
            transactionsList.add("");
            for(Transaction transaction: account.getTranscations()){
                transactionsList.add(transaction.getTransactionType() + "   "  + transaction.getStockSymbol() + " - " + transaction.getStockQuantity() + " | $" + transaction.getPricePaid() + "    " + transaction.getTransactionDate());
            }


            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){
                //load the spinner with a list of accounts
                List<String> transactionList = transactionsList;

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AccountsTransactionsActivity.this, android.R.layout.simple_spinner_item, transactionList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvTransactions.setAdapter(arrayAdapter);


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

        this.finish();
    }
}
