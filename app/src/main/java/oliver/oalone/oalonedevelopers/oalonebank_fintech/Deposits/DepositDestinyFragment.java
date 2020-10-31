package oliver.oalone.oalonedevelopers.oalonebank_fintech.Deposits;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.OliverBankAccountsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;


public class DepositDestinyFragment extends Fragment {

    DatabaseReference bankAccounts,userRef;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    String currentUid,deposit_amount,deposit_currency;
    Button btnContinue;
    RelativeLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deposit_destiny, container, false);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        bankAccounts = FirebaseDatabase.getInstance().getReference().child("Bank Accounts");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.recyclerView);
        btnContinue= view.findViewById(R.id.btnContinue);
        rootLayout = view.findViewById(R.id.rootLayout);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);



        userRef.child(currentUid).child("Current Deposit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("deposit_amount") && dataSnapshot.hasChild("deposit_currency")) {
                    deposit_amount = dataSnapshot.child("deposit_amount").getValue().toString();
                    deposit_currency = dataSnapshot.child("deposit_currency").getValue().toString();


                    displayOliverBankAccounts();

                } else {
                    displayAllOliverBankAccounts();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterDepositActivity.class);
                intent.putExtra("FRAGMENT_ID", 2);
                startActivity(intent);
            }
        });

        return view;
    }

    private void displayAllOliverBankAccounts() {
        Query query = bankAccounts.orderByChild("currrency");
        FirebaseRecyclerAdapter<OliverBankAccountsModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OliverBankAccountsModel, UsersViewHolder>
                (OliverBankAccountsModel.class, R.layout.oliverbank_accounts_layout, UsersViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, OliverBankAccountsModel model, int position) {
                viewHolder.setCc_code(model.getCc_code());
                viewHolder.setCci_code(model.getCci_code());
                viewHolder.setImage(model.getImage());
                viewHolder.setCurrrency(model.getCurrrency());
                viewHolder.setName(model.getName());

                Picasso.with(getActivity()).load(viewHolder.bank_image).fit().into(viewHolder.financial_institution_image);

                viewHolder.btnCcCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("TextView",viewHolder.txtCcCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Nª de Centa Bancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.btnCciCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCciCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Nª de Centa Interbancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void displayOliverBankAccounts() {
        Query query = bankAccounts.orderByChild("currrency").startAt(deposit_currency).endAt(deposit_currency+"\uf8ff");
        FirebaseRecyclerAdapter<OliverBankAccountsModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OliverBankAccountsModel, UsersViewHolder>
                (OliverBankAccountsModel.class, R.layout.oliverbank_accounts_layout, UsersViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, OliverBankAccountsModel model, int position) {
                viewHolder.setCc_code(model.getCc_code());
                viewHolder.setCci_code(model.getCci_code());
                viewHolder.setImage(model.getImage());
                viewHolder.setCurrrency(model.getCurrrency());
                viewHolder.setName(model.getName());

                Picasso.with(getActivity()).load(viewHolder.bank_image).fit().into(viewHolder.financial_institution_image);

                viewHolder.btnCcCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("TextView",viewHolder.txtCcCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Nª de Centa Bancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.btnCciCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCciCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Nª de Centa Interbancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageButton btnCcCode,btnCciCode;
        TextView txtCcCode,txtCciCode;
        CircleImageView financial_institution_image;
        String bank_image;


        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            btnCcCode = mView.findViewById(R.id.btnCcCode);
            btnCciCode = mView.findViewById(R.id.btnCciCode);
            txtCcCode = mView.findViewById(R.id.txtCcCode);
            txtCciCode = mView.findViewById(R.id.txtCciCode);
            financial_institution_image = mView.findViewById(R.id.financialInstitutionImage);

        }
        public void setCc_code(final String cc_code) {
            TextView textView = mView.findViewById(R.id.txtCcCode);
            textView.setText(cc_code);

        }
        public void setCci_code(String cci_code) {
            TextView textView = mView.findViewById(R.id.txtCciCode);
            textView.setText(cci_code);

        }
        public void setCurrrency(String currrency) {
            TextView textView = mView.findViewById(R.id.txtCurrency);
            textView.setText(currrency);

        }
        public void setImage(String image)
        {
            bank_image = image;
        }
        public void setName(String name) {
            TextView textView = mView.findViewById(R.id.txtName);
            textView.setText(name);
        }
    }
}