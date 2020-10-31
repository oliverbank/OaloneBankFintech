package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CreateBillActivity extends AppCompatActivity {

    ImageView imageCompany,imageCompanyVerification;
    TextView txtCompanyName,txtCompanyRuc,txtCompanyVerification;
    EditText edtBillAmmount;
    Button btnStartDay,btnStartMonth,btnStartYear,btnEndDay,btnEndMonth,btnEndYear,btnNext;
    private String buyer_key,my_company_key, currentUserID, company_image, company_name, company_ruc, company_verification,main_currency,saveCurrentDate,saveCurrentTime;
    RadioButton rdCurrencyPen,rdCurrencyUsd;
    LinearLayout rootLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference clickPostRef;
    int current_day, current_month, current_year;

    ArrayList<String> days =new ArrayList<>();
    SpinnerDialog spinnerEndDays;

    ArrayList<String> months =new ArrayList<>();
    SpinnerDialog spinnerEndMonths;

    ArrayList<String> years =new ArrayList<>();
    SpinnerDialog spinnerEndYears;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyRuc = findViewById(R.id.txtCompanyRuc);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);

        edtBillAmmount = findViewById(R.id.edtBillAmmount);
        btnStartDay = findViewById(R.id.btnStartDay);
        btnStartMonth = findViewById(R.id.btnStartMonth);
        btnStartYear = findViewById(R.id.btnStartYear);
        btnEndDay = findViewById(R.id.btnEndDay);
        btnEndMonth = findViewById(R.id.btnEndMonth);
        btnEndYear = findViewById(R.id.btnEndYear);
        btnNext = findViewById(R.id.btnNext);
        rdCurrencyPen = findViewById(R.id.rdCurrencyPen);
        rdCurrencyUsd = findViewById(R.id.rdCurrencyUsd);
        rootLayout = findViewById(R.id.rootLayout);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        buyer_key = getIntent().getExtras().get("buyer_key").toString();
        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("My Companies").child(buyer_key);

        getDataDate();

        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    company_image = dataSnapshot.child("company_image").getValue().toString();
                    company_name = dataSnapshot.child("company_name").getValue().toString();
                    company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                    company_verification = dataSnapshot.child("company_verification").getValue().toString();

                    Picasso.with(CreateBillActivity.this).load(company_image).fit().into(imageCompany);
                    txtCompanyName.setText(company_name);
                    txtCompanyRuc.setText("RUC: "+company_ruc);
                    if (company_verification.equals("true"))
                    {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                        txtCompanyVerification.setText("Verificado Correctamente");
                    }
                    if (company_verification.equals("false"))
                    {
                        imageCompanyVerification.setImageResource(R.drawable.error_icon);
                        txtCompanyVerification.setText("Denegado");
                    }
                    else
                    {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        spinnerEndDays = new SpinnerDialog(CreateBillActivity.this,days,"Selecciona el día");
        spinnerEndDays.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndDay.setText(item);
                Toast.makeText(CreateBillActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndMonths.showSpinerDialog();
            }
        });

        spinnerEndMonths = new SpinnerDialog(CreateBillActivity.this,months,"Selecciona el mes");
        spinnerEndMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndMonth.setText(item);
                Toast.makeText(CreateBillActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndYears.showSpinerDialog();
            }
        });

        spinnerEndYears = new SpinnerDialog(CreateBillActivity.this,years,"Selecciona el año");
        spinnerEndYears.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndYear.setText(item);
                Toast.makeText(CreateBillActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });


        rdCurrencyPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_currency = "PEN";
            }
        });
        rdCurrencyUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_currency = "USD";
            }
        });

        //GetDate for the Issue Date of Bill:

        current_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        current_month = Calendar.getInstance().get(Calendar.MONTH)+1;
        current_year = Calendar.getInstance().get(Calendar.YEAR);

        String day = String.valueOf(current_day);
        String month = String.valueOf(current_month);
        String year = String.valueOf(current_year);

        if (month.equals("0")) {
            month = "1";
        }

        btnStartDay.setEnabled(false);
        btnStartDay.setText(day);
        btnStartMonth.setEnabled(false);
        btnStartMonth.setText(month);
        btnStartYear.setEnabled(false);
        btnStartYear.setText(year);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdCurrencyPen.isChecked() && !rdCurrencyUsd.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la moneda de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtBillAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto total de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnStartDay.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el día de emisión de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnStartMonth.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el mes de emisión de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnStartYear.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el año de emisión de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnEndDay.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el día de vencimiento de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnEndMonth.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el mes de vencimiento de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnEndYear.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el año de vencimiento de la factura", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    goToSumaryActivity();

                }

            }
        });
    }

    private void goToSumaryActivity() {
        Intent intent = new Intent(CreateBillActivity.this, BillSumaryActivity.class);
        intent.putExtra("buyer_key",buyer_key);
        intent.putExtra("my_company_key",my_company_key);
        intent.putExtra("customer_ruc",txtCompanyRuc.getText().toString());
        intent.putExtra("customer_name",txtCompanyName.getText().toString());
        intent.putExtra("main_currency",main_currency);
        intent.putExtra("bill_ammount",edtBillAmmount.getText().toString());
        intent.putExtra("start_day",btnStartDay.getText().toString());
        intent.putExtra("start_month",btnStartMonth.getText().toString());
        intent.putExtra("start_year",btnStartYear.getText().toString());
        intent.putExtra("end_day",btnEndDay.getText().toString());
        intent.putExtra("end_month",btnEndMonth.getText().toString());
        intent.putExtra("end_year",btnEndYear.getText().toString());
        startActivity(intent);
        finish();
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
