package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.PersonalLendings;

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
import oliver.oalone.oalonedevelopers.oalonebank_fintech.UsersModel;

public class CompanySearchUserForLendingActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_company_search_user_for_lending);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        edtUsername = findViewById(R.id.edtUsername);
        rootLayout = findViewById(R.id.rootLayout);
        rdPerson = findViewById(R.id.rdPerson);
        rdCompany = findViewById(R.id.rdCompany);

        allUserDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
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
                edtUsername.setHint("Nombre de usuario...");
            }
        });
        rdCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUsername.setHint("DNI...");
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
                        searchDni();
                    }

                }
                if (edtUsername.getText().toString().equals("")) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void searchDni() {
        String reference = "document_number";
        Query searchPeopleQuery = allUserDatabaseRef.orderByChild(reference).startAt(edtUsername.getText().toString().toLowerCase()).endAt(edtUsername.getText().toString().toLowerCase()+"\uf8ff");
        FirebaseRecyclerAdapter<UsersModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UsersModel, UsersViewHolder>
                (UsersModel.class, R.layout.user_item_layout, UsersViewHolder.class, searchPeopleQuery) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, UsersModel model, final int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();
                        Intent intent = new Intent(CompanySearchUserForLendingActivity.this, CompanySendLendingNotificationActivity.class);
                        intent.putExtra("visit_user_id", visit_user_id);
                        intent.putExtra("my_company_key", my_company_key);
                        startActivity(intent);
                        finish();
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
                viewHolder.setUsername(model.getUsername());
                viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();
                        Intent intent = new Intent(CompanySearchUserForLendingActivity.this, CompanySendLendingNotificationActivity.class);
                        intent.putExtra("visit_user_id", visit_user_id);
                        intent.putExtra("my_company_key", my_company_key);
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
}
