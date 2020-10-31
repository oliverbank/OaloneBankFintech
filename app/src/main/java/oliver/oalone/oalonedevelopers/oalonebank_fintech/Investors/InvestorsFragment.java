package oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.FinanceRequestDetailActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class InvestorsFragment extends Fragment {

    private RecyclerView postList;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, postRef,likeRef;
    String currentUserID;
    Boolean likeChecker = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_investors, container, false);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests");
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        currentUserID = mAuth.getCurrentUser().getUid();

        postList = view.findViewById(R.id.recyclerView);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        displayAllFinanceRequests();

        return view;
    }

    private void displayAllFinanceRequests() {
        Query showPostMostRecentOrder = postRef.orderByChild("counter");

        FirebaseRecyclerAdapter<FactoringFinanceRequestModel, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FactoringFinanceRequestModel, PostViewHolder>
                (FactoringFinanceRequestModel.class, R.layout.factoring_finance_request_item, PostViewHolder.class, showPostMostRecentOrder) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, FactoringFinanceRequestModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setCompany_image(getActivity().getApplicationContext(),model.getCompany_image());
                viewHolder.setCompany_name(model.getCompany_name());
                viewHolder.setCompany_line(model.getCompany_line());
                viewHolder.setCompany_department(model.getCompany_department());
                viewHolder.setBill_rate(model.getBill_rate());
                viewHolder.setEnd_date(model.end_date);
                viewHolder.setMain_currency(model.getMain_currency());
                viewHolder.setThe_real_investment(model.getThe_real_investment());
                viewHolder.setFinance_request_expired(model.getFinance_request_expired());
                viewHolder.setCustomer_social_reason(model.getCustomer_social_reason());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),FactoringRequestDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        Pair[] pairs = new Pair[1];
                        pairs[0] = new Pair<View, String>(viewHolder.imgCompanyImage,"imageTransition");
                        startActivity(intent);
                    }
                });
                viewHolder.btnInvest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),FactoringRequestDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });

            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        Button btnInvest;
        int countLikes;
        String currentUserId;
        DatabaseReference likesRef;
        ImageView imgCompanyImage;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            btnInvest = mView.findViewById(R.id.btnInvest);
            imgCompanyImage = mView.findViewById(R.id.imgCompanyImage);

            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }

        public void setCompany_image(Context ctx, String company_image)
        {
            ImageView imgCompanyImage = mView.findViewById(R.id.imgCompanyImage);
            Picasso.with(ctx).load(company_image).fit().centerCrop().into(imgCompanyImage);
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
        public void setBill_rate(String bill_rate)
        {
            TextView txtInterestRate = mView.findViewById(R.id.txtInterestRate);
            txtInterestRate.setText(bill_rate);
        }
        public void setEnd_date(String end_date)
        {
            TextView txtPaymentFrecuency = mView.findViewById(R.id.txtPaymentFrecuency);
            txtPaymentFrecuency.setText(end_date);
        }
        public void setMain_currency(String main_currency)
        {
            TextView txtInvestmentCurrency = mView.findViewById(R.id.txtInvestmentCurrency);
            txtInvestmentCurrency.setText(main_currency);
        }
        public void setThe_real_investment(String the_real_investment)
        {
            TextView txtAmmount = mView.findViewById(R.id.txtAmmount);
            txtAmmount.setText("Variable");
        }
        public void setCustomer_social_reason(String customer_social_reason) {
            TextView txtText = mView.findViewById(R.id.txtMyCustomer);
            txtText.setText("Cliente: "+customer_social_reason);
        }
        public void setFinance_request_expired(String finance_request_expired) {
            LinearLayout mainLayout = mView.findViewById(R.id.mainLayout);
            if (finance_request_expired.equals("true")) {
                mainLayout.getLayoutParams().height = 0;
            }
        }
    }

}
