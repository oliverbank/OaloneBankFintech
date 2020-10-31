package oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class InvestmentFundRescueActivity extends AppCompatActivity {

    TextView txtCurrentQuoteValue,txtMyLastQuoteValue,txtMyLastTransaction,txtMyQuotesNumber,txtMyFundInvestment,txtMyLastTransactionDate,txtDaysSinceLastTransaction,txtCurrencyRate,
            txtQuotesAdquisitionAmmount,txtSuscriptionFeeAmmount,txtTotalInvestment,txtMyFundInvestmentParticipation,txtMinDaysFund;
    private FirebaseAuth mAuth;
    private DatabaseReference investment_fund_ref,userRef, ratesRef,investmentFundTransactionsRef, myInvestmentFundTransRef,myOperationsRef,oliverBankRef;
    String currentUserID,postKey,fund_name,fund_description,fund_investment,quote_value,fund_currency,quotes_circulating,fund_anual_profit,fund_monthly_profit,min_days,
            recommended_days,fund_risk,suscription_quote_fee,fund_management_fee,fund_rescue,user_verification,basic_account_pen,basic_account_usd,currency_rate_sell,
            currency_rate_buy,fund_code,saveCurrentDate,saveCurrentTime,last_quote_value,last_investment,my_quotes,my_equity,last_transaction_date,quotes_adquisition_ammount_st,
            fee_ammount_st,my_real_total_investment_st,operationRandomName,fund_image,investment_fund_sucription_pen,investment_fund_sucription_usd;
    RadioButton rdPenAccount,rdUsdAccount;
    RelativeLayout rootLayout;
    String rescue_currency = "";
    Button btnQuotesNumber,btnRescueFunds;
    CheckBox cbAgreement;
    private ProgressDialog loadingBar;

    ArrayList<String> quotes =new ArrayList<>();
    SpinnerDialog spinnerQuotes;

    double quotes_to_rescue, quote_value_to_invest, currrency_sell_rate,currrency_buy_rate,quotes_adquisition_ammount,suscription_quotes_fee,suscription_quotes_fee_ammount,
            total_ammount_to_invest,current_quote_value_in_fund, current_equity_in_fund, current_quotes_circulating_in_fund, my_total_quotes,my_total_equity,total_equity_in_this_fund,
            investment_fund_sucription_pen_db,investment_fund_sucription_usd_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_fund_rescue);

        txtCurrentQuoteValue = findViewById(R.id.txtCurrentQuoteValue);
        txtMyLastQuoteValue = findViewById(R.id.txtMyLastQuoteValue);
        txtMyLastTransaction = findViewById(R.id.txtMyLastTransaction);
        txtMyQuotesNumber = findViewById(R.id.txtMyQuotesNumber);
        txtMyFundInvestment = findViewById(R.id.txtMyFundInvestment);
        txtMyLastTransactionDate = findViewById(R.id.txtMyLastTransactionDate);
        txtDaysSinceLastTransaction = findViewById(R.id.txtDaysSinceLastTransaction);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        btnQuotesNumber = findViewById(R.id.btnQuotesNumber);
        btnRescueFunds = findViewById(R.id.btnRescueFunds);
        txtQuotesAdquisitionAmmount = findViewById(R.id.txtQuotesAdquisitionAmmount);
        txtSuscriptionFeeAmmount = findViewById(R.id.txtSuscriptionFeeAmmount);
        txtTotalInvestment = findViewById(R.id.txtTotalInvestment);
        cbAgreement = findViewById(R.id.cbAgreement);
        rootLayout = findViewById(R.id.rootLayout);
        txtMyFundInvestmentParticipation = findViewById(R.id.txtMyFundInvestmentParticipation);
        txtMinDaysFund = findViewById(R.id.txtMinDaysFund);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        investment_fund_ref = FirebaseDatabase.getInstance().getReference().child("Investment Funds").child(postKey);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        investmentFundTransactionsRef = FirebaseDatabase.getInstance().getReference().child("Investment Fund Transactions");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

        getDataDate();

        investment_fund_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    fund_name = dataSnapshot.child("fund_name").getValue().toString();
                    fund_image = dataSnapshot.child("fund_image").getValue().toString();
                    fund_description = dataSnapshot.child("fund_description").getValue().toString();
                    fund_investment = dataSnapshot.child("fund_investment").getValue().toString();
                    quote_value= dataSnapshot.child("quote_value").getValue().toString();
                    fund_currency = dataSnapshot.child("fund_currency").getValue().toString();
                    quotes_circulating = dataSnapshot.child("quotes_circulating").getValue().toString();
                    fund_anual_profit = dataSnapshot.child("fund_anual_profit").getValue().toString();
                    fund_monthly_profit = dataSnapshot.child("fund_monthly_profit").getValue().toString();
                    min_days = dataSnapshot.child("min_days").getValue().toString();
                    recommended_days = dataSnapshot.child("recommended_days").getValue().toString();
                    fund_risk = dataSnapshot.child("fund_risk").getValue().toString();
                    suscription_quote_fee = dataSnapshot.child("suscription_quote_fee").getValue().toString();
                    fund_management_fee = dataSnapshot.child("fund_management_fee").getValue().toString();
                    fund_rescue = dataSnapshot.child("fund_rescue").getValue().toString();
                    fund_code = dataSnapshot.child("fund_code").getValue().toString();

                    txtCurrentQuoteValue.setText("Valor Cuota Actual: "+quote_value+" "+fund_currency);
                    txtMinDaysFund.setText("Plazo mínimo de permanencia: "+min_days+" días");

                    oliverBankRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                investment_fund_sucription_pen = dataSnapshot.child("Investment Fund Rescue PEN").getValue().toString();
                                investment_fund_sucription_usd = dataSnapshot.child("Investment Fund Rescue USD").getValue().toString();

                                investment_fund_sucription_pen_db = Double.parseDouble(investment_fund_sucription_pen);
                                investment_fund_sucription_usd_db = Double.parseDouble(investment_fund_sucription_usd);
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


        investmentFundTransactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentUserID+fund_code) || !dataSnapshot.child(currentUserID+fund_code).hasChild("fund_code"))
                {
                    //Crear referencia con valores cero
                    HashMap postMap = new HashMap();
                    postMap.put("uid",currentUserID);
                    postMap.put("fund_code",fund_code);
                    postMap.put("last_quote_value","0.00");
                    postMap.put("last_investment","0.00");
                    postMap.put("my_quotes","0.00");
                    postMap.put("my_equity","0.00");
                    postMap.put("last_transaction_date","-");
                    investmentFundTransactionsRef.child(currentUserID+fund_code).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                //Toast.makeText(InvestmentFundDetailActivity.this, "Se ha creado una referencia para este fondo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    //MOSTRAR MIS DATOS DEL FONDO
                    //oast.makeText(InvestmentFundDetailActivity.this, "Aqui se muestran los datos de mi participación del fondo", Toast.LENGTH_SHORT).show();
                    investmentFundTransactionsRef.child(currentUserID+fund_code).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                last_quote_value = dataSnapshot.child("last_quote_value").getValue().toString();
                                last_investment = dataSnapshot.child("last_investment").getValue().toString();
                                my_quotes = dataSnapshot.child("my_quotes").getValue().toString();
                                my_equity = dataSnapshot.child("my_equity").getValue().toString();
                                last_transaction_date = dataSnapshot.child("last_transaction_date").getValue().toString();

                                double my_quotes_in_fund = Double.parseDouble(my_quotes);
                                int innnn = (int) Math.round(my_quotes_in_fund);
                                String my_quotes_in_fund_st = String.valueOf(innnn);

                                DecimalFormat decimalFormat = new DecimalFormat("0");

                                double current_quote_value = Double.parseDouble(quote_value);
                                double my_participation = innnn*current_quote_value;
                                String my_participation_st =  decimalFormat.format(my_participation);

                                txtMyLastQuoteValue.setText("Último Valor Cuota adquirido en: "+last_quote_value+" "+fund_currency);
                                txtMyLastTransaction.setText("Último aporte al fondo: "+last_investment+" "+fund_currency);
                                txtMyQuotesNumber.setText("Cuotas adquiridas: "+my_quotes_in_fund_st);
                                txtMyFundInvestment.setText("Mis aportes acumulados: "+my_equity+" "+fund_currency);
                                txtMyLastTransactionDate.setText("Fecha del último aporte: "+last_transaction_date);
                                txtMyFundInvestmentParticipation.setText("Mi patrimonio actual en el fondo: "+my_participation_st+" "+fund_currency);

                                Calendar calForDate = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                saveCurrentDate =currentDate.format(calForDate.getTime());
                                try {
                                    Date date1 = currentDate.parse(last_transaction_date);
                                    Date date2 = currentDate.parse(saveCurrentDate);
                                    long diff = date2.getTime() - date1.getTime();
                                    txtDaysSinceLastTransaction.setText("Días transcurridos: "+ TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_verification = dataSnapshot.child("user_verification").getValue().toString();
                basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+basic_account_pen);
                rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+basic_account_usd);
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

                    txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                rescue_currency = "PEN";
                rdPenAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdPenAccount.setTextColor(Color.BLACK);
                rdUsdAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdUsdAccount.setTextColor(Color.BLACK);
                rdUsdAccount.setEnabled(false);
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                rescue_currency = "USD";
                rdPenAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdPenAccount.setTextColor(Color.BLACK);
                rdUsdAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdUsdAccount.setTextColor(Color.BLACK);
                rdPenAccount.setEnabled(false);
            }
        });

        quotes.add("1");quotes.add("2");quotes.add("3");quotes.add("4");quotes.add("5");quotes.add("6");quotes.add("7");quotes.add("8");quotes.add("9");quotes.add("10");
        quotes.add("11");quotes.add("12");quotes.add("13");quotes.add("14");quotes.add("15");quotes.add("16");quotes.add("17");quotes.add("18");quotes.add("19");quotes.add("20");
        quotes.add("21");quotes.add("22");quotes.add("23");quotes.add("24");quotes.add("25");quotes.add("26");quotes.add("27");quotes.add("28");quotes.add("29");quotes.add("30");
        quotes.add("31");quotes.add("32");quotes.add("33");quotes.add("34");quotes.add("35");quotes.add("36");quotes.add("37");quotes.add("38");quotes.add("39");quotes.add("40");
        quotes.add("41");quotes.add("42");quotes.add("43");quotes.add("44");quotes.add("45");quotes.add("46");quotes.add("47");quotes.add("48");quotes.add("49");quotes.add("50");
        quotes.add("51");quotes.add("52");quotes.add("53");quotes.add("54");quotes.add("55");quotes.add("56");quotes.add("57");quotes.add("58");quotes.add("59");quotes.add("60");
        quotes.add("61");quotes.add("62");quotes.add("63");quotes.add("64");quotes.add("65");quotes.add("66");quotes.add("67");quotes.add("68");quotes.add("69");quotes.add("70");
        quotes.add("71");quotes.add("72");quotes.add("73");quotes.add("74");quotes.add("75");quotes.add("76");quotes.add("77");quotes.add("78");quotes.add("79");quotes.add("80");
        quotes.add("81");quotes.add("82");quotes.add("83");quotes.add("84");quotes.add("85");quotes.add("86");quotes.add("87");quotes.add("88");quotes.add("89");quotes.add("90");
        quotes.add("91");quotes.add("92");quotes.add("93");quotes.add("94");quotes.add("95");quotes.add("96");quotes.add("97");quotes.add("98");quotes.add("99");quotes.add("100");
        quotes.add("101");quotes.add("102");quotes.add("103");quotes.add("104");quotes.add("105");quotes.add("106");quotes.add("107");quotes.add("108");quotes.add("109");quotes.add("110");
        quotes.add("111");quotes.add("112");quotes.add("113");quotes.add("114");quotes.add("115");quotes.add("116");quotes.add("117");quotes.add("118");quotes.add("119");quotes.add("120");

        btnQuotesNumber.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    rdPenAccount.setBackground(getDrawable(R.drawable.button3_background));
                    rdPenAccount.setTextColor(Color.WHITE);
                    rdUsdAccount.setBackground(getDrawable(R.drawable.button3_background));
                    rdUsdAccount.setTextColor(Color.WHITE);
                    Snackbar.make(rootLayout, "Selecciona una cuenta primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    spinnerQuotes.showSpinerDialog();
                }
            }
        });

        spinnerQuotes = new SpinnerDialog(InvestmentFundRescueActivity.this,quotes,"Selecciona el nùmero de cuotas");
        spinnerQuotes.bindOnSpinerListener(new OnSpinerItemClick() {
            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(String item, int i) {
                btnQuotesNumber.setText(item);
                //Make operation here:
                quotes_to_rescue = Double.parseDouble(item);
                quote_value_to_invest = Double.parseDouble(quote_value);
                currrency_sell_rate = Double.parseDouble(currency_rate_sell);
                currrency_buy_rate = Double.parseDouble(currency_rate_buy);
                if (fund_currency.equals(rescue_currency))
                {
                    quotes_adquisition_ammount = quotes_to_rescue*quote_value_to_invest;
                }
                if (fund_currency.equals("USD") && rescue_currency.equals("PEN"))
                {
                    quotes_adquisition_ammount = (quotes_to_rescue*quote_value_to_invest)*currrency_sell_rate;
                }
                if (fund_currency.equals("PEN") && rescue_currency.equals("USD"))
                {
                    quotes_adquisition_ammount = (quotes_to_rescue*quote_value_to_invest)/currrency_buy_rate;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                quotes_adquisition_ammount_st = decimalFormat.format(quotes_adquisition_ammount);
                txtQuotesAdquisitionAmmount.setText("Monto a rescatar: "+quotes_adquisition_ammount_st+" "+rescue_currency);

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                saveCurrentDate =currentDate.format(calForDate.getTime());
                try {
                    Date date1 = currentDate.parse(last_transaction_date);
                    Date date2 = currentDate.parse(saveCurrentDate);
                    long diff = date2.getTime() - date1.getTime();
                    //txtDaysSinceLastTransaction.setText("Días transcurridos: "+ TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        int day_have_passed = Math.toIntExact(TimeUnit.DAYS.convert(diff,TimeUnit.MICROSECONDS));
                        int min_days_for_rescue = Integer.parseInt(min_days);

                        if (day_have_passed < min_days_for_rescue)
                        {
                            suscription_quotes_fee = Double.parseDouble(fund_rescue);
                            suscription_quotes_fee_ammount = (suscription_quotes_fee/100)*quotes_adquisition_ammount;
                            total_ammount_to_invest = quotes_adquisition_ammount-suscription_quotes_fee_ammount;
                            fee_ammount_st = decimalFormat.format(suscription_quotes_fee_ammount);
                            my_real_total_investment_st = decimalFormat.format(total_ammount_to_invest);
                            txtSuscriptionFeeAmmount.setText("Costo de Rescate Anticipado: "+fee_ammount_st+" "+rescue_currency);
                            txtSuscriptionFeeAmmount.setTextColor(getResources().getColor(R.color.redColor));
                            txtTotalInvestment.setText("RESCATE TOTAL: "+my_real_total_investment_st+" "+rescue_currency);
                        }
                        if (day_have_passed >= min_days_for_rescue)
                        {
                            total_ammount_to_invest = quotes_adquisition_ammount;
                            fee_ammount_st = decimalFormat.format(suscription_quotes_fee_ammount);
                            my_real_total_investment_st = decimalFormat.format(total_ammount_to_invest);
                            txtSuscriptionFeeAmmount.setText("Costo de Rescate Anticipado: "+fee_ammount_st+" "+rescue_currency);
                            txtTotalInvestment.setText("RESCATE TOTAL: "+my_real_total_investment_st+" "+rescue_currency);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnRescueFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double quotes_selected = Double.parseDouble(btnQuotesNumber.getText().toString());
                double my_quotes_in_fund = Double.parseDouble(my_quotes);
                if (quotes_selected > my_quotes_in_fund)
                {
                    Snackbar.make(rootLayout, "Sólo puedes rescatar un máximo de "+my_quotes+" cuotas o certificados de participación", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Selecciona una cuenta primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (btnQuotesNumber.getText().toString().equals("0"))
                {
                    Snackbar.make(rootLayout, "Debes adquirir al menos una cuota...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!cbAgreement.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes declarar tu consentimiento para esta operación...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (quotes_selected > my_quotes_in_fund)
                {
                    Snackbar.make(rootLayout, "Sólo puedes rescatar un máximo de "+my_quotes+" cuotas o certificados de participación", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    loadingBar.setTitle("Realizando transacción");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    registerMyInvestmentFundTransaction();
                    registerMyOperations();
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

    private void registerMyOperations() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Rescate de Fondo de Inversión");
        myOperationMap.put("operation_type_code","IFR");
        myOperationMap.put("fund_name",fund_name);
        myOperationMap.put("fund_image",fund_image);
        myOperationMap.put("fund_currency",fund_currency);
        myOperationMap.put("fund_quote_value",quote_value);
        myOperationMap.put("fund_quotes_bought",btnQuotesNumber.getText().toString());
        myOperationMap.put("fund_my_investment",quotes_adquisition_ammount_st);
        myOperationMap.put("fund_suscription_fee",fee_ammount_st);
        myOperationMap.put("fund_total_transaction_cost",my_real_total_investment_st);
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        myOperationMap.put("fund_transaction_currency",rescue_currency);
        myOperationMap.put("uid",currentUserID);
        myOperationMap.put("operation_image",fund_image);
        myOperationMap.put("deposit_ammount","");
        myOperationMap.put("deposit_currency","");
        myOperationMap.put("deposit_real_ammount","");
        myOperationMap.put("deposit_real_currency","");
        myOperationMap.put("deposit_state","");

        myOperationMap.put("transfer_user_origin","");
        myOperationMap.put("transfer_user_destination","");
        myOperationMap.put("sent_ammount","");
        myOperationMap.put("sent_currency","");
        myOperationMap.put("recieved_ammount","");
        myOperationMap.put("recieved_currency","");
        //Direct Investments
        myOperationMap.put("company_finance_name","");
        myOperationMap.put("finance_ammount","");
        myOperationMap.put("finance_currency","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent = new Intent(InvestmentFundRescueActivity.this,InvesmentFundTransactionCompletedActivity.class);
                intent.putExtra("PostKey", postKey);
                intent.putExtra("TransactionCode",operationRandomName+fund_code);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerMyInvestmentFundTransaction() {
        //Actualizar mi partiipación en el fondo
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        current_quote_value_in_fund = Double.parseDouble(quote_value);
        current_equity_in_fund = Double.parseDouble(my_equity);
        current_quotes_circulating_in_fund = Double.parseDouble(my_quotes);

        my_total_quotes = (current_quotes_circulating_in_fund-quotes_to_rescue);

        if (fund_currency.equals(rescue_currency))
        {
            //my_total_equity = (current_equity_in_fund-quotes_adquisition_ammount);
        }
        if (fund_currency.equals("PEN") && rescue_currency.equals("USD"))
        {
            //my_total_equity = (current_equity_in_fund-(quotes_adquisition_ammount*currrency_buy_rate));
        }
        if (fund_currency.equals("USD") && rescue_currency.equals("PEN"))
        {
            //my_total_equity = (current_equity_in_fund-(quotes_adquisition_ammount/currrency_sell_rate));
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        String my_total_quotes_st = decimalFormat1.format(my_total_quotes);
        String my_total_equity_st = decimalFormat.format(my_total_equity);

        investmentFundTransactionsRef.child(currentUserID+fund_code).child("my_quotes").setValue(my_total_quotes_st);
        //investmentFundTransactionsRef.child(currentUserID+fund_code).child("my_equity").setValue(my_total_equity_st);

        //Actualizar datos del fondo

        double quotes_circulating_in_this_fund = Double.parseDouble(quotes_circulating);
        double equity_in_this_fund = Double.parseDouble(fund_investment);

        double total_quotes_in_this_fund = quotes_circulating_in_this_fund-quotes_to_rescue;

        if (fund_currency.equals(rescue_currency))
        {
            total_equity_in_this_fund = equity_in_this_fund - quotes_adquisition_ammount;
        }
        if (fund_currency.equals("PEN") && rescue_currency.equals("USD"))
        {
            total_equity_in_this_fund = equity_in_this_fund-(quotes_adquisition_ammount*currrency_buy_rate);
        }
        if (fund_currency.equals("USD") && rescue_currency.equals("PEN"))
        {
            total_equity_in_this_fund = equity_in_this_fund-(quotes_adquisition_ammount/currrency_sell_rate);
        }

        String total_quotes_in_this_fund_st = decimalFormat.format(total_quotes_in_this_fund);
        String total_equity_in_this_fund_st = decimalFormat.format(total_equity_in_this_fund);

        investment_fund_ref.child("quotes_circulating").setValue(total_quotes_in_this_fund_st);
        investment_fund_ref.child("fund_investment").setValue(total_equity_in_this_fund_st);

        //Aumentar el dinero de la cuenta del usuario
        if (rdPenAccount.isChecked())
        {
            double money_in_my_pen_account = Double.parseDouble(basic_account_pen);

            double current_value_in_my_account_pen = money_in_my_pen_account+total_ammount_to_invest;
            String current_value_in_my_account_pen_st = decimalFormat.format(current_value_in_my_account_pen);

            userRef.child(currentUserID).child("basic_account_pen").setValue(current_value_in_my_account_pen_st);

            //Update Finance Statements:
            double sucription_fee_updated = investment_fund_sucription_pen_db+suscription_quotes_fee_ammount;
            String sucription_fee_updated_st = decimalFormat.format(sucription_fee_updated);
            oliverBankRef.child("Investment Fund Rescue PEN").setValue(sucription_fee_updated_st);
        }
        if (rdUsdAccount.isChecked())
        {
            double money_in_my_usd_account = Double.parseDouble(basic_account_usd);

            double current_value_in_my_account_usd = money_in_my_usd_account+total_ammount_to_invest;
            String current_value_in_my_account_usd_st = decimalFormat.format(current_value_in_my_account_usd);
            userRef.child(currentUserID).child("basic_account_usd").setValue(current_value_in_my_account_usd_st);

            //Update Finance Statements:
            double sucription_fee_updated = investment_fund_sucription_usd_db+suscription_quotes_fee_ammount;
            String sucription_fee_updated_st = decimalFormat.format(sucription_fee_updated);
            oliverBankRef.child("Investment Fund Rescue USD").setValue(sucription_fee_updated_st);
        }
    }
}
