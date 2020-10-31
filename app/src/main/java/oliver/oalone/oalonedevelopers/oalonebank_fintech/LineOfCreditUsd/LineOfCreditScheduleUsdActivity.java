package oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCreditUsd;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class LineOfCreditScheduleUsdActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef,ratesRef;
    String currentUserID,defaulter_credit_pen_daily_rate;
    RecyclerView recyclerView;
    long diff,days_to_expire,defaulter_days;
    DecimalFormat decimalFormat;
    double defaulter_credit_pen_daily_rate_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_of_credit_schedule_usd);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        decimalFormat = new DecimalFormat("0.00");

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaulter_credit_pen_daily_rate = dataSnapshot.child("defaulter_credit_usd_daily_rate").getValue().toString();

                defaulter_credit_pen_daily_rate_db = Double.parseDouble(defaulter_credit_pen_daily_rate)/100;

                showPayments();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getDataDate();
    }

    private void showPayments() {
        Query query = userRef.child(currentUserID).child("Line Of Credit Bills USD").orderByChild("timestamp");
        FirebaseRecyclerAdapter<LineOfCreditModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LineOfCreditModel, myPostViewHolder>
                (LineOfCreditModel.class, R.layout.line_of_credit_quotes_simulation_item, myPostViewHolder.class,query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, LineOfCreditModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setStart_day(model.getStart_day());
                viewHolder.setStart_month(model.getStart_month());
                viewHolder.setStart_year(model.getStart_year());
                viewHolder.setEnd_day(model.getEnd_day());
                viewHolder.setEnd_month(model.getEnd_month());
                viewHolder.setEnd_year(model.getEnd_year());
                viewHolder.setDesgravamen_insurrance(model.getDesgravamen_insurrance());
                viewHolder.setDebt_state(model.getDebt_state());
                viewHolder.setBill_code(model.getBill_code());
                viewHolder.setQuote_amount(model.getQuote_amount());
                viewHolder.setCapital_amount(model.getCapital_amount());
                viewHolder.setMonthly_interest(model.getMonthly_interest());
                viewHolder.setDebt_currency(model.getDebt_currency());
                viewHolder.setVisible(model.getVisible());
                viewHolder.setFixed_quote(model.getFixed_quote());

                viewHolder.txtStartDate.setText(viewHolder.my_start_day+"/"+viewHolder.my_start_month+"/"+viewHolder.my_start_year);
                viewHolder.txtEndDate.setText(viewHolder.my_end_day+"/"+viewHolder.my_end_month+"/"+viewHolder.my_end_year);
                viewHolder.txtAmount.setText("$"+viewHolder.my_quote_amount);

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                String saveCurrentDate =currentDate.format(calForDate.getTime());
                String finishDate = viewHolder.my_end_day+" "+viewHolder.my_end_month+" "+viewHolder.my_end_year;
                try {
                    Date date1 = currentDate.parse(saveCurrentDate);
                    Date date2 = currentDate.parse(finishDate);
                    diff = date2.getTime() - date1.getTime();
                    days_to_expire = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Double capital_val = Double.parseDouble(viewHolder.my_capital_amount);

                if (days_to_expire <= 0 && capital_val > 0.00) {
                    userRef.child(currentUserID).child("Line Of Credit Bills USD").child(postKey).child("debt_state").setValue("2");
                    defaulter_days = Math.abs(days_to_expire);

                    //Setting new value for the quote amount due to the defaulter days
                    double quote_amount_db = Double.parseDouble(viewHolder.my_fixed_quote);
                    double new_value_quote_amount = (quote_amount_db*(Math.pow((1+defaulter_credit_pen_daily_rate_db),defaulter_days)));
                    String new_value_quote_amount_st = decimalFormat.format(new_value_quote_amount);
                    userRef.child(currentUserID).child("Line Of Credit Bills USD").child(postKey).child("quote_amount").setValue(new_value_quote_amount_st);
                    userRef.child(currentUserID).child("Line Of Credit Bills USD").child(postKey).child("total_quote_after_defaulter_days").setValue(new_value_quote_amount_st);
                    userRef.child(currentUserID).child("Line Of Credit Bills USD").child(postKey).child("defaulter").setValue("true");

                }

                if (viewHolder.my_debt_state.equals("0")) {

                }
                if (viewHolder.my_debt_state.equals("1")) {

                }
                if (viewHolder.my_debt_state.equals("2")) {

                }

                int current_year = Calendar.getInstance().get(Calendar.YEAR);
                int current_month = Calendar.getInstance().get(Calendar.MONTH)+1;

                String my_current_month = String.valueOf(current_month);
                String my_current_year = String.valueOf(current_year);
                String current_period_name = my_current_year+""+my_current_month;
                String bill_period_name = viewHolder.my_start_year+""+viewHolder.my_start_month;

                if (!current_period_name.equals(bill_period_name)) {

                }
                if (viewHolder.my_debt_state.equals("1") && !current_period_name.equals(bill_period_name)) {
                    viewHolder.mainLayout.getLayoutParams().height = 0;
                }


            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        String my_start_day,my_start_month,my_start_year,my_end_day,my_end_month,my_end_year,my_desgravamen_insurrance,my_debt_state,my_bill_code,my_quote_amount,my_capital_amount,
                my_monthly_interest,my_debt_currency,my_visible,my_fixed_quote;
        TextView txtAmount,txtStartDate,txtEndDate;
        LinearLayout mainLayout;

        public myPostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtAmount = mView.findViewById(R.id.txtAmount);
            txtStartDate = mView.findViewById(R.id.txtStartDate);
            txtEndDate = mView.findViewById(R.id.txtEndDate);
            mainLayout = mView.findViewById(R.id.mainLayout);
        }

        public void setStart_day(String start_day) {
            my_start_day = start_day;
        }
        public void setStart_month(String start_month) {
            my_start_month = start_month;
        }
        public void setStart_year(String start_year) {
            my_start_year = start_year;
        }
        public void setEnd_day(String end_day) {
            my_end_day = end_day;
        }
        public void setEnd_month(String end_month) {
            my_end_month = end_month;
        }
        public void setEnd_year(String end_year) {
            my_end_year = end_year;
        }
        public void setDesgravamen_insurrance(String desgravamen_insurrance) {
            my_desgravamen_insurrance = desgravamen_insurrance;
        }
        public void setDebt_state(String debt_state) {
            my_debt_state = debt_state;
        }
        public void setBill_code(String bill_code) {
            my_bill_code = bill_code;
        }
        public void setQuote_amount(String quote_amount) {
            my_quote_amount = quote_amount;
        }
        public void setCapital_amount(String capital_amount) {
            my_capital_amount = capital_amount;
        }
        public void setMonthly_interest(String monthly_interest) {
            my_monthly_interest = monthly_interest;
        }
        public void setDebt_currency(String debt_currency) {
            my_debt_currency = debt_currency;
        }
        public void setVisible(String visible) {
            my_visible = visible;
        }
        public void setFixed_quote(String fixed_quote) {
            my_fixed_quote = fixed_quote;
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
