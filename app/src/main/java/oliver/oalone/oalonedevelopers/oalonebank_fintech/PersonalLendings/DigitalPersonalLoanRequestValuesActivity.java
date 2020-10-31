package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineRequestFormActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class DigitalPersonalLoanRequestValuesActivity extends AppCompatActivity {

    private DatabaseReference userRef,ratesRef;
    private FirebaseAuth mAuth;
    private String senderUserID, receiverUserID,saveCurrentDate,saveCurrentTime, my_currency,main_currency,credit_line_request_condition;
    RadioButton rdPenAccount,rdUsdAccount;
    EditText edtTransferAmmount,edtInterestRate;
    Button btnNext,btnMonths,btnPaymentFrencuency,btnGracePeriod,btnCalculateAmortization,btnOmitAndConitnue;
    RelativeLayout rootLayout;

    ArrayList<String> months =new ArrayList<>();
    ArrayList<String> months2 =new ArrayList<>();
    ArrayList<String> months3 =new ArrayList<>();
    SpinnerDialog spinnerMonths;
    SpinnerDialog spinnerMonths2;
    SpinnerDialog spinnerMonths3;

    ArrayList<String> frecuency =new ArrayList<>();
    SpinnerDialog spinnerFrecuency;

    ArrayList<String> graceMonths =new ArrayList<>();
    SpinnerDialog spinnerGracePeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_personal_loan_request_values);

        mAuth = FirebaseAuth.getInstance();

        senderUserID = mAuth.getCurrentUser().getUid();
        //receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        edtTransferAmmount = findViewById(R.id.edtTransferAmmount);
        btnNext = findViewById(R.id.btnNext);
        rootLayout = findViewById(R.id.rootLayout);
        edtInterestRate = findViewById(R.id.edtInterestRate);
        btnMonths = findViewById(R.id.btnMonths);
        btnPaymentFrencuency = findViewById(R.id.btnPaymentFrencuency);
        btnGracePeriod = findViewById(R.id.btnGracePeriod);
        btnCalculateAmortization = findViewById(R.id.btnCalculateAmortization);
        btnOmitAndConitnue = findViewById(R.id.btnOmitAndConitnue);


        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_currency = "PEN";
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_currency = "USD";
            }
        });

        months.add("1");months.add("2");months.add("3"); months.add("4");months.add("5");months.add("6"); months.add("7");months.add("8");months.add("9");months.add("10");
        months.add("11");months.add("12");months.add("13"); months.add("14");months.add("15");months.add("16"); months.add("17");months.add("18");months.add("19");months.add("20");
        months.add("21");months.add("22");months.add("23"); months.add("24");months.add("25");months.add("26"); months.add("27");months.add("28");months.add("29");months.add("30");
        months.add("31");months.add("32");months.add("33"); months.add("34");months.add("35");months.add("36"); months.add("37");months.add("38");months.add("39");months.add("40");
        months.add("41");months.add("42");months.add("43"); months.add("44");months.add("45");months.add("46"); months.add("47");months.add("48");

        months2.add("2"); months2.add("4");months2.add("6");months2.add("8");months2.add("10");
        months2.add("12");months2.add("14");months2.add("16");months2.add("18");months2.add("20");
        months2.add("22");months2.add("24");months2.add("26");months2.add("28");months2.add("30");
        months2.add("32");months2.add("34");months2.add("36");months2.add("38");months2.add("40");
        months2.add("42");months2.add("44");months2.add("46");months2.add("48");

        months3.add("3");months3.add("6");months3.add("9");months3.add("12");months3.add("15");months3.add("18");
        months3.add("21"); months3.add("24"); months3.add("27");months3.add("30");
        months3.add("33");months3.add("36");months3.add("39");months3.add("42");months3.add("45");months3.add("48");

        btnMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Primero debes seleccionar la frecuencia de pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (btnPaymentFrencuency.getText().equals("Mensual")) {
                    spinnerMonths.showSpinerDialog();
                }
                if (btnPaymentFrencuency.getText().equals("Cada 2 meses")) {
                    spinnerMonths2.showSpinerDialog();
                }
                if (btnPaymentFrencuency.getText().equals("Cada 3 meses")) {
                    spinnerMonths3.showSpinerDialog();
                }
            }
        });

        spinnerMonths = new SpinnerDialog(DigitalPersonalLoanRequestValuesActivity.this,months,"Selecciona el número de meses para el préstamo");
        spinnerMonths.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnMonths.setText(item);
                Toast.makeText(DigitalPersonalLoanRequestValuesActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerMonths2 = new SpinnerDialog(DigitalPersonalLoanRequestValuesActivity.this,months2,"Selecciona el número de meses para el préstamo");
        spinnerMonths2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnMonths.setText(item);
                Toast.makeText(DigitalPersonalLoanRequestValuesActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerMonths3 = new SpinnerDialog(DigitalPersonalLoanRequestValuesActivity.this,months3,"Selecciona el número de meses para el préstamo");
        spinnerMonths3.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnMonths.setText(item);
                Toast.makeText(DigitalPersonalLoanRequestValuesActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        graceMonths.add("0");graceMonths.add("1");graceMonths.add("2");graceMonths.add("3");graceMonths.add("4");graceMonths.add("5");
        graceMonths.add("6");graceMonths.add("7");graceMonths.add("8");graceMonths.add("9");graceMonths.add("10");graceMonths.add("11");graceMonths.add("12");

        btnGracePeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerGracePeriod.showSpinerDialog();
            }
        });
        spinnerGracePeriod = new SpinnerDialog(DigitalPersonalLoanRequestValuesActivity.this,graceMonths,"Selecciona el número de meses para el período de gracia");
        spinnerGracePeriod.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnGracePeriod.setText(item);
                Toast.makeText(DigitalPersonalLoanRequestValuesActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        frecuency.add("Mensual");frecuency.add("Cada 2 meses");frecuency.add("Cada 3 meses");
        btnPaymentFrencuency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerFrecuency.showSpinerDialog();
            }
        });

        spinnerFrecuency = new SpinnerDialog(DigitalPersonalLoanRequestValuesActivity.this,frecuency,"Selecciona la frecuencia de pago");
        spinnerFrecuency.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnPaymentFrencuency.setText(item);
                Toast.makeText(DigitalPersonalLoanRequestValuesActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnCalculateAmortization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdUsdAccount.isChecked() && !rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una moneda de tu cuenta para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtTransferAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a prestar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar la tasa de interés anual para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnMonths.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar los meses que durará el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que se realizarán los pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que se realizarán los pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }

                else {
                    showMyGainDialog();
                }

            }
        });

        btnOmitAndConitnue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdUsdAccount.isChecked() && !rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la moneda del préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtTransferAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a prestar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtTransferAmmount.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a prestar", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    showConditionDialog2();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdUsdAccount.isChecked() && !rdPenAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la moneda del préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtTransferAmmount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a prestar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar la tasa de interés anual para el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnMonths.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes especificar los meses que durará el préstamo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que se realizarán los pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnPaymentFrencuency.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes especificar la frecuencia en el que se realizarán los pagos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else {
                    showConditionDialog();
                }

            }
        });
    }

    private void showConditionDialog2() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.condition_for_credit_request,null);

        dialog.setView(add_bank_account);

        ImageButton btnStudent,btnDependentWorker,btnIndependentWorker,btnLessor,btnStockHolder;
        btnStudent = add_bank_account.findViewById(R.id.btnStudent);
        btnDependentWorker = add_bank_account.findViewById(R.id.btnDependentWorker);
        btnIndependentWorker = add_bank_account.findViewById(R.id.btnIndependentWorker);
        btnLessor = add_bank_account.findViewById(R.id.btnLessor);
        btnStockHolder = add_bank_account.findViewById(R.id.btnStockHolder);

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "student";
                omitAndGoToFormActivity();
            }
        });
        btnDependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "dependent_worker";
                omitAndGoToFormActivity();
            }
        });
        btnIndependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "independent_worker";
                omitAndGoToFormActivity();
            }
        });
        btnLessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "lessor";
                omitAndGoToFormActivity();
            }
        });
        btnStockHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "stock_holder";
                omitAndGoToFormActivity();
            }
        });

        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void omitAndGoToFormActivity() {
        Intent intent = new Intent(DigitalPersonalLoanRequestValuesActivity.this, DigitalPersonalLoanRequestFormActivity.class);
        intent.putExtra("credit_line_request_currency", my_currency);
        intent.putExtra("credit_line_request_condition", credit_line_request_condition);
        intent.putExtra("main_currency",my_currency);
        intent.putExtra("ammount",edtTransferAmmount.getText().toString());
        intent.putExtra("interest_rate","0.00");
        intent.putExtra("financing_months","0.00");
        intent.putExtra("financing_frecuency","0.00");
        intent.putExtra("grace_period","0.00");
        intent.putExtra("monthly_interest_rate","0.00");
        intent.putExtra("quote_ammount","0.00");
        intent.putExtra("total_debt","0.00");
        intent.putExtra("cost_of_debt","0.00");
        startActivity(intent);
        finish();
    }


    private void showConditionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.condition_for_credit_request,null);

        dialog.setView(add_bank_account);

        ImageButton btnStudent,btnDependentWorker,btnIndependentWorker,btnLessor,btnStockHolder;
        btnStudent = add_bank_account.findViewById(R.id.btnStudent);
        btnDependentWorker = add_bank_account.findViewById(R.id.btnDependentWorker);
        btnIndependentWorker = add_bank_account.findViewById(R.id.btnIndependentWorker);
        btnLessor = add_bank_account.findViewById(R.id.btnLessor);
        btnStockHolder = add_bank_account.findViewById(R.id.btnStockHolder);

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "student";
                goToRequestFormActivity();
            }
        });
        btnDependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "dependent_worker";
                goToRequestFormActivity();
            }
        });
        btnIndependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "independent_worker";
                goToRequestFormActivity();
            }
        });
        btnLessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "lessor";
                goToRequestFormActivity();
            }
        });
        btnStockHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "stock_holder";
                goToRequestFormActivity();
            }
        });

        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void goToRequestFormActivity() {
        double ammount = Double.parseDouble(edtTransferAmmount.getText().toString());
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

        if (rdPenAccount.isChecked())
        {
            main_currency = "PEN";
        }
        if (rdUsdAccount.isChecked())
        {
            main_currency = "USD";
        }

        Intent intent = new Intent(DigitalPersonalLoanRequestValuesActivity.this, DigitalPersonalLoanRequestFormActivity.class);
        intent.putExtra("credit_line_request_currency", my_currency);
        intent.putExtra("credit_line_request_condition", credit_line_request_condition);
        intent.putExtra("main_currency",my_currency);
        intent.putExtra("ammount",edtTransferAmmount.getText().toString());
        intent.putExtra("interest_rate",edtInterestRate.getText().toString());
        intent.putExtra("financing_months",btnMonths.getText().toString());
        intent.putExtra("financing_frecuency",btnPaymentFrencuency.getText().toString());
        intent.putExtra("grace_period",btnGracePeriod.getText().toString());
        intent.putExtra("monthly_interest_rate",monthly_rate_df);
        intent.putExtra("quote_ammount",quote_df);
        intent.putExtra("total_debt",total_debt_df);
        intent.putExtra("cost_of_debt",cost_of_debt_df);
        startActivity(intent);
        finish();
    }

    private void showMyGainDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View amortization = inflater.inflate(R.layout.my_lending_gain_dialog,null);

        TextView txtQuoteAmmount,txtPaymentFrecuency,txtTotalDebt,txtCostOfDebt,txtQuotesNumber;
        txtQuoteAmmount = amortization.findViewById(R.id.txtQuoteAmmount);
        txtQuotesNumber = findViewById(R.id.txtQuotesNumber);
        txtPaymentFrecuency = amortization.findViewById(R.id.txtPaymentFrecuency);
        txtTotalDebt = amortization.findViewById(R.id.txtTotalDebt);
        txtCostOfDebt = amortization.findViewById(R.id.txtCostOfDebt);
        txtQuotesNumber= amortization.findViewById(R.id.txtQuotesNumber);

        if (rdPenAccount.isChecked())
        {
            main_currency = "PEN";
        }
        if (rdUsdAccount.isChecked())
        {
            main_currency = "USD";
        }
        double ammount = Double.parseDouble(edtTransferAmmount.getText().toString());
        double rate = Double.parseDouble(edtInterestRate.getText().toString());
        double months = Double.parseDouble(btnMonths.getText().toString());
        String frecuency = btnPaymentFrencuency.getText().toString();
        double pay_frecuency = Double.parseDouble(frecuency);

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

        double quote_number = months/pay_frecuency;

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String monthly_rate_df = decimalFormat.format(monthly_rate_express);
        String quote_df = decimalFormat.format(quote);
        String total_debt_df = decimalFormat.format(total_debt);
        String cost_of_debt_df = decimalFormat.format(cost_of_debt);

        txtQuoteAmmount.setText("Cuota Mensual: "+quote_df+" "+main_currency);
        txtPaymentFrecuency.setText("Frecuencia de pago: "+btnPaymentFrencuency.getText().toString());
        txtTotalDebt.setText("Deuda total: "+total_debt_df+" "+main_currency);
        txtCostOfDebt.setText("Interéses: "+cost_of_debt_df+" "+main_currency);
        txtQuotesNumber.setText("Número de cuotas: "+quote_number);

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
