package seniorproject.smartstocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import seniorproject.smartstocks.Classes.Session;
import yahoofinance.Stock;

public class TradingPreviewActivity extends AppCompatActivity {

Session currentSession;
TextView StockName;
    TextView Price;
    TextView Bid;
    TextView Ask;
    TextView AccountType;
    TextView OrderType;
    TextView Shares;
    TextView PriceType;
    TextView StopPrice;
    TextView Term;
    TextView EstimatedCommision;
    TextView EstimatedTotalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_preview);
        StockName =(EditText)findViewById(R.id.txtStockName);
        Price =(EditText)findViewById(R.id.txtPrice);
        Bid =(EditText)findViewById(R.id.txtBid);
        Ask =(EditText)findViewById(R.id.txtAsk);
        AccountType =(EditText)findViewById(R.id.txtAccountType);
        OrderType =(EditText)findViewById(R.id.txtOrderType);
        Shares =(EditText)findViewById(R.id.txtShares);
        PriceType =(EditText)findViewById(R.id.txtPriceType);
        StopPrice =(EditText)findViewById(R.id.txtStopPrice);
        Term = (EditText)findViewById(R.id.txtTerm);
        EstimatedCommision = (EditText)findViewById(R.id.txtEstimatedCommision);
        EstimatedTotalCost = (EditText)findViewById(R.id.txtEstimatedTotalCost);

    }
}
