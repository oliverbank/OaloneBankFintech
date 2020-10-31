package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class SuccessfulSendingPersonalLendingActivity extends AppCompatActivity {

    String ammount,main_currency,receiver_username;
    TextView txtAmmount,txtMessage;
    Button btGoToHome,btnMyLendings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_sending_personal_lending);

        txtAmmount= findViewById(R.id.txtAmmount);
        txtMessage = findViewById(R.id.txtMessage);
        btGoToHome= findViewById(R.id.btGoToHome);
        btnMyLendings = findViewById(R.id.btnMyLendings);

        ammount = getIntent().getExtras().getString("ammount").toString();
        main_currency = getIntent().getExtras().getString("main_currency").toString();
        receiver_username = getIntent().getExtras().getString("receiver_username").toString();

        txtAmmount.setText(ammount+" "+main_currency);
        txtMessage.setText("El ususario +"+receiver_username+" debe confirmar esta operación para generar las boletas de pago, en caso esta oferta sea rechazada el dinero será retornado a tu cuenta automáticamente");

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessfulSendingPersonalLendingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnMyLendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessfulSendingPersonalLendingActivity.this, MySendedPersonalLendingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
