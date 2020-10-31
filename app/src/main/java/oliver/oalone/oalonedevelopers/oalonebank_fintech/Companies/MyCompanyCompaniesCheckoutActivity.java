package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterTransferActivity;

public class MyCompanyCompaniesCheckoutActivity extends AppCompatActivity {

    String company_key,my_company_key;
    ImageView imageCompanyVerification;
    CircleImageView profileImage;
    TextView txtName,txtRuc,txtCompanyVerification,txtCurrencyRate;
    private DatabaseReference userRef,ratesRef, companiesRef;
    private FirebaseAuth mAuth;
    private String currentUserId,saveCurrentDate,saveCurrentTime,basic_account_pen,basic_account_usd, my_currency, user_currency;
    RadioButton rdPenAccount,rdUsdAccount,rdUserPenAccount,rdUserUsdAccount;
    EditText edtTransferAmmount;
    Button btnNext;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_companies_checkout);

        company_key = getIntent().getExtras().get("company_key").toString();
        my_company_key = getIntent().getExtras().get("my_company_key").toString();

        mAuth = FirebaseAuth.getInstance();

        currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        companiesRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        profileImage = findViewById(R.id.imageCompany);
        txtName = findViewById(R.id.txtName);
        txtRuc= findViewById(R.id.txtRuc);
        txtCompanyVerification= findViewById(R.id.txtCompanyVerification);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        edtTransferAmmount = findViewById(R.id.edtTransferAmmount);
        btnNext = findViewById(R.id.btnNext);
        rootLayout = findViewById(R.id.rootLayout);
        rdUserPenAccount = findViewById(R.id.rdUserPenAccount);
        rdUserUsdAccount = findViewById(R.id.rdUserUsdAccount);
        imageCompanyVerification= findViewById(R.id.imageCompanyVerification);

        companiesRef.child(company_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String company_image = dataSnapshot.child("company_image").getValue().toString();
                    String company_name = dataSnapshot.child("company_name").getValue().toString();
                    String company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                    String company_verification = dataSnapshot.child("company_verification").getValue().toString();

                    Picasso.with(MyCompanyCompaniesCheckoutActivity.this).load(company_image).fit().centerCrop().into(profileImage);
                    txtName.setText(company_name);
                    txtRuc.setText(company_ruc);

                    if (company_verification.equals("true")) {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                        txtCompanyVerification.setText("Verificado");
                    }
                    if (company_verification.equals("false")) {
                        imageCompanyVerification.setImageResource(R.drawable.error_icon);
                        txtCompanyVerification.setText("Denegado");
                    } else {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                        txtCompanyVerification.setText("En Proceso");
                    }

                } else {
                    Intent intent = new Intent(MyCompanyCompaniesCheckoutActivity.this, RegisterTransferActivity.class);
                    intent.putExtra("visit_user_id",company_key);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(my_company_key).addValueEventListener(new ValueEventListener() {
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
                    Snackbar.make(rootLayout, "Debes ingresar el monto a pagar...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdUserUsdAccount.isChecked()&&!rdUserPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una de las cuentas del beneficiario para el pago...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdUsdAccount.isChecked()&&!rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una de tus cuentas para realizar el pago...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtTransferAmmount.getText().toString().equals("0") || edtTransferAmmount.getText().toString().equals("0.00"))
                {
                    Snackbar.make(rootLayout, "No puedes pagar cero "+my_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (rdPenAccount.isChecked())
                {
                    double my_money = Double.parseDouble(basic_account_pen);
                    double my_transfer = Double.parseDouble(edtTransferAmmount.getText().toString());
                    if (my_money < my_transfer)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficiente dinero para hacer este pago ", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                }
                if (rdUsdAccount.isChecked())
                {
                    double my_money = Double.parseDouble(basic_account_usd);
                    double my_transfer = Double.parseDouble(edtTransferAmmount.getText().toString());
                    if (my_money < my_transfer)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficiente dinero para hacer este pago ", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                }
                if (edtTransferAmmount.getText().toString().equals("0.000"))
                {
                    Snackbar.make(rootLayout, "No puedes pagar cero "+my_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Intent intent = new Intent(MyCompanyCompaniesCheckoutActivity.this, MyCompanyFinishCheckoutActivity.class);
                    intent.putExtra("company_key", company_key);
                    intent.putExtra("my_company_key", my_company_key);
                    intent.putExtra("my_currency", my_currency);
                    intent.putExtra("user_currency", user_currency);
                    intent.putExtra("ammount", edtTransferAmmount.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
