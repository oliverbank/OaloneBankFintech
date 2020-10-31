package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.QrCodeDoesNotExistActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CreditCardCashRequestErrorActivity extends AppCompatActivity {

    Button btGoToHome,btnMyOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_cash_request_error);

        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditCardCashRequestErrorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditCardCashRequestErrorActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
