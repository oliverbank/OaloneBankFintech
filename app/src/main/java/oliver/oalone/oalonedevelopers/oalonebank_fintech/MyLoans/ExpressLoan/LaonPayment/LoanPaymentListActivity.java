package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.LaonPayment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.MyLoansListFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.MyLoansModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class LoanPaymentListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String currentUid,postkey;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    long diff,days_to_expire,defaulter_days;
    DecimalFormat decimalFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_payment_list);

        recyclerView = findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        postkey = getIntent().getExtras().getString("postkey");

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        showLoanBills();


    }

    private void showLoanBills() {
        Query query = userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").orderByChild("timestamp");
        FirebaseRecyclerAdapter<LoanPaymentModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LoanPaymentModel, myPostViewHolder>
                (LoanPaymentModel.class,R.layout.loan_payment_bill_item,myPostViewHolder.class,query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, LoanPaymentModel model, int position) {
                final String myPostKey = getRef(position).getKey();
                viewHolder.setBill_state(model.getBill_state());
                viewHolder.setDesgravament_insurrance(model.getDesgravament_insurrance());
                viewHolder.setEnd_day(model.getEnd_day());
                viewHolder.setEnd_month(model.getEnd_month());
                viewHolder.setEnd_year(model.getEnd_year());
                viewHolder.setLoan_currency(model.getLoan_currency());
                viewHolder.setQuote_amount(model.getQuote_amount());
                viewHolder.setQuote_capital(model.getQuote_capital());
                viewHolder.setQuote_total_amount(model.getQuote_total_amount());
                viewHolder.setDefaulter_rate(model.getDefaulter_rate());
                viewHolder.setFixed_quote(model.getFixed_quote());

                if (viewHolder.my_end_month.equals("1")) {
                    viewHolder.txtMonthAndYear.setText("ENERO "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("2")) {
                    viewHolder.txtMonthAndYear.setText("FEBRERO "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("3")) {
                    viewHolder.txtMonthAndYear.setText("MARZO "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("4")) {
                    viewHolder.txtMonthAndYear.setText("ABRIL "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("5")) {
                    viewHolder.txtMonthAndYear.setText("MAYO "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("6")) {
                    viewHolder.txtMonthAndYear.setText("JUNIO "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("7")) {
                    viewHolder.txtMonthAndYear.setText("JULIO "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("8")) {
                    viewHolder.txtMonthAndYear.setText("AGOSTO "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("9")) {
                    viewHolder.txtMonthAndYear.setText("SEPTIEMBRE "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("10")) {
                    viewHolder.txtMonthAndYear.setText("OCTUBRE "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("11")) {
                    viewHolder.txtMonthAndYear.setText("NOVIEMBRE "+viewHolder.my_end_year);
                }
                if (viewHolder.my_end_month.equals("12")) {
                    viewHolder.txtMonthAndYear.setText("DICIEMBRE "+viewHolder.my_end_year);
                }

                if (viewHolder.my_loan_currency.equals("PEN")) {
                    viewHolder.txtQuoteAmount.setText("S/ "+viewHolder.my_quote_total_amount);
                }
                if (viewHolder.my_loan_currency.equals("USD")) {
                    viewHolder.txtQuoteAmount.setText("$ "+viewHolder.my_quote_total_amount);
                }

                viewHolder.txtExpirationDate.setText("Fecha de Vencimiento: "+viewHolder.my_end_day+"/"+viewHolder.my_end_month+"/"+viewHolder.my_end_year);

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                String saveCurrentDate =currentDate.format(calForDate.getTime());
                String finishDate = viewHolder.my_end_day+" "+viewHolder.my_end_month+" "+viewHolder.my_end_year;
                try {
                    Date date1 = currentDate.parse(saveCurrentDate);
                    Date date2 = currentDate.parse(finishDate);
                    diff = date2.getTime() - date1.getTime();
                    days_to_expire = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    viewHolder.txtExpirationDaysCount.setText("Vence en: "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                double defaulter_rate_db = Double.parseDouble(viewHolder.my_defaulter_rate)/100;
                double capital_amount_db = Double.parseDouble(viewHolder.my_quote_capital);

                if (days_to_expire < 0 && capital_amount_db > 0.00) {
                    userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(myPostKey).child("bill_state").setValue("default");
                    defaulter_days = Math.abs(days_to_expire);
                    double quote_amount_db = Double.parseDouble(viewHolder.my_fixed_quote);
                    double new_value_quote_amount = (quote_amount_db*(Math.pow((1+defaulter_rate_db),defaulter_days)));
                    String new_value_quote_amount_st = decimalFormat.format(new_value_quote_amount);
                    userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(myPostKey).child("quote_amount").setValue(new_value_quote_amount_st);
                    double desgravament_insurrance_db = Double.parseDouble(viewHolder.my_desgravament_insurrance);
                    double total_amount_quote_db = new_value_quote_amount+desgravament_insurrance_db;
                    String total_amount_quote_st = decimalFormat.format(total_amount_quote_db);
                    userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Bills").child(myPostKey).child("quote_total_amount").setValue(total_amount_quote_st);
                    viewHolder.imgTransactionState.setImageResource(R.drawable.error_icon);
                    viewHolder.txtExpirationDaysCount.setText("Venció hace "+defaulter_days+" días");

                }

                if (viewHolder.my_bill_state.equals("no_payed")) {
                    viewHolder.imgTransactionState.setImageResource(R.drawable.transaction_in_progress);
                    viewHolder.btnPay.setVisibility(View.VISIBLE);
                    viewHolder.txtQuoteAmount.setVisibility(View.VISIBLE);
                }
                if (viewHolder.my_bill_state.equals("payed")) {
                    viewHolder.imgTransactionState.setImageResource(R.drawable.transaction_completed);
                    viewHolder.txtExpirationDaysCount.setText("PAGO EXITOSO");
                    viewHolder.btnPay.setEnabled(false);
                    viewHolder.btnPay.setVisibility(View.GONE);
                    viewHolder.txtQuoteAmount.setVisibility(View.GONE);
                }

                viewHolder.btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LoanPaymentListActivity.this,PayLoanBillActivity.class);
                        intent.putExtra("debt_id",postkey);
                        intent.putExtra("bill_id",myPostKey);
                        startActivity(intent);
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        String my_end_year,my_end_month,my_end_day,my_quote_total_amount,my_quote_amount,my_desgravament_insurrance,my_loan_currency,my_quote_capital,my_bill_state,my_defaulter_rate,my_fixed_quote;
        TextView txtMonthAndYear,txtQuoteAmount,txtExpirationDate,txtExpirationDaysCount;
        ImageView imgTransactionState;
        Button btnPay;

        public myPostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            txtMonthAndYear = mView.findViewById(R.id.txtMonthAndYear);
            txtQuoteAmount = mView.findViewById(R.id.txtQuoteAmount);
            imgTransactionState = mView.findViewById(R.id.imgTransactionState);
            txtExpirationDate = mView.findViewById(R.id.txtExpirationDate);
            btnPay = mView.findViewById(R.id.btnPay);
            txtExpirationDaysCount = mView.findViewById(R.id.txtExpirationDaysCount);

        }
        public void setEnd_year(String end_year) {
            my_end_year = end_year;
        }

        public void setEnd_month(String end_month) {
            my_end_month = end_month;
        }

        public void setEnd_day(String end_day) {
            my_end_day = end_day;
        }


        public void setQuote_total_amount(String quote_total_amount) {
            my_quote_total_amount = quote_total_amount;
        }

        public void setQuote_amount(String quote_amount) {
            my_quote_amount = quote_amount;
        }

        public void setDesgravament_insurrance(String desgravament_insurrance) {
            my_desgravament_insurrance = desgravament_insurrance;
        }
        public void setLoan_currency(String loan_currency) {
            my_loan_currency = loan_currency;
        }

        public void setQuote_capital(String quote_capital) {
            my_quote_capital = quote_capital;
        }

        public void setBill_state(String bill_state) {
            my_bill_state = bill_state;
        }

        public void setDefaulter_rate(String defaulter_rate) {
            my_defaulter_rate = defaulter_rate;
        }
        public void setFixed_quote(String fixed_quote) {
            my_fixed_quote = fixed_quote;
        }
    }
}