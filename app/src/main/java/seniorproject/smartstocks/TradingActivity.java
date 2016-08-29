package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class TradingActivity extends AppCompatActivity {

    Session currentSession;

    Spinner spAccounts;
    Spinner spOrderType;
    Spinner spPriceType;
    EditText txtShares;
    Spinner spTerm;
    Button btnPreview;
    TextView tvSymbol;
    TextView tvPrice;

    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    private getStockDataTask AuthTask1 = null;
    private getAccountsTask AuthTask2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        spAccounts = (Spinner) findViewById(R.id.spAccounts);
        spOrderType = (Spinner) findViewById(R.id.spOrderType);
        spPriceType = (Spinner) findViewById(R.id.spPriceType);
        txtShares = (EditText) findViewById(R.id.txtShares);
        spTerm = (Spinner) findViewById(R.id.spTerm);
        btnPreview = (Button) findViewById(R.id.btnPreview);
        tvSymbol = (TextView) findViewById(R.id.tvSymbol);
        tvPrice = (TextView) findViewById(R.id.tvPrice);

        tvSymbol.setText(previousIntent.getStringExtra("Symbol"));
        AuthTask1 = new getStockDataTask(tvSymbol.getText().toString());
        AuthTask1.execute();

        AuthTask2 = new getAccountsTask(currentSession.getUser_id());
        AuthTask2.execute();

        List<String> orderTypes = new ArrayList<String>();
        orderTypes.add("Buy");
        orderTypes.add("Sell");   //populates spinner with array of order types

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                orderTypes );

        spOrderType.setAdapter(arrayAdapter1);

    }

    public class getStockDataTask extends AsyncTask<Void, Void, Boolean> {

        String Symbol;
        yahoofinance.Stock Stock;

        BigDecimal Price = new BigDecimal(0);

        public getStockDataTask(String symbol) {

            Symbol = symbol;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //load the Stock Data
            try {

                Stock = YahooFinance.get(Symbol);
                Price = Stock.getQuote().getAsk();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask1 = null;

            if (success) {

                tvSymbol.setText(Symbol);
                tvPrice.setText(Price.toString());


            }
        }
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TradingActivity.this, android.R.layout.simple_spinner_item, accountsNickname);
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
