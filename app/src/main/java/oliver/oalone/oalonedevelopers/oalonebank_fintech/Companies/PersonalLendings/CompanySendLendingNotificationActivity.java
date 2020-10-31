package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.PersonalLendings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.PersonalLendingSumaryActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.SendLendingNotificationActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompanySendLendingNotificationActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView txtUsername,txtCurrencyRate,txtUserVerification;
    private DatabaseReference userRef,ratesRef,companyRef;
    private FirebaseAuth mAuth;
    private String senderUserID, receiverUserID,saveCurrentDate,saveCurrentTime,basic_account_pen,basic_account_usd, my_currency, user_currency,main_currency;
    RadioButton rdPenAccount,rdUsdAccount,rdUserPenAccount,rdUserUsdAccount;
    EditText edtTransferAmmount,edtInterestRate,edtDeilyDefaulterRate;
    Button btnNext,btnMonths,btnPaymentFrencuency,btnGracePeriod,btnCalculateAmortization;
    RelativeLayout rootLayout;
    ImageView imageUserVerification;
    String my_company_key;

    ArrayList<String> months =new ArrayList<>();
    ArrayList<String> months2 =new ArrayList<>();
    ArrayList<String> months3 =new ArrayList<>();
    SpinnerDialog spinnerMonths;
    SpinnerDialog spinnerMonths2;
    SpinnerDialog spinnerMonths3;

    ArrayList<String> frecuency =new ArrayList<>();
    SpinnerDialog spinnerFrecuency;

    ArrayList<String> graceMonths =new ArrayList<>();
    SpinnerDialog spinnerGracePeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_send_lending_notification);

        mAuth = FirebaseAuth.getInstance();

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        senderUserID = my_company_key;
        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        companyRef = FirebaseDatabase.getInstance().getReference().child("My Companies");


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
        edtInterestRate = findViewById(R.id.edtInterestRate);
        btnMonths = findViewById(R.id.btnMonths);
        btnPaymentFrencuency = findViewById(R.id.btnPaymentFrencuency);
        btnGracePeriod = findViewById(R.id.btnGracePeriod);
        btnCalculateAmortization = findViewById(R.id.btnCalculateAmortization);
        edtDeilyDefaulterRate= findViewById(R.id.edtDeilyDefaulterRate);
        txtUserVerification= findViewById(R.id.txtUserVerification);
        imageUserVerification = findViewById(R.id.imageUserVerification);

        userRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();
                    String user_verification = dataSnapshot.child("user_verification").getValue().toString();

                    Picasso.with(CompanySendLendingNotificationActivity.this).load(profileimage).fit().centerCrop().into(profileImage);
                    txtUsername.setText(username);

                    if (user_verification.equals("true")) {
                        txtUserVerification.setText("Verificado Correctamente");
                        imageUserVerification.setImageResource(R.drawable.transaction_completed);
                    }
                    if (user_verification.equals("false")) {
                        txtUserVerification.setText("Denegada");
                        imageUserVerification.setImageResource(R.drawable.error_icon);
                    }
                    if (user_verification.equals("progress")) {
                        txtUserVerification.setText("En proceso");
                        imageUserVerification.setImageResource(R.drawable.transaction_in_progress);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        companyRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
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

        rdUserPenAccount.setEnabled(false);
        rdUserUsdAccount.setEnabled(false);

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_currency = "PEN";
                user_currency = "PEN";
                rdUserPenAccount.setChecked(true);
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_currency = "USD";
                user_currency = "USD";
                rdUserUsdAccount.setChecked(true);
            }
        });

        months.add("1");months.add("2");months.add("3"); months.add("4");months.add("5");months.add("6"); months.add("7");months.add("8");months.add("9");months.add("10");
        months.add("11");months.add("12");months.add("13"); months.add("14");months.add("15");months.add("16"); months.add("17");months.add("18");months.add("19");months.add("20");
        months.add("21");months.add("22");months.add("23"); months.add("24");months.add("25");months.add("26"); months.add("27");months.add("28");months.add("29");months.add("30");
        months.add("31");months.add("32");months.add("33"); months.add("34");months.add("35");months.add("36"); months.add("37");months.add("38");months.add("39");months.add("40");
        months.add("41");months.add("42");months.add("43"); months.add("44");months.add("45");months.add("46"); months.add("47");months.add("48");

        months2.add("2"); months2.add("4");months2.add("6");months2.add("8");months2.add("10");
        months2.add("12");months2.add("14");months2.add("16");months2.add("18");months2.add("20");
        months2.add("22");months2.add("24");months2.add("26");months2.add("28");months2.add("30");
        months2.add("32");months2.add("34");months2.add("36");months2.add("38");months2.add("40");
        months2.add("42");months2.add("44");months2.add("46");months2.add("48");

        months3.add("3");months3.add("6");months3.add("9");months3.add("12");months3.add("15");months3.add("18");
        months3.add("21"); months3.add("24"); months3.add("27");months3.add("30");
        months3.add("33");months3.add("36");months3.add("39");months3.add("42");months3.add("45");months3.add("48");

        btnMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Primero debes seleccionar la frecuencia de pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (btnPaymentFrencuency.getText().equals("Mensual")) {
                    spinnerMonths.showSpinerDialog();
                }
                if (btnPaymentFrencuency.getText().equals("Cada 2 meses")) {
                    spinnerMonths2.showSpinerDialog();
                }
                if (btnPaymentFrencuency.getText().equals("Cada 3 meses")) {
                    spinnerMonths3.showSpinerDialog();
                }
            }
        });

        spinnerMonths = new SpinnerDialog(CompanySendLendingNotificationActivity.this,months,"Selecciona el número de meses para el préstamo");
        spinnerMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnMonths.setText(item);
                Toast.makeText(CompanySendLendingNotificationActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerMonths2 = new SpinnerDialog(CompanySendLendingNotificationActivity.this,months2,"Selecciona el número de meses para el préstamo");
        spinnerMonths2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnMonths.setText(item);
                Toast.makeText(CompanySendLendingNotificationActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerMonths3 = new SpinnerDialog(CompanySendLendingNotificationActivity.this,months3,"Selecciona el número de meses para el préstamo");
        spinnerMonths3.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnMonths.setText(item);
                Toast.makeText(CompanySendLendingNotificationActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        graceMonths.add("0");graceMonths.add("1");graceMonths.add("2");graceMonths.add("3");graceMonths.add("4");graceMonths.add("5");
        graceMonths.add("6");graceMonths.add("7");graceMonths.add("8");graceMonths.add("9");graceMonths.add("10");graceMonths.add("11");graceMonths.add("12");

        btnGracePeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerGracePeriod.showSpinerDialog();
            }
        });
        spinnerGracePeriod = new SpinnerDialog(CompanySendLendingNotificationActivity.this,graceMonths,"Selecciona el número de meses para el período de gracia");
        spinnerGracePeriod.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnGracePeriod.setText(item);
                Toast.makeText(CompanySendLendingNotificationActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        frecuency.add("Mensual");frecuency.add("Cada 2 meses");frecuency.add("Cada 3 meses");
        btnPaymentFrencuency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerFrecuency.showSpinerDialog();
            }
        });

        spinnerFrecuency = new SpinnerDialog(CompanySendLendingNotificationActivity.this,frecuency,"Selecciona la frecuencia de pago");
        spinnerFrecuency.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnPaymentFrencuency.setText(item);
                Toast.makeText(CompanySendLendingNotificationActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnCalculateAmortization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdUsdAccount.isChecked() && !rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una moneda de tu cuenta para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdUserUsdAccount.isChecked() && !rdUserPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una moneda de la cuenta del beneficiario para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtTransferAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a prestar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar la tasa de interés anual para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnMonths.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar los meses que durará el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que se realizarán los pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtDeilyDefaulterRate.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la tasa moratoria diaria", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtDeilyDefaulterRate.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la tasa moratoria diaria", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else {
                    showMyGainDialog();
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double amount_to_lend = Double.parseDouble(edtTransferAmmount.getText().toString());

                if (rdPenAccount.isChecked()) {
                    double pen_account = Double.parseDouble(basic_account_pen);
                    if (pen_account < amount_to_lend) {
                        Snackbar.make(rootLayout, "No cuentas con dinero suficiente para realizar esta operación", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (rdUsdAccount.isChecked()) {
                    double usd_account = Double.parseDouble(basic_account_usd);
                    if (usd_account < amount_to_lend) {
                        Snackbar.make(rootLayout, "No cuentas con dinero suficiente para realizar esta operación", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (!rdUsdAccount.isChecked() && !rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una moneda de tu cuenta para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdUserUsdAccount.isChecked() && !rdUserPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una moneda de la cuenta del beneficiario para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtTransferAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a prestar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar la tasa de interés anual para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnMonths.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar los meses que durará el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que se realizarán los pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtDeilyDefaulterRate.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la tasa moratoria diaria", Snackbar.LENGTH_LONG).show();
                    return;
                }else {
                    goToSumaryActivity();
                }

            }
        });
    }

    private void goToSumaryActivity() {
        double ammount = Double.parseDouble(edtTransferAmmount.getText().toString());
        double rate = Double.parseDouble(edtInterestRate.getText().toString());
        double months = Double.parseDouble(btnMonths.getText().toString());
        String frecuency = btnPaymentFrencuency.getText().toString();

        //Tasa efectiva mensual:
        double rate_perone = rate/100;
        double monthly_rate_base = 1+rate_perone;
        double monthly_rate_exp = 0.08333333;
        double monthly_rate = Math.pow(monthly_rate_base,monthly_rate_exp)-1;
        double monthly_rate_express = monthly_rate*100;

        double total_debt_base = (1+monthly_rate);
        double total_debt_exp = months;
        double total_debt = Math.pow(total_debt_base,total_debt_exp)*ammount;
        double monthly_quote = total_debt/months;
        double cost_of_debt = total_debt-ammount;

        double quote = 1.00;
        if (frecuency.equals("Mensual"))
        {
            quote = monthly_quote*1;
        }
        if (frecuency.equals("Cada 2 meses"))
        {
            quote = monthly_quote*2;
        }
        if (frecuency.equals("Cada 3 meses"))
        {
            quote = monthly_quote*3;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String monthly_rate_df = decimalFormat.format(monthly_rate_express);
        String quote_df = decimalFormat.format(quote);
        String total_debt_df = decimalFormat.format(total_debt);
        String cost_of_debt_df = decimalFormat.format(cost_of_debt);

        if (rdPenAccount.isChecked())
        {
            main_currency = "PEN";
        }
        if (rdUsdAccount.isChecked())
        {
            main_currency = "USD";
        }
        Intent intent = new Intent(CompanySendLendingNotificationActivity.this, CompanyPersonalLendingSumaryActivity.class);
        intent.putExtra("main_currency",main_currency);
        intent.putExtra("ammount",edtTransferAmmount.getText().toString());
        intent.putExtra("interest_rate",edtInterestRate.getText().toString());
        intent.putExtra("financing_months",btnMonths.getText().toString());
        intent.putExtra("financing_frecuency",btnPaymentFrencuency.getText().toString());
        intent.putExtra("grace_period",btnGracePeriod.getText().toString());
        intent.putExtra("monthly_interest_rate",monthly_rate_df);
        intent.putExtra("quote_ammount",quote_df);
        intent.putExtra("total_debt",total_debt_df);
        intent.putExtra("cost_of_debt",cost_of_debt_df);
        intent.putExtra("visit_user_id",receiverUserID);
        intent.putExtra("my_company_key",my_company_key);
        intent.putExtra("main_currency",main_currency);
        intent.putExtra("daily_defaulter_rate",edtDeilyDefaulterRate.getText().toString());
        startActivity(intent);
        finish();
    }

    private void showMyGainDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View amortization = inflater.inflate(R.layout.my_lending_gain_dialog,null);

        TextView txtQuoteAmmount,txtPaymentFrecuency,txtTotalDebt,txtCostOfDebt,txtQuotesNumber;
        txtQuoteAmmount = amortization.findViewById(R.id.txtQuoteAmmount);
        txtPaymentFrecuency = amortization.findViewById(R.id.txtPaymentFrecuency);
        txtTotalDebt = amortization.findViewById(R.id.txtTotalDebt);
        txtCostOfDebt = amortization.findViewById(R.id.txtCostOfDebt);
        txtQuotesNumber= amortization.findViewById(R.id.txtQuotesNumber);

        if (rdPenAccount.isChecked())
        {
            main_currency = "PEN";
        }
        if (rdUsdAccount.isChecked())
        {
            main_currency = "USD";
        }
        double ammount = Double.parseDouble(edtTransferAmmount.getText().toString());
        double rate = Double.parseDouble(edtInterestRate.getText().toString());
        double months = Double.parseDouble(btnMonths.getText().toString());
        String frecuency = btnPaymentFrencuency.getText().toString();
        double pay_frecuency = Double.parseDouble(frecuency);

        //Tasa efectiva mensual:
        double rate_perone = rate/100;
        double monthly_rate_base = 1+rate_perone;
        double monthly_rate_exp = 0.08333333;
        double monthly_rate = Math.pow(monthly_rate_base,monthly_rate_exp)-1;
        double monthly_rate_express = monthly_rate*100;

        double total_debt_base = (1+monthly_rate);
        double total_debt_exp = months;
        double total_debt = Math.pow(total_debt_base,total_debt_exp)*ammount;
        double monthly_quote = total_debt/months;
        double cost_of_debt = total_debt-ammount;

        double quote = 1.00;
        if (frecuency.equals("Mensual"))
        {
            quote = monthly_quote*1;
        }
        if (frecuency.equals("Cada 2 meses"))
        {
            quote = monthly_quote*2;
        }
        if (frecuency.equals("Cada 3 meses"))
        {
            quote = monthly_quote*3;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String monthly_rate_df = decimalFormat.format(monthly_rate_express);
        String quote_df = decimalFormat.format(quote);
        String total_debt_df = decimalFormat.format(total_debt);
        String cost_of_debt_df = decimalFormat.format(cost_of_debt);

        double quote_number = months/pay_frecuency;

        txtQuoteAmmount.setText("Monto de la cuota a cobrar: "+quote_df+" "+main_currency);
        txtPaymentFrecuency.setText("Frecuencia de pago: "+btnPaymentFrencuency.getText().toString());
        txtTotalDebt.setText("Retorno total: "+total_debt_df+" "+main_currency);
        txtCostOfDebt.setText("Interés ganado: "+cost_of_debt_df+" "+main_currency);
        txtQuotesNumber.setText("Número de cuotas: "+quote_number);

        dialog.setView(amortization);
        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
