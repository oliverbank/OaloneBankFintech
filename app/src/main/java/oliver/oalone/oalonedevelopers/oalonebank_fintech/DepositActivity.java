package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DepositActivity extends AppCompatActivity {

    RadioButton rdPenAccount,rdUsdAccount,rdDepositPen,rdDepositUsd;
    TextView txtCurrencyRate;
    EditText edtDepositAmmount;
    Button btnNext;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef;
    private ProgressDialog loadingBar;
    RelativeLayout rootLayout;
    String currentUserID,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy, account_currency, deposit_ammount_currency, min_deposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        rdPenAccount= findViewById(R.id.rdPenAccount);
        rdUsdAccount= findViewById(R.id.rdUsdAccount);
        rdDepositPen= findViewById(R.id.rdDepositPen);
        rdDepositUsd= findViewById(R.id.rdDepositUsd);
        txtCurrencyRate= findViewById(R.id.txtCurrencyRate);
        edtDepositAmmount= findViewById(R.id.edtDepositAmmount);
        btnNext= findViewById(R.id.btnNext);
        rootLayout = findViewById(R.id.rootLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                    rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+basic_account_pen);
                    rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+basic_account_usd);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                    min_deposit= dataSnapshot.child("min_deposit").getValue().toString();

                    txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_currency = "PEN";
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_currency = "USD";
            }
        });

        rdDepositPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deposit_ammount_currency = "PEN";
            }
        });
        rdDepositUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deposit_ammount_currency = "USD";
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la cuenta de destino...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnNext.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a depositar...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdDepositPen.isChecked() && !rdDepositUsd.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la moneda en la que realizarás el depósito...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                double min_ammount_deposit = Double.parseDouble(min_deposit);
                double ammount_to_deposit = Double.parseDouble(edtDepositAmmount.getText().toString());
                if (ammount_to_deposit < min_ammount_deposit)
                {
                    Snackbar.make(rootLayout, "Debes realizar un depósito no menor a "+min_deposit+" "+deposit_ammount_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (ammount_to_deposit < min_ammount_deposit)
                {
                    Snackbar.make(rootLayout, "Debes realizar un depósito no menor a "+min_deposit+" "+deposit_ammount_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    //Pasar extras e ir a nueva actividad
                    Intent intent = new Intent(DepositActivity.this, RegisterDepositActivity.class);
                    intent.putExtra("my_account_currency",account_currency);
                    intent.putExtra("ammount_for_deposit",edtDepositAmmount.getText().toString());
                    intent.putExtra("deposit_currency",deposit_ammount_currency);
                    startActivity(intent);
                }
            }
        });
    }
}
