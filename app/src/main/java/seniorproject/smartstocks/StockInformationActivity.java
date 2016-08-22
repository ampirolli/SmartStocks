package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;
import seniorproject.smartstocks.Classes.UserStock;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.Interval;

public class StockInformationActivity extends AppCompatActivity {

    Session currentSession;

    TextView txtSymbol;
    TextView txtCompany;
    TextView txtPrice;
    TextView txtGainAndLoss;
    TextView txtOpen;
    TextView txtClose;
    TextView txtHigh;
    TextView txtLow;
    TextView txtWeekHigh;
    TextView txtWeekLow;

    private getStockDataTask AuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_information);

        Intent previousIntent = getIntent();
        String symbol = previousIntent.getStringExtra("Symbol");
        String company = previousIntent.getStringExtra("Company");

        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        txtSymbol = (TextView)findViewById(R.id.txtSymbol);
        txtSymbol.setText(symbol);
        txtCompany =  (TextView)findViewById(R.id.txtCompany);
        txtCompany.setText(company);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        txtGainAndLoss = (TextView)findViewById(R.id.txtGainLoss);
        txtOpen = (TextView)findViewById(R.id.txtOpen);
        txtClose = (TextView)findViewById(R.id.txtClose);
        txtHigh = (TextView)findViewById(R.id.txtDayHigh);
        txtLow = (TextView)findViewById(R.id.txtDayLow);
        txtWeekHigh= (TextView)findViewById(R.id.txt52weekhigh);
        txtWeekLow = (TextView)findViewById(R.id.txt52WeekLow);

        try {
            AuthTask = new getStockDataTask(symbol);
            AuthTask.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class getStockDataTask extends AsyncTask<Void, Void, Boolean> {

        Stock Stock;

        String Company;
        BigDecimal Price = new BigDecimal(0);
        BigDecimal GainAndLoss = new BigDecimal(0);;
        BigDecimal Open = new BigDecimal(0);;
        BigDecimal Close = new BigDecimal(0);;
        BigDecimal High = new BigDecimal(0);;
        BigDecimal Low = new BigDecimal(0);;
        BigDecimal WeekHigh = new BigDecimal(0);;
        BigDecimal WeekLow = new BigDecimal(0);;

        ArrayList<String> holdingsList = new ArrayList<String>();

        public getStockDataTask(String symbol) throws IOException {

            Stock = YahooFinance.get(symbol);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //load the Stock Data
            try{

               // Price = new BigDecimal(String.valueOf(Stock.getQuote()));
               // GainAndLoss =  new BigDecimal(Stock.toString());
               //Open;
               // Close;
                //High;
                //Low;
                WeekHigh = (BigDecimal) this.Stock.getHistory(Interval.WEEKLY);
                //WeekLow;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            java.util.Date todaysDate = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String today = sdf.format(todaysDate);





            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){

                txtCompany.setText(Company);
                txtPrice.setText(Price.toString());
                txtGainAndLoss.setText(GainAndLoss.toString());
                txtOpen.setText(Open.toString());
                txtClose.setText(Close.toString());
                txtHigh.setText(High.toString());
                txtLow.setText(Low.toString());
                txtWeekHigh.setText(WeekHigh.toString());
                txtWeekLow.setText(WeekLow.toString());

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
