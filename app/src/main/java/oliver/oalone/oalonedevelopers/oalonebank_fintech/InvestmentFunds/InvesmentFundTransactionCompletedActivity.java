package oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class InvesmentFundTransactionCompletedActivity extends AppCompatActivity {

    String postKey,transactionCode;
    Button btGoToHome,btnMyOperations;
    TextView txtTransactionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invesment_fund_transaction_completed);

        //postKey = getIntent().getExtras().get("PostKey").toString();
        transactionCode = getIntent().getExtras().get("TransactionCode").toString();

        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);
        txtTransactionCode.setText("Código de transacción: "+transactionCode);
        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvesmentFundTransactionCompletedActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvesmentFundTransactionCompletedActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
