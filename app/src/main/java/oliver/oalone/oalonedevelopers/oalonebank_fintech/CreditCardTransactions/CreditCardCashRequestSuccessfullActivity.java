package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.SucessfullTransferActivity;

public class CreditCardCashRequestSuccessfullActivity extends AppCompatActivity {

    TextView txtMainAmmount,txtTransactionCode;
    String main_currency,TransactionCode,request_amount;
    Button btGoToHome,btnMyOperations,btGoMyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_cash_request_successfull);

        main_currency = getIntent().getExtras().get("main_currency").toString();
        request_amount = getIntent().getExtras().get("request_amount").toString();
        TransactionCode = getIntent().getExtras().get("TransactionCode").toString();
        txtMainAmmount = findViewById(R.id.txtMainAmmount);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);
        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);
        btGoMyAccount = findViewById(R.id.btGoMyAccount);

        txtMainAmmount.setText(request_amount+" "+main_currency);
        txtTransactionCode.setText("Código de transacción: "+TransactionCode);

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditCardCashRequestSuccessfullActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditCardCashRequestSuccessfullActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btGoMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditCardCashRequestSuccessfullActivity.this, MyAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
