package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanReady;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;


public class LoanReadyConditionsFragment extends Fragment {

    TextView txtAmount,txtMonths,txtGracePeriod,txtQuote,txtInterestRate,txtDesgravamenInsurrance,txtCurrentAmount,txtPaymentDate,txtExpirationDate,txtDailyDefaulterRate;
    FirebaseAuth mAuth;
    String currentUid,postkey,loan_amount,loan_months,loan_grace,loan_rate,loan_insurance_rate,loan_expiration_day,loan_expiration_month,loan_expiration_year,loan_currency,
            total_quote_st,desgravem_amount_st,date,daily_defaulter_rate;
    DatabaseReference userRef,loanReadyRef,ratesRef;
    double loan_amount_db, amount,rate,monthly_rate,future_value,quote_amount, desgravamen_rate, desgravament_amount, total_quote;
    DecimalFormat decimalFormat,decimalFormat2;
    int months,current_month,current_day,current_year,payment_day,payment_month,payment_year,grace_month,last_day_of_the_month;
    Button btnShowPaymentDates,btnContinue;
    Calendar cal;
    ArrayList<PaymentDatesModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loan_ready_conditions, container, false);

        txtAmount = view.findViewById(R.id.txtAmount);
        txtMonths = view.findViewById(R.id.txtMonths);
        txtGracePeriod = view.findViewById(R.id.txtGracePeriod);
        txtQuote = view.findViewById(R.id.txtQuote);
        txtInterestRate = view.findViewById(R.id.txtInterestRate);
        txtDesgravamenInsurrance = view.findViewById(R.id.txtDesgravamenInsurrance);
        txtCurrentAmount = view.findViewById(R.id.txtCurrentAmount);
        txtPaymentDate = view.findViewById(R.id.txtPaymentDate);
        txtExpirationDate = view.findViewById(R.id.txtExpirationDate);
        btnShowPaymentDates= view.findViewById(R.id.btnShowPaymentDates);
        txtDailyDefaulterRate = view.findViewById(R.id.txtDailyDefaulterRate);
        btnContinue= view.findViewById(R.id.btnContinue);


        decimalFormat = new DecimalFormat("0.00");
        decimalFormat2 = new DecimalFormat("0,000.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat2.setRoundingMode(RoundingMode.HALF_UP);

        postkey = getActivity().getIntent().getExtras().getString("postkey");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        loanReadyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Loan Approved");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates").child("Loans").child("express_loan");

        list = new ArrayList<>();

        Date date = new Date();
        cal = Calendar.getInstance();

        current_day = cal.get(Calendar.DAY_OF_MONTH);
        current_month = cal.get(Calendar.MONTH);
        current_year = cal.get(Calendar.YEAR);


        loanReadyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                loan_amount = dataSnapshot.child("loan_amount").getValue().toString();
                loan_months = dataSnapshot.child("loan_months").getValue().toString();
                loan_grace = dataSnapshot.child("loan_grace").getValue().toString();
                loan_rate = dataSnapshot.child("loan_rate").getValue().toString();
                loan_insurance_rate = dataSnapshot.child("loan_insurance_rate").getValue().toString();
                loan_expiration_day = dataSnapshot.child("loan_expiration_day").getValue().toString();
                loan_expiration_month = dataSnapshot.child("loan_expiration_month").getValue().toString();
                loan_expiration_year = dataSnapshot.child("loan_expiration_year").getValue().toString();
                loan_currency = dataSnapshot.child("loan_currency").getValue().toString();

                loan_amount_db = Double.parseDouble(loan_amount);

                if (loan_amount_db > 999.99) {

                    String amount_st = decimalFormat2.format(loan_amount_db);

                    if (loan_currency.equals("PEN")) {
                        txtAmount.setText("S/ " + amount_st);
                        txtCurrentAmount.setText("Monto a recibir hoy: S/ " + amount_st);
                    }
                    if (loan_currency.equals("USD")) {
                        txtAmount.setText("$ " + amount_st);
                        txtCurrentAmount.setText("Monto a recibir hoy: $ " + amount_st);
                    }
                } else {
                    String amount_st = decimalFormat.format(loan_amount_db);

                    if (loan_currency.equals("PEN")) {
                        txtAmount.setText("S/ " + amount_st);
                        txtCurrentAmount.setText("Monto a recibir hoy: S/ " + amount_st);
                    }
                    if (loan_currency.equals("USD")) {
                        txtAmount.setText("$ " + amount_st);
                        txtCurrentAmount.setText("Monto a recibir hoy: $ " + amount_st);
                    }
                }

                txtMonths.setText("Cuotas: "+loan_months+" Cuotas Mensuales");
                txtGracePeriod.setText("Período de Gracia: "+loan_grace+" Meses");
                txtInterestRate.setText("TEA: "+loan_rate+"%");
                txtPaymentDate.setText("Paga tu Cuota el día "+current_day+" de cada mes.");
                txtExpirationDate.setText("Esta oferta de préstamo está disponible hasta el día: "+loan_expiration_day+"/"+loan_expiration_month+"/"+loan_expiration_year);

                ratesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        daily_defaulter_rate = dataSnapshot.child("daily_defaulter_rate").getValue().toString();
                        txtDailyDefaulterRate.setText("Tasa moratoria diaria: "+daily_defaulter_rate+"%");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                
                calculateQuote();
                calculatePaymentDay();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnShowPaymentDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showPaymentDatesDialog();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExpressLoanReadySumaryActivity.class);
                intent.putExtra("FRAGMENT_ID2", 2);
                startActivity(intent);
            }
        });

        return view;
    }

    private void showPaymentDatesDialog() throws ParseException {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Cronograma");
        dialog.setMessage("Conoce tus fechas de pago");

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.loan_payment_dates_dialog,null);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        current_day = cal.get(Calendar.DAY_OF_MONTH);
        current_month = cal.get(Calendar.MONTH);
        current_year = cal.get(Calendar.YEAR);

        grace_month = Integer.parseInt(loan_grace);

        PaymentDatesAdapter paymentDatesAdapter = new PaymentDatesAdapter(list);
        recyclerView.setAdapter(paymentDatesAdapter);

        current_month = current_month+grace_month+1;

        for (int i = 1; i < months; i++) {

            current_month = current_month+1;

            if (current_month > 12) {
                current_month = current_month-12;
                current_year = current_year+1;
            }
            Calendar calendar = new GregorianCalendar(current_year,current_month,current_day);
            int number_of_days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            if (current_day > number_of_days) {
                current_day = number_of_days;
            }


            if (loan_currency.equals("PEN")) {
                list.add(new PaymentDatesModel(current_day+"/"+current_month+"/"+current_year,"S/ "+total_quote_st));
            }
            if (loan_currency.equals("USD")) {
                list.add(new PaymentDatesModel(current_day+"/"+current_month+"/"+current_year,"$ "+total_quote_st));
            }


        }
        dialog.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setView(view);
        dialog.show();

    }

    private void calculatePaymentDay() {
        grace_month = Integer.parseInt(loan_grace);
    }

    private void calculateQuote() {

        months = Integer.parseInt(loan_months);
        amount = Double.parseDouble(loan_amount);
        rate = Double.parseDouble(loan_rate)/100;

        monthly_rate = Math.pow((1+rate),(1*1.0/12))-1;
        future_value = Math.pow(1+monthly_rate,months)*amount;

        quote_amount = future_value/months;

        desgravamen_rate = Double.parseDouble(loan_insurance_rate)/100;

        desgravament_amount = quote_amount*desgravamen_rate;

        total_quote = quote_amount+desgravament_amount;

        desgravem_amount_st = decimalFormat.format(desgravament_amount);

        if(total_quote > 999.99) {
            total_quote_st = decimalFormat2.format(total_quote);

            if ((loan_currency.equals("PEN"))) {
                txtQuote.setText("S/ "+total_quote_st);
                txtDesgravamenInsurrance.setText("Seguro de desgravamen: S/ "+desgravem_amount_st);
            }
            if ((loan_currency.equals("USD"))) {
                txtQuote.setText("$ "+total_quote_st);
                txtDesgravamenInsurrance.setText("Seguro de desgravamen: $ "+desgravem_amount_st);
            }
        }else {
            total_quote_st = decimalFormat.format(total_quote);

            if ((loan_currency.equals("PEN"))) {
                txtQuote.setText("S/ "+total_quote_st);
                txtDesgravamenInsurrance.setText("Seguro de desgravamen: S/ "+desgravem_amount_st);
            }
            if ((loan_currency.equals("USD"))) {
                txtQuote.setText("$ "+total_quote_st);
                txtDesgravamenInsurrance.setText("Seguro de desgravamen: $ "+desgravem_amount_st);
            }

        }


    }
}