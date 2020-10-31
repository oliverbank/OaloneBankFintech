package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class RegisterInvestmentOnFinanceRequestActivity extends AppCompatActivity {

    TextView txtTittle,txtCompanyName,txtFinanceDestination,txtCompanyVerification,txtInvested,txtInvestedCurrency1,txtNoInvested,txtInvestedCurrency2,txtCurrencyRate,
            txtInvestmentAmmount,txtTrea,txtPaymentFrecuency,txtInterestAmmount,txtGracePeriod,txtQuotesNumber,txtQuoteAmmount,txtTransactionCost,txtTotalInvestment,
            txtGracePeriodStartDate,txtGracePeriodEndDate,txtPaymentStartDay,txtEndStartDay,txtStartPayment;
    ImageView imageCompany,imageCompanyVerification;
    BubbleSeekBar seekBarPen;
    EditText edtInvestmentAmmount;
    RadioButton rdPenAccount,rdUsdAccount;
    CheckBox cbAgreement;
    Button btnInvest;
    String postKey,currentUserID,invested,no_invested,ammount,main_currency,company_name,company_image,finance_destiny,company_verification,basic_account_pen,basic_account_usd,
            currency_rate_buy,currency_rate_sell,quote_ammount,company_id,company_ruc,fee_ammount1,fee_ammount2,fee_ammount4;
    private FirebaseAuth mAuth;
    private DatabaseReference financeRequestRef,userRef,ratesRef,imageResourses,myOperationsRef, myCompaniesRef,oliverBankRef;
    RelativeLayout rootLayout;
    LinearLayout transactionDetailsLayout,importantDatesLayout,mainLayout;
    String ammount_to_invest_st,interest_rate,financing_frecuency,cost_of_debt,grace_period,financing_months,invest_rate,total_ammount_to_invest_st,payment_space_days,end_day,
            end_month,end_year,my_pin,username,profileimage, participation_st,direct_investment,saveCurrentDate,saveCurrentTime, operationRandomName,finance_request_investment_pen,
            finance_request_investment_usd;
    double ammount_invested, ammount_no_invested,total_ammount,my_quotes,total_ammount_to_invest,my_pen_aacount,my_usd_aacount,ammount_to_invest,participation,cost_rate,fr_invest_pen,
            fr_invest_usd;
    private ProgressDialog loadingBar;
    DecimalFormat decimalFormat;
    int finance_frec,i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_investment_on_finance_request);

        postKey = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        financeRequestRef = FirebaseDatabase.getInstance().getReference().child("Finance Requests").child(postKey);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        myCompaniesRef =FirebaseDatabase.getInstance().getReference().child("My Companies");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

        //loadingBar = new ProgressDialog(this);

        txtTittle = findViewById(R.id.txtTittle);
        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtFinanceDestination = findViewById(R.id.txtFinanceDestination);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);
        txtInvested = findViewById(R.id.txtInvested);
        txtInvestedCurrency1 = findViewById(R.id.txtInvestedCurrency1);
        seekBarPen = findViewById(R.id.seekBarPen);
        txtNoInvested = findViewById(R.id.txtNoInvested);
        txtInvestedCurrency2 = findViewById(R.id.txtInvestedCurrency2);
        edtInvestmentAmmount = findViewById(R.id.edtInvestmentAmmount);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        txtInvestmentAmmount = findViewById(R.id.txtInvestmentAmmount);
        txtTrea = findViewById(R.id.txtTrea);
        txtPaymentFrecuency = findViewById(R.id.txtPaymentFrecuency);
        txtInterestAmmount = findViewById(R.id.txtInterestAmmount);
        txtGracePeriod = findViewById(R.id.txtGracePeriod);
        txtQuotesNumber= findViewById(R.id.txtQuotesNumber);
        txtQuoteAmmount = findViewById(R.id.txtQuoteAmmount);
        txtTransactionCost= findViewById(R.id.txtTransactionCost);
        txtTotalInvestment= findViewById(R.id.txtTotalInvestment);
        txtGracePeriodStartDate = findViewById(R.id.txtGracePeriodStartDate);
        txtGracePeriodEndDate = findViewById(R.id.txtGracePeriodEndDate);
        txtPaymentStartDay = findViewById(R.id.txtPaymentStartDay);
        txtEndStartDay = findViewById(R.id.txtEndStartDay);
        txtStartPayment = findViewById(R.id.txtStartPayment);
        cbAgreement= findViewById(R.id.cbAgreement);
        btnInvest= findViewById(R.id.btnInvest);
        rootLayout = findViewById(R.id.rootLayout);
        transactionDetailsLayout = findViewById(R.id.transactionDetailsLayout);
        importantDatesLayout = findViewById(R.id.importantDatesLayout);
        mainLayout = findViewById(R.id.mainLayout);

        loadingBar = new ProgressDialog(this);

        transactionDetailsLayout.getLayoutParams().height = 0;
        importantDatesLayout.getLayoutParams().height = 0;
        mainLayout.getLayoutParams().height = 0;

        decimalFormat = new DecimalFormat("0.00");

        getDataDate();

        financeRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    invested = dataSnapshot.child("invested").getValue().toString();
                    no_invested = dataSnapshot.child("no_invested").getValue().toString();
                    ammount = dataSnapshot.child("ammount").getValue().toString();
                    main_currency = dataSnapshot.child("main_currency").getValue().toString();
                    company_name = dataSnapshot.child("company_name").getValue().toString();
                    company_image = dataSnapshot.child("company_image").getValue().toString();
                    finance_destiny = dataSnapshot.child("finance_destiny").getValue().toString();
                    company_verification = dataSnapshot.child("company_verification").getValue().toString();
                    interest_rate = dataSnapshot.child("interest_rate").getValue().toString();
                    financing_frecuency = dataSnapshot.child("financing_frecuency").getValue().toString();
                    cost_of_debt = dataSnapshot.child("cost_of_debt").getValue().toString();
                    grace_period = dataSnapshot.child("grace_period").getValue().toString();
                    financing_months = dataSnapshot.child("financing_months").getValue().toString();
                    end_day = dataSnapshot.child("end_day").getValue().toString();
                    end_month = dataSnapshot.child("end_month").getValue().toString();
                    end_year = dataSnapshot.child("end_year").getValue().toString();
                    quote_ammount = dataSnapshot.child("quote_ammount").getValue().toString();
                    company_id = dataSnapshot.child("company_id").getValue().toString();
                    company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                    fee_ammount1 = dataSnapshot.child("fee_ammount1").getValue().toString();
                    fee_ammount2 = dataSnapshot.child("fee_ammount2").getValue().toString();
                    fee_ammount4 = dataSnapshot.child("fee_ammount4").getValue().toString();

                    if (financing_frecuency.equals("Mensual")) {
                        finance_frec = 1;
                    }
                    if (financing_frecuency.equals("Cada 2 meses")) {
                        finance_frec = 2;
                    }
                    if (financing_frecuency.equals("Cada 3 meses")) {
                        finance_frec = 3;
                    }

                    txtInvested.setText(invested);
                    txtInvestedCurrency1.setText(main_currency);
                    txtNoInvested.setText(no_invested);
                    txtInvestedCurrency2.setText(main_currency);


                    total_ammount = Double.parseDouble(ammount);
                    ammount_invested = Double.parseDouble(invested);
                    ammount_no_invested = Double.parseDouble(no_invested);

                    seekBarPen.setEnabled(false);

                    seekBarPen.getConfigBuilder()
                            .min(0)
                            .max((float) total_ammount)
                            .progress((float) ammount_invested)
                            .build();

                    Picasso.with(RegisterInvestmentOnFinanceRequestActivity.this).load(company_image).fit().into(imageCompany);
                    txtTittle.setText(company_name);
                    txtCompanyName.setText("Nombre comercial: "+company_name);
                    txtFinanceDestination.setText("Destino del Financiamiento: "+finance_destiny);
                    if (company_verification.equals("progress"))
                    {
                        txtCompanyVerification.setText("En proceso");
                        imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                    }
                    if (company_verification.equals("true"))
                    {
                        txtCompanyVerification.setText("Verificado");
                        imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                    }
                    if (company_verification.equals("false"))
                    {
                        txtCompanyVerification.setText("Denegado");
                        imageCompanyVerification.setImageResource(R.drawable.error_icon);
                    }


                    userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                                basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
                                username = dataSnapshot.child("username").getValue().toString();
                                profileimage = dataSnapshot.child("profileimage").getValue().toString();

                                rdPenAccount.setText("Cuenta básica (Soles - PEN): S/"+basic_account_pen);
                                rdUsdAccount.setText("Cuenta básica (Dólares - USD): $"+basic_account_usd);

                                ratesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            invest_rate = dataSnapshot.child("invest_rate").getValue().toString();
                                            payment_space_days = dataSnapshot.child("payment_space_days").getValue().toString();
                                            currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                                            currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();

                                            txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell);

                                            imageResourses.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists())
                                                    {
                                                        direct_investment = dataSnapshot.child("direct_investment").getValue().toString();

                                                        oliverBankRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    finance_request_investment_pen = dataSnapshot.child("Finance Request Investment PEN").getValue().toString();
                                                                    finance_request_investment_usd = dataSnapshot.child("Finance Request Investment USD").getValue().toString();

                                                                    fr_invest_pen = Double.parseDouble(finance_request_investment_pen);
                                                                    fr_invest_usd = Double.parseDouble(finance_request_investment_usd);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        edtInvestmentAmmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (rdPenAccount.isChecked()) {
                    showLayouts();
                    edtInvestmentAmmount.setEnabled(true);
                }
                if (rdUsdAccount.isChecked()) {
                    showLayouts();
                    edtInvestmentAmmount.setEnabled(true);
                } else {

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    //rdPenAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    //rdPenAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }else
                    {
                    //rdPenAccount.setChecked(true);
                    showLayouts();
                }
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    //rdUsdAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    //rdUsdAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }else
                {
                    //rdUsdAccount.setChecked(true);
                    showLayouts();
                }
            }
        });

        btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rdPenAccount.setBackground(getDrawable(R.drawable.button3_background));
                    }
                    rdPenAccount.setTextColor(Color.WHITE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        rdUsdAccount.setBackground(getDrawable(R.drawable.button3_background));
                    }
                    rdUsdAccount.setTextColor(Color.WHITE);
                    Snackbar.make(rootLayout, "Selecciona una cuenta primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!cbAgreement.isChecked()) {
                    Snackbar.make(rootLayout, "Debes declarar tu consentimiento para esta operación...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (rdPenAccount.isChecked()) {
                    my_pen_aacount = Double.parseDouble(basic_account_pen);
                    if (total_ammount_to_invest > my_pen_aacount)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficiente dinero para esta operación...", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (rdUsdAccount.isChecked()) {
                    my_usd_aacount = Double.parseDouble(basic_account_usd);
                    if (total_ammount_to_invest > my_usd_aacount)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficiente dinero para esta operación...", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                }
                if (ammount_to_invest > ammount_no_invested) {
                    Snackbar.make(rootLayout, "La inversión no debe superar el monto sin financiar...", Snackbar.LENGTH_LONG).show();
                    return;

                }
                if (ammount_to_invest > ammount_no_invested) {
                    Snackbar.make(rootLayout, "La inversión no debe superar el monto sin financiar...", Snackbar.LENGTH_LONG).show();
                    return;

                }else {
                    pinDialog();
                }
            }
        });

    }

    private void pinDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("PIN de Seguridad");
        dialog.setIcon(R.drawable.pin_icon);
        dialog.setMessage("Ingresa tu PIN de seguridad");

        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pin_dialog_layout,null);

        final EditText edtPin = pin_dialog.findViewById(R.id.edtPin);
        final Button btnConfirmDeposit = pin_dialog.findViewById(R.id.btnConfirmDeposit);
        final RelativeLayout newRootLayout = pin_dialog.findViewById(R.id.newRootLayout);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    my_pin = dataSnapshot.child("pin").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnConfirmDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPin.getText().toString()))
                {
                    Snackbar.make(newRootLayout, "Debes ingresar tu PIN de seguridad...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!edtPin.getText().toString().equals(my_pin))
                {
                    Snackbar.make(newRootLayout, "PIN INCORRECTO", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    //Set Methods to proceed with the operation
                    btnConfirmDeposit.setEnabled(false);
                    btnConfirmDeposit.setText("Procesando Operación...");
                    sendMoneyToCompanyFromUser();
                    dialog.dismiss();
                    finish();
                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }

    private void sendMoneyToCompanyFromUser() {
        if (rdPenAccount.isChecked()) {
            double new_value_on_pen_account = my_pen_aacount-total_ammount_to_invest;
            String pen_account_value = decimalFormat.format(new_value_on_pen_account);

            final HashMap hashMap = new HashMap();
            hashMap.put("basic_account_pen",pen_account_value);

            userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        double new_value_invested = ammount_invested+ammount_to_invest;
                        double new_value_no_invested = ammount_no_invested-ammount_to_invest;
                        //Pass to String and update children from finance request
                        String invested_value = decimalFormat.format(new_value_invested);
                        String no_invested_value = decimalFormat.format(new_value_no_invested);

                        HashMap postMap = new HashMap();
                        postMap.put("invested",invested_value);
                        postMap.put("no_invested",no_invested_value);
                        financeRequestRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    //register investor
                                    double oliver_bank_fee = (cost_rate/100)*ammount_to_invest;
                                    double fr_pen_updated = fr_invest_pen+oliver_bank_fee;
                                    String fr_fee_updated = decimalFormat.format(fr_pen_updated);
                                    oliverBankRef.child("Finance Request Investment PEN").setValue(fr_fee_updated);
                                    registerInvestor();
                                }
                            }
                        });
                    }

                }
            });
        }
        if (rdUsdAccount.isChecked()) {
            double new_value_on_usd_account = my_usd_aacount-total_ammount_to_invest;
            String usd_account_value = decimalFormat.format(new_value_on_usd_account);

            HashMap hashMap = new HashMap();
            hashMap.put("basic_account_usd",usd_account_value);

            userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        double new_value_invested = ammount_invested+ammount_to_invest;
                        double new_value_no_invested = ammount_no_invested-ammount_to_invest;
                        //Pass to String and update children from finance request
                        String invested_value = decimalFormat.format(new_value_invested);
                        String no_invested_value = decimalFormat.format(new_value_no_invested);

                        HashMap postMap = new HashMap();
                        postMap.put("invested",invested_value);
                        postMap.put("no_invested",no_invested_value);
                        financeRequestRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    double oliver_bank_fee = (cost_rate/100)*ammount_to_invest;
                                    double fr_pen_updated = fr_invest_usd+oliver_bank_fee;
                                    String fr_fee_updated = decimalFormat.format(fr_pen_updated);
                                    oliverBankRef.child("Finance Request Investment USD").setValue(fr_fee_updated);
                                    registerInvestor();
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    private void registerInvestor() {
        financeRequestRef.child("Investors").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String participation_before_st = dataSnapshot.child("participation").getValue().toString();
                    double participation_before = Double.parseDouble(participation_before_st);
                    String investment_ammount_before_st = dataSnapshot.child("investment_amount").getValue().toString();
                    double investment_ammount_before = Double.parseDouble(investment_ammount_before_st);
                    double quote_ammount_db = Double.parseDouble(quote_ammount);

                    double current_participation = (100*participation)+participation_before;
                    double current_investment_amount = ammount_to_invest+investment_ammount_before;
                    String current_participation_st = decimalFormat.format(current_participation);
                    String current_investment_amount_st = decimalFormat.format(current_investment_amount);

                    financeRequestRef.child("Investors").child(currentUserID).child("participation").setValue(current_participation_st);
                    financeRequestRef.child("Investors").child(currentUserID).child("investment_amount").setValue(current_investment_amount_st);

                    int months = Integer.parseInt(financing_months);
                    int bill_number = months/finance_frec;

                    double my_quote = (current_participation/100)*quote_ammount_db;
                    String my_quote_st = decimalFormat.format(my_quote);

                    //Calculate Start Month:
                    int grace_p = Integer.parseInt(grace_period);
                    int my_month = Integer.parseInt(end_month)+grace_p;
                    String my_month_st = String.valueOf(my_month);
                    int my_year = Integer.parseInt(end_year);
                    String my_year_st = String.valueOf(my_year);
                    //Get the last day of payment
                    int day_plus = Integer.parseInt(payment_space_days);
                    int end_dayyy = Integer.parseInt(end_day);
                    int last_day_of_payment= end_dayyy+day_plus;
                    String my_last_day = String.valueOf(last_day_of_payment);

                    //Calculate quote to with Oliver Bank Fees
                    double finacing_months = Double.parseDouble(financing_months);
                    double fee1 = Double.parseDouble(fee_ammount1)/finacing_months;
                    double fee2 = Double.parseDouble(fee_ammount2)/finacing_months;
                    double fee4 = Double.parseDouble(fee_ammount4);

                    double fee_amount_company_1 = (current_participation/100)*fee1;
                    double fee_amount_company_2 = (current_participation/100)*fee2;
                    double fee_amount_company_4 = (current_participation/100)*fee4;

                    double company_quote = my_quote+fee_amount_company_1+fee_amount_company_2+fee_amount_company_4;
                    String company_quote_st = decimalFormat.format(company_quote);

                    for (i = 1; i <= months; i += finance_frec, my_month+=finance_frec) {

                        if (my_month > 12) {
                            my_month = my_month - 12;
                            my_month_st = String.valueOf(my_month);
                            my_year=my_year+1;
                            my_year_st = String.valueOf(my_year);
                        } else {
                            my_month = my_month;
                            my_month_st = String.valueOf(my_month);
                        }

                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                        saveCurrentDate =currentDate.format(calForDate.getTime());

                        Calendar calForTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                        saveCurrentTime =currentTime.format(calForTime.getTime());

                        String postRandomName = saveCurrentTime+saveCurrentDate;

                        HashMap postMap = new HashMap();
                        postMap.put("my_company_user",currentUserID);
                        postMap.put("buyer_company_user","");
                        postMap.put("my_company_id","");
                        postMap.put("buyer_company_id",company_id);
                        postMap.put("date",saveCurrentDate);
                        postMap.put("time",saveCurrentTime);
                        postMap.put("datetime",saveCurrentDate+saveCurrentTime);
                        //My Company Information:
                        postMap.put("my_company_name","");
                        postMap.put("my_company_ruc","");
                        postMap.put("my_company_image","");
                        postMap.put("my_company_verification","");
                        //Buyer Information:
                        postMap.put("buyer_company_name",company_name);
                        postMap.put("buyer_company_ruc",company_ruc);
                        postMap.put("buyer_company_image",company_image);
                        postMap.put("buyer_company_verification",company_verification);
                        //Bill Information:
                        Random rnd = new Random();
                        int number = rnd.nextInt(999999);
                        String randomNumbwe =String.valueOf(number);
                        String bill_code = randomNumbwe+end_day+my_month_st+end_year+i;
                        postMap.put("bill_ammount",my_quote_st);
                        postMap.put("bill_company_ammount",company_quote_st);
                        postMap.put("bill_currency",main_currency);
                        postMap.put("bill_ammount_currency",my_quote_st);
                        postMap.put("bill_issue_date",end_day+"/"+my_month_st+"/"+my_year_st);
                        postMap.put("bill_end_date",my_last_day+"/"+my_month_st+"/"+my_year_st);
                        postMap.put("bill_end_day",my_last_day);
                        postMap.put("bill_end_month",my_month_st);
                        postMap.put("bill_end_year",my_year_st);
                        postMap.put("bill_state","Esperando día de cobranza");
                        postMap.put("bill_id",currentUserID+bill_code);
                        postMap.put("bill_factoring","false");
                        postMap.put("bill_code",bill_code);
                        postMap.put("payed","false");
                        postMap.put("timestamp", ServerValue.TIMESTAMP);
                        financeRequestRef.child("Investors").child(currentUserID).child("Bills").child("bill_number"+i).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterInvestmentOnFinanceRequestActivity.this, "Cargando Operación...", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                    registerOperation();


                } else if (!dataSnapshot.hasChild("username")){

                    double quote_ammount_db = Double.parseDouble(quote_ammount);
                    final double my_quote = (participation)*quote_ammount_db;
                    final String my_quote_st = decimalFormat.format(my_quote);


                    HashMap userMap = new HashMap();
                    userMap.put("username",username);
                    userMap.put("profileimage",profileimage);
                    userMap.put("participation",participation_st);
                    userMap.put("investment_amount",ammount_to_invest_st);
                    userMap.put("main_currency",main_currency);
                    userMap.put("userId",currentUserID);

                    financeRequestRef.child("Investors").child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                int months = Integer.parseInt(financing_months);
                                int bill_number = months/finance_frec;

                                //Calculate Start Month:
                                int grace_p = Integer.parseInt(grace_period);
                                int my_month = Integer.parseInt(end_month)+grace_p;
                                String my_month_st = String.valueOf(my_month);
                                int my_year = Integer.parseInt(end_year);
                                String my_year_st = String.valueOf(my_year);
                                //Get the last day of payment
                                int day_plus = Integer.parseInt(payment_space_days);
                                int end_dayyy = Integer.parseInt(end_day);
                                int last_day_of_payment= end_dayyy+day_plus;
                                String my_last_day = String.valueOf(last_day_of_payment);

                                //Calculate quote to with Oliver Bank Fees
                                double finacing_months = Double.parseDouble(financing_months);
                                double fee1 = Double.parseDouble(fee_ammount1)/finacing_months;
                                double fee2 = Double.parseDouble(fee_ammount2)/finacing_months;
                                double fee4 = Double.parseDouble(fee_ammount4);

                                double fee_amount_company_1 = participation*fee1;
                                double fee_amount_company_2 = participation*fee2;
                                double fee_amount_company_4 = participation*fee4;

                                double company_quote = my_quote+fee_amount_company_1+fee_amount_company_2+fee_amount_company_4;
                                String company_quote_st = decimalFormat.format(company_quote);


                                for (i = 1; i <= months; i += finance_frec,my_month+=finance_frec) {

                                    if (my_month > 12) {
                                        my_month = my_month - 12;
                                        my_month_st = String.valueOf(my_month);
                                        my_year=my_year+1;
                                        my_year_st = String.valueOf(my_year);
                                    } else {
                                        my_month = my_month;
                                        my_month_st = String.valueOf(my_month);
                                    }

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                    saveCurrentDate =currentDate.format(calForDate.getTime());

                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                    saveCurrentTime =currentTime.format(calForTime.getTime());

                                    String postRandomName = saveCurrentTime+saveCurrentDate;

                                    HashMap postMap = new HashMap();
                                    postMap.put("my_company_user",currentUserID);
                                    postMap.put("buyer_company_user","");
                                    postMap.put("my_company_id","");
                                    postMap.put("buyer_company_id",company_id);
                                    postMap.put("date",saveCurrentDate);
                                    postMap.put("time",saveCurrentTime);
                                    postMap.put("datetime",saveCurrentDate+saveCurrentTime);
                                    //My Company Information:
                                    postMap.put("my_company_name","");
                                    postMap.put("my_company_ruc","");
                                    postMap.put("my_company_image","");
                                    postMap.put("my_company_verification","");
                                    //Buyer Information:
                                    postMap.put("buyer_company_name",company_name);
                                    postMap.put("buyer_company_ruc",company_ruc);
                                    postMap.put("buyer_company_image",company_image);
                                    postMap.put("buyer_company_verification",company_verification);
                                    //Bill Information:
                                    Random rnd = new Random();
                                    int number = rnd.nextInt(999999);
                                    String randomNumbwe =String.valueOf(number);
                                    String bill_code = randomNumbwe+end_day+my_month_st+end_year+i;
                                    postMap.put("bill_ammount",my_quote_st);
                                    postMap.put("bill_company_ammount",company_quote_st);
                                    postMap.put("bill_currency",main_currency);
                                    postMap.put("bill_ammount_currency",my_quote_st);
                                    postMap.put("bill_issue_date",end_day+"/"+my_month_st+"/"+my_year_st);
                                    postMap.put("bill_end_date",my_last_day+"/"+my_month_st+"/"+my_year_st);
                                    postMap.put("bill_end_day",my_last_day);
                                    postMap.put("bill_end_month",my_month_st);
                                    postMap.put("bill_end_year",my_year_st);
                                    postMap.put("bill_state","Esperando día de cobranza");
                                    postMap.put("bill_id",currentUserID+bill_code);
                                    postMap.put("bill_factoring","false");
                                    postMap.put("bill_code",bill_code);
                                    postMap.put("payed","false");
                                    postMap.put("timestamp", ServerValue.TIMESTAMP);
                                    financeRequestRef.child("Investors").child(currentUserID).child("Bills").child("bill_number"+i).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterInvestmentOnFinanceRequestActivity.this, "Cargando Operación...", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }
                                registerOperation();
                                /*for (int i = 1; i <bill_number; i+=finance_frec) {
                                    HashMap billsMap = new HashMap();
                                    billsMap.put("bill_number",i);
                                    financeRequestRef.child("Investors").child(currentUserID).child("Bills").child("number"+i).updateChildren(billsMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(RegisterInvestmentOnFinanceRequestActivity.this, "EXITO", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }*/

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;
        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Inversión en Crowdfunding");
        myOperationMap.put("operation_type_code","DI");
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        //1 -> user need to upload the bill
        //2 -> the bill is being verificated
        //3 -> the ammount has been deposited
        //4 -> there is a notification about the ammount
        //5 -> bill is not valid
        myOperationMap.put("deposit_state","1");
        myOperationMap.put("deposit_ammount","");
        myOperationMap.put("deposit_currency","");
        myOperationMap.put("deposit_real_ammount","");
        myOperationMap.put("deposit_real_currency","");
        myOperationMap.put("uid",currentUserID);
        myOperationMap.put("operation_image",direct_investment);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");
        //Transfers
        myOperationMap.put("transfer_user_origin","");
        myOperationMap.put("transfer_user_destination","");
        myOperationMap.put("sent_ammount","");
        myOperationMap.put("sent_currency","");
        myOperationMap.put("recieved_ammount","");
        myOperationMap.put("recieved_currency","");
        //Direct Investments
        myOperationMap.put("company_finance_name",company_name);
        myOperationMap.put("finance_ammount",edtInvestmentAmmount.getText().toString());
        myOperationMap.put("finance_currency",main_currency);
        //Credit Line
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(RegisterInvestmentOnFinanceRequestActivity.this, InvesmentFundTransactionCompletedActivity.class);
                    intent.putExtra("PostKey", postKey);
                    intent.putExtra("TransactionCode",operationRandomName+"IFR");
                    startActivity(intent);
                }
            }
        });
    }


    private void showLayouts() {
        //transactionDetailsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        //importantDatesLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        edtInvestmentAmmount.setEnabled(false);
        rdPenAccount.setEnabled(false);
        rdUsdAccount.setEnabled(false);

        ammount_to_invest = Double.parseDouble(edtInvestmentAmmount.getText().toString());
        participation = ammount_to_invest/total_ammount;
        double interest_to_pay = Double.parseDouble(cost_of_debt);
        double my_interest_gained = interest_to_pay*participation;
        String my_interest_gained_st = decimalFormat.format(my_interest_gained);
        double months_of_financing = Double.parseDouble(financing_months);
        cost_rate = Double.parseDouble(invest_rate);
        double buy_rate = Double.parseDouble(currency_rate_buy);
        double sell_rate = Double.parseDouble(currency_rate_sell);

        double particip_100 = participation*100;
        participation_st = decimalFormat.format(particip_100);

        if (rdPenAccount.isChecked()&& main_currency.equals("PEN"))
        {
            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Monto a financiar: "+ammount_to_invest_st+" "+main_currency);
            txtTrea.setText("Rendimiento anual: "+interest_rate+"%");
            txtPaymentFrecuency.setText("Pagos: "+financing_frecuency);
            txtInterestAmmount.setText("Interés total ganado: "+my_interest_gained_st+" "+main_currency);
            txtGracePeriod.setText("Periodo de gracia: "+grace_period+" Meses");
            txtQuotesNumber.setText("Número de cuotas: "+financing_months+" "+financing_frecuency);
            txtTransactionCost.setText("Costo de transacción: "+invest_rate+"%");
            if (financing_frecuency.equals("Mensual")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing);
            }
            if (financing_frecuency.equals("Cada 2 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*2;
            }
            if (financing_frecuency.equals("Cada 3 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*3;
            }
            String my_quotes_st = decimalFormat.format(my_quotes);
            txtQuoteAmmount.setText("Monto a cobrar a cobrar "+financing_frecuency+": "+my_quotes_st+" "+main_currency);
            total_ammount_to_invest = ((cost_rate/100)*ammount_to_invest)+ammount_to_invest;
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+main_currency);
        }

        if (rdUsdAccount.isChecked()&& main_currency.equals("PEN"))
        {
            ammount_to_invest = ammount_to_invest*buy_rate;

            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Monto a financiar: "+ammount_to_invest_st+" "+main_currency);
            txtTrea.setText("Rendimiento anual: "+interest_rate+"%");
            txtPaymentFrecuency.setText("Pagos: "+financing_frecuency);
            txtInterestAmmount.setText("Interés total ganado: "+my_interest_gained_st+" "+main_currency);
            txtGracePeriod.setText("Periodo de gracia: "+grace_period+" Meses");
            txtQuotesNumber.setText("Número de cuotas: "+financing_months+" "+financing_frecuency);
            txtTransactionCost.setText("Costo de transacción: "+invest_rate+"%");
            if (financing_frecuency.equals("Mensual")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing);
            }
            if (financing_frecuency.equals("Cada 2 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*2;
            }
            if (financing_frecuency.equals("Cada 3 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*3;
            }
            String my_quotes_st = decimalFormat.format(my_quotes);
            txtQuoteAmmount.setText("Monto a cobrar a cobrar "+financing_frecuency+": "+my_quotes_st+" "+main_currency);
            total_ammount_to_invest = ((((cost_rate/100)*ammount_to_invest)+ammount_to_invest))/buy_rate;
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+"USD");
        }

        if (rdUsdAccount.isChecked()&& main_currency.equals("USD"))
        {
            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Monto a financiar: "+ammount_to_invest_st+" "+main_currency);
            txtTrea.setText("Rendimiento anual: "+interest_rate+"%");
            txtPaymentFrecuency.setText("Pagos: "+financing_frecuency);
            txtInterestAmmount.setText("Interés total ganado: "+my_interest_gained_st+" "+main_currency);
            txtGracePeriod.setText("Periodo de gracia: "+grace_period+" Meses");
            txtQuotesNumber.setText("Número de cuotas: "+financing_months+" "+financing_frecuency);
            txtTransactionCost.setText("Costo de transacción: "+invest_rate+"%");
            if (financing_frecuency.equals("Mensual")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing);
            }
            if (financing_frecuency.equals("Cada 2 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*2;
            }
            if (financing_frecuency.equals("Cada 3 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*3;
            }
            String my_quotes_st = decimalFormat.format(my_quotes);
            txtQuoteAmmount.setText("Monto a cobrar a cobrar "+financing_frecuency+": "+my_quotes_st+" "+main_currency);
            total_ammount_to_invest = ((cost_rate/100)*ammount_to_invest)+ammount_to_invest;
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+main_currency);
        }

        if (rdPenAccount.isChecked()&& main_currency.equals("USD"))
        {
            ammount_to_invest = ammount_to_invest/sell_rate;

            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Monto a financiar: "+ammount_to_invest_st+" "+main_currency);
            txtTrea.setText("Rendimiento anual: "+interest_rate+"%");
            txtPaymentFrecuency.setText("Pagos: "+financing_frecuency);
            txtInterestAmmount.setText("Interés total ganado: "+my_interest_gained_st+" "+main_currency);
            txtGracePeriod.setText("Periodo de gracia: "+grace_period+" Meses");
            txtQuotesNumber.setText("Número de cuotas: "+financing_months+" "+financing_frecuency);
            txtTransactionCost.setText("Costo de transacción: "+invest_rate+"%");
            if (financing_frecuency.equals("Mensual")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing);
            }
            if (financing_frecuency.equals("Cada 2 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*2;
            }
            if (financing_frecuency.equals("Cada 3 meses")) {
                my_quotes = (ammount_to_invest/months_of_financing)+(my_interest_gained/months_of_financing)*3;
            }
            String my_quotes_st = decimalFormat.format(my_quotes);
            txtQuoteAmmount.setText("Monto a cobrar "+financing_frecuency+": "+my_quotes_st+" "+main_currency);
            total_ammount_to_invest = ((((cost_rate/100)*ammount_to_invest)+ammount_to_invest))*sell_rate;
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+"PEN");
        }

        int end_day_val = Integer.parseInt(end_day);
        int end_month_val = Integer.parseInt(end_month);
        int end_year_val = Integer.parseInt(end_year);
        int grace_period_val = Integer.parseInt(grace_period);
        int month_date_end_grace_period = end_month_val+grace_period_val;
        int day_plus = Integer.parseInt(payment_space_days);

        if (month_date_end_grace_period > 12) {
            if (grace_period.equals("1")) {
                end_month_val = (end_month_val + 1) - 12;
                end_year_val = end_year_val + 1;
            }
            if (grace_period.equals("2")) {
                end_month_val = (end_month_val + 2) - 12;
                end_year_val = end_year_val + 1;
            }
            if (grace_period.equals("3")) {
                end_month_val = (end_month_val + 3) - 12;
                end_year_val = end_year_val + 1;
            }
        } else {
            end_month_val = end_month_val+grace_period_val;
        }

        int last_day_of_charge = end_day_val+day_plus;

        String day_value = String.valueOf(end_day_val);
        String month_value = String.valueOf(end_month_val);
        String year_value = String.valueOf(end_year_val);
        String last_day_of_charge_st = String.valueOf(last_day_of_charge);

        txtGracePeriodStartDate.setText("Inicia el perdio de gracia: "+end_day+"/"+end_month+"/"+end_year);
        txtGracePeriodEndDate.setText("Finaliza el perdio de gracia: "+day_value+"/"+month_value+"/"+year_value);
        txtPaymentStartDay.setText("Primer día de cobro: "+end_day+" de cada mes");
        txtEndStartDay.setText("Último día de cobro: "+last_day_of_charge_st+" de cada mes");
        txtStartPayment.setText("Puedes cobrar a partir del: "+day_value+"/"+month_value+"/"+year_value);


        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(100);
        transactionDetailsLayout.setLayoutParams(params);
        importantDatesLayout.setLayoutParams(params);


    }

    private void getDataDate() {
        String URL = "http://worldclockapi.com/api/json/est/now";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String current_date_time = response.getString("currentDateTime");
                            String date_only = current_date_time.substring(0, current_date_time.length()-12);

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                            String saveCurrentDate =currentDate.format(calForDate.getTime());

                            if (!date_only.equals(saveCurrentDate)) {
                                showDateErrorDialog();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(objectRequest);
    }

    private void showDateErrorDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.error_dialog,null);


        dialog.setView(finance_method);
        dialog.show();
    }
}
