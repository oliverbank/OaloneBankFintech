package oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.JavaMailAPI;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class RegisterInfo5Activity extends AppCompatActivity {

    EditText edtUsername,edtPin,edtConfirmPin;
    CheckBox checkDataTrue,checkDataManagement;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, contractRef;
    String currentUserID,name,email,fullname, contract;
    String saveCurrentDate,saveCurrentTime,postRandomName,phoneNumber;
    String userVerification = "progress", username_exist;
    RelativeLayout rootLayout;
    Button btnRegister;
    private StorageReference userProfileImageRef;
    private ProgressDialog loadingBar;
    String TAG = "GenerateQRCode",downloadUrlQr;
    ImageView qrCode;
    String edtValue;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    Uri uri;
    String permission = "";
    private static final int MY_STORAGE_REQUEST_CODE = 200;
    TextView txtTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info5);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
            }
        }

        edtUsername = findViewById(R.id.edtUsername);
        edtPin = findViewById(R.id.edtPin);
        edtConfirmPin = findViewById(R.id.edtConfirmPin);
        checkDataTrue = findViewById(R.id.checkDataTrue);
        checkDataManagement = findViewById(R.id.checkDataManagement);
        rootLayout = findViewById(R.id.rootLayout);
        btnRegister = findViewById(R.id.btnRegister);
        qrCode = findViewById(R.id.qrCode);
        txtTerms = findViewById(R.id.txtTerms);
        loadingBar = new ProgressDialog(this);
        username_exist = "false";

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        contractRef = FirebaseDatabase.getInstance().getReference().child("Contracts");
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        phoneNumber = mAuth.getCurrentUser().getPhoneNumber();

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    email = dataSnapshot.child("email").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    fullname = dataSnapshot.child("fullname").getValue().toString();


                    contractRef.child("Terms And Conditios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            contract = dataSnapshot.child("Oliver Bank Terms").getValue().toString();
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

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(edtUsername.getText().toString()))
                {
                    username_exist = "false";
                    Query query1 =  userRef.orderByChild("username").equalTo(edtUsername.getText().toString());
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot id : dataSnapshot.getChildren()) {
                                String user_id = id.getKey();
                                userRef.child(Objects.requireNonNull(user_id)).child("username").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(edtUsername.getText().toString())){
                                            if (dataSnapshot.exists()) {
                                                username_exist = "true";
                                                Snackbar.make(rootLayout, "El nombre de usuario "+edtUsername.getText().toString()+" ya existe, debes crear otro.", Snackbar.LENGTH_LONG).show();
                                                return;
                                            }
                                            if (dataSnapshot.exists()) {
                                                username_exist = "true";
                                                Snackbar.make(rootLayout, "El nombre de usuario "+edtUsername.getText().toString()+" ya existe, debes crear otro.", Snackbar.LENGTH_LONG).show();
                                                return;
                                            }

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
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtUsername.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar un nombre de usuario", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtPin.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar tu PIN de seguridad", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtConfirmPin.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes confirmar tu PIN de seguridad", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtPin.getText().toString().length() != 4)
                {
                    Snackbar.make(rootLayout, "Tu PIN debe ser de 4 dígitos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!edtPin.getText().toString().equals(edtConfirmPin.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Los PIN de seguridad no coinciden", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!checkDataTrue.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes confirmar que la información proporcionada es correcta y verdadera", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!checkDataManagement.isChecked()) {
                    Snackbar.make(rootLayout, "Debes confirmar que estás de acuerdo con las políticas, términos y condiciones", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!checkDataManagement.isChecked()) {
                    Snackbar.make(rootLayout, "Debes confirmar que estás de acuerdo con las políticas, términos y condiciones", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (username_exist.equals("true")) {
                    Snackbar.make(rootLayout, "El nombre de usuario "+edtUsername.getText().toString()+" ya existe, debes crear otro.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (permission.equals("false")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                        }
                    }
                    Snackbar.make(rootLayout, "Debes permitir el acceso a la memoria del teléfono para almacenar tu código QR de pagos", Snackbar.LENGTH_LONG).show();
                    return;

                }
                if (permission.equals("false")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                        }
                    }
                    Snackbar.make(rootLayout, "Debes permitir el acceso a la memoria del teléfono para almacenar tu código QR de pagos", Snackbar.LENGTH_LONG).show();
                    return;

                }else {

                    loadingBar.setTitle("Preparando todo...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                    saveCurrentDate = currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    saveCurrentTime = currentTime.format(calForTime.getTime());

                    sendEmail();
                    generateQrCode();
                    saveQrCode();
                    sendQrCodeToDatabase();

                }
            }
        });
    }

    private void showTermsDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.terms_and_conditions_dialog,null);

        TextView txtMyTerms = add_bank_account.findViewById(R.id.txtTerms);

        txtMyTerms.setText(contract);

        dialog.setView(add_bank_account);



        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void sendEmail() {
        String subject = name+", Bienvenido a Oliver Bank";
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
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/Oliver-Bank-Tittle-Logo.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 275px; display: block;\" title=\"Alternate text\" width=\"275\"/>\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div>\n" +
                "</div>\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
                "<img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/Bienvenido-a-Oliver-Bank.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 500px; display: block;\" title=\"Alternate text\" width=\"500\"/>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:10px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">¡Hola "+name+"!</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">Este mensaje es para darte la bienvenida y celebrar tus nuevas oportunidades en Oliver Bank, tu nuevo banco digital. A continuación conocerás todo lo que puedes hacer en Oliver Bank.</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:20px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/piggy-bank.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 125px; display: block;\" title=\"Alternate text\" width=\"125\"/>\n" +
                "<div style=\"font-size:1px;line-height:20px\"> </div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">Tus Cuentas:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">Ahora tienes 2 cuentas donde puedes ahorrar:</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">1. En soles peruanos</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">2. En Dólares americanos</p>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:25px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/payment.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 85px; display: block;\" title=\"Alternate text\" width=\"85\"/>\n" +
                "<div style=\"font-size:1px;line-height:25px\"> </div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">Tus Operaciones Frecuentes:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Depósitos a tus cuentas en Oliver Bank</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Retiros a otras instituciones financieras</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Retiros en cajero sin tarjeta o cuentas bancarias</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Transferencias a otros usuarios de Oliver Bank</p>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/qr_code_payment_icon.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 85px; display: block;\" title=\"Alternate text\" width=\"85\"/>\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">Divisas y Pagos:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Paga y Recibe Pagos con Código QR</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Compra dólalres y soles</p>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:15px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/credit_card_out_icon.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 85px; display: block;\" title=\"Alternate text\" width=\"85\"/>\n" +
                "<div style=\"font-size:1px;line-height:15px\"> </div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">Para Financiarse:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Líneas de crédito digitales</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Préstamos de Oliver Bank</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Préstamos de terceros</p>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:30px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/iconfinder_Money-Increase_379342.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 85px; display: block;\" title=\"Alternate text\" width=\"85\"/>\n" +
                "<div style=\"font-size:1px;line-height:25px\"> </div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">Para Invertir:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Fondos Mutuos de Inversión</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Crowdfunding de Empresas</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Factoring de Empresas</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\"> </p>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:30px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/store.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 85px; display: block;\" title=\"Alternate text\" width=\"85\"/>\n" +
                "<div style=\"font-size:1px;line-height:25px\"> </div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">Oliver Bank Negocios:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Cobra en tu establecimiento con Código QR</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Cuentas corrientes disponibles las 24 horas</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Financiate con Crowdfunding, Factoring y Líneas de crédito</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Emite facturas a otras empresas</p>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                "<div style=\"font-size:1px;line-height:30px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/tick.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 85px; display: block;\" title=\"Alternate text\" width=\"85\"/>\n" +
                "<div style=\"font-size:1px;line-height:25px\"> </div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 18px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 36px; margin: 0;\"><span style=\"color: #000080; font-size: 18px;\">Recomendaciones:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:10px;padding-bottom:0px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; color: #555555; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Verifica tu documento de identidad para acceder a todos los productos y beneficios de Oliver Bank</p>\n" +
                "<p style=\"line-height: 2; word-break: break-word; mso-line-height-alt: NaNpx; margin: 0;\">- Nunca compartas tu PIN de seguridad ni clave de autenticación SMS</p>\n" +
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
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 2px solid #1C336A; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
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
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 5px;padding-left: 5px;\">\n" +
                "<div style=\"font-size:1px;line-height:5px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/02/oliver_bank_main_icon.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 50px; display: block;\" title=\"Alternate text\" width=\"50\"/>\n" +
                "<div style=\"font-size:1px;line-height:5px\"> </div>\n" +
                "</div>\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 16px; line-height: 2; word-break: break-word; text-align: center; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 32px; margin: 0;\"><span style=\"font-size: 16px; color: #333333;\">Oliver Bank</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:-0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><sub><span style=\"color: #333333;\">Más información en: oliver-bank.com</span></sub></p>\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><sub><span style=\"color: #333333;\">¿Problemas? Escríbe un WhatsApp a: +51 947 625 082</span></sub></p>\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><sub><span style=\"color: #333333;\">Oliver Bank, Lima, Perú 2020</span></sub></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, email,subject,message);
        javaMailAPI.execute();
    }

    private void sendQrCodeToDatabase() {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(RegisterInfo5Activity.this.getContentResolver(), bitmap, "Title", null);
        uri = Uri.parse(path);

        StorageReference filePath = userProfileImageRef.child(uri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    downloadUrlQr = task.getResult().getDownloadUrl().toString();

                    HashMap userMap = new HashMap();
                    userMap.put("username",edtUsername.getText().toString().toLowerCase());
                    userMap.put("user_verification",userVerification);
                    userMap.put("pin",edtPin.getText().toString());
                    userMap.put("document_verification","none");
                    userMap.put("basic_account_pen","0.00");
                    userMap.put("basic_account_usd","0.00");
                    userMap.put("state","offline");
                    userMap.put("credit_line_pen","false");
                    userMap.put("credit_line_pen_available","0.00");
                    userMap.put("credit_line_pen_tcea","800.00");
                    userMap.put("credit_line_pen_total","0.00");
                    userMap.put("credit_line_pen_used","0.00");
                    userMap.put("credit_line_usd","false");
                    userMap.put("credit_line_usd_available","0.00");
                    userMap.put("credit_line_usd_tcea","800.00");
                    userMap.put("credit_line_usd_total","0.00");
                    userMap.put("credit_line_usd_used","0.00");
                    userMap.put("credit_risk_param","0.00");
                    userMap.put("credit_score","5");
                    userMap.put("credit_line_pen_request_state","false");
                    userMap.put("credit_line_usd_request_state","false");
                    userMap.put("qr_code_image",downloadUrlQr);
                    userMap.put("timestamp", ServerValue.TIMESTAMP);

                    userMap.put("my_company_number","0");

                    userMap.put("daily_claim_pen_account","false");
                    userMap.put("daily_claim_usd_account","false");
                    userMap.put("pen_accoount_is_enabled","true");
                    userMap.put("usd_accoount_is_enabled","true");

                    userMap.put("user_is_enabled","true");
                    userMap.put("pin_intents", 0);

                    userMap.put("phone_number",phoneNumber);

                    userMap.put("register_date",saveCurrentDate);
                    userMap.put("register_time",saveCurrentTime);

                    userMap.put("lending_notification","false");

                    userMap.put("app_identifier","olb");

                    userMap.put("main_activity","none");

                    userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterInfo5Activity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loadingBar.dismiss();
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterInfo5Activity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });

                }
            }
        });

    }

    private void saveQrCode() {
        boolean save;
        String result;
        try {
            save = QRGSaver.save(savePath, postRandomName.trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
            result = save ? "Image Saved" : "Image Not Saved";
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateQrCode() {
        inputValue = currentUserID;
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                qrCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission = "true";
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                permission = "false";
                Toast.makeText(this, "AL RECHAZAR LOS PERMISOS ALGUNAS FUNCIONES NO ESTARÁN DISPONIBLES", Toast.LENGTH_LONG).show();
            }
        }
    }
}
