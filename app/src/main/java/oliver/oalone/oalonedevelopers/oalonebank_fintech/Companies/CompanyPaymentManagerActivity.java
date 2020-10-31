package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompanyPaymentManagerActivity extends AppCompatActivity {

    Button btnCreateBill;
    String my_company_key,currentUserID;
    private RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference postsRef,userRef;
    EditText edtUsername;
    FirebaseRecyclerAdapter<BillsModel, CompanyPaymentManagerActivity.myPostViewHolder> firebaseRecyclerAdapter;
    long diff, days_to_finance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_payment_manager);

        btnCreateBill= findViewById(R.id.btnCreateBill);
        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        edtUsername = findViewById(R.id.edtUsername);


        getDataDate();

        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);

        btnCreateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyPaymentManagerActivity.this, SelectCompanyForBiillingActivity.class);
                intent.putExtra("my_company_key", my_company_key);
                startActivity(intent);

            }
        });

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                displayBillByNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        displayMyBills();

    }

    private void displayBillByNumber() {
        Query myPostQuery = postsRef.orderByChild("bill_code").startAt(edtUsername.getText().toString()).endAt(edtUsername.getText().toString()+"\uf8ff");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BillsModel, myPostViewHolder>
                (BillsModel.class, R.layout.bills_to_charge, CompanyPaymentManagerActivity.myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final myPostViewHolder viewHolder, BillsModel model, int position) {
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

                viewHolder.postsRef.child(postKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String end_day = dataSnapshot.child("bill_end_day").getValue().toString();
                            String end_month = dataSnapshot.child("bill_end_month").getValue().toString();
                            String end_year = dataSnapshot.child("bill_end_year").getValue().toString();
                            String my_company_user = dataSnapshot.child("my_company_id").getValue().toString();

                            if (!my_company_user.equals(my_company_key)) {
                                viewHolder.mainLayout.getLayoutParams().height = 0;
                            }

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                            String saveCurrentDate =currentDate.format(calForDate.getTime());
                            String finishDate = end_day+" "+end_month+" "+end_year;

                            try {
                                Date date1 = currentDate.parse(saveCurrentDate);
                                Date date2 = currentDate.parse(finishDate);
                                diff = date2.getTime() - date1.getTime();
                                days_to_finance =TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                viewHolder.txtDaysToExpire.setText(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (days_to_finance <= 0) {
                                postsRef.child(postKey).child("bill_state").setValue("Vencido");
                                viewHolder.txtDaysToExpire.setText("VENCIDO");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.btnFactoring.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CompanyPaymentManagerActivity.this, FactoringDiscountRateActivity.class);
                        intent.putExtra("bill_id", postKey);
                        startActivity(intent);
                    }
                });

            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    private void displayMyBills() {
        Query myPostQuery = postsRef.orderByChild("my_company_id").startAt(my_company_key).endAt(my_company_key+"\uf8ff");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BillsModel, myPostViewHolder>
                (BillsModel.class, R.layout.bills_to_charge, CompanyPaymentManagerActivity.myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final myPostViewHolder viewHolder, BillsModel model, int position) {
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

                viewHolder.postsRef.child(postKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String end_day = dataSnapshot.child("bill_end_day").getValue().toString();
                            String end_month = dataSnapshot.child("bill_end_month").getValue().toString();
                            String end_year = dataSnapshot.child("bill_end_year").getValue().toString();
                            String my_company_user = dataSnapshot.child("my_company_id").getValue().toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                            String saveCurrentDate =currentDate.format(calForDate.getTime());
                            String finishDate = end_day+" "+end_month+" "+end_year;

                            try {
                                Date date1 = currentDate.parse(saveCurrentDate);
                                Date date2 = currentDate.parse(finishDate);
                                diff = date2.getTime() - date1.getTime();
                                days_to_finance =TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                viewHolder.txtDaysToExpire.setText(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (days_to_finance <= 0) {
                                postsRef.child(postKey).child("bill_state").setValue("Vencido");
                                viewHolder.txtDaysToExpire.setText("VENCIDO");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.btnCancelBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postsRef.child(postKey).removeValue();
                    }
                });

                viewHolder.btnFactoring.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CompanyPaymentManagerActivity.this, FactoringDiscountRateActivity.class);
                        intent.putExtra("bill_id", postKey);
                        startActivity(intent);
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
        LinearLayout cancelLayout,factoringLayout,mainLayout;
        SimpleDateFormat currentDate;
        String saveCurrentDate;
        DatabaseReference postsRef;
        TextView txtDaysToExpire;
        Button btnCancelBill,btnFactoring;

        public myPostViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

            imageCompanyVerification = mView.findViewById(R.id.imageCompanyVerification);
            txtDaysToExpire = mView.findViewById(R.id.txtDaysToExpire);
            cancelLayout = mView.findViewById(R.id.cancelLayout);
            factoringLayout = mView.findViewById(R.id.factoringLayout);
            btnCancelBill = mView.findViewById(R.id.btnCancelBill);
            btnFactoring = mView.findViewById(R.id.btnFactoring);
            mainLayout = mView.findViewById(R.id.mainLayout);
            postsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");

        }

        public void setBill_code(String bill_code) {
            TextView textView = mView.findViewById(R.id.txtBillNumber);
            textView.setText("Nº"+bill_code);
        }
        public void setBuyer_company_image(Context ctx,  String buyer_company_image) {
            ImageView image = mView.findViewById(R.id.imageCompany);
            Picasso.with(ctx).load(buyer_company_image).fit().into(image);
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
            if (bill_state.equals("Confirmado y Por Pagar")) {
                cancelLayout.getLayoutParams().height = 0;
                factoringLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (bill_state.equals("Pagado")) {
                cancelLayout.getLayoutParams().height = 0;
                factoringLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Rechazado")) {
                cancelLayout.getLayoutParams().height = 0;
                factoringLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Vencido")) {
                cancelLayout.getLayoutParams().height = 0;
                factoringLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Financiado Exitosamente")) {
                cancelLayout.getLayoutParams().height = 0;
                factoringLayout.getLayoutParams().height = 0;
            }
        }
        public void setBill_factoring(String bill_factoring) {
            if (bill_factoring.equals("true")) {
                factoringLayout.getLayoutParams().height = 0;
                cancelLayout.getLayoutParams().height = 0;
                TextView textView = mView.findViewById(R.id.txtBillState);
                textView.setText("Financiando con Factoring");
            }
            if (bill_factoring.equals("success")) {
                factoringLayout.getLayoutParams().height = 0;
                cancelLayout.getLayoutParams().height = 0;
                TextView textView = mView.findViewById(R.id.txtBillState);
                textView.setText("Financiado Exitosamente");
            }
            if (bill_factoring.equals("canceled")) {
                factoringLayout.getLayoutParams().height = 0;
                cancelLayout.getLayoutParams().height = 0;
                TextView textView = mView.findViewById(R.id.txtBillState);
                textView.setText("Pagado sin Factoring");
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
