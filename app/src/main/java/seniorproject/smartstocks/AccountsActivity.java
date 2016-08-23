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
import seniorproject.smartstocks.Classes.User;

public class AccountsActivity extends AppCompatActivity {

    Session currentSession;
    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    ListView lvAccountActivitiesList;
    Spinner spAccounts;

    private getAccountsTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();


        lvAccountActivitiesList = (ListView) findViewById(R.id.lvAccountPages);
        spAccounts = (Spinner) findViewById(R.id.spAccount);

        executeAuthTask();

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

        lvAccountActivitiesList.setAdapter(arrayAdapter1);

        lvAccountActivitiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if(id == 0){
                    Intent i = new Intent(AccountsActivity.this, AccountsBalancesActivity.class); //creates intent that launches Balances
                    i.putExtra("Session", currentSession.getUser_id());
                    i.putParcelableArrayListExtra("AccountsList", accountsList);
                    i.putStringArrayListExtra("AccountsNumberList", accountsNumberList);
                    i.putExtra("SelectedAccount", accountSelectionValue);
                    i.putExtra("SelectedIndex", accountSelectionIndex);
                    startActivityForResult(i, 1);
                }else if(id == 1){
                    Intent i = new Intent(AccountsActivity.this, AccountsOrdersActivity.class); //creates intent that launches Orders
                    i.putExtra("Session", currentSession.getUser_id());
                    i.putParcelableArrayListExtra("AccountsList", accountsList);
                    i.putStringArrayListExtra("AccountsNumberList", accountsNumberList);
                    i.putExtra("SelectedAccount", accountSelectionValue);
                    i.putExtra("SelectedIndex", accountSelectionIndex);
                    startActivityForResult(i, 1);
                }else if(id == 2){
                    Intent i = new Intent(AccountsActivity.this, AccountsPortfolioActivity.class); //creates intent that launches Portfolio
                    i.putExtra("Session", currentSession.getUser_id());
                    i.putParcelableArrayListExtra("AccountsList", accountsList);
                    i.putStringArrayListExtra("AccountsNumberList", accountsNumberList);
                    i.putExtra("SelectedAccount", accountSelectionValue);
                    i.putExtra("SelectedIndex", accountSelectionIndex);
                    startActivityForResult(i, 1);
                }else if(id == 3){
                    Intent i = new Intent(AccountsActivity.this, AccountsTransactionsActivity.class); //creates intent that Transactions
                    i.putExtra("Session", currentSession.getUser_id());
                    i.putParcelableArrayListExtra("AccountsList", accountsList);
                    i.putStringArrayListExtra("AccountsNumberList", accountsNumberList);
                    i.putExtra("SelectedAccount", accountSelectionValue);
                    i.putExtra("SelectedIndex", accountSelectionIndex);
                    startActivityForResult(i, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == AccountsActivity.RESULT_OK){

                spAccounts.setSelection(data.getIntExtra("result", 0));
            }
            if (resultCode == AccountsActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void executeAuthTask(){
        AuthTask = new getAccountsTask(currentSession.getUser_id());
        AuthTask.execute((Void) null);
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
                        accountsNickname.add(account.getAccountNumber());
                    else
                        accountsNickname.add(account.getNickname() + "-" + account.getAccountNumber());
                    accountsNumberList.add(account.getAccountNumber());

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AccountsActivity.this, android.R.layout.simple_spinner_item, accountsNickname);
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
            AuthTask = null;

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
