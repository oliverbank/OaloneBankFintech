package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

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

public class SucessfullPaymentActivity extends AppCompatActivity {

    String transactionCode,company_name,username,ammount_sended,currency_sended,ammount_received,currency_received,date, time;
    Button btGoToHome,btnMyOperations;
    TextView txtTransactionCode,txtCompanyName,txtAmmountSended,txtAmmountReceived,txtUserName,txtDate,txtMainAmmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucessfull_payment);

        transactionCode = getIntent().getExtras().get("TransactionCode").toString();
        company_name = getIntent().getExtras().get("company_name").toString();
        username = getIntent().getExtras().get("username").toString();
        ammount_sended = getIntent().getExtras().get("ammount_sended").toString();
        currency_sended = getIntent().getExtras().get("currency_sended").toString();
        ammount_received = getIntent().getExtras().get("ammount_received").toString();
        currency_received = getIntent().getExtras().get("currency_received").toString();
        date = getIntent().getExtras().get("date").toString();
        time = getIntent().getExtras().get("time").toString();

        btGoToHome = findViewById(R.id.btGoToHome);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtAmmountSended = findViewById(R.id.txtAmmountSended);
        txtAmmountReceived = findViewById(R.id.txtAmmountReceived);
        txtUserName = findViewById(R.id.txtUserName);
        txtDate = findViewById(R.id.txtDate);
        txtMainAmmount = findViewById(R.id.txtMainAmmount);
        btnMyOperations = findViewById(R.id.btnMyOperations);

        txtTransactionCode.setText("Código de transacción: "+transactionCode);
        txtCompanyName.setText("Empresa o Comercio: "+company_name);
        txtAmmountSended.setText("Monto Pagado: "+ammount_sended+" "+currency_sended);
        txtAmmountReceived.setText("Monto Cobrado: "+ammount_received+" "+currency_received);
        txtUserName.setText("Pado por: "+username);
        txtDate.setText("Fecha y hora: "+date+" a las "+time);
        txtMainAmmount.setText(ammount_received+" "+currency_received);
        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SucessfullPaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SucessfullPaymentActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
