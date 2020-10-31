package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.RegisterInvestmentInFundActivity;

public class RegisterDepositActivity extends AppCompatActivity {

    String deposit_currency,account_currency, deposit_ammount,currentUserID,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy,my_pin,saveCurrentDate,
            saveCurrentTime,operationRandomName,real_deposit,real_ammount, deposit_image,postKey,email,name;
    private DatabaseReference userRef, ratesRef,bankAccounts, imageResourses,myOperationsRef;
    private FirebaseAuth mAuth;
    TextView txtMyAccount,txtAmmountToDeposit,txtCurrencyRate,txtAmmountIDeposit;
    double my_ammount,currrency_buy_rate,currrency_sell_rate;
    CheckBox cbAgreement;
    Button btnDeposit;
    RelativeLayout rootLayout;
    RecyclerView recyclerView;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_deposit);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        bankAccounts = FirebaseDatabase.getInstance().getReference().child("Bank Accounts");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");

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
        loadingBar = new ProgressDialog(this);


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
                    email = dataSnapshot.child("email").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

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
                viewHolder.setImage(model.getImage());
                viewHolder.setCurrrency(model.getCurrrency());
                viewHolder.setName(model.getName());

                Picasso.with(RegisterDepositActivity.this).load(viewHolder.bank_image).fit().into(viewHolder.financial_institution_image);

                viewHolder.btnCcCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCcCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(RegisterDepositActivity.this, "Nª de Centa Bancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.btnCciCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCciCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(RegisterDepositActivity.this, "Nª de Centa Interbancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void pinDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("PIN de Seguridad");
        dialog.setIcon(R.drawable.pin_icon);
        dialog.setMessage("Ingresa tu PIN de seguridad");

        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pin_dialog_layout,null);

        final EditText edtPin = pin_dialog.findViewById(R.id.edtPin);
        Button btnConfirmDeposit = pin_dialog.findViewById(R.id.btnConfirmDeposit);
        final RelativeLayout newRootLayout = pin_dialog.findViewById(R.id.newRootLayout);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
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
                    dialog.dismiss();
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

        loadingBar.setTitle("Registrando depósito...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Depósito a mi cuenta");
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

                sendMail();
                Intent intent = new Intent(RegisterDepositActivity.this,DepositTransactionInProgressActivity.class);
                intent.putExtra("deposit_currency",deposit_currency);
                intent.putExtra("TransactionCode",operationRandomName+"DP");
                loadingBar.dismiss();
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendMail() {

        String subject = name+", Tienes un Depósito pendiente en Oliver Bank";
        String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "<head>\n" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
                "<meta content=\"width=device-width\" name=\"viewport\"/>\n" +
                "<meta content=\"IE=edge\" http-equiv=\"X-UA-Compatible\"/>\n" +
                "<title></title>\n" +
                "<style type=\"text/css\">\n" +
                "\t\tbody {\n" +
                "\t\t\tmargin: 0;\n" +
                "\t\t\tpadding: 0;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable,\n" +
                "\t\ttd,\n" +
                "\t\ttr {\n" +
                "\t\t\tvertical-align: top;\n" +
                "\t\t\tborder-collapse: collapse;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t* {\n" +
                "\t\t\tline-height: inherit;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ta[x-apple-data-detectors=true] {\n" +
                "\t\t\tcolor: inherit !important;\n" +
                "\t\t\ttext-decoration: none !important;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "<style id=\"media-query\" type=\"text/css\">\n" +
                "\t\t@media (max-width: 520px) {\n" +
                "\n" +
                "\t\t\t.block-grid,\n" +
                "\t\t\t.col {\n" +
                "\t\t\t\tmin-width: 320px !important;\n" +
                "\t\t\t\tmax-width: 100% !important;\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.block-grid {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.col {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.col>div {\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\timg.fullwidth,\n" +
                "\t\t\timg.fullwidthOnMobile {\n" +
                "\t\t\t\tmax-width: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col {\n" +
                "\t\t\t\tmin-width: 0 !important;\n" +
                "\t\t\t\tdisplay: table-cell !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack.two-up .col {\n" +
                "\t\t\t\twidth: 50% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num4 {\n" +
                "\t\t\t\twidth: 33% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num8 {\n" +
                "\t\t\t\twidth: 66% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num4 {\n" +
                "\t\t\t\twidth: 33% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num3 {\n" +
                "\t\t\t\twidth: 25% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num6 {\n" +
                "\t\t\t\twidth: 50% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num9 {\n" +
                "\t\t\t\twidth: 75% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.video-block {\n" +
                "\t\t\t\tmax-width: none !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mobile_hide {\n" +
                "\t\t\t\tmin-height: 0px;\n" +
                "\t\t\t\tmax-height: 0px;\n" +
                "\t\t\t\tmax-width: 0px;\n" +
                "\t\t\t\tdisplay: none;\n" +
                "\t\t\t\toverflow: hidden;\n" +
                "\t\t\t\tfont-size: 0px;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.desktop_hide {\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t\tmax-height: none !important;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #FFFFFF;\">\n" +
                "<table bgcolor=\"#FFFFFF\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; Margin: 0 auto; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #FFFFFF; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/Oliver-Bank-Tittle-Logo.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 250px; display: block;\" title=\"Alternate text\" width=\"250\"/>\n" +
                "</div>\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
                "<div style=\"font-size:1px;line-height:15px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/Constancia-de-Transferencia-1.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 500px; display: block;\" title=\"Alternate text\" width=\"500\"/>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; word-break: break-word; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Monto y Moneda a Depositar:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+real_ammount+" "+account_currency+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Fecha:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+saveCurrentDate+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Hora:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+saveCurrentTime+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Código de operación:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+operationRandomName+"DP"+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 2px solid #0068A5; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 30px;padding-left: 30px;\">\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 30px;padding-left: 30px;\" align=\"center\"><![endif]-->\n" +
                "<div style=\"font-size:1px;line-height:45px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/transaction_in_progress.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 65px; display: block;\" title=\"Alternate text\" width=\"65\"/>\n" +
                "<div style=\"font-size:1px;line-height:45px\"> </div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:20px;padding-right:15px;padding-bottom:15px;padding-left:15px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; font-size: 12px; mso-line-height-alt: 24px; margin: 0;\"><span style=\"font-size: 12px;\"><span style=\"color: #000080;\">Pasos a seguir:</span></span></p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; font-size: 12px; mso-line-height-alt: 24px; margin: 0;\"><span style=\"font-size: 12px;\"><span style=\"color: #000080;\">1. Deposita o transfiere a una de las cuentas de Oliver Bank</span></span></p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; font-size: 12px; mso-line-height-alt: 24px; margin: 0;\"><span style=\"font-size: 12px;\"><span style=\"color: #000080;\">2. Suba una foto o captura del comprobante desde \"Mis Operaciones\"</span></span></p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; font-size: 12px; mso-line-height-alt: 24px; margin: 0;\"><span style=\"font-size: 12px;\"><span style=\"color: #000080;\">3. Espera a la confirmación de tu depósito</span></span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; text-align: center; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">¿Dificultades para hacer un depósito?</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<div align=\"center\" class=\"button-container\" style=\"padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
                "<a href=\"https://oliver-bank.com/depositos/\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #0068a5; border-radius: 4px; -webkit-border-radius: 4px; -moz-border-radius: 4px; width: auto; width: auto; border-top: 1px solid #0068a5; border-right: 1px solid #0068a5; border-bottom: 1px solid #0068a5; border-left: 1px solid #0068a5; padding-top: 5px; padding-bottom: 5px; font-family: Arial, Helvetica Neue, Helvetica, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\" target=\"_blank\"><span style=\"padding-left:20px;padding-right:20px;font-size:16px;display:inline-block;\"><span style=\"font-size: 16px; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\">Consultar guía rápida</span></span></a>\n" +
                "<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 10px; line-height: 2; text-align: center; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; word-break: break-word; mso-line-height-alt: 20px; margin: 0;\"><span style=\"font-size: 10px;\">Oliver Bank, Lima Perú 2020</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (IE)]></div><![endif]-->\n" +
                "</body>\n" +
                "</html>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,subject,message);
        javaMailAPI.execute();

    }

    private void registerMyDepositTransaction() {

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageButton btnCcCode,btnCciCode;
        TextView txtCcCode,txtCciCode;
        CircleImageView financial_institution_image;
        String bank_image;


        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            btnCcCode = mView.findViewById(R.id.btnCcCode);
            btnCciCode = mView.findViewById(R.id.btnCciCode);
            txtCcCode = mView.findViewById(R.id.txtCcCode);
            txtCciCode = mView.findViewById(R.id.txtCciCode);
            financial_institution_image = mView.findViewById(R.id.financialInstitutionImage);

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
        public void setImage(String image)
        {
            bank_image = image;
        }
        public void setName(String name) {
            TextView textView = mView.findViewById(R.id.txtName);
            textView.setText(name);
        }
    }
}
