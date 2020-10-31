package oliver.oalone.oalonedevelopers.oalonebank_fintech.Deposits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CurrentDepositActivity extends AppCompatActivity {

    TextView txtDepositInformation,txtDate,txtTime,txtOperationCode;
    String deposit_amount,deposit_currency,operation_code,deposit_date,deposit_time;
    Button btnMyOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_deposit);

        txtDepositInformation = findViewById(R.id.txtDepositInformation);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtOperationCode = findViewById(R.id.txtOperationCode);
        btnMyOperations = findViewById(R.id.btnMyOperations);

        deposit_amount = getIntent().getExtras().getString("deposit_amount");
        deposit_currency = getIntent().getExtras().getString("deposit_currency");
        operation_code = getIntent().getExtras().getString("operation_code");
        deposit_date = getIntent().getExtras().getString("deposit_date");
        deposit_time = getIntent().getExtras().getString("deposit_time");

        if (deposit_currency.equals("PEN")) {
            txtDepositInformation.setText("Monto y Moneda: S/ "+deposit_amount);
        }
        if (deposit_currency.equals("USD")) {
            txtDepositInformation.setText("Monto y Moneda: $ "+deposit_amount);
        }

        txtDate.setText("Fecha: "+deposit_date);
        txtTime.setText("Hora: "+deposit_time);
        txtOperationCode.setText("Código de Operación: "+operation_code);

        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentDepositActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}