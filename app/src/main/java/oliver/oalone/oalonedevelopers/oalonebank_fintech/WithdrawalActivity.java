package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.AddCompanyActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.MyCompaniesActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.MyCompaniesModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvestmentFundDetailActivity;

public class WithdrawalActivity extends AppCompatActivity {

    Button btnAddBankAccount,btnWithdrawalWithoutCard;
    private DatabaseReference userRef, postRef;
    private FirebaseAuth mAuth;
    String currentUserID,account_currency;
    RelativeLayout rootLayout;
    private RecyclerView myPostList;

    ArrayList<String> banks =new ArrayList<>();
    SpinnerDialog spinnerBanks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Users Bank Accounts");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        btnAddBankAccount = findViewById(R.id.btnAddBankAccount);
        btnWithdrawalWithoutCard = findViewById(R.id.btnWithdrawalWithoutCard);

        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);

        displayMyBankAccounts();

        btnAddBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBankAccountDialog();
            }
        });

        btnWithdrawalWithoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WithdrawalActivity.this, WithdrawalWithoutCardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayMyBankAccounts() {
        Query myPostQuery = postRef.orderByChild("uid").startAt(currentUserID).endAt(currentUserID+"\uf8ff");
        FirebaseRecyclerAdapter<BankAccountsModel, WithdrawalActivity.myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BankAccountsModel, WithdrawalActivity.myPostViewHolder>
                (BankAccountsModel.class, R.layout.my_bank_acount_item, WithdrawalActivity.myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(WithdrawalActivity.myPostViewHolder viewHolder, BankAccountsModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setFinancial_institution(model. getFinancial_institution());
                viewHolder.setBank_account(model.getBank_account());
                viewHolder.setInterbbanking_account(model.getInterbbanking_account());
                viewHolder.setAccount_currency(model.getAccount_currency());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(WithdrawalActivity.this, RegisterWithdrawalActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });

            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public myPostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }
        public void setFinancial_institution(String financial_institution) {
            TextView textView = mView.findViewById(R.id.txtBankName);
            textView.setText(financial_institution);
        }
        public void setBank_account(String bank_account) {
            TextView textView = mView.findViewById(R.id.txtCc);
            textView.setText("CC: "+bank_account);
        }
        public void setInterbbanking_account(String interbbanking_account) {
            TextView textView = mView.findViewById(R.id.txtCci);
            textView.setText("CCI: "+interbbanking_account);
        }
        public void setAccount_currency(String account_currency) {
            TextView textView = mView.findViewById(R.id.txtCurrency);
            textView.setText("Moneda: "+account_currency);
        }
    }

    private void showAddBankAccountDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Agregar cuenta bancaria");

        LayoutInflater inflater = LayoutInflater.from(this);
        View add_bank_account = inflater.inflate(R.layout.add_bank_account_layout,null);

        final Button btnBank,btnAdd;
        final EditText edtAccountNumber,edtInterbankingAccount;
        final RadioButton rdCurrencyPen,rdCurrencyUsd;

        btnBank = add_bank_account.findViewById(R.id.btnBank);
        edtAccountNumber = add_bank_account.findViewById(R.id.edtAccountNumber);
        edtInterbankingAccount = add_bank_account.findViewById(R.id.edtInterbankingAccount);
        btnAdd = add_bank_account.findViewById(R.id.btnAdd);
        rdCurrencyPen = add_bank_account.findViewById(R.id.rdCurrencyPen);
        rdCurrencyUsd = add_bank_account.findViewById(R.id.rdCurrencyUsd);
        rootLayout = add_bank_account.findViewById(R.id.rootLayout);

        rdCurrencyPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_currency = "PEN";
            }
        });
        rdCurrencyUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_currency = "USD";
            }
        });


        banks.add("Interbank");banks.add("BCP");banks.add("BBVA");banks.add("ScotiaBank");
        banks.add("Banbif");banks.add("Caja Arequipa");

        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerBanks.showSpinerDialog();
            }
        });
        spinnerBanks = new SpinnerDialog(WithdrawalActivity.this,banks,"Selecciona la Institución Financiera");
        spinnerBanks.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnBank.setText(item);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(btnBank.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la institución financiera", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAccountNumber.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el número de cuenta bancario", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtInterbankingAccount.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el número de cuenta interbancario", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdCurrencyPen.isChecked() && !rdCurrencyUsd.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar la moneda de la cuenta", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                    String saveCurrentDate =currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    String saveCurrentTime =currentTime.format(calForTime.getTime());

                    String postRandomName = saveCurrentDate+saveCurrentTime;

                    HashMap hashMap = new HashMap();
                    hashMap.put("uid",currentUserID);
                    hashMap.put("financial_institution",btnBank.getText().toString());
                    hashMap.put("bank_account",edtAccountNumber.getText().toString());
                    hashMap.put("interbbanking_account",edtInterbankingAccount.getText().toString());
                    hashMap.put("account_currency",account_currency);
                    postRef.child(currentUserID+postRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(WithdrawalActivity.this, WithdrawalActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

            }
        });

        dialog.setView(add_bank_account);
        dialog.show();
    }

}
