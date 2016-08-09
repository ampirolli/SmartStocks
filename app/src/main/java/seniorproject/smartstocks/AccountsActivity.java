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
import seniorproject.smartstocks.Classes.Session;

public class AccountsActivity extends AppCompatActivity {

    Session currentSession;
    ListView AccountActivitiesList;
    Spinner spAccounts;
    ArrayList<Account> accountsList;

    private getAccountsTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent previousIntent = getIntent();
        currentSession = (Session) previousIntent.getParcelableExtra("Session");
        currentSession.getUser_id();

        AccountActivitiesList = (ListView) findViewById(R.id.lvAccountPages);
        spAccounts = (Spinner) findViewById(R.id.spAccount);

        AuthTask = new getAccountsTask(currentSession.getUser_id());
        AuthTask.execute((Void) null);

        //load the spinner with a list of accounts
        List<String> accountsNickname= new ArrayList<String>();
        for (Account account: accountsList) {
            accountsNickname.add(account.getNickname());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                accountsNickname);

        spAccounts.setAdapter(arrayAdapter);


        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> accountActivities = new ArrayList<String>();
        accountActivities.add("Balances");
        accountActivities.add("Orders");
        accountActivities.add("Portfolio");
        accountActivities.add("Transactions");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                accountActivities );

        AccountActivitiesList.setAdapter(arrayAdapter1);

        AccountActivitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if(id == 0){
                    Intent i = new Intent(AccountsActivity.this, AccountsBalancesActivity.class); //creates intent that launches main menu
                    startActivity(i);
                }else if(id == 1){
                    Intent i = new Intent(AccountsActivity.this, AccountsOrdersActivity.class); //creates intent that launches main menu
                    startActivity(i);
                }else if(id == 2){
                    Intent i = new Intent(AccountsActivity.this, AccountsPortfolioActivity.class); //creates intent that launches main menu
                    startActivity(i);
                }else if(id == 3){
                    Intent i = new Intent(AccountsActivity.this, AccountsTransactionsActivity.class); //creates intent that launches main menu
                    startActivity(i);
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent i = new Intent(AccountsActivity.this, AccountsOpenAccountActivity.class); //creates intent that launches main menu
                startActivity(i);
            }
        });
    }



    public class getAccountsTask extends AsyncTask<Void, Void, Boolean> {

        String user_id;

        public getAccountsTask(String user_id) {
            this.user_id = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                Account account = new Account();
                accountsList = account.getAccounts(user_id);

            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
