package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class FactoringDiscountRateActivity extends AppCompatActivity {

    CircleImageView imageCompany,imageBuyerCompany;
    TextView txtCompanyName,txtCompanyRuc,txtCompanyVerification,txtBuyerCompanyName,txtBuyerCompanyRuc,txtBuyerCompanyVerification,txtBillAmmount,txtStartBill,txtEndBill,txtMinimunAnualInterestRate;
    ImageView imageCompanyVerification,imageBuyerCompanyVerification;
    EditText edtInterestRate;
    Button btnCalculateDiscount,btnNext,btnEndDay,btnEndMonth,btnEndYear;
    RelativeLayout rootLayout;
    private DatabaseReference billRefRef,companyRef,rates;
    String bill_key,factoring_min_ammount,factoring_min_discount,factoring_resguard_rate,bill_ammount,main_currency,saveCurrentDate,finishDate,bill_issue_date,end_day,end_month,end_year,
            my_company_id,buyer_company_id,my_company_image,buyer_company_image,my_company_name,buyer_company_name,my_company_ruc,buyer_company_ruc,my_company_verification,buyer_company_verification,
            bill_end_date;
    double min_ammount, bill_amount,min_rate,real_rate;
    long diff,days_to__finance;

    ArrayList<String> days =new ArrayList<>();
    SpinnerDialog spinnerEndDays;

    ArrayList<String> months =new ArrayList<>();
    SpinnerDialog spinnerEndMonths;

    ArrayList<String> years =new ArrayList<>();
    SpinnerDialog spinnerEndYears;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factoring_discount_rate);

        bill_key = getIntent().getExtras().get("bill_id").toString();

        billRefRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");
        companyRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        rates = FirebaseDatabase.getInstance().getReference().child("Rates");

        imageCompany = findViewById(R.id.imageCompany);
        imageBuyerCompany = findViewById(R.id.imageBuyerCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyRuc = findViewById(R.id.txtCompanyRuc);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        txtBuyerCompanyName = findViewById(R.id.txtBuyerCompanyName);
        txtBuyerCompanyRuc = findViewById(R.id.txtBuyerCompanyRuc);
        txtBuyerCompanyVerification = findViewById(R.id.txtBuyerCompanyVerification);
        txtBillAmmount = findViewById(R.id.txtBillAmmount);
        txtStartBill = findViewById(R.id.txtStartBill);
        txtEndBill = findViewById(R.id.txtEndBill);
        txtMinimunAnualInterestRate = findViewById(R.id.txtMinimunAnualInterestRate);
        edtInterestRate = findViewById(R.id.edtInterestRate);
        btnCalculateDiscount = findViewById(R.id.btnCalculateDiscount);
        btnNext = findViewById(R.id.btnNext);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);
        imageBuyerCompanyVerification = findViewById(R.id.imageBuyerCompanyVerification);
        btnEndDay = findViewById(R.id.btnEndDay);
        btnEndMonth = findViewById(R.id.btnEndMonth);
        btnEndYear = findViewById(R.id.btnEndYear);
        rootLayout = findViewById(R.id.rootLayout);

        getDataDate();

        days.add("1"); days.add("2"); days.add("3"); days.add("4"); days.add("5"); days.add("6"); days.add("7"); days.add("8"); days.add("9"); days.add("10");
        days.add("11"); days.add("12"); days.add("13"); days.add("14"); days.add("15"); days.add("16"); days.add("17"); days.add("18"); days.add("19"); days.add("20");
        days.add("21"); days.add("22"); days.add("23"); days.add("24"); days.add("25"); days.add("26"); days.add("27"); days.add("28"); days.add("29"); days.add("30");
        days.add("31");

        months.add("1");months.add("2");months.add("3");months.add("4");months.add("5");months.add("6");months.add("7");months.add("8");months.add("9");months.add("10");
        months.add("11");months.add("12");

        years.add("2020");years.add("2021");years.add("2022");years.add("2023");years.add("2024");years.add("2025");
        years.add("2026");years.add("2027");years.add("2028");years.add("2029");years.add("2030");years.add("2031");years.add("2032");years.add("2033");years.add("2034");
        years.add("2035");years.add("2036");years.add("2037");years.add("2038");years.add("2039");years.add("2040");

        btnEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndDays.showSpinerDialog();
            }
        });

        spinnerEndDays = new SpinnerDialog(FactoringDiscountRateActivity.this,days,"Selecciona el día");
        spinnerEndDays.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndDay.setText(item);
                Toast.makeText(FactoringDiscountRateActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndMonths.showSpinerDialog();
            }
        });

        spinnerEndMonths = new SpinnerDialog(FactoringDiscountRateActivity.this,months,"Selecciona el mes");
        spinnerEndMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndMonth.setText(item);
                Toast.makeText(FactoringDiscountRateActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndYears.showSpinerDialog();
            }
        });

        spinnerEndYears = new SpinnerDialog(FactoringDiscountRateActivity.this,years,"Selecciona el año");
        spinnerEndYears.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndYear.setText(item);
                Toast.makeText(FactoringDiscountRateActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        rates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    factoring_min_ammount  = dataSnapshot.child("factoring_min_ammount").getValue().toString();
                    factoring_min_discount = dataSnapshot.child("factoring_min_discount").getValue().toString();
                    factoring_resguard_rate = dataSnapshot.child("factoring_resguard_rate").getValue().toString();

                    billRefRef.child(bill_key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                bill_ammount = dataSnapshot.child("bill_ammount").getValue().toString();
                                main_currency = dataSnapshot.child("bill_currency").getValue().toString();
                                bill_issue_date = dataSnapshot.child("bill_issue_date").getValue().toString();
                                end_day = dataSnapshot.child("bill_end_day").getValue().toString();
                                end_month = dataSnapshot.child("bill_end_month").getValue().toString();
                                end_year = dataSnapshot.child("bill_end_year").getValue().toString();
                                bill_end_date = dataSnapshot.child("bill_end_date").getValue().toString();
                                my_company_id = dataSnapshot.child("my_company_id").getValue().toString();
                                buyer_company_id = dataSnapshot.child("buyer_company_id").getValue().toString();

                                my_company_image = dataSnapshot.child("my_company_image").getValue().toString();
                                buyer_company_image = dataSnapshot.child("buyer_company_image").getValue().toString();
                                my_company_name = dataSnapshot.child("my_company_name").getValue().toString();
                                buyer_company_name = dataSnapshot.child("buyer_company_name").getValue().toString();
                                my_company_ruc = dataSnapshot.child("my_company_ruc").getValue().toString();
                                buyer_company_ruc = dataSnapshot.child("buyer_company_ruc").getValue().toString();
                                my_company_verification = dataSnapshot.child("my_company_verification").getValue().toString();
                                buyer_company_verification = dataSnapshot.child("buyer_company_verification").getValue().toString();

                                Picasso.with(FactoringDiscountRateActivity.this).load(my_company_image).fit().into(imageCompany);
                                Picasso.with(FactoringDiscountRateActivity.this).load(buyer_company_image).fit().into(imageBuyerCompany);
                                txtCompanyName.setText("Nombre: "+my_company_name);
                                txtBuyerCompanyName.setText("Nombre: "+buyer_company_name);
                                txtCompanyRuc.setText("RUC O DNI: "+my_company_ruc);
                                txtBuyerCompanyRuc.setText("RUC O DNI: "+buyer_company_ruc);
                                if (my_company_verification.equals("true"))
                                {
                                    imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                                    txtCompanyVerification.setText("Verificado Correctamente");
                                }
                                if (my_company_verification.equals("false"))
                                {
                                    imageCompanyVerification.setImageResource(R.drawable.error_icon);
                                    txtCompanyVerification.setText("Denegado");
                                }
                                else
                                {
                                    imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                                }

                                if (buyer_company_verification.equals("true"))
                                {
                                    imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                                    txtCompanyVerification.setText("Verificado Correctamente");
                                }
                                if (buyer_company_verification.equals("false"))
                                {
                                    imageCompanyVerification.setImageResource(R.drawable.error_icon);
                                    txtCompanyVerification.setText("Denegado");
                                }
                                else
                                {
                                    imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                                }

                                txtBillAmmount.setText("Importe de la factura: "+bill_ammount+" "+main_currency);
                                txtStartBill.setText("Emisión de la factura: "+bill_issue_date);
                                txtEndBill.setText("Vencimiento de la factura: "+bill_end_date);

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


        btnCalculateDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min_ammount = Double.parseDouble(factoring_min_ammount);
                bill_amount = Double.parseDouble(bill_ammount);
                if (bill_amount < min_ammount)
                {
                    Snackbar.make(rootLayout, "El importe total de la factura debe ser mayor a "+factoring_min_ammount+" "+main_currency, Snackbar.LENGTH_LONG).show();
                    return;
                }
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                String saveCurrentDate =currentDate.format(calForDate.getTime());
                String finishDate = end_day+" "+end_month+" "+end_year;

                try {
                    Date date1 = currentDate.parse(saveCurrentDate);
                    Date date2 = currentDate.parse(finishDate);
                    diff = date2.getTime() - date1.getTime();
                    days_to__finance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (days_to__finance <= 0)
                {
                    Snackbar.make(rootLayout, "Tu factura está vencida", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar la tasa efectiva anual", Snackbar.LENGTH_LONG).show();
                    return;
                }
                min_rate = Double.parseDouble(factoring_min_discount);
                real_rate = Double.parseDouble(edtInterestRate.getText().toString());
                if (real_rate < min_rate)
                {
                    txtMinimunAnualInterestRate.setText("TREA mínima: "+factoring_min_discount+"%");
                    Snackbar.make(rootLayout, "La tasa de descuento debe ser mayor a "+factoring_min_discount+"%", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (real_rate < min_rate)
                {
                    txtMinimunAnualInterestRate.setText("TREA mínima: "+factoring_min_discount+"%");
                    Snackbar.make(rootLayout, "La tasa de descuento debe ser mayor a "+factoring_min_discount+"%", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    showDiscountDialog();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSumaryActivity();
            }
        });
    }

    private void goToSumaryActivity() {
        Intent intent = new Intent(FactoringDiscountRateActivity.this,FactoringMyCompanySumaryActivity.class);
        intent.putExtra("PostKey",bill_key);
        intent.putExtra("end_day_factoring",btnEndDay.getText().toString());
        intent.putExtra("end_month_factoring",btnEndMonth.getText().toString());
        intent.putExtra("end_year_factoring",btnEndYear.getText().toString());
        intent.putExtra("rate",edtInterestRate.getText().toString());
        startActivity(intent);
    }

    private void showDiscountDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Descuento por Factoring");
        dialog.setIcon(R.drawable.money2_icon);
        dialog.setMessage("Montos Estimados:");

        LayoutInflater inflater = LayoutInflater.from(this);
        View factoring_layout = inflater.inflate(R.layout.factoring_discount_layout,null);

        TextView txtAmmount,txtAnualInterestRate,txtCurrentDate,txtFinishDate,txtDaysToFinance,txtPaymentInAdvance,txtDailyInterestRate,txtPeriodicalRate,txtDiscountAmmount,txtResguardRate,txtMoneyTotheEnd,txtMoneyToNow;

        txtAmmount = factoring_layout.findViewById(R.id.txtAmmount);
        txtAnualInterestRate = factoring_layout.findViewById(R.id.txtAnualInterestRate);
        txtCurrentDate = factoring_layout.findViewById(R.id.txtCurrentDate);
        txtFinishDate = factoring_layout.findViewById(R.id.txtFinishDate);
        txtDaysToFinance = factoring_layout.findViewById(R.id.txtDaysToFinance);
        txtDailyInterestRate = factoring_layout.findViewById(R.id.txtDailyInterestRate);
        txtPeriodicalRate = factoring_layout.findViewById(R.id.txtPeriodicalRate);
        txtDiscountAmmount = factoring_layout.findViewById(R.id.txtDiscountAmmount);
        txtResguardRate = factoring_layout.findViewById(R.id.txtResguardRate);
        txtMoneyTotheEnd = factoring_layout.findViewById(R.id.txtMoneyTotheEnd);
        txtMoneyToNow = factoring_layout.findViewById(R.id.txtMoneyToNow);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());
        finishDate = end_day+" "+end_month+" "+end_year;

        try {
            Date date1 = currentDate.parse(saveCurrentDate);
            Date date2 = currentDate.parse(finishDate);
            long diff = date2.getTime() - date1.getTime();
            txtDaysToFinance.setText("Periodo a financiar: "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Tasa efectiva diaria:
        double rate_perone = real_rate/100;
        double monthly_rate_base = 1+rate_perone;
        double monthly_rate_exp = 0.002777777;
        double monthly_rate = Math.pow(monthly_rate_base,monthly_rate_exp)-1;
        double monthly_rate_express = monthly_rate*100;

        //double periodical_discount_rate = monthly_rate*days_to__finance;
        double periodical_discount_rate = Math.pow(1+monthly_rate,days_to__finance)-1;
        double periodical_discount_rate_express = periodical_discount_rate*100;


        double discount_ammount_before = bill_amount-0;
        double discount_ammount = discount_ammount_before*periodical_discount_rate;

        double resguard_rate = Double.parseDouble(factoring_resguard_rate);
        double resguard_ammount = 0;

        double factoring_ammount = bill_amount-0-discount_ammount-resguard_ammount;

        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        String monthly_rate_df = decimalFormat.format(monthly_rate_express);
        String periodical_rate_df = decimalFormat.format(periodical_discount_rate_express);
        DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
        String discount_ammount_df = decimalFormat2.format(discount_ammount);
        String resguard_ammount_df = decimalFormat2.format(resguard_ammount);
        String factoring_ammount_df = decimalFormat2.format(factoring_ammount);

        txtAmmount.setText("Importe de la factura: "+bill_ammount+" "+main_currency);
        txtAnualInterestRate.setText("Tasa de interés efectiva anual: "+edtInterestRate.getText().toString()+"%");
        Calendar calForDate2 = Calendar.getInstance();
        SimpleDateFormat currentDate2 = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate2 =currentDate2.format(calForDate2.getTime());
        txtCurrentDate.setText("Fecha actual: "+saveCurrentDate2);
        txtFinishDate.setText("Vencimiento de la factura: "+finishDate);
        txtDailyInterestRate.setText("Tasa de interés efectiva diaria: "+monthly_rate_df+"%");
        txtPeriodicalRate.setText("Tasa aplicable para el período: "+periodical_rate_df+"%");
        txtDiscountAmmount.setText("Monto de descuento: "+discount_ammount_df+" "+main_currency);
        txtResguardRate.setText("Tasa de Resguardo: "+factoring_resguard_rate+"%");
        txtMoneyTotheEnd.setText("Monto a recibir al vencimiento: "+resguard_ammount_df+" "+main_currency);
        txtMoneyToNow.setText("Monto a recibir hoy: "+factoring_ammount_df+" "+main_currency);

        dialog.setView(factoring_layout);
        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
