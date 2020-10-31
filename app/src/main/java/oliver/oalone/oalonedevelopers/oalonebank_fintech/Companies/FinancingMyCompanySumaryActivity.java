package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class FinancingMyCompanySumaryActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtCompanyName,txtCompanyType,txtBusinessLine,txtCompanyDepartment,txtFinancingDestination,txtFixedAssetType,txtFixedAssetName,txtFixedAssetMode,
            txtWorkingCapitalDestination,txtWorkingCapitalDescription,txtExportMoment,txtExportTransport,txtExportPaymentMethod,txtExportDescription,txtExportCountry,
            txtImportMoment,txtImportTransport,txtImportPaymentMethod,txtImportDescription,txtImportCountry,txtAmmount,txtAnualInterestRate,txtFinanceMonth,txtPaymentFrecuency,
            txtGracePeriod,txtMonthlyInterestRate,txtQuoteAmmount,txtTotalDebt,txtCostOfDebt,txtWarrantyType,txtWarrantyValue,txtWarrantyDescription,txtPropertyType,txtPropertyAddress,
            txtPropertyArea,txtMachinaryBrand,txtMachinaryYear,txtCarBrand,txtCarYear,txtCarRegister,txtBailletterBank,txtImageVerification,txtFeeRate1,txtFeeRate2,txtFeeRate3,
            txtFeeRate4,txtDailyFeeDefaulter,txtFee1Cost,txtFee2Cost,txtFee3Cost,txtFee4Cost,txtTotalQuoate,txtExchangeRate,txtEndDate;
    ImageView imageCompany;
    ImageButton btnWarrantyImage;
    Button btnContract,btnFinish;
    CheckBox cbAgreement;
    String finance_destiny,postKey,currentUserID,company_name,company_image,company_type,company_line,company_department,fixed_asset_type,fixed_asset_name,fixed_asset_mode,
            working_capital_type,working_capital_description,export_porject_moment,export_porject_transport,export_porject_payment,export_porject_description,
            export_porject_country,import_porject_moment,import_porject_transport,import_porject_payment,import_porject_description,import_porject_country,main_currency,ammount,
            interest_rate,financing_months,financing_frecuency,grace_period,monthly_interest_rate,quote_ammount,total_debt,cost_of_debt,fee_rate1,
            fee_rate2,fee_rate3,fee_rate4,currency_exchange,daily_defaulter_rate,company_social_reason,company_ruc, company_district,company_address,company_bth_day,company_bth_month,
            company_bth_year,company_workers,company_verification,st_fee1,st_fee2,st_fee3,st_fee4,st_total_quote,fullname,document_type,document_number,address,username,user_verification,
            nacionality,email,end_day,end_month,end_year,credit_score;
    RelativeLayout rootLayout;
    LinearLayout assetAcquisitionLayout,workingCapitalLayout,exportProjectLayout,importProjectLayout;
    private DatabaseReference clickPostRef,userRef,financeRequestRef, rates,postRef;
    private FirebaseAuth mAuth;
    final static int Gallery_Pick = 1;
    private Uri imageUri;
    private StorageReference postImagesReference;
    private ProgressDialog loadingBar;
    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl;
    private long countPost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financing_my_company_sumary);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("My Companies").child(postKey);
        postRef = FirebaseDatabase.getInstance().getReference().child("Finance Requests");
        loadingBar = new ProgressDialog(this);
        postImagesReference = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        financeRequestRef = FirebaseDatabase.getInstance().getReference().child("Finance Requests");
        rates = FirebaseDatabase.getInstance().getReference().child("Rates");
        mAuth = FirebaseAuth.getInstance();
        finance_destiny = getIntent().getExtras().get("finance_destiny").toString();
        fixed_asset_type = getIntent().getExtras().get("fixed_asset_type").toString();
        fixed_asset_name = getIntent().getExtras().get("fixed_asset_name").toString();
        fixed_asset_mode = getIntent().getExtras().get("fixed_asset_mode").toString();
        working_capital_type = getIntent().getExtras().get("working_capital_type").toString();
        working_capital_description = getIntent().getExtras().get("working_capital_description").toString();
        export_porject_moment = getIntent().getExtras().get("export_porject_moment").toString();
        export_porject_transport = getIntent().getExtras().get("export_porject_transport").toString();
        export_porject_payment = getIntent().getExtras().get("export_porject_payment").toString();
        export_porject_description = getIntent().getExtras().get("export_porject_description").toString();
        export_porject_country = getIntent().getExtras().get("export_porject_country").toString();
        import_porject_moment = getIntent().getExtras().get("import_porject_moment").toString();
        import_porject_transport = getIntent().getExtras().get("import_porject_transport").toString();
        import_porject_payment = getIntent().getExtras().get("import_porject_payment").toString();
        import_porject_description = getIntent().getExtras().get("import_porject_description").toString();
        import_porject_country = getIntent().getExtras().get("import_porject_country").toString();
        main_currency = getIntent().getExtras().get("main_currency").toString();
        ammount = getIntent().getExtras().get("ammount").toString();
        interest_rate = getIntent().getExtras().get("interest_rate").toString();
        financing_months = getIntent().getExtras().get("financing_months").toString();
        financing_frecuency = getIntent().getExtras().get("financing_frecuency").toString();
        grace_period= getIntent().getExtras().get("grace_period").toString();
        monthly_interest_rate = getIntent().getExtras().get("monthly_interest_rate").toString();
        quote_ammount = getIntent().getExtras().get("quote_ammount").toString();
        total_debt = getIntent().getExtras().get("total_debt").toString();
        cost_of_debt = getIntent().getExtras().get("cost_of_debt").toString();
        txtEndDate = findViewById(R.id.txtEndDate);

        end_day = getIntent().getExtras().get("end_day").toString();
        end_month = getIntent().getExtras().get("end_month").toString();
        end_year = getIntent().getExtras().get("end_year").toString();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Resumen del financiamiento");
        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        rootLayout = findViewById(R.id.rootLayout);
        txtCompanyType = findViewById(R.id.txtCompanyType);
        txtBusinessLine = findViewById(R.id.txtBusinessLine);
        txtCompanyDepartment = findViewById(R.id.txtCompanyDepartment);
        txtFinancingDestination = findViewById(R.id.txtFinancingDestination);
        assetAcquisitionLayout = findViewById(R.id.assetAcquisitionLayout);
        txtFixedAssetType = findViewById(R.id.txtFixedAssetType);
        txtFixedAssetName = findViewById(R.id.txtFixedAssetName);
        txtFixedAssetMode = findViewById(R.id.txtFixedAssetMode);
        workingCapitalLayout = findViewById(R.id.workingCapitalLayout);
        txtWorkingCapitalDestination = findViewById(R.id.txtWorkingCapitalDestination);
        txtWorkingCapitalDescription = findViewById(R.id.txtWorkingCapitalDescription);
        exportProjectLayout = findViewById(R.id.exportProjectLayout);
        txtExportMoment = findViewById(R.id.txtExportMoment);
        txtExportTransport = findViewById(R.id.txtExportTransport);
        txtExportPaymentMethod = findViewById(R.id.txtExportPaymentMethod);
        txtExportDescription = findViewById(R.id.txtExportDescription);
        txtExportCountry = findViewById(R.id.txtExportCountry);
        importProjectLayout = findViewById(R.id.importProjectLayout);
        txtImportMoment = findViewById(R.id.txtImportMoment);
        txtImportTransport = findViewById(R.id.txtImportTransport);
        txtImportPaymentMethod = findViewById(R.id.txtImportPaymentMethod);
        txtImportDescription = findViewById(R.id.txtImportDescription);
        txtImportCountry = findViewById(R.id.txtImportCountry);
        txtAmmount = findViewById(R.id.txtAmmount);
        txtAnualInterestRate = findViewById(R.id.txtAnualInterestRate);
        txtFinanceMonth = findViewById(R.id.txtFinanceMonth);
        txtPaymentFrecuency = findViewById(R.id.txtPaymentFrecuency);
        txtGracePeriod = findViewById(R.id.txtGracePeriod);
        txtMonthlyInterestRate = findViewById(R.id.txtMonthlyInterestRate);
        txtQuoteAmmount = findViewById(R.id.txtQuoteAmmount);
        txtTotalDebt = findViewById(R.id.txtTotalDebt);
        txtCostOfDebt = findViewById(R.id.txtCostOfDebt);
        txtImageVerification = findViewById(R.id.txtImageVerification);
        txtFeeRate1 = findViewById(R.id.txtFeeRate1);
        txtFeeRate2 = findViewById(R.id.txtFeeRate2);
        txtFeeRate3 = findViewById(R.id.txtFeeRate3);
        txtFeeRate4 = findViewById(R.id.txtFeeRate4);
        txtDailyFeeDefaulter = findViewById(R.id.txtDailyFeeDefaulter);
        txtFee1Cost = findViewById(R.id.txtFee1Cost);
        txtFee2Cost = findViewById(R.id.txtFee2Cost);
        txtFee3Cost = findViewById(R.id.txtFee3Cost);
        txtFee4Cost = findViewById(R.id.txtFee4Cost);
        txtTotalQuoate = findViewById(R.id.txtTotalQuoate);
        txtExchangeRate = findViewById(R.id.txtExchangeRate);
        btnContract = findViewById(R.id.btnContract);
        cbAgreement = findViewById(R.id.cbAgreement);
        btnFinish = findViewById(R.id.btnFinish);

        getDataDate();

        assetAcquisitionLayout.getLayoutParams().height = 0;
        workingCapitalLayout.getLayoutParams().height = 0;
        exportProjectLayout.getLayoutParams().height = 0;
        importProjectLayout.getLayoutParams().height = 0;
        if (finance_destiny.equals("Adquisición de activos fijos"))
        {
            assetAcquisitionLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            workingCapitalLayout.getLayoutParams().height = 0;
            exportProjectLayout.getLayoutParams().height = 0;
            importProjectLayout.getLayoutParams().height = 0;
        }
        if (finance_destiny.equals("Capital de trabajo opeartivo"))
        {
            workingCapitalLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            assetAcquisitionLayout.getLayoutParams().height = 0;
            exportProjectLayout.getLayoutParams().height = 0;
            importProjectLayout.getLayoutParams().height = 0;
        }
        if (finance_destiny.equals("Proyecto de exportación"))
        {
            exportProjectLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            assetAcquisitionLayout.getLayoutParams().height = 0;
            workingCapitalLayout.getLayoutParams().height = 0;
            importProjectLayout.getLayoutParams().height = 0;
        }
        if (finance_destiny.equals("Proyecto de importación"))
        {
            importProjectLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            assetAcquisitionLayout.getLayoutParams().height = 0;
            workingCapitalLayout.getLayoutParams().height = 0;
            exportProjectLayout.getLayoutParams().height = 0;
        }

        rates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fee_rate1 = dataSnapshot.child("fee_rate1").getValue().toString();
                fee_rate2 = dataSnapshot.child("fee_rate2").getValue().toString();
                fee_rate3 = dataSnapshot.child("fee_rate3").getValue().toString();
                fee_rate4 = dataSnapshot.child("fee_rate4").getValue().toString();
                currency_exchange = dataSnapshot.child("currency_rate_sell").getValue().toString();
                daily_defaulter_rate = dataSnapshot.child("daily_defaulter_rate").getValue().toString();

                txtFeeRate1.setText(fee_rate1+"%");
                txtFeeRate2.setText(fee_rate2+"%");
                txtFeeRate4.setText(fee_rate4+"%");
                txtDailyFeeDefaulter.setText("*Tasa moratoria diaria: "+daily_defaulter_rate+"%");

                double db_ammount = Double.parseDouble(ammount);
                double db_quote = Double.parseDouble(quote_ammount);
                double db_exchange = Double.parseDouble(currency_exchange);
                double rate1 = Double.parseDouble(fee_rate1);
                double rate2 = Double.parseDouble(fee_rate2);
                double rate3 = Double.parseDouble(fee_rate3);
                double rate4 = Double.parseDouble(fee_rate4);

                double fee1 = (rate1/100)*db_ammount;
                double fee2 = (rate2/100)*db_ammount;
                double fee4 = 1;

                fee4 = (rate4/100)*db_quote;

                double months = Double.parseDouble(financing_months);
                double fees = 1;
                double finac_frec = 1;
                if (financing_frecuency.equals("Mensual"))
                {
                   finac_frec = 1;
                }
                if (financing_frecuency.equals("Cada 2 meses"))
                {
                    finac_frec = 2;
                }
                if (financing_frecuency.equals("Cada 3 meses"))
                {
                    finac_frec = 3;
                }

                fees = ((fee1+fee2)/(months/finac_frec))+fee4;

                double total_quote = db_quote+fees;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                st_fee1 = decimalFormat.format(fee1);
                st_fee2 = decimalFormat.format(fee2);
                st_fee4 = decimalFormat.format(fee4);
                st_total_quote = decimalFormat.format(total_quote);

                txtFee1Cost.setText("Gestión del financiamiento: "+st_fee1+" "+main_currency);
                txtFee2Cost.setText("Fideicomiso y contrato: "+st_fee2+" "+main_currency);
                txtFee4Cost.setText("Transferencia de flujos de efectivo: "+st_fee4+" "+main_currency);
                txtTotalQuoate.setText("Cuota fija a pagar "+financing_frecuency+": "+st_total_quote+" "+main_currency);
                txtExchangeRate.setText("*Tipo de cambio: "+currency_exchange);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    company_image = dataSnapshot.child("company_image").getValue().toString();
                    company_name = dataSnapshot.child("company_name").getValue().toString();
                    company_type = dataSnapshot.child("company_type").getValue().toString();
                    company_line = dataSnapshot.child("company_line").getValue().toString();
                    company_department  = dataSnapshot.child("company_department").getValue().toString();
                    credit_score = dataSnapshot.child("credit_score").getValue().toString();

                    company_social_reason = dataSnapshot.child("company_social_reason").getValue().toString();
                    company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                    company_district = dataSnapshot.child("company_district").getValue().toString();
                    company_address = dataSnapshot.child("company_address").getValue().toString();
                    company_bth_day = dataSnapshot.child("company_bth_day").getValue().toString();
                    company_bth_month = dataSnapshot.child("company_bth_month").getValue().toString();
                    company_bth_year = dataSnapshot.child("company_bth_year").getValue().toString();
                    company_workers = dataSnapshot.child("company_workers").getValue().toString();
                    company_verification = dataSnapshot.child("company_verification").getValue().toString();

                    Picasso.with(FinancingMyCompanySumaryActivity.this).load(company_image).fit().into(imageCompany);
                    txtCompanyName.setText(company_name);
                    txtCompanyType.setText(company_type);
                    txtBusinessLine.setText(company_line);
                    txtCompanyDepartment.setText(company_department);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtFinancingDestination.setText(finance_destiny);
        txtFixedAssetType.setText("Tipo de activo fijo: "+fixed_asset_type);
        txtFixedAssetName.setText("Nombre del activo fijo: "+fixed_asset_name);
        txtFixedAssetMode.setText("Modalidad del financiamiento: "+fixed_asset_mode);
        txtWorkingCapitalDestination.setText("Destino del capital: "+working_capital_type);
        txtWorkingCapitalDescription.setText("Descripción del capital de trabajo: "+working_capital_description);
        txtExportMoment.setText("Etapa a financiar: "+export_porject_moment);
        txtExportTransport.setText("Transporte: "+export_porject_transport);
        txtExportPaymentMethod.setText("Método de pago: "+export_porject_payment);
        txtExportCountry.setText("País de destino: "+export_porject_country);
        txtExportDescription.setText("Descripción del proyecto: "+export_porject_description);
        txtImportMoment.setText("Etapa a financiar: "+import_porject_moment);
        txtImportTransport.setText("Transporte: "+import_porject_transport);
        txtImportPaymentMethod.setText("Método de pago: "+import_porject_payment);
        txtImportCountry.setText("País de destino: "+import_porject_country);
        txtImportDescription.setText("Descripción del proyecto: "+import_porject_description);
        txtAmmount.setText("Monto y moneda: "+ammount+" "+main_currency);
        txtAnualInterestRate.setText("TREA propuesta: "+interest_rate+"%");
        txtFinanceMonth.setText("Duración del financiamiento: "+financing_months+" Meses");
        txtPaymentFrecuency.setText("Frecuencia de pago: "+financing_frecuency);
        txtGracePeriod.setText("Período de gracia: "+grace_period+" meses");
        txtMonthlyInterestRate.setText("TREM: "+monthly_interest_rate+"%");
        txtQuoteAmmount.setText("Cuota: "+quote_ammount+" "+main_currency);
        txtTotalDebt.setText("Deuda total: "+total_debt+" "+main_currency);
        txtCostOfDebt.setText("Costo de deuda: "+cost_of_debt+" "+main_currency);
        txtEndDate.setText("Vencimiento del la Solicitud: "+end_day+"/"+end_month+"/"+end_year);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                     fullname = dataSnapshot.child("fullname").getValue().toString();
                     document_type = dataSnapshot.child("document_type").getValue().toString();
                     document_number = dataSnapshot.child("document_number").getValue().toString();
                     address = dataSnapshot.child("address").getValue().toString();
                     username = dataSnapshot.child("username").getValue().toString();
                     user_verification = dataSnapshot.child("user_verification").getValue().toString();
                     nacionality = dataSnapshot.child("nacionality").getValue().toString();
                     email = dataSnapshot.child("email").getValue().toString();

                     cbAgreement.setText("Yo, "+fullname+" con "+document_type+" "+document_number+" domiciliado en "+address+" acepto todas las claúsulas del contrato y me comprometo a honrrar con las obligaciones establecidas en el presente resumen.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishFinanceRequest();
            }
        });
    }

    private void finishFinanceRequest() {
        if (!cbAgreement.isChecked())
        {
            Snackbar.make(rootLayout, "Debes aceptar todas las claúsulas del contrato.", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!cbAgreement.isChecked())
        {
            Snackbar.make(rootLayout, "Debes aceptar todas las claúsulas del contrato.", Snackbar.LENGTH_LONG).show();
            return;
        }
        else
        {
            savingPostInformationToDatabase();
        }
    }


    private void savingPostInformationToDatabase() {

        loadingBar.setTitle("Finalizando solicitud de financiamiento");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        HashMap postMap = new HashMap();
        postMap.put("uid",currentUserID);
        postMap.put("date",saveCurrentDate);
        postMap.put("time",saveCurrentTime);
        postMap.put("warranty_image",downloadUrl);
        postMap.put("fullname",fullname);
        postMap.put("document_type",document_type);
        postMap.put("document_number",document_number);
        postMap.put("address",address);
        postMap.put("username",username);
        postMap.put("user_verification",user_verification);
        postMap.put("nacionality",nacionality);
        postMap.put("email",email);
        postMap.put("company_image",company_image);
        postMap.put("company_name",company_name);
        postMap.put("company_type",company_type);
        postMap.put("company_line",company_line);
        postMap.put("company_department",company_department);
        postMap.put("company_social_reason",company_social_reason);
        postMap.put("company_ruc",company_ruc);
        postMap.put("company_district",company_district);
        postMap.put("company_address",company_address);
        postMap.put("company_bth_day",company_bth_day);
        postMap.put("company_bth_month",company_bth_month);
        postMap.put("company_bth_year",company_bth_year);
        postMap.put("company_workers",company_workers);
        postMap.put("company_verification",company_verification);
        postMap.put("finance_destiny",finance_destiny);
        postMap.put("fixed_asset_type",fixed_asset_type);
        postMap.put("fixed_asset_name",fixed_asset_name);
        postMap.put("fixed_asset_mode",fixed_asset_mode);
        postMap.put("working_capital_type",working_capital_type);
        postMap.put("working_capital_description",working_capital_description);
        postMap.put("export_porject_moment",export_porject_moment);
        postMap.put("export_porject_transport",export_porject_transport);
        postMap.put("export_porject_payment",export_porject_payment);
        postMap.put("export_porject_country",export_porject_country);
        postMap.put("export_porject_description",export_porject_description);
        postMap.put("import_porject_moment",import_porject_moment);
        postMap.put("import_porject_transport",import_porject_transport);
        postMap.put("import_porject_payment",import_porject_payment);
        postMap.put("import_porject_country",import_porject_country);
        postMap.put("import_porject_description",import_porject_description);
        postMap.put("ammount",ammount);
        postMap.put("main_currency",main_currency);
        postMap.put("interest_rate",interest_rate);
        postMap.put("financing_months",financing_months);
        postMap.put("financing_frecuency",financing_frecuency);
        postMap.put("grace_period",grace_period);
        postMap.put("monthly_interest_rate",monthly_interest_rate);
        postMap.put("quote_ammount",quote_ammount);
        postMap.put("total_debt",total_debt);
        postMap.put("cost_of_debt",cost_of_debt);
        postMap.put("fee_rate1",txtFeeRate1.getText().toString());
        postMap.put("fee_rate2",txtFeeRate2.getText().toString());
        postMap.put("fee_rate4",txtFeeRate4.getText().toString());
        postMap.put("fee_ammount1",st_fee1);
        postMap.put("fee_ammount2",st_fee2);
        postMap.put("fee_ammount3",st_fee3);
        postMap.put("fee_ammount4",st_fee4);
        postMap.put("total_quote",st_total_quote);
        postMap.put("end_day",end_day);
        postMap.put("end_month",end_month);
        postMap.put("end_year",end_year);
        postMap.put("finance_request_verification","progress");
        postMap.put("invested","0.00");
        postMap.put("no_invested",ammount);
        postMap.put("credit_score",credit_score);
        postMap.put("company_id",postKey);
        postMap.put("finance_request_expired", "false");
        postMap.put("timestamp", ServerValue.TIMESTAMP);
        postRef.child(currentUserID+postRandomName).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    //sendUserToMyCompaniesActivity();
                    Toast.makeText(FinancingMyCompanySumaryActivity.this, "Solicitud publicada", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FinancingMyCompanySumaryActivity.this, MainActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                    finish();
                }
                else
                {
                    Toast.makeText(FinancingMyCompanySumaryActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void uploadWarrantyImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri = data.getData();
            btnWarrantyImage.setImageURI(imageUri);
            txtImageVerification.setText("Imágen cargada con éxito");
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
