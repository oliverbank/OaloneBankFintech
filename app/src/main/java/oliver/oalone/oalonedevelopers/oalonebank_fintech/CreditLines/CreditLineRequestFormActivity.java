package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DocumentVerificationActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class CreditLineRequestFormActivity extends AppCompatActivity {

    String credit_request_currency, credit_request_condition;
    ImageView imgCondition;
    TextView txtCondition,txtCreditScore;
    LinearLayout studentLayout,dependentWorkerLayout,independentWorkerLayout,lessorLayout,stockHolderLayout;
    ImageButton btnReceiptLightWater,btnSchoolRegister,btnSchoolReceipt,btnPaymentReceipt1,btnPaymentReceipt2,btnPaymentReceipt3,btnRentTax1,btnRentTax2,btnHonorPaymentReceipt1,
            btnHonorPaymentReceipt2,btnHonorPaymentReceipt3,btnSunatPaymentReceipt1,btnSunatPaymentReceipt2,btnSunatPaymentReceipt3,btnRentDeclaration1,btnRentDeclaration2,btnLeasingContract,
            btnProperty,btnSunatPayment1,btnSunatPayment2,btnDeclarationOfCompany,btnCurrentStateAccount,btnIgvPayment1,btnIgvPayment2,btnIgvPayment3;
    Button btnSchool,btnSemesterNumber,btnDegree,btnRequestCreditLine;
    RelativeLayout rootLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, creditLineRequestRef;
    String currentUserID;
    private ProgressDialog loadingBar;
    final static int Gallery_Pick = 0;
    final static int Gallery_Pick1 = 1;
    final static int Gallery_Pick2 = 2;
    final static int Gallery_Pick3 = 3;
    final static int Gallery_Pick4 = 4;
    final static int Gallery_Pick5 = 5;
    final static int Gallery_Pick6 = 6;
    final static int Gallery_Pick7 = 7;
    final static int Gallery_Pick8 = 8;
    final static int Gallery_Pick9 = 9;
    final static int Gallery_Pick10 = 10;
    final static int Gallery_Pick11 = 11;
    final static int Gallery_Pick12 = 12;
    final static int Gallery_Pick13 = 13;
    final static int Gallery_Pick14 = 14;
    final static int Gallery_Pick15 = 15;
    final static int Gallery_Pick16 = 16;
    final static int Gallery_Pick17 = 17;
    final static int Gallery_Pick18 = 18;
    final static int Gallery_Pick19 = 19;
    final static int Gallery_Pick20 = 20;
    final static int Gallery_Pick21 = 21;
    final static int Gallery_Pick22 = 22;
    final static int Gallery_Pick23 = 23;
    final static int Gallery_Pick24 = 24;
    private Uri imageUri,imageUri1,imageUri2,imageUri3,imageUri4,imageUri5,imageUri6,imageUri7,imageUri8,imageUri9,imageUri10,imageUri11,imageUri12,imageUri13,imageUri14,imageUri15,imageUri16,
            imageUri17,imageUri18,imageUri19,imageUri20,imageUri21,imageUri22,imageUri23,imageUri24;
    private StorageReference postImagesReference;
    private String saveCurrentDate, saveCurrentTime, postRandomName,downloadUrl,downloadUrl1,downloadUrl2,downloadUrl3,downloadUrl4,downloadUrl5,downloadUrl6,downloadUrl7,downloadUrl8,
            downloadUrl9,downloadUrl10,downloadUrl11,downloadUrl12,downloadUrl13,downloadUrl14,downloadUrl15,downloadUrl16,downloadUrl17,downloadUrl18,downloadUrl19,downloadUrl20,
            downloadUrl21,downloadUrl22,downloadUrl23,downloadUrl24;
    String image_verification,image_verification1,image_verification2,image_verification3,image_verification4,image_verification5,image_verification6,image_verification7,
            image_verification8,image_verification9,image_verification10,image_verification11,image_verification12,image_verification13,image_verification14,image_verification15,
            image_verification16,image_verification17,image_verification18,image_verification19,image_verification20,image_verification21,image_verification22,image_verification23,
            image_verification24;

    ArrayList<String> schools =new ArrayList<>();
    SpinnerDialog spinnerSchools;

    ArrayList<String> semester_number =new ArrayList<>();
    SpinnerDialog spinnerSemesterNumber;

    ArrayList<String> degree =new ArrayList<>();
    SpinnerDialog spinnerDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_line_request_form);

        postImagesReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        creditLineRequestRef = FirebaseDatabase.getInstance().getReference().child("Credit Line Request");

        credit_request_currency = getIntent().getStringExtra("credit_line_request_currency");
        credit_request_condition = getIntent().getStringExtra("credit_line_request_condition");
        imgCondition = findViewById(R.id.imgCondition);
        txtCondition = findViewById(R.id.txtCondition);
        txtCreditScore = findViewById(R.id.txtCreditScore);
        studentLayout = findViewById(R.id.studentLayout);
        dependentWorkerLayout = findViewById(R.id.dependentWorkerLayout);
        independentWorkerLayout = findViewById(R.id.independentWorkerLayout);
        lessorLayout = findViewById(R.id.lessorLayout);
        stockHolderLayout = findViewById(R.id.stockHolderLayout);
        btnReceiptLightWater = findViewById(R.id.btnReceiptLightWater);
        btnSchool= findViewById(R.id.btnSchool);
        btnSemesterNumber= findViewById(R.id.btnSemesterNumber);
        btnDegree= findViewById(R.id.btnDegree);
        btnSchoolRegister = findViewById(R.id.btnSchoolRegister);
        btnSchoolReceipt = findViewById(R.id.btnSchoolReceipt);
        btnPaymentReceipt1 = findViewById(R.id.btnPaymentReceipt1);
        btnPaymentReceipt2 = findViewById(R.id.btnPaymentReceipt2);
        btnPaymentReceipt3 = findViewById(R.id.btnPaymentReceipt3);
        btnRentTax1= findViewById(R.id.btnRentTax1);
        btnRentTax2 = findViewById(R.id.btnRentTax2);
        btnHonorPaymentReceipt1 = findViewById(R.id.btnHonorPaymentReceipt1);
        btnHonorPaymentReceipt2 = findViewById(R.id.btnHonorPaymentReceipt2);
        btnHonorPaymentReceipt3 = findViewById(R.id.btnHonorPaymentReceipt3);
        btnSunatPaymentReceipt1 = findViewById(R.id.btnSunatPaymentReceipt1);
        btnSunatPaymentReceipt2 = findViewById(R.id.btnSunatPaymentReceipt2);
        btnSunatPaymentReceipt3 = findViewById(R.id.btnSunatPaymentReceipt3);
        btnRentDeclaration1 = findViewById(R.id.btnRentDeclaration1);
        btnRentDeclaration2 = findViewById(R.id.btnRentDeclaration2);
        btnLeasingContract = findViewById(R.id.btnLeasingContract);
        btnProperty = findViewById(R.id.btnProperty);
        btnSunatPayment1 = findViewById(R.id.btnSunatPayment1);
        btnSunatPayment2 = findViewById(R.id.btnSunatPayment2);
        btnDeclarationOfCompany = findViewById(R.id.btnDeclarationOfCompany);
        btnCurrentStateAccount = findViewById(R.id.btnCurrentStateAccount);
        btnIgvPayment1 = findViewById(R.id.btnIgvPayment1);
        btnIgvPayment2 = findViewById(R.id.btnIgvPayment2);
        btnIgvPayment3 = findViewById(R.id.btnIgvPayment3);
        loadingBar = new ProgressDialog(this);
        btnRequestCreditLine = findViewById(R.id.btnRequestCreditLine);
        rootLayout = findViewById(R.id.rootLayout);

        studentLayout.getLayoutParams().height = 0;
        dependentWorkerLayout.getLayoutParams().height = 0;
        independentWorkerLayout.getLayoutParams().height = 0;
        lessorLayout.getLayoutParams().height = 0;
        stockHolderLayout.getLayoutParams().height = 0;

        schools.add("Pontificia Universidad Católica del Perú");schools.add("Universidad Alas Peruanas");schools.add("Universidad Andina del Cusco");schools.add("Universidad Andina Néstor Cáceres Velásquez");schools.add("Universidad Antonio Ruiz de Montoya");
        schools.add("Universidad Autónoma de Ica");schools.add("Universidad Autónoma del Perú");schools.add("Universidad Autónoma San Francisco");schools.add("Universidad Católica de Santa María");schools.add("Universidad Católica de Trujillo Benedicto XVI");
        schools.add("Universidad Católica Los Ángeles de Chimbote");schools.add("Universidad Católica San José");schools.add("Universidad Católica San Pablo");schools.add("Universidad Católica Santo Toribio de Mogrovejo");schools.add("Universidad Católica Sedes Sapientiae");
        schools.add("Universidad Ciencias de la Salud");schools.add("Universidad Científica del Sur");schools.add("Universidad Continental");schools.add("Universidad César Vallejo");schools.add("Universidad de Ciencias y Artes de América Latina");
        schools.add("Universidad de Ciencias y Humanidades");schools.add("Universidad de Ingeniería y Tecnología");schools.add("Universidad de Lima");schools.add("Universidad de Piura");schools.add("Universidad de San Martín de Porres");
        schools.add("Universidad del Pacífico");schools.add("Universidad ESAN");schools.add("Universidad Femenina del Sagrado Corazón");schools.add("Universidad Inca Garcilaso de la Vega");schools.add("Universidad Interamericana para el Desarrollo");
        schools.add("Universidad Jaime Bausate y Meza");schools.add("Universidad La Salle");schools.add("Universidad Le Cordon Bleu");schools.add("Universidad Marcelino Champagnat");schools.add("Universidad María Auxiliadora");
        schools.add("Universidad Marítima del Perú");schools.add("Universidad Peruana Cayetano Heredia");schools.add("Universidad Peruana de Arte Orval");schools.add("Universidad Peruana de Ciencias Aplicadas");schools.add("Universidad Peruana de Ciencias e Informática");
        schools.add("Universidad Peruana de Integración Global");schools.add("Universidad Peruana de Investigación y Negocios");schools.add("Universidad Peruana de las Américas");schools.add("Universidad Peruana Los Andes");schools.add("Universidad Peruana Simón Bolivar");
        schools.add("Universidad Peruana Unión");schools.add("Universidad Privada Arzobispo Loayza");schools.add("Universidad Privada Autónoma del Sur");schools.add("Universidad Privada de Trujillo");schools.add("Universidad Privada del Norte");
        schools.add("Universidad Privada Juan Pablo II");schools.add("Universidad Privada Norbert Wiener");schools.add("Universidad Privada Peruano Alemana");schools.add("Universidad Privada San Juan Bautista");schools.add("Universidad Privada SISE");
        schools.add("Universidad Privada Telesup");schools.add("Universidad Ricardo Palma");schools.add("Universidad San Andrés");schools.add("Universidad San Ignacio de Loyola");schools.add("Universidad Santo Domingo de Guzmán");
        schools.add("Universidad Sergio Bernales");schools.add("Universidad Señor de Sipán");schools.add("Universidad Tecnológica del Perú");schools.add("Universidad Autónoma Municipal de Los Olivos");schools.add("Universidad Nacional Agraria La Molina");
        schools.add("Universidad Nacional Ciro Alegría");schools.add("Universidad Nacional de Barranca");schools.add("Universidad Nacional de Cañete");schools.add("Universidad Nacional de Frontera");schools.add("Universidad Nacional de Ingeniería");
        schools.add("Universidad Nacional de San Antonio Abad del Cusco");schools.add("Universidad Nacional de Trujillo");schools.add("Universidad Nacional de Tumbes");schools.add("Universidad Nacional del Callao");schools.add("Universidad Nacional Federico Villarreal\t");
        schools.add("Universidad Nacional José Faustino Sánchez Carrión");schools.add("Universidad Nacional Mayor de San Marcos");schools.add("Universidad Nacional Tecnológica de Lima Sur");schools.add("Otro");

        semester_number.add("0");semester_number.add("1");semester_number.add("2");semester_number.add("3");semester_number.add("4");semester_number.add("5");
        semester_number.add("6");semester_number.add("7");semester_number.add("8");semester_number.add("9");semester_number.add("10");semester_number.add("11");
        semester_number.add("12");semester_number.add("13");semester_number.add("14");semester_number.add("15");semester_number.add("16");semester_number.add("17");
        semester_number.add("18");

        degree.add("Administración");degree.add("Administración de Negocios Internacionales");degree.add("Arquitectura");degree.add("Ciencias de la Comunicación");degree.add("Contabilidad");degree.add("Derecho");
        degree.add("Economía");degree.add("Educación");degree.add("Enfermería");degree.add("Finanzas");degree.add("Recursos Humanos");degree.add("Ingeniería Civil");
        degree.add("Ingeniería de Sistemas");degree.add("Ingeniería Industrial");degree.add("Ingeniería Informática");degree.add("Marketing");degree.add("Medicina Humana");degree.add("Obstetricia");
        degree.add("Odontología");degree.add("Psicología");degree.add("Turismo y Hotelería");degree.add("Otro");

        //Set value to string;
        image_verification = "false";
        image_verification1 = "false";
        image_verification2 = "false";
        image_verification3 = "false";
        image_verification4 = "false";
        image_verification5 = "false";
        image_verification6 = "false";
        image_verification7 = "false";
        image_verification8 = "false";
        image_verification9 = "false";
        image_verification10 = "false";
        image_verification11 = "false";
        image_verification12 = "false";
        image_verification13 = "false";
        image_verification14 = "false";
        image_verification15 = "false";
        image_verification16 = "false";
        image_verification17 = "false";
        image_verification18= "false";
        image_verification19 = "false";
        image_verification20 = "false";
        image_verification21 = "false";
        image_verification22 = "false";
        image_verification23 = "false";
        image_verification24 = "false";

        if (credit_request_condition.equals("student"))
        {
            imgCondition.setImageResource(R.drawable.student_condition_icon);
            txtCondition.setText("ESTUDIANTE UNIVERSITARIO");
            studentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        if (credit_request_condition.equals("dependent_worker"))
        {
            imgCondition.setImageResource(R.drawable.dependent_worker_condition_icon);
            txtCondition.setText("TRABAJADOR DEPENDIENTE");
            dependentWorkerLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        if (credit_request_condition.equals("independent_worker"))
        {
            imgCondition.setImageResource(R.drawable.independent_worker_condition_icon);
            txtCondition.setText("TRABAJADOR INDEPENDIENTE");
            independentWorkerLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        if (credit_request_condition.equals("lessor"))
        {
            imgCondition.setImageResource(R.drawable.leaser_condition_icon);
            txtCondition.setText("RENTISTA O ARRENDADOR");
            lessorLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        if (credit_request_condition.equals("stock_holder"))
        {
            imgCondition.setImageResource(R.drawable.holder_stock_condition_icon);
            txtCondition.setText("ACCIONISTA DE UNA EMPRESA");
            stockHolderLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        if (credit_request_currency.equals("PEN"))
        {
            txtCreditScore.setText("Línea de Crédito Solicitada en: Soles (S/) - PEN");
        }
        if (credit_request_currency.equals("USD"))
        {
            txtCreditScore.setText("Línea de Crédito Solicitada en: Dólares ($) - USD");
        }

        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerSchools.showSpinerDialog();
            }
        });

        btnSemesterNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerSemesterNumber.showSpinerDialog();
            }
        });

        btnDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDegree.showSpinerDialog();
            }
        });

        spinnerDegree = new SpinnerDialog(CreditLineRequestFormActivity.this,degree, "Selecciona tu carrera");
        spinnerDegree.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDegree.setText(item);
            }
        });

        spinnerSemesterNumber = new SpinnerDialog(CreditLineRequestFormActivity.this,semester_number, "Selecciona el semestre");
        spinnerSemesterNumber.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnSemesterNumber.setText(item);
            }
        });

        spinnerSchools = new SpinnerDialog(CreditLineRequestFormActivity.this,schools,"Selecciona tu Institución Educativa");
        spinnerSchools.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnSchool.setText(item);
            }
        });

        //Select Photos
        btnReceiptLightWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery1();
            }
        });
        btnSchoolRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery2();
            }
        });
        btnSchoolReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery3();
            }
        });

        btnPaymentReceipt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery4();
            }
        });

        btnPaymentReceipt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery5();
            }
        });

        btnPaymentReceipt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery6();
            }
        });

        btnRentTax1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery7();
            }
        });

        btnRentTax2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery8();
            }
        });

        btnHonorPaymentReceipt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery9();
            }
        });

        btnHonorPaymentReceipt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery10();
            }
        });

        btnHonorPaymentReceipt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery11();
            }
        });

        btnSunatPaymentReceipt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery12();
            }
        });

        btnSunatPaymentReceipt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery13();
            }
        });

        btnSunatPaymentReceipt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery14();
            }
        });

        btnRentDeclaration1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery15();
            }
        });

        btnRentDeclaration2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery16();
            }
        });

        btnLeasingContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery17();
            }
        });

        btnProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery18();
            }
        });

        btnSunatPayment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery19();
            }
        });
        btnSunatPayment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery20();
            }
        });

        btnDeclarationOfCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery21();
            }
        });

        btnCurrentStateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery22();
            }
        });

        btnIgvPayment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery23();
            }
        });

        btnIgvPayment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery24();
            }
        });

        btnIgvPayment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery25();
            }
        });

        btnRequestCreditLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!image_verification.equals("true"))
                {
                    Snackbar.make(rootLayout, "Debes subir una foto de tu último recibo de agua o luz pagado", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (credit_request_condition.equals("student"))
                {
                    if (TextUtils.isEmpty(btnSchool.getText().toString()))
                    {
                        Snackbar.make(rootLayout, "Debes seleccionar a qué institución educativa perteneces", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(btnSemesterNumber.getText().toString()))
                    {
                        Snackbar.make(rootLayout, "Debes seleccionar un semestre académico", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(btnDegree.getText().toString()))
                    {
                        Snackbar.make(rootLayout, "Debes seleccionar tu carrera en curso", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification1.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu constancia de matrícula", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification2.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu boleta de pago cancelada más reciente", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (credit_request_condition.equals("dependent_worker"))
                {
                    if (!image_verification3.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu boleta de pago como trabajador dependiente.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification4.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu boleta de pago como trabajador dependiente.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification5.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu boleta de pago como trabajador dependiente.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (credit_request_condition.equals("independent_worker"))
                {
                    if (!image_verification6.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de declaración anual de Impuesto a la Renta de cuarta categoría.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification7.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de declaración anual de Impuesto a la Renta de cuarta categoría.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification8.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimos recibos por honorarios.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification9.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimos recibos por honorarios.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification10.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimos recibos por honorarios.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification11.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimos pagos a la SUNAT.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification12.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimos pagos a la SUNAT.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification13.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimos pagos a la SUNAT.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (credit_request_condition.equals("lessor"))
                {
                    if (!image_verification14.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimas declaraciones juradas de Impuesto a La Renta.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification15.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimas declaraciones juradas de Impuesto a La Renta.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification16.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu último contrato de alquiler", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification17.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu autovalúo o propiedad arrendada", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification18.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimas pagos a la Sunat.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification19.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tus últimas pagos a la Sunat.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (credit_request_condition.equals("stock_holder"))
                {
                    if (!image_verification20.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu última declaración jurada de la empresa o ficha registral.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification21.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto del estado de cuenta corriente de la empresa", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification22.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu úlimo pago de IGV", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification23.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu úlimo pago de IGV", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!image_verification24.equals("true"))
                    {
                        Snackbar.make(rootLayout, "Debes subir una foto de tu úlimo pago de IGV", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                else
                {
                    loadingBar.setTitle("Preparando tu solicitud...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);
                    storingImageToFirebaseStorage();
                }
            }
        });
    }

    private void openGallery25() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick24);
    }

    private void openGallery24() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick23);
    }

    private void openGallery23() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick22);
    }

    private void openGallery22() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick21);
    }

    private void openGallery21() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick20);
    }

    private void openGallery20() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick19);
    }

    private void openGallery19() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick18);
    }

    private void openGallery18() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick17);
    }

    private void openGallery17() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick16);
    }

    private void openGallery16() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick15);
    }

    private void openGallery15() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick14);
    }

    private void openGallery14() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick13);
    }

    private void openGallery13() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick12);
    }

    private void openGallery12() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick11);
    }

    private void openGallery11() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick10);
    }

    private void openGallery10() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick9);
    }

    private void openGallery9() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick8);
    }

    private void openGallery8() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick7);
    }

    private void openGallery7() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick6);
    }

    private void openGallery6() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick5);
    }

    private void openGallery5() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick4);
    }

    private void openGallery4() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick3);
    }

    private void storingImageToFirebaseStorage() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        StorageReference filePath = postImagesReference.child("Credit Line Requests").child(imageUri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    downloadUrl = task.getResult().getDownloadUrl().toString();

                    if (credit_request_condition.equals("student"))
                    {
                        StorageReference filePath1 = postImagesReference.child("Credit Line Requests").child(imageUri1.getLastPathSegment()+postRandomName+".jpg");
                        filePath1.putFile(imageUri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    downloadUrl1 = task.getResult().getDownloadUrl().toString();

                                    StorageReference filePath2 = postImagesReference.child("Credit Line Requests").child(imageUri2.getLastPathSegment()+postRandomName+".jpg");
                                    filePath2.putFile(imageUri2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                downloadUrl2 = task.getResult().getDownloadUrl().toString();
                                                savingPostInformationToDatabase();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    if (credit_request_condition.equals("dependent_worker"))
                    {
                        StorageReference filePath3 = postImagesReference.child("Credit Line Requests").child(imageUri3.getLastPathSegment()+postRandomName+".jpg");
                        filePath3.putFile(imageUri3).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    downloadUrl3 = task.getResult().getDownloadUrl().toString();
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        StorageReference filePath4 = postImagesReference.child("Credit Line Requests").child(imageUri4.getLastPathSegment()+postRandomName+".jpg");
                        filePath4.putFile(imageUri4).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    downloadUrl4 = task.getResult().getDownloadUrl().toString();

                                    StorageReference filePath5 = postImagesReference.child("Credit Line Requests").child(imageUri5.getLastPathSegment()+postRandomName+".jpg");
                                    filePath5.putFile(imageUri5).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                downloadUrl5 = task.getResult().getDownloadUrl().toString();
                                                savingPostInformationToDatabase();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    if (credit_request_condition.equals("independent_worker"))
                    {
                        StorageReference filePath6 = postImagesReference.child("Credit Line Requests").child(imageUri6.getLastPathSegment()+postRandomName+".jpg");
                        filePath6.putFile(imageUri6).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    downloadUrl6 = task.getResult().getDownloadUrl().toString();

                                    StorageReference filePath7 = postImagesReference.child("Credit Line Requests").child(imageUri7.getLastPathSegment()+postRandomName+".jpg");
                                    filePath7.putFile(imageUri7).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                downloadUrl7 = task.getResult().getDownloadUrl().toString();

                                                StorageReference filePath8 = postImagesReference.child("Credit Line Requests").child(imageUri8.getLastPathSegment()+postRandomName+".jpg");
                                                filePath8.putFile(imageUri8).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            downloadUrl8 = task.getResult().getDownloadUrl().toString();

                                                            StorageReference filePath9 = postImagesReference.child("Credit Line Requests").child(imageUri9.getLastPathSegment()+postRandomName+".jpg");
                                                            filePath9.putFile(imageUri9).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        downloadUrl9 = task.getResult().getDownloadUrl().toString();

                                                                        StorageReference filePath10 = postImagesReference.child("Credit Line Requests").child(imageUri10.getLastPathSegment()+postRandomName+".jpg");
                                                                        filePath10.putFile(imageUri10).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                if (task.isSuccessful())
                                                                                {
                                                                                    downloadUrl10 = task.getResult().getDownloadUrl().toString();

                                                                                    StorageReference filePath11 = postImagesReference.child("Credit Line Requests").child(imageUri11.getLastPathSegment()+postRandomName+".jpg");
                                                                                    filePath11.putFile(imageUri11).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                            if (task.isSuccessful())
                                                                                            {
                                                                                                downloadUrl11 = task.getResult().getDownloadUrl().toString();

                                                                                                StorageReference filePath12 = postImagesReference.child("Credit Line Requests").child(imageUri12.getLastPathSegment()+postRandomName+".jpg");
                                                                                                filePath12.putFile(imageUri12).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                                        if (task.isSuccessful())
                                                                                                        {
                                                                                                            downloadUrl12 = task.getResult().getDownloadUrl().toString();

                                                                                                            StorageReference filePath13 = postImagesReference.child("Credit Line Requests").child(imageUri13.getLastPathSegment()+postRandomName+".jpg");
                                                                                                            filePath13.putFile(imageUri13).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                                                    if (task.isSuccessful())
                                                                                                                    {
                                                                                                                        downloadUrl13 = task.getResult().getDownloadUrl().toString();
                                                                                                                        savingPostInformationToDatabase();
                                                                                                                    }
                                                                                                                    else
                                                                                                                    {
                                                                                                                        String message = task.getException().getMessage();
                                                                                                                        Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                        else
                                                                                                        {
                                                                                                            String message = task.getException().getMessage();
                                                                                                            Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                String message = task.getException().getMessage();
                                                                                                Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                                else
                                                                                {
                                                                                    String message = task.getException().getMessage();
                                                                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                    else
                                                                    {
                                                                        String message = task.getException().getMessage();
                                                                        Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                        else
                                                        {
                                                            String message = task.getException().getMessage();
                                                            Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    if (credit_request_condition.equals("lessor"))
                    {
                        StorageReference filePath14 = postImagesReference.child("Credit Line Requests").child(imageUri14.getLastPathSegment()+postRandomName+".jpg");
                        filePath14.putFile(imageUri14).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    downloadUrl14 = task.getResult().getDownloadUrl().toString();

                                    StorageReference filePath15 = postImagesReference.child("Credit Line Requests").child(imageUri15.getLastPathSegment()+postRandomName+".jpg");
                                    filePath15.putFile(imageUri15).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                downloadUrl15 = task.getResult().getDownloadUrl().toString();

                                                StorageReference filePath16 = postImagesReference.child("Credit Line Requests").child(imageUri16.getLastPathSegment()+postRandomName+".jpg");
                                                filePath16.putFile(imageUri16).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            downloadUrl16 = task.getResult().getDownloadUrl().toString();

                                                            StorageReference filePath17 = postImagesReference.child("Credit Line Requests").child(imageUri17.getLastPathSegment()+postRandomName+".jpg");
                                                            filePath17.putFile(imageUri17).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        downloadUrl17 = task.getResult().getDownloadUrl().toString();

                                                                        StorageReference filePath18 = postImagesReference.child("Credit Line Requests").child(imageUri18.getLastPathSegment()+postRandomName+".jpg");
                                                                        filePath18.putFile(imageUri18).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                if (task.isSuccessful())
                                                                                {
                                                                                    downloadUrl18 = task.getResult().getDownloadUrl().toString();

                                                                                    StorageReference filePath19 = postImagesReference.child("Credit Line Requests").child(imageUri19.getLastPathSegment()+postRandomName+".jpg");
                                                                                    filePath19.putFile(imageUri19).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                            if (task.isSuccessful())
                                                                                            {
                                                                                                downloadUrl19 = task.getResult().getDownloadUrl().toString();
                                                                                                savingPostInformationToDatabase();
                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                String message = task.getException().getMessage();
                                                                                                Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                                else
                                                                                {
                                                                                    String message = task.getException().getMessage();
                                                                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                    else
                                                                    {
                                                                        String message = task.getException().getMessage();
                                                                        Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                        else
                                                        {
                                                            String message = task.getException().getMessage();
                                                            Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    if (credit_request_condition.equals("stock_holder"))
                    {
                        StorageReference filePath20 = postImagesReference.child("Credit Line Requests").child(imageUri20.getLastPathSegment()+postRandomName+".jpg");
                        filePath20.putFile(imageUri20).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    downloadUrl20 = task.getResult().getDownloadUrl().toString();

                                    StorageReference filePath21 = postImagesReference.child("Credit Line Requests").child(imageUri21.getLastPathSegment()+postRandomName+".jpg");
                                    filePath21.putFile(imageUri21).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                downloadUrl21 = task.getResult().getDownloadUrl().toString();

                                                StorageReference filePath22 = postImagesReference.child("Credit Line Requests").child(imageUri22.getLastPathSegment()+postRandomName+".jpg");
                                                filePath22.putFile(imageUri22).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            downloadUrl22 = task.getResult().getDownloadUrl().toString();

                                                            StorageReference filePath23 = postImagesReference.child("Credit Line Requests").child(imageUri23.getLastPathSegment()+postRandomName+".jpg");
                                                            filePath23.putFile(imageUri23).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        downloadUrl23 = task.getResult().getDownloadUrl().toString();

                                                                        StorageReference filePath24 = postImagesReference.child("Credit Line Requests").child(imageUri24.getLastPathSegment()+postRandomName+".jpg");
                                                                        filePath24.putFile(imageUri24).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                if (task.isSuccessful())
                                                                                {
                                                                                    downloadUrl24 = task.getResult().getDownloadUrl().toString();
                                                                                    savingPostInformationToDatabase();
                                                                                }
                                                                                else
                                                                                {
                                                                                    String message = task.getException().getMessage();
                                                                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                    else
                                                                    {
                                                                        String message = task.getException().getMessage();
                                                                        Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                        else
                                                        {
                                                            String message = task.getException().getMessage();
                                                            Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(CreditLineRequestFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void savingPostInformationToDatabase() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        HashMap userMap = new HashMap();
        userMap.put("credit_line_request_condition",credit_request_condition);
        userMap.put("credit_line_request_currency",credit_request_currency);
        userMap.put("everyone_water_light_receipt",downloadUrl);
        userMap.put("date",saveCurrentDate);
        userMap.put("time",saveCurrentTime);
        userMap.put("date_time",saveCurrentDate+saveCurrentTime);
        userMap.put("uid",currentUserID);
        userMap.put("timestamp", ServerValue.TIMESTAMP);
        if (credit_request_condition.equals("student"))
        {
            //Students
            userMap.put("student_school_name",btnSchool.getText().toString());
            userMap.put("student_school_semester_numer",btnSemesterNumber.getText().toString());
            userMap.put("student_school_current_degree",btnDegree.getText().toString());
            userMap.put("student_school_register_sheet",downloadUrl1);
            userMap.put("student_school_payment_receipt",downloadUrl2);
        }
        if (credit_request_condition.equals("dependent_worker"))
        {
            userMap.put("dependent_worker_payment_receipt1",downloadUrl3);
            userMap.put("dependent_worker_payment_receipt2",downloadUrl4);
            userMap.put("dependent_worker_payment_receipt3",downloadUrl5);
        }
        if (credit_request_condition.equals("independent_worker"))
        {
            userMap.put("independent_worker_rent_tax_declaration1",downloadUrl6);
            userMap.put("independent_worker_rent_tax_declaration2",downloadUrl7);
            userMap.put("independent_worker_honorary_receipt1",downloadUrl8);
            userMap.put("independent_worker_honorary_receipt2",downloadUrl9);
            userMap.put("independent_worker_honorary_receipt3",downloadUrl10);
            userMap.put("independent_worker_payment_to_sunat1",downloadUrl11);
            userMap.put("independent_worker_payment_to_sunat2",downloadUrl12);
            userMap.put("independent_worker_payment_to_sunat3",downloadUrl13);
        }
        if (credit_request_condition.equals("lessor"))
        {
            userMap.put("lessor_rent_tax_declaration1",downloadUrl14);
            userMap.put("lessor_rent_tax_declaration2",downloadUrl15);
            userMap.put("lessor_last_leasing_contract",downloadUrl16);
            userMap.put("lessor_property_in_leasing",downloadUrl17);
            userMap.put("lessor_payment_to_sunat1",downloadUrl18);
            userMap.put("lessor_payment_to_sunat2",downloadUrl19);
        }
        if (credit_request_condition.equals("stock_holder"))
        {
            userMap.put("stock_holder_last_company_declaration",downloadUrl20);
            userMap.put("stock_holder_bank_account_current_state",downloadUrl21);
            userMap.put("stock_holder_bank_igv_payment1",downloadUrl22);
            userMap.put("stock_holder_bank_igv_payment2",downloadUrl23);
            userMap.put("stock_holder_bank_igv_payment3",downloadUrl24);
        }

        creditLineRequestRef.child(currentUserID+postRandomName).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                changeCreditLineRequestState();
            }
        });
    }

    private void changeCreditLineRequestState() {
        HashMap userMap = new HashMap();
        if (credit_request_currency.equals("PEN"))
        {
            userMap.put("credit_line_pen_request_state","requested");
        }
        if (credit_request_currency.equals("USD"))
        {
            userMap.put("credit_line_usd_request_state","requested");
        }

        userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                loadingBar.dismiss();
                Intent intent = new Intent(CreditLineRequestFormActivity.this, MyAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void openGallery3() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick2);
    }

    private void openGallery2() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick1);
    }

    private void openGallery1() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri = data.getData();
            btnReceiptLightWater.setImageURI(imageUri);
            Picasso.with(this).load(imageUri).fit().into(btnReceiptLightWater);
            image_verification = "true";
        }
        if (requestCode==Gallery_Pick1&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri1 = data.getData();
            btnSchoolRegister.setImageURI(imageUri1);
            Picasso.with(this).load(imageUri1).fit().into(btnSchoolRegister);
            image_verification1 = "true";
        }
        if (requestCode==Gallery_Pick2&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri2 = data.getData();
            btnSchoolReceipt.setImageURI(imageUri2);
            Picasso.with(this).load(imageUri2).fit().into(btnSchoolReceipt);
            image_verification2 = "true";
        }
        if (requestCode==Gallery_Pick3&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri3 = data.getData();
            btnPaymentReceipt1.setImageURI(imageUri3);
            Picasso.with(this).load(imageUri3).fit().into(btnPaymentReceipt1);
            image_verification3 = "true";
        }
        if (requestCode==Gallery_Pick4&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri4 = data.getData();
            btnPaymentReceipt2.setImageURI(imageUri4);
            Picasso.with(this).load(imageUri4).fit().into(btnPaymentReceipt2);
            image_verification4 = "true";
        }
        if (requestCode==Gallery_Pick5&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri5 = data.getData();
            btnPaymentReceipt3.setImageURI(imageUri5);
            Picasso.with(this).load(imageUri5).fit().into(btnPaymentReceipt3);
            image_verification5 = "true";
        }
        if (requestCode==Gallery_Pick6&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri6 = data.getData();
            btnRentTax1.setImageURI(imageUri6);
            Picasso.with(this).load(imageUri6).fit().into(btnRentTax1);
            image_verification6 = "true";
        }
        if (requestCode==Gallery_Pick7&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri7 = data.getData();
            btnRentTax2.setImageURI(imageUri7);
            Picasso.with(this).load(imageUri7).fit().into(btnRentTax2);
            image_verification7 = "true";
        }
        if (requestCode==Gallery_Pick8&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri8 = data.getData();
            btnHonorPaymentReceipt1.setImageURI(imageUri8);
            Picasso.with(this).load(imageUri8).fit().into(btnHonorPaymentReceipt1);
            image_verification8 = "true";
        }
        if (requestCode==Gallery_Pick9&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri9 = data.getData();
            btnHonorPaymentReceipt2.setImageURI(imageUri9);
            Picasso.with(this).load(imageUri9).fit().into(btnHonorPaymentReceipt2);
            image_verification9 = "true";
        }
        if (requestCode==Gallery_Pick10&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri10 = data.getData();
            btnHonorPaymentReceipt3.setImageURI(imageUri10);
            Picasso.with(this).load(imageUri10).fit().into(btnHonorPaymentReceipt3);
            image_verification10 = "true";
        }
        if (requestCode==Gallery_Pick11&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri11 = data.getData();
            btnSunatPaymentReceipt1.setImageURI(imageUri11);
            Picasso.with(this).load(imageUri11).fit().into(btnSunatPaymentReceipt1);
            image_verification11 = "true";
        }
        if (requestCode==Gallery_Pick12&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri12 = data.getData();
            btnSunatPaymentReceipt2.setImageURI(imageUri12);
            Picasso.with(this).load(imageUri12).fit().into(btnSunatPaymentReceipt2);
            image_verification12 = "true";
        }
        if (requestCode==Gallery_Pick13&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri13 = data.getData();
            btnSunatPaymentReceipt3.setImageURI(imageUri13);
            Picasso.with(this).load(imageUri13).fit().into(btnSunatPaymentReceipt3);
            image_verification13 = "true";
        }
        if (requestCode==Gallery_Pick14&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri14 = data.getData();
            btnRentDeclaration1.setImageURI(imageUri14);
            Picasso.with(this).load(imageUri14).fit().into(btnRentDeclaration1);
            image_verification14 = "true";
        }
        if (requestCode==Gallery_Pick15&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri15 = data.getData();
            btnRentDeclaration2.setImageURI(imageUri15);
            Picasso.with(this).load(imageUri15).fit().into(btnRentDeclaration2);
            image_verification15 = "true";
        }
        if (requestCode==Gallery_Pick16&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri16 = data.getData();
            btnLeasingContract.setImageURI(imageUri16);
            Picasso.with(this).load(imageUri16).fit().into(btnLeasingContract);
            image_verification16 = "true";
        }
        if (requestCode==Gallery_Pick17&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri17 = data.getData();
            btnProperty.setImageURI(imageUri17);
            Picasso.with(this).load(imageUri17).fit().into(btnProperty);
            image_verification17 = "true";
        }
        if (requestCode==Gallery_Pick18&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri18 = data.getData();
            btnSunatPayment1.setImageURI(imageUri18);
            Picasso.with(this).load(imageUri18).fit().into(btnSunatPayment1);
            image_verification18 = "true";
        }
        if (requestCode==Gallery_Pick19&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri19 = data.getData();
            btnSunatPayment2.setImageURI(imageUri19);
            Picasso.with(this).load(imageUri19).fit().into(btnSunatPayment2);
            image_verification19 = "true";
        }
        if (requestCode==Gallery_Pick20&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri20 = data.getData();
            btnDeclarationOfCompany.setImageURI(imageUri20);
            Picasso.with(this).load(imageUri20).fit().into(btnDeclarationOfCompany);
            image_verification20 = "true";
        }
        if (requestCode==Gallery_Pick21&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri21 = data.getData();
            btnCurrentStateAccount.setImageURI(imageUri21);
            Picasso.with(this).load(imageUri21).fit().into(btnCurrentStateAccount);
            image_verification21 = "true";
        }
        if (requestCode==Gallery_Pick22&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri22 = data.getData();
            btnIgvPayment1.setImageURI(imageUri22);
            Picasso.with(this).load(imageUri22).fit().into(btnIgvPayment1);
            image_verification22 = "true";
        }
        if (requestCode==Gallery_Pick23&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri23 = data.getData();
            btnIgvPayment2.setImageURI(imageUri23);
            Picasso.with(this).load(imageUri23).fit().into(btnIgvPayment2);
            image_verification23 = "true";
        }
        if (requestCode==Gallery_Pick24&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri24 = data.getData();
            btnIgvPayment3.setImageURI(imageUri24);
            Picasso.with(this).load(imageUri24).fit().into(btnIgvPayment3);
            image_verification24 = "true";
        }

    }
}
