package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import de.hdodenhof.circleimageview.CircleImageView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.AddCompanyActivity;

public class RegisterInfoActivity extends AppCompatActivity {

    ImageButton btnImageProfile;
    EditText edtName,edtSurname,edtDocumentNumber,edtEmail,edtAddress,edtUsername,edtPin,edtConfirmPin;
    Button btnDocumentType,btnDepartment,btnDistrict,btnOccupation,btnAcademicDegree,btnRegister,btnProvince,edtBthDay,edtBthMonth,edtBthYear;
    RadioButton rdMan,rdWoman;
    CountryCodePicker ccpCNationalityountry;
    CheckBox checkDataTrue,checkDataManagement;
    TextView txtImageVerification;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID, phoneNumber, user_address;
    private ProgressDialog loadingBar;
    final static int Gallery_Pick = 1;
    private StorageReference userProfileImageRef;
    private RelativeLayout rootLayout;
    String gender,saveCurrentDate,saveCurrentTime,postRandomName;
    String userVerification = "progress", dni_exist, username_exist;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_STORAGE_REQUEST_CODE = 200;

    String company_id;
    String TAG = "GenerateQRCode",downloadUrlQr;
    ImageView qrCode;
    String edtValue;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    Uri uri;
    Context inContext;
    Button btnFinish;

    ArrayList<String> ocupations =new ArrayList<>();
    SpinnerDialog spinnerOcupation;

    ArrayList<String> documentType =new ArrayList<>();
    SpinnerDialog spinnerDocummentType;

    ArrayList<String> academicDegrees =new ArrayList<>();
    SpinnerDialog spinnerAcademicDegrees;

    ArrayList<String> department =new ArrayList<>();
    SpinnerDialog spinnerDepartment;

    ArrayList<String> province =new ArrayList<>();
    SpinnerDialog spinnerprovince;

    ArrayList<String> provinceCallao =new ArrayList<>();
    SpinnerDialog spinnerprovinceCallao;

    ArrayList<String> district_list =new ArrayList<>();
    SpinnerDialog spinnerdistrict_list;

    ArrayList<String> district_listBarranca =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listBarranca;

    ArrayList<String> district_listCajatambo =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCajatambo;

    ArrayList<String> district_listCañete =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCañete;

    ArrayList<String> district_listCanta =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCanta;

    ArrayList<String> district_listHuaral =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listHuaral;

    ArrayList<String> district_listHuarochirí =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listHuarochirí;

    ArrayList<String> district_listHuaura =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listHuaura;

    ArrayList<String> district_listOyon =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listOyon;

    ArrayList<String> district_listYauyos =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listYauyos;

    ArrayList<String> district_listCallao =new ArrayList<>();
    SpinnerDialog spinnerdistrict_listCallao;

    ArrayList<String> bthDay =new ArrayList<>();
    SpinnerDialog bthDayDialog;

    ArrayList<String> bthMonth =new ArrayList<>();
    SpinnerDialog bthMonthDialog;

    ArrayList<String> bthYear =new ArrayList<>();
    SpinnerDialog bthYearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
            }
        }

        btnImageProfile = findViewById(R.id.btnImageProfile);
        edtName = findViewById(R.id.edtName);
        edtSurname = findViewById(R.id.edtSurname);
        edtDocumentNumber = findViewById(R.id.edtDocumentNumber);
        edtBthDay = findViewById(R.id.edtBthDay);
        edtBthMonth = findViewById(R.id.edtBthMonth);
        edtBthYear = findViewById(R.id.edtBthYear);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtUsername = findViewById(R.id.edtUsername);
        btnDocumentType = findViewById(R.id.btnDocumentType);
        btnDepartment = findViewById(R.id.btnDepartment);
        btnProvince = findViewById(R.id.btnProvince);
        btnDistrict = findViewById(R.id.btnDistrict);
        btnOccupation = findViewById(R.id.btnOccupation);
        btnAcademicDegree = findViewById(R.id.btnAcademicDegree);
        btnRegister = findViewById(R.id.btnRegister);
        rdMan = findViewById(R.id.rdMan);
        rdWoman = findViewById(R.id.rdWoman);
        ccpCNationalityountry = findViewById(R.id.ccpCNationalityountry);
        edtPin = findViewById(R.id.edtPin);
        edtConfirmPin = findViewById(R.id.edtConfirmPin);
        checkDataTrue = findViewById(R.id.checkDataTrue);
        checkDataManagement = findViewById(R.id.checkDataManagement);
        rootLayout = findViewById(R.id.rootLayout);
        txtImageVerification = findViewById(R.id.txtImageVerification);
        qrCode = findViewById(R.id.qrCode);
        loadingBar = new ProgressDialog(this);

        dni_exist = "false";
        username_exist = "false";

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        phoneNumber = mAuth.getCurrentUser().getPhoneNumber();

        //Ocupations:
        ocupations.add("Abogado");ocupations.add("Actor");ocupations.add("Accionista");ocupations.add("Artista");ocupations.add("Director de Espectáculos");ocupations.add("Administrador");ocupations.add("Agente de Aduanas");
        ocupations.add("Aeromozo");ocupations.add("Agente de Bolsa");ocupations.add("Agente de Turismo");ocupations.add("Agricultor");ocupations.add("Agrónomo");ocupations.add("Analista de Sistemas");
        ocupations.add("Antropólogo");ocupations.add("Arqueólogo");ocupations.add("Archivero");ocupations.add("Armador de Barco");ocupations.add("Arquitecto");ocupations.add("Artesano");
        ocupations.add("Asistente Social");ocupations.add("Autor Literario");ocupations.add("Avicultor");ocupations.add("Bacteriólogo");ocupations.add("Biólogo");ocupations.add("Basurero");
        ocupations.add("Cajero");ocupations.add("Camarero");ocupations.add("Cambista de Divisas");ocupations.add("Campesino");ocupations.add("Capataz");ocupations.add("Cargador");
        ocupations.add("Carpintero");ocupations.add("Cartero");ocupations.add("Cerrajero");ocupations.add("Chef");ocupations.add("Científico");ocupations.add("Cobrador");ocupations.add("Comerciante");ocupations.add("Conductor");
        ocupations.add("Conserje");ocupations.add("Constructor");ocupations.add("Contador");ocupations.add("Contratista");ocupations.add("Corredor Inmobiliario");ocupations.add("Corredor de Seguros");
        ocupations.add("Corte y Confección de Ropas");ocupations.add("Cosmetólogo");ocupations.add("Decorador");ocupations.add("Dibujante");ocupations.add("Dentista");ocupations.add("Deportista ");
        ocupations.add("Distribuidor");ocupations.add("Docente");ocupations.add("Doctor - Medicina");ocupations.add("Economista");ocupations.add("Electricista");ocupations.add("Empresario");ocupations.add("Exportador");
        ocupations.add("Importador"); ocupations.add("Inversionista");ocupations.add("Enfermero");ocupations.add("Ensamblador");ocupations.add("Escultor");ocupations.add("Estudiante");ocupations.add("Fotógrafo");
        ocupations.add("Gerente");ocupations.add("Ingeniero");ocupations.add("Jubilado");ocupations.add("Maquinista");ocupations.add("Mayorista");ocupations.add("Mecánico");ocupations.add("Médico");
        ocupations.add("Miembro de las Fuerzas Armadas");ocupations.add("Nutricionista");ocupations.add("Obstetriz");ocupations.add("Obrero de Construcción");ocupations.add("Organizador de Eventos");ocupations.add("Panadero");ocupations.add("Pastelero");
        ocupations.add("Paramédico");ocupations.add("Periodista");ocupations.add("Perito");ocupations.add("Pescador");ocupations.add("Piloto");ocupations.add("Pintor");
        ocupations.add("Policía");ocupations.add("Productor de Cine");ocupations.add("Programador");ocupations.add("Psicólogo");ocupations.add("Relojero");ocupations.add("Rentista");ocupations.add("Repartidor");
        ocupations.add("Secretaría");ocupations.add("Seguridad");ocupations.add("Sociólogo");ocupations.add("Tasador");ocupations.add("Trabajador Independiente");
        ocupations.add("Trabajador Dependiente");ocupations.add("Transportista");ocupations.add("Veterinario");
        ocupations.add("Visitador Medico");ocupations.add("Zapatero");

        btnOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerOcupation.showSpinerDialog();
            }
        });

        spinnerOcupation = new SpinnerDialog(RegisterInfoActivity.this,ocupations,"Selecciona tu Ocupación");
        spinnerOcupation.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnOccupation.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        documentType.add("D.N.I");
        documentType.add("Pasaporte");

        btnDocumentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDocummentType.showSpinerDialog();
            }
        });

        spinnerDocummentType = new SpinnerDialog(RegisterInfoActivity.this,documentType, "Selecciona el Tipo de Documento");
        spinnerDocummentType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                btnDocumentType.setText(item2);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item2, Toast.LENGTH_SHORT).show();
            }
        });

        bthDay.add("1"); bthDay.add("2"); bthDay.add("3"); bthDay.add("4"); bthDay.add("5"); bthDay.add("6"); bthDay.add("7"); bthDay.add("8"); bthDay.add("9"); bthDay.add("10");
        bthDay.add("11"); bthDay.add("12"); bthDay.add("13"); bthDay.add("14"); bthDay.add("15"); bthDay.add("16"); bthDay.add("17"); bthDay.add("18"); bthDay.add("19"); bthDay.add("20");
        bthDay.add("21"); bthDay.add("22"); bthDay.add("23"); bthDay.add("24"); bthDay.add("25"); bthDay.add("26"); bthDay.add("27"); bthDay.add("28"); bthDay.add("29"); bthDay.add("30");
        bthDay.add("31");

        edtBthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bthDayDialog.showSpinerDialog();
            }
        });

        bthDayDialog = new SpinnerDialog(RegisterInfoActivity.this,bthDay, "Selecciona tu Día de Nacimiento");
        bthDayDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthDay.setText(item2);
            }
        });

        bthMonth.add("1");bthMonth.add("2");bthMonth.add("3");bthMonth.add("4");bthMonth.add("5");bthMonth.add("6");bthMonth.add("7");bthMonth.add("8");bthMonth.add("9");bthMonth.add("10");
        bthMonth.add("11");bthMonth.add("12");

        edtBthMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bthMonthDialog.showSpinerDialog();
            }
        });

        bthMonthDialog = new SpinnerDialog(RegisterInfoActivity.this,bthMonth, "Selecciona tu Mes de Nacimiento");
        bthMonthDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthMonth.setText(item2);
            }
        });

        bthYear.add("2019");bthYear.add("2018");bthYear.add("2017");bthYear.add("2016");bthYear.add("2015");bthYear.add("2014");bthYear.add("2013");bthYear.add("2012");bthYear.add("2011");bthYear.add("2010");
        bthYear.add("2009");bthYear.add("2008");bthYear.add("2007");bthYear.add("2006");bthYear.add("2005");bthYear.add("2004");bthYear.add("2003");bthYear.add("2002");bthYear.add("2001");bthYear.add("2000");
        bthYear.add("1999");bthYear.add("1998");bthYear.add("1997");bthYear.add("1996");bthYear.add("1995");bthYear.add("1994");bthYear.add("1993");bthYear.add("1992");bthYear.add("1991");bthYear.add("1990");
        bthYear.add("1989");bthYear.add("1988");bthYear.add("1987");bthYear.add("1986");bthYear.add("1985");bthYear.add("1984");bthYear.add("1983");bthYear.add("1982");bthYear.add("1981");bthYear.add("1980");
        bthYear.add("1979");bthYear.add("1978");bthYear.add("1977");bthYear.add("1976");bthYear.add("1975");bthYear.add("1974");bthYear.add("1973");bthYear.add("1972");bthYear.add("1971");bthYear.add("1970");
        bthYear.add("1969");bthYear.add("1968");bthYear.add("1967");bthYear.add("1966");bthYear.add("1965");bthYear.add("1964");bthYear.add("1963");bthYear.add("1962");bthYear.add("1961");bthYear.add("1960");
        bthYear.add("1959");bthYear.add("1958");bthYear.add("1957");bthYear.add("1956");bthYear.add("1955");bthYear.add("1954");bthYear.add("1953");bthYear.add("1952");bthYear.add("1951");bthYear.add("1950");
        bthYear.add("1949");bthYear.add("1948");bthYear.add("1947");bthYear.add("1946");bthYear.add("1945");bthYear.add("1944");bthYear.add("1943");bthYear.add("1942");bthYear.add("1941");bthYear.add("1940");
        bthYear.add("1939");bthYear.add("1938");bthYear.add("1937");bthYear.add("1936");bthYear.add("1935");bthYear.add("1934");bthYear.add("1933");bthYear.add("1932");bthYear.add("1931");bthYear.add("1930");

        edtBthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bthYearDialog.showSpinerDialog();
            }
        });

        bthYearDialog = new SpinnerDialog(RegisterInfoActivity.this,bthYear, "Selecciona tu Año de Nacimiento");
        bthYearDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthYear.setText(item2);
            }
        });

        academicDegrees.add("Educación Inicial");academicDegrees.add("Educación Primaria");academicDegrees.add("Educación Secundaria");
        academicDegrees.add("Educación Superior Técnica");academicDegrees.add("Educación Superior Universitaria");academicDegrees.add("Maestría");
        academicDegrees.add("Doctorado");

        btnAcademicDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerAcademicDegrees.showSpinerDialog();
            }
        });

        spinnerAcademicDegrees = new SpinnerDialog(RegisterInfoActivity.this,academicDegrees, "Selecciona tu Grado Académico");
        spinnerAcademicDegrees.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                btnAcademicDegree.setText(item2);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item2, Toast.LENGTH_SHORT).show();
            }
        });

        department.add("Lima");department.add("Callao");

        btnDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDepartment.showSpinerDialog();
            }
        });

        spinnerDepartment = new SpinnerDialog(RegisterInfoActivity.this,department,"Selecciona tu Departamento");
        spinnerDepartment.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDepartment.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        province.add("Lima");province.add("Barranca");province.add("Cajatambo");province.add("Cañete");province.add("Canta");province.add("Huaral");province.add("Huarochirí");
        province.add("Huaura");province.add("Oyón");province.add("Yauyos");

        provinceCallao.add("Callao");

        btnProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDistrict.setText("");
                if (btnDepartment.getText().toString().equals("Lima"))
                {
                    spinnerprovince.showSpinerDialog();
                }
                if (btnDepartment.getText().toString().equals("Callao"))
                {
                    spinnerprovinceCallao.showSpinerDialog();
                }
                else
                {
                    Snackbar.make(rootLayout, "Debes seleccionar un departamento primero", Snackbar.LENGTH_LONG).show();
                    return;
                }

            }
        });

        spinnerprovince = new SpinnerDialog(RegisterInfoActivity.this,province,"Selecciona tu Provincia");
        spinnerprovince.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnProvince.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerprovinceCallao = new SpinnerDialog(RegisterInfoActivity.this,provinceCallao,"Selecciona tu Provincia");
        spinnerprovinceCallao.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnProvince.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        district_list.add("Ancón");district_list.add("Ate");district_list.add("Barranco");district_list.add("Breña");district_list.add("Carabayllo");
        district_list.add("Chaclacayo");district_list.add("Chorrillos");district_list.add("Cieneguilla");district_list.add("Comas");
        district_list.add("El Agustino");district_list.add("Independencia");district_list.add("Jesús María");district_list.add("La Molina");
        district_list.add("La Victoria");district_list.add("Lima");district_list.add("Lince");district_list.add("Los Olivos");
        district_list.add("Lurigancho");district_list.add("Lurín");district_list.add("Magdalena del Mar");district_list.add("Miraflores");
        district_list.add("Pachacamac");district_list.add("Pucusana");district_list.add("Pueblo Libre");district_list.add("Puente Piedra");
        district_list.add("Punta Hermosa");district_list.add("Punta Negra");district_list.add("Rímac");district_list.add("San Bartolo");
        district_list.add("San Borja");district_list.add("San Isidro");district_list.add("San Juan de Lurigancho");district_list.add("San Juan de Miraflores");
        district_list.add("San Luis");district_list.add("San Martín de Porres");district_list.add("San Miguel");district_list.add("Santa Anita");
        district_list.add("Santa Rosa");district_list.add("Santiago de Surco");district_list.add("Surquillo");district_list.add("Villa El Salvador");
        district_list.add("Villa María del Triunfo");

        district_listBarranca.add("Barranca");district_listBarranca.add("Paramonga");district_listBarranca.add("Pativilca");district_listBarranca.add("Supe");
        district_listBarranca.add("Supe Puerto");

        district_listCajatambo.add("Cajatambo");district_listCajatambo.add("Copa");district_listCajatambo.add("Gorgor");
        district_listCajatambo.add("Huancapón");district_listCajatambo.add("Manás");

        district_listCañete.add("Asia");district_listCañete.add("Calango");district_listCañete.add("Cerro Azul");district_listCañete.add("Chilca");
        district_listCañete.add("Coayllo");district_listCañete.add("Imperial");district_listCañete.add("Lunahuaná");district_listCañete.add("Mala");
        district_listCañete.add("Nuevo Imperial");district_listCañete.add("Pacarán");district_listCañete.add("Quilmaná");district_listCañete.add("San Antonio");
        district_listCañete.add("San Luis");district_listCañete.add("San Vicente de Cañete");district_listCañete.add("Santa Cruz de Flores");

        district_listCanta.add("Arahuay"); district_listCanta.add("Canta"); district_listCanta.add("Huamatanga"); district_listCanta.add("Huaros");
        district_listCanta.add("Lachaqui");district_listCanta.add("San Buenaventura"); district_listCanta.add("Santa Rosa de Quives");

        district_listHuaral.add("Atavillos Alto");district_listHuaral.add("Atavillos Bajo");district_listHuaral.add("Aucallama");district_listHuaral.add("Chancay");
        district_listHuaral.add("Huaral");district_listHuaral.add("Ihuarí");district_listHuaral.add("Lampián");district_listHuaral.add("Pacaraos");
        district_listHuaral.add("San Miguel de Acos");district_listHuaral.add("Santa Cruz de Andamarca");district_listHuaral.add("Sumbilca");
        district_listHuaral.add("Veintisiete de Noviembre");

        district_listHuarochirí.add("Antioquía"); district_listHuarochirí.add("Callahuanca"); district_listHuarochirí.add("Carampoma"); district_listHuarochirí.add("Chicla");
        district_listHuarochirí.add("Cuenca");district_listHuarochirí.add("Huachupampa"); district_listHuarochirí.add("Huanza"); district_listHuarochirí.add("Huarochirí");
        district_listHuarochirí.add("Lahuaytambo");district_listHuarochirí.add("Langa");district_listHuarochirí.add("Mariatana"); district_listHuarochirí.add("Matucana");
        district_listHuarochirí.add("Ricardo Palma");district_listHuarochirí.add("San Andrés de Tupicocha"); district_listHuarochirí.add("San Antonio de Chaclla");
        district_listHuarochirí.add("San Bartolomé"); district_listHuarochirí.add("San Damián"); district_listHuarochirí.add("San Juan de Iris");
        district_listHuarochirí.add("San Juan de Tantaranche"); district_listHuarochirí.add("San Lorenzo de Quinti");district_listHuarochirí.add("San Mateo");
        district_listHuarochirí.add("San Mateo de Otao"); district_listHuarochirí.add("San Pedro de Casta");district_listHuarochirí.add("San Pedro de Huancayre");
        district_listHuarochirí.add("Sangallaya");district_listHuarochirí.add("Santa Cruz de Cocachacra"); district_listHuarochirí.add("Santa Eulalia");
        district_listHuarochirí.add("Santiago de Anchucaya");district_listHuarochirí.add("Santiago de Tuna"); district_listHuarochirí.add("Santo Domingo de los Olleros");
        district_listHuarochirí.add("Surco");

        district_listHuaura.add("Ámbar");district_listHuaura.add("Caleta de Carquín");district_listHuaura.add("Checras");
        district_listHuaura.add("Huacho");district_listHuaura.add("Hualmay");district_listHuaura.add("Huaura");district_listHuaura.add("Leoncio Prado");
        district_listHuaura.add("Paccho");district_listHuaura.add("Santa Leonor");district_listHuaura.add("Santa María");district_listHuaura.add("Sayán");
        district_listHuaura.add("Végueta");

        district_listOyon.add("Andajes"); district_listOyon.add("Caujul"); district_listOyon.add("Cochamarca"); district_listOyon.add("Naván");
        district_listOyon.add("Oyón"); district_listOyon.add("Pachangara");

        district_listYauyos.add("Alis");district_listYauyos.add("Ayauca");district_listYauyos.add("Ayaviri");district_listYauyos.add("Azángaro");district_listYauyos.add("Cacra");
        district_listYauyos.add("Carania");district_listYauyos.add("Catahuasi");district_listYauyos.add("Chocos");district_listYauyos.add("Cochas");district_listYauyos.add("Colonia");
        district_listYauyos.add("Hongos");district_listYauyos.add("Huampará");district_listYauyos.add("Huancaya");district_listYauyos.add("Huangáscar");district_listYauyos.add("Huañec");
        district_listYauyos.add("Huantán");district_listYauyos.add("Laraos");district_listYauyos.add("Lincha");district_listYauyos.add("Madeán");district_listYauyos.add("Miraflores");
        district_listYauyos.add("Omas");district_listYauyos.add("Putinza");district_listYauyos.add("Quinches");district_listYauyos.add("Quinocay");district_listYauyos.add("San Joaquín");
        district_listYauyos.add("San Pedro de Pilas");district_listYauyos.add("Tanta");district_listYauyos.add("Tauripampa");district_listYauyos.add("Tomas");district_listYauyos.add("Tupe");
        district_listYauyos.add("Viñac");district_listYauyos.add("Vitis");district_listYauyos.add("Yauyos");

        district_listCallao.add("Bellavista");district_listCallao.add("Callao");district_listCallao.add("Carmen de La Legua-Reynoso");district_listCallao.add("La Perla");
        district_listCallao.add("La Punta");district_listCallao.add("Mi Perú");district_listCallao.add("Ventanilla");

        btnDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnProvince.getText().toString().equals("Lima"))
                {
                    spinnerdistrict_list.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Barranca"))
                {
                    spinnerdistrict_listBarranca.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Cajatambo"))
                {
                    spinnerdistrict_listCajatambo.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Cañete"))
                {
                    spinnerdistrict_listCañete.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Canta"))
                {
                    spinnerdistrict_listCanta.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Huaral"))
                {
                    spinnerdistrict_listHuaral.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Huarochirí"))
                {
                    spinnerdistrict_listHuarochirí.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Huaura"))
                {
                    spinnerdistrict_listHuarochirí.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Oyón"))
                {
                    spinnerdistrict_listOyon.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Yauyos"))
                {
                    spinnerdistrict_listYauyos.showSpinerDialog();
                }
                if (btnProvince.getText().toString().equals("Callao"))
                {
                    spinnerdistrict_listCallao.showSpinerDialog();
                }
                if (TextUtils.isEmpty(btnProvince.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una provincia primero", Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
        });

        spinnerdistrict_list = new SpinnerDialog(RegisterInfoActivity.this,district_list,"Selecciona tu Distrito");
        spinnerdistrict_list.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listBarranca = new SpinnerDialog(RegisterInfoActivity.this,district_listBarranca,"Selecciona tu Distrito");
        spinnerdistrict_listBarranca.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCajatambo = new SpinnerDialog(RegisterInfoActivity.this,district_listCajatambo,"Selecciona tu Distrito");
        spinnerdistrict_listCajatambo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCañete = new SpinnerDialog(RegisterInfoActivity.this,district_listCañete,"Selecciona tu Distrito");
        spinnerdistrict_listCañete.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCanta = new SpinnerDialog(RegisterInfoActivity.this,district_listCanta,"Selecciona tu Distrito");
        spinnerdistrict_listCanta.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listHuaral = new SpinnerDialog(RegisterInfoActivity.this,district_listHuaral,"Selecciona tu Distrito");
        spinnerdistrict_listHuaral.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listHuarochirí = new SpinnerDialog(RegisterInfoActivity.this,district_listHuarochirí,"Selecciona tu Distrito");
        spinnerdistrict_listHuarochirí.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listHuaura = new SpinnerDialog(RegisterInfoActivity.this,district_listHuaura,"Selecciona tu Distrito");
        spinnerdistrict_listHuaura.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listOyon = new SpinnerDialog(RegisterInfoActivity.this,district_listOyon,"Selecciona tu Distrito");
        spinnerdistrict_listOyon.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listYauyos = new SpinnerDialog(RegisterInfoActivity.this,district_listYauyos,"Selecciona tu Distrito");
        spinnerdistrict_listYauyos.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerdistrict_listCallao = new SpinnerDialog(RegisterInfoActivity.this,district_listCallao,"Selecciona tu Distrito");
        spinnerdistrict_listCallao.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnDistrict.setText(item);
                Toast.makeText(RegisterInfoActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(RegisterInfoActivity.this).load(image).fit().into(btnImageProfile);
                        txtImageVerification.setText("Imágen cargada con éxito");
                    }
                    else
                    {
                        Toast.makeText(RegisterInfoActivity.this, "Usted debe seleccionar una imágen", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserInformation();
            }
        });

    }

    private void registerUserInformation() {
        if (rdMan.isChecked())
        {
            gender = "Hombre";
        }
        if (rdWoman.isChecked())
        {
            gender = "Mujer";
        }
        if (TextUtils.isEmpty(txtImageVerification.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes subir una foto de perfil", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtName.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar tu nombre", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtSurname.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar tus apellidos", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnDocumentType.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el tipo de documento de identidad", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtDocumentNumber.getText().toString())) {
            Snackbar.make(rootLayout, "Debes ingresar el número de tu documento de identidad", Snackbar.LENGTH_LONG).show();
            return;
        } else {
            Query query =  userRef.orderByChild("document_number").equalTo(edtDocumentNumber.getText().toString());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        String user_id = id.getKey();
                        userRef.child(Objects.requireNonNull(user_id)).child("document_number").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(edtDocumentNumber.getText().toString())){
                                    dni_exist = "true";

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
        if (dni_exist.equals("true")) {
            Snackbar.make(rootLayout, "Este número de documento (DNI) ya fue registrado por otro usuario de Oliver Bank.", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!rdMan.isChecked() && !rdWoman.isChecked())
        {
            Snackbar.make(rootLayout, "Debes especificar tu género o sexo", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtBthDay.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el día de tu nacimiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtBthMonth.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el mes de tu nacimiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtBthYear.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el año de tu nacimiento", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtEmail.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar tu correo electrónico", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnDepartment.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el departamento donde vives", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnDistrict.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el distrito donde vives", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtAddress.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar tu dirección exacta", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnOccupation.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar tu ocupación", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnAcademicDegree.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar tu grado académico", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtUsername.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar un nombre de usuario", Snackbar.LENGTH_LONG).show();
            return;
        }
        else
        {
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
                                    username_exist = "true";

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
        if (username_exist.equals("true")) {
            Snackbar.make(rootLayout, "El nombre de usuario "+edtUsername.getText().toString()+" ya existe, debes crear otro.", Snackbar.LENGTH_LONG).show();
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
        if (!checkDataManagement.isChecked())
        {
            Snackbar.make(rootLayout, "Debes confirmar que estás de acuerdo con las políticas, términos y condiciones", Snackbar.LENGTH_LONG).show();
            return;
        }
        else
        {
            loadingBar.setTitle("Finalizando Registro");
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

            postRandomName = saveCurrentDate+saveCurrentTime;

            generateQrCode();
            saveQrCode();
            sendQrCodeToDatabase();

        }
    }

    private void sendQrCodeToDatabase() {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(RegisterInfoActivity.this.getContentResolver(), bitmap, "Title", null);
        uri = Uri.parse(path);

        StorageReference filePath = userProfileImageRef.child(uri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    downloadUrlQr = task.getResult().getDownloadUrl().toString();

                    HashMap userMap = new HashMap();
                    userMap.put("name",edtName.getText().toString());
                    userMap.put("surname",edtSurname.getText().toString());
                    userMap.put("fullname",edtName.getText().toString()+" "+edtSurname.getText().toString());
                    userMap.put("document_type",btnDocumentType.getText().toString());
                    userMap.put("document_number",edtDocumentNumber.getText().toString());
                    userMap.put("gender",gender);
                    userMap.put("bth_day",edtBthDay.getText().toString());
                    userMap.put("bth_month",edtBthMonth.getText().toString());
                    userMap.put("bth_year",edtBthYear.getText().toString());
                    userMap.put("nacionality",ccpCNationalityountry.getSelectedCountryName());
                    userMap.put("email",edtEmail.getText().toString());
                    userMap.put("department",btnDepartment.getText().toString());
                    userMap.put("district",btnDistrict.getText().toString());
                    userMap.put("address",edtAddress.getText().toString());
                    userMap.put("occupation",btnOccupation.getText().toString());
                    userMap.put("academic_degree",btnAcademicDegree.getText().toString());
                    userMap.put("username",edtUsername.getText().toString());
                    userMap.put("user_verification",userVerification);
                    userMap.put("pin",edtPin.getText().toString());
                    userMap.put("document_verification","none");
                    userMap.put("basic_account_pen","0.00");
                    userMap.put("basic_account_usd","0.00");
                    userMap.put("state","offline");

                    userMap.put("credit_line_pen","false");
                    userMap.put("credit_line_pen_available","0.00");
                    userMap.put("credit_line_pen_default_day1","false");
                    userMap.put("credit_line_pen_default_day2","false");
                    userMap.put("credit_line_pen_default_day3","false");
                    userMap.put("credit_line_pen_default_day4","false");
                    userMap.put("credit_line_pen_default_day5","false");
                    userMap.put("credit_line_pen_default_day6","false");
                    userMap.put("credit_line_pen_default_day7","false");
                    userMap.put("credit_line_pen_default_day8","false");
                    userMap.put("credit_line_pen_default_day9","false");
                    userMap.put("credit_line_pen_default_day10","false");
                    userMap.put("credit_line_pen_payment_month1","0.00");
                    userMap.put("credit_line_pen_payment_month2","0.00");
                    userMap.put("credit_line_pen_payment_month3","0.00");
                    userMap.put("credit_line_pen_payment_month4","0.00");
                    userMap.put("credit_line_pen_payment_month5","0.00");
                    userMap.put("credit_line_pen_payment_month6","0.00");
                    userMap.put("credit_line_pen_payment_month7","0.00");
                    userMap.put("credit_line_pen_payment_month8","0.00");
                    userMap.put("credit_line_pen_payment_month9","0.00");
                    userMap.put("credit_line_pen_payment_month10","0.00");
                    userMap.put("credit_line_pen_payment_month11","0.00");
                    userMap.put("credit_line_pen_payment_month12","0.00");
                    userMap.put("credit_line_pen_tcea","800.00");
                    userMap.put("credit_line_pen_total","0.00");
                    userMap.put("credit_line_pen_used","0.00");
                    userMap.put("credit_line_pen_was_used","false");
                    userMap.put("credit_line_usd","false");
                    userMap.put("credit_line_usd_available","0.00");
                    userMap.put("credit_line_usd_default_day1","false");
                    userMap.put("credit_line_usd_default_day2","false");
                    userMap.put("credit_line_usd_default_day3","false");
                    userMap.put("credit_line_usd_default_day4","false");
                    userMap.put("credit_line_usd_default_day5","false");
                    userMap.put("credit_line_usd_default_day6","false");
                    userMap.put("credit_line_usd_default_day7","false");
                    userMap.put("credit_line_usd_default_day8","false");
                    userMap.put("credit_line_usd_default_day9","false");
                    userMap.put("credit_line_usd_default_day10","false");
                    userMap.put("credit_line_usd_payment_month1","0.00");
                    userMap.put("credit_line_usd_payment_month2","0.00");
                    userMap.put("credit_line_usd_payment_month3","0.00");
                    userMap.put("credit_line_usd_payment_month4","0.00");
                    userMap.put("credit_line_usd_payment_month5","0.00");
                    userMap.put("credit_line_usd_payment_month6","0.00");
                    userMap.put("credit_line_usd_payment_month7","0.00");
                    userMap.put("credit_line_usd_payment_month8","0.00");
                    userMap.put("credit_line_usd_payment_month9","0.00");
                    userMap.put("credit_line_usd_payment_month10","0.00");
                    userMap.put("credit_line_usd_payment_month11","0.00");
                    userMap.put("credit_line_usd_payment_month12","0.00");
                    userMap.put("credit_line_pen_payment_year1",0);
                    userMap.put("credit_line_pen_payment_year2",0);
                    userMap.put("credit_line_pen_payment_year3",0);
                    userMap.put("credit_line_pen_payment_year4",0);
                    userMap.put("credit_line_pen_payment_year5",0);
                    userMap.put("credit_line_pen_payment_year6",0);
                    userMap.put("credit_line_pen_payment_year7",0);
                    userMap.put("credit_line_pen_payment_year8",0);
                    userMap.put("credit_line_pen_payment_year9",0);
                    userMap.put("credit_line_pen_payment_year10",0);
                    userMap.put("credit_line_pen_payment_year11",0);
                    userMap.put("credit_line_pen_payment_year12",0);
                    userMap.put("credit_line_usd_payment_year1",0);
                    userMap.put("credit_line_usd_payment_year2",0);
                    userMap.put("credit_line_usd_payment_year3",0);
                    userMap.put("credit_line_usd_payment_year4",0);
                    userMap.put("credit_line_usd_payment_year5",0);
                    userMap.put("credit_line_usd_payment_year6",0);
                    userMap.put("credit_line_usd_payment_year7",0);
                    userMap.put("credit_line_usd_payment_year8",0);
                    userMap.put("credit_line_usd_payment_year9",0);
                    userMap.put("credit_line_usd_payment_year10",0);
                    userMap.put("credit_line_usd_payment_year11",0);
                    userMap.put("credit_line_usd_payment_year12",0);
                    userMap.put("credit_line_usd_tcea","800.00");
                    userMap.put("credit_line_usd_total","0.00");
                    userMap.put("credit_line_usd_used","0.00");
                    userMap.put("credit_line_usd_was_used","false");
                    userMap.put("credit_risk_param","0.00");
                    userMap.put("credit_score","5");
                    userMap.put("credit_line_pen_request_state","false");
                    userMap.put("credit_line_usd_request_state","false");
                    userMap.put("qr_code_image",downloadUrlQr);

                    userMap.put("daily_claim_pen_account","false");
                    userMap.put("daily_claim_usd_account","false");
                    userMap.put("pen_accoount_is_enabled","true");
                    userMap.put("usd_accoount_is_enabled","true");

                    userMap.put("user_is_enabled","true");

                    userMap.put("phone_number",phoneNumber);


                    userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                sendUserToMainActivity();
                                Toast.makeText(RegisterInfoActivity.this, "REGISTRO COMPLETO", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterInfoActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(RegisterInfoActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
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

    private void sendUserToMainActivity() {
        Intent intent = new Intent(RegisterInfoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick&&resultCode==RESULT_OK&&data!=null)
        {
            //Uri imageUri = data.getData();

            Uri resultUri = data.getData();

            loadingBar.setTitle("Foto de perfil");
            loadingBar.setMessage("Cargando...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");

            filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(RegisterInfoActivity.this, "Image cargada con éxito", Toast.LENGTH_SHORT).show();
                        Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                userRef.child("profileimage").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(RegisterInfoActivity.this, "Imágen subida con éxito", Toast.LENGTH_SHORT).show();
                                                    txtImageVerification.setText("Imágen Cargada con éxito");
                                                    loadingBar.dismiss();
                                                }
                                                else
                                                {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(RegisterInfoActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }
            });


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "AL RECHAZAR LOS PERMISOS ALGUNAS FUNCIONES NO ESTARÁN DISPONIBLES", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == MY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "AL RECHAZAR LOS PERMISOS ALGUNAS FUNCIONES NO ESTARÁN DISPONIBLES", Toast.LENGTH_LONG).show();
            }
        }
    }
}
