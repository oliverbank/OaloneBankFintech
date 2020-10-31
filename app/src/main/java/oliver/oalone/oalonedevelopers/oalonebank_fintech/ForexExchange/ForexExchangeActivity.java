package oliver.oalone.oalonedevelopers.oalonebank_fintech.ForexExchange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class ForexExchangeActivity extends AppCompatActivity {

    TextView txtCurrencySell,txtCurrencyBuy;
    Button btnBuyDollars,btnBuySoles;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forex_exchange);

        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserID = mAuth.getCurrentUser().getUid();

        txtCurrencySell = findViewById(R.id.txtCurrencySell);
        txtCurrencyBuy = findViewById(R.id.txtCurrencyBuy);
        btnBuyDollars = findViewById(R.id.btnBuyDollars);
        btnBuySoles = findViewById(R.id.btnBuySoles);

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                    String currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    String currency_spot = dataSnapshot.child("currency_spot").getValue().toString();
                    txtCurrencySell.setText("Tipo de Cambio: S/"+currency_rate_sell);
                    txtCurrencyBuy.setText("Tipo de Cambio: S/"+currency_rate_buy);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnBuyDollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForexExchangeActivity.this, ForexExchangeAmmountActivity.class);
                intent.putExtra("operation","buy_dollars");
                intent.putExtra("origin","PEN");
                intent.putExtra("receiver","USD");
                startActivity(intent);
            }
        });
        btnBuySoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForexExchangeActivity.this, ForexExchangeAmmountActivity.class);
                intent.putExtra("operation","buy_soles");
                intent.putExtra("origin","USD");
                intent.putExtra("receiver","PEN");
                startActivity(intent);
            }
        });


    }
}
