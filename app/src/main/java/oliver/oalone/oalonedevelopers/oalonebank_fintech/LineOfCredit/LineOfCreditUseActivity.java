package oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.xw.repo.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUseCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUsePenActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class LineOfCreditUseActivity extends AppCompatActivity {

    TextView txtCreditLineUsedPen,txtCreditLineAvaiilablePen,txtCreditLineTotalPen,txtCreditLineTcea,txtCurrencyRate,txtDesgravamenInsurrance,txtQuoteAmmount,txtMonths,txtRealAmmount,
            txtDailyDefaulterRate;
    BubbleSeekBar seekBarPen;
    EditText edtAmount;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,ratesRef,myOperationsRef,imageResourses,oliverBankRef;
    String currentUserID,credit_line_pen_total,credit_line_pen_used,credit_line_pen_available,credit_line_pen_tcea,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy,
            desgravamen_insurance,credit_line_start_day_payment,credit_line_end_day_payment,quote_amount_st,start_date,end_date,my_amount_st,my_amount_usd_st,capital_amount_st,monthly_interes_st,
            defaulter_credit_pen_daily_rate,credit_line_image,amount_df,issued_capital_amount,current_capital_amount,total_future_value_amount,current_future_value_amount,total_interest_amount_accumulated,current_interest_amount,
            rate_accumulated,transactions_number,number_of_quotes;
    double credit_line_total_pen_double,credit_line__used_pen_double,credit_line_available_pen_double, usd_amount,monthly_interes,issued_capital_amount_db,current_capital_amount_db,total_future_value_amount_db,current_future_value_amount_db,
            total_interest_amount_accumulated_db,current_interest_amount_db,rate_accumulated_db,transactions_number_db,number_of_quotes_db;
    RecyclerView recyclerView;
    ArrayList<LineOfCreditSimulationModel> list;
    RadioButton rdPenAccount,rdUsdAccount;
    Button btnQuotesNumber,btnSendCredit;
    double amount_double,available_pen,tcea_rate_double,tcem_rate_double,quote_amount,currency_sell,capital_amount,credit_line_pen_used_db,credit_line_pen_available_db,basic_account_pen_db,
            basic_account_usd_db;
    RelativeLayout rootLayout;
    ArrayList<String> quotes =new ArrayList<>();
    SpinnerDialog spinnerQuotes;
    int quotes_number,current_year,current_month,start_day,end_day;
    DecimalFormat decimalFormat;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_of_credit_use);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

        txtCreditLineUsedPen= findViewById(R.id.txtCreditLineUsedPen);
        txtCreditLineAvaiilablePen = findViewById(R.id.txtCreditLineAvaiilablePen);
        txtCreditLineTotalPen= findViewById(R.id.txtCreditLineTotalPen);
        seekBarPen = findViewById(R.id.seekBarPen);
        txtCreditLineTcea = findViewById(R.id.txtCreditLineTcea);
        recyclerView = findViewById(R.id.recyclerView);
        edtAmount = findViewById(R.id.edtAmount);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        btnQuotesNumber = findViewById(R.id.btnQuotesNumber);
        rootLayout = findViewById(R.id.rootLayout);
        txtDesgravamenInsurrance = findViewById(R.id.txtDesgravamenInsurrance);
        txtQuoteAmmount = findViewById(R.id.txtQuoteAmmount);
        txtMonths = findViewById(R.id.txtMonths);
        txtRealAmmount = findViewById(R.id.txtRealAmmount);
        btnSendCredit = findViewById(R.id.btnSendCredit);
        txtDailyDefaulterRate = findViewById(R.id.txtDailyDefaulterRate);
        decimalFormat = new DecimalFormat("0.00");
        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                credit_line_pen_total = dataSnapshot.child("credit_line_pen_total").getValue().toString();
                credit_line_pen_used= dataSnapshot.child("credit_line_pen_used").getValue().toString();
                credit_line_pen_available = dataSnapshot.child("credit_line_pen_available").getValue().toString();
                credit_line_pen_tcea = dataSnapshot.child("credit_line_pen_tcea").getValue().toString();
                basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                credit_line_total_pen_double = Double.parseDouble(credit_line_pen_total);
                credit_line__used_pen_double = Double.parseDouble(credit_line_pen_used);
                credit_line_available_pen_double = Double.parseDouble(credit_line_pen_available);

                credit_line_available_pen_double = credit_line_total_pen_double-credit_line__used_pen_double;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String available_pen = decimalFormat.format(credit_line_available_pen_double);

                HashMap userMap = new HashMap();
                userMap.put("credit_line_pen_available",available_pen);
                userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        txtCreditLineTotalPen.setText("Línea Aprobada: S/ "+credit_line_pen_total);
                        txtCreditLineUsedPen.setText("S/"+credit_line_pen_used);
                        txtCreditLineAvaiilablePen.setText("S/"+credit_line_pen_available);

                        seekBarPen.getConfigBuilder()
                                .min(0)
                                .max((float) credit_line_total_pen_double)
                                .progress((float) credit_line__used_pen_double)
                                .build();

                        rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+basic_account_pen);
                        rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+basic_account_usd);

                        txtCreditLineTcea.setText("Tasa de Costo Efectivo Anual: "+credit_line_pen_tcea+"%");

                        ratesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                                currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                                desgravamen_insurance = dataSnapshot.child("desgravamen_insurance").getValue().toString();
                                credit_line_start_day_payment = dataSnapshot.child("credit_line_start_day_payment").getValue().toString();
                                credit_line_end_day_payment = dataSnapshot.child("credit_line_end_day_payment").getValue().toString();
                                defaulter_credit_pen_daily_rate = dataSnapshot.child("defaulter_credit_pen_daily_rate").getValue().toString();

                                txtCurrencyRate.setText("Tipo de cambio: Compra: S/"+currency_rate_buy+" Venta: S/"+currency_rate_sell);

                                oliverBankRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            //Total Issued Capital Accumulated Amount (Without Interest) (This will be only summed)
                                            issued_capital_amount = dataSnapshot.child("Line Of Credit Issued Capital Amount PEN").getValue().toString();
                                            //Total Issued Capital Accumulated Amount (Without Interest) (This will be summed and then rested when user pay the debt)
                                            current_capital_amount = dataSnapshot.child("Line Of Credit Current Capital Amount PEN").getValue().toString();
                                            //Total Future Value Expected to Return (This will be only summed)
                                            total_future_value_amount = dataSnapshot.child("Line Of Credit Total Future Value Amount PEN").getValue().toString();
                                            //Current Future Value Expected to Return (This will be summed and then rested when the user pay the debt)
                                            current_future_value_amount = dataSnapshot.child("Line Of Credit Current Future Value Amount PEN").getValue().toString();
                                            //Total Interest Amount Accumulated (This will be only summed)
                                            total_interest_amount_accumulated = dataSnapshot.child("Line Of Credit Total Interest Amount PEN").getValue().toString();
                                            //Current Interest Amount (This will be summed and then rested when the user pay the debt)
                                            current_interest_amount = dataSnapshot.child("Line Of Credit Current Interest Amount PEN").getValue().toString();
                                            //Total Rates Accumulated in %
                                            rate_accumulated = dataSnapshot.child("Line Of Credit Rate Accumulated PEN").getValue().toString();
                                            //Total number of transactions
                                            transactions_number = dataSnapshot.child("Line Of Credit Transactions Number PEN").getValue().toString();
                                            //Number of quotes accumulated
                                            number_of_quotes = dataSnapshot.child("Line Of Credit Number of Quotes PEN").getValue().toString();

                                            issued_capital_amount_db = Double.parseDouble(issued_capital_amount);
                                            total_future_value_amount_db = Double.parseDouble(total_future_value_amount);
                                            current_future_value_amount_db = Double.parseDouble(current_future_value_amount);
                                            total_interest_amount_accumulated_db = Double.parseDouble(total_interest_amount_accumulated);
                                            current_interest_amount_db = Double.parseDouble(current_interest_amount);
                                            rate_accumulated_db = Double.parseDouble(rate_accumulated);
                                            transactions_number_db = Double.parseDouble(transactions_number);
                                            number_of_quotes_db = Double.parseDouble(number_of_quotes);
                                            current_capital_amount_db = Double.parseDouble(current_capital_amount);

                                            imageResourses.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists())
                                                    {
                                                        credit_line_image = dataSnapshot.child("credit_line_image").getValue().toString();
                                                        loadingBar.dismiss();
                                                        getDataDate();
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

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        quotes.add("1");quotes.add("2");quotes.add("3");quotes.add("4");quotes.add("5");quotes.add("6");quotes.add("7");quotes.add("8");quotes.add("9");quotes.add("10");
        quotes.add("11");quotes.add("12");quotes.add("13");quotes.add("14");quotes.add("15");quotes.add("16");quotes.add("17");quotes.add("18");quotes.add("19");quotes.add("20");
        quotes.add("21");quotes.add("22");quotes.add("23");quotes.add("24");quotes.add("25");quotes.add("26");quotes.add("27");quotes.add("28");quotes.add("29");quotes.add("30");
        quotes.add("31");quotes.add("32");quotes.add("33");quotes.add("34");quotes.add("35");quotes.add("36");quotes.add("37");quotes.add("38");quotes.add("39");quotes.add("40");
        quotes.add("41");quotes.add("42");quotes.add("43");quotes.add("44");quotes.add("45");quotes.add("46");quotes.add("47");quotes.add("48");

        btnQuotesNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtAmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Ingresa el monto primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                amount_double = Double.parseDouble(edtAmount.getText().toString());
                available_pen = Double.parseDouble(credit_line_pen_available);
                if (amount_double > credit_line_available_pen_double)
                {
                    Snackbar.make(rootLayout, "Sólo tienes un saldo disponible de S/"+available_pen, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    rdPenAccount.setBackgroundResource(R.drawable.button3_background);
                    rdPenAccount.setTextColor(Color.WHITE);
                    rdUsdAccount.setBackgroundResource(R.drawable.button3_background);
                    rdUsdAccount.setTextColor(Color.WHITE);
                    Snackbar.make(rootLayout, "Selecciona una cuenta primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    rdPenAccount.setBackgroundResource(R.drawable.button3_background);
                    rdPenAccount.setTextColor(Color.WHITE);
                    rdUsdAccount.setBackgroundResource(R.drawable.button3_background);
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

        spinnerQuotes = new SpinnerDialog(LineOfCreditUseActivity.this,quotes,"Selecciona el número de cuotas");
        spinnerQuotes.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                quotes_number = Integer.parseInt(item);

                btnQuotesNumber.setText(item);
                rdPenAccount.setEnabled(false);
                rdUsdAccount.setEnabled(false);
                edtAmount.setEnabled(false);

                simulation();

            }
        });

        btnSendCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtAmount.getText().toString())) {
                    Snackbar.make(rootLayout, "Ingresa el monto primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                amount_double = Double.parseDouble(edtAmount.getText().toString());
                available_pen = Double.parseDouble(credit_line_pen_available);
                if (amount_double > credit_line_available_pen_double) {
                    Snackbar.make(rootLayout, "Sólo tienes un saldo disponible de S/" + available_pen, Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked()) {
                    rdPenAccount.setBackgroundResource(R.drawable.button3_background);
                    rdPenAccount.setTextColor(Color.WHITE);
                    rdUsdAccount.setBackgroundResource(R.drawable.button3_background);
                    rdUsdAccount.setTextColor(Color.WHITE);
                    Snackbar.make(rootLayout, "Selecciona una cuenta primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnQuotesNumber.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar el número de cuotas...", Snackbar.LENGTH_LONG).show();
                    return;

                }
                if (TextUtils.isEmpty(btnQuotesNumber.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar el número de cuotas...", Snackbar.LENGTH_LONG).show();
                    return;

                } else {

                    loadingBar.setTitle("Preparando tu crédito...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    btnQuotesNumber.setEnabled(false);
                    generateBills();
                    changeValuesAtLineOfCredit();
                }
            }
        });


    }

    private void changeValuesAtLineOfCredit() {
        credit_line_pen_used_db = Double.parseDouble(credit_line_pen_used);
        credit_line_pen_available_db = Double.parseDouble(credit_line_pen_available);

        double new_credit_line_use = credit_line_pen_used_db+amount_double;
        double new_credit_line_available = credit_line_pen_available_db-amount_double;

        String new_credit_line_use_st = decimalFormat.format(new_credit_line_use);
        String new_credit_line_available_st = decimalFormat.format(new_credit_line_available);

        userRef.child(currentUserID).child("credit_line_pen_used").setValue(new_credit_line_use_st);

        sendMoneyToUser();
    }

    private void sendMoneyToUser() {
        if (rdPenAccount.isChecked()) {
            my_amount_st = decimalFormat.format(amount_double);

            basic_account_pen_db = Double.parseDouble(basic_account_pen);

            double new_amount_on_my_acount = basic_account_pen_db+amount_double;

            String new_amount_st = decimalFormat.format(new_amount_on_my_acount);

            userRef.child(currentUserID).child("basic_account_pen").setValue(new_amount_st);
        }
        if (rdUsdAccount.isChecked()) {
            currency_sell = Double.parseDouble(currency_rate_sell);
            usd_amount = amount_double/currency_sell;
            my_amount_usd_st = decimalFormat.format(usd_amount);

            basic_account_usd_db = Double.parseDouble(basic_account_usd);

            double new_amount_on_my_acount = basic_account_usd_db+usd_amount;

            String new_amount_st = decimalFormat.format(new_amount_on_my_acount);

            userRef.child(currentUserID).child("basic_account_usd").setValue(new_amount_st);
        }

        updateFinancialState();
        registerOperation();
    }

    private void updateFinancialState() {
        double total_future_value_amount = (amount_double*(Math.pow((1+tcem_rate_double),quotes_number)));
        double total_interest_amount = total_future_value_amount-amount_double;

        double new_issued_capital_amount = issued_capital_amount_db+amount_double;
        double new_current_capital_amount = current_capital_amount_db+amount_double;
        double new_total_future_value_amount = total_future_value_amount_db+total_future_value_amount;
        double new_current_future_value_amount = current_future_value_amount_db+total_future_value_amount;
        double new_total_interest_amount_accumulated = total_interest_amount_accumulated_db+total_interest_amount;
        double new_current_interest_amount = current_interest_amount_db+total_interest_amount;
        double new_rate_accumulated = rate_accumulated_db+(tcem_rate_double);
        double new_transactions_number = transactions_number_db+1;
        double new_number_of_quotes = number_of_quotes_db+quotes_number;

        String new_issued_capital_amount_st = decimalFormat.format(new_issued_capital_amount);
        String new_current_capital_amount_st = decimalFormat.format(new_current_capital_amount);
        String new_total_future_value_amount_st = decimalFormat.format(new_total_future_value_amount);
        String new_current_future_value_amount_st = decimalFormat.format(new_current_future_value_amount);
        String new_total_interest_amount_accumulated_st = decimalFormat.format(new_total_interest_amount_accumulated);
        String new_current_interest_amount_st = decimalFormat.format(new_current_interest_amount);
        String new_rate_accumulated_st = decimalFormat.format(new_rate_accumulated);
        String new_transactions_number_st = decimalFormat.format(new_transactions_number);
        String new_number_of_quotes_st = decimalFormat.format(new_number_of_quotes);

        oliverBankRef.child("Line Of Credit Issued Capital Amount PEN").setValue(new_issued_capital_amount_st);
        oliverBankRef.child("Line Of Credit Current Capital Amount PEN").setValue(new_current_capital_amount_st);
        oliverBankRef.child("Line Of Credit Total Future Value Amount PEN").setValue(new_total_future_value_amount_st);
        oliverBankRef.child("Line Of Credit Current Future Value Amount PEN").setValue(new_current_future_value_amount_st);
        oliverBankRef.child("Line Of Credit Total Interest Amount PEN").setValue(new_total_interest_amount_accumulated_st);
        oliverBankRef.child("Line Of Credit Current Interest Amount PEN").setValue(new_current_interest_amount_st);
        oliverBankRef.child("Line Of Credit Rate Accumulated PEN").setValue(new_rate_accumulated_st);
        oliverBankRef.child("Line Of Credit Transactions Number PEN").setValue(new_transactions_number_st);
        oliverBankRef.child("Line Of Credit Number of Quotes PEN").setValue(new_number_of_quotes_st);

    }

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime =currentTime.format(calForTime.getTime());

        final String operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Uso de línea de crédito");
        myOperationMap.put("operation_type_code","CL");
        myOperationMap.put("fund_name","");
        myOperationMap.put("fund_image","");
        myOperationMap.put("fund_currency","");
        myOperationMap.put("fund_quote_value","");
        myOperationMap.put("fund_quotes_bought","");
        myOperationMap.put("fund_my_investment","");
        myOperationMap.put("fund_suscription_fee","");
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        myOperationMap.put("fund_transaction_currency","");
        myOperationMap.put("uid",currentUserID);
        myOperationMap.put("operation_image",credit_line_image);
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
        //Credit Line
        amount_df = decimalFormat.format(amount_double);
        myOperationMap.put("credit_request_ammount",amount_df);
        myOperationMap.put("credit_request_ammount_currency","PEN");
        myOperationMap.put("credit_quotes",btnQuotesNumber.getText().toString());
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent = new Intent(LineOfCreditUseActivity.this, CreditLineUseCompletedActivity.class);
                intent.putExtra("TransactionCode",operationRandomName+"CL");
                intent.putExtra("ammount", edtAmount.getText().toString());
                intent.putExtra("currency", "PEN");
                loadingBar.dismiss();
                startActivity(intent);
                finish();
            }
        });
    }

    private void generateBills() {
        btnSendCredit.setEnabled(false);
        btnSendCredit.setText("Cargando crédito...");
        btnSendCredit.setBackgroundResource(R.drawable.edit_text_background1);

        current_year = Calendar.getInstance().get(Calendar.YEAR);
        current_month = Calendar.getInstance().get(Calendar.MONTH)+1;
        start_day = Integer.parseInt(credit_line_start_day_payment);
        end_day = Integer.parseInt(credit_line_end_day_payment);

        capital_amount = amount_double/quotes_number;
        capital_amount_st = decimalFormat.format(capital_amount);

        monthly_interes = (amount_double*(Math.pow((1+tcem_rate_double),quotes_number))-amount_double)/quotes_number;
        monthly_interes_st = decimalFormat.format(monthly_interes);

        for (int i = 1; i <= quotes_number; i++) {

            userRef.child(currentUserID).child("Line Of Credit Bills").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    amount_double = Double.parseDouble(edtAmount.getText().toString());

                    quote_amount = (amount_double*(Math.pow((1+tcem_rate_double),quotes_number))/quotes_number);
                    quote_amount_st = decimalFormat.format(quote_amount);
                    capital_amount = amount_double/quotes_number;
                    capital_amount_st = decimalFormat.format(capital_amount);

                    monthly_interes = (amount_double*(Math.pow((1+tcem_rate_double),quotes_number))-amount_double)/quotes_number;
                    monthly_interes_st = decimalFormat.format(monthly_interes);

                    if (current_month >= 12) {
                        current_month = current_month - 13;
                        current_month = current_month+1;
                        current_year = current_year+1;
                        start_date = start_day+"/"+current_month+"/"+current_year;
                        end_date = end_day+"/"+current_month+"/"+current_year;
                    }
                    if (current_month < 12) {
                        current_month = current_month+1;
                        start_date = start_day+"/"+current_month+"/"+current_year;
                        end_date = end_day+"/"+current_month+"/"+current_year;
                    }

                    if (dataSnapshot.hasChild(current_year+""+current_month)) {

                        String last_quote_amount = dataSnapshot.child(current_year+""+current_month).child("quote_amount").getValue().toString();
                        String last_capital_amount = dataSnapshot.child(current_year+""+current_month).child("capital_amount").getValue().toString();
                        String last_monthly_interest = dataSnapshot.child(current_year+""+current_month).child("monthly_interest").getValue().toString();

                        double last_quote_amount_db = Double.parseDouble(last_quote_amount);
                        double last_capital_amount_db = Double.parseDouble(last_capital_amount);
                        double last_monthly_interest_db = Double.parseDouble(last_monthly_interest);

                        double new_quote_amount = last_quote_amount_db+quote_amount;
                        double new_capital_amount =last_capital_amount_db+capital_amount;
                        double new_monthly_interest = last_monthly_interest_db+monthly_interes;

                        quote_amount_st = decimalFormat.format(new_quote_amount);
                        capital_amount_st = decimalFormat.format(new_capital_amount);
                        monthly_interes_st = decimalFormat.format(new_monthly_interest);

                        userRef.child(currentUserID).child("Line Of Credit Bills").child(current_year+""+current_month).child("quote_amount").setValue(quote_amount_st);
                        userRef.child(currentUserID).child("Line Of Credit Bills").child(current_year+""+current_month).child("fixed_quote").setValue(quote_amount_st);
                        userRef.child(currentUserID).child("Line Of Credit Bills").child(current_year+""+current_month).child("capital_amount").setValue(capital_amount_st);
                        userRef.child(currentUserID).child("Line Of Credit Bills").child(current_year+""+current_month).child("monthly_interest").setValue(monthly_interes_st);


                    } else if (!dataSnapshot.hasChild(current_year+""+current_month)) {

                        amount_double = Double.parseDouble(edtAmount.getText().toString());

                        quote_amount = (amount_double*(Math.pow((1+tcem_rate_double),quotes_number))/quotes_number);
                        quote_amount_st = decimalFormat.format(quote_amount);
                        capital_amount = amount_double/quotes_number;
                        capital_amount_st = decimalFormat.format(capital_amount);

                        monthly_interes = (amount_double*(Math.pow((1+tcem_rate_double),quotes_number))-amount_double)/quotes_number;
                        monthly_interes_st = decimalFormat.format(monthly_interes);

                        HashMap hashMap = new HashMap();
                        hashMap.put("start_day",start_day+"");
                        hashMap.put("start_month",current_month+"");
                        hashMap.put("start_year",current_year+"");
                        hashMap.put("end_day",end_day+"");
                        hashMap.put("end_month",current_month+"");
                        hashMap.put("end_year",current_year+"");
                        hashMap.put("desgravamen_insurrance",desgravamen_insurance);
                        hashMap.put("debt_state","0");
                        hashMap.put("bill_code",current_year+""+current_month);
                        hashMap.put("quote_amount",quote_amount_st);
                        hashMap.put("capital_amount",capital_amount_st);
                        hashMap.put("monthly_interest",monthly_interes_st);
                        hashMap.put("debt_currency","PEN");
                        hashMap.put("visible","true");
                        hashMap.put("defaulter","false");
                        hashMap.put("fixed_quote",quote_amount_st);
                        hashMap.put("timestamp", ServerValue.TIMESTAMP);
                        userRef.child(currentUserID).child("Line Of Credit Bills").child(current_year+""+current_month).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                //Toast.makeText(LineOfCreditUseActivity.this, "NUEVOS CARGOS REGISTRADOS", Toast.LENGTH_SHORT).show();
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


    private void simulation() {

        tcea_rate_double = Double.parseDouble(credit_line_pen_tcea);
        tcem_rate_double = Math.pow(((tcea_rate_double/100)+1),(0.08333333))-1;
        amount_double = Double.parseDouble(edtAmount.getText().toString());

        quote_amount = (amount_double*(Math.pow((1+tcem_rate_double),quotes_number))/quotes_number);
        quote_amount_st = decimalFormat.format(quote_amount);

        txtDesgravamenInsurrance.setText("Seguro de Desgravamen: S/"+desgravamen_insurance+" Mensual");
        txtQuoteAmmount.setText("Cuota Mensual a Pagar: S/"+quote_amount_st);
        txtMonths.setText("Número de Cuotas: "+btnQuotesNumber.getText().toString());
        txtDailyDefaulterRate.setText("Tasa moratoria diaria: "+defaulter_credit_pen_daily_rate+"%");

        if (rdPenAccount.isChecked()) {
            my_amount_st = decimalFormat.format(amount_double);
            txtRealAmmount.setText("Monto a recibir ahora: S/"+my_amount_st+" PEN");
        }
        if (rdUsdAccount.isChecked()) {
            currency_sell = Double.parseDouble(currency_rate_sell);
            usd_amount = amount_double/currency_sell;
            my_amount_usd_st = decimalFormat.format(usd_amount);
            txtRealAmmount.setText("Monto a recibir ahora: $"+my_amount_usd_st+" USD");

        }

        list = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        LineOfCreditAdapter lineOfCreditAdapter = new LineOfCreditAdapter(list);
        recyclerView.setAdapter(lineOfCreditAdapter);


        current_year = Calendar.getInstance().get(Calendar.YEAR);
        current_month = Calendar.getInstance().get(Calendar.MONTH)+1;
        start_day = Integer.parseInt(credit_line_start_day_payment);
        end_day = Integer.parseInt(credit_line_end_day_payment);


        for (int i = 1; i <= quotes_number; i++) {
            if (current_month >= 12) {
                current_month = current_month - 13;
                current_month = current_month+1;
                current_year = current_year+1;
                start_date = start_day+"/"+current_month+"/"+current_year;
                end_date = end_day+"/"+current_month+"/"+current_year;
            }
            if (current_month < 12) {
                current_month = current_month+1;
                start_date = start_day+"/"+current_month+"/"+current_year;
                end_date = end_day+"/"+current_month+"/"+current_year;
            }

            list.add(new LineOfCreditSimulationModel("S/"+quote_amount_st,""+start_date,""+end_date));
        }
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
