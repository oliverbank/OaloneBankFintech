package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositTransactionInProgressActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.OliverBankAccountsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterDepositActivity;

public class RegisterMyCompanyDepositActivity extends AppCompatActivity {

    String my_company_key,thRealCurrentId;
    String deposit_currency,account_currency, deposit_ammount,currentUserID,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy,my_pin,saveCurrentDate,
            saveCurrentTime,operationRandomName,real_deposit,real_ammount, deposit_image,postKey;
    private DatabaseReference userRef, ratesRef,bankAccounts, imageResourses,myOperationsRef,theRealuserRef;
    private FirebaseAuth mAuth;
    TextView txtMyAccount,txtAmmountToDeposit,txtCurrencyRate,txtAmmountIDeposit;
    double my_ammount,currrency_buy_rate,currrency_sell_rate;
    CheckBox cbAgreement;
    Button btnDeposit;
    RelativeLayout rootLayout;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_my_company_deposit);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = my_company_key;
        thRealCurrentId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        theRealuserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        bankAccounts = FirebaseDatabase.getInstance().getReference().child("Bank Accounts");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");

        account_currency = getIntent().getExtras().get("my_account_currency").toString();
        deposit_currency = getIntent().getExtras().get("deposit_currency").toString();
        deposit_ammount = getIntent().getExtras().get("ammount_for_deposit").toString();

        txtMyAccount = findViewById(R.id.txtMyAccount);
        txtAmmountToDeposit = findViewById(R.id.txtAmmountToDeposit);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        txtAmmountIDeposit = findViewById(R.id.txtAmmountIDeposit);
        cbAgreement = findViewById(R.id.cbAgreement);
        btnDeposit = findViewById(R.id.btnDeposit);
        rootLayout = findViewById(R.id.rootLayout);
        recyclerView = findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        displayOliverBankAccounts();

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                    if (account_currency.equals("PEN"))
                    {
                        txtMyAccount.setText("Cuenta básica (Soles - PEN): S/ "+basic_account_pen);
                    }
                    if (account_currency.equals("USD"))
                    {
                        txtMyAccount.setText("Cuenta básica (Dólares - USD): $ "+basic_account_usd);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();

                    currrency_buy_rate = Double.parseDouble(currency_rate_buy);
                    currrency_sell_rate = Double.parseDouble(currency_rate_sell);


                    txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");

                    double ammount_to_deposit = Double.parseDouble(deposit_ammount);
                    if (account_currency.equals("PEN") && deposit_currency.equals("USD"))
                    {
                        my_ammount = ammount_to_deposit*currrency_buy_rate;
                    }
                    if (account_currency.equals("USD") && deposit_currency.equals("PEN"))
                    {
                        my_ammount = ammount_to_deposit/currrency_sell_rate;
                    }
                    if (account_currency.equals("PEN") && deposit_currency.equals("PEN"))
                    {
                        my_ammount = Double.parseDouble(deposit_ammount);
                    }
                    if (account_currency.equals("USD") && deposit_currency.equals("USD"))
                    {
                        my_ammount = Double.parseDouble(deposit_ammount);
                    }


                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    real_ammount = decimalFormat.format(my_ammount);

                    txtAmmountToDeposit.setText(real_ammount+" "+account_currency);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageResourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    deposit_image = dataSnapshot.child("deposit_image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        double my_real_deposit = Double.parseDouble(deposit_ammount);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        real_deposit = decimalFormat.format(my_real_deposit);
        txtAmmountIDeposit.setText(real_deposit+" "+deposit_currency);


        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbAgreement.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes comprometerte a realizar el depósito...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!cbAgreement.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes comprometerte a realizar el depósito...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    pinDialog();
                }
            }
        });
    }

    private void displayOliverBankAccounts() {
        Query query = bankAccounts.orderByChild("currrency").startAt(deposit_currency).endAt(deposit_currency+"\uf8ff");
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
                        Toast.makeText(RegisterMyCompanyDepositActivity.this, "Nª de Centa Bancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.btnCciCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCciCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(RegisterMyCompanyDepositActivity.this, "Nª de Centa Interbancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void pinDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("PIN de Seguridad");
        dialog.setIcon(R.drawable.pin_icon);
        dialog.setMessage("Ingresa tu PIN de seguridad");

        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pin_dialog_layout,null);

        final EditText edtPin = pin_dialog.findViewById(R.id.edtPin);
        Button btnConfirmDeposit = pin_dialog.findViewById(R.id.btnConfirmDeposit);
        final RelativeLayout newRootLayout = pin_dialog.findViewById(R.id.newRootLayout);

        theRealuserRef.child(thRealCurrentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    my_pin = dataSnapshot.child("pin").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnConfirmDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPin.getText().toString()))
                {
                    Snackbar.make(newRootLayout, "Debes ingresar tu PIN de seguridad...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!edtPin.getText().toString().equals(my_pin))
                {
                    Snackbar.make(newRootLayout, "PIN INCORRECTO", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    //Toast.makeText(RegisterDepositActivity.this, "EL DINERO ES DINERO EQUIS DE DE DE", Toast.LENGTH_LONG).show();
                    registerMyDepositTransaction();
                    registerMyOperation();
                    finish();
                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }

    private void registerMyOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Deppósito en mi cuenta");
        myOperationMap.put("operation_type_code","DP");
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        //1 -> user need to upload the bill
        //2 -> the bill is being verificated
        //3 -> the ammount has been deposited
        //4 -> there is a notification about the ammount
        //5 -> bill is not valid
        myOperationMap.put("deposit_state","1");
        myOperationMap.put("deposit_ammount",real_deposit);
        myOperationMap.put("deposit_currency",deposit_currency);
        myOperationMap.put("deposit_real_ammount",real_ammount);
        myOperationMap.put("deposit_real_currency",account_currency);
        myOperationMap.put("uid",currentUserID);
        myOperationMap.put("operation_image",deposit_image);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");

        myOperationMap.put("transfer_user_origin","");
        myOperationMap.put("transfer_user_destination","");
        myOperationMap.put("sent_ammount","");
        myOperationMap.put("sent_currency","");
        myOperationMap.put("recieved_ammount","");
        myOperationMap.put("recieved_currency","");
        //Direct Investments
        myOperationMap.put("company_finance_name","");
        myOperationMap.put("finance_ammount","");
        myOperationMap.put("finance_currency","");
        //Credit Line
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent = new Intent(RegisterMyCompanyDepositActivity.this, MyCompanyDepositTransactionInProgressActivity.class);
                intent.putExtra("deposit_currency",deposit_currency);
                intent.putExtra("TransactionCode",operationRandomName+"DP");
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerMyDepositTransaction() {

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
