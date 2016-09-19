package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import seniorproject.smartstocks.Classes.Session;

public class ToolsStockAnalyzerRunActivity extends AppCompatActivity {

    Session currentSession;
    TextView tvTitle;
    GraphView graph;
    ListView lvResults;

    AnalyzeStockTask AuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_stock_analyzer_run);

        currentSession = Session.getInstance(0);

        tvTitle = (TextView) findViewById(R.id.tvSymbol);
        graph = (GraphView) findViewById(R.id.graph);
        lvResults = (ListView) findViewById(R.id.lvResults);

        Intent previousIntent = getIntent();
        String symbol = previousIntent.getStringExtra("Symbol");
        BigDecimal balance = new BigDecimal(previousIntent.getStringExtra("Balance"));

        AuthTask = new AnalyzeStockTask(symbol, balance);
        AuthTask.execute();
    }
    public class AnalyzeStockTask extends AsyncTask<Void, Void, Boolean> {

        String Symbol;
        BigDecimal Balance;
        BigDecimal StartingBalance;

        ArrayList<String> Result = new ArrayList<String>();
        ArrayList<BigDecimal> testQuotes = new ArrayList<BigDecimal>();

        final BigDecimal transactionPrice = new BigDecimal("9.99");

        BigDecimal assets;			// Account Information
        BigDecimal profit;			//
        BigDecimal numberOfShares = new BigDecimal(0);	//

        BigDecimal stopBelow = new BigDecimal(0); 	// .05 below entry point
        BigDecimal stopAbove = new BigDecimal(0); 	// .10 cent target above entry point
        BigDecimal stopAbovePoint = new BigDecimal(.08);
        BigDecimal stopBelowPoint = new BigDecimal(.04);			//
        BigDecimal lastQuote = new BigDecimal(0); 			// Stock Data
        BigDecimal stockHighPoint = new BigDecimal(0); 		//
        BigDecimal stockLowPoint = new BigDecimal(0); 		//

        int openRangeCounter = 0;	//

        boolean openRangeComplete = false;
        boolean hasBrokenRange = false;
        boolean hasBrokenLow = false;
        boolean hasSold = false;


        public AnalyzeStockTask(String symbol, BigDecimal balance) {
            Symbol = symbol;
            Balance = balance;
            StartingBalance = balance;

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            BigDecimal stockQuote = new BigDecimal(0);
            BigDecimal lastQuote =new BigDecimal(0);
            BigDecimal stockHighPoint = new BigDecimal(0);
            BigDecimal stockLowPoint = new BigDecimal(0);

            int depressionCount = 0;
            int increaseCount = 0;

            boolean isLong;
            boolean isShort;

            BigDecimal breakPrice;

            BigDecimal stopBelow = new BigDecimal(0); // .05 below entry point
            BigDecimal stopAbove = new BigDecimal(0);// .10 cent target above entry point
            BigDecimal stopAbovePoint = new BigDecimal(.04);
            BigDecimal stopBelowPoint = new BigDecimal(.02);

            testQuotes = stocksIntraday(Symbol);

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.set(Calendar.HOUR, 9);
            cal.set(Calendar.MINUTE, 30);
            cal.set(Calendar.AM_PM, Calendar.AM );
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            for(BigDecimal quotes : testQuotes){
                //begin

                if (stockQuote.equals(0) == false)
                    lastQuote = stockQuote;
                stockQuote = new BigDecimal(quotes.toString());

                if(openRangeCounter == 6){
                    openRangeComplete = true;

                }else{
                    openRangeCounter++;
                    openRangeComplete = false;
                }

                if(openRangeComplete == true && hasSold == false){
                    if(stockQuote.compareTo(stockHighPoint) == 1){

                        if(hasBrokenRange == true){ // checks if range has been broken,

                            if(stockQuote.compareTo(lastQuote) == 1)
                                increaseCount++;
                            else if(stockQuote.compareTo(lastQuote) == -1)
                                depressionCount++;

                            if(stockQuote.compareTo(stopAbove) == 1 || stockQuote.compareTo(stopAbove) ==0){
                                stopAbove = stockQuote.add(stopAbovePoint);
                                stopBelow = stockQuote.subtract(stopBelowPoint);
                            }
                            else if(stockQuote.compareTo(stopBelow) == -1 || stockQuote.compareTo(stopBelow) == 0){
                                sellAll(stockQuote);
                                resetAlgo();
                            }
                            else{
                                if(stockQuote.compareTo(lastQuote) == -1 && stockQuote.compareTo(stopBelow) == 1 && stockQuote.compareTo(stopAbove) == -1){

                                    buy(stockQuote);

                                }

                            }

                        }else{	//if not, sets it to hasBroken range to true
                            buy(stockQuote); //buys stock

                            stopAbove = stockQuote.add(stopAbovePoint);
                            stopBelow = stockQuote.subtract(stopBelowPoint);

                            hasBrokenRange = true;
                            hasBrokenLow = false;
                        }


                    }else if(stockQuote.compareTo(stockLowPoint) == -1){
                        sellAll(stockQuote);
                        hasBrokenLow = true;
                        hasBrokenRange = false;
                        resetAlgo();

                    }

                }else{
                    if(stockHighPoint.equals(0) == true)
                        stockHighPoint = stockQuote;
                    if(stockLowPoint.equals(0) == true)
                        stockLowPoint = stockQuote;
                    if(stockQuote.compareTo(stockHighPoint) == 1)
                        stockHighPoint = stockQuote;
                    if(stockQuote.compareTo(stockHighPoint) == -1)
                        stockLowPoint = stockQuote;
                }

                //end

                assets = numberOfShares.multiply(stockQuote);
                String resultStr = "";

                String date = sdf.format(cal.getTime());
                Result.add("Time: " + date);
                Result.add("Current Price: " + stockQuote);
                Result.add("\nAmount of shares: " + numberOfShares);
                Result.add("\nTotal: " + (Balance.add(assets)));
                Result.add("-------------------------------------------------");

                cal.add(Calendar.MINUTE, 5);
            }


            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if(success) {

                List<String> fiveMinuteResults = Result;

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ToolsStockAnalyzerRunActivity.this, android.R.layout.simple_spinner_item, fiveMinuteResults);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvResults.setAdapter(arrayAdapter);LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                int i = 0;
                BigDecimal largest = new BigDecimal(0);
                BigDecimal smallest = new BigDecimal(100000000);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setXAxisBoundsManual(true);

                Calendar cal = Calendar.getInstance(); // creates calendar
                cal.setTime(new Date()); // sets calendar time/date
                cal.set(Calendar.HOUR, 9);
                cal.set(Calendar.MINUTE, 30);
                cal.set(Calendar.AM_PM, Calendar.AM );

                for(BigDecimal fiveMinute : testQuotes) {
                    Date d1 = cal.getTime();

                    series.appendData(new DataPoint( i, fiveMinute.doubleValue()), false, 78);
                    if(largest.compareTo(fiveMinute) == -1){
                        largest = fiveMinute;
                        graph.getViewport().setMaxY(largest.doubleValue()); //does nothing right now
                    }
                    if(smallest.compareTo(fiveMinute) == 1){
                        smallest = fiveMinute;
                        graph.getViewport().setMinY(smallest.doubleValue()); // does nothing right now
                    }
                    cal.add(Calendar.MINUTE, 5);
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

        void buy(BigDecimal stockQuote) {
            BigDecimal buyQuantity = buyQuantity(stockQuote);
            BigDecimal buyTotal = stockQuote.multiply(buyQuantity) ;
            buyTotal = buyTotal.add(transactionPrice);
            if((Balance.subtract(buyTotal)).doubleValue() > 0 && buyQuantity.doubleValue() > 0){
                Balance = Balance.subtract(buyTotal);
                numberOfShares = numberOfShares.add(buyQuantity);
                Result.add("Buy Executed @ $" + stockQuote);
            }

        }
        void sellAll(BigDecimal stockQuote){
            if(numberOfShares.doubleValue() > 0){
                assets = stockQuote.multiply(numberOfShares);
                Balance = Balance.add(assets);
                Balance = Balance.subtract(transactionPrice);
                numberOfShares = new BigDecimal(0);
                Result.add("Sell Executed @ $" + stockQuote);
                hasSold = true;
            }

        }
        BigDecimal buyQuantity(BigDecimal stockQuote){


            int quantity = (int)Balance.divide(stockQuote, 2, RoundingMode.DOWN).doubleValue();
            int i = 1;
            BigDecimal subractByTansactionPrice = new BigDecimal(0);
            while(true){
                subractByTansactionPrice = stockQuote.multiply(new BigDecimal(i));
                if(subractByTansactionPrice.doubleValue() >= transactionPrice.doubleValue())
                {
                    quantity = quantity - i;
                    break;
                }
                i++;
            }

            BigDecimal buyQuantity = new BigDecimal(quantity);
            return buyQuantity;

        }
        void resetAlgo(){
            hasBrokenRange = false;
            openRangeComplete = false;
            openRangeCounter = 0;
            stopBelow = new BigDecimal(0); 	// .05 below entry point
            stopAbove = new BigDecimal(0);

        }

        ArrayList<BigDecimal> stocksIntraday(String symbol){
            ArrayList<BigDecimal> intraday = new ArrayList<BigDecimal>();
            try {
                URL url = new URL("http://chartapi.finance.yahoo.com/instrument/1.0/" + symbol + "/chartdata;type=quote;range=1d/csv");
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
                        intraday.add(new BigDecimal(minute).setScale(2, RoundingMode.CEILING));//gets every 5 minute data from every one minute data
                    if(i == 5)
                        i = 0;

                }


            }
            catch (IOException e) {
                e.printStackTrace();
                e.toString();
                return intraday;
            }
            catch (Exception e) {
                e.printStackTrace();
                return intraday;
            }
            return intraday;



        }


    }
}
