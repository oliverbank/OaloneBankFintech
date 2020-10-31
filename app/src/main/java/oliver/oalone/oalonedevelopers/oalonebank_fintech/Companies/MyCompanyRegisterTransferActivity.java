package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.FinishTransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.QrCodeDoesNotExistActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterTransferActivity;

public class MyCompanyRegisterTransferActivity extends AppCompatActivity {

    String my_company_key;
    CircleImageView profileImage;
    TextView txtUsername,txtCurrencyRate;
    private DatabaseReference userRef,ratesRef,myCompanyRef;
    private FirebaseAuth mAuth;
    private String  receiverUserID,saveCurrentDate,saveCurrentTime,basic_account_pen,basic_account_usd, my_currency, user_currency;
    RadioButton rdPenAccount,rdUsdAccount,rdUserPenAccount,rdUserUsdAccount;
    EditText edtTransferAmmount;
    Button btnNext;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_register_transfer);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();

        //senderUserID = mAuth.getCurrentUser().getUid();
        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myCompanyRef = FirebaseDatabase.getInstance().getReference().child("My Companies");

        profileImage = findViewById(R.id.profileImage);
        txtUsername = findViewById(R.id.txtUsername);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        edtTransferAmmount = findViewById(R.id.edtTransferAmmount);
        btnNext = findViewById(R.id.btnNext);
        rootLayout = findViewById(R.id.rootLayout);
        rdUserPenAccount = findViewById(R.id.rdUserPenAccount);
        rdUserUsdAccount = findViewById(R.id.rdUserUsdAccount);

        userRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();

                    Picasso.with(MyCompanyRegisterTransferActivity.this).load(profileimage).fit().into(profileImage);
                    txtUsername.setText(username);
                } else
                {
                    Intent intent = new Intent(MyCompanyRegisterTransferActivity.this, QrCodeDoesNotExistActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myCompanyRef.child(my_company_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                    rdPenAccount.setText("Cuenta corriente (Soles - PEN): S/ "+basic_account_pen);
                    rdUsdAccount.setText("Cuenta corriente (Dólares - USD): $ "+basic_account_usd);
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
                    String currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    String currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();

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
                my_currency = "PEN";
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_currency = "USD";
            }
        });
        rdUserPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_currency = "PEN";
            }
        });
        rdUserUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_currency = "USD";
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtTransferAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a transferir...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdUserUsdAccount.isChecked()&&!rdUserPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una de las cuentas del destinatario para la transferencia...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdUsdAccount.isChecked()&&!rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una de tus cuentas para realizar la transferencia...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtTransferAmmount.getText().toString().equals("0") || edtTransferAmmount.getText().toString().equals("0.00"))
                {
                    Snackbar.make(rootLayout, "No puedes transferir cero "+my_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (rdPenAccount.isChecked())
                {
                    double my_money = Double.parseDouble(basic_account_pen);
                    double my_transfer = Double.parseDouble(edtTransferAmmount.getText().toString());
                    if (my_money < my_transfer)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficiente dinero para hacer esta transferencia ", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                }
                if (rdUsdAccount.isChecked())
                {
                    double my_money = Double.parseDouble(basic_account_usd);
                    double my_transfer = Double.parseDouble(edtTransferAmmount.getText().toString());
                    if (my_money < my_transfer)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficiente dinero para hacer esta transferencia ", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                }
                if (edtTransferAmmount.getText().toString().equals("0"))
                {
                    Snackbar.make(rootLayout, "No puedes transferir cero "+my_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtTransferAmmount.getText().toString().equals("0"))
                {
                    Snackbar.make(rootLayout, "No puedes transferir cero "+my_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Intent intent = new Intent(MyCompanyRegisterTransferActivity.this, MyCompanyFinishTransferActivity.class);
                    intent.putExtra("visit_user_id", receiverUserID);
                    intent.putExtra("my_currency", my_currency);
                    intent.putExtra("user_currency", user_currency);
                    intent.putExtra("my_company_key", my_company_key);
                    intent.putExtra("ammount", edtTransferAmmount.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
