package oliver.oalone.oalonedevelopers.oalonebank_fintech.WalletFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompaniesCheckoutActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.MyCompaniesModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterTransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.TransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.UsersModel;

public class TransferFragment extends Fragment {

    private EditText edtUsername;
    private RecyclerView searcchResultList;
    private DatabaseReference allUserDatabaseRef,allCompaniesRef;
    RelativeLayout rootLayout;
    RadioButton rdPerson,rdCompany;
    String filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);

        edtUsername = view.findViewById(R.id.edtUsername);
        rootLayout = view.findViewById(R.id.rootLayout);
        rdPerson = view.findViewById(R.id.rdPerson);
        rdCompany = view.findViewById(R.id.rdCompany);

        allUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        allCompaniesRef = FirebaseDatabase.getInstance().getReference().child("My Companies");

        searcchResultList = view.findViewById(R.id.search_result_list);
        searcchResultList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        searcchResultList.setLayoutManager(linearLayoutManager);

        edtUsername.setHint("Nombre de usuario...");

        edtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdCompany.isChecked() && !rdPerson.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar a quién transferirás primero (Persona o Empresa)", Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
        });

        rdPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "person";
                edtUsername.setHint("Nombre de usuario...");
            }
        });
        rdCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = "company";
                edtUsername.setHint("Nombre de la empresa o comercio...");
                edtUsername.setEnabled(true);

            }
        });

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (rdPerson.isChecked())
                {
                    if (TextUtils.isEmpty(edtUsername.getText().toString())) {

                    }
                    if (!TextUtils.isEmpty(edtUsername.getText().toString())) {
                        searchUsers();
                    }
                }
                if (rdCompany.isChecked())
                {
                    if (!TextUtils.isEmpty(edtUsername.getText().toString())) {
                        searchCompanies();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    private void searchCompanies() {
        String reference = "company_name";

        Query searchPeopleQuery = allCompaniesRef.orderByChild(reference).startAt(edtUsername.getText().toString().toUpperCase()).endAt(edtUsername.getText().toString().toUpperCase()+"\uf8ff");
        FirebaseRecyclerAdapter<MyCompaniesModel, CompaniesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyCompaniesModel, CompaniesViewHolder>
                (MyCompaniesModel.class, R.layout.company_item_for_search, CompaniesViewHolder.class, searchPeopleQuery) {
            @Override
            protected void populateViewHolder(CompaniesViewHolder viewHolder, MyCompaniesModel model, final int position) {
                final String visit_user_id = getRef(position).getKey();
                viewHolder.setCompany_name(model.getCompany_name());
                viewHolder.setCompany_ruc(model.getCompany_ruc());
                viewHolder.setCompany_image(getActivity().getApplicationContext(), model.getCompany_image());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), CompaniesCheckoutActivity.class);
                        intent.putExtra("company_key", visit_user_id);
                        startActivity(intent);
                    }
                });
            }
        };
        searcchResultList.setAdapter(firebaseRecyclerAdapter);

    }


    private void searchUsers() {
        String reference = "username";
        Query searchPeopleQuery = allUserDatabaseRef.orderByChild(reference).startAt(edtUsername.getText().toString().toLowerCase()).endAt(edtUsername.getText().toString().toLowerCase()+"\uf8ff");
        FirebaseRecyclerAdapter<UsersModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersModel, UsersViewHolder>
                (UsersModel.class, R.layout.user_item_layout, UsersViewHolder.class, searchPeopleQuery) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, UsersModel model, final int position) {
                final String visit_user_id = getRef(position).getKey();
                viewHolder.setUsername(model.getUsername());
                viewHolder.setProfileimage(getActivity().getApplicationContext(), model.getProfileimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), RegisterTransferActivity.class);
                        intent.putExtra("visit_user_id", visit_user_id);
                        startActivity(intent);
                    }
                });
            }
        };
        searcchResultList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView image = mView.findViewById(R.id.profileImage);
            Picasso.with(ctx).load(profileimage).fit().centerCrop().into(image);
        }
        public void setUsername(String username)
        {
            TextView userName = mView.findViewById(R.id.txtUsername);
            userName.setText(username);
        }
    }

    public static class CompaniesViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public CompaniesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setCompany_image(Context ctx, String company_image) {
            CircleImageView image = mView.findViewById(R.id.imageCompany);
            Picasso.with(ctx).load(company_image).fit().centerCrop().into(image);
        }
        public void setCompany_name(String company_name) {
            TextView userName = mView.findViewById(R.id.edtName);
            userName.setText(company_name);
        }
        public void setCompany_ruc(String company_ruc) {
            TextView userName = mView.findViewById(R.id.edtRuc);
            userName.setText(company_ruc);
        }
    }

}
