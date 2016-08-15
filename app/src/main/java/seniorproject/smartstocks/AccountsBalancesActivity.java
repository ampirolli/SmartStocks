package seniorproject.smartstocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import seniorproject.smartstocks.Classes.Account;
import seniorproject.smartstocks.Classes.Session;

public class AccountsBalancesActivity extends AppCompatActivity {

    Session currentSession;
    ArrayList<Account> accountsList = new  ArrayList<Account>() ; //list to save accounts nickname + number
    ArrayList<String> accountsNumberList = new  ArrayList<String>() ; //list to save the selected accounts account number
    String accountSelectionValue = new String(); //String to resolve which account was selected
    Integer accountSelectionIndex = new Integer(0); // String to resolve the index oof the selected account
    ArrayList<String> balances = new ArrayList<String>(); // array to get balance of selected account


    Spinner spAccounts;
    TextView txtAccountValue;
    TextView txtNetValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_balances);

        Intent previousIntent = getIntent();
        currentSession = Session.getInstance(previousIntent.getIntExtra("Session", 0)); //loads current session into intent
        currentSession.getUser_id();
        accountsList = previousIntent.getParcelableArrayListExtra("AccountsList"); // pulls account list from previous intent for loading the spinner
        accountsNumberList = previousIntent.getStringArrayListExtra("AccountsNumberList"); //pulls account number from the previous intent to call sql statments regarding the account
        accountSelectionValue = previousIntent.getStringExtra("SelectedAccount"); // pulls selected account from previous intent
        accountSelectionIndex = previousIntent.getIntExtra("SelectedIndex", 0); //pull selected accounts index from previous intent

        spAccounts = (Spinner) findViewById(R.id.spAccount);
        txtAccountValue = (TextView) findViewById(R.id.txtAccountValue);
        txtNetValue = (TextView) findViewById(R.id.txtNetAsset);

        //load the spinner with a list of accounts
        List<String> accountsNickname= new ArrayList<String>();
        for (Account account: accountsList) {
            if(account.getNickname().equals(""))
                accountsNickname.add(account.getAccountNumber());
            else
                accountsNickname.add(account.getNickname() + "-" + account.getAccountNumber());
            accountsNumberList.add(account.getAccountNumber());
            balances.add(account.getBalance());

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountsNickname);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAccounts.setAdapter(arrayAdapter);
        spAccounts.setSelection(accountSelectionIndex);
        txtAccountValue.setText(balances.get(accountSelectionIndex).toString());

        BigDecimal netBalance = new BigDecimal(0);
        for(String balance: balances){
           netBalance = netBalance.add(new BigDecimal(balance.toString()));

        }

        txtNetValue.setText(netBalance.toString());


        spAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                accountSelectionValue = accountsNumberList.get(i);
                accountSelectionIndex = i;
                txtAccountValue.setText(balances.get(accountSelectionIndex).toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });



    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}
