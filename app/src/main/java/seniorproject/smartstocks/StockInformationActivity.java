package seniorproject.smartstocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class StockInformationActivity extends AppCompatActivity {

    TextView Symbol;
    TextView Company;
    TextView Price;
    TextView GainAndLoss;
    TextView Open;
    TextView Close;
    TextView High;
    TextView Low;
    TextView WeekHigh;
    TextView WeekLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_information);
       Symbol = (EditText)findViewById(R.id.txtSymbol);
       Company =  (EditText)findViewById(R.id.txtCompany);
        Price = (EditText)findViewById(R.id.txtPrice);
        GainAndLoss = (EditText)findViewById(R.id.txtGainLoss);
        Open = (EditText)findViewById(R.id.txtOpen);
        Close = (EditText)findViewById(R.id.txtClose);
        High = (EditText)findViewById(R.id.txtDayHigh);
        Low = (EditText)findViewById(R.id.txtDayLow);
        WeekHigh= (EditText)findViewById(R.id.txt52weekhigh);
        WeekLow = (EditText)findViewById(R.id.txt52WeekLow);



    }
}
