package seniorproject.smartstocks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ToolsStockAnalyzerActivity extends AppCompatActivity {



    EditText txtSymbol;
    EditText txtBalance;
    Button btnAnalyze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_stock_analyzer);


        txtSymbol = (EditText) findViewById(R.id.txtSymbol);
        txtBalance = (EditText) findViewById(R.id.txtBalance);
        btnAnalyze = (Button) findViewById(R.id.btnAnalyze);

        btnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ToolsStockAnalyzerActivity.this, ToolsStockAnalyzerRunActivity.class); //creates intent that launches Portfolio
                i.putExtra("Symbol", txtSymbol.getText().toString());
                i.putExtra("Balance", txtBalance.getText().toString());
                startActivity(i);
            }
        });



    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
