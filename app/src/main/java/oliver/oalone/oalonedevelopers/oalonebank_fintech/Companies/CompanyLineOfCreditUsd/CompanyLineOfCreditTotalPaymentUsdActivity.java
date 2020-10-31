package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompanyLineOfCreditUsd;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCreditUsd.LineOfCreditPayUsdActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCreditUsd.LineOfCreditTotalPaymentUsdActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompanyLineOfCreditTotalPaymentUsdActivity extends AppCompatActivity {

    String my_company_key;
    TextView txtTittle,txtCreditLineUsedPen,txtCreditLineAvaiilablePen,txtCreditLineTotalPen,txtTextMonth,txtCurrencyRate,txtDesgravamenInsurrance,txtAmountToPay,txtQuoteAmountToPay,txtDebtPercent,
            txtQuoteAmount;
    BubbleSeekBar seekBarPen;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,ratesRef,myOperationsRef,imageResourses,oliverBankRef;
    String currentUserID,credit_line_pen_total,credit_line_pen_used,credit_line_pen_available,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy,credit_line_image,postKey,
            start_month,start_year,quote_amount,capital_amount,monthly_interest,desgravamen_insurrance,my_amount_usd_st,total_amount_to_pay_st,issued_capital_amount,current_capital_amount,total_future_value_amount,current_future_value_amount,total_interest_amount_accumulated,current_interest_amount,
            rate_accumulated,transactions_number,number_of_quotes,ammount_df,pen_amount_to_pay_st;
    double credit_line_total_pen_double,credit_line_used_pen_double,credit_line_available_pen_double,currency_sell,usd_amount,amount_double,basic_account_pen_db,basic_account_usd_db,quote_amount_db,
            capital_amount_db,total_amount_to_pay,desgravamen_insurrance_db,issued_capital_amount_db,current_capital_amount_db,total_future_value_amount_db,current_future_value_amount_db,
            total_interest_amount_accumulated_db,current_interest_amount_db,rate_accumulated_db,transactions_number_db,number_of_quotes_db,monthly_interest_db,currency_buy,pen_amount_to_pay;
    RadioButton rdPenAccount,rdUsdAccount;
    RelativeLayout rootLayout;
    DecimalFormat decimalFormat;
    private ProgressDialog loadingBar;
    Button btnPayNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_line_of_credit_total_payment_usd);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = my_company_key;
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

        postKey = getIntent().getExtras().get("postKey").toString();

        txtCreditLineUsedPen= findViewById(R.id.txtCreditLineUsedPen);
        txtCreditLineAvaiilablePen = findViewById(R.id.txtCreditLineAvaiilablePen);
        txtCreditLineTotalPen= findViewById(R.id.txtCreditLineTotalPen);
        seekBarPen = findViewById(R.id.seekBarPen);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        rootLayout = findViewById(R.id.rootLayout);
        txtTextMonth = findViewById(R.id.txtTextMonth);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        txtDesgravamenInsurrance = findViewById(R.id.txtDesgravamenInsurrance);
        txtAmountToPay= findViewById(R.id.txtAmountToPay);
        txtQuoteAmountToPay = findViewById(R.id.txtQuoteAmountToPay);
        txtDebtPercent = findViewById(R.id.txtDebtPercent);
        txtTittle = findViewById(R.id.txtTittle);
        txtQuoteAmount = findViewById(R.id.txtQuoteAmount);
        btnPayNow = findViewById(R.id.btnPayNow);
        decimalFormat = new DecimalFormat("0.00");

        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        userRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                credit_line_pen_total = dataSnapshot.child("credit_line_usd_total").getValue().toString();
                credit_line_pen_used= dataSnapshot.child("credit_line_usd_used").getValue().toString();
                credit_line_pen_available = dataSnapshot.child("credit_line_usd_available").getValue().toString();
                basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                credit_line_total_pen_double = Double.parseDouble(credit_line_pen_total);
                credit_line_used_pen_double = Double.parseDouble(credit_line_pen_used);
                credit_line_available_pen_double = Double.parseDouble(credit_line_pen_available);

                credit_line_available_pen_double = credit_line_total_pen_double-credit_line_used_pen_double;
                final DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String available_pen = decimalFormat.format(credit_line_available_pen_double);

                HashMap userMap = new HashMap();
                userMap.put("credit_line_pen_available",available_pen);
                userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        txtCreditLineTotalPen.setText("Línea Aprobada: $"+credit_line_pen_total);
                        txtCreditLineUsedPen.setText("$"+credit_line_pen_used);
                        txtCreditLineAvaiilablePen.setText("$"+credit_line_pen_available);

                        seekBarPen.getConfigBuilder()
                                .min(0)
                                .max((float) credit_line_total_pen_double)
                                .progress((float) credit_line_used_pen_double)
                                .build();

                        rdPenAccount.setText("Cuenta corriente (Soles - PEN): S/ "+basic_account_pen);
                        rdUsdAccount.setText("Cuenta corriente (Dólares - USD): $ "+basic_account_usd);
                        ratesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                                currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();

                                txtCurrencyRate.setText("Tipo de cambio: Compra: S/"+currency_rate_buy+" Venta: S/"+currency_rate_sell);

                                imageResourses.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            credit_line_image = dataSnapshot.child("credit_line_image").getValue().toString();

                                            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    start_month = dataSnapshot.child("start_month").getValue().toString();
                                                    start_year = dataSnapshot.child("start_year").getValue().toString();
                                                    quote_amount = dataSnapshot.child("quote_amount").getValue().toString();
                                                    capital_amount = dataSnapshot.child("capital_amount").getValue().toString();
                                                    monthly_interest = dataSnapshot.child("monthly_interest").getValue().toString();
                                                    desgravamen_insurrance = dataSnapshot.child("desgravamen_insurrance").getValue().toString();

                                                    capital_amount_db = Double.parseDouble(capital_amount);
                                                    quote_amount_db = Double.parseDouble(quote_amount);
                                                    desgravamen_insurrance_db = Double.parseDouble(desgravamen_insurrance);
                                                    monthly_interest_db = Double.parseDouble(monthly_interest);

                                                    total_amount_to_pay = quote_amount_db+desgravamen_insurrance_db;
                                                    total_amount_to_pay_st = decimalFormat.format(total_amount_to_pay);



                                                    if (start_month.equals("1")) {
                                                        txtTittle.setText("Pago del mes: Enero "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Enero "+start_year);
                                                    }
                                                    if (start_month.equals("2")) {
                                                        txtTittle.setText("Pago del mes: Febrero "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Febrero "+start_year);
                                                    }
                                                    if (start_month.equals("3")) {
                                                        txtTittle.setText("Pago del mes: Marzo "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Marzo "+start_year);
                                                    }
                                                    if (start_month.equals("4")) {
                                                        txtTittle.setText("Pago del mes: Abril "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Abril "+start_year);
                                                    }
                                                    if (start_month.equals("5")) {
                                                        txtTittle.setText("Pago del mes: Mayo "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Mayo "+start_year);
                                                    }
                                                    if (start_month.equals("6")) {
                                                        txtTittle.setText("Pago del mes: Junio "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Junio "+start_year);
                                                    }
                                                    if (start_month.equals("7")) {
                                                        txtTittle.setText("Pago del mes: Julio "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Julio "+start_year);
                                                    }
                                                    if (start_month.equals("8")) {
                                                        txtTittle.setText("Pago del mes: Agosto "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Agosto "+start_year);
                                                    }
                                                    if (start_month.equals("9")) {
                                                        txtTittle.setText("Pago del mes: Septiembre "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Septiembre "+start_year);
                                                    }
                                                    if (start_month.equals("10")) {
                                                        txtTittle.setText("Pago del mes: Ocutbre "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Octubre "+start_year);
                                                    }
                                                    if (start_month.equals("11")) {
                                                        txtTittle.setText("Pago del mes: Noviembre "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Noviembre "+start_year);
                                                    }
                                                    if (start_month.equals("12")) {
                                                        txtTittle.setText("Pago del mes: Diciembre "+start_year);
                                                        txtTextMonth.setText("Pago del mes: Diciembre "+start_year);
                                                    }

                                                    txtQuoteAmount.setText("$"+quote_amount);
                                                    amount_double = Double.parseDouble(quote_amount);

                                                    rdUsdAccount.setChecked(true);

                                                    txtDesgravamenInsurrance.setText("Seguro de desgravamen: $"+desgravamen_insurrance);
                                                    txtAmountToPay.setText("Monto a pagar: $"+total_amount_to_pay_st+" USD");
                                                    txtQuoteAmountToPay.setText("Deuda a pagar: $"+quote_amount);
                                                    txtDebtPercent.setText("% de Deuda a pagar: 100%");

                                                    oliverBankRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                //Total Issued Capital Accumulated Amount (Without Interest) (This will be only summed)
                                                                issued_capital_amount = dataSnapshot.child("Company Line Of Credit Issued Capital Amount USD").getValue().toString();
                                                                //Total Issued Capital Accumulated Amount (Without Interest) (This will be summed and then rested when user pay the debt)
                                                                current_capital_amount = dataSnapshot.child("Company Line Of Credit Current Capital Amount USD").getValue().toString();
                                                                //Total Future Value Expected to Return (This will be only summed)
                                                                total_future_value_amount = dataSnapshot.child("Company Line Of Credit Total Future Value Amount USD").getValue().toString();
                                                                //Current Future Value Expected to Return (This will be summed and then rested when the user pay the debt)
                                                                current_future_value_amount = dataSnapshot.child("Company Line Of Credit Current Future Value Amount USD").getValue().toString();
                                                                //Total Interest Amount Accumulated (This will be only summed)
                                                                total_interest_amount_accumulated = dataSnapshot.child("Company Line Of Credit Total Interest Amount USD").getValue().toString();
                                                                //Current Interest Amount (This will be summed and then rested when the user pay the debt)
                                                                current_interest_amount = dataSnapshot.child("Company Line Of Credit Current Interest Amount USD").getValue().toString();
                                                                //Total Rates Accumulated in %
                                                                rate_accumulated = dataSnapshot.child("Company Line Of Credit Rate Accumulated USD").getValue().toString();
                                                                //Total number of transactions
                                                                transactions_number = dataSnapshot.child("Company Line Of Credit Transactions Number USD").getValue().toString();
                                                                //Number of quotes accumulated
                                                                number_of_quotes = dataSnapshot.child("Company Line Of Credit Number of Quotes USD").getValue().toString();

                                                                issued_capital_amount_db = Double.parseDouble(issued_capital_amount);
                                                                total_future_value_amount_db = Double.parseDouble(total_future_value_amount);
                                                                current_future_value_amount_db = Double.parseDouble(current_future_value_amount);
                                                                total_interest_amount_accumulated_db = Double.parseDouble(total_interest_amount_accumulated);
                                                                current_interest_amount_db = Double.parseDouble(current_interest_amount);
                                                                rate_accumulated_db = Double.parseDouble(rate_accumulated);
                                                                transactions_number_db = Double.parseDouble(transactions_number);
                                                                number_of_quotes_db = Double.parseDouble(number_of_quotes);
                                                                current_capital_amount_db = Double.parseDouble(current_capital_amount);

                                                                loadingBar.dismiss();
                                                                getDataDate();


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

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currency_buy = Double.parseDouble(currency_rate_buy);
                pen_amount_to_pay = total_amount_to_pay*currency_buy;
                pen_amount_to_pay_st = decimalFormat.format(pen_amount_to_pay);

                txtAmountToPay.setText("Monto a pagar: S/"+pen_amount_to_pay_st+" PEN");
            }
        });

        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currency_sell = Double.parseDouble(currency_rate_sell);

                usd_amount = total_amount_to_pay;
                my_amount_usd_st = decimalFormat.format(usd_amount);
                txtAmountToPay.setText("Monto a pagar: $"+my_amount_usd_st+" USD");
            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basic_account_pen_db = Double.parseDouble(basic_account_pen);
                basic_account_usd_db = Double.parseDouble(basic_account_usd);
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked()) {
                    Snackbar.make(rootLayout, "Debes seleccionar una cuenta para pagar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (rdPenAccount.isChecked()) {
                    if (basic_account_pen_db < pen_amount_to_pay) {
                        Snackbar.make(findViewById(android.R.id.content), "No cuentas con dinero suficiente para realizar esta operación", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        doThePayment();
                        updateFinancialState();
                        registerOperation();
                    }
                }
                if (rdUsdAccount.isChecked()) {
                    if (basic_account_usd_db < total_amount_to_pay) {
                        Snackbar.make(findViewById(android.R.id.content), "No cuentas con dinero suficiente para realizar esta operación", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (basic_account_usd_db < total_amount_to_pay) {
                        Snackbar.make(findViewById(android.R.id.content), "No cuentas con dinero suficiente para realizar esta operación", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        doThePayment();
                        updateFinancialState();
                        registerOperation();
                    }
                }
            }
        });
    }

    private void updateFinancialState() {
        double new_current_capital_amount = current_capital_amount_db-capital_amount_db;
        double new_current_future_value_amount = current_future_value_amount_db-quote_amount_db;
        double new_current_interest_amount = current_interest_amount_db-monthly_interest_db;

        String new_current_capital_amount_st = decimalFormat.format(new_current_capital_amount);
        String new_current_future_value_amount_st = decimalFormat.format(new_current_future_value_amount);
        String new_current_interest_amount_st = decimalFormat.format(new_current_interest_amount);

        oliverBankRef.child("Company Line Of Credit Current Capital Amount USD").setValue(new_current_capital_amount_st);
        oliverBankRef.child("Company Line Of Credit Current Future Value Amount USD").setValue(new_current_future_value_amount_st);
        oliverBankRef.child("Company Line Of Credit Current Interest Amount USD").setValue(new_current_interest_amount_st);

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
        myOperationMap.put("operation_type","Pago de cuota en línea de crédito");
        myOperationMap.put("operation_type_code","PC");
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
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        //Credit Line Payment
        ammount_df = decimalFormat.format(total_amount_to_pay);
        myOperationMap.put("credit_payment_ammount",ammount_df);
        myOperationMap.put("credit_payment_ammount_currency","USD");
        myOperationMap.put("credit_month",""+start_month);
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent = new Intent(CompanyLineOfCreditTotalPaymentUsdActivity.this, CompanyLineOfCreditPayUsdActivity.class);
                loadingBar.dismiss();
                Toasty.success(CompanyLineOfCreditTotalPaymentUsdActivity.this, "PAGO EXITOSO!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });
    }

    private void doThePayment() {
        btnPayNow.setEnabled(false);
        btnPayNow.setText("Pagando...");
        btnPayNow.setBackgroundResource(R.drawable.edit_text_background1);

        if (rdPenAccount.isChecked()) {
            double new_my_acount = basic_account_pen_db-pen_amount_to_pay;
            double new_credit_line_used = credit_line_used_pen_double-capital_amount_db;

            String new_my_acount_st = decimalFormat.format(new_my_acount);
            String new_credit_line_used_st = decimalFormat.format(new_credit_line_used);

            userRef.child(currentUserID).child("current_account_pen").setValue(new_my_acount_st);
            userRef.child(currentUserID).child("credit_line_usd_used").setValue(new_credit_line_used_st);
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("quote_amount").setValue("0.00");
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("capital_amount").setValue("0.00");
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("monthly_interest").setValue("0.00");
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("debt_state").setValue("1");
        }
        if (rdUsdAccount.isChecked()) {
            double new_my_acount = basic_account_usd_db-total_amount_to_pay;
            double new_credit_line_used = credit_line_used_pen_double-capital_amount_db;

            String new_my_acount_st = decimalFormat.format(new_my_acount);
            String new_credit_line_used_st = decimalFormat.format(new_credit_line_used);

            userRef.child(currentUserID).child("current_account_usd").setValue(new_my_acount_st);
            userRef.child(currentUserID).child("credit_line_usd_used").setValue(new_credit_line_used_st);
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("quote_amount").setValue("0.00");
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("capital_amount").setValue("0.00");
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("monthly_interest").setValue("0.00");
            userRef.child(currentUserID).child("Company Line Of Credit Bills USD").child(postKey).child("debt_state").setValue("1");
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
