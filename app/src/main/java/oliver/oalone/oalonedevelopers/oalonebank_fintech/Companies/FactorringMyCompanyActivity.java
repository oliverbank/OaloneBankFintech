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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class FactorringMyCompanyActivity extends AppCompatActivity {

    ImageView imageCompany,imageCompanyVerification;
    TextView txtCompanyName,txtCompanyRuc,txtCompanyVerification,txtMinimunAnualInterestRate;
    EditText edtCustomerSocialReason,edtCustomerRuc,edtCustomerName,edtCustomerAddress,edtBillAmmount,edtAdvancedPayment,edtInterestRate;
    Button btnDepartment,btnProvince,btnDistrict,btnStartDay,btnStartMonth,btnStartYear,btnEndDay,btnEndMonth,btnEndYear,btnCalculateDiscount,btnNext;
    private String postKey, currentUserID, company_image, company_name, company_ruc, company_verification,main_currency,factoring_min_ammount,factoring_min_discount,
            saveCurrentDate, finishDate, startDate,factoring_resguard_rate,payment_method;
    private FirebaseAuth mAuth;
    private DatabaseReference clickPostRef, rates;
    RadioButton rdCurrencyPen,rdCurrencyUsd,rdOnePay,rdQuotesPay;
    RelativeLayout rootLayout;
    double min_ammount, min_rate, bill_amount, real_rate;
    long days_to__finance,bill_emition;

    ArrayList<String> department =new ArrayList<>();
    SpinnerDialog spinnerDepartment;

    ArrayList<String> province =new ArrayList<>();
    SpinnerDialog spinnerprovince;

    ArrayList<String> provinceCallao =new ArrayList<>();
    SpinnerDialog spinnerprovinceCallao;

    ArrayList<String> district_list =new ArrayList<>();
    SpinnerDialog spinnerdistrict_list;

    ArrayList<String> district_listBarranca =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listBarranca;

    ArrayList<String> district_listCajatambo =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCajatambo;

    ArrayList<String> district_listCañete =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCañete;

    ArrayList<String> district_listCanta =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCanta;

    ArrayList<String> district_listHuaral =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listHuaral;

    ArrayList<String> district_listHuarochirí =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listHuarochirí;

    ArrayList<String> district_listHuaura =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listHuaura;

    ArrayList<String> district_listOyon =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listOyon;

    ArrayList<String> district_listYauyos =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listYauyos;

    ArrayList<String> district_listCallao =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCallao;

    ArrayList<String> days =new ArrayList<>();
    SpinnerDialog spinnerDays;
    SpinnerDialog spinnerEndDays;

    ArrayList<String> months =new ArrayList<>();
    SpinnerDialog spinnerMonths;
    SpinnerDialog spinnerEndMonths;

    ArrayList<String> years =new ArrayList<>();
    SpinnerDialog spinnerYears;
    SpinnerDialog spinnerEndYears;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factorring_my_company);

        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyRuc = findViewById(R.id.txtCompanyRuc);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);

        txtMinimunAnualInterestRate = findViewById(R.id.txtMinimunAnualInterestRate);
        edtCustomerSocialReason = findViewById(R.id.edtCustomerSocialReason);
        edtCustomerRuc = findViewById(R.id.edtCustomerRuc);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtCustomerAddress = findViewById(R.id.edtCustomerAddress);
        edtBillAmmount = findViewById(R.id.edtBillAmmount);
        edtAdvancedPayment = findViewById(R.id.edtAdvancedPayment);
        edtInterestRate = findViewById(R.id.edtInterestRate);
        btnDepartment = findViewById(R.id.btnDepartment);
        btnProvince = findViewById(R.id.btnProvince);
        btnDistrict = findViewById(R.id.btnDistrict);
        btnStartDay = findViewById(R.id.btnStartDay);
        btnStartMonth = findViewById(R.id.btnStartMonth);
        btnStartYear = findViewById(R.id.btnStartYear);
        btnEndDay = findViewById(R.id.btnEndDay);
        btnEndMonth = findViewById(R.id.btnEndMonth);
        btnEndYear = findViewById(R.id.btnEndYear);
        btnCalculateDiscount = findViewById(R.id.btnCalculateDiscount);
        btnNext = findViewById(R.id.btnNext);
        rdCurrencyPen = findViewById(R.id.rdCurrencyPen);
        rdCurrencyUsd = findViewById(R.id.rdCurrencyUsd);
        rdOnePay = findViewById(R.id.rdOnePay);
        rdQuotesPay = findViewById(R.id.rdQuotesPay);
        rootLayout = findViewById(R.id.rootLayout);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("My Companies").child(postKey);
        rates = FirebaseDatabase.getInstance().getReference().child("Rates");

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

                    Picasso.with(FactorringMyCompanyActivity.this).load(company_image).fit().into(imageCompany);
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

        rates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                factoring_min_ammount  = dataSnapshot.child("factoring_min_ammount").getValue().toString();
                factoring_min_discount = dataSnapshot.child("factoring_min_discount").getValue().toString();
                factoring_resguard_rate = dataSnapshot.child("factoring_resguard_rate").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        department.add("Lima");department.add("Callao");

        btnDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDepartment.showSpinerDialog();
            }
        });

        spinnerDepartment = new SpinnerDialog(FactorringMyCompanyActivity.this,department,"Selecciona tu Departamento");
        spinnerDepartment.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDepartment.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        province.add("Lima");province.add("Barranca");province.add("Cajatambo");province.add("Cañete");province.add("Canta");province.add("Huaral");province.add("Huarochirí");
        province.add("Huaura");province.add("Oyón");province.add("Yauyos");

        provinceCallao.add("Callao");

        btnProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDistrict.setText("");
                if (btnDepartment.getText().toString().equals("Lima"))
                {
                    spinnerprovince.showSpinerDialog();
                }
                if (btnDepartment.getText().toString().equals("Callao"))
                {
                    spinnerprovinceCallao.showSpinerDialog();
                }
                else
                {
                    Snackbar.make(rootLayout, "Debes seleccionar un departamento primero", Snackbar.LENGTH_LONG).show();
                    return;
                }

            }
        });

        spinnerprovince = new SpinnerDialog(FactorringMyCompanyActivity.this,province,"Selecciona tu Provincia");
        spinnerprovince.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnProvince.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerprovinceCallao = new SpinnerDialog(FactorringMyCompanyActivity.this,provinceCallao,"Selecciona tu Provincia");
        spinnerprovinceCallao.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnProvince.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        district_list.add("Ancón");district_list.add("Ate");district_list.add("Barranco");district_list.add("Breña");district_list.add("Carabayllo");
        district_list.add("Chaclacayo");district_list.add("Chorrillos");district_list.add("Cieneguilla");district_list.add("Comas");
        district_list.add("El Agustino");district_list.add("Independencia");district_list.add("Jesús María");district_list.add("La Molina");
        district_list.add("La Victoria");district_list.add("Lima");district_list.add("Lince");district_list.add("Los Olivos");
        district_list.add("Lurigancho");district_list.add("Lurín");district_list.add("Magdalena del Mar");district_list.add("Miraflores");
        district_list.add("Pachacamac");district_list.add("Pucusana");district_list.add("Pueblo Libre");district_list.add("Puente Piedra");
        district_list.add("Punta Hermosa");district_list.add("Punta Negra");district_list.add("Rímac");district_list.add("San Bartolo");
        district_list.add("San Borja");district_list.add("San Isidro");district_list.add("San Juan de Lurigancho");district_list.add("San Juan de Miraflores");
        district_list.add("San Luis");district_list.add("San Martín de Porres");district_list.add("San Miguel");district_list.add("Santa Anita");
        district_list.add("Santa Rosa");district_list.add("Santiago de Surco");district_list.add("Surquillo");district_list.add("Villa El Salvador");
        district_list.add("Villa María del Triunfo");

        district_listBarranca.add("Barranca");district_listBarranca.add("Paramonga");district_listBarranca.add("Pativilca");district_listBarranca.add("Supe");
        district_listBarranca.add("Supe Puerto");

        district_listCajatambo.add("Cajatambo");district_listCajatambo.add("Copa");district_listCajatambo.add("Gorgor");
        district_listCajatambo.add("Huancapón");district_listCajatambo.add("Manás");

        district_listCañete.add("Asia");district_listCañete.add("Calango");district_listCañete.add("Cerro Azul");district_listCañete.add("Chilca");
        district_listCañete.add("Coayllo");district_listCañete.add("Imperial");district_listCañete.add("Lunahuaná");district_listCañete.add("Mala");
        district_listCañete.add("Nuevo Imperial");district_listCañete.add("Pacarán");district_listCañete.add("Quilmaná");district_listCañete.add("San Antonio");
        district_listCañete.add("San Luis");district_listCañete.add("San Vicente de Cañete");district_listCañete.add("Santa Cruz de Flores");

        district_listCanta.add("Arahuay"); district_listCanta.add("Canta"); district_listCanta.add("Huamatanga"); district_listCanta.add("Huaros");
        district_listCanta.add("Lachaqui");district_listCanta.add("San Buenaventura"); district_listCanta.add("Santa Rosa de Quives");

        district_listHuaral.add("Atavillos Alto");district_listHuaral.add("Atavillos Bajo");district_listHuaral.add("Aucallama");district_listHuaral.add("Chancay");
        district_listHuaral.add("Huaral");district_listHuaral.add("Ihuarí");district_listHuaral.add("Lampián");district_listHuaral.add("Pacaraos");
        district_listHuaral.add("San Miguel de Acos");district_listHuaral.add("Santa Cruz de Andamarca");district_listHuaral.add("Sumbilca");
        district_listHuaral.add("Veintisiete de Noviembre");

        district_listHuarochirí.add("Antioquía"); district_listHuarochirí.add("Callahuanca"); district_listHuarochirí.add("Carampoma"); district_listHuarochirí.add("Chicla");
        district_listHuarochirí.add("Cuenca");district_listHuarochirí.add("Huachupampa"); district_listHuarochirí.add("Huanza"); district_listHuarochirí.add("Huarochirí");
        district_listHuarochirí.add("Lahuaytambo");district_listHuarochirí.add("Langa");district_listHuarochirí.add("Mariatana"); district_listHuarochirí.add("Matucana");
        district_listHuarochirí.add("Ricardo Palma");district_listHuarochirí.add("San Andrés de Tupicocha"); district_listHuarochirí.add("San Antonio de Chaclla");
        district_listHuarochirí.add("San Bartolomé"); district_listHuarochirí.add("San Damián"); district_listHuarochirí.add("San Juan de Iris");
        district_listHuarochirí.add("San Juan de Tantaranche"); district_listHuarochirí.add("San Lorenzo de Quinti");district_listHuarochirí.add("San Mateo");
        district_listHuarochirí.add("San Mateo de Otao"); district_listHuarochirí.add("San Pedro de Casta");district_listHuarochirí.add("San Pedro de Huancayre");
        district_listHuarochirí.add("Sangallaya");district_listHuarochirí.add("Santa Cruz de Cocachacra"); district_listHuarochirí.add("Santa Eulalia");
        district_listHuarochirí.add("Santiago de Anchucaya");district_listHuarochirí.add("Santiago de Tuna"); district_listHuarochirí.add("Santo Domingo de los Olleros");
        district_listHuarochirí.add("Surco");

        district_listHuaura.add("Ámbar");district_listHuaura.add("Caleta de Carquín");district_listHuaura.add("Checras");
        district_listHuaura.add("Huacho");district_listHuaura.add("Hualmay");district_listHuaura.add("Huaura");district_listHuaura.add("Leoncio Prado");
        district_listHuaura.add("Paccho");district_listHuaura.add("Santa Leonor");district_listHuaura.add("Santa María");district_listHuaura.add("Sayán");
        district_listHuaura.add("Végueta");

        district_listOyon.add("Andajes"); district_listOyon.add("Caujul"); district_listOyon.add("Cochamarca"); district_listOyon.add("Naván");
        district_listOyon.add("Oyón"); district_listOyon.add("Pachangara");

        district_listYauyos.add("Alis");district_listYauyos.add("Ayauca");district_listYauyos.add("Ayaviri");district_listYauyos.add("Azángaro");district_listYauyos.add("Cacra");
        district_listYauyos.add("Carania");district_listYauyos.add("Catahuasi");district_listYauyos.add("Chocos");district_listYauyos.add("Cochas");district_listYauyos.add("Colonia");
        district_listYauyos.add("Hongos");district_listYauyos.add("Huampará");district_listYauyos.add("Huancaya");district_listYauyos.add("Huangáscar");district_listYauyos.add("Huañec");
        district_listYauyos.add("Huantán");district_listYauyos.add("Laraos");district_listYauyos.add("Lincha");district_listYauyos.add("Madeán");district_listYauyos.add("Miraflores");
        district_listYauyos.add("Omas");district_listYauyos.add("Putinza");district_listYauyos.add("Quinches");district_listYauyos.add("Quinocay");district_listYauyos.add("San Joaquín");
        district_listYauyos.add("San Pedro de Pilas");district_listYauyos.add("Tanta");district_listYauyos.add("Tauripampa");district_listYauyos.add("Tomas");district_listYauyos.add("Tupe");
        district_listYauyos.add("Viñac");district_listYauyos.add("Vitis");district_listYauyos.add("Yauyos");

        district_listCallao.add("Bellavista");district_listCallao.add("Callao");district_listCallao.add("Carmen de La Legua-Reynoso");district_listCallao.add("La Perla");
        district_listCallao.add("La Punta");district_listCallao.add("Mi Perú");district_listCallao.add("Ventanilla");

        btnDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnProvince.getText().toString().equals("Lima"))
                {
                    spinnerdistrict_list.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Barranca"))
                {
                    spinnerdistrict_listBarranca.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Cajatambo"))
                {
                    spinnerdistrict_listCajatambo.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Cañete"))
                {
                    spinnerdistrict_listCañete.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Canta"))
                {
                    spinnerdistrict_listCanta.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Huaral"))
                {
                    spinnerdistrict_listHuaral.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Huarochirí"))
                {
                    spinnerdistrict_listHuarochirí.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Huaura"))
                {
                    spinnerdistrict_listHuarochirí.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Oyón"))
                {
                    spinnerdistrict_listOyon.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Yauyos"))
                {
                    spinnerdistrict_listYauyos.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Callao"))
                {
                    spinnerdistrict_listCallao.showSpinerDialog();
                }
                if (TextUtils.isEmpty(btnProvince.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una provincia primero", Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
        });

        spinnerdistrict_list = new SpinnerDialog(FactorringMyCompanyActivity.this,district_list,"Selecciona tu Distrito");
        spinnerdistrict_list.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listBarranca = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listBarranca,"Selecciona tu Distrito");
        spinnerdistrict_listBarranca.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCajatambo = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listCajatambo,"Selecciona tu Distrito");
        spinnerdistrict_listCajatambo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCañete = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listCañete,"Selecciona tu Distrito");
        spinnerdistrict_listCañete.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCanta = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listCanta,"Selecciona tu Distrito");
        spinnerdistrict_listCanta.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listHuaral = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listHuaral,"Selecciona tu Distrito");
        spinnerdistrict_listHuaral.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listHuarochirí = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listHuarochirí,"Selecciona tu Distrito");
        spinnerdistrict_listHuarochirí.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listHuaura = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listHuaura,"Selecciona tu Distrito");
        spinnerdistrict_listHuaura.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listOyon = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listOyon,"Selecciona tu Distrito");
        spinnerdistrict_listOyon.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listYauyos = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listYauyos,"Selecciona tu Distrito");
        spinnerdistrict_listYauyos.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCallao = new SpinnerDialog(FactorringMyCompanyActivity.this,district_listCallao,"Selecciona tu Distrito");
        spinnerdistrict_listCallao.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        days.add("1"); days.add("2"); days.add("3"); days.add("4"); days.add("5"); days.add("6"); days.add("7"); days.add("8"); days.add("9"); days.add("10");
        days.add("11"); days.add("12"); days.add("13"); days.add("14"); days.add("15"); days.add("16"); days.add("17"); days.add("18"); days.add("19"); days.add("20");
        days.add("21"); days.add("22"); days.add("23"); days.add("24"); days.add("25"); days.add("26"); days.add("27"); days.add("28"); days.add("29"); days.add("30");
        days.add("31");

        months.add("1");months.add("2");months.add("3");months.add("4");months.add("5");months.add("6");months.add("7");months.add("8");months.add("9");months.add("10");
        months.add("11");months.add("12");

        years.add("1990"); years.add("1991"); years.add("1992"); years.add("1993"); years.add("1994"); years.add("1995"); years.add("1996"); years.add("1997"); years.add("1998");
        years.add("1999"); years.add("2000");years.add("2001");years.add("2002");years.add("2003");years.add("2004");years.add("2005");years.add("2006");years.add("2007");
        years.add("2008");years.add("2009");years.add("2010");years.add("2011");years.add("2012");years.add("2013");years.add("2014");years.add("2015");years.add("2016");
        years.add("2017");years.add("2018");years.add("2019");years.add("2020");years.add("2021");years.add("2022");years.add("2023");years.add("2024");years.add("2025");
        years.add("2026");years.add("2027");years.add("2028");years.add("2029");years.add("2030");years.add("2031");years.add("2032");years.add("2033");years.add("2034");
        years.add("2035");years.add("2036");years.add("2037");years.add("2038");years.add("2039");years.add("2040");

        btnStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDays.showSpinerDialog();
            }
        });

        spinnerDays = new SpinnerDialog(FactorringMyCompanyActivity.this,days,"Selecciona el día");
        spinnerDays.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnStartDay.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnStartMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerMonths.showSpinerDialog();
            }
        });

        spinnerMonths = new SpinnerDialog(FactorringMyCompanyActivity.this,months,"Selecciona el mes");
        spinnerMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnStartMonth.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnStartYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerYears.showSpinerDialog();
            }
        });

        spinnerYears = new SpinnerDialog(FactorringMyCompanyActivity.this,years,"Selecciona el año");
        spinnerYears.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnStartYear.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndDays.showSpinerDialog();
            }
        });

        spinnerEndDays = new SpinnerDialog(FactorringMyCompanyActivity.this,days,"Selecciona el día");
        spinnerEndDays.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndDay.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndMonths.showSpinerDialog();
            }
        });

        spinnerEndMonths = new SpinnerDialog(FactorringMyCompanyActivity.this,months,"Selecciona el mes");
        spinnerEndMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndMonth.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndYears.showSpinerDialog();
            }
        });

        spinnerEndYears = new SpinnerDialog(FactorringMyCompanyActivity.this,years,"Selecciona el año");
        spinnerEndYears.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndYear.setText(item);
                Toast.makeText(FactorringMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
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

        btnCalculateDiscount.setOnClickListener(new View.OnClickListener() {
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
                min_ammount = Double.parseDouble(factoring_min_ammount);
                bill_amount = Double.parseDouble(edtBillAmmount.getText().toString());
                if (bill_amount < min_ammount)
                {
                    Snackbar.make(rootLayout, "El importe total de la factura debe ser mayor a "+factoring_min_ammount+" "+main_currency, Snackbar.LENGTH_LONG).show();
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
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                saveCurrentDate =currentDate.format(calForDate.getTime());
                startDate = btnStartDay.getText().toString()+" "+btnStartMonth.getText().toString()+" "+btnStartYear.getText().toString();
                try {
                    Date date1 = currentDate.parse(saveCurrentDate);
                    Date date2 = currentDate.parse(startDate);
                    long diff = date2.getTime() - date1.getTime();
                    bill_emition = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (bill_emition > 0)
                {
                    Snackbar.make(rootLayout, "Esta factura tiene una fecha de emisión incorrecta", Snackbar.LENGTH_LONG).show();
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
                Calendar calForDate3 = Calendar.getInstance();
                SimpleDateFormat currentDate3 = new SimpleDateFormat("dd MM yyyy");
                saveCurrentDate =currentDate3.format(calForDate3.getTime());
                finishDate = btnEndDay.getText().toString()+" "+btnEndMonth.getText().toString()+" "+btnEndYear.getText().toString();
                try {
                    Date date1 = currentDate.parse(saveCurrentDate);
                    Date date2 = currentDate.parse(finishDate);
                    long diff = date2.getTime() - date1.getTime();
                    days_to__finance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (days_to__finance < 0)
                {
                    Snackbar.make(rootLayout, "Tu factura está vencida", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdOnePay.isChecked() && !rdQuotesPay.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar el modo de pago", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAdvancedPayment.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto del pago por adelantado de tu cliente", Snackbar.LENGTH_LONG).show();
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
                else
                {
                    showDiscountDialog();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtCustomerSocialReason.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar la razón social del cliente", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCustomerRuc.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el RUC del cliente", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCustomerName.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el nombre completo del cliente", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnDepartment.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar el departamento o región del cliente", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnProvince.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la provincia del cliente", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnDistrict.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar el distrito del cliente", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCustomerAddress.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar la dirección del cliente del cliente", Snackbar.LENGTH_LONG).show();
                    return;
                }
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
                min_ammount = Double.parseDouble(factoring_min_ammount);
                bill_amount = Double.parseDouble(edtBillAmmount.getText().toString());
                if (bill_amount < min_ammount)
                {
                    Snackbar.make(rootLayout, "El importe total de la factura debe ser mayor a "+factoring_min_ammount+" "+main_currency, Snackbar.LENGTH_LONG).show();
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
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                saveCurrentDate =currentDate.format(calForDate.getTime());
                startDate = btnStartDay.getText().toString()+" "+btnStartMonth.getText().toString()+" "+btnStartYear.getText().toString();
                try {
                    Date date1 = currentDate.parse(saveCurrentDate);
                    Date date2 = currentDate.parse(startDate);
                    long diff = date2.getTime() - date1.getTime();
                    bill_emition = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (bill_emition > 0)
                {
                    Snackbar.make(rootLayout, "Esta factura tiene una fecha de emisión incorrecta", Snackbar.LENGTH_LONG).show();
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
                Calendar calForDate4 = Calendar.getInstance();
                SimpleDateFormat currentDate4 = new SimpleDateFormat("dd MM yyyy");
                saveCurrentDate =currentDate4.format(calForDate4.getTime());
                finishDate = btnEndDay.getText().toString()+" "+btnEndMonth.getText().toString()+" "+btnEndYear.getText().toString();
                try {
                    Date date1 = currentDate.parse(saveCurrentDate);
                    Date date2 = currentDate.parse(finishDate);
                    long diff = date2.getTime() - date1.getTime();
                    days_to__finance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (days_to__finance < 0)
                {
                    Snackbar.make(rootLayout, "Tu factura está vencida", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdOnePay.isChecked() && !rdQuotesPay.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar el modo de pago", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAdvancedPayment.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto del pago por adelantado de tu cliente", Snackbar.LENGTH_LONG).show();
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
                else
                {
                    goToSumaryActivity();

                }

            }
        });
    }

    private void goToSumaryActivity() {
        Intent intent = new Intent(FactorringMyCompanyActivity.this,FactoringMyCompanySumaryActivity.class);
        intent.putExtra("PostKey",getIntent().getExtras().get("PostKey").toString());
        intent.putExtra("customer_social_reason",edtCustomerSocialReason.getText().toString());
        intent.putExtra("customer_ruc",edtCustomerRuc.getText().toString());
        intent.putExtra("customer_name",edtCustomerName.getText().toString());
        intent.putExtra("customer_address",btnDepartment.getText().toString()+", "+btnProvince.getText().toString()+", "+btnDistrict.getText().toString()+", "+edtCustomerAddress.getText().toString());
        intent.putExtra("main_currency",main_currency);
        intent.putExtra("bill_ammount",edtBillAmmount.getText().toString());
        intent.putExtra("start_day",btnStartDay.getText().toString());
        intent.putExtra("start_month",btnStartMonth.getText().toString());
        intent.putExtra("start_year",btnStartYear.getText().toString());
        intent.putExtra("end_day",btnEndDay.getText().toString());
        intent.putExtra("end_month",btnEndMonth.getText().toString());
        intent.putExtra("end_year",btnEndYear.getText().toString());
        if (rdOnePay.isChecked())
        {
            payment_method = "1";
        }
        if (rdQuotesPay.isChecked())
        {
            payment_method = "2";
        }
        intent.putExtra("payment_method",payment_method);
        intent.putExtra("payment_in_advance",edtAdvancedPayment.getText().toString());
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
        finishDate = btnEndDay.getText().toString()+" "+btnEndMonth.getText().toString()+" "+btnEndYear.getText().toString();

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

        double pay_in_advance = Double.parseDouble(edtAdvancedPayment.getText().toString());

        double discount_ammount_before = bill_amount-pay_in_advance;
        double discount_ammount = discount_ammount_before*periodical_discount_rate;

        double resguard_rate = Double.parseDouble(factoring_resguard_rate);
        double resguard_ammount = (resguard_rate/100)*(bill_amount-pay_in_advance);

        double factoring_ammount = bill_amount-pay_in_advance-discount_ammount-resguard_ammount;

        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        String monthly_rate_df = decimalFormat.format(monthly_rate_express);
        String periodical_rate_df = decimalFormat.format(periodical_discount_rate_express);
        DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
        String discount_ammount_df = decimalFormat2.format(discount_ammount);
        String resguard_ammount_df = decimalFormat2.format(resguard_ammount);
        String factoring_ammount_df = decimalFormat2.format(factoring_ammount);

        txtAmmount.setText("Importe de la factura: "+edtBillAmmount.getText().toString()+" "+main_currency);
        txtAnualInterestRate.setText("Tasa de interés efectiva anual: "+edtInterestRate.getText().toString()+"%");
        Calendar calForDate2 = Calendar.getInstance();
        SimpleDateFormat currentDate2 = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate2 =currentDate2.format(calForDate2.getTime());
        txtCurrentDate.setText("Fecha actual: "+saveCurrentDate2);
        txtFinishDate.setText("Vencimiento de la factura: "+btnEndDay.getText().toString()+"-"+btnEndMonth.getText().toString()+"-"+btnEndYear.getText().toString());
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
