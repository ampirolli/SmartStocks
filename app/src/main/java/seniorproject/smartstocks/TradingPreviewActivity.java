package seniorproject.smartstocks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Order;
import seniorproject.smartstocks.Classes.Session;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class TradingPreviewActivity extends AppCompatActivity {

    Session currentSession;
    TextView txtStockName;
    TextView txtPrice;
    TextView txtBid;
    TextView txtAsk;
    TextView txtAccount;
    TextView txtTransactiontype;
    TextView txtShares;
    TextView txtPriceType;
    TextView txtTerm;
    TextView txtEstimatedTotalCost;

    Button btnConfirm;

    String Symbol;
    static String Price;
    static String Bid;
    static String Ask;
    String AccountNumber;
    String Transactiontype;
    String Shares;
    String PriceType;
    String Term;

    private getStockDataTask AuthTask1 = null;
    private requestTradeTask AuthTask2 = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_preview);
        txtStockName =(TextView)findViewById(R.id.txtStockName);
        txtPrice =(TextView)findViewById(R.id.txtPrice);
        txtBid =(TextView)findViewById(R.id.txtBid);
        txtAsk =(TextView)findViewById(R.id.txtAsk);
        txtAccount =(TextView)findViewById(R.id.txtAccount);
        txtTransactiontype =(TextView)findViewById(R.id.txtTransactionType);
        txtShares =(TextView)findViewById(R.id.txtShares);
        txtPriceType =(TextView)findViewById(R.id.txtPriceType);
        txtTerm = (TextView)findViewById(R.id.txtTerm);
        txtEstimatedTotalCost = (TextView)findViewById(R.id.txtEstimatedTotalCost);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AuthTask2 = new requestTradeTask(Symbol, Integer.valueOf(AccountNumber), Transactiontype, Integer.valueOf(Shares), PriceType, Term);
                AuthTask2.execute();

            }
        });

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        Symbol = (previousIntent.getStringExtra("Symbol"));

        AuthTask1 = new getStockDataTask(Symbol);
        AuthTask1.execute();

        AccountNumber = (previousIntent.getStringExtra("Account_Number"));
        Transactiontype = (previousIntent.getStringExtra("Order_Type"));
        Shares = String.valueOf((previousIntent.getIntExtra("Share_Quantity", 0)));
        PriceType = (previousIntent.getStringExtra("Price_Type"));
        Term = (previousIntent.getStringExtra("Term"));

        txtStockName.setText(Symbol);
        txtAccount.setText(AccountNumber);
        txtTransactiontype.setText(Transactiontype);
        txtShares.setText(Shares);
        txtPriceType.setText(PriceType);
        txtTerm.setText(Term);

    }


    public class getStockDataTask extends AsyncTask<Void, Void, Boolean> {

        String Symbol;
        Stock Stock;

        BigDecimal Price = new BigDecimal(0);
        BigDecimal GainAndLoss = new BigDecimal(0);
        BigDecimal Ask = new BigDecimal(0);
        BigDecimal Bid = new BigDecimal(0);

        public getStockDataTask(String symbol) {
            Symbol = symbol;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Stock = YahooFinance.get(Symbol);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //load the Stock Data
            Price = Stock.getQuote().getPrice();
            GainAndLoss = Stock.getQuote().getChange();
            Ask = Stock.getQuote().getAsk();   // sets info off the main thread because it requires an api call
            Bid = Stock.getQuote().getBid();

            TradingPreviewActivity.Price = String.valueOf(Stock.getQuote().getPrice());
            TradingPreviewActivity.Ask = String.valueOf(Stock.getQuote().getAsk());  //sets static variables for sql call
            TradingPreviewActivity.Bid = String.valueOf(Stock.getQuote().getBid());

            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask1 = null;

            if(success){

                txtPrice.setText(Price.toString());
                txtBid.setText(Bid.toString());
                txtAsk.setText(Ask.toString());
                txtEstimatedTotalCost.setText(String.valueOf(  Ask.multiply(new BigDecimal(Shares))  ));

            }
        }


        @Override
        protected void onCancelled() {
            AuthTask1 = null;

        }
    }

    public class requestTradeTask extends AsyncTask<Void, Void, Boolean> {

        String Symbol;
        Stock Stock;

        Integer AccountNumber;
        String Transactiontype;
        Integer Quantity;
        String PriceType;
        String Term;
        String TransactionTime;

        BigDecimal Price = new BigDecimal(0);

        public requestTradeTask(String symbol, Integer accountNumber, String transactionType, Integer quantity, String priceType, String term) {
            Symbol = symbol;
            AccountNumber = accountNumber;
            Transactiontype = transactionType;
            Quantity = quantity;
            PriceType = priceType;
            Term = term;

            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            TransactionTime = sdf.format(date).toString();


        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Stock = YahooFinance.get(Symbol);
                Price = Stock.getQuote().getAsk();

            } catch (IOException e) {
                e.printStackTrace();
            }


            Account account = new Account();
            account.setAccountNumber(AccountNumber);

            BigDecimal TransactionAmmount = Price.multiply(new BigDecimal(Quantity));
            Integer Order_ID = account.requestOrder(Transactiontype ,PriceType, Price, TransactionTime, Term, Symbol, Quantity, TransactionAmmount);

            if ( Order_ID != null) {
               try {
                    Socket client;
                    PrintWriter printWriter;

                    client = new Socket("10.0.3.2", 4444);

                    printWriter = new PrintWriter(client.getOutputStream()); // notifys the server of the order that has just been placed
                    printWriter.write(String.valueOf(Order_ID));
                    printWriter.flush();
                    printWriter.close();
                    return true;
                }
                catch(Exception e) {

                    account.deleteOrder(Order_ID);
                    return false;
                }

            }
            else
                return false;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask2 = null;

            if(success){
                Context context = getApplicationContext();
                CharSequence text = "Trade Request Successful!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();


            }
            else{
                Context context = getApplicationContext();
                CharSequence text = "Trade Request Failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                finish();
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
