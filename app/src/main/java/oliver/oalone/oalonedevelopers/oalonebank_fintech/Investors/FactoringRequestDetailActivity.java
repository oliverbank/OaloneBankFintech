package oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.FinanceRequestDetailActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.RegisterInvestmentOnFinanceRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class FactoringRequestDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imgCompanyImage,imageCompanyVerification;
    TextView txtCompanyName,txtCompanyType,txtBusinessLine,txtCompanyLocation,txtCompanyAge,txtBusinessMan,txtCompanyVerification,txtRequesstDate,txtCustomerSocialReason,
            txtBillAmmount,txtAmmount,txtInvestmentCurrency,txtInterestRate,txtEndDate,txtFinanceDays,txtPeriodicalRate,txtResguardAmmount,
            txtTotalDebt,txtInterestAmmount,txtFactoringRequestEnds;
    private String postKey, currentUserID, databaseUserID,company_name,company_image,company_type,company_line,company_department,company_bth_day,company_bth_month,company_bth_year,
            username,company_verification,date,time,customer_social_reason,bill_ammount,main_currency,the_real_investment,bill_rate,end_date,
            end_day,end_month,end_year,saveCurrentDate,finishDate,factoring_resguard_rate,bill_id,bill_factoring,end_day_factoring,end_month_factoring,end_year_factoring,
            finance_request_expired;
    private FirebaseAuth mAuth;
    private DatabaseReference clickPostRef,rates,billsRef;
    long diff,days_to_expire,diff2;
    double resguard_rate,periodical_discount_rate;
    Button btnInvest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factoring_request_detail);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests").child(postKey);
        rates = FirebaseDatabase.getInstance().getReference().child("Rates");
        billsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");

        toolbar = findViewById(R.id.toolbar);
        imgCompanyImage = findViewById(R.id.imgCompanyImage);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyType = findViewById(R.id.txtCompanyType);
        txtBusinessLine = findViewById(R.id.txtBusinessLine);
        txtCompanyLocation = findViewById(R.id.txtCompanyLocation);
        txtCompanyAge = findViewById(R.id.txtCompanyAge);
        txtBusinessMan = findViewById(R.id.txtBusinessMan);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);
        txtRequesstDate = findViewById(R.id.txtRequesstDate);
        txtCustomerSocialReason = findViewById(R.id.txtCustomerSocialReason);
        txtBillAmmount = findViewById(R.id.txtBillAmmount);
        txtAmmount = findViewById(R.id.txtAmmount);
        txtInvestmentCurrency = findViewById(R.id.txtInvestmentCurrency);
        txtInterestRate = findViewById(R.id.txtInterestRate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtFinanceDays = findViewById(R.id.txtFinanceDays);
        txtPeriodicalRate = findViewById(R.id.txtPeriodicalRate);
        txtResguardAmmount = findViewById(R.id.txtResguardAmmount);
        txtTotalDebt = findViewById(R.id.txtTotalDebt);
        txtInterestAmmount = findViewById(R.id.txtInterestAmmount);
        txtFactoringRequestEnds = findViewById(R.id.txtFactoringRequestEnds);
        btnInvest = findViewById(R.id.btnInvest);

        getDataDate();

        clickPostRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    databaseUserID = dataSnapshot.child("uid").getValue().toString();
                    company_name = dataSnapshot.child("company_name").getValue().toString();
                    company_image = dataSnapshot.child("company_image").getValue().toString();
                    company_type = dataSnapshot.child("company_type").getValue().toString();
                    company_line = dataSnapshot.child("company_line").getValue().toString();
                    company_department = dataSnapshot.child("company_department").getValue().toString();
                    company_bth_day = dataSnapshot.child("company_bth_day").getValue().toString();
                    company_bth_month = dataSnapshot.child("company_bth_month").getValue().toString();
                    company_bth_year = dataSnapshot.child("company_bth_year").getValue().toString();
                    username = dataSnapshot.child("username").getValue().toString();
                    company_verification = dataSnapshot.child("company_verification").getValue().toString();
                    date = dataSnapshot.child("date").getValue().toString();
                    time = dataSnapshot.child("time").getValue().toString();
                    customer_social_reason = dataSnapshot.child("customer_social_reason").getValue().toString();
                    bill_ammount = dataSnapshot.child("bill_ammount").getValue().toString();
                    main_currency = dataSnapshot.child("main_currency").getValue().toString();
                    the_real_investment = dataSnapshot.child("the_real_investment").getValue().toString();
                    bill_rate = dataSnapshot.child("bill_rate").getValue().toString();
                    end_date = dataSnapshot.child("end_date").getValue().toString();
                    end_day = dataSnapshot.child("end_day").getValue().toString();
                    end_month = dataSnapshot.child("end_month").getValue().toString();
                    end_year = dataSnapshot.child("end_year").getValue().toString();
                    bill_id = dataSnapshot.child("bill_id").getValue().toString();
                    end_day_factoring = dataSnapshot.child("end_day_factoring").getValue().toString();
                    end_month_factoring = dataSnapshot.child("end_month_factoring").getValue().toString();
                    end_year_factoring = dataSnapshot.child("end_year_factoring").getValue().toString();
                    finance_request_expired = dataSnapshot.child("finance_request_expired").getValue().toString();

                    toolbar.setTitle(company_name);
                    Picasso.with(FactoringRequestDetailActivity.this).load(company_image)
                            //.resize(840,840)
                            //.onlyScaleDown()
                            .centerCrop()
                            .fit()
                            .into(imgCompanyImage);
                    txtCompanyName.setText("Nombre comercial: "+company_name);
                    txtCompanyType.setText("Tipo de empresa: "+company_type);
                    txtBusinessLine.setText("Actividad económica: "+company_line);
                    txtCompanyLocation.setText("Localización: "+company_department);
                    int day = Integer.parseInt(company_bth_day);
                    int month = Integer.parseInt(company_bth_month);
                    int year = Integer.parseInt(company_bth_year);
                    //Get Years
                    Calendar dob = Calendar.getInstance();
                    dob.set(year, month, day);
                    Calendar today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                    //LocalDate birth_of_date =LocalDate.of(year,month,day);
                    //Get Current Day
                    //LocalDate today = LocalDate.now();
                    //Get the age
                    //int age = Period.between(birth_of_date, today).getYears();
                    String company_age = Integer.toString(age);
                    txtCompanyAge.setText("Años en el mercado: "+company_age+" años");
                    txtBusinessMan.setText("Empresario (nombre de usuario): "+username);
                    if (company_verification.equals("progress"))
                    {
                        txtCompanyVerification.setText("Verificación de la emrpresa: Verficación en proceso");
                        imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                    }
                    if (company_verification.equals("true"))
                    {
                        txtCompanyVerification.setText("Verificación de la emrpresa: Verficación exitosa");
                        imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                    }
                    if (company_verification.equals("false"))
                    {
                        txtCompanyVerification.setText("Verificación de la emrpresa: Verficación fallida");
                        imageCompanyVerification.setImageResource(R.drawable.demo_icon);
                    }

                    txtCustomerSocialReason.setText("Razón social del cliente: "+customer_social_reason);
                    txtBillAmmount.setText("Importe de la factura: "+bill_ammount+" "+main_currency);

                    txtInvestmentCurrency.setText(main_currency);
                    txtInterestRate.setText(bill_rate);
                    txtEndDate.setText(end_date);

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                    saveCurrentDate =currentDate.format(calForDate.getTime());
                    finishDate = end_day+" "+end_month+" "+end_year;
                    try {
                        Date date1 = currentDate.parse(saveCurrentDate);
                        Date date2 = currentDate.parse(finishDate);
                        diff = date2.getTime() - date1.getTime();
                        txtFinanceDays.setText("Periodo a financiar: "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Get time of Finace Request Expiration
                    String expiration_finance_request_date = end_day_factoring+" "+end_month_factoring+" "+end_year_factoring;
                    try {
                        Date date1 = currentDate.parse(saveCurrentDate);
                        Date date2 = currentDate.parse(expiration_finance_request_date);
                        diff2 = date2.getTime() - date1.getTime();
                        days_to_expire = TimeUnit.DAYS.convert(diff2, TimeUnit.MILLISECONDS);

                        if (days_to_expire <= 0) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("finance_request_expired","true");
                            clickPostRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        txtFactoringRequestEnds.setText("Esta subasta ha finalizado");
                                        btnInvest.setText("SUBASTA FINALIZADA");
                                        btnInvest.setEnabled(false);
                                    }
                                }
                            });
                        }
                        if (days_to_expire > 0) {
                            txtFactoringRequestEnds.setText("Esta subasta termina en "+days_to_expire+" dias");
                        }

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

                    txtPeriodicalRate.setText("Tasa aplicable para el período: "+periodical_discount_rate_express_df+"%");


                    rates.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                factoring_resguard_rate = dataSnapshot.child("factoring_resguard_rate").getValue().toString();
                                resguard_rate = Double.parseDouble(factoring_resguard_rate);

                                double bill_amount = Double.parseDouble(bill_ammount);
                                double discount_ammount_before = bill_amount-0;
                                double discount_ammount = discount_ammount_before*periodical_discount_rate;

                                DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
                                //double resguard_ammount = (resguard_rate/100)*(bill_amount-0);
                                double resguard_ammount = 0;
                                String resguard_ammount_df = decimalFormat2.format(resguard_ammount);

                                txtResguardAmmount.setText("Resguardo: "+resguard_ammount_df+" "+main_currency);

                                double factoring_ammount = bill_amount-0-discount_ammount-resguard_ammount;
                                String factoring_ammount_df = decimalFormat2.format(factoring_ammount);

                                txtAmmount.setText(factoring_ammount_df);

                                txtTotalDebt.setText("Retorno total: "+the_real_investment+" "+main_currency);

                                String discount_ammount_df = decimalFormat2.format(discount_ammount);
                                txtInterestAmmount.setText(discount_ammount_df+" "+main_currency);

                                billsRef.child(bill_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            bill_factoring = dataSnapshot.child("bill_factoring").getValue().toString();
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



                    txtRequesstDate.setText("Solicitud publicada el "+date+" a las "+time+" por "+username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bill_factoring.equals("success")) {
                    showRiskRestrictionDialog();
                }
                if (bill_factoring.equals("canceled")) {
                    showRiskRestrictionDialog();
                }
                if (bill_factoring.equals("true")) {
                    Intent intent = new Intent(FactoringRequestDetailActivity.this, RegisterInvestmentOnFactoringRequestActivity.class);
                    intent.putExtra("PostKey",postKey);
                    startActivity(intent);
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

    private void showRiskRestrictionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.bill_already_invested,null);

        dialog.setView(add_bank_account);

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}
