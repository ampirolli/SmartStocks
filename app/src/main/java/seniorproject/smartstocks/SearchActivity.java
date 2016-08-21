package seniorproject.smartstocks;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.*;
import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;
import yahoofinance.YahooFinance;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Session currentSession;

    SearchView searchView;
    ListView lvResults;
    private searchTask AuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0));  //loads current session into intent
        currentSession.getUser_id();

        lvResults = (ListView) findViewById(R.id.lvResults);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedFromList = (String) lvResults.getItemAtPosition(position);
                String[] selectedStock = selectedFromList.split("-");
                String symbol = selectedStock[0];
                Intent i = new Intent(SearchActivity.this, StockInformationActivity.class); //creates intent that launches Balances
                i.putExtra("Session", currentSession.getUser_id());
                i.putExtra("Symbol", symbol);

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        AuthTask = new searchTask(newText);
        AuthTask.execute();


        return false;
    }

    public class searchTask extends AsyncTask<Void, Void, Boolean> {

        ArrayList<String> stockResults = new ArrayList<String>();
        String Symbol;

        public searchTask(String symbol) {
            this.Symbol = symbol;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                URL url = new URL("http://d.yimg.com/aq/autoc?query=" + Symbol + "&region=US&lang=en-US&callback=YAHOO.util.ScriptNodeDataSource.callbacks");
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String line = null;
                String regex1 = "\\(";
                String regex2 = "\\)";

                // read each line and write to System.out
                while ((line = br.readLine()) != null) {

                    String[] parts = line.split(regex1);
                    String[] json = parts[1].split(regex2);
                    String jsonString = json[0].toString();

                    JSONObject jObject  = new JSONObject(jsonString); // json
                    JSONObject resultSet = jObject.getJSONObject("ResultSet"); // get data object
                    JSONArray results = resultSet.getJSONArray("Result"); // user JSONArray because Result begins an array of all results


                    for(int i = 0; i < results.length(); i++)
                    {
                        JSONObject stocks = results.getJSONObject(i);
                        String symbol = stocks.getString("symbol");
                        String company = stocks.getString("name");

                        String result = symbol + "\n-" + company;
                        stockResults.add(result);
                        //Iterate through the elements of the array i.
                        //Get thier value.
                        //Get the value for the first element and the value for the last element.
                    }

                }
                return true;

            }
            catch (IOException e) {
                e.printStackTrace();
                e.toString();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask = null;

            if (success) {
                List<String> listItems= new ArrayList<String>();
                listItems = stockResults;
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, listItems);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvResults.setAdapter(arrayAdapter);
                lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String selectedItem = lvResults.getItemAtPosition(position).toString();
                        String[] selectedItemSplit = selectedItem.split("-");
                        String selectedSymbol = selectedItemSplit[0];
                        Intent i = new Intent(SearchActivity.this, StockInformationActivity.class); //creates intent that launches Balances
                        i.putExtra("Session", currentSession.getUser_id());
                        i.putExtra("Symbol", selectedSymbol);
                        finish();

                    }
                });
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
