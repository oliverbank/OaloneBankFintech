package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanReady;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.MyLoansListFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.MyLoansModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class LoanContractFragment extends Fragment {

    TextView txtIssuerMessage,txtClientMessage;
    DatabaseReference companyInfo,userRef;
    String legal_name,ruc, currentUid,fullname,document_number,postkey;
    FirebaseAuth mAuth;
    CheckBox cbContract;
    RecyclerView recyclerView;
    Button btnContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_contract, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        postkey = getActivity().getIntent().getExtras().getString("postkey");

        txtIssuerMessage = view.findViewById(R.id.txtIssuerMessage);
        txtClientMessage = view.findViewById(R.id.txtClientMessage);
        cbContract = view.findViewById(R.id.cbContract);
        btnContinue= view.findViewById(R.id.btnContinue);

        companyInfo = FirebaseDatabase.getInstance().getReference().child("Contracts");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        companyInfo.child("Company Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                legal_name = dataSnapshot.child("legal_name").getValue().toString();
                ruc = dataSnapshot.child("ruc").getValue().toString();

                txtIssuerMessage.setText("Emisor del crédito: "+legal_name+" con RUC "+ruc+". En adelante Oliver Bank.");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fullname = dataSnapshot.child("fullname").getValue().toString();
                document_number = dataSnapshot.child("document_number").getValue().toString();

                txtClientMessage.setText("Beneficiario del crédito: "+fullname+" con DNI "+document_number+". En adelante Cliente.");
                cbContract.setText("Yo "+fullname+" con DNI "+document_number+", afirmo que he leído y acepto los terminos y condiciones del presente contrato.");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cbContract.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Loan Approved").child("contract_checked").setValue("true");

                } else {
                    userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(postkey).child("Loan Approved").child("contract_checked").setValue("false");
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExpressLoanReadySumaryActivity.class);
                intent.putExtra("FRAGMENT_ID2", 3);
                startActivity(intent);
            }
        });

        showContract();

        return view;
    }

    private void showContract() {
        Query query = companyInfo.child("Express Loan").orderByChild("position");
        FirebaseRecyclerAdapter<ContractItemsModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ContractItemsModel, myPostViewHolder>
                (ContractItemsModel.class,R.layout.contract_clause_item,myPostViewHolder.class,query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, ContractItemsModel model, int position) {
                viewHolder.setMessage(model.getMessage());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setPosition(model.getPosition());

                viewHolder.txtMessage.setText(viewHolder.my_message);
                viewHolder.txtTitle.setText(viewHolder.my_title);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView txtTitle,txtMessage;
        String my_title,my_message;

        public myPostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.txtTitle);
            txtMessage = mView.findViewById(R.id.txtMessage);

        }

        public void setMessage(String message) {
            my_message = message;
        }

        public void setPosition(String position) {

        }
        public void setTitle(String title) {
            my_title = title;
        }
    }
}