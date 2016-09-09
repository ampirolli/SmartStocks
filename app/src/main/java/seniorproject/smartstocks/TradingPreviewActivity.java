package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    TextView txtOrderType;
    TextView txtShares;
    TextView txtPriceType;
    TextView txtStopPrice;
    TextView txtTerm;
    TextView txtEstimatedCommision;
    TextView txtEstimatedTotalCost;

    private getStockDataTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_preview);
        txtStockName =(EditText)findViewById(R.id.txtStockName);
        txtPrice =(EditText)findViewById(R.id.txtPrice);
        txtBid =(EditText)findViewById(R.id.txtBid);
        txtAsk =(EditText)findViewById(R.id.txtAsk);
        txtAccount =(EditText)findViewById(R.id.txtAccount);
        txtOrderType =(EditText)findViewById(R.id.txtOrderType);
        txtShares =(EditText)findViewById(R.id.txtShares);
        txtPriceType =(EditText)findViewById(R.id.txtPriceType);
        txtStopPrice =(EditText)findViewById(R.id.txtStopPrice);
        txtTerm = (EditText)findViewById(R.id.txtTerm);
        txtEstimatedCommision = (EditText)findViewById(R.id.txtEstimatedCommision);
        txtEstimatedTotalCost = (EditText)findViewById(R.id.txtEstimatedTotalCost);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        txtStockName.setText(previousIntent.getStringExtra("Symbol"));
        AuthTask = new getStockDataTask(previousIntent.getStringExtra("Symbol"));
        AuthTask.execute();

        txtAccount.setText(previousIntent.getStringExtra("Account_Number"));
        txtOrderType.setText(previousIntent.getStringExtra("Order_Type"));
        txtShares.setText(previousIntent.getStringExtra("Share_Quantity"));
        txtPriceType.setText(previousIntent.getStringExtra("Price_Type"));
        //txtStopPrice.setText(previousIntent.getStringExtra("A"));
        txtTerm.setText(previousIntent.getStringExtra("Term"));
        //txtEstimatedCommision.setText(previousIntent.getStringExtra("A"));
        //txtEstimatedTotalCost.setText(previousIntent.getStringExtra("A"));



    }


    public class getStockDataTask extends AsyncTask<Void, Void, Boolean> {

        String Symbol;
        Stock Stock;

        BigDecimal Price = new BigDecimal(0);
        BigDecimal GainAndLoss = new BigDecimal(0);
        BigDecimal Ask = new BigDecimal(0);
        BigDecimal Bid = new BigDecimal(0);

        public getStockDataTask(String symbol) {

            try {
                Stock = YahooFinance.get(Symbol);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //load the Stock Data
            Price = Stock.getQuote().getPrice();
            GainAndLoss = Stock.getQuote().getChange();
            Ask = Stock.getQuote().getAsk();
            Bid = Stock.getQuote().getBid();

            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){

                txtPrice.setText(Price.toString());
                txtBid.setText(Bid.toString());
                txtAsk.setText(Ask.toString());

            }
        }


        @Override
        protected void onCancelled() {
            AuthTask = null;

        }
    }


}
