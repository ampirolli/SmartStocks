package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.AutoTrade;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;

public class ToolsAutoTradesActivity extends AppCompatActivity {


    Session currentSession;
    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    ArrayList<AutoTrade> autoTradeList = new ArrayList<AutoTrade>(); //list to save autotrades


    ListView lvAutoTrades;
    Spinner spAccounts;

    private getAccountsTask AuthTask = null;
    private getAutoTradesTask AuthTask2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_auto_trades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentSession = Session.getInstance(0);

        lvAutoTrades = (ListView) findViewById(R.id.lvAutoTrades);
        spAccounts = (Spinner) findViewById(R.id.spAccount);

        AuthTask = new getAccountsTask(currentSession.getUser_id());
        AuthTask.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ToolsAutoTradesActivity.this, ToolsAutoTradeRunActivity.class); //creates intent that launches Portfolio
                startActivity(i);

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

            AuthTask = null;

            if(success){
                //load the spinner with a list of accounts
                List<String> accountsNickname= new ArrayList<String>();
                for (Account account: accountsList) {
                    if(account.getNickname().equals(""))
                        accountsNickname.add(account.getAccountNumber().toString());
                    else
                        accountsNickname.add(account.getNickname() + "-" + account.getAccountNumber());
                    accountsNumberList.add(account.getAccountNumber().toString());

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ToolsAutoTradesActivity.this, android.R.layout.simple_spinner_item, accountsNickname);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spAccounts.setAdapter(arrayAdapter);

                accountSelectionValue = accountsList.get(spAccounts.getSelectedItemPosition()).getAccountNumber().toString();
                AuthTask2 = new getAutoTradesTask(Integer.valueOf(accountSelectionValue));
                AuthTask2.execute();

                spAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        accountSelectionValue = accountsNumberList.get(i);
                        accountSelectionIndex = i;
                        AuthTask2 = new getAutoTradesTask(Integer.valueOf(accountSelectionValue));
                        AuthTask2.execute();
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
            AuthTask = null;

        }


    }

    public class getAutoTradesTask extends AsyncTask<Void, Void, Boolean> {

        Integer account_id;

        public getAutoTradesTask(Integer account_id) {
            this.account_id = account_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Account account = new Account(account_id);
            account.setAutoTrades();
            autoTradeList = account.getAutoTrades();

            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask2 = null;

            if(success){
                //load the spinner with a list of accounts
                List<String> autoTradesListTitles= new ArrayList<String>();
                for (AutoTrade autoTrade: autoTradeList) {
                    autoTradesListTitles.add(autoTrade.getAutoTrade_id() +" - "+ autoTrade.getStockSymbol() + " : " + autoTrade.getStockQuantity() + " @"+ autoTrade.getAutoTradeTime());

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ToolsAutoTradesActivity.this, android.R.layout.simple_spinner_item, autoTradesListTitles);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvAutoTrades.setAdapter(arrayAdapter);

                lvAutoTrades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
