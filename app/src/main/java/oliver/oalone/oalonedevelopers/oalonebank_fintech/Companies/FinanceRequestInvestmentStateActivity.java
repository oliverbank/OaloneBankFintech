package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class FinanceRequestInvestmentStateActivity extends AppCompatActivity {

    String currentUserID,finance_request_id,profileimage,investment_amount,main_currency,participation,cost_of_debt,investor_bill_ammount,investor_bill_currency,
            basic_acount_pen,basic_acount_usd,company_id,current_account_pen,current_account_usd,bill_company_ammount,fee_ammount1,fee_ammount2,fee_ammount4,
            financing_months,financing_frecuency,finance_request_fee_pen,finance_request_fee_usd,finance_request_investment_pen,finance_request_investment_usd;
    private RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference postsRef,userRef, ratesRef,myCompnyRef, oliverBankRef;
    FirebaseRecyclerAdapter<BillsModel, FinanceRequestInvestmentStateActivity.myPostViewHolder> firebaseRecyclerAdapter;
    long diff, days_to_finance;
    int space_days;
    CircleImageView profileImage;
    TextView txtAmmount,txtPercent,txtInterestAmmount;
    DecimalFormat decimalFormat;
    double fee1,fee2,fee3, frecuency_months, fee_to_pay, fr_fee_pen, fr_fee_usd, fr_invest_pen, fr_invest_usd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_request_investment_state);

        finance_request_id = getIntent().getExtras().get("finance_request_id").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Finance Requests").child(finance_request_id);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myCompnyRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

        profileImage = findViewById(R.id.profileImage);
        txtAmmount= findViewById(R.id.txtAmmount);
        txtPercent = findViewById(R.id.txtPercent);
        txtInterestAmmount = findViewById(R.id.txtInterestAmmount);

        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        myPostList.setLayoutManager(linearLayoutManager);

        decimalFormat = new DecimalFormat("0.00");

        getDataDate();

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String payment_space_days = dataSnapshot.child("payment_space_days").getValue().toString();
                space_days = Integer.parseInt(payment_space_days);

                postsRef.child("Investors").child(currentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            profileimage = dataSnapshot.child("profileimage").getValue().toString();
                            Picasso.with(FinanceRequestInvestmentStateActivity.this).load(profileimage).fit().centerCrop().into(profileImage);
                            investment_amount = dataSnapshot.child("investment_amount").getValue().toString();
                            main_currency = dataSnapshot.child("main_currency").getValue().toString();
                            txtAmmount.setText(investment_amount+" "+main_currency);
                            participation = dataSnapshot.child("participation").getValue().toString();
                            txtPercent.setText(participation+"%");

                            postsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        cost_of_debt = dataSnapshot.child("cost_of_debt").getValue().toString();
                                        company_id = dataSnapshot.child("company_id").getValue().toString();
                                        fee_ammount1 = dataSnapshot.child("fee_ammount1").getValue().toString();
                                        fee_ammount2 = dataSnapshot.child("fee_ammount2").getValue().toString();
                                        fee_ammount4 = dataSnapshot.child("fee_ammount4").getValue().toString();
                                        financing_months = dataSnapshot.child("financing_months").getValue().toString();
                                        financing_frecuency = dataSnapshot.child("financing_frecuency").getValue().toString();

                                        double months_to_finance = Double.parseDouble(financing_months);

                                        if (financing_frecuency.equals("Mensual")) {
                                            frecuency_months = 1;
                                        }
                                        if (financing_frecuency.equals("Cada 2 meses")) {
                                            frecuency_months = 2;
                                        }
                                        if (financing_frecuency.equals("Cada 3 meses")) {
                                            frecuency_months = 3;
                                        }

                                        fee1 = (Double.parseDouble(fee_ammount1)/months_to_finance)*frecuency_months;
                                        fee2 = (Double.parseDouble(fee_ammount2)/months_to_finance)*frecuency_months;
                                        fee3 = Double.parseDouble(fee_ammount4)*frecuency_months;

                                        double interest = Double.parseDouble(cost_of_debt);
                                        double participation_percent = Double.parseDouble(participation)/100;
                                        double my_interest_gained = interest*participation_percent;

                                        fee_to_pay = (fee1+fee2+fee3)*participation_percent;
                                        //Toast.makeText(FinanceRequestInvestmentStateActivity.this, "Oliver Bank Fee: "+fee_to_pay+" "+main_currency, Toast.LENGTH_LONG).show();

                                        String my_gain = decimalFormat.format(my_interest_gained);
                                        txtInterestAmmount.setText(my_gain+" "+main_currency);

                                        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                basic_acount_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                                                basic_acount_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                                                myCompnyRef.child(company_id).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        current_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                                                        current_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                                                        oliverBankRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    finance_request_fee_pen = dataSnapshot.child("Finance Request Fee PEN").getValue().toString();
                                                                    finance_request_fee_usd = dataSnapshot.child("Finance Request Fee USD").getValue().toString();
                                                                    finance_request_investment_pen = dataSnapshot.child("Finance Request Investment PEN").getValue().toString();
                                                                    finance_request_investment_usd = dataSnapshot.child("Finance Request Investment USD").getValue().toString();

                                                                    fr_fee_pen = Double.parseDouble(finance_request_fee_pen);
                                                                    fr_fee_usd = Double.parseDouble(finance_request_fee_usd);
                                                                    fr_invest_pen = Double.parseDouble(finance_request_investment_pen);
                                                                    fr_invest_usd = Double.parseDouble(finance_request_investment_usd);
                                                                }
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        displayMyBills();
    }

    private void displayMyBills() {
        Query myPostQuery = postsRef.child("Investors").child(currentUserID).child("Bills").orderByChild("timestamp");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BillsModel, FinanceRequestInvestmentStateActivity.myPostViewHolder>
                (BillsModel.class, R.layout.investment_bill, FinanceRequestInvestmentStateActivity.myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final FinanceRequestInvestmentStateActivity.myPostViewHolder viewHolder, BillsModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setBuyer_company_image(getApplicationContext(),model.getBuyer_company_image());
                viewHolder.setBill_code(model. getBill_code());
                viewHolder.setBuyer_company_name(model. getBuyer_company_name());
                viewHolder.setBuyer_company_ruc(model. getBuyer_company_ruc());
                viewHolder.setBuyer_company_verification(model. getBuyer_company_verification());
                viewHolder.setBill_ammount(model. getBill_ammount());
                viewHolder.setBill_currency(model. getBill_currency());
                viewHolder.setBill_issue_date(model. getBill_issue_date());
                viewHolder.setBill_end_date(model. getBill_end_date());
                viewHolder.setBill_state(model. getBill_state());
                viewHolder.setBill_factoring(model.getBill_factoring());
                viewHolder.setPayed(model.getPayed());

                viewHolder.postsRef.child(finance_request_id).child("Investors").child(currentUserID).child("Bills").child(postKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String end_day = dataSnapshot.child("bill_end_day").getValue().toString();
                            String end_month = dataSnapshot.child("bill_end_month").getValue().toString();
                            String end_year = dataSnapshot.child("bill_end_year").getValue().toString();
                            bill_company_ammount = dataSnapshot.child("bill_company_ammount").getValue().toString();
                            investor_bill_currency = dataSnapshot.child("bill_currency").getValue().toString();
                            investor_bill_ammount = dataSnapshot.child("bill_ammount").getValue().toString();
                            String payed = dataSnapshot.child("payed").getValue().toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                            String saveCurrentDate =currentDate.format(calForDate.getTime());
                            String finishDate = end_day+" "+end_month+" "+end_year;

                            try {
                                Date date1 = currentDate.parse(saveCurrentDate);
                                Date date2 = currentDate.parse(finishDate);
                                diff = date2.getTime() - date1.getTime();
                                days_to_finance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                viewHolder.txtDaysToExpire.setText(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)-space_days+" días");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (days_to_finance <= 0) {
                                postsRef.child("Investors").child(currentUserID).child("Bills").child(postKey).child("bill_state").setValue("Vencido");
                                viewHolder.txtDaysToExpire.setText("Vencido");
                            }
                            if (days_to_finance <= space_days && days_to_finance > 0) {
                                postsRef.child("Investors").child(currentUserID).child("Bills").child(postKey).child("bill_state").setValue("Disponible para cobrar");
                                viewHolder.txtDaysToExpire.setText("Disponible para cobrar");
                            }

                            if (payed.equals("true")) {
                                viewHolder.txtDaysToExpire.setText("Cobrado exitosamente");
                                viewHolder.txtBillState.setText("Cobrado exitosamente");
                            }

                            viewHolder.postsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {


                                    viewHolder.btnPayment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Dont forget to add Oliver Bank fees

                                            double investor_quote_value = Double.parseDouble(investor_bill_ammount);
                                            double pen_acount_value = Double.parseDouble(basic_acount_pen);
                                            double usd_acount_value = Double.parseDouble(basic_acount_usd);

                                            double new_value_on_account;

                                            double company_pen_acount = Double.parseDouble(current_account_pen);
                                            double company_usd_acount = Double.parseDouble(current_account_usd);

                                            double company_ammount = Double.parseDouble(bill_company_ammount)+fee_to_pay;


                                            if (investor_bill_currency.equals("PEN"))
                                            {
                                                if (company_pen_acount < company_ammount) {
                                                    Toast.makeText(FinanceRequestInvestmentStateActivity.this, "LA EMPRESA NO TIENE FONDOS SUFICIENTE", Toast.LENGTH_SHORT).show();
                                                }
                                                if (company_pen_acount >= company_ammount) {
                                                    viewHolder.btnPayment.setEnabled(false);
                                                    new_value_on_account = pen_acount_value+investor_quote_value;
                                                    String pen_acount_now = decimalFormat.format(new_value_on_account);
                                                    userRef.child(currentUserID).child("basic_account_pen").setValue(pen_acount_now);
                                                    viewHolder.postsRef.child(finance_request_id).child("Investors").child(currentUserID).child("Bills").child(postKey).child("bill_state").setValue("Cobrado Exitosamente");

                                                    double ammount_to_company = Double.parseDouble(bill_company_ammount);
                                                    double new_value_on_account_company = company_pen_acount-ammount_to_company;
                                                    String new_value_on_account_company_st = decimalFormat.format(new_value_on_account_company);
                                                    myCompnyRef.child(company_id).child("current_account_pen").setValue(new_value_on_account_company_st);

                                                    viewHolder.postsRef.child(finance_request_id).child("Investors").child(currentUserID).child("Bills").child(postKey).child("payed").setValue("true");

                                                    //Oliver Bank State Update
                                                    double fr_pen_updated = fr_fee_pen+fee_to_pay;
                                                    String fr_fee_updated = decimalFormat.format(fr_pen_updated);
                                                    oliverBankRef.child("Finance Request Fee PEN").setValue(fr_fee_updated);

                                                }

                                            }
                                            if (investor_bill_currency.equals("USD"))
                                            {
                                                if (company_pen_acount < company_ammount) {
                                                    Toast.makeText(FinanceRequestInvestmentStateActivity.this, "LA EMPRESA NO TIENE FONDOS SUFICIENTES", Toast.LENGTH_SHORT).show();
                                                }
                                                if (company_usd_acount >= company_ammount) {
                                                    viewHolder.btnPayment.setEnabled(false);
                                                    new_value_on_account = usd_acount_value+investor_quote_value;
                                                    String usd_acount_now = decimalFormat.format(new_value_on_account);
                                                    userRef.child(currentUserID).child("basic_account_usd").setValue(usd_acount_now);
                                                    viewHolder.postsRef.child(finance_request_id).child("Investors").child(currentUserID).child("Bills").child(postKey).child("bill_state").setValue("Cobrado Exitosamente");

                                                    double ammount_to_company = Double.parseDouble(bill_company_ammount);
                                                    double new_value_on_account_company = company_usd_acount-ammount_to_company;
                                                    String new_value_on_account_company_st = decimalFormat.format(new_value_on_account_company);
                                                    myCompnyRef.child(company_id).child("current_account_usd").setValue(new_value_on_account_company_st);

                                                    viewHolder.postsRef.child(finance_request_id).child("Investors").child(currentUserID).child("Bills").child(postKey).child("payed").setValue("true");

                                                    //Oliver Bank State Update
                                                    double fr_pen_updated = fr_fee_usd+fee_to_pay;
                                                    String fr_fee_updated = decimalFormat.format(fr_pen_updated);
                                                    oliverBankRef.child("Finance Request Fee USD").setValue(fr_fee_updated);
                                                }
                                            }



                                        }
                                    });
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


            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        ImageView imageCompanyVerification;
        LinearLayout factoringLayout,mainLayout;
        SimpleDateFormat currentDate;
        String saveCurrentDate,currentUserId;
        DatabaseReference postsRef;
        TextView txtDaysToExpire,txtBillState;
        Button btnCancelBill,btnPayment;
        private FirebaseAuth mAuth;


        public myPostViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

            mAuth = FirebaseAuth.getInstance();
            currentUserId = mAuth.getCurrentUser().getUid();

            imageCompanyVerification = mView.findViewById(R.id.imageCompanyVerification);
            txtDaysToExpire = mView.findViewById(R.id.txtDaysToExpire);
            factoringLayout = mView.findViewById(R.id.factoringLayout);
            btnCancelBill = mView.findViewById(R.id.btnCancelBill);
            btnPayment = mView.findViewById(R.id.btnPayment);
            mainLayout = mView.findViewById(R.id.mainLayout);
            txtBillState = mView.findViewById(R.id.txtBillState);
            postsRef = FirebaseDatabase.getInstance().getReference().child("Finance Requests");

        }

        public void setBill_code(String bill_code) {
            TextView textView = mView.findViewById(R.id.txtBillNumber);
            textView.setText("Nº"+bill_code);
        }
        public void setBuyer_company_image(Context ctx, String buyer_company_image) {
            ImageView image = mView.findViewById(R.id.imageCompany);
            Picasso.with(ctx).load(buyer_company_image).fit().centerCrop().into(image);
        }
        public void setBuyer_company_name(String buyer_company_name) {
            TextView textView = mView.findViewById(R.id.txtName);
            textView.setText("Cliente: "+buyer_company_name);
        }
        public void setBuyer_company_ruc(String buyer_company_ruc) {
            TextView textView = mView.findViewById(R.id.txtRuc);
            textView.setText(buyer_company_ruc);
        }
        public void setBuyer_company_verification(String buyer_company_verification) {
            TextView textView = mView.findViewById(R.id.txtCompanyVerification);
            if (buyer_company_verification.equals("true"))
            {
                imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                textView.setText("Verificado Correctamente");
            }
            if (buyer_company_verification.equals("false"))
            {
                imageCompanyVerification.setImageResource(R.drawable.error_icon);
                textView.setText("Denegado");
            }
            else
            {
                imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
            }
        }
        public void setBill_ammount(String bill_ammount) {
            TextView textView = mView.findViewById(R.id.txtAmmount);
            textView.setText("Monto: "+bill_ammount);
        }
        public void setBill_currency(String bill_currency) {
            TextView textView = mView.findViewById(R.id.txtCurrency);
            textView.setText(bill_currency);
        }
        public void setBill_issue_date(String bill_issue_date) {
            TextView textView = mView.findViewById(R.id.txtIssueDate);
            textView.setText("Emisión: "+bill_issue_date);
        }
        public void setBill_end_date(String bill_end_date) {
            TextView textView = mView.findViewById(R.id.txtEndDate);
            textView.setText("Vencimiento: "+bill_end_date);

        }
        public void setBill_state(final String bill_state) {
            TextView textView = mView.findViewById(R.id.txtBillState);
            textView.setText(bill_state);

            if (bill_state.equals("Esperando Confirmación")) {
                factoringLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Disponible para cobrar")) {
                factoringLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (bill_state.equals("Vencido")) {
                factoringLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Cobrado Exitosamente")) {
                factoringLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Vencido")) {
                factoringLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Financiado Exitosamente")) {
                factoringLayout.getLayoutParams().height = 0;
            }
        }
        public void setBill_factoring(String bill_factoring) {
            if (bill_factoring.equals("true")) {
                factoringLayout.getLayoutParams().height = 0;
                TextView textView = mView.findViewById(R.id.txtBillState);
                textView.setText("Financiando con Factoring");
            }
            if (bill_factoring.equals("success")) {
                factoringLayout.getLayoutParams().height = 0;
                TextView textView = mView.findViewById(R.id.txtBillState);
                textView.setText("Financiado Exitosamente");
            }
            if (bill_factoring.equals("canceled")) {
                factoringLayout.getLayoutParams().height = 0;
                TextView textView = mView.findViewById(R.id.txtBillState);
                textView.setText("Pagado sin Factoring");
            }
        }
        public void setPayed(String payed) {
            if (payed.equals("true")) {
                factoringLayout.getLayoutParams().height = 0;
            }
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
