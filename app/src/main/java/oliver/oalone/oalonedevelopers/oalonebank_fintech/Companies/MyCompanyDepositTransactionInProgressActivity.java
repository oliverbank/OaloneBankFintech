package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositTransactionInProgressActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.OliverBankAccountsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterDepositActivity;

public class MyCompanyDepositTransactionInProgressActivity extends AppCompatActivity {

    String my_company_key;
    private DatabaseReference bankAccounts;
    String postKey,transactionCode,deposit_currency;
    Button btGoToHome,btnMyOperations;
    TextView txtTransactionCode;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_deposit_transaction_in_progress);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        bankAccounts = FirebaseDatabase.getInstance().getReference().child("Bank Accounts");

        deposit_currency = getIntent().getExtras().get("deposit_currency").toString();
        //postKey = getIntent().getExtras().get("PostKey").toString();
        transactionCode = getIntent().getExtras().get("TransactionCode").toString();

        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);
        txtTransactionCode = findViewById(R.id.txtTransactionCode);

        recyclerView = findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        txtTransactionCode.setText("Código de transacción: "+transactionCode);

        displayOliverBankAccounts();

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyDepositTransactionInProgressActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyDepositTransactionInProgressActivity.this, MyCompanyOperationsActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayOliverBankAccounts() {
        Query query = bankAccounts.orderByChild("currency");
        FirebaseRecyclerAdapter<OliverBankAccountsModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OliverBankAccountsModel, UsersViewHolder>
                (OliverBankAccountsModel.class, R.layout.oliverbank_accounts_layout, UsersViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, OliverBankAccountsModel model, int position) {
                viewHolder.setCc_code(model.getCc_code());
                viewHolder.setCci_code(model.getCci_code());
                viewHolder.setCurrrency(model.getCurrrency());
                viewHolder.setName(model.getName());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.btnCcCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("TextView",viewHolder.txtCcCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MyCompanyDepositTransactionInProgressActivity.this, "Nª de Centa Bancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.btnCciCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCciCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MyCompanyDepositTransactionInProgressActivity.this, "Nª de Centa Interbancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
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


        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            btnCcCode = mView.findViewById(R.id.btnCcCode);
            btnCciCode = mView.findViewById(R.id.btnCciCode);
            txtCcCode = mView.findViewById(R.id.txtCcCode);
            txtCciCode = mView.findViewById(R.id.txtCciCode);

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
        public void setImage(Context ctx, String image)
        {
            CircleImageView financial_institution_image = mView.findViewById(R.id.financialInstitutionImage);
            Picasso.with(ctx).load(image).fit().into(financial_institution_image);
        }
        public void setName(String name) {
            TextView textView = mView.findViewById(R.id.txtName);
            textView.setText(name);
        }
    }
}
