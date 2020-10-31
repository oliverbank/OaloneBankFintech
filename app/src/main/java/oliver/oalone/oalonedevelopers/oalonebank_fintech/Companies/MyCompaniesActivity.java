package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.PersonalLendings.MyCompanySendedPersonalLendingsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DocumentVerificationActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.MySendedPersonalLendingsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class MyCompaniesActivity extends AppCompatActivity {

    Button btnAddCompany;
    private RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference postsRef,userRef, permissionRef;
    private String currentUserID,user_verification, add_company_permission,document_verification;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_companies);

        btnAddCompany = findViewById(R.id.btnAddCompany);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        permissionRef = FirebaseDatabase.getInstance().getReference().child("Permissions");

        rootLayout = findViewById(R.id.rootLayout);

        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    user_verification = dataSnapshot.child("user_verification").getValue().toString();
                    document_verification = dataSnapshot.child("document_verification").getValue().toString();

                    permissionRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                add_company_permission = dataSnapshot.child("add_company_permission").getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAddCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_company_permission.equals("false"))
                {
                    Snackbar.make(rootLayout, "Por el momento esta función no está habilitada, Oliver Bank lo arreglará pronto.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (document_verification.equals("none"))
                {
                    showRestrictionDialog();
                }
                else if (document_verification.equals("in_progress"))
                {
                    showRestriction2Dialog();
                }
                else if (user_verification.equals("true"))
                {
                    showConditionDialog();
                }
            }
        });

        displayMyCompaniess();
    }

    private void showRestriction2Dialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_document_in_progress,null);

        TextView txtOkay = add_bank_account.findViewById(R.id.txtOkay);

        txtOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.setView(add_bank_account);
        dialog.show();
    }

    private void showRestrictionDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_user_verification,null);

        Button btnIdentify = add_bank_account.findViewById(R.id.btnIdentify);

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompaniesActivity.this, DocumentVerificationActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });


        dialog.setView(add_bank_account);
        dialog.show();
    }

    private void showConditionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.business_condition_layout,null);

        ImageButton btnPersonalBusiness,btnFormalCompany;

        btnPersonalBusiness = pin_dialog.findViewById(R.id.btnPersonalBusiness);
        btnFormalCompany = pin_dialog.findViewById(R.id.btnFormalCompany);

        btnPersonalBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompaniesActivity.this, AddPersonalBusinessActivity.class);
                startActivity(intent);
            }
        });

        btnFormalCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompaniesActivity.this, AddCompanyActivity.class);
                startActivity(intent);
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }

    String  postKey2;
    private void displayMyCompaniess() {
        Query myPostQuery = postsRef.orderByChild("uid").startAt(currentUserID).endAt(currentUserID+"\uf8ff");
        FirebaseRecyclerAdapter<MyCompaniesModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyCompaniesModel, myPostViewHolder>
                (MyCompaniesModel.class, R.layout.my_company_item, myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final myPostViewHolder viewHolder, MyCompaniesModel model, final int position) {
                final String postKey = getRef(position).getKey();
                postKey2 = postKey;
                viewHolder.setCompany_image(getApplicationContext(),model.getCompany_image());
                viewHolder.setCompany_name(model. getCompany_name());
                viewHolder.setCompany_ruc(model.getCompany_ruc());
                viewHolder.setCompany_verification(model.getCompany_verification());
                viewHolder.setCredit_score(model.getCredit_score());


                viewHolder.btnFinance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //showFinanceMethodDialog();
                        if (viewHolder.my_company_verification.equals("progress")) {
                            showRestrictionDialog();
                        }
                        if (viewHolder.my_company_verification.equals("false")) {
                            showRestrictionDialog();
                        }
                        if (viewHolder.my_credit_score.equals("1")) {

                        }
                        if (viewHolder.my_credit_score.equals("2")) {

                        }
                        if (viewHolder.my_credit_score.equals("3")) {

                        }
                        if (viewHolder.my_credit_score.equals("4")) {

                        }
                        if (viewHolder.my_company_verification.equals("true")) {
                            Intent intent = new Intent(MyCompaniesActivity.this, FinanceMyCompanyActivity.class);
                            intent.putExtra("PostKey", postKey);
                            startActivity(intent);
                        }
                    }
                });

                viewHolder.btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Go To activity to see my receipts
                        Intent intent = new Intent(MyCompaniesActivity.this, CompanyToPayManagerActivity.class);
                        intent.putExtra("my_company_key", postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.btnReceive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyCompaniesActivity.this, CompanyPaymentManagerActivity.class);
                        intent.putExtra("my_company_key", postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.btnManage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Go To activity to manage company
                    }
                });

                viewHolder.showQrLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyCompaniesActivity.this, QrCompanyCodeActivity.class);
                        intent.putExtra("my_company_key", postKey);
                        startActivity(intent);
                    }
                });
                viewHolder.accountsAndCreditsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyCompaniesActivity.this, MyCompanyAccountActivity.class);
                        intent.putExtra("my_company_key", postKey);
                        startActivity(intent);
                    }
                });
                viewHolder.operationsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyCompaniesActivity.this, MyCompanyOperationsActivity.class);
                        intent.putExtra("my_company_key", postKey);
                        startActivity(intent);
                    }
                });
                viewHolder.btnInvest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showInvestDialog();
                    }

                    private void showInvestDialog() {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MyCompaniesActivity.this);
                        dialog.setTitle("Para invertir");
                        dialog.setIcon(R.drawable.money2_icon);

                        LayoutInflater inflater = LayoutInflater.from(MyCompaniesActivity.this);
                        View finance_method = inflater.inflate(R.layout.company_invest_button_layout,null);

                        LinearLayout btnSendLendingToPerson = finance_method.findViewById(R.id.btnSendLendingToPerson);

                        btnSendLendingToPerson.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (viewHolder.my_company_verification.equals("true"))
                                {
                                    Intent intent = new Intent(MyCompaniesActivity.this, MyCompanySendedPersonalLendingsActivity.class);
                                    intent.putExtra("my_company_key", postKey);
                                    startActivity(intent);
                                }
                            }
                        });

                        dialog.setView(finance_method);

                        dialog.setView(finance_method);
                        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    private void showFinanceMethodDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Método de financiamiento");
        dialog.setIcon(R.drawable.money2_icon);
        dialog.setMessage("¿Cómo deseas financiar tu empresa?");

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.finance_request_method_layout,null);

        LinearLayout factoringLayout,warrantyLayout;

        factoringLayout = finance_method.findViewById(R.id.factoringLayout);
        warrantyLayout = finance_method.findViewById(R.id.warrantyLayout);

        factoringLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompaniesActivity.this, FactorringMyCompanyActivity.class);
                intent.putExtra("PostKey", postKey2);
                startActivity(intent);
            }
        });
        warrantyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompaniesActivity.this, FinanceMyCompanyActivity.class);
                intent.putExtra("PostKey", postKey2);
                startActivity(intent);
            }
        });

        dialog.setView(finance_method);
        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        String currentUserId,my_company_verification, my_credit_score;
        Button btnFinance,btnPay,btnReceive,btnManage,btnInvest;
        ImageView verification_process_image;
        LinearLayout showQrLayout,accountsAndCreditsLayout,operationsLayout;

        public myPostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            btnFinance = mView.findViewById(R.id.btnFinance);
            btnPay = mView.findViewById(R.id.btnPay);
            btnReceive = mView.findViewById(R.id.btnReceive);
            btnManage = mView.findViewById(R.id.btnManage);
            verification_process_image = mView.findViewById(R.id.imageCompanyVerification);
            showQrLayout = mView.findViewById(R.id.showQrLayout);
            accountsAndCreditsLayout = mView.findViewById(R.id.accountsAndCreditsLayout);
            operationsLayout = mView.findViewById(R.id.operationsLayout);
            btnInvest = mView.findViewById(R.id.btnInvest);
        }

        public void setCompany_image(Context ctx, String company_image) {
            ImageView image = mView.findViewById(R.id.imageCompany);
            Picasso.with(ctx).load(company_image).fit().centerCrop().into(image);
        }
        public void setCompany_name(String company_name) {
            TextView textView = mView.findViewById(R.id.edtName);
            textView.setText(company_name);
        }
        public void  setCompany_ruc(String company_ruc) {
            TextView textView = mView.findViewById(R.id.edtRuc);
            textView.setText(company_ruc);
        }
        public void  setCompany_verification(String company_verification) {
            my_company_verification = company_verification;
            TextView textView = mView.findViewById(R.id.edpCompanyVerification);
            if (company_verification.equals("true"))
            {
                verification_process_image.setImageResource(R.drawable.transaction_completed);
                textView.setText("Verificado");
            }
            else if (company_verification.equals("false"))
            {
                verification_process_image.setImageResource(R.drawable.error_icon);
                textView.setText("Denegado");
            }
            else if (company_verification.equals("progress"))
            {
                verification_process_image.setImageResource(R.drawable.transaction_in_progress);
                textView.setText("En Proceso");
            }

        }
        public void setCredit_score(String credit_score) {
            my_credit_score = credit_score;
        }
    }
}
