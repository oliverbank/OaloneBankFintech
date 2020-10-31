package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xw.repo.BubbleSeekBar;

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

import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositTransactionInProgressActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterDepositActivity;

public class FinanceRequestDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imgCompanyImage,imageCompanyVerification,imgFinanceDestination;
    TextView txtCompanyName,txtCompanyType,txtBusinessLine,txtCompanyLocation,txtCompanyAge,txtBusinessMan,txtCompanyVerification,txtFinanceDestination,txtFixedAssetType,txtFixedAssetName,
            txtFixedAssetMode,txtWorkingCapitalDestination,txtWorkingCapitalDescription,txtExportMoment,txtExportTransport,txtExportPaymentMethod,txtExportDescription,
            txtExportCountry,txtImportMoment,txtImportTransport,txtImportPaymentMethod,txtImportDescription,txtImportCountry,txtAmmount,txtInvestmentCurrency,txtInterestRate,
            txtPaymentFrecuency,txtFinanceMonth,txtQuoteAmmount,txtTotalDebt,txtGracePeriod,txtInterestAmmount,txtWarrantyType,txtWarrantyValue,txtWarrantyDescription,txtRequesstDate,
            txtCurrencyRate,txtInvestmentAmmount,txtFinanceAmmount,txtTransactionCost,txtTotalInvestment,txtInvested,txtInvestedCurrency1,txtNoInvested,txtInvestedCurrency2,txtDaysToExpire;
    LinearLayout fixedAssetLayout,workingCapitalLayout,exportProjectLayout,importProjectLayout;
    private String postKey, currentUserID, databaseUserID,company_name,company_image,company_type,company_line,company_department,company_bth_day,company_bth_month,company_bth_year,
            username,company_verification,finance_destiny,fixed_asset_type,fixed_asset_name,fixed_asset_mode,working_capital_type,working_capital_description,export_porject_moment,
            export_porject_transport,export_porject_payment,export_porject_country,export_porject_description,import_porject_moment,import_porject_transport,import_porject_payment,
            import_porject_country,import_porject_description,ammount,main_currency,interest_rate,financing_frecuency,financing_months,quote_ammount,total_debt,grace_period,cost_of_debt,
            warranty_type,warranty_value,warranty_currency,warranty_description,date,time, investor_currency,currency_rate_sell,currency_rate_buy, invest_rate,my_pin,saveCurrentDate,
            saveCurrentTime,operationRandomName, direct_investment,basic_account_pen,basic_account_usd,invested,no_invested,end_day,end_month,end_year;
    private FirebaseAuth mAuth;
    private DatabaseReference clickPostRef,userRef,ratesRef,imageResourses,myOperationsRef;
    Button btnInvest;
    double currrency_buy_rate,currrency_sell_rate,my_investment, invest_fee,the_real_investment, my_money_in_pen, my_money_in_usd,investment_ammount;
    BubbleSeekBar seekBarPen;
    private RecyclerView myPostList;
    FirebaseRecyclerAdapter<InvestorsFinanceRequestModel, FinanceRequestDetailActivity.myPostViewHolder> firebaseRecyclerAdapter;
    long days_to_expire,diff2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_request_detail);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("Finance Requests").child(postKey);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");

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
        imgFinanceDestination = findViewById(R.id.imgFinanceDestination);
        txtFinanceDestination = findViewById(R.id.txtFinanceDestination);
        fixedAssetLayout = findViewById(R.id.fixedAssetLayout);
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
        txtInvestmentCurrency = findViewById(R.id.txtInvestmentCurrency);
        txtInterestRate = findViewById(R.id.txtInterestRate);
        txtPaymentFrecuency = findViewById(R.id.txtPaymentFrecuency);
        txtFinanceMonth = findViewById(R.id.txtFinanceMonth);
        txtQuoteAmmount = findViewById(R.id.txtQuoteAmmount);
        txtTotalDebt = findViewById(R.id.txtTotalDebt);
        txtGracePeriod = findViewById(R.id.txtGracePeriod);
        txtInterestAmmount = findViewById(R.id.txtInterestAmmount);
        txtRequesstDate = findViewById(R.id.txtRequesstDate);
        btnInvest = findViewById(R.id.btnInvest);
        txtInvested = findViewById(R.id.txtInvested);
        txtInvestedCurrency1 = findViewById(R.id.txtInvestedCurrency1);
        txtNoInvested = findViewById(R.id.txtNoInvested);
        txtInvestedCurrency2 = findViewById(R.id.txtInvestedCurrency2);
        txtDaysToExpire = findViewById(R.id.txtDaysToExpire);
        seekBarPen = findViewById(R.id.seekBarPen);

        fixedAssetLayout.getLayoutParams().height = 0;
        workingCapitalLayout.getLayoutParams().height = 0;
        exportProjectLayout.getLayoutParams().height = 0;
        importProjectLayout.getLayoutParams().height = 0;

        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);

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
                    finance_destiny = dataSnapshot.child("finance_destiny").getValue().toString();
                    fixed_asset_type = dataSnapshot.child("fixed_asset_type").getValue().toString();
                    fixed_asset_name = dataSnapshot.child("fixed_asset_name").getValue().toString();
                    fixed_asset_mode = dataSnapshot.child("fixed_asset_mode").getValue().toString();
                    working_capital_type = dataSnapshot.child("working_capital_type").getValue().toString();
                    working_capital_description = dataSnapshot.child("working_capital_description").getValue().toString();
                    export_porject_moment = dataSnapshot.child("export_porject_moment").getValue().toString();
                    export_porject_transport = dataSnapshot.child("export_porject_transport").getValue().toString();
                    export_porject_payment = dataSnapshot.child("export_porject_payment").getValue().toString();
                    export_porject_country = dataSnapshot.child("export_porject_country").getValue().toString();
                    export_porject_description = dataSnapshot.child("export_porject_description").getValue().toString();
                    import_porject_moment = dataSnapshot.child("import_porject_moment").getValue().toString();
                    import_porject_transport = dataSnapshot.child("import_porject_transport").getValue().toString();
                    import_porject_payment = dataSnapshot.child("import_porject_payment").getValue().toString();
                    import_porject_country = dataSnapshot.child("import_porject_country").getValue().toString();
                    import_porject_description = dataSnapshot.child("import_porject_description").getValue().toString();
                    ammount = dataSnapshot.child("ammount").getValue().toString();
                    main_currency = dataSnapshot.child("main_currency").getValue().toString();
                    interest_rate = dataSnapshot.child("interest_rate").getValue().toString();
                    financing_frecuency = dataSnapshot.child("financing_frecuency").getValue().toString();
                    financing_months = dataSnapshot.child("financing_months").getValue().toString();
                    quote_ammount = dataSnapshot.child("quote_ammount").getValue().toString();
                    total_debt = dataSnapshot.child("total_debt").getValue().toString();
                    grace_period = dataSnapshot.child("grace_period").getValue().toString();
                    cost_of_debt = dataSnapshot.child("cost_of_debt").getValue().toString();
                    date = dataSnapshot.child("date").getValue().toString();
                    time = dataSnapshot.child("time").getValue().toString();
                    invested = dataSnapshot.child("invested").getValue().toString();
                    no_invested = dataSnapshot.child("no_invested").getValue().toString();
                    end_day = dataSnapshot.child("end_day").getValue().toString();
                    end_month = dataSnapshot.child("end_month").getValue().toString();
                    end_year = dataSnapshot.child("end_year").getValue().toString();

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                    saveCurrentDate =currentDate.format(calForDate.getTime());
                    String expiration_finance_request_date = end_day+" "+end_month+" "+end_year;

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
                                        txtDaysToExpire.setText("Esta subasta ha finalizado");
                                        btnInvest.setText("SUBASTA FINALIZADA");
                                        btnInvest.setEnabled(false);
                                    }
                                }
                            });
                        }
                        if (days_to_expire > 0) {
                            txtDaysToExpire.setText("Esta subasta termina en "+days_to_expire+" dias");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    txtInvested.setText(invested);
                    txtInvestedCurrency1.setText(main_currency);
                    txtNoInvested.setText(no_invested);
                    txtInvestedCurrency2.setText(main_currency);

                    double ammount_invested, ammount_no_invested,total_ammount;

                    total_ammount = Double.parseDouble(ammount);
                    ammount_invested = Double.parseDouble(invested);

                    seekBarPen.setEnabled(false);
                    seekBarPen.getConfigBuilder()
                            .min(0)
                            .max((float) total_ammount)
                            .progress((float) ammount_invested)
                            .build();

                    toolbar.setTitle(company_name);
                    Picasso.with(FinanceRequestDetailActivity.this).load(company_image)
                            //.resize(840,840)
                            //.onlyScaleDown()
                            //.centerCrop()
                            .fit()
                            .centerCrop()
                            .into(imgCompanyImage);
                    txtCompanyName.setText("Nombre comercial: "+company_name);
                    txtCompanyType.setText("Tipo de empresa: "+company_type);
                    txtBusinessLine.setText("Actividad económica: "+company_line);
                    txtCompanyLocation.setText("Localización: "+company_department);
                    int day = Integer.parseInt(company_bth_day);
                    int month = Integer.parseInt(company_bth_month);
                    int year = Integer.parseInt(company_bth_year);

                    Calendar dob = Calendar.getInstance();
                    dob.set(year, month, day);
                    Calendar today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                    //Get Years
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
                        txtCompanyVerification.setText("Verificación de la emrpresa: En proceso");
                        imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                    }
                    if (company_verification.equals("true"))
                    {
                        txtCompanyVerification.setText("Verificación de la emrpresa: Verificado");
                        imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                    }
                    if (company_verification.equals("false"))
                    {
                        txtCompanyVerification.setText("Verificación de la emrpresa: Denegado");
                        imageCompanyVerification.setImageResource(R.drawable.error_icon);
                    }
                    if (finance_destiny.equals("Adquisición de activos fijos"))
                    {
                        fixedAssetLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        workingCapitalLayout.getLayoutParams().height = 0;
                        exportProjectLayout.getLayoutParams().height = 0;
                        importProjectLayout.getLayoutParams().height = 0;
                        txtFinanceDestination.setText("Adquisición de activos fijos");
                        imgFinanceDestination.setImageResource(R.drawable.asset_aquisition_image);
                    }
                    if (finance_destiny.equals("Capital de trabajo opearativo"))
                    {
                        workingCapitalLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        fixedAssetLayout.getLayoutParams().height = 0;
                        exportProjectLayout.getLayoutParams().height = 0;
                        importProjectLayout.getLayoutParams().height = 0;
                        txtFinanceDestination.setText("Capital de trabajo operativo");
                        imgFinanceDestination.setImageResource(R.drawable.working_capital_image);
                    }
                    if (finance_destiny.equals("Proyecto de exportación"))
                    {
                        exportProjectLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        fixedAssetLayout.getLayoutParams().height = 0;
                        workingCapitalLayout.getLayoutParams().height = 0;
                        importProjectLayout.getLayoutParams().height = 0;
                        txtFinanceDestination.setText("Proyecto de exportación");
                        imgFinanceDestination.setImageResource(R.drawable.export_project_image);
                    }
                    if (finance_destiny.equals("Proyecto de importación"))
                    {
                        importProjectLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        fixedAssetLayout.getLayoutParams().height = 0;
                        workingCapitalLayout.getLayoutParams().height = 0;
                        exportProjectLayout.getLayoutParams().height = 0;
                        txtFinanceDestination.setText("Proyecto de importación");
                        imgFinanceDestination.setImageResource(R.drawable.import_project_image);
                    }
                    txtFixedAssetType.setText("Activo fijo: "+fixed_asset_type);
                    txtFixedAssetName.setText("Nombre del activo fijo: "+fixed_asset_name);
                    txtFixedAssetMode.setText("Modalidad: "+fixed_asset_mode);
                    txtWorkingCapitalDestination.setText("Capital de trabajo: "+working_capital_type);
                    txtWorkingCapitalDescription.setText("Descripción del capital de trabajo: "+working_capital_description);
                    txtExportMoment.setText("Etapa a financiar: "+export_porject_moment);
                    txtExportTransport.setText("Transporte: "+export_porject_transport);
                    txtExportPaymentMethod.setText("Método de pago: "+export_porject_payment);
                    txtExportDescription.setText("Descripción del proyecto: "+export_porject_description);
                    txtExportCountry.setText("País de destino: "+export_porject_country);
                    txtImportMoment.setText("Etapa a financiar: "+import_porject_moment);
                    txtImportTransport.setText("Transporte: "+import_porject_transport);
                    txtImportPaymentMethod.setText("Método de pago: "+import_porject_payment);
                    txtImportDescription.setText("Descripción del proyecto: "+import_porject_description);
                    txtImportCountry.setText("País de origen: "+import_porject_country);
                    investment_ammount = Double.parseDouble(ammount);
                    DecimalFormat decimalFormat;
                    if (investment_ammount > 999.99 && investment_ammount < 1000000.00)
                    {
                        decimalFormat = new DecimalFormat("0,000.00");
                        ammount = decimalFormat.format(investment_ammount);
                    }
                    if (investment_ammount >= 1000000.00)
                    {
                        decimalFormat = new DecimalFormat("0,000,000.00");
                        ammount = decimalFormat.format(investment_ammount);
                    }
                    txtAmmount.setText(ammount);
                    txtInvestmentCurrency.setText(main_currency);
                    txtInterestRate.setText(interest_rate);
                    txtPaymentFrecuency.setText(financing_frecuency);
                    txtFinanceMonth.setText("Duración del financiamiento: "+financing_months+" meses");
                    txtQuoteAmmount.setText("Monto de la cuota: "+quote_ammount+" "+main_currency);
                    txtTotalDebt.setText("Retorno total: "+total_debt+" "+main_currency);
                    txtGracePeriod.setText("Período de gracia: "+grace_period+" meses");
                    txtInterestAmmount.setText(cost_of_debt+" "+main_currency);
                    txtRequesstDate.setText("Solicitud publicada el "+date+" a las "+time+" por "+username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageResourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    direct_investment = dataSnapshot.child("direct_investment").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        showInvestors();

        btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showInvestDialog();
                Intent intent = new Intent(FinanceRequestDetailActivity.this, RegisterInvestmentOnFinanceRequestActivity.class);
                intent.putExtra("PostKey",postKey);
                startActivity(intent);
            }
        });

    }

    private void showInvestors() {
        Query myPostQuery = clickPostRef.child("Investors").orderByChild("participation");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<InvestorsFinanceRequestModel, FinanceRequestDetailActivity.myPostViewHolder>
                (InvestorsFinanceRequestModel.class, R.layout.investor_user_finance_request, FinanceRequestDetailActivity.myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final FinanceRequestDetailActivity.myPostViewHolder viewHolder, InvestorsFinanceRequestModel model, int position) {
                viewHolder.setProfileimage(getApplicationContext(),model.getProfileimage());
                viewHolder.setUsername(model. getUsername());
                viewHolder.setInvestment_amount(model. getInvestment_amount());
                viewHolder.setMain_currency(model. getMain_currency());
                viewHolder.setParticipation(model. getParticipation());
                viewHolder.setUserId(model.getUserId());


                if (!model.getUserId().equals(currentUserID)) {
                    viewHolder.btnMyInvestment.getLayoutParams().height = 0;
                }
                if (model.getUserId().equals(currentUserID)) {
                    viewHolder.btnMyInvestment.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                }

                viewHolder.btnGoToInvestmenState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FinanceRequestDetailActivity.this, FinanceRequestInvestmentStateActivity.class);
                        intent.putExtra("finance_request_id",postKey);
                        startActivity(intent);
                    }
                });

            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
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
        myOperationMap.put("operation_type","Inversión directa");
        myOperationMap.put("operation_type_code","DI");
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        //1 -> user need to upload the bill
        //2 -> the bill is being verificated
        //3 -> the ammount has been deposited
        //4 -> there is a notification about the ammount
        //5 -> bill is not valid
        myOperationMap.put("deposit_state","1");
        myOperationMap.put("deposit_ammount","");
        myOperationMap.put("deposit_currency","");
        myOperationMap.put("deposit_real_ammount","");
        myOperationMap.put("deposit_real_currency","");
        myOperationMap.put("uid",currentUserID);
        myOperationMap.put("operation_image",direct_investment);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");
        //Transfers
        myOperationMap.put("transfer_user_origin","");
        myOperationMap.put("transfer_user_destination","");
        myOperationMap.put("sent_ammount","");
        myOperationMap.put("sent_currency","");
        myOperationMap.put("recieved_ammount","");
        myOperationMap.put("recieved_currency","");
        //Direct Investments
        myOperationMap.put("company_finance_name",company_name);
        myOperationMap.put("finance_ammount",ammount);
        myOperationMap.put("finance_currency",main_currency);
        //Credit Line
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    if (investor_currency.equals("PEN"))
                    {
                        double my_money = Double.parseDouble(basic_account_pen);
                        double my_money_now = my_money-the_real_investment;

                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String my_money_now_df = decimalFormat.format(my_money_now);

                        HashMap hashMap = new HashMap();
                        hashMap.put("basic_account_pen",my_money_now_df);
                        userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (task.isSuccessful())
                                {
                                    sendCompanyOwnerNotification();
                                }
                            }
                        });
                    }
                    if (investor_currency.equals("USD"))
                    {
                        double my_money = Double.parseDouble(basic_account_usd);
                        double my_money_now = my_money-the_real_investment;

                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String my_money_now_df = decimalFormat.format(my_money_now);

                        HashMap hashMap = new HashMap();
                        hashMap.put("basic_account_usd",my_money_now_df);
                        userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    sendCompanyOwnerNotification();
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    private void sendCompanyOwnerNotification() {

        Intent intent = new Intent(FinanceRequestDetailActivity.this,InvesmentFundTransactionCompletedActivity.class);
        intent.putExtra("TransactionCode",currentUserID+operationRandomName+"DI");
        startActivity(intent);
        finish();
        //Create another HashMap for company owner

    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        LinearLayout btnMyInvestment;
        DatabaseReference userRef;
        FirebaseAuth mAuth;
        String currentUserID;
        Button btnGoToInvestmenState;

        public myPostViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

            btnMyInvestment = mView.findViewById(R.id.btnMyInvestment);
            btnGoToInvestmenState = mView.findViewById(R.id.btnGoToInvestmenState);
            btnMyInvestment.getLayoutParams().height = 0;

        }

        public void setInvestment_amount(String investment_amount) {
            TextView textView = mView.findViewById(R.id.txtAmmount);
            textView.setText(investment_amount);
        }
        public void setMain_currency(String main_currency) {
            TextView textView = mView.findViewById(R.id.txtCurrency);
            textView.setText(main_currency);

        }
        public void setParticipation(String participation) {
            TextView textView = mView.findViewById(R.id.txtParticipation);
            textView.setText(participation+"%");

        }
        public void setProfileimage(Context ctx, String profileimage) {
            ImageView image = mView.findViewById(R.id.profileImage);
            Picasso.with(ctx).load(profileimage).fit().centerCrop().into(image);

        }
        public void setUsername(String username) {
            TextView textView = mView.findViewById(R.id.txtUsername);
            textView.setText(username);
        }
        public void setUserId(final String userId) {

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
