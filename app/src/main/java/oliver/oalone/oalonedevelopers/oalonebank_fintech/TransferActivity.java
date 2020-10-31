package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompaniesCheckoutActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.MyCompaniesModel;

public class TransferActivity extends AppCompatActivity {

    private EditText edtUsername;
    private RecyclerView searcchResultList;
    private DatabaseReference allUserDatabaseRef,allCompaniesRef;
    RelativeLayout rootLayout;
    RadioButton rdPerson,rdCompany;
    String filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

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
        String reference = "company_name";

        Query searchPeopleQuery = allCompaniesRef.orderByChild(reference).startAt(edtUsername.getText().toString().toUpperCase()).endAt(edtUsername.getText().toString().toUpperCase()+"\uf8ff");
        FirebaseRecyclerAdapter<MyCompaniesModel,CompaniesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyCompaniesModel, CompaniesViewHolder>
                (MyCompaniesModel.class, R.layout.company_item_for_search, CompaniesViewHolder.class, searchPeopleQuery) {
            @Override
            protected void populateViewHolder(CompaniesViewHolder viewHolder, MyCompaniesModel model, final int position) {
                final String visit_user_id = getRef(position).getKey();
                viewHolder.setCompany_name(model.getCompany_name());
                viewHolder.setCompany_ruc(model.getCompany_ruc());
                viewHolder.setCompany_image(getApplicationContext(), model.getCompany_image());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(TransferActivity.this, CompaniesCheckoutActivity.class);
                        intent.putExtra("company_key", visit_user_id);
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
        String reference = "username";
        Query searchPeopleQuery = allUserDatabaseRef.orderByChild(reference).startAt(edtUsername.getText().toString().toLowerCase()).endAt(edtUsername.getText().toString().toLowerCase()+"\uf8ff");
        FirebaseRecyclerAdapter<UsersModel,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersModel, UsersViewHolder>
                (UsersModel.class, R.layout.user_item_layout, UsersViewHolder.class, searchPeopleQuery) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, UsersModel model, final int position) {
                final String visit_user_id = getRef(position).getKey();
                viewHolder.setUsername(model.getUsername());
                viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TransferActivity.this, RegisterTransferActivity.class);
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
