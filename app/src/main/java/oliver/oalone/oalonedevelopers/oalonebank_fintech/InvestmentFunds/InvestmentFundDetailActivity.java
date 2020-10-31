package oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.HomeFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class InvestmentFundDetailActivity extends AppCompatActivity {

    LineChart mLineChart;
    TextView txtTittle,txtFundDescription,txtCurrentQuoteValue,txtTotalInvestment,txtQuotesCirculating,txtFundAnualProfit,txtFundMonthlyProfit,txtMinDaysFund,txtRecomendedDays,
            txtFundRisk,txtMyLastQuoteValue,txtMyLastTransaction,txtMyQuotesNumber,txtMyFundInvestment,txtMyLastTransactionDate,txtDaysSinceLastTransaction,txtSuscriptionFee,
            txtManagementFee,txtFundRescue,txtMonth1,txtMonth2,txtMonth3,txtMonth4,txtMonth5,txtMonth6,txtMonth7,txtMonth8,txtMonth9,txtMonth10,txtMonth11,txtMonth12,
            txtStartQuote1,txtStartQuote2,txtStartQuote3,txtStartQuote4,txtStartQuote5,txtStartQuote6,txtStartQuote7,txtStartQuote8,txtStartQuote9,txtStartQuote10,txtStartQuote11,
            txtStartQuote12,txtEndQuote1,txtEndQuote2,txtEndQuote3,txtEndQuote4,txtEndQuote5,txtEndQuote6,txtEndQuote7,txtEndQuote8,txtEndQuote9,txtEndQuote10,txtEndQuote11,
            txtEndQuote12,txtTotalFund1,txtTotalFund2,txtTotalFund3,txtTotalFund4,txtTotalFund5,txtTotalFund6,txtTotalFund7,txtTotalFund8,txtTotalFund9,txtTotalFund10,txtTotalFund11,
            txtTotalFund12,txtProfit1,txtProfit2,txtProfit3,txtProfit4,txtProfit5,txtProfit6,txtProfit7,txtProfit8,txtProfit9,txtProfit10,txtProfit11,txtProfit12,txtCurrentYear,
            txtLastYear,txtStartQuote13,txtEndQuote13,txtTotalFund13,txtProfit13,txtMyFundInvestmentParticipation;
    private FirebaseAuth mAuth;
    private DatabaseReference investment_fund_ref,userRef, ratesRef,investmentFundTransactionsRef, myInvestmentFundTransRef, fundAssetsRef;
    String currentUserID,postKey,fund_name,fund_description,fund_investment,quote_value,fund_currency,quotes_circulating,fund_anual_profit,fund_monthly_profit,min_days,
            recommended_days,fund_risk,suscription_quote_fee,fund_management_fee,fund_rescue,quote_value1,quote_value2,quote_value3,quote_value4,quote_value5,quote_value6,
            quote_value7,quote_value8,quote_value9,quote_value10,quote_value11,quote_value12,quote_value13,user_verification,basic_account_pen,basic_account_usd,currency_rate_sell,
            currency_rate_buy,fund_code,saveCurrentDate,saveCurrentTime,last_quote_value,last_investment,my_quotes,my_equity,last_transaction_date,fund_investment1,fund_investment2,
            fund_investment3,fund_investment4,fund_investment5,fund_investment6,fund_investment7,fund_investment8,fund_investment9,fund_investment10,fund_investment11,fund_investment12,
            fund_monthly_profit1,fund_monthly_profit2,fund_monthly_profit3,fund_monthly_profit4,fund_monthly_profit5,fund_monthly_profit6,fund_monthly_profit7,fund_monthly_profit8,
            fund_monthly_profit9,fund_monthly_profit10,fund_monthly_profit11,fund_monthly_profit12;
    int month;
    float f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12;
    Button btnInvest,btnQuotesNumber,btnRescue;
    String invest_currency = "";
    private ProgressDialog loadingBar;
    double total_to_investment_fund,real_total_invest,quote_numbers;
    AlertDialog.Builder dialog;
    RecyclerView recyclerFound;
    FirebaseRecyclerAdapter<FundAssetsModel, InvestmentFundDetailActivity.PostViewHolder> firebaseRecyclerAdapter;

    ArrayList<String> quotes =new ArrayList<>();
    SpinnerDialog spinnerQuotes;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_fund_detail);

        mLineChart = findViewById(R.id.linechart);
        txtTittle = findViewById(R.id.txtTittle);
        txtFundDescription = findViewById(R.id.txtFundDescription);
        txtCurrentQuoteValue = findViewById(R.id.txtCurrentQuoteValue);
        txtTotalInvestment = findViewById(R.id.txtTotalInvestment);
        txtQuotesCirculating = findViewById(R.id.txtQuotesCirculating);
        txtFundAnualProfit = findViewById(R.id.txtFundAnualProfit);
        txtFundMonthlyProfit = findViewById(R.id.txtFundMonthlyProfit);
        txtMinDaysFund = findViewById(R.id.txtMinDaysFund);
        txtRecomendedDays = findViewById(R.id.txtRecomendedDays);
        txtFundRisk = findViewById(R.id.txtFundRisk);
        txtMyLastQuoteValue = findViewById(R.id.txtMyLastQuoteValue);
        txtMyLastTransaction = findViewById(R.id.txtMyLastTransaction);
        txtMyQuotesNumber = findViewById(R.id.txtMyQuotesNumber);
        txtMyFundInvestment = findViewById(R.id.txtMyFundInvestment);
        txtMyLastTransactionDate = findViewById(R.id.txtMyLastTransactionDate);
        txtDaysSinceLastTransaction = findViewById(R.id.txtDaysSinceLastTransaction);
        txtSuscriptionFee = findViewById(R.id.txtSuscriptionFee);
        txtManagementFee = findViewById(R.id.txtManagementFee);
        txtFundRescue = findViewById(R.id.txtFundRescue);
        txtMonth1 = findViewById(R.id.txtMonth1);
        txtMonth2 = findViewById(R.id.txtMonth2);
        txtMonth3 = findViewById(R.id.txtMonth3);
        txtMonth4 = findViewById(R.id.txtMonth4);
        txtMonth5 = findViewById(R.id.txtMonth5);
        txtMonth6 = findViewById(R.id.txtMonth6);
        txtMonth7 = findViewById(R.id.txtMonth7);
        txtMonth8 = findViewById(R.id.txtMonth8);
        txtMonth9 = findViewById(R.id.txtMonth9);
        txtMonth10 = findViewById(R.id.txtMonth10);
        txtMonth11 = findViewById(R.id.txtMonth11);
        txtMonth12 = findViewById(R.id.txtMonth12);
        txtStartQuote1= findViewById(R.id.txtStartQuote1);
        txtStartQuote2= findViewById(R.id.txtStartQuote2);
        txtStartQuote3= findViewById(R.id.txtStartQuote3);
        txtStartQuote4= findViewById(R.id.txtStartQuote4);
        txtStartQuote5= findViewById(R.id.txtStartQuote5);
        txtStartQuote6= findViewById(R.id.txtStartQuote6);
        txtStartQuote7= findViewById(R.id.txtStartQuote7);
        txtStartQuote8= findViewById(R.id.txtStartQuote8);
        txtStartQuote9= findViewById(R.id.txtStartQuote9);
        txtStartQuote10= findViewById(R.id.txtStartQuote10);
        txtStartQuote11= findViewById(R.id.txtStartQuote11);
        txtStartQuote12= findViewById(R.id.txtStartQuote12);
        txtEndQuote1 = findViewById(R.id.txtEndQuote1);
        txtEndQuote2 = findViewById(R.id.txtEndQuote2);
        txtEndQuote3 = findViewById(R.id.txtEndQuote3);
        txtEndQuote4 = findViewById(R.id.txtEndQuote4);
        txtEndQuote5 = findViewById(R.id.txtEndQuote5);
        txtEndQuote6 = findViewById(R.id.txtEndQuote6);
        txtEndQuote7 = findViewById(R.id.txtEndQuote7);
        txtEndQuote8 = findViewById(R.id.txtEndQuote8);
        txtEndQuote9 = findViewById(R.id.txtEndQuote9);
        txtEndQuote10 = findViewById(R.id.txtEndQuote10);
        txtEndQuote11 = findViewById(R.id.txtEndQuote11);
        txtEndQuote12 = findViewById(R.id.txtEndQuote12);
        txtTotalFund1 = findViewById(R.id.txtTotalFund1);
        txtTotalFund2 = findViewById(R.id.txtTotalFund2);
        txtTotalFund3 = findViewById(R.id.txtTotalFund3);
        txtTotalFund4 = findViewById(R.id.txtTotalFund4);
        txtTotalFund5 = findViewById(R.id.txtTotalFund5);
        txtTotalFund6 = findViewById(R.id.txtTotalFund6);
        txtTotalFund7 = findViewById(R.id.txtTotalFund7);
        txtTotalFund8 = findViewById(R.id.txtTotalFund8);
        txtTotalFund9 = findViewById(R.id.txtTotalFund9);
        txtTotalFund10 = findViewById(R.id.txtTotalFund10);
        txtTotalFund11 = findViewById(R.id.txtTotalFund11);
        txtTotalFund12 = findViewById(R.id.txtTotalFund12);
        txtProfit1 = findViewById(R.id.txtProfit1);
        txtProfit2 = findViewById(R.id.txtProfit2);
        txtProfit3 = findViewById(R.id.txtProfit3);
        txtProfit4 = findViewById(R.id.txtProfit4);
        txtProfit5 = findViewById(R.id.txtProfit5);
        txtProfit6 = findViewById(R.id.txtProfit6);
        txtProfit7 = findViewById(R.id.txtProfit7);
        txtProfit8 = findViewById(R.id.txtProfit8);
        txtProfit9 = findViewById(R.id.txtProfit9);
        txtProfit10 = findViewById(R.id.txtProfit10);
        txtProfit11 = findViewById(R.id.txtProfit11);
        txtProfit12= findViewById(R.id.txtProfit12);
        txtCurrentYear = findViewById(R.id.txtCurrentYear);
        txtLastYear = findViewById(R.id.txtLastYear);
        txtStartQuote13 = findViewById(R.id.txtStartQuote13);
        txtEndQuote13 = findViewById(R.id.txtEndQuote13);
        txtTotalFund13 = findViewById(R.id.txtTotalFund13);
        txtProfit13 = findViewById(R.id.txtProfit13);
        btnInvest= findViewById(R.id.btnInvest);
        btnRescue= findViewById(R.id.btnRescue);
        txtMyFundInvestmentParticipation= findViewById(R.id.txtMyFundInvestmentParticipation);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        investment_fund_ref = FirebaseDatabase.getInstance().getReference().child("Investment Funds").child(postKey);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        investmentFundTransactionsRef = FirebaseDatabase.getInstance().getReference().child("Investment Fund Transactions");
        fundAssetsRef = FirebaseDatabase.getInstance().getReference().child("Investment Funds").child(postKey).child("Fund Assets");

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month = localDate.getMonthValue();

        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int last_year = current_year-1;

        String this_year = String.valueOf(current_year);
        String the_last_year = String.valueOf(last_year);

        txtCurrentYear.setText("PRESENTE AÑO: "+this_year);
        txtLastYear.setText("AÑO PASADO: "+the_last_year);

        investment_fund_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    fund_name = dataSnapshot.child("fund_name").getValue().toString();
                    fund_description = dataSnapshot.child("fund_description").getValue().toString();
                    fund_investment = dataSnapshot.child("fund_investment").getValue().toString();
                    quote_value= dataSnapshot.child("quote_value").getValue().toString();
                    fund_currency = dataSnapshot.child("fund_currency").getValue().toString();
                    quotes_circulating = dataSnapshot.child("quotes_circulating").getValue().toString();
                    fund_anual_profit = dataSnapshot.child("fund_anual_profit").getValue().toString();
                    fund_monthly_profit = dataSnapshot.child("fund_monthly_profit").getValue().toString();
                    min_days = dataSnapshot.child("min_days").getValue().toString();
                    recommended_days = dataSnapshot.child("recommended_days").getValue().toString();
                    fund_risk = dataSnapshot.child("fund_risk").getValue().toString();
                    suscription_quote_fee = dataSnapshot.child("suscription_quote_fee").getValue().toString();
                    fund_management_fee = dataSnapshot.child("fund_management_fee").getValue().toString();
                    fund_rescue = dataSnapshot.child("fund_rescue").getValue().toString();
                    fund_code = dataSnapshot.child("fund_code").getValue().toString();

                    quote_value1= dataSnapshot.child("quote_value1").getValue().toString();
                    quote_value2= dataSnapshot.child("quote_value2").getValue().toString();
                    quote_value3= dataSnapshot.child("quote_value3").getValue().toString();
                    quote_value4= dataSnapshot.child("quote_value4").getValue().toString();
                    quote_value5= dataSnapshot.child("quote_value5").getValue().toString();
                    quote_value6= dataSnapshot.child("quote_value6").getValue().toString();
                    quote_value7= dataSnapshot.child("quote_value7").getValue().toString();
                    quote_value8= dataSnapshot.child("quote_value8").getValue().toString();
                    quote_value9= dataSnapshot.child("quote_value9").getValue().toString();
                    quote_value10= dataSnapshot.child("quote_value10").getValue().toString();
                    quote_value11= dataSnapshot.child("quote_value11").getValue().toString();
                    quote_value12= dataSnapshot.child("quote_value12").getValue().toString();
                    quote_value13= dataSnapshot.child("quote_value13").getValue().toString();
                    myInvestmentFundTransRef= FirebaseDatabase.getInstance().getReference().child("Investment Fund Transactions").child(currentUserID+fund_code);

                    fund_investment1 = dataSnapshot.child("fund_investment1").getValue().toString();
                    fund_investment2 = dataSnapshot.child("fund_investment2").getValue().toString();
                    fund_investment3 = dataSnapshot.child("fund_investment3").getValue().toString();
                    fund_investment4 = dataSnapshot.child("fund_investment4").getValue().toString();
                    fund_investment5 = dataSnapshot.child("fund_investment5").getValue().toString();
                    fund_investment6 = dataSnapshot.child("fund_investment6").getValue().toString();
                    fund_investment7 = dataSnapshot.child("fund_investment7").getValue().toString();
                    fund_investment8 = dataSnapshot.child("fund_investment8").getValue().toString();
                    fund_investment9 = dataSnapshot.child("fund_investment9").getValue().toString();
                    fund_investment10 = dataSnapshot.child("fund_investment10").getValue().toString();
                    fund_investment11 = dataSnapshot.child("fund_investment11").getValue().toString();
                    fund_investment12 = dataSnapshot.child("fund_investment12").getValue().toString();

                    fund_monthly_profit1 = dataSnapshot.child("fund_monthly_profit1").getValue().toString();
                    fund_monthly_profit2 = dataSnapshot.child("fund_monthly_profit2").getValue().toString();
                    fund_monthly_profit3 = dataSnapshot.child("fund_monthly_profit3").getValue().toString();
                    fund_monthly_profit4 = dataSnapshot.child("fund_monthly_profit4").getValue().toString();
                    fund_monthly_profit5 = dataSnapshot.child("fund_monthly_profit5").getValue().toString();
                    fund_monthly_profit6 = dataSnapshot.child("fund_monthly_profit6").getValue().toString();
                    fund_monthly_profit7 = dataSnapshot.child("fund_monthly_profit7").getValue().toString();
                    fund_monthly_profit8 = dataSnapshot.child("fund_monthly_profit8").getValue().toString();
                    fund_monthly_profit9 = dataSnapshot.child("fund_monthly_profit9").getValue().toString();
                    fund_monthly_profit10 = dataSnapshot.child("fund_monthly_profit10").getValue().toString();
                    fund_monthly_profit11 = dataSnapshot.child("fund_monthly_profit11").getValue().toString();
                    fund_monthly_profit12 = dataSnapshot.child("fund_monthly_profit12").getValue().toString();

                    double quotes_circulating_in_this_fund = Double.parseDouble(quotes_circulating);
                    DecimalFormat decimalFormat = new DecimalFormat("0");
                    String quotes_circulating_in_this_fund_st = decimalFormat.format(quotes_circulating_in_this_fund);

                    txtTittle.setText(fund_name);
                    txtFundDescription.setText(fund_description);
                    txtCurrentQuoteValue.setText("Valor Cuota Actual: "+quote_value+" "+fund_currency);
                    txtTotalInvestment.setText("Patrimonio total: "+fund_investment+" "+fund_currency);
                    txtQuotesCirculating.setText("Cuotas en circulación: "+quotes_circulating_in_this_fund_st);
                    txtFundAnualProfit.setText(fund_anual_profit+"%");
                    txtFundMonthlyProfit.setText(fund_monthly_profit+"%");
                    txtMinDaysFund.setText("Plazo mínimo de permanencia: "+min_days+" días");
                    txtRecomendedDays.setText("Plazo recomendado: "+recommended_days+" días");
                    txtFundRisk.setText("Riesgo: "+fund_risk);
                    txtSuscriptionFee.setText("Suscripción por cuota: "+suscription_quote_fee+"%");
                    txtManagementFee.setText("Administración anual del fondo: "+fund_management_fee+"% del patrimonio");
                    txtFundRescue.setText("Rescate anticipado: "+fund_rescue+"% del rescate");


                    double circulating_quotes = Double.parseDouble(quotes_circulating);
                    double fund_equity = Double.parseDouble(fund_investment);
                    double new_quote_value = fund_equity/circulating_quotes;

                    DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
                    String current_quote_value_new = decimalFormat1.format(new_quote_value);

                    HashMap fundMap = new HashMap();
                    if (month == 1)
                    {
                        double current_qvalue = Double.parseDouble(quote_value1);
                        double last_qvalue = Double.parseDouble(quote_value13);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit1",current_monthly_profit);
                        txtProfit1.setText(fund_monthly_profit+"%");
                        txtFundAnualProfit.setText(fund_monthly_profit+"%");

                        fundMap.put("current_month","Enero");
                        fundMap.put("fund_investment1",fund_investment);
                        fundMap.put("quote_value1",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth1.setBackgroundResource(R.color.redColor);

                        txtTotalFund1.setText(fund_investment+" "+fund_currency);
                        txtStartQuote1.setText(quote_value13+" "+fund_currency);
                        txtEndQuote1.setText(quote_value+" "+fund_currency);
                    }
                    if (month == 2)
                    {
                        double current_qvalue = Double.parseDouble(quote_value2);
                        double last_qvalue = Double.parseDouble(quote_value1);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit2",current_monthly_profit);
                        txtProfit2.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Febrero");
                        fundMap.put("fund_investment2",fund_investment);
                        fundMap.put("quote_value2",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth2.setBackgroundResource(R.color.redColor);

                        txtTotalFund2.setText(fund_investment+" "+fund_currency);
                        txtStartQuote2.setText(quote_value1+" "+fund_currency);
                        txtEndQuote2.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                    }
                    if (month == 3)
                    {
                        double current_qvalue = Double.parseDouble(quote_value3);
                        double last_qvalue = Double.parseDouble(quote_value2);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit3",current_monthly_profit);
                        txtProfit3.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Marzo");
                        fundMap.put("fund_investment3",fund_investment);
                        fundMap.put("quote_value3",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth3.setBackgroundResource(R.color.redColor);

                        txtTotalFund3.setText(fund_investment+" "+fund_currency);
                        txtStartQuote3.setText(quote_value2+" "+fund_currency);
                        txtEndQuote3.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                    }
                    if (month == 4)
                    {
                        double current_qvalue = Double.parseDouble(quote_value4);
                        double last_qvalue = Double.parseDouble(quote_value3);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit4",current_monthly_profit);
                        txtProfit4.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Abril");
                        fundMap.put("fund_investment4",fund_investment);
                        fundMap.put("quote_value4",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth4.setBackgroundResource(R.color.redColor);

                        txtTotalFund4.setText(fund_investment+" "+fund_currency);
                        txtStartQuote4.setText(quote_value3+" "+fund_currency);
                        txtEndQuote4.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                    }
                    if (month == 5)
                    {
                        double current_qvalue = Double.parseDouble(quote_value5);
                        double last_qvalue = Double.parseDouble(quote_value4);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit5",current_monthly_profit);
                        txtProfit5.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Mayo");
                        fundMap.put("fund_investment5",fund_investment);
                        fundMap.put("quote_value5",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth5.setBackgroundResource(R.color.redColor);

                        txtTotalFund5.setText(fund_investment+" "+fund_currency);
                        txtStartQuote5.setText(quote_value4+" "+fund_currency);
                        txtEndQuote5.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");
                    }
                    if (month == 6)
                    {
                        double current_qvalue = Double.parseDouble(quote_value6);
                        double last_qvalue = Double.parseDouble(quote_value5);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit6",current_monthly_profit);
                        txtProfit6.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Junio");
                        fundMap.put("fund_investment6",fund_investment);
                        fundMap.put("quote_value6",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth6.setBackgroundResource(R.color.redColor);

                        txtTotalFund6.setText(fund_investment+" "+fund_currency);
                        txtStartQuote6.setText(quote_value5+" "+fund_currency);
                        txtEndQuote6.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");

                        txtStartQuote5.setText(quote_value4);
                        txtEndQuote5.setText(quote_value5);
                        txtTotalFund5.setText(fund_investment5+" "+fund_currency);
                        txtProfit5.setText(fund_monthly_profit5+"%");


                    }
                    if (month == 7)
                    {
                        double current_qvalue = Double.parseDouble(quote_value7);
                        double last_qvalue = Double.parseDouble(quote_value6);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit7",current_monthly_profit);
                        txtProfit7.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Julio");
                        fundMap.put("fund_investment7",fund_investment);
                        fundMap.put("quote_value7",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth7.setBackgroundResource(R.color.redColor);

                        txtTotalFund7.setText(fund_investment+" "+fund_currency);
                        txtStartQuote7.setText(quote_value6+" "+fund_currency);
                        txtEndQuote7.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");

                        txtStartQuote5.setText(quote_value4);
                        txtEndQuote5.setText(quote_value5);
                        txtTotalFund5.setText(fund_investment5+" "+fund_currency);
                        txtProfit5.setText(fund_monthly_profit5+"%");

                        txtStartQuote6.setText(quote_value5);
                        txtEndQuote6.setText(quote_value6);
                        txtTotalFund6.setText(fund_investment6+" "+fund_currency);
                        txtProfit6.setText(fund_monthly_profit6+"%");

                    }
                    if (month == 8)
                    {
                        double current_qvalue = Double.parseDouble(quote_value8);
                        double last_qvalue = Double.parseDouble(quote_value7);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit8",current_monthly_profit);
                        txtProfit8.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Agosto");
                        fundMap.put("fund_investment8",fund_investment);
                        fundMap.put("quote_value8",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth8.setBackgroundResource(R.color.redColor);

                        txtTotalFund8.setText(fund_investment+" "+fund_currency);
                        txtStartQuote8.setText(quote_value7+" "+fund_currency);
                        txtEndQuote8.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");

                        txtStartQuote5.setText(quote_value4);
                        txtEndQuote5.setText(quote_value5);
                        txtTotalFund5.setText(fund_investment5+" "+fund_currency);
                        txtProfit5.setText(fund_monthly_profit5+"%");

                        txtStartQuote6.setText(quote_value5);
                        txtEndQuote6.setText(quote_value6);
                        txtTotalFund6.setText(fund_investment6+" "+fund_currency);
                        txtProfit6.setText(fund_monthly_profit6+"%");

                        txtStartQuote7.setText(quote_value6);
                        txtEndQuote7.setText(quote_value7);
                        txtTotalFund7.setText(fund_investment7+" "+fund_currency);
                        txtProfit7.setText(fund_monthly_profit7+"%");
                    }
                    if (month == 9)
                    {
                        double current_qvalue = Double.parseDouble(quote_value9);
                        double last_qvalue = Double.parseDouble(quote_value8);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit9",current_monthly_profit);
                        txtProfit9.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Setiembre");
                        fundMap.put("fund_investment9",fund_investment);
                        fundMap.put("quote_value9",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth9.setBackgroundResource(R.color.redColor);

                        txtTotalFund9.setText(fund_investment+" "+fund_currency);
                        txtStartQuote9.setText(quote_value8+" "+fund_currency);
                        txtEndQuote9.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");

                        txtStartQuote5.setText(quote_value4);
                        txtEndQuote5.setText(quote_value5);
                        txtTotalFund5.setText(fund_investment5+" "+fund_currency);
                        txtProfit5.setText(fund_monthly_profit5+"%");

                        txtStartQuote6.setText(quote_value5);
                        txtEndQuote6.setText(quote_value6);
                        txtTotalFund6.setText(fund_investment6+" "+fund_currency);
                        txtProfit6.setText(fund_monthly_profit6+"%");

                        txtStartQuote7.setText(quote_value6);
                        txtEndQuote7.setText(quote_value7);
                        txtTotalFund7.setText(fund_investment7+" "+fund_currency);
                        txtProfit7.setText(fund_monthly_profit7+"%");

                        txtStartQuote8.setText(quote_value7);
                        txtEndQuote8.setText(quote_value8);
                        txtTotalFund8.setText(fund_investment8+" "+fund_currency);
                        txtProfit8.setText(fund_monthly_profit8+"%");

                    }
                    if (month == 10)
                    {
                        double current_qvalue = Double.parseDouble(quote_value10);
                        double last_qvalue = Double.parseDouble(quote_value9);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit10",current_monthly_profit);
                        txtProfit10.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Octubre");
                        fundMap.put("fund_investment10",fund_investment);
                        fundMap.put("quote_value10",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth10.setBackgroundResource(R.color.redColor);

                        txtTotalFund10.setText(fund_investment+" "+fund_currency);
                        txtStartQuote10.setText(quote_value9+" "+fund_currency);
                        txtEndQuote10.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");

                        txtStartQuote5.setText(quote_value4);
                        txtEndQuote5.setText(quote_value5);
                        txtTotalFund5.setText(fund_investment5+" "+fund_currency);
                        txtProfit5.setText(fund_monthly_profit5+"%");

                        txtStartQuote6.setText(quote_value5);
                        txtEndQuote6.setText(quote_value6);
                        txtTotalFund6.setText(fund_investment6+" "+fund_currency);
                        txtProfit6.setText(fund_monthly_profit6+"%");

                        txtStartQuote7.setText(quote_value6);
                        txtEndQuote7.setText(quote_value7);
                        txtTotalFund7.setText(fund_investment7+" "+fund_currency);
                        txtProfit7.setText(fund_monthly_profit7+"%");

                        txtStartQuote8.setText(quote_value7);
                        txtEndQuote8.setText(quote_value8);
                        txtTotalFund8.setText(fund_investment8+" "+fund_currency);
                        txtProfit8.setText(fund_monthly_profit8+"%");

                        txtStartQuote9.setText(quote_value8);
                        txtEndQuote9.setText(quote_value9);
                        txtTotalFund9.setText(fund_investment9+" "+fund_currency);
                        txtProfit9.setText(fund_monthly_profit9+"%");

                    }
                    if (month == 11)
                    {
                        double current_qvalue = Double.parseDouble(quote_value11);
                        double last_qvalue = Double.parseDouble(quote_value10);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit11",current_monthly_profit);
                        txtProfit11.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Nomviembre");
                        fundMap.put("fund_investment11",fund_investment);
                        fundMap.put("quote_value11",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth11.setBackgroundResource(R.color.redColor);

                        txtTotalFund11.setText(fund_investment+" "+fund_currency);
                        txtStartQuote11.setText(quote_value10+" "+fund_currency);
                        txtEndQuote11.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");

                        txtStartQuote5.setText(quote_value4);
                        txtEndQuote5.setText(quote_value5);
                        txtTotalFund5.setText(fund_investment5+" "+fund_currency);
                        txtProfit5.setText(fund_monthly_profit5+"%");

                        txtStartQuote6.setText(quote_value5);
                        txtEndQuote6.setText(quote_value6);
                        txtTotalFund6.setText(fund_investment6+" "+fund_currency);
                        txtProfit6.setText(fund_monthly_profit6+"%");

                        txtStartQuote7.setText(quote_value6);
                        txtEndQuote7.setText(quote_value7);
                        txtTotalFund7.setText(fund_investment7+" "+fund_currency);
                        txtProfit7.setText(fund_monthly_profit7+"%");

                        txtStartQuote8.setText(quote_value7);
                        txtEndQuote8.setText(quote_value8);
                        txtTotalFund8.setText(fund_investment8+" "+fund_currency);
                        txtProfit8.setText(fund_monthly_profit8+"%");

                        txtStartQuote9.setText(quote_value8);
                        txtEndQuote9.setText(quote_value9);
                        txtTotalFund9.setText(fund_investment9+" "+fund_currency);
                        txtProfit9.setText(fund_monthly_profit9+"%");

                        txtStartQuote10.setText(quote_value9);
                        txtEndQuote10.setText(quote_value10);
                        txtTotalFund10.setText(fund_investment10+" "+fund_currency);
                        txtProfit10.setText(fund_monthly_profit10+"%");

                    }
                    if (month == 12)
                    {
                        double current_qvalue = Double.parseDouble(quote_value12);
                        double last_qvalue = Double.parseDouble(quote_value11);
                        double monthly_profit_dec = (current_qvalue-last_qvalue)/current_qvalue;
                        double monthly_profit = monthly_profit_dec*100;
                        String current_monthly_profit = decimalFormat.format(monthly_profit);
                        fundMap.put("fund_monthly_profit",current_monthly_profit);
                        fundMap.put("fund_monthly_profit12",current_monthly_profit);
                        txtProfit12.setText(fund_monthly_profit+"%");

                        double first_quuote_value = Double.parseDouble(quote_value13);

                        double anual_profit_dec = (current_qvalue-first_quuote_value)/current_qvalue;
                        double anual_profit = anual_profit_dec*100;
                        String anual_profit_st = decimalFormat.format(anual_profit);
                        txtFundAnualProfit.setText(anual_profit_st+"%");
                        fundMap.put("fund_anual_profit",anual_profit_st);

                        fundMap.put("current_month","Diciembre");
                        fundMap.put("fund_investment12",fund_investment);
                        fundMap.put("quote_value12",current_quote_value_new);
                        fundMap.put("quote_value",current_quote_value_new);

                        txtMonth12.setBackgroundResource(R.color.redColor);

                        txtTotalFund12.setText(fund_investment+" "+fund_currency);
                        txtStartQuote12.setText(quote_value11+" "+fund_currency);
                        txtEndQuote12.setText(quote_value+" "+fund_currency);

                        //Load previuos months information
                        txtStartQuote1.setText(quote_value13);
                        txtEndQuote1.setText(quote_value1);
                        txtTotalFund1.setText(fund_investment1+" "+fund_currency);
                        txtProfit1.setText(fund_monthly_profit1+"%");

                        txtStartQuote2.setText(quote_value1);
                        txtEndQuote2.setText(quote_value2);
                        txtTotalFund2.setText(fund_investment2+" "+fund_currency);
                        txtProfit2.setText(fund_monthly_profit2+"%");

                        txtStartQuote3.setText(quote_value2);
                        txtEndQuote3.setText(quote_value3);
                        txtTotalFund3.setText(fund_investment3+" "+fund_currency);
                        txtProfit3.setText(fund_monthly_profit3+"%");

                        txtStartQuote4.setText(quote_value3);
                        txtEndQuote4.setText(quote_value4);
                        txtTotalFund4.setText(fund_investment4+" "+fund_currency);
                        txtProfit4.setText(fund_monthly_profit4+"%");

                        txtStartQuote5.setText(quote_value4);
                        txtEndQuote5.setText(quote_value5);
                        txtTotalFund5.setText(fund_investment5+" "+fund_currency);
                        txtProfit5.setText(fund_monthly_profit5+"%");

                        txtStartQuote6.setText(quote_value5);
                        txtEndQuote6.setText(quote_value6);
                        txtTotalFund6.setText(fund_investment6+" "+fund_currency);
                        txtProfit6.setText(fund_monthly_profit6+"%");

                        txtStartQuote7.setText(quote_value6);
                        txtEndQuote7.setText(quote_value7);
                        txtTotalFund7.setText(fund_investment7+" "+fund_currency);
                        txtProfit7.setText(fund_monthly_profit7+"%");

                        txtStartQuote8.setText(quote_value7);
                        txtEndQuote8.setText(quote_value8);
                        txtTotalFund8.setText(fund_investment8+" "+fund_currency);
                        txtProfit8.setText(fund_monthly_profit8+"%");

                        txtStartQuote9.setText(quote_value8);
                        txtEndQuote9.setText(quote_value9);
                        txtTotalFund9.setText(fund_investment9+" "+fund_currency);
                        txtProfit9.setText(fund_monthly_profit9+"%");

                        txtStartQuote10.setText(quote_value9);
                        txtEndQuote10.setText(quote_value10);
                        txtTotalFund10.setText(fund_investment10+" "+fund_currency);
                        txtProfit10.setText(fund_monthly_profit10+"%");

                        txtStartQuote11.setText(quote_value10);
                        txtEndQuote11.setText(quote_value11);
                        txtTotalFund11.setText(fund_investment11+" "+fund_currency);
                        txtProfit11.setText(fund_monthly_profit11+"%");

                        Calendar cal = Calendar.getInstance();
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        if (dayOfMonth == 31)
                        {
                            fundMap.put("fund_investment13",fund_investment);
                            fundMap.put("quote_value13",current_quote_value_new);
                            fundMap.put("fund_monthly_profit13",current_monthly_profit);
                            txtProfit13.setText(fund_monthly_profit+"%");

                            txtTotalFund13.setText(fund_investment+" "+fund_currency);
                            txtStartQuote13.setText(quote_value12+" "+fund_currency);
                            txtEndQuote13.setText(quote_value+" "+fund_currency);

                            txtTotalFund1.setText("-");
                            txtTotalFund2.setText("-");
                            txtTotalFund3.setText("-");
                            txtTotalFund4.setText("-");
                            txtTotalFund5.setText("-");
                            txtTotalFund6.setText("-");
                            txtTotalFund7.setText("-");
                            txtTotalFund8.setText("-");
                            txtTotalFund9.setText("-");
                            txtTotalFund10.setText("-");
                            txtTotalFund11.setText("-");
                            txtTotalFund12.setText("-");
                            txtStartQuote1.setText("-");
                            txtStartQuote2.setText("-");
                            txtStartQuote3.setText("-");
                            txtStartQuote4.setText("-");
                            txtStartQuote5.setText("-");
                            txtStartQuote6.setText("-");
                            txtStartQuote7.setText("-");
                            txtStartQuote8.setText("-");
                            txtStartQuote9.setText("-");
                            txtStartQuote10.setText("-");
                            txtStartQuote11.setText("-");
                            txtStartQuote12.setText("-");
                            txtEndQuote1.setText("-");
                            txtEndQuote2.setText("-");
                            txtEndQuote3.setText("-");
                            txtEndQuote4.setText("-");
                            txtEndQuote5.setText("-");
                            txtEndQuote6.setText("-");
                            txtEndQuote7.setText("-");
                            txtEndQuote8.setText("-");
                            txtEndQuote9.setText("-");
                            txtEndQuote10.setText("-");
                            txtEndQuote11.setText("-");
                            txtEndQuote12.setText("-");
                            txtProfit1.setText("-");
                            txtProfit2.setText("-");
                            txtProfit3.setText("-");
                            txtProfit4.setText("-");
                            txtProfit5.setText("-");
                            txtProfit6.setText("-");
                            txtProfit7.setText("-");
                            txtProfit8.setText("-");
                            txtProfit9.setText("-");
                            txtProfit10.setText("-");
                            txtProfit11.setText("-");
                            txtProfit12.setText("-");

                        }

                    }
                    investment_fund_ref.updateChildren(fundMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            LineDataSet lineDataSet1 = new LineDataSet(dataValues1(),"Valor Cuota");
                            lineDataSet1.setLineWidth(2);
                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(lineDataSet1);

                            LineData data = new LineData(dataSets);
                            mLineChart.setData(data);
                            mLineChart.invalidate();
                            mLineChart.getDescription().setEnabled(false);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_verification = dataSnapshot.child("user_verification").getValue().toString();
                basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        investmentFundTransactionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentUserID+fund_code) || !dataSnapshot.child(currentUserID+fund_code).hasChild("fund_code"))
                {
                    //Crear referencia con valores cero
                    HashMap postMap = new HashMap();
                    postMap.put("uid",currentUserID);
                    postMap.put("fund_code",fund_code);
                    postMap.put("last_quote_value","0.00");
                    postMap.put("last_investment","0.00");
                    postMap.put("my_quotes","0.00");
                    postMap.put("my_equity","0.00");
                    postMap.put("last_transaction_date","-");
                    investmentFundTransactionsRef.child(currentUserID+fund_code).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                //Toast.makeText(InvestmentFundDetailActivity.this, "Se ha creado una referencia para este fondo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    //MOSTRAR MIS DATOS DEL FONDO
                    //oast.makeText(InvestmentFundDetailActivity.this, "Aqui se muestran los datos de mi participación del fondo", Toast.LENGTH_SHORT).show();
                    investmentFundTransactionsRef.child(currentUserID+fund_code).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                last_quote_value = dataSnapshot.child("last_quote_value").getValue().toString();
                                last_investment = dataSnapshot.child("last_investment").getValue().toString();
                                my_quotes = dataSnapshot.child("my_quotes").getValue().toString();
                                my_equity = dataSnapshot.child("my_equity").getValue().toString();
                                last_transaction_date = dataSnapshot.child("last_transaction_date").getValue().toString();

                                double my_quotes_in_fund = Double.parseDouble(my_quotes);
                                int innnn = (int) Math.round(my_quotes_in_fund);
                                String my_quotes_in_fund_st = String.valueOf(innnn);

                                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                                double current_quote_value = Double.parseDouble(quote_value);
                                double my_participation = innnn*current_quote_value;
                                String my_participation_st =  decimalFormat.format(my_participation);

                                txtMyLastQuoteValue.setText("Último Valor Cuota adquirido en: "+last_quote_value+" "+fund_currency);
                                txtMyLastTransaction.setText("Último aporte al fondo: "+last_investment+" "+fund_currency);
                                txtMyQuotesNumber.setText("Cuotas adquiridas: "+my_quotes_in_fund_st);
                                txtMyFundInvestment.setText("Mis aportes acumulados: "+my_equity+" "+fund_currency);
                                txtMyLastTransactionDate.setText("Fecha del último aporte: "+last_transaction_date);
                                txtMyFundInvestmentParticipation.setText("Mi patrimonio actual en el fondo: "+my_participation_st+" "+fund_currency);

                                Calendar calForDate = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                saveCurrentDate =currentDate.format(calForDate.getTime());
                                try {
                                    Date date1 = currentDate.parse(last_transaction_date);
                                    Date date2 = currentDate.parse(saveCurrentDate);
                                    long diff = date2.getTime() - date1.getTime();
                                    txtDaysSinceLastTransaction.setText("Días transcurridos: "+ TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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

        btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvestmentFundDetailActivity.this, RegisterInvestmentInFundActivity.class);
                intent.putExtra("PostKey", postKey);
                startActivity(intent);
                //showInvestDialog();
            }
        });

        btnRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvestmentFundDetailActivity.this, InvestmentFundRescueActivity.class);
                intent.putExtra("PostKey", postKey);
                startActivity(intent);
            }
        });

        recyclerFound = findViewById(R.id.recyclerFound);
        recyclerFound.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerFound.setLayoutManager(linearLayoutManager);

        displayFundAssets();

    }

    private void displayFundAssets() {
        Query showPostMostRecentOrder = fundAssetsRef.orderByChild("fund_asset_last_transaction");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FundAssetsModel, PostViewHolder>
                (FundAssetsModel.class,R.layout.investment_fund_asset_layout,InvestmentFundDetailActivity.PostViewHolder.class,showPostMostRecentOrder) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, FundAssetsModel model, int position) {
                viewHolder.setFund_asset_name(model.getFund_asset_name());
                viewHolder.setFund_asset_type(model.getFund_asset_type());
                viewHolder.setFund_asset_last_transaction(model.getFund_asset_last_transaction());
                viewHolder.setFund_asset_current_position(model.getFund_asset_current_position());
                viewHolder.setFund_asset_participation(model.getFund_asset_participation());
                viewHolder.setFund_asset_amount(model.getFund_asset_amount()+" "+fund_currency);
                viewHolder.setFund_asset_profit(model.getFund_asset_profit());
            }
        };
        recyclerFound.setAdapter(firebaseRecyclerAdapter);
    }

    private void showInvestDialog() {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("INVERTIR EN EL FONDO");
        dialog.setIcon(R.drawable.money2_icon);

        LayoutInflater inflater = LayoutInflater.from(this);
        View invest_fund = inflater.inflate(R.layout.investment_fund_invest_layout,null);

        quotes.add("1");quotes.add("2");quotes.add("3");quotes.add("4");quotes.add("5");quotes.add("6");quotes.add("7");quotes.add("8");quotes.add("9");quotes.add("10");
        quotes.add("11");quotes.add("12");quotes.add("13");quotes.add("14");quotes.add("15");quotes.add("16");quotes.add("17");quotes.add("18");quotes.add("19");quotes.add("20");
        quotes.add("21");quotes.add("22");quotes.add("23");quotes.add("24");quotes.add("25");quotes.add("26");quotes.add("27");quotes.add("28");quotes.add("29");quotes.add("30");
        quotes.add("31");quotes.add("32");quotes.add("33");quotes.add("34");quotes.add("35");quotes.add("36");quotes.add("37");quotes.add("38");quotes.add("39");quotes.add("40");
        quotes.add("41");quotes.add("42");quotes.add("43");quotes.add("44");quotes.add("45");quotes.add("46");quotes.add("47");quotes.add("48");quotes.add("49");quotes.add("50");
        quotes.add("51");quotes.add("52");quotes.add("53");quotes.add("54");quotes.add("55");quotes.add("56");quotes.add("57");quotes.add("58");quotes.add("59");quotes.add("60");
        quotes.add("61");quotes.add("62");quotes.add("63");quotes.add("64");quotes.add("65");quotes.add("66");quotes.add("67");quotes.add("68");quotes.add("69");quotes.add("70");
        quotes.add("71");quotes.add("72");quotes.add("73");quotes.add("74");quotes.add("75");quotes.add("76");quotes.add("77");quotes.add("78");quotes.add("79");quotes.add("80");
        quotes.add("81");quotes.add("82");quotes.add("83");quotes.add("84");quotes.add("85");quotes.add("86");quotes.add("87");quotes.add("88");quotes.add("89");quotes.add("90");
        quotes.add("91");quotes.add("92");quotes.add("93");quotes.add("94");quotes.add("95");quotes.add("96");quotes.add("97");quotes.add("98");quotes.add("99");quotes.add("100");
        quotes.add("101");quotes.add("102");quotes.add("103");quotes.add("104");quotes.add("105");quotes.add("106");quotes.add("107");quotes.add("108");quotes.add("109");quotes.add("110");
        quotes.add("111");quotes.add("112");quotes.add("113");quotes.add("114");quotes.add("115");quotes.add("116");quotes.add("117");quotes.add("118");quotes.add("119");quotes.add("120");



        final TextView txtCurrentQuoteValue,txtTotalToInvest,txtCurrencyRate,txtQuotesAdquisitionAmmount,txtSuscriptionFeeAmmount;
        final RadioButton rdPenAccount,rdUsdAccount;
        final Button btnInvestInFound;
        final CheckBox cbAgreement;

        txtCurrentQuoteValue = invest_fund.findViewById(R.id.txtCurrentQuoteValue);
        rdPenAccount = invest_fund.findViewById(R.id.rdPenAccount);
        rdUsdAccount = invest_fund.findViewById(R.id.rdUsdAccount);
        btnQuotesNumber = invest_fund.findViewById(R.id.btnQuotesNumber);
        txtQuotesAdquisitionAmmount = invest_fund.findViewById(R.id.txtQuotesAdquisitionAmmount);
        txtSuscriptionFeeAmmount = invest_fund.findViewById(R.id.txtSuscriptionFeeAmmount);
        txtTotalToInvest = invest_fund.findViewById(R.id.txtTotalInvestment);
        cbAgreement = invest_fund.findViewById(R.id.cbAgreement);
        btnInvestInFound= invest_fund.findViewById(R.id.btnInvestInFound);
        txtCurrencyRate = invest_fund.findViewById(R.id.txtCurrencyRate);

        txtCurrentQuoteValue.setText("Valor Cuota Actual: "+quote_value+" "+fund_currency);
        rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+basic_account_pen);
        rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+basic_account_usd);
        txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                invest_currency = "PEN";
                rdPenAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdPenAccount.setTextColor(Color.BLACK);
                rdUsdAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdUsdAccount.setTextColor(Color.BLACK);
                rdUsdAccount.setEnabled(false);
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                invest_currency = "USD";
                rdPenAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdPenAccount.setTextColor(Color.BLACK);
                rdUsdAccount.setBackground(getDrawable(R.drawable.edit_text_background1));
                rdUsdAccount.setTextColor(Color.BLACK);
                rdPenAccount.setEnabled(false);
            }
        });

        btnQuotesNumber.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    rdPenAccount.setBackground(getDrawable(R.drawable.button3_background));
                    rdPenAccount.setTextColor(Color.WHITE);
                    rdUsdAccount.setBackground(getDrawable(R.drawable.button3_background));
                    rdUsdAccount.setTextColor(Color.WHITE);
                    Toast.makeText(InvestmentFundDetailActivity.this, "SELECCIONA UNA CUENTA PRIMERO", Toast.LENGTH_SHORT).show();
                }
                else {
                    spinnerQuotes.showSpinerDialog();
                }
            }
        });

        spinnerQuotes = new SpinnerDialog(InvestmentFundDetailActivity.this,quotes,"Selecciona el nùmero de cuotas");
        spinnerQuotes.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnQuotesNumber.setText(item);

                double quote_number = Integer.parseInt(item);
                double quote_value_now = Double.parseDouble(quote_value);
                double total_to_investment = 0;
                double sell_rate = Double.parseDouble(currency_rate_sell);
                double buy_rate = Double.parseDouble(currency_rate_buy);
                if (fund_currency.equals(invest_currency))
                {
                    total_to_investment = quote_number*quote_value_now;
                }
                if (fund_currency.equals("USD") && invest_currency.equals("PEN"))
                {
                    total_to_investment = (quote_number*quote_value_now)*sell_rate;
                }
                if (fund_currency.equals("PEN") && invest_currency.equals("USD"))
                {
                    total_to_investment = (quote_number*quote_value_now)/buy_rate;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String total_invest_in_the_fund = decimalFormat.format(total_to_investment);
                txtQuotesAdquisitionAmmount.setText("Adquisición de cuotas: "+total_invest_in_the_fund+" "+invest_currency);
                double quotes_fee = Double.parseDouble(suscription_quote_fee);
                double fee_ammount = (quotes_fee/100)*total_to_investment;
                real_total_invest = total_to_investment+fee_ammount;
                String fee_ammount_st = decimalFormat.format(fee_ammount);
                String my_real_tota_investment_st = decimalFormat.format(real_total_invest);
                txtSuscriptionFeeAmmount.setText("Suscripción de cuotas: "+fee_ammount_st+" "+invest_currency);
                txtTotalToInvest.setText("TOTAL A INVERTIR: "+my_real_tota_investment_st+" "+invest_currency);
            }
        });

        btnInvestInFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    Toast.makeText(InvestmentFundDetailActivity.this, "Debes seleccionar una cuenta primero", Toast.LENGTH_LONG).show();
                }
                if (btnQuotesNumber.getText().toString().equals("0"))
                {
                    Toast.makeText(InvestmentFundDetailActivity.this, "Debes comprar al menos una cuota", Toast.LENGTH_LONG).show();
                }
                if (!cbAgreement.isChecked())
                {
                    Toast.makeText(InvestmentFundDetailActivity.this, "Debes declarar el concentimiento para esta operación", Toast.LENGTH_LONG).show();
                }
                quote_numbers = Integer.parseInt(btnQuotesNumber.getText().toString());
                double quote_value_now_current = Double.parseDouble(quote_value);
                total_to_investment_fund = quote_numbers*quote_value_now_current;
                double my_account_pen = Double.parseDouble(basic_account_pen);
                double my_account_usd = Double.parseDouble(basic_account_usd);

                if (invest_currency.equals("PEN"))
                {
                    if (my_account_pen < real_total_invest)
                    {
                        Toast.makeText(InvestmentFundDetailActivity.this, "No cuentas con el dinero suficiente para esta operación", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(InvestmentFundDetailActivity.this, "Operación Exitosa", Toast.LENGTH_SHORT).show();
                        loadingBar.setTitle("Realizando transacción");
                        loadingBar.setMessage("Cargando...");
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(false);
                        registerMyInvestmentFundTransaction();
                        registerMyOperations();
                    }
                }
                if (invest_currency.equals("USD"))
                {
                    if (my_account_usd < real_total_invest)
                    {

                        Toast.makeText(InvestmentFundDetailActivity.this, "No cuentas con el dinero suficiente para esta operación", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(InvestmentFundDetailActivity.this, "Operación Exitosa", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.setView(invest_fund);
        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void registerMyOperations() {
    }

    private void registerMyInvestmentFundTransaction() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        investmentFundTransactionsRef.child(currentUserID+fund_code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    last_quote_value = dataSnapshot.child("last_quote_value").getValue().toString();
                    last_investment = dataSnapshot.child("last_investment").getValue().toString();
                    my_quotes = dataSnapshot.child("my_quotes").getValue().toString();
                    my_equity = dataSnapshot.child("my_equity").getValue().toString();
                    last_transaction_date = dataSnapshot.child("last_transaction_date").getValue().toString();

                    double current_quotes_number = Double.parseDouble(my_quotes);
                    double current_equity = Double.parseDouble(my_equity);

                    double add_new_quotes = Double.parseDouble(btnQuotesNumber.getText().toString());
                    double add_new_quote_value = Double.parseDouble(quote_value);
                    double add_new_last_investment = add_new_quote_value*add_new_quotes;
                    double add_new_quotes_numer = current_quotes_number+add_new_quotes;
                    double add_new_equity = current_equity+add_new_last_investment;

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String last_investment_st = decimalFormat.format(add_new_last_investment);
                    String new_quotes_number_st = String.valueOf(add_new_quotes_numer);
                    String my_new_equity_st = decimalFormat.format(add_new_equity);

                    HashMap postMap = new HashMap();
                    postMap.put("last_quote_value",quote_value);
                    postMap.put("last_investment",last_investment_st);
                    postMap.put("my_quotes",new_quotes_number_st);
                    postMap.put("my_equity",my_new_equity_st);
                    postMap.put("last_transaction_date",saveCurrentDate);
                    myInvestmentFundTransRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            //Toast.makeText(InvestmentFundDetailActivity.this, "HOLA", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });
                }
                /*{


                    investmentFundTransactionsRef.child(currentUserID+fund_code).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(InvestmentFundDetailActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                loadingBar.dismiss();
                            }
                            if (task.isSuccessful())
                            {
                                loadingBar.setMessage("Casi listo...");

                                int current_quotes_circulating = Integer.parseInt(quotes_circulating);
                                double current_equity_in_fund = Double.parseDouble(fund_investment);

                                double quote_value_now_2 = Double.parseDouble(quote_value);

                                int add_new_quotes_to_fund = Integer.parseInt(btnQuotesNumber.getText().toString());
                                double add_new_equity_to_fund = add_new_quotes_to_fund*quote_value_now_2;

                                int total_quotes_circulating = current_quotes_circulating+add_new_quotes_to_fund;
                                double total_equity_in_fund = current_equity_in_fund+add_new_equity_to_fund;

                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                String total_quotes_circulating_st = String.valueOf(total_quotes_circulating);
                                String total_equity_in_fund_st = decimalFormat.format(total_equity_in_fund);

                                HashMap fundMap = new HashMap();
                                fundMap.put("fund_investment",total_equity_in_fund_st);
                                fundMap.put("quotes_circulating",total_quotes_circulating_st);

                                investment_fund_ref.updateChildren(fundMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful())
                                        {
                                            loadingBar.dismiss();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private ArrayList<Entry> dataValues1()
    {
        f1 = Float.parseFloat(quote_value1);
        f2 = Float.parseFloat(quote_value2);
        f3 = Float.parseFloat(quote_value3);
        f4 = Float.parseFloat(quote_value4);
        f5 = Float.parseFloat(quote_value5);
        f6 = Float.parseFloat(quote_value6);
        f7 = Float.parseFloat(quote_value7);
        f8 = Float.parseFloat(quote_value8);
        f9 = Float.parseFloat(quote_value9);
        f10 = Float.parseFloat(quote_value10);
        f11 = Float.parseFloat(quote_value11);
        f12 = Float.parseFloat(quote_value12);

        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, f1));
        dataVals.add(new Entry(1, f2));
        dataVals.add(new Entry(2, f3));
        dataVals.add(new Entry(3, f4));
        dataVals.add(new Entry(4, f5));
        dataVals.add(new Entry(5,  f6));
        dataVals.add(new Entry(6, f7));
        dataVals.add(new Entry(7, f8));
        dataVals.add(new Entry(8, f9));
        dataVals.add(new Entry(9, f10));
        dataVals.add(new Entry(10, f11));
        dataVals.add(new Entry(11, f12));
        return dataVals;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public PostViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

        }

        public void setFund_asset_name(String fund_asset_name)
        {
            TextView textView = mView.findViewById(R.id.txtFundAssetName);
            textView.setText("Nombre del Activo: "+fund_asset_name);
        }
        public void setFund_asset_type(String fund_asset_type)
        {
            TextView textView = mView.findViewById(R.id.txtFundAssetType);
            textView.setText("Tipo Activo: "+fund_asset_type);
        }
        public void setFund_asset_last_transaction(String fund_asset_last_transaction)
        {
            TextView textView = mView.findViewById(R.id.txtFundAssetLastTransaction);
            textView.setText("Última operación: "+fund_asset_last_transaction);
        }
        public void setFund_asset_current_position(String fund_asset_current_position)
        {
            TextView textView = mView.findViewById(R.id.txtFundAssetCurrentPosition);
            textView.setText("Posición actual: "+fund_asset_current_position);
        }
        public void setFund_asset_participation(String fund_asset_participation)
        {
            TextView textView = mView.findViewById(R.id.txtFundAssetParticipation);
            textView.setText("Participación en el fondo: "+fund_asset_participation+"%");
        }
        public void setFund_asset_amount(String fund_asset_amount)
        {
            TextView textView = mView.findViewById(R.id.txtFundAssetAmmount);
            textView.setText(fund_asset_amount+" ");
        }
        public void setFund_asset_profit(String fund_asset_profit)
        {
            TextView textView = mView.findViewById(R.id.txtFundAssetProfit);
            textView.setText(fund_asset_profit+"%");
        }
    }
}
