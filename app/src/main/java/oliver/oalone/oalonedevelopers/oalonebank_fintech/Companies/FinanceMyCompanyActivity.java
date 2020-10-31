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
import android.view.ViewGroup;
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
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class FinanceMyCompanyActivity extends AppCompatActivity {

    EditText edtFixedAssetName,edtWorkingCapitalDescription,edtExportDescription,edtImportDescription,edtAmmount,edtInterestRate;
    RadioButton rdFixedAssetAcquisition,rdWorkingCapital,rdExportProject,rdImportProject,rdCurrencyPen,rdCurrencyUsd;
    LinearLayout assetAcquisitionLayout,workingCapitalLayout,exportProjectLayout,importProjectLayout;
    ImageView imageCompany,imageCompanyVerification;
    TextView txtCompanyName,txtCompanyRuc,txtCompanyVerification,txtFinanceDescription,txtMinimunAnualInterestRate;
    CountryCodePicker ccpDestinationCountry,ccpOriginalCountry;
    Button btnFixedAssetType, btnFixedAssetMode,btnWorkingCapitalType,btnExportProjectMoment,btnExportTransport,btnExportPayment,btnimportProjectMoment,btnimportTransport,
            btnimportPayment,btnMonths,btnPaymentFrencuency,btnCalculateAmortization,btnWarranties,btnMorgageType,btnNext,btnGracePeriod;
    RelativeLayout rootLayout;
    private String postKey, currentUserID, company_image, company_name, company_ruc, company_verification, main_currency,min_interest_rate;
    private FirebaseAuth mAuth;
    private DatabaseReference clickPostRef, rates;
    double interest_rate;
    double minimun_interest_rate;
    Button btnEndDay,btnEndMonth,btnEndYear;

    ArrayList<String> fixedAssetType =new ArrayList<>();
    SpinnerDialog spinnerfixedAssetType;

    ArrayList<String> fixedAssetMode =new ArrayList<>();
    SpinnerDialog spinnerfixedAssetMode;

    ArrayList<String> workingCapitalType =new ArrayList<>();
    SpinnerDialog spinnerworkingCapitalType;

    ArrayList<String> exportMoment =new ArrayList<>();
    SpinnerDialog spinnerExportMoment;

    ArrayList<String> exportTransport =new ArrayList<>();
    SpinnerDialog spinnerExportTransport;

    ArrayList<String> exportPayment =new ArrayList<>();
    SpinnerDialog spinnerExportPayment;

    SpinnerDialog spinnerImportMoment,spinnerImportTransport, spinnerImportPayment;

    ArrayList<String> months =new ArrayList<>();
    SpinnerDialog spinnerMonths;

    ArrayList<String> frecuency =new ArrayList<>();
    SpinnerDialog spinnerFrecuency;

    ArrayList<String> gracePeriod =new ArrayList<>();
    SpinnerDialog spinnerGracePeriod;

    ArrayList<String> warranties =new ArrayList<>();
    SpinnerDialog spinnerWarranties;

    ArrayList<String> morgageType =new ArrayList<>();
    SpinnerDialog spinnermorgageType;

    ArrayList<String> days =new ArrayList<>();
    SpinnerDialog spinnerEndDays;

    ArrayList<String> monthss =new ArrayList<>();
    SpinnerDialog spinnerEndMonths;

    ArrayList<String> years =new ArrayList<>();
    SpinnerDialog spinnerEndYears;

    ArrayList<String> graceMonths =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_my_company);

        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyRuc = findViewById(R.id.txtCompanyRuc);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);
        rdFixedAssetAcquisition = findViewById(R.id.rdFixedAssetAcquisition);
        rdWorkingCapital = findViewById(R.id.rdWorkingCapital);
        rdExportProject = findViewById(R.id.rdExportProject);
        rdImportProject = findViewById(R.id.rdImportProject);
        assetAcquisitionLayout = findViewById(R.id.assetAcquisitionLayout);
        workingCapitalLayout = findViewById(R.id.workingCapitalLayout);
        exportProjectLayout = findViewById(R.id.exportProjectLayout);
        importProjectLayout = findViewById(R.id.importProjectLayout);
        txtFinanceDescription = findViewById(R.id.txtFinanceDescription);
        edtFixedAssetName = findViewById(R.id.edtFixedAssetName);
        btnFixedAssetMode = findViewById(R.id.btnFixedAssetMode);
        btnWorkingCapitalType = findViewById(R.id.btnWorkingCapitalType);
        edtWorkingCapitalDescription = findViewById(R.id.edtWorkingCapitalDescription);
        btnExportProjectMoment = findViewById(R.id.btnExportProjectMoment);
        btnExportTransport = findViewById(R.id.btnExportTransport);
        btnExportPayment = findViewById(R.id.btnExportPayment);
        edtExportDescription = findViewById(R.id.edtExportDescription);
        ccpDestinationCountry = findViewById(R.id.ccpDestinationCountry);
        btnimportProjectMoment = findViewById(R.id.btnimportProjectMoment);
        btnimportTransport = findViewById(R.id.btnimportTransport);
        btnimportPayment = findViewById(R.id.btnimportPayment);
        edtImportDescription = findViewById(R.id.edtImportDescription);
        ccpOriginalCountry = findViewById(R.id.ccpOriginalCountry);
        rdCurrencyPen = findViewById(R.id.rdCurrencyPen);
        rdCurrencyUsd = findViewById(R.id.rdCurrencyUsd);
        edtAmmount = findViewById(R.id.edtAmmount);
        edtInterestRate = findViewById(R.id.edtInterestRate);
        btnMonths = findViewById(R.id.btnMonths);
        btnPaymentFrencuency = findViewById(R.id.btnPaymentFrencuency);
        btnGracePeriod = findViewById(R.id.btnGracePeriod);
        btnCalculateAmortization = findViewById(R.id.btnCalculateAmortization);
        btnNext  = findViewById(R.id.btnNext);
        txtMinimunAnualInterestRate = findViewById(R.id.txtMinimunAnualInterestRate);
        btnEndDay = findViewById(R.id.btnEndDay);
        btnEndMonth = findViewById(R.id.btnEndMonth);
        btnEndYear = findViewById(R.id.btnEndYear);
        rootLayout = findViewById(R.id.rootLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("My Companies").child(postKey);
        rates = FirebaseDatabase.getInstance().getReference().child("Rates");

        getDataDate();

        days.add("1"); days.add("2"); days.add("3"); days.add("4"); days.add("5"); days.add("6"); days.add("7"); days.add("8"); days.add("9"); days.add("10");
        days.add("11"); days.add("12"); days.add("13"); days.add("14"); days.add("15"); days.add("16"); days.add("17"); days.add("18");

        monthss.add("1");monthss.add("2");monthss.add("3");monthss.add("4");monthss.add("5");monthss.add("6");monthss.add("7");monthss.add("8");monthss.add("9");monthss.add("10");
        monthss.add("11");monthss.add("12");

       years.add("2020");years.add("2021");years.add("2022");years.add("2023");years.add("2024");years.add("2025");
        years.add("2026");years.add("2027");years.add("2028");years.add("2029");years.add("2030");years.add("2031");years.add("2032");years.add("2033");years.add("2034");
        years.add("2035");years.add("2036");years.add("2037");years.add("2038");years.add("2039");years.add("2040");

        btnEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndDays.showSpinerDialog();
            }
        });

        spinnerEndDays = new SpinnerDialog(FinanceMyCompanyActivity.this,days,"Selecciona el día");
        spinnerEndDays.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndDay.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndMonths.showSpinerDialog();
            }
        });

        spinnerEndMonths = new SpinnerDialog(FinanceMyCompanyActivity.this,monthss,"Selecciona el mes");
        spinnerEndMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndMonth.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnEndYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerEndYears.showSpinerDialog();
            }
        });

        spinnerEndYears = new SpinnerDialog(FinanceMyCompanyActivity.this,years,"Selecciona el año");
        spinnerEndYears.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnEndYear.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });


        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    company_image = dataSnapshot.child("company_image").getValue().toString();
                    company_name = dataSnapshot.child("company_name").getValue().toString();
                    company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                    company_verification = dataSnapshot.child("company_verification").getValue().toString();

                    Picasso.with(FinanceMyCompanyActivity.this).load(company_image).fit().centerCrop().into(imageCompany);
                    txtCompanyName.setText(company_name);
                    txtCompanyRuc.setText("RUC: "+company_ruc);
                    if (company_verification.equals("true"))
                    {
                        //imageCompanyVerification.setImageResource(R.drawable.verification_succesfull);
                    }
                    if (company_verification.equals("false"))
                    {
                        //imageCompanyVerification.setImageResource(R.drawable.verification_error);
                    }
                    else
                    {
                        //imageCompanyVerification.setImageResource(R.drawable.verification_in_progress);
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
                min_interest_rate = dataSnapshot.child("min_active_rate").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        assetAcquisitionLayout.getLayoutParams().height = 0;
        workingCapitalLayout.getLayoutParams().height = 0;
        exportProjectLayout.getLayoutParams().height = 0;
        importProjectLayout.getLayoutParams().height = 0;

        btnFixedAssetType = findViewById(R.id.btnFixedAssetType);

        fixedAssetType.add("Terreno");fixedAssetType.add("Construcciones");fixedAssetType.add("Instalaciones");fixedAssetType.add("Maquinaria");
        fixedAssetType.add("Mobiliario");fixedAssetType.add("Equipos para procesos informáticos");fixedAssetType.add("Vehículos para transporte");
        fixedAssetType.add("Otros");fixedAssetType.add("Marca");fixedAssetType.add("Patente");fixedAssetType.add("Derecho de autor");
        fixedAssetType.add("Licencias o permisos");

        btnFixedAssetType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerfixedAssetType.showSpinerDialog();
            }
        });

        spinnerfixedAssetType = new SpinnerDialog(FinanceMyCompanyActivity.this,fixedAssetType,"Selecciona el Activo fijo");
        spinnerfixedAssetType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnFixedAssetType.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        fixedAssetMode.add("Leasing");fixedAssetMode.add("Leaseback");fixedAssetMode.add("Compra definitiva");
        btnFixedAssetMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerfixedAssetMode.showSpinerDialog();
            }
        });

        spinnerfixedAssetMode = new SpinnerDialog(FinanceMyCompanyActivity.this,fixedAssetMode,"Selecciona el método de financiamiento");
        spinnerfixedAssetMode.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnFixedAssetMode.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        workingCapitalType.add("Inventarios"); workingCapitalType.add("Planillas"); workingCapitalType.add("Otros Pasivos");
        btnWorkingCapitalType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerworkingCapitalType.showSpinerDialog();
            }
        });

        spinnerworkingCapitalType = new SpinnerDialog(FinanceMyCompanyActivity.this,workingCapitalType,"Selecciona el destino del capital");
        spinnerworkingCapitalType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnWorkingCapitalType.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        exportMoment.add("Producción");exportMoment.add("Embase y embalaje en origen");exportMoment.add("Transporte Interno en origen");exportMoment.add("Almacenamiento Interno en origen");
        exportMoment.add("Flete internacional");exportMoment.add("Almacenamiento en país de destino");exportMoment.add("Transporte interno en país de destino");

        btnExportProjectMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerExportMoment.showSpinerDialog();
            }
        });
        spinnerExportMoment = new SpinnerDialog(FinanceMyCompanyActivity.this,exportMoment,"Selecciona la etapa del financiamiento");
        spinnerExportMoment.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnExportProjectMoment.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        exportTransport.add("Aéreo");exportTransport.add("Marítimo");exportTransport.add("Terrestre");exportTransport.add("Mixto");
        btnExportTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerExportTransport.showSpinerDialog();
            }
        });

        spinnerExportTransport = new SpinnerDialog(FinanceMyCompanyActivity.this,exportTransport,"Selecciona el medio de transporte");
        spinnerExportTransport.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnExportTransport.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        exportPayment.add("Transferencia internacional");exportPayment.add("Cobranza siimple de exportación");exportPayment.add("Cobranza documentaria");
        exportPayment.add("Carta de crédito");exportPayment.add("Otro");
        btnExportPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerExportPayment.showSpinerDialog();
            }
        });

        spinnerExportPayment = new SpinnerDialog(FinanceMyCompanyActivity.this,exportPayment,"Selecciona el medio de pago");
        spinnerExportPayment.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnExportPayment.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnimportProjectMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerImportMoment.showSpinerDialog();
            }
        });
        spinnerImportMoment = new SpinnerDialog(FinanceMyCompanyActivity.this,exportMoment,"Selecciona la etapa del financiamiento");
        spinnerImportMoment.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnimportProjectMoment.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnimportTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerImportTransport.showSpinerDialog();
            }
        });
        spinnerImportTransport = new SpinnerDialog(FinanceMyCompanyActivity.this,exportTransport,"Selecciona el medio de transporte");
        spinnerImportTransport.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnimportTransport.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnimportPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerImportPayment.showSpinerDialog();
            }
        });
        spinnerImportPayment = new SpinnerDialog(FinanceMyCompanyActivity.this,exportPayment,"Selecciona el medio de pago");
        spinnerImportPayment.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnimportPayment.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        months.add("1");months.add("2");months.add("3"); months.add("4");months.add("5");months.add("6"); months.add("7");months.add("8");months.add("9");months.add("10");
        months.add("11");months.add("12");months.add("13"); months.add("14");months.add("15");months.add("16"); months.add("17");months.add("18");months.add("19");months.add("20");
        months.add("21");months.add("22");months.add("23"); months.add("24");months.add("25");months.add("26"); months.add("27");months.add("28");months.add("29");months.add("30");
        months.add("31");months.add("32");months.add("33"); months.add("34");months.add("35");months.add("36"); months.add("37");months.add("38");months.add("39");months.add("40");
        months.add("41");months.add("42");months.add("43"); months.add("44");months.add("45");months.add("46"); months.add("47");months.add("48");
        btnMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerMonths.showSpinerDialog();
            }
        });

        spinnerMonths = new SpinnerDialog(FinanceMyCompanyActivity.this,months,"Selecciona el número de meses para el financiamiento");
        spinnerMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnMonths.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        graceMonths.add("1");graceMonths.add("2");graceMonths.add("3");

        btnGracePeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerGracePeriod.showSpinerDialog();
            }
        });
        spinnerGracePeriod = new SpinnerDialog(FinanceMyCompanyActivity.this,graceMonths,"Selecciona el número de meses para el período de gracia");
        spinnerGracePeriod.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnGracePeriod.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });
        
        frecuency.add("Mensual");frecuency.add("Cada 2 meses");frecuency.add("Cada 3 meses");
        btnPaymentFrencuency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerFrecuency.showSpinerDialog();
            }
        });

        spinnerFrecuency = new SpinnerDialog(FinanceMyCompanyActivity.this,frecuency,"Selecciona la frecuencia de pago");
        spinnerFrecuency.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnPaymentFrencuency.setText(item);
                Toast.makeText(FinanceMyCompanyActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });
        
        rdFixedAssetAcquisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdFixedAssetAcquisition.setChecked(true);
                rdWorkingCapital.setChecked(false);
                rdExportProject.setChecked(false);
                rdImportProject.setChecked(false);
                assetAcquisitionLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                workingCapitalLayout.getLayoutParams().height = 0;
                exportProjectLayout.getLayoutParams().height = 0;
                importProjectLayout.getLayoutParams().height = 0;
                txtFinanceDescription.setBackgroundResource(R.drawable.edit_text_background1);
                txtFinanceDescription.setText("Adquiere activos fijos para tu negocio mediante leasing o leaseback, en esta categoría podrás solicitar financiamiento para la adquisión de maquinaria y equipo, inmuebles, terrenos, entre otros.");
            }
        });
        rdWorkingCapital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdFixedAssetAcquisition.setChecked(false);
                rdWorkingCapital.setChecked(true);
                rdExportProject.setChecked(false);
                rdImportProject.setChecked(false);
                workingCapitalLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                assetAcquisitionLayout.getLayoutParams().height = 0;
                exportProjectLayout.getLayoutParams().height = 0;
                importProjectLayout.getLayoutParams().height = 0;
                txtFinanceDescription.setBackgroundResource(R.drawable.edit_text_background1);
                txtFinanceDescription.setText("Obtén el capital de trabajo que necesitas para financiar tus operaciones a corto plazo u operaciones relacionadas a la proucción de bienes o servicios");
            }
        });
        rdExportProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdFixedAssetAcquisition.setChecked(false);
                rdWorkingCapital.setChecked(false);
                rdExportProject.setChecked(true);
                rdImportProject.setChecked(false);
                exportProjectLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                assetAcquisitionLayout.getLayoutParams().height = 0;
                workingCapitalLayout.getLayoutParams().height = 0;
                importProjectLayout.getLayoutParams().height = 0;
                txtFinanceDescription.setBackgroundResource(R.drawable.edit_text_background1);
                txtFinanceDescription.setText("Financia parte de tu proyecto de exportación ya sea antes o despues del embarque");
            }
        });
        rdImportProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdFixedAssetAcquisition.setChecked(false);
                rdWorkingCapital.setChecked(false);
                rdExportProject.setChecked(false);
                rdImportProject.setChecked(true);
                importProjectLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                assetAcquisitionLayout.getLayoutParams().height = 0;
                workingCapitalLayout.getLayoutParams().height = 0;
                exportProjectLayout.getLayoutParams().height = 0;
                txtFinanceDescription.setBackgroundResource(R.drawable.edit_text_background1);
                txtFinanceDescription.setText("Financia parte de tu proyecto de importación ya sea antes o despues del embarque");
            }
        });
        
        btnCalculateAmortization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdCurrencyPen.isChecked() && !rdCurrencyUsd.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una moneda del monto a financiar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a financiar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar la tasa de interés anual para el financiamiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnMonths.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar los meses que durará el financiamiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que realizarás los pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                interest_rate = Double.parseDouble(edtInterestRate.getText().toString());
                minimun_interest_rate = Double.parseDouble(min_interest_rate);
                if (interest_rate < minimun_interest_rate)
                {
                    txtMinimunAnualInterestRate.setText("TREA mínima: "+minimun_interest_rate+"%");
                    Snackbar.make(rootLayout, "La tasa debe ser mayor a la TREA mínima: "+minimun_interest_rate+"%", Snackbar.LENGTH_LONG).show();
                    return;
                }
                showAmortizationDialog();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
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

    }

    private void checkValidation() {
        if (!rdFixedAssetAcquisition.isChecked() && !rdWorkingCapital.isChecked() && !rdExportProject.isChecked() && !rdImportProject.isChecked())
        {
            Snackbar.make(rootLayout, "Debes seleccionar el destino del financiamiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (rdFixedAssetAcquisition.isChecked())
        {
            if (TextUtils.isEmpty(btnFixedAssetType.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar el activo fijo", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(edtFixedAssetName.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes especificar el nombre del activo fijo", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(btnFixedAssetMode.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes indicar la modalidad de financiamiento del activo fijo", Snackbar.LENGTH_LONG).show();
                return;
            }
        }
        if (rdWorkingCapital.isChecked())
        {
            if (TextUtils.isEmpty(btnWorkingCapitalType.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar el destino del capital de trabajo", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(edtWorkingCapitalDescription.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes ingresar una descripción del capital de trabajo", Snackbar.LENGTH_LONG).show();
                return;
            }
        }
        if (rdExportProject.isChecked())
        {
            if (TextUtils.isEmpty(btnExportProjectMoment.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar una etapa a financiar para el proyecto de exportación", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(btnExportTransport.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar un medio de transporte para el proyecto de exportación", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(btnExportPayment.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar un medio de pago para el proyecto de exportación", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(edtExportDescription.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes ingresar una descripción del proyecto de exportación", Snackbar.LENGTH_LONG).show();
                return;
            }
        }
        if (rdImportProject.isChecked())
        {
            if (TextUtils.isEmpty(btnimportProjectMoment.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar una etapa a financiar para el proyecto de importación", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(btnimportTransport.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar un medio de transporte para el proyecto de importación", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(btnimportPayment.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes seleccionar un medio de pago para el proyecto de importación", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(edtImportDescription.getText().toString()))
            {
                Snackbar.make(rootLayout, "Debes ingresar una descripción del proyecto de importación", Snackbar.LENGTH_LONG).show();
                return;
            }
        }
        if (!rdCurrencyPen.isChecked() && !rdCurrencyUsd.isChecked())
        {
            Snackbar.make(rootLayout, "Debes seleccionar una moneda del monto a financiar", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtAmmount.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el monto a financiar", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes especificar la tasa de interés anual para el financiamiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnMonths.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes especificar los meses que durará el financiamiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que realizarás los pagos", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnGracePeriod.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes especificar los días para el período de gracia", Snackbar.LENGTH_LONG).show();
            return;
        }
        interest_rate = Double.parseDouble(edtInterestRate.getText().toString());
        minimun_interest_rate = Double.parseDouble(min_interest_rate);
        if (interest_rate < minimun_interest_rate)
        {
            txtMinimunAnualInterestRate.setText("TREA mínima: "+minimun_interest_rate+"%");
            Snackbar.make(rootLayout, "La tasa debe ser mayor a la TREA mínima: "+minimun_interest_rate+"%", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnEndDay.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el día del vencimiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnEndMonth.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el mes del vencimiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnEndYear.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el año del vencimiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        else
        {
            sendInformationToSumaryActivity();
        }
    }

    private void sendInformationToSumaryActivity() {
        double ammount = Double.parseDouble(edtAmmount.getText().toString());
        double rate = Double.parseDouble(edtInterestRate.getText().toString());
        double months = Double.parseDouble(btnMonths.getText().toString());
        String frecuency = btnPaymentFrencuency.getText().toString();

        //Tasa efectiva mensual:
        double rate_perone = rate/100;
        double monthly_rate_base = 1+rate_perone;
        double monthly_rate_exp = 0.08333333;
        double monthly_rate = Math.pow(monthly_rate_base,monthly_rate_exp)-1;
        double monthly_rate_express = monthly_rate*100;

        double total_debt_base = (1+monthly_rate);
        double total_debt_exp = months;
        double total_debt = Math.pow(total_debt_base,total_debt_exp)*ammount;
        double monthly_quote = total_debt/months;
        double cost_of_debt = total_debt-ammount;

        double quote = 1.00;
        if (frecuency.equals("Mensual"))
        {
            quote = monthly_quote*1;
        }
        if (frecuency.equals("Cada 2 meses"))
        {
            quote = monthly_quote*2;
        }
        if (frecuency.equals("Cada 3 meses"))
        {
            quote = monthly_quote*3;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String monthly_rate_df = decimalFormat.format(monthly_rate_express);
        String quote_df = decimalFormat.format(quote);
        String total_debt_df = decimalFormat.format(total_debt);
        String cost_of_debt_df = decimalFormat.format(cost_of_debt);

        Intent intent = new Intent(FinanceMyCompanyActivity.this,FinancingMyCompanySumaryActivity.class);
        intent.putExtra("PostKey",getIntent().getExtras().get("PostKey").toString());
        String financing_destination = "";
        if (rdFixedAssetAcquisition.isChecked())
        {
            financing_destination = "Adquisición de activos fijos";
        }
        if (rdWorkingCapital.isChecked())
        {
            financing_destination = "Capital de trabajo opearativo";
        }
        if (rdExportProject.isChecked())
        {
            financing_destination = "Proyecto de exportación";
        }
        if (rdImportProject.isChecked())
        {
            financing_destination = "Proyecto de importación";
        }
        intent.putExtra("finance_destiny",financing_destination);
        intent.putExtra("fixed_asset_type",btnFixedAssetType.getText().toString());
        intent.putExtra("fixed_asset_name",edtFixedAssetName.getText().toString());
        intent.putExtra("fixed_asset_mode",btnFixedAssetMode.getText().toString());
        intent.putExtra("working_capital_type",btnWorkingCapitalType.getText().toString());
        intent.putExtra("working_capital_description",edtWorkingCapitalDescription.getText().toString());
        intent.putExtra("export_porject_moment",btnExportProjectMoment.getText().toString());
        intent.putExtra("export_porject_transport",btnExportTransport.getText().toString());
        intent.putExtra("export_porject_payment",btnExportTransport.getText().toString());
        intent.putExtra("export_porject_description",edtExportDescription.getText().toString());
        intent.putExtra("export_porject_country",ccpDestinationCountry.getSelectedCountryName().toString());
        intent.putExtra("import_porject_moment",btnimportProjectMoment.getText().toString());
        intent.putExtra("import_porject_transport",btnimportTransport.getText().toString());
        intent.putExtra("import_porject_payment",btnimportPayment.getText().toString());
        intent.putExtra("import_porject_description",edtImportDescription.getText().toString());
        intent.putExtra("import_porject_country",ccpOriginalCountry.getSelectedCountryName().toString());
        if (rdCurrencyPen.isChecked())
        {
            main_currency = "PEN";
        }
        if (rdCurrencyUsd.isChecked())
        {
            main_currency = "USD";
        }
        intent.putExtra("main_currency",main_currency);
        intent.putExtra("ammount",edtAmmount.getText().toString());
        intent.putExtra("interest_rate",edtInterestRate.getText().toString());
        intent.putExtra("financing_months",btnMonths.getText().toString());
        intent.putExtra("financing_frecuency",btnPaymentFrencuency.getText().toString());
        intent.putExtra("grace_period",btnGracePeriod.getText().toString());
        intent.putExtra("monthly_interest_rate",monthly_rate_df);
        intent.putExtra("quote_ammount",quote_df);
        intent.putExtra("total_debt",total_debt_df);
        intent.putExtra("cost_of_debt",cost_of_debt_df);
        intent.putExtra("end_day",btnEndDay.getText().toString());
        intent.putExtra("end_month",btnEndMonth.getText().toString());
        intent.putExtra("end_year",btnEndYear.getText().toString());
        startActivity(intent);
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

    private void showAmortizationDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Amortización de la Deuda");
        dialog.setIcon(R.drawable.money2_icon);
        dialog.setMessage("Montos Estimados:");

        LayoutInflater inflater = LayoutInflater.from(this);
        View amortization = inflater.inflate(R.layout.amortization_layout,null);

        TextView txtAmmount,txtAnualInterestRate,txtFinanceMonth,txtPaymentFrecuency,txtMonthlyInterestRate,txtQuoteAmmount,txtTotalDebt,txtCostOfDebt,txtSumary;

        txtAmmount = amortization.findViewById(R.id.txtAmmount);
        txtAnualInterestRate = amortization.findViewById(R.id.txtAnualInterestRate);
        txtFinanceMonth = amortization.findViewById(R.id.txtFinanceMonth);
        txtPaymentFrecuency = amortization.findViewById(R.id.txtPaymentFrecuency);
        txtMonthlyInterestRate = amortization.findViewById(R.id.txtMonthlyInterestRate);
        txtQuoteAmmount = amortization.findViewById(R.id.txtQuoteAmmount);
        txtTotalDebt = amortization.findViewById(R.id.txtTotalDebt);
        txtCostOfDebt = amortization.findViewById(R.id.txtCostOfDebt);
        txtSumary = amortization.findViewById(R.id.txtSumary);

        if (rdCurrencyPen.isChecked())
        {
            main_currency = "PEN";
        }
        if (rdCurrencyUsd.isChecked())
        {
            main_currency = "USD";
        }

        double ammount = Double.parseDouble(edtAmmount.getText().toString());
        double rate = Double.parseDouble(edtInterestRate.getText().toString());
        double months = Double.parseDouble(btnMonths.getText().toString());
        String frecuency = btnPaymentFrencuency.getText().toString();

        //Tasa efectiva mensual:
        double rate_perone = rate/100;
        double monthly_rate_base = 1+rate_perone;
        double monthly_rate_exp = 0.08333333;
        double monthly_rate = Math.pow(monthly_rate_base,monthly_rate_exp)-1;
        double monthly_rate_express = monthly_rate*100;

        double total_debt_base = (1+monthly_rate);
        double total_debt_exp = months;
        double total_debt = Math.pow(total_debt_base,total_debt_exp)*ammount;
        double monthly_quote = total_debt/months;
        double cost_of_debt = total_debt-ammount;

        double quote = 1.00;
        if (frecuency.equals("Mensual"))
        {
            quote = monthly_quote*1;
        }
        if (frecuency.equals("Cada 2 meses"))
        {
            quote = monthly_quote*2;
        }
        if (frecuency.equals("Cada 3 meses"))
        {
            quote = monthly_quote*3;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String monthly_rate_df = decimalFormat.format(monthly_rate_express);
        String quote_df = decimalFormat.format(quote);
        String total_debt_df = decimalFormat.format(total_debt);
        String cost_of_debt_df = decimalFormat.format(cost_of_debt);

        txtAmmount.setText("Monto a financiar: "+edtAmmount.getText().toString()+" "+main_currency);
        txtAnualInterestRate.setText("Tasa de interés efectiva anual: "+edtInterestRate.getText().toString()+"%");
        txtFinanceMonth.setText("Duración del financiamiento: "+btnMonths.getText().toString()+" meses");
        txtPaymentFrecuency.setText("Frecuencia de pago: "+btnPaymentFrencuency.getText().toString());
        txtMonthlyInterestRate.setText("Tasa efectiva mensual: "+monthly_rate_df+"%");
        txtQuoteAmmount.setText("Monto de cada cuota: "+quote_df+" "+main_currency);
        txtTotalDebt.setText("Deuda total: "+total_debt_df+" "+main_currency);
        txtCostOfDebt.setText("Costo de la deuda: "+cost_of_debt_df+" "+main_currency);
        txtSumary.setText(frecuency+" deberás pagar el monto de: "+quote_df+" "+main_currency);

        dialog.setView(amortization);
        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
