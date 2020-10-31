package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.xw.repo.BubbleSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompaniesForFinanceRequestActivity extends AppCompatActivity {

    private RecyclerView postList;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, postRef,likeRef;
    String currentUserID;
    Boolean likeChecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies_for_finance_request);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Finance Requests");
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        currentUserID = mAuth.getCurrentUser().getUid();

        postList = findViewById(R.id.recyclerView);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        displayAllFinanceRequests();

        getDataDate();
    }

    private void displayAllFinanceRequests() {
        Query showPostMostRecentOrder = postRef.orderByChild("counter");

        FirebaseRecyclerAdapter<FinanceRequestModel, CompaniesFragment.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FinanceRequestModel, CompaniesFragment.PostViewHolder>
                (FinanceRequestModel.class,R.layout.finance_request_item, CompaniesFragment.PostViewHolder.class,showPostMostRecentOrder) {
            @Override
            protected void populateViewHolder(CompaniesFragment.PostViewHolder viewHolder, FinanceRequestModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setCompany_image(getApplicationContext(),model.getCompany_image());
                viewHolder.setCompany_name(model.getCompany_name());
                viewHolder.setCompany_line(model.getCompany_line());
                viewHolder.setCompany_department(model.getCompany_department());
                viewHolder.setMain_currency(model.getMain_currency());
                viewHolder.setFinance_destiny(model.getFinance_destiny());
                viewHolder.setAmmount(model.getAmmount());
                viewHolder.setInterest_rate(model.getInterest_rate());
                viewHolder.setFinancing_frecuency(model.getFinancing_frecuency());
                viewHolder.setInvested(model.getInvested());
                viewHolder.setNo_invested(model.getNo_invested());
                viewHolder.setCredit_score(model.getCredit_score());
                viewHolder.setFinance_request_expired(model.getFinance_request_expired());



                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CompaniesForFinanceRequestActivity.this,FinanceRequestDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });
                viewHolder.btnInvest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CompaniesForFinanceRequestActivity.this,FinanceRequestDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.seekBarPen.setEnabled(false);
                viewHolder.seekBarPen.getConfigBuilder()
                        .min(0)
                        .max((float) viewHolder.total_ammount)
                        .progress((float) viewHolder.ammount_invested)
                        .build();

            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        Button btnInvest;
        String currentUserId;
        DatabaseReference likesRef;
        double ammount_invested, ammount_no_invested, total_ammount;
        BubbleSeekBar seekBarPen;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            btnInvest = mView.findViewById(R.id.btnInvest);
            seekBarPen = mView.findViewById(R.id.seekBarPen);

            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }

        public void setCompany_image(Context ctx, String company_image)
        {
            ImageView imgCompanyImage = mView.findViewById(R.id.imgCompanyImage);
            Picasso.with(ctx).load(company_image).fit().into(imgCompanyImage);
        }
        public void setCompany_name(String company_name)
        {
            TextView txtCompanyName = mView.findViewById(R.id.txtCompanyName);
            txtCompanyName.setText("Empresa "+company_name);
            txtCompanyName.setSelected(true);
        }
        public void setCompany_line(String company_line)
        {
            TextView txtBusinessLine = mView.findViewById(R.id.txtBusinessLine);
            txtBusinessLine.setText("Actividad ecnómica: "+company_line);
        }
        public void setCompany_department(String company_department)
        {
            TextView txtCompanyLocation = mView.findViewById(R.id.txtCompanyLocation);
            txtCompanyLocation.setText("Localización: "+company_department);
        }
        public void setMain_currency(String main_currency)
        {
            TextView txtMainCurrency = mView.findViewById(R.id.txtMainCurrency);
            TextView txtInvestmentCurrency = mView.findViewById(R.id.txtInvestmentCurrency);
            TextView txtInvestedCurrency1 = mView.findViewById(R.id.txtInvestedCurrency1);
            TextView txtInvestedCurrency2 = mView.findViewById(R.id.txtInvestedCurrency2);
            txtMainCurrency.setText("Moneda: "+main_currency);
            txtInvestmentCurrency.setText(main_currency);
            txtInvestedCurrency1.setText(main_currency);
            txtInvestedCurrency2.setText(main_currency);
        }
        public void setFinance_destiny(String finance_destiny)
        {
            TextView txtFinanceDestination = mView.findViewById(R.id.txtFinanceDestination);
            txtFinanceDestination.setText("Financiamiento para "+finance_destiny);
        }
        public void setAmmount(String ammount)
        {
            TextView txtAmmount = mView.findViewById(R.id.txtAmmount);
            txtAmmount.setText(ammount);
            total_ammount = Double.parseDouble(ammount);
        }
        public void setInterest_rate(String interest_rate)
        {
            TextView txtInterestRate = mView.findViewById(R.id.txtInterestRate);
            txtInterestRate.setText(interest_rate);
        }
        public void setFinancing_frecuency(String financing_frecuency)
        {
            TextView txtPaymentFrecuency = mView.findViewById(R.id.txtPaymentFrecuency);
            txtPaymentFrecuency.setText(financing_frecuency);
        }
        public void setInvested(String invested) {
            TextView textView = mView.findViewById(R.id.txtInvested);
            textView.setText(invested);
            ammount_invested = Double.parseDouble(invested);
        }
        public void setNo_invested(String no_invested) {
            TextView textView = mView.findViewById(R.id.txtNoInvested);
            textView.setText(no_invested);
            ammount_no_invested = Double.parseDouble(no_invested);
        }
        public void setCredit_score(String credit_score) {
            TextView txtCreditScore = mView.findViewById(R.id.txtCreditScore);
            if (credit_score.equals("0"))
            {
                txtCreditScore.setText("RIESGO DE LA EMPRESA: Normal");
            }
            if (credit_score.equals("1"))
            {
                txtCreditScore.setText("RIESGO DE LA EMPRESA: Problemas Potenciales");
            }
            if (credit_score.equals("2"))
            {
                txtCreditScore.setText("RIESGO DE LA EMPRESA: Deficiente");
            }
            if (credit_score.equals("3"))
            {
                txtCreditScore.setText("RIESGO DE LA EMPRESA: Dudoso");
            }
            if (credit_score.equals("4"))
            {
                txtCreditScore.setText("RIESGO DE LA EMPRESA: Pérdida");
            }
            if (credit_score.equals("5"))
            {
                txtCreditScore.setText("RIESGO DE LA EMPRESA: Desconocido");
            }
        }
        public void setFinance_request_expired(String finance_request_expired) {
            LinearLayout mainLayout = mView.findViewById(R.id.mainLayout);
            if (finance_request_expired.equals("true")) {
                mainLayout.getLayoutParams().height = 0;
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
