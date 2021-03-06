package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Order;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.Transaction;
import seniorproject.smartstocks.Classes.User;

public class AccountsOrdersActivity extends AppCompatActivity {

    Session currentSession;
    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    Spinner spAccounts;
    ListView lvOrders;

    private getOrdersTask AuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_orders);

        Intent previousIntent = getIntent();

        currentSession = Session.getInstance(0);

        accountsList = previousIntent.getParcelableArrayListExtra("AccountsList"); // pulls account list from previous intent for loading the spinner
        accountsNumberList = previousIntent.getStringArrayListExtra("AccountsNumberList"); //pulls account number from the previous intent to call sql statments regarding the account
        accountSelectionValue = previousIntent.getStringExtra("SelectedAccount"); // pulls selected account from previous intent
        accountSelectionIndex = previousIntent.getIntExtra("SelectedIndex",0); //pull selected accounts index from previous intent


        spAccounts = (Spinner) findViewById(R.id.spAccount);
        lvOrders = (ListView) findViewById(R.id.lvOrders);

        AuthTask = new getOrdersTask(currentSession.getUser_id());
        AuthTask.execute();


        //load the spinner with a list of accounts
        List<String> accountsNickname= new ArrayList<String>();
        for (Account account: accountsList) {
            if(account.getNickname().equals(""))
                accountsNickname.add(account.getAccountNumber().toString());
            else
                accountsNickname.add(account.getNickname() + "-" + account.getAccountNumber());
            accountsNumberList.add(account.getAccountNumber().toString());

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

    public class getOrdersTask extends AsyncTask<Void, Void, Boolean> {

        Integer User_ID;
        ArrayList<String> ordersList = new ArrayList<String>();

        public getOrdersTask(Integer user_id) {
            User_ID = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            User user = new User(User_ID);
            user.setAccounts();
            Account account = user.getAccounts().get(accountSelectionIndex);
            account.setOrders(Integer.valueOf(account.getAccountNumber()));
            ordersList.add("");
            for (Order order : account.getOrders()) {
                ordersList.add(order.getOrderType() + "   " + order.getStockSymbol() + "-" + order.getStockQuantity() + " | " + order.getPricePaid());
            }

            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if (success) {
                //load the spinner with a list of accounts
                List<String> transactionList = ordersList;

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AccountsOrdersActivity.this, android.R.layout.simple_spinner_item, ordersList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvOrders.setAdapter(arrayAdapter);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
