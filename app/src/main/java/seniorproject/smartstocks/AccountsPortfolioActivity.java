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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.Transaction;
import seniorproject.smartstocks.Classes.User;
import seniorproject.smartstocks.Classes.UserStock;

public class AccountsPortfolioActivity extends AppCompatActivity {

    Session currentSession;
    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    Spinner spAccounts;
    ListView lvHoldings;
    TextView txtDaysGainLoss;
    TextView txtTotalGainLoss;
    TextView txtCash;


    private getPortfolioTask AuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_portfolio);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0)); //loads current session into intent
        currentSession.getUser_id();
        accountsList = previousIntent.getParcelableArrayListExtra("AccountsList"); // pulls account list from previous intent for loading the spinner
        accountsNumberList = previousIntent.getStringArrayListExtra("AccountsNumberList"); //pulls account number from the previous intent to call sql statments regarding the account
        accountSelectionValue = previousIntent.getStringExtra("SelectedAccount"); // pulls selected account from previous intent
        accountSelectionIndex = previousIntent.getIntExtra("SelectedIndex",0); //pull selected accounts index from previous intent

        spAccounts = (Spinner) findViewById(R.id.spAccount);
        lvHoldings = (ListView) findViewById(R.id.lvHoldings);
        txtDaysGainLoss = (TextView) findViewById(R.id.txtTodayGainLoss);
        txtTotalGainLoss = (TextView) findViewById(R.id.txtTotalGainLoss);
        txtCash = (TextView) findViewById(R.id.txtCash);

        AuthTask = new getPortfolioTask( currentSession.getUser_id());
        AuthTask.execute();

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
                AuthTask = new getPortfolioTask( currentSession.getUser_id());
                AuthTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

    }

    public class getPortfolioTask extends AsyncTask<Void, Void, Boolean> {

        Integer User_ID;
        BigDecimal HoldingsSum = new BigDecimal(0);
        BigDecimal DaysGainLoss = new BigDecimal(0);
        BigDecimal TotalGainLoss = new BigDecimal(0);
        ArrayList<String> HoldingsList = new ArrayList<String>();

        public getPortfolioTask(Integer user_id){
            User_ID = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            User user = new User(User_ID);
            user.setAccounts();
            Account account = user.getAccounts().get(accountSelectionIndex);
            account.setHoldings(Integer.valueOf(account.getAccountNumber()));

            HoldingsList.add("");

            BigDecimal daysTotalOpen = new BigDecimal(0);
            BigDecimal daysTotalGain = new BigDecimal(0);

            BigDecimal allTimePricePaid = new BigDecimal(0);
            BigDecimal allTimeTotalGain = new BigDecimal(0);


            for(UserStock userStock: account.getHoldings()){
                HoldingsList.add(userStock.getStockSymbol());

                HoldingsSum = (userStock.getStock().getQuote().getPrice()).multiply(new BigDecimal(userStock.getQuantity())); // adds up all holdings
                HoldingsSum = new BigDecimal(account.getBalance()).subtract(HoldingsSum); //subtracts it from total to determine purchasing power

                daysTotalOpen = daysTotalOpen.add(userStock.getStock().getQuote().getOpen().multiply(BigDecimal.valueOf(userStock.getQuantity()))); // sum of all stocks opening price * the quantity
                daysTotalGain = daysTotalGain.add(userStock.getStock().getQuote().getPrice().multiply(BigDecimal.valueOf(userStock.getQuantity()))); // sum of all stocks prices * the quantity

                DaysGainLoss = daysTotalGain.subtract(daysTotalOpen);

                allTimePricePaid = allTimePricePaid.add(userStock.getPricePaid().multiply(BigDecimal.valueOf(userStock.getQuantity()))); //sum of all prices paid


                TotalGainLoss = allTimePricePaid.subtract(daysTotalGain);



            }
            if(account.getHoldings().size() < 1)
                HoldingsSum = new BigDecimal(account.getBalance());


            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){
                //load the spinner with a list of accounts
                List<String> holdingList = HoldingsList;

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AccountsPortfolioActivity.this, android.R.layout.simple_spinner_item, HoldingsList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvHoldings.setAdapter(arrayAdapter);
                txtDaysGainLoss.setText(DaysGainLoss.toString());
                txtTotalGainLoss.setText(TotalGainLoss.toString());
                txtCash.setText(HoldingsSum.toString());


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
