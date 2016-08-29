package seniorproject.smartstocks.Classes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import seniorproject.smartstocks.R;

public class TradingPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_preview);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
