package seniorproject.smartstocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;
import seniorproject.smartstocks.Classes.User;
import seniorproject.smartstocks.Classes.UserStock;
import yahoofinance.YahooFinance;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Session currentSession;
    ListView lvFavorites;
    ListView lvHoldings;
    private getPortfolioTask AuthTask1 = null;
    private getFavoritesTask AuthTask2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvFavorites =(ListView) findViewById(R.id.lvFavorites);
        lvHoldings =(ListView) findViewById(R.id.lvHoldings);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("USER_ID", 0));

        AuthTask1 = new getPortfolioTask(currentSession.getUser_id());
        AuthTask1.execute();
        AuthTask2 = new getFavoritesTask(currentSession.getUser_id());
        AuthTask2.execute();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accounts) {
            Intent i = new Intent(MainActivity.this, AccountsActivity.class); //creates intent that launches main menu

            startActivity(i);
            finish();
        } else if (id == R.id.nav_tools) {

            Intent i = new Intent(MainActivity.this, ToolsActivity.class); //creates intent that launches main menu

            startActivity(i);
            finish();

        } else if (id == R.id.nav_personal) {

            Intent i = new Intent(MainActivity.this, PersonalInfoActivity.class); //creates intent that launches main menu

            startActivity(i);
            finish();

        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            this.finish();

        } else if (id == R.id.nav_search){
            Intent i = new Intent(MainActivity.this, SearchActivity.class);

            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class getPortfolioTask extends AsyncTask<Void, Void, Boolean> {

        Integer User_ID;
        ArrayList<String> holdingsList = new ArrayList<String>();

        public getPortfolioTask(Integer user_id){
            User_ID = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            User user = new User(User_ID);
            user.setAccounts();
            for(Account account : user.getAccounts()){
                account.setHoldings(Integer.valueOf(account.getAccountNumber()));

                for(UserStock userStock: account.getHoldings()){
                    if(!userStock.getStockSymbol().isEmpty()) {
                        BigDecimal profit = userStock.getStock().getQuote().getPrice().multiply(new BigDecimal(userStock.getQuantity())).subtract(userStock.getPricePaid());
                        holdingsList.add(userStock.getStockSymbol() + "- " +"Account: " +userStock.getAccountID() + " Quantity: " + userStock.getQuantity()+" Price Paid: " +userStock.getPricePaid() +" Profit:" + profit );
                    }
                }
            }

            return true;
        }

        protected void onPostExecute(final Boolean success) {

            AuthTask1 = null;

            if(success){
                //load the spinner with a list of accounts
                List<String> holdingList = holdingsList;

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, holdingList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvHoldings.setAdapter(arrayAdapter);

                lvHoldings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        String selectedFromList = (String) lvHoldings.getItemAtPosition(position);
                        String[] selectedStock = selectedFromList.split("-");
                        String symbol = selectedStock[0];

                        Intent i = new Intent(MainActivity.this, StockInformationActivity.class); //creates intent that launches Balances
                        i.putExtra("Session", currentSession.getUser_id());
                        i.putExtra("Symbol", symbol);

                        startActivityForResult(i, 1);
                        finish();


                    }
                });


            }
        }


        @Override
        protected void onCancelled() {
            AuthTask1 = null;

        }
    }

    public class getFavoritesTask extends AsyncTask<Void, Void, Boolean> {

        Integer user_id;
        ArrayList<String>favoritesList = new ArrayList<>();
        public getFavoritesTask(Integer user_id) {
            this.user_id = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User user = new User(user_id);
            favoritesList = user.setFavorites();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            AuthTask2 = null;

            if (success) {
                //load the spinner with a list of accounts
                List<String> favoriteSymbols= new ArrayList<String>();
                favoriteSymbols = favoritesList;
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, favoriteSymbols);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lvFavorites.setAdapter(arrayAdapter);

                lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        String selectedFromList = (String) lvFavorites.getItemAtPosition(position);
                        String[] selectedStock = selectedFromList.split("-");
                        String symbol = selectedStock[0];

                        Intent i = new Intent(MainActivity.this, StockInformationActivity.class); //creates intent that launches Balances
                        i.putExtra("Session", currentSession.getUser_id());
                        i.putExtra("Symbol", symbol);

                        startActivityForResult(i, 1);
                        finish();


                    }
                });

            }
        }

        @Override
        protected void onCancelled() {
            AuthTask2 = null;

        }


    }

}
