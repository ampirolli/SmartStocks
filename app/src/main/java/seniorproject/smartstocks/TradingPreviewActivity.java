package seniorproject.smartstocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import seniorproject.smartstocks.Classes.Session;
import yahoofinance.Stock;

public class TradingPreviewActivity extends AppCompatActivity {

Session currentSession;
TextView txtStockName;
    TextView txtPrice;
    TextView txtBid;
    TextView txtAsk;
    TextView txtAccountType;
    TextView txtOrderType;
    TextView txtShares;
    TextView txtPriceType;
    TextView txtStopPrice;
    TextView txtTerm;
    TextView txtEstimatedCommision;
    TextView txtEstimatedTotalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_preview);
        txtStockName =(EditText)findViewById(R.id.txtStockName);
        txtPrice =(EditText)findViewById(R.id.txtPrice);
        txtBid =(EditText)findViewById(R.id.txtBid);
        txtAsk =(EditText)findViewById(R.id.txtAsk);
        txtAccountType =(EditText)findViewById(R.id.txtAccountType);
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
        txtPrice.setText(previousIntent.getStringExtra("Asking_Price"));

        txtAccountType.setText(previousIntent.getStringExtra("Account_Number"));



    }

}
