package oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.RegisterInvestmentOnFinanceRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class RegisterInvestmentOnFactoringRequestActivity extends AppCompatActivity {

    TextView txtTittle,txtCompanyName,txtFinanceDestination,txtCompanyVerification,txtInvestedCurrency1,txtInvestedCurrency2,txtCurrencyRate,
            txtInvestmentAmmount,txtTrea,txtInterestAmmount,txtTransactionCost,txtTotalInvestment,txtTotalReturnDate,txtTotalReturn,edtInvestmentAmmount,txtDaysToFinance;
    ImageView imageCompany,imageCompanyVerification;
    RadioButton rdPenAccount,rdUsdAccount;
    CheckBox cbAgreement;
    Button btnInvest;
    String postKey,currentUserID,ammount,main_currency,company_name,company_image,company_verification,basic_account_pen,basic_account_usd,
            currency_rate_buy,currency_rate_sell,finishDate,bill_rate;
    private FirebaseAuth mAuth;
    private DatabaseReference factoringRequestRef,userRef,ratesRef,imageResourses,myOperationsRef, myCompaniesRef,billsRef,factoringToPay,oliverBankRef;
    RelativeLayout rootLayout;
    LinearLayout transactionDetailsLayout,importantDatesLayout,mainLayout;
    String ammount_to_invest_st,interest_rate,financing_frecuency,cost_of_debt,grace_period,financing_months,invest_rate,total_ammount_to_invest_st,payment_space_days,end_day,
            end_month,end_year,my_pin,username,profileimage, participation_st,direct_investment,saveCurrentDate,saveCurrentTime, operationRandomName,factoring_resguard_rate,my_company_id,buyer_company_id,
            current_account_pen,current_account_usd,bill_id,factoring_image,factoring_ammount_df,investment_currency,bill_factoring,factoring_fee_rate1,factoring_fee_rate2,factoring_fee_rate3,
            finance_request_investment_pen,finance_request_investment_usd,factoring_request_fee_pen,factoring_request_fee_usd;
    double total_ammount,my_quotes,total_ammount_to_invest,my_pen_aacount,my_usd_aacount,ammount_to_invest,periodical_discount_rate,resguard_rate,factoring_ammount,
            finance_request_investment_pen_db,finance_request_investment_usd_db,cost_rate,oliverbank_fee,factoring_request_fee_pen_db,factoring_request_fee_usd_db;
    private ProgressDialog loadingBar;
    DecimalFormat decimalFormat;
    long diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_investment_on_factoring_request);

        postKey = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        factoringRequestRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests").child(postKey);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        myCompaniesRef =FirebaseDatabase.getInstance().getReference().child("My Companies");
        billsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");
        factoringToPay = FirebaseDatabase.getInstance().getReference().child("Factoring To Pay");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

        loadingBar = new ProgressDialog(this);

        txtTittle = findViewById(R.id.txtTittle);
        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtFinanceDestination = findViewById(R.id.txtFinanceDestination);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);
        txtInvestedCurrency1 = findViewById(R.id.txtInvestedCurrency1);
        txtInvestedCurrency2 = findViewById(R.id.txtInvestedCurrency2);
        edtInvestmentAmmount = findViewById(R.id.edtInvestmentAmmount);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        txtInvestmentAmmount = findViewById(R.id.txtInvestmentAmmount);
        txtTrea = findViewById(R.id.txtTrea);
        txtInterestAmmount = findViewById(R.id.txtInterestAmmount);
        txtTransactionCost= findViewById(R.id.txtTransactionCost);
        txtTotalInvestment= findViewById(R.id.txtTotalInvestment);
        cbAgreement= findViewById(R.id.cbAgreement);
        btnInvest= findViewById(R.id.btnInvest);
        rootLayout = findViewById(R.id.rootLayout);
        transactionDetailsLayout = findViewById(R.id.transactionDetailsLayout);
        importantDatesLayout = findViewById(R.id.importantDatesLayout);
        txtDaysToFinance = findViewById(R.id.txtDaysToFinance);
        txtTotalReturnDate = findViewById(R.id.txtTotalReturnDate);
        txtTotalReturn = findViewById(R.id.txtTotalReturn);
        mainLayout = findViewById(R.id.mainLayout);

        transactionDetailsLayout.getLayoutParams().height = 0;
        importantDatesLayout.getLayoutParams().height = 0;
        mainLayout.getLayoutParams().height = 0;

        decimalFormat = new DecimalFormat("0.00");

        getDataDate();

        factoringRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ammount = dataSnapshot.child("bill_ammount").getValue().toString();
                    main_currency = dataSnapshot.child("main_currency").getValue().toString();
                    company_name = dataSnapshot.child("company_name").getValue().toString();
                    company_image = dataSnapshot.child("company_image").getValue().toString();
                    company_verification = dataSnapshot.child("company_verification").getValue().toString();
                    interest_rate = dataSnapshot.child("bill_rate").getValue().toString();
                    end_day = dataSnapshot.child("end_day").getValue().toString();
                    end_month = dataSnapshot.child("end_month").getValue().toString();
                    end_year = dataSnapshot.child("end_year").getValue().toString();
                    bill_rate = dataSnapshot.child("bill_rate").getValue().toString();
                    my_company_id = dataSnapshot.child("my_compnay_id").getValue().toString();
                    buyer_company_id = dataSnapshot.child("buyer_company_id").getValue().toString();
                    bill_id = dataSnapshot.child("bill_id").getValue().toString();

                    total_ammount = Double.parseDouble(ammount);

                    Picasso.with(RegisterInvestmentOnFactoringRequestActivity.this).load(company_image).fit().into(imageCompany);
                    txtTittle.setText(company_name);
                    txtCompanyName.setText("Nombre comercial: "+company_name);
                    txtTrea.setText("Rendimiento Anual: "+bill_rate+"%");
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

                    txtTotalReturnDate.setText("Retorno total efectuado el: "+end_day+"/"+end_month+"/"+end_year);
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                    saveCurrentDate =currentDate.format(calForDate.getTime());
                    finishDate = end_day+" "+end_month+" "+end_year;
                    try {
                        Date date1 = currentDate.parse(saveCurrentDate);
                        Date date2 = currentDate.parse(finishDate);
                        diff = date2.getTime() - date1.getTime();
                        txtDaysToFinance.setText("Periodo a financiar: "+ TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Tasa efectiva diaria:
                    double real_rate = Double.parseDouble(bill_rate);

                    double rate_perone = real_rate/100;
                    double monthly_rate_base = 1+rate_perone;
                    double monthly_rate_exp = 0.002777777;
                    double monthly_rate = Math.pow(monthly_rate_base,monthly_rate_exp)-1;
                    double monthly_rate_express = monthly_rate*100;
                    double days_to__finance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    periodical_discount_rate = Math.pow(1+monthly_rate,days_to__finance)-1;
                    double periodical_discount_rate_express = periodical_discount_rate*100;

                    DecimalFormat decimalFormat = new DecimalFormat("0.0000");
                    String periodical_discount_rate_express_df = decimalFormat.format(periodical_discount_rate_express);

                    ratesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                factoring_resguard_rate = dataSnapshot.child("factoring_resguard_rate").getValue().toString();
                                resguard_rate = Double.parseDouble(factoring_resguard_rate);
                                factoring_fee_rate1 = dataSnapshot.child("factoring_fee_rate1").getValue().toString();
                                factoring_fee_rate2 = dataSnapshot.child("factoring_fee_rate2").getValue().toString();
                                factoring_fee_rate3 = dataSnapshot.child("factoring_fee_rate3").getValue().toString();

                                double bill_amount = Double.parseDouble(ammount);
                                double discount_ammount_before = bill_amount-0;
                                double discount_ammount = discount_ammount_before*periodical_discount_rate;

                                DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
                                double resguard_ammount = 0;
                                String resguard_ammount_df = decimalFormat2.format(resguard_ammount);

                                //txtResguardAmmount.setText("Resguardo: "+resguard_ammount_df+" "+main_currency);

                                factoring_ammount = bill_amount-0-discount_ammount-resguard_ammount;
                                factoring_ammount_df = decimalFormat2.format(factoring_ammount);

                                edtInvestmentAmmount.setText(factoring_ammount_df+" "+main_currency);
                                txtInvestmentAmmount.setText("Monto a financiar: "+factoring_ammount_df+" "+main_currency);

                                txtTotalReturn.setText("Retorno total: "+ammount+" "+main_currency);

                                String discount_ammount_df = decimalFormat2.format(discount_ammount);
                                txtInterestAmmount.setText("Interés total ganado: "+discount_ammount_df+" "+main_currency);

                                userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
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
                                                        txtTransactionCost.setText("Costo de transacción: "+invest_rate+"%");

                                                        imageResourses.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists())
                                                                {
                                                                    direct_investment = dataSnapshot.child("direct_investment").getValue().toString();
                                                                    factoring_image = dataSnapshot.child("factoring_image").getValue().toString();
                                                                    myCompaniesRef.child(my_company_id).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()) {
                                                                                current_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                                                                                current_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                                                                                oliverBankRef.addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.exists()) {
                                                                                            finance_request_investment_pen = dataSnapshot.child("Factoring Request Investment PEN").getValue().toString();
                                                                                            finance_request_investment_usd = dataSnapshot.child("Factoring Request Investment USD").getValue().toString();

                                                                                            factoring_request_fee_pen = dataSnapshot.child("Factoring Request Fee PEN").getValue().toString();
                                                                                            factoring_request_fee_usd = dataSnapshot.child("Factoring Request Fee USD").getValue().toString();

                                                                                            factoring_request_fee_pen_db = Double.parseDouble(factoring_request_fee_pen);
                                                                                            factoring_request_fee_usd_db = Double.parseDouble(factoring_request_fee_usd);

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

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    rdPenAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    rdPenAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }else
                {
                    rdPenAccount.setChecked(true);
                    showLayouts();
                }
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    rdUsdAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInvestmentAmmount.getText().toString())) {
                    rdUsdAccount.setChecked(false);
                    Snackbar.make(rootLayout, "Debes ingresar el monto a invertir...", Snackbar.LENGTH_LONG).show();
                    return;
                }else
                {
                    rdUsdAccount.setChecked(true);
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
                    if (total_ammount_to_invest > my_usd_aacount)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficiente dinero para esta operación...", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                else
                {
                    pinDialog();
                }
            }
        });
    }

    private void pinDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("PIN de Seguridad");
        dialog.setIcon(R.drawable.pin_icon);
        dialog.setMessage("Ingresa tu PIN de seguridad");

        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pin_dialog_layout,null);

        final EditText edtPin = pin_dialog.findViewById(R.id.edtPin);
        Button btnConfirmDeposit = pin_dialog.findViewById(R.id.btnConfirmDeposit);
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
                    sendMoneyToCompanyFromUser();
                    finish();
                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }

    private void sendMoneyToCompanyFromUser() {
        HashMap hashMap = new HashMap();
        if (rdPenAccount.isChecked()) {
            double new_value_on_pen_account = my_pen_aacount-total_ammount_to_invest;
            String pen_account_value = decimalFormat.format(new_value_on_pen_account);

            double factorign_request_value_updated = finance_request_investment_pen_db+(cost_rate/100)*ammount_to_invest;
            String factorign_request_value_updated_st = decimalFormat.format(factorign_request_value_updated);
            oliverBankRef.child("Factoring Request Investment PEN").setValue(factorign_request_value_updated_st);

            hashMap.put("basic_account_pen",pen_account_value);

        }
        if (rdUsdAccount.isChecked()) {
            double new_value_on_usd_account = my_usd_aacount-total_ammount_to_invest;
            String usd_account_value = decimalFormat.format(new_value_on_usd_account);

            double factorign_request_value_updated = finance_request_investment_usd_db+(cost_rate/100)*ammount_to_invest;
            String factorign_request_value_updated_st = decimalFormat.format(factorign_request_value_updated);
            oliverBankRef.child("Factoring Request Investment USD").setValue(factorign_request_value_updated_st);

            hashMap.put("basic_account_usd",usd_account_value);

        }
        userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    double factoring_fee1 = Double.parseDouble(factoring_fee_rate1);
                    double factoring_fee2 = Double.parseDouble(factoring_fee_rate2);
                    double factoring_fee3 = Double.parseDouble(factoring_fee_rate3);
                    double sume_fee = (factoring_fee1+factoring_fee2+factoring_fee3)/100;
                    oliverbank_fee = ammount_to_invest*sume_fee;

                            HashMap hashMap = new HashMap();
                    if (main_currency.equals("PEN")) {
                        double pen_account = Double.parseDouble(current_account_pen);
                        double new_value_account = pen_account+ammount_to_invest-oliverbank_fee;

                        String new_value_account_st = decimalFormat.format(new_value_account);

                        //
                        double factoring_fee_updated = factoring_request_fee_pen_db+oliverbank_fee;
                        String factoring_fee = decimalFormat.format(factoring_fee_updated);
                        oliverBankRef.child("Factoring Request Fee PEN").setValue(factoring_fee);

                        hashMap.put("current_account_pen",new_value_account_st);
                    }
                    if (main_currency.equals("USD")) {
                        double usd_account = Double.parseDouble(current_account_usd);
                        double new_value_account = usd_account+ammount_to_invest-oliverbank_fee;
                        String new_value_account_st = decimalFormat.format(new_value_account);

                        //
                        double factoring_fee_updated = factoring_request_fee_usd_db+oliverbank_fee;
                        String factoring_fee = decimalFormat.format(factoring_fee_updated);
                        oliverBankRef.child("Factoring Request Fee USD").setValue(factoring_fee);

                        hashMap.put("current_account_usd",new_value_account_st);
                    }
                    myCompaniesRef.child(my_company_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            HashMap billMap = new HashMap();
                            billMap.put("bill_state","Financiado Exitosamente");
                            billMap.put("bill_investor_id",currentUserID);
                            billsRef.child(bill_id).updateChildren(billMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        HashMap newMap = new HashMap();
                                        newMap.put("bill_state","Financiado Exitosamente");
                                        newMap.put("bill_factoring","success");
                                        billsRef.child(bill_id).updateChildren(newMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    registerOperation();
                                                }
                                            }
                                        });
                                    }
                                }
                            });


                        }
                    });

                }

            }
        });
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

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());;
        if (rdUsdAccount.isChecked()) {
            investment_currency = "PEN";
        }
        if (rdUsdAccount.isChecked()) {
            investment_currency = "USD";
        }

        operationRandomName = saveCurrentDate+saveCurrentTime;
        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Inversión en Factoring");
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
        myOperationMap.put("operation_image",factoring_image);
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
        myOperationMap.put("finance_ammount",ammount_to_invest_st);
        myOperationMap.put("finance_currency",investment_currency);
        //Credit Line
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {

                    HashMap hashMap1 = new HashMap();
                    hashMap1.put("bill_id", bill_id);
                    hashMap1.put("state","Financiado Exitosamente");
                    hashMap1.put("investor",currentUserID);
                    hashMap1.put("bill_ammount",ammount);
                    hashMap1.put("bill_currency",main_currency);
                    factoringToPay.child(bill_id).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(RegisterInvestmentOnFactoringRequestActivity.this, InvesmentFundTransactionCompletedActivity.class);
                                    intent.putExtra("PostKey", postKey);
                                    intent.putExtra("TransactionCode",operationRandomName+"FT");
                                    startActivity(intent);
                                }
                            }
                        }
                    });


                }
            }
        });
    }

    private void showLayouts() {
        edtInvestmentAmmount.setEnabled(false);
        rdPenAccount.setEnabled(false);
        rdUsdAccount.setEnabled(false);
        ammount_to_invest = factoring_ammount;
        cost_rate = Double.parseDouble(invest_rate);
        double buy_rate = Double.parseDouble(currency_rate_buy);
        double sell_rate = Double.parseDouble(currency_rate_sell);

        if (rdPenAccount.isChecked()&& main_currency.equals("PEN"))
        {
            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Valor de la factura: "+ammount+" PEN");
            total_ammount_to_invest = ((cost_rate/100)*ammount_to_invest)+ammount_to_invest;
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+main_currency);
        }

        if (rdUsdAccount.isChecked()&& main_currency.equals("PEN"))
        {
            ammount_to_invest = ammount_to_invest/sell_rate;
            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Valor de la factura: "+ammount+" PEN");
            total_ammount_to_invest = ((((cost_rate/100)*ammount_to_invest)+ammount_to_invest));
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+"USD");

        }

        if (rdUsdAccount.isChecked()&& main_currency.equals("USD"))
        {
            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Valor de la factura: "+ammount+" USD");
            total_ammount_to_invest = ((cost_rate/100)*ammount_to_invest)+ammount_to_invest;
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+main_currency);
        }

        if (rdPenAccount.isChecked()&& main_currency.equals("USD"))
        {
            ammount_to_invest = ammount_to_invest*buy_rate;
            ammount_to_invest_st = decimalFormat.format(ammount_to_invest);
            txtInvestmentAmmount.setText("Valor de la factura: "+ammount+" USD");
            total_ammount_to_invest = ((((cost_rate/100)*ammount_to_invest)+ammount_to_invest));
            total_ammount_to_invest_st = decimalFormat.format(total_ammount_to_invest);
            txtTotalInvestment.setText("TOTAL A INVERTIR: "+total_ammount_to_invest_st+" "+"PEN");
        }

        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(100);
        transactionDetailsLayout.setLayoutParams(params);
        importantDatesLayout.setLayoutParams(params);


    }
}
