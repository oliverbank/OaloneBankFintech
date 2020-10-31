package oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.HomeFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class InvestmentFundsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, fundRef,ratesRef;
    String currentUserID;
    FirebaseRecyclerAdapter<FundModel, PostViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_funds);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        fundRef = FirebaseDatabase.getInstance().getReference().child("Investment Funds");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        currentUserID = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayInvestmentFunds();
    }

    private void displayInvestmentFunds() {
        Query showPostMostRecentOrder = fundRef.orderByChild("fund_monthly_profit");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FundModel, PostViewHolder>
                (FundModel.class,R.layout.investment_fund_item, PostViewHolder.class,showPostMostRecentOrder) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, FundModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setFund_image(getApplicationContext(),model.getFund_image());
                viewHolder.setFund_name(model.getFund_name());
                viewHolder.setFund_investment(model.getFund_investment());
                viewHolder.setFund_currency(model.getFund_currency());
                viewHolder.setFund_anual_profit(model.getFund_anual_profit());
                viewHolder.setFund_monthly_profit(model.getFund_monthly_profit());
                viewHolder.setFund_risk(model.getFund_risk());
                viewHolder.setQuote_value(model.getQuote_value());

                viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(InvestmentFundsActivity.this,InvestmentFundDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });
                viewHolder.btnInvest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(InvestmentFundsActivity.this,InvestmentFundDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        Button btnInvest, btnDetail;
        String currentUserId;

        public PostViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

            btnInvest = mView.findViewById(R.id.btnInvest);
            btnDetail = mView.findViewById(R.id.btnDetail);
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setFund_image(Context ctx, String fund_image)
        {
            ImageView imageViewFund = mView.findViewById(R.id.imageViewFund);
            Picasso.with(ctx).load(fund_image).fit().into(imageViewFund);
        }
        public void setFund_name(String fund_name) {
            TextView textView = mView.findViewById(R.id.txtFundName);
            textView.setText(fund_name);
        }
        public void setFund_investment(String fund_investment) {
            TextView textView = mView.findViewById(R.id.txtFundInvestment);
            textView.setText(fund_investment);
        }
        public void setFund_currency(String fund_currency) {
            TextView textView = mView.findViewById(R.id.txtFundCurrency);
            TextView txtQuoteCurrency = mView.findViewById(R.id.txtQuoteCurrency);
            textView.setText(fund_currency);
            txtQuoteCurrency.setText(fund_currency);
        }
        public void setFund_anual_profit(String fund_anual_profit) {
            TextView textView = mView.findViewById(R.id.txtFundAnualProfit);
            textView.setText(fund_anual_profit);
        }
        public void setFund_monthly_profit(String fund_monthly_profit) {
            TextView textView = mView.findViewById(R.id.txtFundMonthlyProfit);
            textView.setText(fund_monthly_profit);
        }
        public void setFund_risk(String fund_risk) {
            TextView textView = mView.findViewById(R.id.txtFundRisk);
            textView.setText(fund_risk);
        }
        public void setQuote_value(String quote_value) {
            TextView txtQuoteValue = mView.findViewById(R.id.txtQuoteValue);
            txtQuoteValue.setText(quote_value);
        }
    }
}
