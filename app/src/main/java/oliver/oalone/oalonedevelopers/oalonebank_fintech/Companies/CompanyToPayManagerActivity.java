package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompanyToPayManagerActivity extends AppCompatActivity {

    String my_company_key,currentUserID;
    private RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference postsRef,userRef;
    EditText edtUsername;
    FirebaseRecyclerAdapter<BillsModel, CompanyToPayManagerActivity.myPostViewHolder> firebaseRecyclerAdapter;
    long diff,days_to_finance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_to_pay_manager);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataDate();

        edtUsername = findViewById(R.id.edtUsername);

        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);

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
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BillsModel, CompanyToPayManagerActivity.myPostViewHolder>
                (BillsModel.class, R.layout.bills_to_pay, CompanyToPayManagerActivity.myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final CompanyToPayManagerActivity.myPostViewHolder viewHolder, BillsModel model, final int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setMy_company_image(getApplicationContext(),model.getMy_company_image());
                viewHolder.setBill_code(model. getBill_code());
                viewHolder.setMy_company_name(model. getMy_company_name());
                viewHolder.setMy_company_ruc(model. getMy_company_ruc());
                viewHolder.setMy_company_verification(model. getMy_company_verification());
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

                            String my_company_user = dataSnapshot.child("buyer_company_id").getValue().toString();

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

                viewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //postKey = getRef(position).getKey();

                        HashMap hashMap = new HashMap();
                        hashMap.put("bill_state","Confirmado");

                        postsRef.child(postKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CompanyToPayManagerActivity.this, "LA FACTURA HA SIDO CONFIRMADA", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                viewHolder.btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //postKey = getRef(position).getKey();

                        HashMap hashMap = new HashMap();
                        hashMap.put("bill_state","Rechazado");

                        postsRef.child(postKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CompanyToPayManagerActivity.this, "LA FACTURA HA SIDO RECHAZADA", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    private void displayMyBills() {
        Query myPostQuery = postsRef.orderByChild("buyer_company_id").startAt(my_company_key).endAt(my_company_key+"\uf8ff");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BillsModel, CompanyToPayManagerActivity.myPostViewHolder>
                (BillsModel.class, R.layout.bills_to_pay, CompanyToPayManagerActivity.myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final CompanyToPayManagerActivity.myPostViewHolder viewHolder, final BillsModel model, final int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setMy_company_image(getApplicationContext(),model.getMy_company_image());
                viewHolder.setBill_code(model. getBill_code());
                viewHolder.setMy_company_name(model. getMy_company_name());
                viewHolder.setMy_company_ruc(model. getMy_company_ruc());
                viewHolder.setMy_company_verification(model. getMy_company_verification());
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

                viewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //postKey = getRef(position).getKey();

                        HashMap hashMap = new HashMap();
                        hashMap.put("bill_state","Confirmado y Por Pagar");

                        postsRef.child(postKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CompanyToPayManagerActivity.this, "LA FACTURA HA SIDO CONFIRMADA", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                viewHolder.btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //postKey = getRef(position).getKey();

                        HashMap hashMap = new HashMap();
                        hashMap.put("bill_state","Rechazado");

                        postsRef.child(postKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CompanyToPayManagerActivity.this, "LA FACTURA HA SIDO RECHAZADA", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                viewHolder.btnPayNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CompanyToPayManagerActivity.this, PayBillActivity.class);
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
        Button btnConfirm,btnReject,btnPayNow;
        LinearLayout optionsLayout,payNowLayout,mainLayout;
        SimpleDateFormat currentDate;
        String saveCurrentDate;
        DatabaseReference postsRef;
        TextView txtDaysToExpire;

        public myPostViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

            imageCompanyVerification = mView.findViewById(R.id.imageCompanyVerification);
            txtDaysToExpire = mView.findViewById(R.id.txtDaysToExpire);
            btnConfirm = mView.findViewById(R.id.btnConfirm);
            btnReject = mView.findViewById(R.id.btnReject);
            btnPayNow = mView.findViewById(R.id.btnPayNow);
            optionsLayout = mView.findViewById(R.id.optionsLayout);
            payNowLayout = mView.findViewById(R.id.payNowLayout);
            mainLayout = mView.findViewById(R.id.mainLayout);
            postsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");

            payNowLayout.getLayoutParams().height = 0;


        }

        public void setBill_code(String bill_code) {
            TextView textView = mView.findViewById(R.id.txtBillNumber);
            textView.setText("Nº"+bill_code);
        }
        public void setMy_company_image(Context ctx, String my_company_image) {
            ImageView image = mView.findViewById(R.id.imageCompany);
            Picasso.with(ctx).load(my_company_image).fit().into(image);
        }
        public void setMy_company_name(String my_company_name) {
            TextView textView = mView.findViewById(R.id.txtName);
            textView.setText("Beneficiario: "+my_company_name);
        }
        public void setMy_company_ruc(String my_company_ruc) {
            TextView textView = mView.findViewById(R.id.txtRuc);
            textView.setText(my_company_ruc);
        }
        public void setMy_company_verification(String my_company_verification) {
            TextView textView = mView.findViewById(R.id.txtCompanyVerification);
            if (my_company_verification.equals("true"))
            {
                imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                textView.setText("Verificado Correctamente");
            }
            if (my_company_verification.equals("false"))
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
        public void setBill_state(String bill_state) {
            TextView textView = mView.findViewById(R.id.txtBillState);
            textView.setText(bill_state);
            if (bill_state.equals("Esperando Confirmación")) {

            }
            if (bill_state.equals("Confirmado y Por Pagar")) {
                optionsLayout.getLayoutParams().height = 0;
                payNowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (bill_state.equals("Financiado Exitosamente")) {
                optionsLayout.getLayoutParams().height = 0;
                textView.setText("Confirmado y Por Pagar");
                payNowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (bill_state.equals("Pagado")) {
                optionsLayout.getLayoutParams().height = 0;
                payNowLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Rechazado")) {
                optionsLayout.getLayoutParams().height = 0;
                payNowLayout.getLayoutParams().height = 0;
            }
            if (bill_state.equals("Vencido")) {
                optionsLayout.getLayoutParams().height = 0;
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
