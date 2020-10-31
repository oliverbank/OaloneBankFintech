package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvestmentFundDetailActivity;

public class RegisterWithdrawalActivity extends AppCompatActivity {

    TextView txtTittle,txtBankAccountInformation,txtCurrencyRate,txtOliverBankWithdrawl,txtOtherBankDeposit;
    EditText edtWithDrawAmmount;
    RadioButton rdPenAccount,rdUsdAccount;
    Button btnWithdrawal;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef, usersBankAccounts,imageResourses,myOperationsRef;
    String currentUserID,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy, account_currency, deposit_ammount_currency, min_withdrawal,postKey,financial_institution,
            bank_account,interbbanking_account,bank_account_currency,saveCurrentDate,saveCurrentTime,operationRandomName,withdrawal_image;
    double wd_ammount,wd_pen_ammount,wd_usd_ammount, money_in_pen,money_in_usd,buy_currency_rate,sell_currency_rate,min_withdrawal_db;
    String wd_ammount_st,wd_pen_ammount_st,wd_usd_ammount_st,name,email,withdrawal_amount_df;
    RelativeLayout rootLayout;
    DecimalFormat decimalFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_withdrawal);

        txtTittle= findViewById(R.id.txtTittle);
        txtTittle.setSelected(true);
        txtBankAccountInformation= findViewById(R.id.txtBankAccountInformation);
        edtWithDrawAmmount = findViewById(R.id.edtWithDrawAmmount);
        rdPenAccount= findViewById(R.id.rdPenAccount);
        rdUsdAccount= findViewById(R.id.rdUsdAccount);
        txtCurrencyRate= findViewById(R.id.txtCurrencyRate);
        txtOliverBankWithdrawl = findViewById(R.id.txtOliverBankWithdrawl);
        txtOtherBankDeposit = findViewById(R.id.txtOtherBankDeposit);
        btnWithdrawal= findViewById(R.id.btnWithdrawal);
        postKey = getIntent().getExtras().get("PostKey").toString();
        rootLayout = findViewById(R.id.rootLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        usersBankAccounts = FirebaseDatabase.getInstance().getReference().child("Users Bank Accounts").child(postKey);
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");

        decimalFormat = new DecimalFormat("0.00");

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
                    email = dataSnapshot.child("email").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();

                    rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+basic_account_pen);
                    rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+basic_account_usd);

                    ratesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                                currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                                min_withdrawal= dataSnapshot.child("min_withdrawal").getValue().toString();

                                min_withdrawal_db = Double.parseDouble(min_withdrawal);
                                min_withdrawal = decimalFormat.format(min_withdrawal_db);

                                txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");

                                imageResourses.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            withdrawal_image = dataSnapshot.child("withdrawal_image").getValue().toString();
                                        }
                                        usersBankAccounts.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                financial_institution= dataSnapshot.child("financial_institution").getValue().toString();
                                                bank_account= dataSnapshot.child("bank_account").getValue().toString();
                                                interbbanking_account= dataSnapshot.child("interbbanking_account").getValue().toString();
                                                bank_account_currency= dataSnapshot.child("account_currency").getValue().toString();

                                                txtBankAccountInformation.setText(financial_institution+": "+bank_account+" ("+bank_account_currency+")");
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtWithDrawAmmount.getText().toString()))
                {
                    rdPenAccount.setChecked(false);
                    edtWithDrawAmmount.setBackground(getDrawable(R.drawable.button3_background));
                    edtWithDrawAmmount.setTextColor(Color.WHITE);
                    Toast.makeText(RegisterWithdrawalActivity.this, "INGRESA EL MONTO A RETIRAR PRIMERO", Toast.LENGTH_SHORT).show();
                }
                wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
                money_in_pen = Double.parseDouble(basic_account_pen);
                if (wd_ammount < min_withdrawal_db) {
                    Snackbar.make(rootLayout, "El monto mínimo de retiro es de "+min_withdrawal+" unidades monetarias", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (wd_ammount > money_in_pen)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (wd_ammount > money_in_pen)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    account_currency = "PEN";
                    edtWithDrawAmmount.setEnabled(false);
                    getTransactionDetails();
                }
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtWithDrawAmmount.getText().toString()))
                {
                    rdUsdAccount.setChecked(false);
                    edtWithDrawAmmount.setBackground(getDrawable(R.drawable.button3_background));
                    edtWithDrawAmmount.setTextColor(Color.WHITE);
                    Snackbar.make(rootLayout, "Ingresa el monto a retirar primero", Snackbar.LENGTH_LONG).show();
                    return;
                }
                wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
                money_in_usd = Double.parseDouble(basic_account_usd);
                if (wd_ammount < min_withdrawal_db) {
                    Snackbar.make(rootLayout, "El monto mínimo de retiro es de "+min_withdrawal+" unidades monetarias", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (wd_ammount > money_in_usd)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (wd_ammount > money_in_usd)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    account_currency = "USD";
                    edtWithDrawAmmount.setEnabled(false);
                    getTransactionDetails();
                }
            }
        });
        
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtWithDrawAmmount.getText().toString()))
                {
                    rdPenAccount.setChecked(false);
                    rdUsdAccount.setChecked(false);
                    edtWithDrawAmmount.setBackground(getDrawable(R.drawable.button3_background));
                    edtWithDrawAmmount.setTextColor(Color.WHITE);
                    Toast.makeText(RegisterWithdrawalActivity.this, "INGRESA EL MONTO A RETIRAR PRIMERO", Toast.LENGTH_SHORT).show();
                }
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una cuenta primero", Snackbar.LENGTH_LONG).show();
                    return;
                }
                wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
                money_in_usd = Double.parseDouble(basic_account_usd);
                if (rdPenAccount.isChecked())
                {
                    if (wd_ammount > money_in_pen)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (wd_ammount > money_in_pen)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        registerWithdrawal();
                        registerOperation();
                    }
                }
                if (rdUsdAccount.isChecked())
                {
                    if (wd_ammount > money_in_usd)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (wd_ammount > money_in_usd)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        registerWithdrawal();
                        registerOperation();
                    }
                }
            }
        });
    }

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Retiro a otro Banco");
        myOperationMap.put("operation_type_code","WD");
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        //1 -> user need to upload the bill
        //2 -> the bill is being verificated
        //3 -> the ammount has been deposited
        //4 -> there is a notification about the ammount
        //5 -> bill is not valid
        myOperationMap.put("deposit_state","1");
        myOperationMap.put("deposit_ammount","");
        myOperationMap.put("deposit_currency","");
        myOperationMap.put("deposit_real_ammount","");
        myOperationMap.put("deposit_real_currency","");
        myOperationMap.put("uid",currentUserID);
        myOperationMap.put("operation_image",withdrawal_image);
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
        //Withdrawal
        //1 -> In Proccess
        //2 -> Error in the account number
        //3 -> Withdrawal Completed Successfully
        withdrawal_amount_df = decimalFormat.format(wd_ammount);
        myOperationMap.put("withdrawal_state","1");
        myOperationMap.put("withdrawal_cc_code",bank_account);
        myOperationMap.put("withdrawal_cci_code",interbbanking_account);
        myOperationMap.put("withdrawal_financial_institution",financial_institution);
        myOperationMap.put("withdrawal_financial_institution_account_currency",bank_account_currency);
        myOperationMap.put("withdrawal_ammount",withdrawal_amount_df);
        myOperationMap.put("withdrawal_ammount_currency",account_currency);
        myOperationMap.put("other_bank_ammount_currency",bank_account_currency);
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                sendEmail();
                Intent intent = new Intent(RegisterWithdrawalActivity.this,WithdrawalTransactionInProgressActivity.class);
                intent.putExtra("deposit_currency","");
                intent.putExtra("TransactionCode",operationRandomName+"WD");
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendEmail() {
        String subject = name+", Sobre tu Retiro en Proceso en Oliver Bank";
        String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "<head>\n" +
                "<!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]-->\n" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
                "<meta content=\"width=device-width\" name=\"viewport\"/>\n" +
                "<!--[if !mso]><!-->\n" +
                "<meta content=\"IE=edge\" http-equiv=\"X-UA-Compatible\"/>\n" +
                "<!--<![endif]-->\n" +
                "<title></title>\n" +
                "<!--[if !mso]><!-->\n" +
                "<!--<![endif]-->\n" +
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
                "<!--[if IE]><div class=\"ie-browser\"><![endif]-->\n" +
                "<table bgcolor=\"#FFFFFF\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; Margin: 0 auto; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #FFFFFF; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n" +
                "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color:#FFFFFF\"><![endif]-->\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 20px;padding-left: 20px;\" align=\"center\"><![endif]-->\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/Oliver-Bank-Tittle-Logo.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 250px; display: block;\" title=\"Alternate text\" width=\"250\"/>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]-->\n" +
                "<div style=\"font-size:1px;line-height:15px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/Constancia-de-Transferencia-2.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 500px; display: block;\" title=\"Alternate text\" width=\"500\"/>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "</div>\n" +
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
                "<p style=\"font-size: 14px; line-height: 2; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; word-break: break-word; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Monto y Moneda Retirado:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "\n" +
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
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+withdrawal_amount_df+" "+account_currency+"</span></p>\n" +
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
                "\n" +
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
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Destino:</span></p>\n" +
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
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+"CC:"+bank_account+" "+financial_institution+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+saveCurrentDate+"</span></p>\n" +
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
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
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
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Hora:</span></p>\n" +
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
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+saveCurrentTime+"</span></p>\n" +
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
                "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
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
                "<!--<![endif]-->\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top: 0px; padding-bottom: 0px; font-family: 'Trebuchet MS', Tahoma, sans-serif\"><![endif]-->\n" +
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
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+operationRandomName+"WD</span></p>\n" +
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
                "<div style=\"font-size:1px;line-height:10px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/transaction_in_progress.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 65px; display: block;\" title=\"Alternate text\" width=\"65\"/>\n" +
                "<div style=\"font-size:1px;line-height:15px\"> </div>\n" +
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
                "<p style=\"text-align: left; line-height: 2; word-break: break-word; font-size: 14px; mso-line-height-alt: 28px; margin: 0;\"><span style=\"font-size: 14px;\"><span style=\"color: #000080;\">Estamos procesando tu retiro, revisa el estado de tu retiro desde \"Mis Operaciones\".</span></span></p>\n" +
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
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 10px; line-height: 2; text-align: center; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; word-break: break-word; mso-line-height-alt: 20px; margin: 0;\"><span style=\"font-size: 10px;\">Oliver Bank, Lima Perú 2020</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
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


    private void registerWithdrawal() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double ammount_to_withdrawal = Double.parseDouble(edtWithDrawAmmount.getText().toString());
        double money_in_my_account;
        String money_in_my_account_st = "";
        if (account_currency.equals("PEN"))
        {
            double money_in_pen = Double.parseDouble(basic_account_pen);
            money_in_my_account = money_in_pen-ammount_to_withdrawal;
            money_in_my_account_st = decimalFormat.format(money_in_my_account);

            HashMap userMap = new HashMap();
            userMap.put("basic_account_pen",money_in_my_account_st);
            userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                }
            });
        }
        if (account_currency.equals("USD"))
        {
            double money_in_usd = Double.parseDouble(basic_account_usd);
            money_in_my_account = money_in_usd-ammount_to_withdrawal;
            money_in_my_account_st = decimalFormat.format(money_in_my_account);

            HashMap userMap = new HashMap();
            userMap.put("basic_account_usd",money_in_my_account_st);
            userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                }
            });
        }

    }

    private void getTransactionDetails() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        buy_currency_rate = Double.parseDouble(currency_rate_buy);
        sell_currency_rate = Double.parseDouble(currency_rate_sell);
        if (account_currency.equals("PEN") && bank_account_currency.equals("PEN"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = wd_ammount;
            wd_usd_ammount = 0.00;

            wd_pen_ammount_st = decimalFormat.format(wd_pen_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_pen_ammount+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_pen_ammount_st+" "+bank_account_currency);
        }
        if (account_currency.equals("USD") && bank_account_currency.equals("USD"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = 0.00;
            wd_usd_ammount = wd_ammount;

            wd_usd_ammount_st = decimalFormat.format(wd_usd_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_usd_ammount+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_usd_ammount_st+" "+bank_account_currency);
        }
        if (account_currency.equals("PEN") && bank_account_currency.equals("USD"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = wd_ammount;
            wd_usd_ammount = wd_ammount/sell_currency_rate;

            wd_pen_ammount_st = decimalFormat.format(wd_pen_ammount);
            wd_usd_ammount_st = decimalFormat.format(wd_usd_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_pen_ammount_st+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_usd_ammount_st+" "+bank_account_currency);
        }
        if (account_currency.equals("USD") && bank_account_currency.equals("PEN"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = wd_ammount*buy_currency_rate;
            wd_usd_ammount = wd_ammount;

            wd_pen_ammount_st = decimalFormat.format(wd_pen_ammount);
            wd_usd_ammount_st = decimalFormat.format(wd_usd_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_usd_ammount_st+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_pen_ammount_st+" "+bank_account_currency);
        }
    }
}
