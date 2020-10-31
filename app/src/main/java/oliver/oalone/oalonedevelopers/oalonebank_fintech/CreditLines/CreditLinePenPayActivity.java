package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import me.anwarshahriar.calligrapher.Calligrapher;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CreditLinePenPayActivity extends AppCompatActivity  {

    TextView txtTittle,txtCreditLinePaymentMonthPen,txtCurrencyRate,txtMonth,txtxDebt,txtRealAmmount,txtLastDebt;
    RadioButton rdPenAccount,rdUsdAccount;
    RelativeLayout rootLayout;
    EditText edtAmmount;
    Button btnPayCreditPen;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,ratesRef,myOperationsRef,imageResourses,oliverBankRef;
    String currentUserID,basic_account_pen,basic_account_usd,credit_line_pen_tcea,credit_line_pen,credit_line_usd,currency_rate_sell,currency_rate_buy,account_currency,
            defaulter_credit_pen_daily_rate,credit_line_pen_payment_month1,
            credit_line_pen_payment_month2,credit_line_pen_payment_month3,credit_line_pen_payment_month4,credit_line_pen_payment_month5,credit_line_pen_payment_month6,
            credit_line_pen_payment_month7,credit_line_pen_payment_month8,credit_line_pen_payment_month9,credit_line_pen_payment_month10,credit_line_pen_payment_month11,
            credit_line_pen_payment_month12,credit_line_start_day_payment,credit_line_end_day_payment,new_payment_st,quotes_acomulated_st,
            credit_line_pen_default_day1,credit_line_pen_default_day2,credit_line_pen_default_day3,credit_line_pen_default_day4,credit_line_pen_default_day5,
            credit_line_pen_default_day6,credit_line_pen_default_day7,credit_line_pen_default_day8,credit_line_pen_default_day9,credit_line_pen_default_day10,
            credit_line_pen_default_day11,credit_line_pen_default_day12,credit_line_pen_default_day13,saveCurrentDate,saveCurrentTime,operationRandomName,credit_line_image,
            credit_line_pen_total,credit_line_pen_used,credit_line_pen_available,postKey,my_pin,credit_line_to_chage_now;
    double ammount, last_debt,my_pen_account_double,money_account_pen,credit_line_total_pen_double,credit_line__used_pen_double,credit_line_available_pen_double;
    int month,day,current_year;
    double credit_month_double1,credit_month_double2,credit_month_double3,credit_month_double4,credit_month_double5,credit_month_double6,credit_month_double7,
            credit_month_double8,credit_month_double9,credit_month_double10,credit_month_double11,credit_month_double12,defaulter_credit_pen_daily_rate_double,defaulter_rate,
            new_payment, quotes_acomulated, current_user_debt,credit_line_to_chage_now_db;
    int credit_line_pen_payment_year1,credit_line_pen_payment_year2,credit_line_pen_payment_year3,credit_line_pen_payment_year4,
            credit_line_pen_payment_year5,credit_line_pen_payment_year6,credit_line_pen_payment_year7,credit_line_pen_payment_year8,credit_line_pen_payment_year9,credit_line_pen_payment_year10,
            credit_line_pen_payment_year11,credit_line_pen_payment_year12,start_day,end_day;
    private ProgressDialog loadingBar;
    DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_line_pen_pay);

        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);
        loadingBar.show();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        txtTittle = findViewById(R.id.txtTittle);
        txtTittle.setSelected(true);
        txtCreditLinePaymentMonthPen = findViewById(R.id.txtCreditLinePaymentMonthPen);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        rootLayout = findViewById(R.id.rootLayout);
        edtAmmount= findViewById(R.id.edtAmmount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        txtMonth = findViewById(R.id.txtMonth);
        txtxDebt = findViewById(R.id.txtxDebt);
        txtRealAmmount = findViewById(R.id.txtRealAmmount);
        txtLastDebt = findViewById(R.id.txtLastDebt);
        btnPayCreditPen = findViewById(R.id.btnPayCreditPen);

        decimalFormat = new DecimalFormat("0.00");

        getDataDate();


        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Monthly Payments
                credit_line_pen_payment_month1 = dataSnapshot.child("credit_line_pen_payment_month1").getValue().toString();
                credit_line_pen_payment_month2 = dataSnapshot.child("credit_line_pen_payment_month2").getValue().toString();
                credit_line_pen_payment_month3 = dataSnapshot.child("credit_line_pen_payment_month3").getValue().toString();
                credit_line_pen_payment_month4 = dataSnapshot.child("credit_line_pen_payment_month4").getValue().toString();
                credit_line_pen_payment_month5 = dataSnapshot.child("credit_line_pen_payment_month5").getValue().toString();
                credit_line_pen_payment_month6 = dataSnapshot.child("credit_line_pen_payment_month6").getValue().toString();
                credit_line_pen_payment_month7 = dataSnapshot.child("credit_line_pen_payment_month7").getValue().toString();
                credit_line_pen_payment_month8 = dataSnapshot.child("credit_line_pen_payment_month8").getValue().toString();
                credit_line_pen_payment_month9 = dataSnapshot.child("credit_line_pen_payment_month9").getValue().toString();
                credit_line_pen_payment_month10 = dataSnapshot.child("credit_line_pen_payment_month10").getValue().toString();
                credit_line_pen_payment_month11 = dataSnapshot.child("credit_line_pen_payment_month11").getValue().toString();
                credit_line_pen_payment_month12 = dataSnapshot.child("credit_line_pen_payment_month12").getValue().toString();

                credit_line_pen_payment_year1 = dataSnapshot.child("credit_line_pen_payment_year1").getValue(Integer.class);
                credit_line_pen_payment_year2 = dataSnapshot.child("credit_line_pen_payment_year2").getValue(Integer.class);
                credit_line_pen_payment_year3 = dataSnapshot.child("credit_line_pen_payment_year3").getValue(Integer.class);
                credit_line_pen_payment_year4 = dataSnapshot.child("credit_line_pen_payment_year4").getValue(Integer.class);
                credit_line_pen_payment_year5 = dataSnapshot.child("credit_line_pen_payment_year5").getValue(Integer.class);
                credit_line_pen_payment_year6 = dataSnapshot.child("credit_line_pen_payment_year6").getValue(Integer.class);
                credit_line_pen_payment_year7 = dataSnapshot.child("credit_line_pen_payment_year7").getValue(Integer.class);
                credit_line_pen_payment_year8 = dataSnapshot.child("credit_line_pen_payment_year8").getValue(Integer.class);
                credit_line_pen_payment_year9 = dataSnapshot.child("credit_line_pen_payment_year9").getValue(Integer.class);
                credit_line_pen_payment_year10 = dataSnapshot.child("credit_line_pen_payment_year10").getValue(Integer.class);
                credit_line_pen_payment_year11 = dataSnapshot.child("credit_line_pen_payment_year11").getValue(Integer.class);
                credit_line_pen_payment_year12 = dataSnapshot.child("credit_line_pen_payment_year12").getValue(Integer.class);

                credit_line_pen_default_day1 = dataSnapshot.child("credit_line_pen_default_day1").getValue().toString();
                credit_line_pen_default_day2 = dataSnapshot.child("credit_line_pen_default_day2").getValue().toString();
                credit_line_pen_default_day3 = dataSnapshot.child("credit_line_pen_default_day3").getValue().toString();
                credit_line_pen_default_day4 = dataSnapshot.child("credit_line_pen_default_day4").getValue().toString();
                credit_line_pen_default_day5 = dataSnapshot.child("credit_line_pen_default_day5").getValue().toString();
                credit_line_pen_default_day6 = dataSnapshot.child("credit_line_pen_default_day6").getValue().toString();
                credit_line_pen_default_day7 = dataSnapshot.child("credit_line_pen_default_day7").getValue().toString();
                credit_line_pen_default_day8 = dataSnapshot.child("credit_line_pen_default_day8").getValue().toString();
                credit_line_pen_default_day9 = dataSnapshot.child("credit_line_pen_default_day9").getValue().toString();
                credit_line_pen_default_day10 = dataSnapshot.child("credit_line_pen_default_day10").getValue().toString();

                basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
                credit_line_pen_tcea = dataSnapshot.child("credit_line_pen_tcea").getValue().toString();

                credit_line_pen = dataSnapshot.child("credit_line_pen").getValue().toString();
                credit_line_usd = dataSnapshot.child("credit_line_usd").getValue().toString();

                rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+basic_account_pen);
                rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+basic_account_usd);

                money_account_pen = Double.parseDouble(basic_account_pen);

                //Credit Line (Soles)
                credit_line_pen_total = dataSnapshot.child("credit_line_pen_total").getValue().toString();
                credit_line_pen_used = dataSnapshot.child("credit_line_pen_used").getValue().toString();
                credit_line_pen_available = dataSnapshot.child("credit_line_pen_available").getValue().toString();

                credit_line_total_pen_double = Double.parseDouble(credit_line_pen_total);
                credit_line__used_pen_double = Double.parseDouble(credit_line_pen_used);
                credit_line_available_pen_double = Double.parseDouble(credit_line_pen_available);

                oliverBankRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            credit_line_to_chage_now = dataSnapshot.child("Credit Line Amount to Charge Now PEN").getValue().toString();

                            credit_line_to_chage_now_db = Double.parseDouble(credit_line_to_chage_now);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CreditLinePenPayActivity.this, "ERROR AL CARGAR", Toast.LENGTH_SHORT).show();
            }
        });

        ratesRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                    credit_line_start_day_payment = dataSnapshot.child("credit_line_start_day_payment").getValue().toString();
                    credit_line_end_day_payment = dataSnapshot.child("credit_line_end_day_payment").getValue().toString();

                    txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");

                    defaulter_credit_pen_daily_rate= dataSnapshot.child("defaulter_credit_pen_daily_rate").getValue().toString();

                    current_year = Calendar.getInstance().get(Calendar.YEAR);

                    Date date = new Date();
                    //LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    month = Calendar.getInstance().get(Calendar.MONTH)+1;
                    day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                    credit_month_double1 = Double.parseDouble(credit_line_pen_payment_month1);
                    credit_month_double2 = Double.parseDouble(credit_line_pen_payment_month2);
                    credit_month_double3 = Double.parseDouble(credit_line_pen_payment_month3);
                    credit_month_double4 = Double.parseDouble(credit_line_pen_payment_month4);
                    credit_month_double5 = Double.parseDouble(credit_line_pen_payment_month5);
                    credit_month_double6 = Double.parseDouble(credit_line_pen_payment_month6);
                    credit_month_double7 = Double.parseDouble(credit_line_pen_payment_month7);
                    credit_month_double8 = Double.parseDouble(credit_line_pen_payment_month8);
                    credit_month_double9 = Double.parseDouble(credit_line_pen_payment_month9);
                    credit_month_double10 = Double.parseDouble(credit_line_pen_payment_month10);
                    credit_month_double11 = Double.parseDouble(credit_line_pen_payment_month11);
                    credit_month_double12 = Double.parseDouble(credit_line_pen_payment_month12);

                    defaulter_credit_pen_daily_rate_double = Double.parseDouble(defaulter_credit_pen_daily_rate);


                    start_day = Integer.parseInt(credit_line_start_day_payment);
                    end_day = Integer.parseInt(credit_line_end_day_payment);


                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    HashMap hashMap = new HashMap();

                    if (month == 1)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year1 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month1);
                        }
                        if (day > end_day && credit_month_double1 > 0.00 && credit_line_pen_payment_year1 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month1);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*1;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*2;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*3;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*4;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*5;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*6;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*7;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*8;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*9;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*10;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month1",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double2;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month2",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month1","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month1);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month1);
                        }

                    }

                    if (month == 2)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year2 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month2);
                        }
                        if (day > end_day && credit_month_double2 > 0.00 && credit_line_pen_payment_year2 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month3);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*1;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*2;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*3;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*4;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*5;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*6;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*7;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*8;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*9;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*10;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month2",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double3;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month3",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month2","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month2);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month2);
                        }
                    }

                    if (month == 3)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year3 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month3);
                        }
                        if (day > end_day && credit_month_double3 > 0.00 && credit_line_pen_payment_year3 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month3);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*1;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*2;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*3;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*4;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*5;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*6;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*7;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*8;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*9;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*10;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month3",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double4;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month4",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month3","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month3);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month3);
                        }
                    }

                    if (month == 4)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year4 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month4);
                        }
                        if (day > end_day && credit_month_double4 > 0.00 && credit_line_pen_payment_year4 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month4);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*1;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*2;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*3;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*4;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*5;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*6;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*7;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*8;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*9;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*10;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month4",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double5;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month5",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month4","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month4);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month4);
                        }
                    }

                    if (month == 5)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year5 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month5);
                        }
                        if (day > end_day && credit_month_double5 > 0.00 && credit_line_pen_payment_year5 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month5);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*1;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*2;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*3;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*4;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*5;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*6;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*7;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*8;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*9;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*10;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month5",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double6;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month6",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month5","0.00");

                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month5);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month5);
                        }
                    }

                    if (month == 6)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year6 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month6);
                        }
                        if (day > end_day && credit_month_double6 > 0.00 && credit_line_pen_payment_year6 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month6);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*1;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*2;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*3;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*4;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*5;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*6;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*7;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*8;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*9;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*10;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month6",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double7;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month7",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month6","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month6);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month6);
                        }
                    }

                    if (month == 7)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year7 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month7);
                        }
                        if (day > end_day && credit_month_double8 > 0.00 && credit_line_pen_payment_year7 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month7);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*1;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*2;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*3;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*4;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*5;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*6;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*7;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*8;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*9;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*10;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month7",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double8;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month8",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month7","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month7);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month7);
                        }
                    }

                    if (month == 8)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year8 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month8);
                        }
                        if (day > end_day && credit_month_double8 > 0.00 && credit_line_pen_payment_year8 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month8);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*1;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*2;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*3;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*4;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*5;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*6;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*7;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*8;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*9;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*10;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month8",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_pen_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double9;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month9",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month8","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month8);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month8);
                        }
                    }

                    if (month == 9)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year9 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month9);
                        }
                        if (day > end_day && credit_month_double9 > 0.00 && credit_line_pen_payment_year9 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month9);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*1;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*2;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*3;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*4;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*5;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*6;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*7;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*8;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*9;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*10;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month9",new_payment_st);
                                hashMap.put("credit_line_pen_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double10;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month10",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month9","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month9);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month9);
                        }
                    }

                    if (month == 10)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year10 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month10);
                        }
                        if (day > end_day && credit_month_double10 > 0.00 && credit_line_pen_payment_year10 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month10);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*1;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*2;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*3;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*4;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*5;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*6;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*7;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*8;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*9;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*10;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month10",new_payment_st);
                                hashMap.put("credit_line_pen_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double11;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month11",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month10","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month10);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month10);
                        }

                    }

                    if (month == 11)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year11 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month11);
                        }
                        if (day > end_day && credit_month_double11 > 0.00 && credit_line_pen_payment_year11 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month11);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*1;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*2;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*3;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*4;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*5;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*6;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*7;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*8;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*9;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*10;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month11",new_payment_st);
                                hashMap.put("credit_line_pen_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double12;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month12",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month11","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month11);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month11);
                        }
                    }

                    if (month == 12)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year12 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month12);
                        }
                        if (day > end_day && credit_month_double12 > 0.00 && credit_line_pen_payment_year12 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month12);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*1;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*2;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*3;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*4;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*5;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*6;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*7;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*8;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*9;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*10;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_pen_payment_month12",new_payment_st);
                                hashMap.put("credit_line_pen_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double1;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_pen_payment_month1",quotes_acomulated_st);
                                hashMap.put("credit_line_pen_payment_month12","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month12);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month12);
                        }
                    }


                    userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            loadingBar.dismiss();
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
                if (TextUtils.isEmpty(edtAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Ingresa el monto a pagar primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    account_currency = "PEN";
                    edtAmmount.setEnabled(false);

                    if (month == 1 && credit_line_pen_payment_year1 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month1);
                    }
                    if (month == 1 && credit_line_pen_payment_year1 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 2 && credit_line_pen_payment_year2 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month2);
                    }
                    if (month == 2 && credit_line_pen_payment_year2 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 3 && credit_line_pen_payment_year3 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month3);
                    }
                    if (month == 3 && credit_line_pen_payment_year3 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 4 && credit_line_pen_payment_year4 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month4);
                    }
                    if (month == 4 && credit_line_pen_payment_year4 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 5 && credit_line_pen_payment_year5 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month5);
                    }
                    if (month == 5 && credit_line_pen_payment_year5 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 6 && credit_line_pen_payment_year6 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month6);
                    }
                    if (month == 6 && credit_line_pen_payment_year6 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 7 && credit_line_pen_payment_year7 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month7);
                    }
                    if (month == 7 && credit_line_pen_payment_year7 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 8 && credit_line_pen_payment_year8 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month8);
                    }
                    if (month == 8 && credit_line_pen_payment_year8 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 9 && credit_line_pen_payment_year9 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month9);
                    }
                    if (month == 9 && credit_line_pen_payment_year9 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 10 && credit_line_pen_payment_year10 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month10);
                    }
                    if (month == 10 && credit_line_pen_payment_year10 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 11 && credit_line_pen_payment_year11 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month11);
                    }
                    if (month == 11 && credit_line_pen_payment_year11 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (month == 12 && credit_line_pen_payment_year12 == current_year)
                    {
                        current_user_debt = Double.parseDouble(credit_line_pen_payment_month12);
                    }
                    if (month == 12 && credit_line_pen_payment_year12 != current_year)
                    {
                        current_user_debt = 0.00;
                    }
                    if (ammount > current_user_debt)
                    {
                        Snackbar.make(rootLayout, "El monto que deseas pagar excede tu deuda, no deberías pagar más...", Snackbar.LENGTH_LONG).show();
                        return;
                    }


                    ammount = Double.parseDouble(edtAmmount.getText().toString());

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String current_debt_st = decimalFormat.format(current_user_debt);
                    String ammount_st = decimalFormat.format(ammount);
                    txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+current_debt_st);
                    txtMonth.setText("Mes a pagar: "+month);
                    txtxDebt.setText("Deuda actual: S/"+current_debt_st);
                    txtRealAmmount.setText("Monto a pagar: S/"+ammount_st);

                    last_debt = current_user_debt-ammount;
                    String last_debt_st = decimalFormat.format(last_debt);
                    if (last_debt <= 0.00)
                    {
                        txtLastDebt.setText("Deuda restante: S/0.00");
                    }
                    if (last_debt > 0.00)
                    {
                        txtLastDebt.setText("Deuda restante: S/"+last_debt_st);
                    }

                }
            }
        });

        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdUsdAccount.setEnabled(false);
                //Show Dialog of Forex Exchange Market
                showAccoutDialog();
            }
        });

        btnPayCreditPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (money_account_pen < ammount)
                {
                    Snackbar.make(rootLayout, "No cuentas con dinero suficiente para esta operación...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Ingresa el monto a pagar primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar tu cuenta en soles (S/) primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (last_debt < 0.00)
                {
                    Snackbar.make(rootLayout, "El monto que deseas pagar excede a la deuda, no deberías pagar más", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (current_user_debt == 0.00)
                {
                    Snackbar.make(rootLayout, "No tienes deudas para este mes...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    pinDialog();
                }
            }
        });

        imageResourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    credit_line_image = dataSnapshot.child("credit_line_image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void pinDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
                    paymentCreditLine();
                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }

    private void paymentCreditLine() {
        loadingBar.setTitle("Pagando Crédito...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);
        final DecimalFormat decimalFormat = new DecimalFormat("0.00");
        HashMap hashMap = new HashMap();
        String last_debt_st = decimalFormat.format(last_debt);
        if (month == 1)
        {
            hashMap.put("credit_line_pen_payment_month1",last_debt_st);
        }
        if (month == 2)
        {
            hashMap.put("credit_line_pen_payment_month2",last_debt_st);
        }
        if (month == 3)
        {
            hashMap.put("credit_line_pen_payment_month3",last_debt_st);
        }
        if (month == 4)
        {
            hashMap.put("credit_line_pen_payment_month4",last_debt_st);
        }
        if (month == 5)
        {
            hashMap.put("credit_line_pen_payment_month5",last_debt_st);
        }
        if (month == 6)
        {
            hashMap.put("credit_line_pen_payment_month6",last_debt_st);
        }
        if (month == 7)
        {
            hashMap.put("credit_line_pen_payment_month7",last_debt_st);
        }
        if (month == 8)
        {
            hashMap.put("credit_line_pen_payment_month8",last_debt_st);
        }
        if (month == 9)
        {
            hashMap.put("credit_line_pen_payment_month9",last_debt_st);
        }
        if (month == 10)
        {
            hashMap.put("credit_line_pen_payment_month10",last_debt_st);
        }
        if (month == 11)
        {
            hashMap.put("credit_line_pen_payment_month11",last_debt_st);
        }
        if (month == 12)
        {
            hashMap.put("credit_line_pen_payment_month12",last_debt_st);
        }

        ammount = Double.parseDouble(edtAmmount.getText().toString());
        double ammount_in_account = money_account_pen-ammount;
        String ammount_in_account_st = decimalFormat.format(ammount_in_account);
        hashMap.put("basic_account_pen",ammount_in_account_st);

        //Update credit line
        double new_used_credit_line = credit_line__used_pen_double-ammount;
        String new_used_credit_line_st;
        if (new_used_credit_line < 0.00) {
            new_used_credit_line_st = "0.00";
        } else {
            new_used_credit_line_st = decimalFormat.format(new_used_credit_line);
        }

        hashMap.put("credit_line_pen_used",new_used_credit_line_st);

        userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    double real_amount_tocharge = credit_line_to_chage_now_db-ammount;
                    String real_amount_tocharge_st = decimalFormat.format(real_amount_tocharge);
                    oliverBankRef.child("Credit Line Amount to Charge Now PEN").setValue(real_amount_tocharge_st);
                    registerOperation();
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
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;

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
        String ammount_df = decimalFormat.format(ammount);
        myOperationMap.put("credit_payment_ammount",ammount_df);
        myOperationMap.put("credit_payment_ammount_currency","PEN");
        myOperationMap.put("credit_month",""+month);
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent = new Intent(CreditLinePenPayActivity.this, InvesmentFundTransactionCompletedActivity.class);
                intent.putExtra("PostKey", postKey);
                intent.putExtra("TransactionCode",operationRandomName+"CL");
                startActivity(intent);
                loadingBar.dismiss();
                finish();
            }
        });
    }

    private void showAccoutDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Debes usar una cuenta en soles");
        dialog.setIcon(R.drawable.money2_icon);
        dialog.setMessage("Para cumplir con tus obliaciones en soles debes convertir tu dinero a soles.");

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
