package oliver.oalone.oalonedevelopers.oalonebank_fintech.ForexExchange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.MyCompanyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.SucessfullPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class FxOperationSuccessfullActivity extends AppCompatActivity {

    TextView txtMainAmmount,txtTransactionCode;
    String buy_ammount,TransactionCode;
    Button btGoToHome,btnMyOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fx_operation_successfull);

        buy_ammount = getIntent().getExtras().get("buy_ammount").toString();
        TransactionCode = getIntent().getExtras().get("TransactionCode").toString();
        txtMainAmmount = findViewById(R.id.txtMainAmmount);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);
        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);

        txtMainAmmount.setText(buy_ammount);
        txtTransactionCode.setText("Código de transacción: "+TransactionCode);

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FxOperationSuccessfullActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FxOperationSuccessfullActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
