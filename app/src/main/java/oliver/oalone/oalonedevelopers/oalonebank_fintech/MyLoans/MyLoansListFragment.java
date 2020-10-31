package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanReady.ExpressLoanReadySumaryActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.LaonPayment.LoanPaymentListActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class MyLoansListFragment extends Fragment {

    RecyclerView recyclerView;
    String currentUid;
    FirebaseAuth mAuth;
    DatabaseReference expressLoanRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_loans_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.recyclerView);

        expressLoanRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("Requests Sent");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        showLoans();

        return view;
    }

    private void showLoans() {
        Query query = expressLoanRef.orderByChild("timestamp");
        FirebaseRecyclerAdapter<MyLoansModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyLoansModel, myPostViewHolder>
                (MyLoansModel.class,R.layout.loan_item,myPostViewHolder.class,query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, MyLoansModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setDate(model.getDate());
                viewHolder.setLoan_state(model.getLoan_state());
                viewHolder.setLoan_type(model.getLoan_type());
                viewHolder.setTime(model.getTime());

                if (viewHolder.my_loan_type.equals("express_loan")) {
                    viewHolder.txtLoanName.setText("Préstamo Express");
                    viewHolder.imgLoanImage.setImageResource(R.drawable.loan1_icon);
                }

                if (viewHolder.my_loan_state.equals("progress")) {
                    viewHolder.layoutApproved.getLayoutParams().height = 0;
                    viewHolder.layoutRejected.getLayoutParams().height = 0;
                    viewHolder.layoutReady.getLayoutParams().height = 0;
                    viewHolder.layoutButton.getLayoutParams().height = 0;
                    viewHolder.layoutState.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.layoutOptions.getLayoutParams().height = 0;

                    viewHolder.imgSendRequest.setImageResource(R.drawable.send_request_icon);
                    viewHolder.txtSendRequest.setTextColor(Color.BLACK);
                }
                if (viewHolder.my_loan_state.equals("checking")) {
                    viewHolder.layoutApproved.getLayoutParams().height = 0;
                    viewHolder.layoutRejected.getLayoutParams().height = 0;
                    viewHolder.layoutReady.getLayoutParams().height = 0;
                    viewHolder.layoutButton.getLayoutParams().height = 0;
                    viewHolder.layoutState.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.layoutOptions.getLayoutParams().height = 0;

                    viewHolder.imgSendRequest.setImageResource(R.drawable.send_request_icon);
                    viewHolder.txtSendRequest.setTextColor(Color.BLACK);
                    viewHolder.imgChecking.setImageResource(R.drawable.checklist_icon);
                    viewHolder.txtChecking.setTextColor(Color.BLACK);
                }
                if (viewHolder.my_loan_state.equals("approved")) {
                    viewHolder.layoutApproved.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.layoutRejected.getLayoutParams().height = 0;
                    viewHolder.layoutReady.getLayoutParams().height = 0;
                    viewHolder.layoutButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.layoutState.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.layoutOptions.getLayoutParams().height = 0;

                    viewHolder.imgSendRequest.setImageResource(R.drawable.send_request_icon);
                    viewHolder.txtSendRequest.setTextColor(Color.BLACK);
                    viewHolder.imgChecking.setImageResource(R.drawable.checklist_icon);
                    viewHolder.txtChecking.setTextColor(Color.BLACK);
                    viewHolder.imgApproved.setImageResource(R.drawable.check_mark);
                    viewHolder.txtApproved.setTextColor(Color.BLACK);
                    viewHolder.txtApproved.setText("Aprobado");
                }
                if (viewHolder.my_loan_state.equals("rejected")) {
                    viewHolder.layoutRejected.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.layoutApproved.getLayoutParams().height = 0;
                    viewHolder.layoutReady.getLayoutParams().height = 0;
                    viewHolder.layoutButton.getLayoutParams().height = 0;
                    viewHolder.layoutState.getLayoutParams().height = 0;
                    viewHolder.layoutOptions.getLayoutParams().height = 0;

                    viewHolder.imgSendRequest.setImageResource(R.drawable.send_request_icon);
                    viewHolder.txtSendRequest.setTextColor(Color.BLACK);
                    viewHolder.imgChecking.setImageResource(R.drawable.checklist_icon);
                    viewHolder.txtChecking.setTextColor(Color.BLACK);
                    viewHolder.imgApproved.setImageResource(R.drawable.error_icon);
                    viewHolder.txtApproved.setTextColor(Color.RED);
                    viewHolder.txtApproved.setText("Rechazado");
                }
                if (viewHolder.my_loan_state.equals("ready")) {
                    viewHolder.layoutApproved.getLayoutParams().height = 0;
                    viewHolder.layoutRejected.getLayoutParams().height = 0;
                    viewHolder.layoutReady.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    viewHolder.layoutButton.getLayoutParams().height = 0;
                    viewHolder.layoutState.getLayoutParams().height = 0;
                    viewHolder.layoutOptions.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                    viewHolder.imgSendRequest.setImageResource(R.drawable.send_request_icon);
                    viewHolder.txtSendRequest.setTextColor(Color.BLACK);
                    viewHolder.imgChecking.setImageResource(R.drawable.checklist_icon);
                    viewHolder.txtChecking.setTextColor(Color.BLACK);
                    viewHolder.imgApproved.setImageResource(R.drawable.transaction_completed);
                    viewHolder.txtApproved.setTextColor(Color.BLACK);
                    viewHolder.txtApproved.setText("Listo");
                }

                viewHolder.btnContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ExpressLoanReadySumaryActivity.class);
                        intent.putExtra("postkey",postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.btnPayLoan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), LoanPaymentListActivity.class);
                        intent.putExtra("postkey",postKey);
                        startActivity(intent);
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        String my_date,my_loan_state,my_loan_type,my_time;
        ImageView imgLoanImage,imgSendRequest,imgChecking,imgApproved;
        TextView txtLoanName,txtSendRequest,txtChecking,txtApproved;
        LinearLayout layoutState,layoutApproved,layoutRejected,layoutReady,layoutButton,layoutOptions;
        Button btnContinue,btnPayLoan;

        public myPostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            imgLoanImage = mView.findViewById(R.id.imgLoanImage);
            txtLoanName = mView.findViewById(R.id.txtLoanName);
            imgSendRequest = mView.findViewById(R.id.imgSendRequest);
            txtSendRequest = mView.findViewById(R.id.txtSendRequest);
            imgChecking = mView.findViewById(R.id.imgChecking);
            txtChecking = mView.findViewById(R.id.txtChecking);
            imgApproved = mView.findViewById(R.id.imgApproved);
            txtApproved = mView.findViewById(R.id.txtApproved);
            btnPayLoan = mView.findViewById(R.id.btnPayLoan);

            layoutState = mView.findViewById(R.id.layoutState);
            layoutApproved = mView.findViewById(R.id.layoutApproved);
            layoutRejected = mView.findViewById(R.id.layoutRejected);
            layoutReady = mView.findViewById(R.id.layoutReady);
            layoutButton = mView.findViewById(R.id.layoutButton);
            layoutOptions = mView.findViewById(R.id.layoutOptions);

            btnContinue = mView.findViewById(R.id.btnContinue);
        }

        public void setDate(String date) {
            my_date = date;
        }

        public void setLoan_state(String loan_state) {
            my_loan_state = loan_state;
        }

        public void setLoan_type(String loan_type) {
            my_loan_type = loan_type;
        }

        public void setTime(String time) {
            my_time = time;
        }
    }
}