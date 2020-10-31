package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WithdrawalTransactionInProgressActivity extends AppCompatActivity {

    String transactionCode;
    Button btGoToHome,btnMyOperations;
    TextView txtTransactionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_transaction_in_progress);

        transactionCode = getIntent().getExtras().get("TransactionCode").toString();

        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);

        txtTransactionCode.setText("Código de transacción: "+transactionCode);

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WithdrawalTransactionInProgressActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WithdrawalTransactionInProgressActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
