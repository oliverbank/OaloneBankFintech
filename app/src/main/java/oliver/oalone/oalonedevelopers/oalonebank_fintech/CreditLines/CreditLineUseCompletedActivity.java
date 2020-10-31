package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CreditLineUseCompletedActivity extends AppCompatActivity {

    String postKey,transactionCode,ammount,currency;
    Button btGoToHome;
    TextView txtTransactionCode,txtAmmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_line_use_completed);

        //postKey = getIntent().getExtras().get("PostKey").toString();
        transactionCode = getIntent().getExtras().get("TransactionCode").toString();
        ammount = getIntent().getExtras().get("ammount").toString();
        currency = getIntent().getExtras().get("currency").toString();

        btGoToHome = findViewById(R.id.btGoToHome);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);
        txtAmmount = findViewById(R.id.txtAmmount);

        if (currency.equals("PEN")) {
            txtAmmount.setText("S/"+ammount+" "+currency);
        }
        if (currency.equals("USD")) {
            txtAmmount.setText("$"+ammount+" "+currency);
        }
        txtTransactionCode.setText("Código de transacción: "+transactionCode);
        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditLineUseCompletedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
