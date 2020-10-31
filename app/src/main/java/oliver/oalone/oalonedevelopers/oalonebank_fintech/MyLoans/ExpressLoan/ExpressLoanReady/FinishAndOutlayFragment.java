package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanReady;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUseCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditUseActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class FinishAndOutlayFragment extends Fragment {

    Button btnGetMoney;
    FirebaseAuth mAuth;
    String currentUid,postkey,contract_checked,basic_account_pen,basic_account_usd,loan_currency,loan_amount, new_account_amount_st,loans_image,loan_months,loan_grace,loan_rate,loan_insurance_rate,loan_expiration_day,loan_expiration_month,loan_expiration_year,
            daily_defaulter_rate,total_quote_st,quote_capital_st,quote_amount_st,desgravament_amount_st;
    DatabaseReference userRef,myOperationsRef,imgRoursesRef,loanReadyRef,ratesRef;
    RelativeLayout rootLayout;
    double pen_account,usd_account, loan_amount_db, new_account_amount,amount,rate,monthly_rate,future_value,quote_amount,desgravamen_rate,desgravament_amount,total_quote,quote_capital;
    DecimalFormat decimalFormat;
    int current_day,current_month,current_year,grace_month,months;
    ImageView imgContractChecked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finish_and_outlay, container, false);

        btnGetMoney = view.findViewById(R.id.btnGetMoney);
        rootLayout = view.findViewById(R.id.rootLayout);
        imgContractChecked = view.findViewById(R.id.imgContractChecked);

        postkey = getActivity().getIntent().getExtras().getString("postkey");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        loanReadyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Loan Approved");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates").child("Loans").child("express_loan");

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        imgRoursesRef = FirebaseDatabase.getInstance().getReference().child("Image Resources");

        userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Loan Approved").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contract_checked = dataSnapshot.child("contract_checked").getValue().toString();
                loan_currency = dataSnapshot.child("loan_currency").getValue().toString();
                loan_amount = dataSnapshot.child("loan_amount").getValue().toString();
                loan_months = dataSnapshot.child("loan_months").getValue().toString();
                loan_grace = dataSnapshot.child("loan_grace").getValue().toString();
                loan_rate = dataSnapshot.child("loan_rate").getValue().toString();
                loan_insurance_rate = dataSnapshot.child("loan_insurance_rate").getValue().toString();
                loan_expiration_day = dataSnapshot.child("loan_expiration_day").getValue().toString();
                loan_expiration_month = dataSnapshot.child("loan_expiration_month").getValue().toString();
                loan_expiration_year = dataSnapshot.child("loan_expiration_year").getValue().toString();

                loan_amount_db = Double.parseDouble(loan_amount);

                if (contract_checked.equals("false")) {
                    imgContractChecked.setImageResource(R.drawable.transaction_in_progress);
                }
                if (contract_checked.equals("true")) {
                    imgContractChecked.setImageResource(R.drawable.transaction_completed);
                }

                userRef.child(currentUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                        basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                        pen_account = Double.parseDouble(basic_account_pen);
                        usd_account = Double.parseDouble(basic_account_usd);

                        imgRoursesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                loans_image = dataSnapshot.child("loans_image").getValue().toString();

                                ratesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        daily_defaulter_rate = dataSnapshot.child("daily_defaulter_rate").getValue().toString();

                                        //Calculate quote amount
                                        months = Integer.parseInt(loan_months);
                                        amount = Double.parseDouble(loan_amount);
                                        rate = Double.parseDouble(loan_rate)/100;

                                        monthly_rate = Math.pow((1+rate),(1*1.0/12))-1;
                                        future_value = Math.pow(1+monthly_rate,months)*amount;

                                        quote_amount = future_value/months;

                                        quote_amount_st = decimalFormat.format(quote_amount);

                                        desgravamen_rate = Double.parseDouble(loan_insurance_rate)/100;

                                        desgravament_amount = quote_amount*desgravamen_rate;

                                        desgravament_amount_st = decimalFormat.format(desgravament_amount);

                                        total_quote = quote_amount+desgravament_amount;

                                        total_quote_st = decimalFormat.format(total_quote);

                                        //Calculate Capital amount
                                        quote_capital = amount/months;
                                        quote_capital_st = decimalFormat.format(quote_capital);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contract_checked.equals("false")) {
                    Snackbar.make(rootLayout, "Debes aceptar los términos y condiciones del contrato", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (contract_checked.equals("true")) {
                    btnGetMoney.setEnabled(false);
                    sendMoneyToUser();
                }
            }
        });

        return view;
    }

    private void sendMoneyToUser() {
        //Update loan state
        userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("loan_state").setValue("ready");

        //Calculate new user account and send money
        if (loan_currency.equals("PEN")) {
            new_account_amount = pen_account+loan_amount_db;

            new_account_amount_st = decimalFormat.format(new_account_amount);

            userRef.child(currentUid).child("basic_account_pen").setValue(new_account_amount_st);
        }
        if (loan_currency.equals("USD")) {
            new_account_amount = usd_account+loan_amount_db;

            new_account_amount_st = decimalFormat.format(new_account_amount);

            userRef.child(currentUid).child("basic_account_usd").setValue(new_account_amount_st);
        }

        //Register Operation
        registerOperation();

    }

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime =currentTime.format(calForTime.getTime());

        final String operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Desembolso de Préstamo Express");
        myOperationMap.put("operation_type_code","EXLEN");
        myOperationMap.put("fund_name","");
        myOperationMap.put("fund_image","");
        myOperationMap.put("fund_currency","");
        myOperationMap.put("fund_quote_value","");
        myOperationMap.put("fund_quotes_bought","");
        myOperationMap.put("fund_my_investment","");
        myOperationMap.put("fund_suscription_fee","");
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        myOperationMap.put("fund_transaction_currency","");
        myOperationMap.put("uid",currentUid);
        myOperationMap.put("operation_image",loans_image);
        myOperationMap.put("deposit_ammount","");
        myOperationMap.put("deposit_currency","");
        myOperationMap.put("deposit_real_ammount","");
        myOperationMap.put("deposit_real_currency","");
        myOperationMap.put("deposit_state","");

        myOperationMap.put("transfer_user_origin","");
        myOperationMap.put("transfer_user_destination","");
        myOperationMap.put("sent_ammount","");
        myOperationMap.put("sent_currency","");
        myOperationMap.put("recieved_ammount","");
        myOperationMap.put("recieved_currency","");
        //Direct Investments
        myOperationMap.put("company_finance_name","");
        myOperationMap.put("finance_ammount","");
        myOperationMap.put("finance_currency","");
        //Credit Line
        myOperationMap.put("credit_request_ammount",loan_amount);
        myOperationMap.put("credit_request_ammount_currency",loan_currency);
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUid+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                generateBills();

                Intent intent = new Intent(getActivity(), SuccessfulLoanActivity.class);
                intent.putExtra("amount",loan_amount);
                intent.putExtra("currency",loan_currency);
                intent.putExtra("transaction_code",operationRandomName);
                startActivity(intent);
                getActivity().finish();

            }
        });
    }

    private void generateBills() {

        Calendar cal;
        cal = Calendar.getInstance();

        current_day = cal.get(Calendar.DAY_OF_MONTH);
        current_month = cal.get(Calendar.MONTH);
        current_year = cal.get(Calendar.YEAR);

        grace_month = Integer.parseInt(loan_grace);
        current_month = current_month+grace_month+1;
        months = Integer.parseInt(loan_months);

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

            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("end_year").setValue(""+current_year);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("end_month").setValue(""+current_month);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("end_day").setValue(""+current_day);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("quote_total_amount").setValue(total_quote_st);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("quote_amount").setValue(quote_amount_st);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("desgravament_insurrance").setValue(desgravament_amount_st);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("loan_currency").setValue(loan_currency);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("quote_capital").setValue(quote_capital_st);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("bill_state").setValue("no_payed");
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("defaulter_rate").setValue(daily_defaulter_rate);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("fixed_quote").setValue(quote_amount_st);
            userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(current_year+""+current_month).child("timestamp").setValue(ServerValue.TIMESTAMP);

        }
    }
}