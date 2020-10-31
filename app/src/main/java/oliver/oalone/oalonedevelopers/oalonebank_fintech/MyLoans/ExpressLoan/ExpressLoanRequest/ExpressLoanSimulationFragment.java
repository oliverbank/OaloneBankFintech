package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditUseActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.DependentWorker.DependentWorkerExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.IndependentWorker.IndependentWorkerExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.RentierRequest.RentierExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class ExpressLoanSimulationFragment extends Fragment {

    EditText edtAmount;
    RadioButton rdSoles,rdDollars;
    Button btnMonths,btnGrace,btnContinue;
    TextView txtQuoteAmount;
    DatabaseReference ratesRef,expressLoanRef,userRef;
    String base_ref_rate,grace_month_ref_init,payment_month_ref_init,quote_st,currentUid,user_condition;
    int months;
    double amount,rate,monthly_rate,future_value,quote_amount;
    DecimalFormat decimalFormat,decimalFormat2;
    ArrayList<String> quotes =new ArrayList<>();
    SpinnerDialog spinnerQuotes;
    ArrayList<String> gracesPeriod =new ArrayList<>();
    SpinnerDialog spinnerGrace;
    FirebaseAuth mAuth;
    RelativeLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_express_loan_simulation, container, false);

        edtAmount = view.findViewById(R.id.edtAmount);
        rdSoles = view.findViewById(R.id.rdSoles);
        rdDollars = view.findViewById(R.id.rdDollars);
        btnMonths = view.findViewById(R.id.btnMonths);
        btnGrace = view.findViewById(R.id.btnGrace);
        txtQuoteAmount = view.findViewById(R.id.txtQuoteAmount);
        btnContinue = view.findViewById(R.id.btnContinue);
        rootLayout = view.findViewById(R.id.rootLayout);

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat2 = new DecimalFormat("0,000.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat2.setRoundingMode(RoundingMode.HALF_UP);

        user_condition = getActivity().getIntent().getExtras().getString("user_condition");

        quotes.add("1");quotes.add("2");quotes.add("3");quotes.add("4");quotes.add("5");quotes.add("6");quotes.add("7");quotes.add("8");quotes.add("9");quotes.add("10");
        quotes.add("11");quotes.add("12");quotes.add("13");quotes.add("14");quotes.add("15");quotes.add("16");quotes.add("17");quotes.add("18");quotes.add("19");quotes.add("20");
        quotes.add("21");quotes.add("22");quotes.add("23");quotes.add("24");quotes.add("25");quotes.add("26");quotes.add("27");quotes.add("28");quotes.add("29");quotes.add("30");
        quotes.add("31");quotes.add("32");quotes.add("33");quotes.add("34");quotes.add("35");quotes.add("36");quotes.add("37");quotes.add("38");quotes.add("39");quotes.add("40");
        quotes.add("41");quotes.add("42");quotes.add("43");quotes.add("44");quotes.add("45");quotes.add("46");quotes.add("47");quotes.add("48");

        gracesPeriod.add("0");gracesPeriod.add("1");gracesPeriod.add("2");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        expressLoanRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("express_loan");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        ratesRef.child("Loans").child("express_loan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                base_ref_rate = dataSnapshot.child("base_ref_rate").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child("Loans Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("express_loan").child("Loan Requests").hasChild("amount")) {
                    String db_amount = dataSnapshot.child("express_loan").child("Loan Requests").child("amount").getValue().toString();

                    edtAmount.setText(db_amount);

                }
                if (dataSnapshot.child("express_loan").child("Loan Requests").hasChild("months")) {
                    String db_months = dataSnapshot.child("express_loan").child("Loan Requests").child("months").getValue().toString();

                    btnMonths.setText(db_months);

                }
                if (dataSnapshot.child("express_loan").child("Loan Requests").hasChild("grace")) {
                    String db_grace = dataSnapshot.child("express_loan").child("Loan Requests").child("grace").getValue().toString();

                    btnGrace.setText(db_grace);

                }
                if (dataSnapshot.child("express_loan").child("Loan Requests").hasChild("currency")) {
                    String db_currency = dataSnapshot.child("express_loan").child("Loan Requests").child("currency").getValue().toString();

                    if (db_currency.equals("PEN")) {
                        rdSoles.setChecked(true);
                    }
                    if (db_currency.equals("USD")) {
                        rdDollars.setChecked(true);
                    }

                    simulateQuote();

                } else if (!dataSnapshot.child("express_loan").hasChild("Loan Requests")){
                    ratesRef.child("Loans").child("express_loan").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            base_ref_rate = dataSnapshot.child("base_ref_rate").getValue().toString();
                            grace_month_ref_init = dataSnapshot.child("grace_month_ref_init").getValue().toString();
                            payment_month_ref_init = dataSnapshot.child("payment_month_ref_init").getValue().toString();

                            btnMonths.setText(payment_month_ref_init);
                            btnGrace.setText(grace_month_ref_init);
                            rdSoles.setChecked(true);

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



        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(edtAmount.getText().toString())) {
                    txtQuoteAmount.setText("0.00");
                } else if (!TextUtils.isEmpty(edtAmount.getText().toString())&&!TextUtils.isEmpty(btnMonths.getText().toString())&&!TextUtils.isEmpty(btnGrace.getText().toString())){
                    simulateQuote();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerQuotes.showSpinerDialog();
            }
        });

        spinnerQuotes = new SpinnerDialog(getActivity(),quotes,"Selecciona el número de cuotas");
        spinnerQuotes.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                btnMonths.setText(item);
                simulateQuote();

            }
        });

        btnGrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerGrace.showSpinerDialog();
            }
        });

        spinnerGrace = new SpinnerDialog(getActivity(),gracesPeriod,"Selecciona el período de gracia");
        spinnerGrace.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                btnGrace.setText(item);
                simulateQuote();

            }
        });

        rdSoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simulateQuote();
            }
        });
        rdDollars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simulateQuote();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtAmount.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a solicitar", Snackbar.LENGTH_LONG).show();
                } else {
                    if (user_condition.equals("student")) {
                        Intent intent = new Intent(getActivity(), ExpressLoanRequestActivity.class);
                        intent.putExtra("FRAGMENT_ID", 1);
                        intent.putExtra("user_condition","student");
                        startActivity(intent);
                        getActivity().finish();
                    }
                    if (user_condition.equals("dependent_worker")) {
                        Intent intent = new Intent(getActivity(), DependentWorkerExpressLoanRequestActivity.class);
                        intent.putExtra("FRAGMENT_ID", 1);
                        intent.putExtra("user_condition","dependent_worker");
                        startActivity(intent);
                        getActivity().finish();
                    }
                    if (user_condition.equals("independent_worker")) {
                        Intent intent = new Intent(getActivity(), IndependentWorkerExpressLoanRequestActivity.class);
                        intent.putExtra("FRAGMENT_ID", 1);
                        intent.putExtra("user_condition","independent_worker");
                        startActivity(intent);
                        getActivity().finish();
                    }
                    if (user_condition.equals("rentier")) {
                        Intent intent = new Intent(getActivity(), RentierExpressLoanRequestActivity.class);
                        intent.putExtra("FRAGMENT_ID", 1);
                        intent.putExtra("user_condition","rentier");
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }
        });


        return view;
    }

    private void simulateQuote() {
        months = Integer.parseInt(btnMonths.getText().toString());
        amount = Double.parseDouble(edtAmount.getText().toString());
        rate = Double.parseDouble(base_ref_rate)/100;

        monthly_rate = Math.pow((1+rate),(1*1.0/12))-1;
        future_value = Math.pow(1+monthly_rate,months)*amount;

        quote_amount = future_value/months;

        if(quote_amount > 999.99) {
            quote_st = decimalFormat2.format(quote_amount);

            if (rdSoles.isChecked()) {
                txtQuoteAmount.setText("S/ "+quote_st);
            }
            if (rdDollars.isChecked()) {
                txtQuoteAmount.setText("$ "+quote_st);
            }
        }else {
            quote_st = decimalFormat.format(quote_amount);

            if (rdSoles.isChecked()) {
                txtQuoteAmount.setText("S/ "+quote_st);
            }
            if (rdDollars.isChecked()) {
                txtQuoteAmount.setText("$ "+quote_st);
            }

        }

        expressLoanRef.child("Loan Requests").child("amount").setValue(edtAmount.getText().toString());
        expressLoanRef.child("Loan Requests").child("months").setValue(btnMonths.getText().toString());
        expressLoanRef.child("Loan Requests").child("grace").setValue(btnGrace.getText().toString());

        if (rdSoles.isChecked()) {
            expressLoanRef.child("Loan Requests").child("currency").setValue("PEN");
        }
        if (rdDollars.isChecked()) {
            expressLoanRef.child("Loan Requests").child("currency").setValue("USD");
        }


    }
}