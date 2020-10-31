package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CreditLineCompanyUsdSchduleActivity extends AppCompatActivity {

    TextView txtQuoteAmmount1,txtQuoteAmmount2,txtQuoteAmmount3,txtQuoteAmmount4,txtQuoteAmmount5,txtQuoteAmmount6,
            txtQuoteAmmount7,txtQuoteAmmount8,txtQuoteAmmount9,txtQuoteAmmount10,txtQuoteAmmount11,txtQuoteAmmount12,
            txtStartDate1,txtStartDate2,txtStartDate3,txtStartDate4,txtStartDate5,txtStartDate6,txtStartDate7,txtStartDate8,txtStartDate9,txtStartDate10,txtStartDate11,
            txtStartDate12,txtEndDate1,txtEndDate2,txtEndDate3,txtEndDate4,txtEndDate5,txtEndDate6,txtEndDate7,txtEndDate8,txtEndDate9,
            txtEndDate10,txtEndDate11,txtEndDate12,txtCreditLinePaymentMonthPen,txtTittle;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,ratesRef,myOperationsRef,imageResourses;
    String currentUserID, credit_line_pen_payment_month1,
            credit_line_pen_payment_month2,credit_line_pen_payment_month3,credit_line_pen_payment_month4,credit_line_pen_payment_month5,credit_line_pen_payment_month6,
            credit_line_pen_payment_month7,credit_line_pen_payment_month8,credit_line_pen_payment_month9,credit_line_pen_payment_month10,credit_line_pen_payment_month11,
            credit_line_pen_payment_month12,credit_line_start_day_payment,credit_line_end_day_payment,defaulter_credit_pen_daily_rate,new_payment_st,quotes_acomulated_st,
            credit_line_pen_default_day1,credit_line_pen_default_day2,credit_line_pen_default_day3,credit_line_pen_default_day4,credit_line_pen_default_day5,
            credit_line_pen_default_day6,credit_line_pen_default_day7,credit_line_pen_default_day8,credit_line_pen_default_day9,credit_line_pen_default_day10;
    int credit_line_pen_payment_year1,credit_line_pen_payment_year2,credit_line_pen_payment_year3,credit_line_pen_payment_year4,
            credit_line_pen_payment_year5,credit_line_pen_payment_year6,credit_line_pen_payment_year7,credit_line_pen_payment_year8,credit_line_pen_payment_year9,credit_line_pen_payment_year10,
            credit_line_pen_payment_year11,credit_line_pen_payment_year12;
    int month,day, start_day, end_day,current_year;
    double credit_month_double1,credit_month_double2,credit_month_double3,credit_month_double4,credit_month_double5,credit_month_double6,credit_month_double7,
            credit_month_double8,credit_month_double9,credit_month_double10,credit_month_double11,credit_month_double12,defaulter_credit_pen_daily_rate_double,defaulter_rate,
            new_payment, quotes_acomulated;
    String credit_line_pen_payment_year1_st,credit_line_pen_payment_year2_st,credit_line_pen_payment_year3_st,credit_line_pen_payment_year4_st,credit_line_pen_payment_year5_st,
            credit_line_pen_payment_year6_st,credit_line_pen_payment_year7_st,credit_line_pen_payment_year8_st,credit_line_pen_payment_year9_st,credit_line_pen_payment_year10_st
            ,credit_line_pen_payment_year11_st,credit_line_pen_payment_year12_st;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_line_company_usd_schdule);

        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = getIntent().getExtras().get("company_key").toString();

        txtTittle = findViewById(R.id.txtTittle);
        txtTittle.setSelected(true);

        txtQuoteAmmount1 = findViewById(R.id.txtQuoteAmmount1);
        txtQuoteAmmount2 = findViewById(R.id.txtQuoteAmmount2);
        txtQuoteAmmount3 = findViewById(R.id.txtQuoteAmmount3);
        txtQuoteAmmount4 = findViewById(R.id.txtQuoteAmmount4);
        txtQuoteAmmount5 = findViewById(R.id.txtQuoteAmmount5);
        txtQuoteAmmount6 = findViewById(R.id.txtQuoteAmmount6);
        txtQuoteAmmount7 = findViewById(R.id.txtQuoteAmmount7);
        txtQuoteAmmount8 = findViewById(R.id.txtQuoteAmmount8);
        txtQuoteAmmount9 = findViewById(R.id.txtQuoteAmmount9);
        txtQuoteAmmount10 = findViewById(R.id.txtQuoteAmmount10);
        txtQuoteAmmount11 = findViewById(R.id.txtQuoteAmmount11);
        txtQuoteAmmount12 = findViewById(R.id.txtQuoteAmmount12);

        txtStartDate1 = findViewById(R.id.txtStartDate1);
        txtStartDate2 = findViewById(R.id.txtStartDate2);
        txtStartDate3 = findViewById(R.id.txtStartDate3);
        txtStartDate4 = findViewById(R.id.txtStartDate4);
        txtStartDate5 = findViewById(R.id.txtStartDate5);
        txtStartDate6 = findViewById(R.id.txtStartDate6);
        txtStartDate7 = findViewById(R.id.txtStartDate7);
        txtStartDate8 = findViewById(R.id.txtStartDate8);
        txtStartDate9 = findViewById(R.id.txtStartDate9);
        txtStartDate10 = findViewById(R.id.txtStartDate10);
        txtStartDate11 = findViewById(R.id.txtStartDate11);
        txtStartDate12= findViewById(R.id.txtStartDate12);

        txtEndDate1 = findViewById(R.id.txtEndDate1);
        txtEndDate2 = findViewById(R.id.txtEndDate2);
        txtEndDate3 = findViewById(R.id.txtEndDate3);
        txtEndDate4 = findViewById(R.id.txtEndDate4);
        txtEndDate5 = findViewById(R.id.txtEndDate5);
        txtEndDate6 = findViewById(R.id.txtEndDate6);
        txtEndDate7 = findViewById(R.id.txtEndDate7);
        txtEndDate8 = findViewById(R.id.txtEndDate8);
        txtEndDate9 = findViewById(R.id.txtEndDate9);
        txtEndDate10 = findViewById(R.id.txtEndDate10);
        txtEndDate11 = findViewById(R.id.txtEndDate11);
        txtEndDate12 = findViewById(R.id.txtEndDate12);

        loadingBar = new ProgressDialog(this);

        getDataDate();

        txtCreditLinePaymentMonthPen= findViewById(R.id.txtCreditLinePaymentMonthPen);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingBar.setTitle("Preparando todo...");
                loadingBar.setMessage("Cargando...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.setCancelable(false);
                //Monthly Payments
                credit_line_pen_payment_month1 = dataSnapshot.child("credit_line_usd_payment_month1").getValue().toString();
                credit_line_pen_payment_month2 = dataSnapshot.child("credit_line_usd_payment_month2").getValue().toString();
                credit_line_pen_payment_month3 = dataSnapshot.child("credit_line_usd_payment_month3").getValue().toString();
                credit_line_pen_payment_month4 = dataSnapshot.child("credit_line_usd_payment_month4").getValue().toString();
                credit_line_pen_payment_month5 = dataSnapshot.child("credit_line_usd_payment_month5").getValue().toString();
                credit_line_pen_payment_month6 = dataSnapshot.child("credit_line_usd_payment_month6").getValue().toString();
                credit_line_pen_payment_month7 = dataSnapshot.child("credit_line_usd_payment_month7").getValue().toString();
                credit_line_pen_payment_month8 = dataSnapshot.child("credit_line_usd_payment_month8").getValue().toString();
                credit_line_pen_payment_month9 = dataSnapshot.child("credit_line_usd_payment_month9").getValue().toString();
                credit_line_pen_payment_month10 = dataSnapshot.child("credit_line_usd_payment_month10").getValue().toString();
                credit_line_pen_payment_month11 = dataSnapshot.child("credit_line_usd_payment_month11").getValue().toString();
                credit_line_pen_payment_month12 = dataSnapshot.child("credit_line_usd_payment_month12").getValue().toString();

                credit_line_pen_payment_year1_st = dataSnapshot.child("credit_line_usd_payment_year1").getValue().toString();
                credit_line_pen_payment_year2_st = dataSnapshot.child("credit_line_usd_payment_year2").getValue().toString();
                credit_line_pen_payment_year3_st = dataSnapshot.child("credit_line_usd_payment_year3").getValue().toString();
                credit_line_pen_payment_year4_st = dataSnapshot.child("credit_line_usd_payment_year4").getValue().toString();
                credit_line_pen_payment_year5_st = dataSnapshot.child("credit_line_usd_payment_year5").getValue().toString();
                credit_line_pen_payment_year6_st = dataSnapshot.child("credit_line_usd_payment_year6").getValue().toString();
                credit_line_pen_payment_year7_st = dataSnapshot.child("credit_line_usd_payment_year7").getValue().toString();
                credit_line_pen_payment_year8_st = dataSnapshot.child("credit_line_usd_payment_year8").getValue().toString();
                credit_line_pen_payment_year9_st = dataSnapshot.child("credit_line_usd_payment_year9").getValue().toString();
                credit_line_pen_payment_year10_st = dataSnapshot.child("credit_line_usd_payment_year10").getValue().toString();
                credit_line_pen_payment_year11_st = dataSnapshot.child("credit_line_usd_payment_year11").getValue().toString();
                credit_line_pen_payment_year12_st = dataSnapshot.child("credit_line_usd_payment_year12").getValue().toString();

                credit_line_pen_payment_year1 = Integer.parseInt(credit_line_pen_payment_year1_st);
                credit_line_pen_payment_year2 = Integer.parseInt(credit_line_pen_payment_year2_st);
                credit_line_pen_payment_year3 = Integer.parseInt(credit_line_pen_payment_year3_st);
                credit_line_pen_payment_year4 = Integer.parseInt(credit_line_pen_payment_year4_st);
                credit_line_pen_payment_year5 = Integer.parseInt(credit_line_pen_payment_year5_st);
                credit_line_pen_payment_year6 = Integer.parseInt(credit_line_pen_payment_year6_st);
                credit_line_pen_payment_year7 = Integer.parseInt(credit_line_pen_payment_year7_st);
                credit_line_pen_payment_year8 = Integer.parseInt(credit_line_pen_payment_year8_st);
                credit_line_pen_payment_year9 = Integer.parseInt(credit_line_pen_payment_year9_st);
                credit_line_pen_payment_year10 = Integer.parseInt(credit_line_pen_payment_year10_st);
                credit_line_pen_payment_year11 = Integer.parseInt(credit_line_pen_payment_year11_st);
                credit_line_pen_payment_year12 = Integer.parseInt(credit_line_pen_payment_year12_st);

                credit_line_pen_default_day1 = dataSnapshot.child("credit_line_usd_default_day1").getValue().toString();
                credit_line_pen_default_day2 = dataSnapshot.child("credit_line_usd_default_day2").getValue().toString();
                credit_line_pen_default_day3 = dataSnapshot.child("credit_line_usd_default_day3").getValue().toString();
                credit_line_pen_default_day4 = dataSnapshot.child("credit_line_usd_default_day4").getValue().toString();
                credit_line_pen_default_day5 = dataSnapshot.child("credit_line_usd_default_day5").getValue().toString();
                credit_line_pen_default_day6 = dataSnapshot.child("credit_line_usd_default_day6").getValue().toString();
                credit_line_pen_default_day7 = dataSnapshot.child("credit_line_usd_default_day7").getValue().toString();
                credit_line_pen_default_day8 = dataSnapshot.child("credit_line_usd_default_day8").getValue().toString();
                credit_line_pen_default_day9 = dataSnapshot.child("credit_line_usd_default_day9").getValue().toString();
                credit_line_pen_default_day10 = dataSnapshot.child("credit_line_usd_default_day10").getValue().toString();

                txtQuoteAmmount1.setText("$ "+credit_line_pen_payment_month1);
                txtQuoteAmmount2.setText("$ "+credit_line_pen_payment_month2);
                txtQuoteAmmount3.setText("$ "+credit_line_pen_payment_month3);
                txtQuoteAmmount4.setText("$ "+credit_line_pen_payment_month4);
                txtQuoteAmmount5.setText("$ "+credit_line_pen_payment_month5);
                txtQuoteAmmount6.setText("$ "+credit_line_pen_payment_month6);
                txtQuoteAmmount7.setText("$ "+credit_line_pen_payment_month7);
                txtQuoteAmmount8.setText("$ "+credit_line_pen_payment_month8);
                txtQuoteAmmount9.setText("$ "+credit_line_pen_payment_month9);
                txtQuoteAmmount10.setText("$ "+credit_line_pen_payment_month10);
                txtQuoteAmmount11.setText("$ "+credit_line_pen_payment_month11);
                txtQuoteAmmount12.setText("$ "+credit_line_pen_payment_month12);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratesRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    credit_line_start_day_payment = dataSnapshot.child("credit_line_start_day_payment").getValue().toString();
                    credit_line_end_day_payment = dataSnapshot.child("credit_line_end_day_payment").getValue().toString();
                    defaulter_credit_pen_daily_rate= dataSnapshot.child("defaulter_credit_pen_daily_rate").getValue().toString();

                    txtStartDate1.setText(credit_line_start_day_payment+" de Enero de "+credit_line_pen_payment_year1);
                    txtStartDate2.setText(credit_line_start_day_payment+" de Febrero de "+credit_line_pen_payment_year2);
                    txtStartDate3.setText(credit_line_start_day_payment+" de Marzo de "+credit_line_pen_payment_year3);
                    txtStartDate4.setText(credit_line_start_day_payment+" de Abril de "+credit_line_pen_payment_year4);
                    txtStartDate5.setText(credit_line_start_day_payment+" de Mayo de "+credit_line_pen_payment_year5);
                    txtStartDate6.setText(credit_line_start_day_payment+" de Junio de "+credit_line_pen_payment_year6);
                    txtStartDate7.setText(credit_line_start_day_payment+" de Julio de "+credit_line_pen_payment_year7);
                    txtStartDate8.setText(credit_line_start_day_payment+" de Agosto de "+credit_line_pen_payment_year8);
                    txtStartDate9.setText(credit_line_start_day_payment+" de Septiembre de "+credit_line_pen_payment_year9);
                    txtStartDate10.setText(credit_line_start_day_payment+" de Octubre de "+credit_line_pen_payment_year10);
                    txtStartDate11.setText(credit_line_start_day_payment+" de Noviembre de "+credit_line_pen_payment_year11);
                    txtStartDate12.setText(credit_line_start_day_payment+" de Diciembre de "+credit_line_pen_payment_year12);

                    txtEndDate1.setText(credit_line_end_day_payment+" de Enero de "+credit_line_pen_payment_year1);
                    txtEndDate2.setText(credit_line_end_day_payment+" de Febrero de "+credit_line_pen_payment_year2);
                    txtEndDate3.setText(credit_line_end_day_payment+" de Marzo de "+credit_line_pen_payment_year3);
                    txtEndDate4.setText(credit_line_end_day_payment+" de Abril de "+credit_line_pen_payment_year4);
                    txtEndDate5.setText(credit_line_end_day_payment+" de Mayo de "+credit_line_pen_payment_year5);
                    txtEndDate6.setText(credit_line_end_day_payment+" de Junio de "+credit_line_pen_payment_year6);
                    txtEndDate7.setText(credit_line_end_day_payment+" de Julio de "+credit_line_pen_payment_year7);
                    txtEndDate8.setText(credit_line_end_day_payment+" de Agosto de "+credit_line_pen_payment_year8);
                    txtEndDate9.setText(credit_line_end_day_payment+" de Septiembre de "+credit_line_pen_payment_year9);
                    txtEndDate10.setText(credit_line_end_day_payment+" de Octubre de "+credit_line_pen_payment_year10);
                    txtEndDate11.setText(credit_line_end_day_payment+" de Noviembre de "+credit_line_pen_payment_year11);
                    txtEndDate12.setText(credit_line_end_day_payment+" de Diciembre de "+credit_line_pen_payment_year12);

                    current_year = Calendar.getInstance().get(Calendar.YEAR);

                    Date date = new Date();
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    month = localDate.getMonthValue();
                    day = localDate.getDayOfMonth();

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
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month1);
                        }
                        if (day > end_day && credit_month_double1 > 0.00 && credit_line_pen_payment_year1 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month1);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*1;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*2;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*3;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*4;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*5;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*6;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*7;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*8;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*9;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*10;
                                new_payment = credit_month_double1+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month1",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double2;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month2",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month1","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month1);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month1);
                        }

                    }

                    if (month == 2)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year2 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month2);
                        }
                        if (day > end_day && credit_month_double2 > 0.00 && credit_line_pen_payment_year2 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month3);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double1))*1;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*2;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*3;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*4;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*5;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*6;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*7;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*8;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*9;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double2))*10;
                                new_payment = credit_month_double2+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month2",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double3;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month3",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month2","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month2);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month2);
                        }
                    }

                    if (month == 3)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year3 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month3);
                        }
                        if (day > end_day && credit_month_double3 > 0.00 && credit_line_pen_payment_year3 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month3);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*1;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*2;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*3;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*4;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*5;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*6;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*7;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*8;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*9;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double3))*10;
                                new_payment = credit_month_double3+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month3",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double4;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month4",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month3","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month3);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month3);
                        }
                    }

                    if (month == 4)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year4 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month4);
                        }
                        if (day > end_day && credit_month_double4 > 0.00 && credit_line_pen_payment_year4 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month4);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*1;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*2;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*3;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*4;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*5;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*6;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*7;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*8;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*9;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double4))*10;
                                new_payment = credit_month_double4+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month4",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double5;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month5",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month4","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month4);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month4);
                        }
                    }

                    if (month == 5)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year5 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month5);
                        }
                        if (day > end_day && credit_month_double5 > 0.00 && credit_line_pen_payment_year5 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month5);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*1;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*2;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*3;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*4;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*5;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*6;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*7;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*8;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*9;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double5))*10;
                                new_payment = credit_month_double5+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month5",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double6;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month6",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month5","0.00");

                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month5);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month5);
                        }
                    }

                    if (month == 6)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year6 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month6);
                        }
                        if (day > end_day && credit_month_double6 > 0.00 && credit_line_pen_payment_year6 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month6);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*1;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*2;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*3;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*4;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*5;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*6;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*7;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*8;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*9;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double6))*10;
                                new_payment = credit_month_double6+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month6",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double7;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month7",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month6","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month6);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month6);
                        }
                    }

                    if (month == 7)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year7 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month7);
                        }
                        if (day > end_day && credit_month_double7 > 0.00 && credit_line_pen_payment_year7 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month7);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*1;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*2;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*3;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*4;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*5;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*6;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*7;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*8;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*9;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double7))*10;
                                new_payment = credit_month_double7+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month7",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double8;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month8",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month7","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month7);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month7);
                        }
                    }

                    if (month == 8)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year8 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month8);
                        }
                        if (day > end_day && credit_month_double8 > 0.00 && credit_line_pen_payment_year8 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month8);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*1;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*2;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*3;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*4;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*5;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*6;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*7;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*8;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*9;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double8))*10;
                                new_payment = credit_month_double8+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month8",new_payment_st);
                                hashMap.put("user_is_defaulter","true");
                                hashMap.put("credit_line_usd_default_day10","true");
                                //This quite is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double9;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month9",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month8","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month8);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month8);
                        }
                    }

                    if (month == 9)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year9 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month9);
                        }
                        if (day > end_day && credit_month_double9 > 0.00 && credit_line_pen_payment_year9 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month9);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*1;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*2;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
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
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*5;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*6;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*7;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*8;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*9;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double9))*10;
                                new_payment = credit_month_double9+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month9",new_payment_st);
                                hashMap.put("credit_line_usd_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double10;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month10",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month9","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month9);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month9);
                        }
                    }

                    if (month == 10)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year10 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month10);
                        }
                        if (day > end_day && credit_month_double10 > 0.00 && credit_line_pen_payment_year10 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month10);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*1;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*2;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*3;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*4;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*5;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*6;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*7;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*8;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*9;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double10))*10;
                                new_payment = credit_month_double10+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month10",new_payment_st);
                                hashMap.put("credit_line_usd_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double11;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month11",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month10","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month10);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month10);
                        }

                    }

                    if (month == 11)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year11 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month11);
                        }
                        if (day > end_day && credit_month_double11 > 0.00 && credit_line_pen_payment_year11 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month11);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*1;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*2;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*3;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*4;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*5;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*6;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*7;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
                            }
                            if (day == end_day+8 && credit_line_pen_default_day8.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*8;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day8","true");
                            }
                            if (day == end_day+9 && credit_line_pen_default_day9.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*9;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double11))*10;
                                new_payment = credit_month_double11+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month11",new_payment_st);
                                hashMap.put("credit_line_usd_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double12;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month12",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month11","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month11);
                            }

                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/ "+credit_line_pen_payment_month11);
                        }
                    }

                    if (month == 12)
                    {
                        if (day >= start_day && day <= end_day && credit_line_pen_payment_year12 == current_year)
                        {
                            //Customer should pay in these days
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month12);
                        }
                        if (day > end_day && credit_month_double12 > 0.00 && credit_line_pen_payment_year12 == current_year)
                        {
                            txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month12);
                            //Customer didn't pay on time
                            if (day == end_day+1 && credit_line_pen_default_day1.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*1;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day1","true");
                            }
                            if (day == end_day+2 && credit_line_pen_default_day2.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*2;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day2","true");
                            }
                            if (day == end_day+3 && credit_line_pen_default_day3.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*3;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day3","true");
                            }
                            if (day == end_day+4 && credit_line_pen_default_day4.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*4;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day4","true");
                            }
                            if (day == end_day+5 && credit_line_pen_default_day5.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*5;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day5","true");
                            }
                            if (day == end_day+6 && credit_line_pen_default_day6.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*6;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day6","true");
                            }
                            if (day == end_day+7 && credit_line_pen_default_day7.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*7;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day7","true");
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
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day9","true");
                            }
                            if (day == end_day+10 && credit_line_pen_default_day10.equals("false"))
                            {
                                defaulter_rate = (((defaulter_credit_pen_daily_rate_double/100)*credit_month_double12))*10;
                                new_payment = credit_month_double12+defaulter_rate;
                                new_payment_st = decimalFormat.format(new_payment);
                                hashMap.put("credit_line_usd_payment_month12",new_payment_st);
                                hashMap.put("credit_line_usd_default_day10","true");
                                hashMap.put("user_is_defaulter","true");
                                //This quote is for the next month: debt + next quote
                                quotes_acomulated = new_payment+credit_month_double1;
                                quotes_acomulated_st = decimalFormat.format(quotes_acomulated);
                                hashMap.put("credit_line_usd_payment_month1",quotes_acomulated_st);
                                hashMap.put("credit_line_usd_payment_month12","0.00");
                            }
                            else
                            {
                                //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ 0.00");
                                txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: $ "+credit_line_pen_payment_month12);
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
