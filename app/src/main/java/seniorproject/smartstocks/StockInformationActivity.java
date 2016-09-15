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
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
    Button btnFavorite;
    GraphView graph;

    private getStockDataTask AuthTask = null;
    private getFavoritesTask AuthTask2 = null;
    private isFavoriteTask AuthTask3 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_information);

        Intent previousIntent = getIntent();
        String symbol = previousIntent.getStringExtra("Symbol");

        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();


        txtSymbol = (TextView)findViewById(R.id.txtSymbol);
        txtSymbol.setText(symbol);
        txtCompany =  (TextView)findViewById(R.id.txtCompany);

        txtPrice = (TextView)findViewById(R.id.txtPrice);
        txtGainAndLoss = (TextView)findViewById(R.id.txtGainLoss);
        txtOpen = (TextView)findViewById(R.id.txtOpen);
        txtClose = (TextView)findViewById(R.id.txtClose);
        txtHigh = (TextView)findViewById(R.id.txtDayHigh);
        txtLow = (TextView)findViewById(R.id.txtDayLow);
        txtWeekHigh= (TextView)findViewById(R.id.txt52weekhigh);
        txtWeekLow = (TextView)findViewById(R.id.txt52WeekLow);
        btnTrade = (Button) findViewById(R.id.btnTrade);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);

        AuthTask2 = new getFavoritesTask(currentSession.getUser_id(), txtSymbol.getText().toString());
        AuthTask2.execute();

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

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnFavorite.getText().equals("add to favorites"))
                    AuthTask3 = new isFavoriteTask(false, currentSession.getUser_id(), txtSymbol.getText().toString());
                else if(btnFavorite.getText().equals("remove from favorites"))
                    AuthTask3 = new isFavoriteTask(true, currentSession.getUser_id(), txtSymbol.getText().toString());

                AuthTask3.execute();


            }
        });



    }

    public class getStockDataTask extends AsyncTask<Void, Void, Boolean> {

        String Symbol;
        Stock Stock;
        String Company;

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

            try {

                Stock = YahooFinance.get(Symbol);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //load the Stock Data
            try{



                Price = Stock.getQuote().getPrice();
                Company = Stock.getName();
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
                        minuteInterval.add(items.get(4)); //adds every one minite data

                    }
                    i =0;
                    for(String minute : minuteInterval)
                    {
                        i++;
                        if(i == 1)
                            Intraday.add(new BigDecimal(minute).setScale(2, RoundingMode.CEILING));   //gets every 5 minute data from every one minute data
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

                txtCompany.setText(Company);

                txtPrice.setText(Price.toString());
                txtCompany.setText(Stock.getName());
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

    public class getFavoritesTask extends AsyncTask<Void, Void, Boolean> {

        Integer user_id;
        String Symbol;

        public getFavoritesTask(Integer user_id, String symbol) {

            this.user_id = user_id;
            Symbol = symbol;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return getFavoriteBySymbol();
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask2 = null;

            if (success) {
                btnFavorite.setText("remove from favorites");


            }else{
                btnFavorite.setText("add to favorites");
            }
        }

        @Override
        protected void onCancelled() {
            AuthTask2 = null;

        }

        public boolean getFavoriteBySymbol() {
            LICS loginConnectionString = new LICS();
            String connectionUrl = loginConnectionString.LoginConnectionString();

            boolean isFavorite = false;

            // Declare the JDBC objects.
            Connection conn = null;
            Statement stmt = null;
            ResultSet result = null;
            //Declare email+password
            ArrayList<Account> accountList = new ArrayList<>();

            try {
                // Establish the connection.
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                conn = DriverManager.getConnection(connectionUrl);
                // Create and execute an SQL statement that returns some data.

                String SQL = "SELECT * FROM dbo.UserFavorites WHERE user_id = " + user_id + " and stock_symbol = '" + Symbol + "';";
                stmt = conn.createStatement();
                result = stmt.executeQuery(SQL);
                int counter = 0;
                if (result.next() == false)
                    isFavorite = false;
                else
                    isFavorite = true;



            }

            // Handle any errors that may have occurred.
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            finally {
                if (result != null) try { result.close(); } catch(Exception e) {}
                if (stmt != null) try { stmt.close(); } catch(Exception e) {}
                if (conn != null) try { conn.close(); } catch(Exception e) {}
                return isFavorite;
            }
        }


    }

    public class isFavoriteTask extends AsyncTask<Void, Void, Boolean> {

        boolean IsFavorite;
        String StockSymbol;
        Integer user_id;

        public isFavoriteTask(boolean isFavorite, Integer user_id, String stockSymbol) {

            IsFavorite = isFavorite;
            this.user_id = user_id;
            StockSymbol = stockSymbol;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (!IsFavorite)
                updateFavorites("sp_add_favorite " + user_id + ", '"+ StockSymbol +"';");
            else
                updateFavorites("sp_remove_favorite " + user_id + ", '"+ StockSymbol +"';");
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask3 = null;

            if (success) {
                AuthTask2 = new getFavoritesTask(currentSession.getUser_id(), txtSymbol.getText().toString());
                AuthTask2.execute();


            }
        }

        @Override
        protected void onCancelled() {
            AuthTask3 = null;

        }

        public void updateFavorites(String SQL) {
            LICS loginConnectionString = new LICS();
            String connectionUrl = loginConnectionString.LoginConnectionString();

            // Declare the JDBC objects.
            Connection conn = null;
            Statement stmt = null;
            ResultSet result = null;
            //Declare email+password
            ArrayList<Account> accountList = new ArrayList<>();

            try {
                // Establish the connection.
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                conn = DriverManager.getConnection(connectionUrl);
                // Create and execute an SQL statement that returns some data.


                stmt = conn.createStatement();
                stmt.executeQuery(SQL);
                int counter = 0;


            }

            // Handle any errors that may have occurred.
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (stmt != null) try { stmt.close(); } catch(Exception e) {}
                if (conn != null) try { conn.close(); } catch(Exception e) {}

            }
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(StockInformationActivity.this, MainActivity.class);
        i.putExtra("Session", currentSession.getUser_id());
        startActivity(i);
        this.finish();
    }
}
