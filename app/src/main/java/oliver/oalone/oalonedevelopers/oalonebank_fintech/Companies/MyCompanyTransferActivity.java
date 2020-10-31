package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterTransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.TransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.UsersModel;

public class MyCompanyTransferActivity extends AppCompatActivity {

    String my_company_key;
    private EditText edtUsername;
    private RecyclerView searcchResultList;
    private DatabaseReference allUserDatabaseRef,allCompaniesRef;
    RelativeLayout rootLayout;
    RadioButton rdPerson,rdCompany;
    String filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_transfer);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        edtUsername = findViewById(R.id.edtUsername);
        rootLayout = findViewById(R.id.rootLayout);
        rdPerson = findViewById(R.id.rdPerson);
        rdCompany = findViewById(R.id.rdCompany);

        allUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        allCompaniesRef = FirebaseDatabase.getInstance().getReference().child("My Companies");

        searcchResultList = findViewById(R.id.search_result_list);
        searcchResultList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
                    if (TextUtils.isEmpty(edtUsername.getText().toString())) {

                    }
                    if (!TextUtils.isEmpty(edtUsername.getText().toString())) {
                        searchCompanies();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchCompanies() {
        Query searchPeopleQuery = allCompaniesRef.orderByChild("company_name").startAt(edtUsername.getText().toString()).endAt(edtUsername.getText().toString()+"\uf8ff");
        FirebaseRecyclerAdapter<MyCompaniesModel, CompaniesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyCompaniesModel, CompaniesViewHolder>
                (MyCompaniesModel.class, R.layout.company_item_for_search, CompaniesViewHolder.class, searchPeopleQuery) {
            @Override
            protected void populateViewHolder(CompaniesViewHolder viewHolder, MyCompaniesModel model, final int position) {
                viewHolder.setCompany_name(model.getCompany_name());
                viewHolder.setCompany_ruc(model.getCompany_ruc());
                viewHolder.setCompany_image(getApplicationContext(), model.getCompany_image());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();
                        Intent intent = new Intent(MyCompanyTransferActivity.this, MyCompanyCompaniesCheckoutActivity.class);
                        intent.putExtra("company_key", visit_user_id);
                        intent.putExtra("my_company_key",my_company_key);
                        startActivity(intent);
                    }
                });
            }
        };
        searcchResultList.setAdapter(firebaseRecyclerAdapter);

    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void searchUsers() {
        Query searchPeopleQuery = allUserDatabaseRef.orderByChild("username").startAt(edtUsername.getText().toString()).endAt(edtUsername.getText().toString()+"\uf8ff");
        FirebaseRecyclerAdapter<UsersModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersModel, UsersViewHolder>
                (UsersModel.class, R.layout.user_item_layout,UsersViewHolder.class, searchPeopleQuery) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, UsersModel model, final int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();
                        Intent intent = new Intent(MyCompanyTransferActivity.this, MyCompanyRegisterTransferActivity.class);
                        intent.putExtra("visit_user_id", visit_user_id);
                        intent.putExtra("my_company_key",my_company_key);
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
            Picasso.with(ctx).load(profileimage).fit().into(image);
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
            Picasso.with(ctx).load(company_image).fit().into(image);
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
