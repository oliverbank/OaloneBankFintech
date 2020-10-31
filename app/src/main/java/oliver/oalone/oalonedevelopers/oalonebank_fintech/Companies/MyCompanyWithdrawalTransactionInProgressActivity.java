package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WithdrawalTransactionInProgressActivity;

public class MyCompanyWithdrawalTransactionInProgressActivity extends AppCompatActivity {

    String transactionCode,my_company_key;
    Button btGoToHome,btnMyOperations;
    TextView txtTransactionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_withdrawal_transaction_in_progress);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        transactionCode = getIntent().getExtras().get("TransactionCode").toString();

        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);

        txtTransactionCode.setText("Código de transacción: "+transactionCode);

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyWithdrawalTransactionInProgressActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyWithdrawalTransactionInProgressActivity.this, MyCompanyOperationsActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
                finish();
            }
        });
    }
}
