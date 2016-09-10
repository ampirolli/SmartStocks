package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;
import seniorproject.smartstocks.Classes.UserStock;
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

    ArrayList<UserStock> userStocksList = new ArrayList<UserStock>();

    ArrayList<Integer> userStocksTransactionIds = new ArrayList<Integer>();
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

        spOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(position == 0){
                    spUserStocks.setVisibility(View.GONE);
                }else{
                    spUserStocks.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;

                String quantity = txtShares.getText().toString();

                if (TextUtils.isEmpty(quantity)) {
                    txtShares.setError(getString(R.string.error_field_required));
                    focusView = txtShares;
                    cancel = true;
                } else if (isHigherThanOwnedShares(Integer.valueOf(quantity))) {
                    txtShares.setError("Shares must be equal to or less than the amount of owned stock");
                    focusView = txtShares;
                    cancel = true;
                } else if (Integer.valueOf(quantity) <= 0 ) {
                    txtShares.setError("Shares must be greater than zero");
                    focusView = txtShares;
                    cancel = true;
                }
                if (cancel == false)
                {
                    Intent i = new Intent(TradingActivity.this, TradingPreviewActivity.class);
                    i.putExtra("Session", currentSession.getUser_id());
                    i.putExtra("Account_Number", accountSelectionValue);
                    i.putExtra("Order_Type", spOrderType.getSelectedItem().toString());
                    i.putExtra("Price_Type", spPriceType.getSelectedItem().toString());
                    i.putExtra("Share_Quantity", Integer.valueOf(txtShares.getText().toString()));
                    i.putExtra("Term", spTerm.getSelectedItem().toString());
                    i.putExtra("Symbol", tvSymbol.getText().toString());
                    if(spOrderType.getSelectedItem().toString().equals("Sell"))
                        i.putExtra("Transaction_id", userStocksList.get(spUserStocks.getSelectedItemPosition()).getTransactionID());

                    startActivity(i);
                }


            }
        });

    }

    public boolean isHigherThanOwnedShares(Integer shares)
    {
        if(spUserStocks.getSelectedItem() ==null )
            return false;
        if(shares > userStocksList.get(spUserStocks.getSelectedItemPosition()).getQuantity() && spOrderType.getSelectedItem().toString().equals("Sell"))
            return true;
        else
            return false;
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
                        accountsNickname.add(account.getAccountNumber().toString());
                    else
                        accountsNickname.add(account.getNickname() + "-" + account.getAccountNumber());
                    accountsNumberList.add(account.getAccountNumber().toString());

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TradingActivity.this, android.R.layout.simple_spinner_item, accountsNickname);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spAccounts.setAdapter(arrayAdapter);

                spAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        accountSelectionValue = accountsNumberList.get(i);
                        accountSelectionIndex = i;

                        AuthTask3 = new getPortfolioTask(currentSession.getUser_id());
                        AuthTask3.execute();


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
        boolean ownsStock = false;

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
                    ownsStock = true;
                }
            }



            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask3 = null;

            if(success){
                List<String> orderTypes = new ArrayList<String>();
                List<String> userStocks = new ArrayList<String>();
                userStocksList = new ArrayList<UserStock>();
                boolean hasStock = true;
                spUserStocks.setVisibility(View.GONE);
                orderTypes.add("Buy");
                for(UserStock stock : StockList) {
                    if(stock.getStockSymbol().equals(StockSymbol)) {
                        if(hasStock == true){
                            orderTypes.add("Sell");   //populates spinner with array of order types
                            hasStock = false; // will set to false to avoid adding sell multiple times
                        }
                        userStocksTransactionIds.add(stock.getTransactionID());
                        userStocks.add(stock.getStockSymbol() + " - Quantity: " + stock.getQuantity());
                        userStocksList.add(stock);
                        spUserStocks.setVisibility(View.VISIBLE);

                        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(
                                TradingActivity.this,
                                android.R.layout.simple_list_item_1,
                                userStocks );

                        spUserStocks.setAdapter(arrayAdapter5);


                    }
                }



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
