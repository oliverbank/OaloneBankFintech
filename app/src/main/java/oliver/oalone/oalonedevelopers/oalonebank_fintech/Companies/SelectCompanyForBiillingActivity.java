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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class SelectCompanyForBiillingActivity extends AppCompatActivity {

    private EditText edtUsername;
    private RecyclerView searcchResultList;
    private DatabaseReference allCompaniesDatabase;
    RelativeLayout rootLayout;
    String postKey,my_company_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company_for_biilling);


        edtUsername = findViewById(R.id.edtUsername);
        rootLayout = findViewById(R.id.rootLayout);
        my_company_key = getIntent().getExtras().get("my_company_key").toString();

        allCompaniesDatabase = FirebaseDatabase.getInstance().getReference().child("My Companies");

        searcchResultList = findViewById(R.id.search_result_list);
        searcchResultList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        searcchResultList.setLayoutManager(linearLayoutManager);

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchUsers() {
        Query searchCompanyQuery = allCompaniesDatabase.orderByChild("company_ruc").startAt(edtUsername.getText().toString()).endAt(edtUsername.getText().toString()+"\uf8ff");
        FirebaseRecyclerAdapter<MyCompaniesModel, MyCompaniesActivity.myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyCompaniesModel, MyCompaniesActivity.myPostViewHolder>
                (MyCompaniesModel.class, R.layout.company_item_for_search, MyCompaniesActivity.myPostViewHolder.class, searchCompanyQuery) {
            @Override
            protected void populateViewHolder(MyCompaniesActivity.myPostViewHolder viewHolder, MyCompaniesModel model, final int position) {
                postKey = getRef(position).getKey();
                viewHolder.setCompany_image(getApplicationContext(),model.getCompany_image());
                viewHolder.setCompany_name(model. getCompany_name());
                viewHolder.setCompany_ruc(model.getCompany_ruc());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postKey = getRef(position).getKey();
                        Intent intent = new Intent(SelectCompanyForBiillingActivity.this, CreateBillActivity.class);
                        intent.putExtra("buyer_key", postKey);
                        intent.putExtra("my_company_key", my_company_key);
                        startActivity(intent);
                        finish();
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

    public static class myPostViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        String currentUserId;
        Button btnFinance,btnPay,btnReceive,btnManage;
        ImageView verification_process_image;

        public myPostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setCompany_image(Context ctx, String company_image) {
            ImageView image = mView.findViewById(R.id.imageCompany);
            Picasso.with(ctx).load(company_image).fit().into(image);
        }
        public void setCompany_name(String company_name) {
            TextView textView = mView.findViewById(R.id.edtName);
            textView.setText(company_name);
        }
        public void  setCompany_ruc(String company_ruc) {
            TextView textView = mView.findViewById(R.id.edtRuc);
            textView.setText(company_ruc);
        }
    }
}
