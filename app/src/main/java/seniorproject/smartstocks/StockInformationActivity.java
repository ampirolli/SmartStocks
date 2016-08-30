package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;
import seniorproject.smartstocks.Classes.UserStock;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
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
    Button btnTrade;
    GraphView graph;

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
        btnTrade = (Button) findViewById(R.id.btnTrade);

        graph = (GraphView) findViewById(R.id.graph);

        try {
            AuthTask = new getStockDataTask(symbol);
            AuthTask.execute();
        }catch (Exception e) {
            e.printStackTrace();
        }


        GraphView graph = (GraphView) findViewById(R.id.graph);


        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StockInformationActivity.this, TradingActivity.class);
                i.putExtra("Session", currentSession.getUser_id());
                i.putExtra("Symbol", txtSymbol.getText());
                startActivity(i);
                finish();

            }
        });



    }

    public class getStockDataTask extends AsyncTask<Void, Void, Boolean> {

        String Symbol;
        Stock Stock;

        BigDecimal Price = new BigDecimal(0);
        BigDecimal GainAndLoss = new BigDecimal(0);
        BigDecimal Open = new BigDecimal(0);
        BigDecimal Close = new BigDecimal(0);
        BigDecimal High = new BigDecimal(0);
        BigDecimal Low = new BigDecimal(0);
        BigDecimal WeekHigh = new BigDecimal(0);
        BigDecimal WeekLow = new BigDecimal(0);

        List<BigDecimal> Intraday = new ArrayList<BigDecimal>();

        ArrayList<String> holdingsList = new ArrayList<String>();

        public getStockDataTask(String symbol) {

            Symbol = symbol;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //load the Stock Data
            try{

                Stock = YahooFinance.get(Symbol);

                Price = Stock.getQuote().getPrice();
                GainAndLoss =  Stock.getQuote().getChange(); //Price.subtract(Stock.getQuote().getOpen());
                Open = Stock.getQuote().getOpen();
                Close = Stock.getQuote().getPreviousClose();
                High = Stock.getQuote().getDayHigh();;
                Low = Stock.getQuote().getDayLow();;
                WeekHigh = Stock.getQuote().getYearHigh();
                WeekLow = Stock.getQuote().getYearLow();




                try {
                    URL url = new URL("http://chartapi.finance.yahoo.com/instrument/1.0/" + Symbol + "/chartdata;type=quote;range=1d/csv");
                    URLConnection con = url.openConnection();
                    InputStream is = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    String line = null;
                    String regex1 = "\\(";
                    String regex2 = "\\)";

                    int i = 0;
                    ArrayList<String> rawIntraday = new ArrayList<String>();
                    // read each line and write to System.out
                    while ((line = br.readLine()) != null) {
                        if(i == 17)
                        {

                            rawIntraday.add(line); // once prices begin in the csv

                        }else
                            i++;
                    }
                    ArrayList<String> minuteInterval = new ArrayList<String>();
                    for(String raw : rawIntraday){
                        List<String> items = Arrays.asList(raw.split(",")); // trim by comma
                        minuteInterval.add(items.get(1)); //adds every one minite data

                    }
                    i =0;
                    for(String minute : minuteInterval)
                    {
                        i++;
                        if(i == 1)
                            Intraday.add(new BigDecimal(minute));   //gets every 5 minute data from every one minute data
                        if(i == 5)
                            i = 0;

                    }


                }
                catch (IOException e) {
                    e.printStackTrace();
                    e.toString();
                    return false;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }



            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }



            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success){

                txtPrice.setText(Price.toString());
                txtGainAndLoss.setText(GainAndLoss.toString());
                txtOpen.setText(Open.toString());
                txtClose.setText(Close.toString());
                txtHigh.setText(High.toString());
                txtLow.setText(Low.toString());
                txtWeekHigh.setText(WeekHigh.toString());
                txtWeekLow.setText(WeekLow.toString());


                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                int i = 0;
                BigDecimal largest = new BigDecimal(0);
                BigDecimal smallest = new BigDecimal(100000000);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setXAxisBoundsManual(true);

                for(BigDecimal fiveMinute : Intraday) {
                    series.appendData(new DataPoint(i , fiveMinute.doubleValue()), false, 78);
                    if(largest.compareTo(fiveMinute) == -1){
                        largest = fiveMinute;
                        graph.getViewport().setMaxY(largest.doubleValue()); //does nothing right now
                    }
                    if(smallest.compareTo(fiveMinute) == 1){
                        smallest = fiveMinute;
                        graph.getViewport().setMinY(smallest.doubleValue()); // does nothing right now
                    }
                    i++;
                }
                graph.getViewport().setMaxX(78);
                graph.addSeries(series);



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
