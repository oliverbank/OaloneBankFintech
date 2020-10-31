package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;

public class DepositTransactionInProgressActivity extends AppCompatActivity {

    
    private DatabaseReference bankAccounts;
    String postKey,transactionCode,deposit_currency;
    Button btGoToHome,btnMyOperations;
    TextView txtTransactionCode; 
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_transaction_in_progress);

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
                Intent intent = new Intent(DepositTransactionInProgressActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DepositTransactionInProgressActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void displayOliverBankAccounts() {
        Query query = bankAccounts.orderByChild("currrency").startAt(deposit_currency).endAt(deposit_currency+"\uf8ff");
        FirebaseRecyclerAdapter<OliverBankAccountsModel, RegisterDepositActivity.UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OliverBankAccountsModel, RegisterDepositActivity.UsersViewHolder>
                (OliverBankAccountsModel.class, R.layout.oliverbank_accounts_layout, RegisterDepositActivity.UsersViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final RegisterDepositActivity.UsersViewHolder viewHolder, OliverBankAccountsModel model, int position) {
                viewHolder.setCc_code(model.getCc_code());
                viewHolder.setCci_code(model.getCci_code());
                viewHolder.setCurrrency(model.getCurrrency());
                viewHolder.setName(model.getName());
                viewHolder.setImage( model.getImage());

                Picasso.with(DepositTransactionInProgressActivity.this).load(viewHolder.bank_image).fit().into(viewHolder.financial_institution_image);

                viewHolder.btnCcCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("TextView",viewHolder.txtCcCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(DepositTransactionInProgressActivity.this, "Nª de Cuenta Bancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.btnCciCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCciCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(DepositTransactionInProgressActivity.this, "Nª de Cuenta Interbancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
