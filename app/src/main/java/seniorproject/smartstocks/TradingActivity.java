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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;
import seniorproject.smartstocks.Classes.UserStock;
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
    Spinner spUserStocks;

    String StockSymbol;

    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account

    private getStockDataTask AuthTask1 = null;
    private getAccountsTask AuthTask2 = null;
    private getPortfolioTask AuthTask3 = null;

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
        spUserStocks = (Spinner) findViewById(R.id.spUserStocks);

        tvSymbol.setText(previousIntent.getStringExtra("Symbol"));
        StockSymbol = (previousIntent.getStringExtra("Symbol"));
        AuthTask1 = new getStockDataTask(tvSymbol.getText().toString());
        AuthTask1.execute();

        AuthTask2 = new getAccountsTask(currentSession.getUser_id());
        AuthTask2.execute();

        AuthTask3 = new getPortfolioTask(currentSession.getUser_id());
        AuthTask3.execute();

        List<String> priceTypes = new ArrayList<String>();
        priceTypes.add("Market");
        priceTypes.add("Limit");   //populates spinner with array of price types
        priceTypes.add("Stop");
        priceTypes.add("Stop - Limit");

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                priceTypes );

        spPriceType.setAdapter(arrayAdapter2);

        spPriceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                List<String> terms = new ArrayList<String>();
                terms.add("1 day");
                if(position != 0){
                    terms.add("30 days");   //populates spinner with array of price types
                    terms.add("60 days");
                }

                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(
                        TradingActivity.this,
                        android.R.layout.simple_list_item_1,
                        terms );

                spTerm.setAdapter(arrayAdapter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

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

    public class getPortfolioTask extends AsyncTask<Void, Void, Boolean> {

        Integer User_ID;

        ArrayList<UserStock> StockList = new ArrayList<UserStock>();

        public getPortfolioTask(Integer user_id){
            User_ID = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            User user = new User(User_ID);
            user.setAccounts();
            Account account = user.getAccounts().get(accountSelectionIndex);
            account.setHoldings(Integer.valueOf(account.getAccountNumber()));

            for(UserStock userStock: account.getHoldings()){
                if(StockSymbol.equals(userStock.getStockSymbol())) {
                    StockList.add(userStock);
                }

            }


            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask3 = null;

            if(success){
                List<String> orderTypes = new ArrayList<String>();
                for(UserStock stock : StockList) {
                    if(stock.getStockSymbol().equals(StockSymbol)) {
                        orderTypes.add("Sell");   //populates spinner with array of order types

                    }
                }
                orderTypes.add("Buy");

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                        TradingActivity.this,
                        android.R.layout.simple_list_item_1,
                        orderTypes );

                spOrderType.setAdapter(arrayAdapter1);


            }
        }

        @Override
        protected void onCancelled() {
            AuthTask3 = null;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
