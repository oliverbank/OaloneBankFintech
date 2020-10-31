package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanReady;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class SuccessfulLoanActivity extends AppCompatActivity {

    TextView txtAmount,txtTransactionCode;
    Button btGoToHome;
    String amount,currency,transaction_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_loan);

        txtAmount = findViewById(R.id.txtAmount);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);
        btGoToHome = findViewById(R.id.btGoToHome);

        amount = getIntent().getExtras().getString("amount");
        currency = getIntent().getExtras().getString("currency");
        transaction_code = getIntent().getExtras().getString("transaction_code");

        txtAmount.setText(amount+" "+currency);
        txtTransactionCode.setText("Código de transacción: "+transaction_code);

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessfulLoanActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}