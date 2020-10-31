package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Locations.PeruLocationsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class AddPersonalBusinessActivity extends AppCompatActivity {

    ImageButton btnCompanyImage;
    EditText edtName,edtAddress,edtNumberOfWorkers;
    Button btnBusinessLine,btnDepartment,btnDistrict,btnRegister,edtBthDay,edtBthMonth,edtBthYear,btnProvince;
    CheckBox checkDataTrue,checkDataManagement;
    TextView txtImageVerification;
    RelativeLayout rootLayout;
    final static int Gallery_Pick = 1;
    final static int Gallery_Pick2 = 2;
    final static int Gallery_Pick3 = 3;
    private Uri imageUri,imageUri2,imageUri3;
    private StorageReference postImagesReference;
    private DatabaseReference userRef, postRef,ratesRef,oliverBankRef,peruLocations;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, currentUserID,user_id, ruc_exist,user_dni,my_company_number,
            add_company_fee_unit,basic_account_pen,add_company_fee_state,document_number;
    private long countPost = 0;
    int my_companies;
    double add_company_fee, add_fee_amount,basic_account_pen_db,add_company_fee_state_db;
    DecimalFormat decimalFormat;
    RecyclerView recyclerView;
    EditText edtSearch;
    AlertDialog departmentDialog;
    String province_code;

    String company_id;
    String TAG = "GenerateQRCode",downloadUrlQr;
    ImageView qrImage;
    String edtValue;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    Uri uri;
    Context inContext;
    Button btnFinish;

    ArrayList<String> bussinessLine =new ArrayList<>();
    SpinnerDialog spinnerActivity_list;

    ArrayList<String> bthDay =new ArrayList<>();
    SpinnerDialog bthDayDialog;

    ArrayList<String> bthMonth =new ArrayList<>();
    SpinnerDialog bthMonthDialog;

    ArrayList<String> bthYear =new ArrayList<>();
    SpinnerDialog bthYearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_business);

        btnCompanyImage= findViewById(R.id.btnCompanyImage);
        edtName= findViewById(R.id.edtName);
        edtAddress= findViewById(R.id.edtAddress);
        edtBthDay= findViewById(R.id.edtBthDay);
        edtBthMonth= findViewById(R.id.edtBthMonth);
        edtBthYear= findViewById(R.id.edtBthYear);
        edtNumberOfWorkers= findViewById(R.id.edtNumberOfWorkers);
        btnBusinessLine= findViewById(R.id.btnBusinessLine);
        btnDepartment= findViewById(R.id.btnDepartment);
        btnDistrict= findViewById(R.id.btnDistrict);
        btnRegister= findViewById(R.id.btnRegister);
        checkDataTrue= findViewById(R.id.checkDataTrue);
        checkDataManagement= findViewById(R.id.checkDataManagement);
        rootLayout = findViewById(R.id.rootLayout);
        txtImageVerification = findViewById(R.id.txtImageVerification);
        qrImage = findViewById(R.id.qrCode);
        btnProvince = findViewById(R.id.btnProvince);
        loadingBar = new ProgressDialog(this);
        decimalFormat = new DecimalFormat("0.00");

        postImagesReference = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        postRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");
        peruLocations= FirebaseDatabase.getInstance().getReference().child("Peru Locations");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_dni = dataSnapshot.child("document_number").getValue().toString();
                my_company_number = dataSnapshot.child("my_company_number").getValue().toString();
                basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                document_number = dataSnapshot.child("document_number").getValue().toString();

                my_companies = Integer.parseInt(my_company_number);

                basic_account_pen_db = Double.parseDouble(basic_account_pen);

                ratesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            add_company_fee_unit = dataSnapshot.child("add_company_fee_unit").getValue().toString();
                            add_company_fee = Double.parseDouble(add_company_fee_unit);

                            oliverBankRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        add_company_fee_state = dataSnapshot.child("Add Company Fee PEN").getValue().toString();

                                        add_company_fee_state_db = Double.parseDouble(add_company_fee_state);
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bussinessLine.add("Producción Agropecuaria");bussinessLine.add("Servicios Agrícolas");bussinessLine.add("Caza");bussinessLine.add("Silvicultura");
        bussinessLine.add("Pesca");bussinessLine.add("Minas, Petróleo y Canteras");bussinessLine.add("Producción de Productos Alimenticios");
        bussinessLine.add("Producción de Bebidas");bussinessLine.add("Producción de Tabaco");bussinessLine.add("Producción Textil");
        bussinessLine.add("Confección de Prendas de Vestir");bussinessLine.add("Producción de Cuero");bussinessLine.add("Producción de Productos de Madera y Corcho");
        bussinessLine.add("Producción de Papel");bussinessLine.add("Imprenta y Editoriales");bussinessLine.add("Producción de Substancias Químicas Industriales");bussinessLine.add("Fabricación de Prótesis");
        bussinessLine.add("Producción de Productos Derivados del Petróleo o Carbón");bussinessLine.add("Producción de Productos de Caucho");bussinessLine.add("Producción de Vidrio, Loza o Porcelan");
        bussinessLine.add("Producción de Productos Metálicos Básicos");bussinessLine.add("Construcción de Maquinarias");bussinessLine.add("Producción de Aparatos, Accesorios y Suministros Eléctricos");
        bussinessLine.add("Construcción de Material de Transporte");bussinessLine.add("Fabricación de Equipo Profesional y Científico");bussinessLine.add("Producción de Joyas");
        bussinessLine.add("Producción y Distribución de Gas o Agua");bussinessLine.add("Construcción de Inmuebles");bussinessLine.add("Comercio por Mayor de Productos Agrícolas");
        bussinessLine.add("Comercio por Mayor de Minerales");bussinessLine.add("Comercio por Mayor de Productos Alimenticios");bussinessLine.add("Comercio por Mayor de Bebidas");
        bussinessLine.add("Comercio por Mayor de Tabaco");bussinessLine.add("Comercio por Mayor de Textiles");bussinessLine.add("Comercio por Mayor de Madera");bussinessLine.add("Fabricación de Medicinas");
        bussinessLine.add("Comercio por Mayor de Papel");bussinessLine.add("Comercio por Mayor de Substancias Químicas");bussinessLine.add("Comercio por Mayor de Porcelana, Loza o Vidrio");
        bussinessLine.add("Comercio por Mayor de Productos Metálicos");bussinessLine.add("Comercio por Menor de Alimentos y Bebidas");bussinessLine.add("Comercio por Menor de Tabaco y Utencilios para Fumar");
        bussinessLine.add("Comercio por Menor de Papel y Artículos de Librería");bussinessLine.add("Comercio por Menor de Joyería y Bisutería");bussinessLine.add("Comercio por Menor de Medicinas");
        bussinessLine.add("Restaurantes");bussinessLine.add("Salones de té");bussinessLine.add("Servicios de Catering");bussinessLine.add("Hotelería");bussinessLine.add("Transporte");
        bussinessLine.add("Almacénes y Depósitos");bussinessLine.add("Agencia de Turismo");bussinessLine.add("Banco");bussinessLine.add("Financieras");bussinessLine.add("Sociedades de Inversión y Administración de Fondos");
        bussinessLine.add("Casas de Cambio");bussinessLine.add("Sociedades Agente de Bolsa");bussinessLine.add("Compañía de Seguros y Reaseguros");bussinessLine.add("Arrendamiento de Activos");
        bussinessLine.add("Corredores de Inmuebles");bussinessLine.add("Servicios Jurídicos");bussinessLine.add("Notarias");bussinessLine.add("Desarrollo de Software");
        bussinessLine.add("Agencia de Marketing y Publicidad");bussinessLine.add("Agencia de Empleos");bussinessLine.add("Institución Educativa Primaria y Secundaria");
        bussinessLine.add("Institución Educativa Superior");bussinessLine.add("Escuelas de Arte");bussinessLine.add("Institutos de investigación");bussinessLine.add("Hospitales");
        bussinessLine.add("Clínicas");bussinessLine.add("Servicios Odontológicos");bussinessLine.add("Servicios de Podología");bussinessLine.add("Veterinarias");
        bussinessLine.add("Centros de Belleza y Relajación");bussinessLine.add("Producción de Películas Cinematográficas");bussinessLine.add("Producción de Obras Literarias");
        bussinessLine.add("Producción de obras teatralas");bussinessLine.add("Reparaciones");bussinessLine.add("Vida Nocturna");bussinessLine.add("Comercialización de Armas de Fuego");
        bussinessLine.add("Producción de Hierbas Medicinales");bussinessLine.add("Comercio de Drogas");bussinessLine.add("Comercio de Intangibles Tecnológicos");bussinessLine.add("Conservación del Medio Abiente");

        btnBusinessLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerActivity_list.showSpinerDialog();
            }
        });

        spinnerActivity_list = new SpinnerDialog(AddPersonalBusinessActivity.this,bussinessLine,"Selecciona la Actividad Económica");
        spinnerActivity_list.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnBusinessLine.setText(item);
                Toast.makeText(AddPersonalBusinessActivity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
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

        bthDayDialog = new SpinnerDialog(AddPersonalBusinessActivity.this,bthDay, "Selecciona el Día");
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

        bthMonthDialog = new SpinnerDialog(AddPersonalBusinessActivity.this,bthMonth, "Selecciona el Mes");
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

        bthYearDialog = new SpinnerDialog(AddPersonalBusinessActivity.this,bthYear, "Selecciona el Año");
        bthYearDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthYear.setText(item2);
            }
        });

        btnCompanyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCompanyImage();
            }
        });

        btnDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDepartmentDilog();
            }
        });

        btnProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(btnDepartment.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar un departamento o región primero", Snackbar.LENGTH_LONG).show();
                    return;

                } else if (TextUtils.isEmpty(btnDepartment.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar un departamento o región primero", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    showProvinceDialog();
                }
            }
        });


        btnDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(btnDepartment.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar un departamento o región primero", Snackbar.LENGTH_LONG).show();
                    return;

                } else if (TextUtils.isEmpty(btnProvince.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar una provincia o región primero", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(btnProvince.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar una provincia o región primero", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    showDistrictDialog();
                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCompany();
            }
        });
    }

    private void registerCompany() {
        if (TextUtils.isEmpty(txtImageVerification.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes subir una imagen que represente a tu compañía", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtName.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el nombre comercial de tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnBusinessLine.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar la actividad económica", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnDepartment.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el departamento donde está ubicada tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(btnDistrict.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes seleccionar el distrito donde está ubicada tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtAddress.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar la dirección exacta de tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtBthDay.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el día de la constitución de tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtBthMonth.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el mes de la constitución de tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtBthYear.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el año de la constitución de tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(edtNumberOfWorkers.getText().toString()))
        {
            Snackbar.make(rootLayout, "Debes ingresar el número de trabajadores en tu empresa", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!checkDataManagement.isChecked())
        {
            Snackbar.make(rootLayout, "Debes confirmar que estás de acuerdo con las políticas, términos y condiciones", Snackbar.LENGTH_LONG).show();
            return;
        }
        else
        {
            if (my_companies > 0) {
                showConfirmPaymentDialog();
            } else if (my_companies == 0) {

                storingImageToFirebaseStorage();
            }


        }
    }

    private void showDistrictDialog() {
        departmentDialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.departments_locations_dialog,null);

        edtSearch = finance_method.findViewById(R.id.edtSearch);
        recyclerView = finance_method.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        showDistrictLocations();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchDistricts();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        departmentDialog.setView(finance_method);
        departmentDialog.show();
    }

    private void searchDistricts() {
        Query query = peruLocations.child("Districs").child(province_code).orderByChild("nombre_ubigeo").startAt(edtSearch.getText().toString()).endAt(edtSearch.getText().toString()+"\uf8ff");
        FirebaseRecyclerAdapter<PeruLocationsModel,LocationsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PeruLocationsModel, LocationsViewHolder>
                (PeruLocationsModel.class, R.layout.location_item, LocationsViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final LocationsViewHolder viewHolder, PeruLocationsModel model, final int position) {
                viewHolder.setNombre_ubigeo(model.getNombre_ubigeo());
                viewHolder.setId_ubigeo(model.getId_ubigeo());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnDistrict.setText(viewHolder.location_name);
                        departmentDialog.dismiss();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDistrictLocations() {
        Query query = peruLocations.child("Districs").child(province_code).orderByChild("nombre_ubigeo");
        FirebaseRecyclerAdapter<PeruLocationsModel,LocationsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PeruLocationsModel, LocationsViewHolder>
                (PeruLocationsModel.class, R.layout.location_item, LocationsViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final LocationsViewHolder viewHolder, PeruLocationsModel model, final int position) {
                viewHolder.setNombre_ubigeo(model.getNombre_ubigeo());
                viewHolder.setId_ubigeo(model.getId_ubigeo());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnDistrict.setText(viewHolder.location_name);
                        departmentDialog.dismiss();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showProvinceDialog() {
        departmentDialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.departments_locations_dialog,null);

        edtSearch = finance_method.findViewById(R.id.edtSearch);
        recyclerView = finance_method.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        showProvinceLocations();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchProvinces();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        departmentDialog.setView(finance_method);
        departmentDialog.show();

    }

    private void searchProvinces() {
        Query query = peruLocations.child("Provinces").child(province_code).orderByChild("nombre_ubigeo").startAt(edtSearch.getText().toString()).endAt(edtSearch.getText().toString()+"\uf8ff");
        FirebaseRecyclerAdapter<PeruLocationsModel,LocationsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PeruLocationsModel, LocationsViewHolder>
                (PeruLocationsModel.class, R.layout.location_item, LocationsViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final LocationsViewHolder viewHolder, PeruLocationsModel model, final int position) {
                viewHolder.setNombre_ubigeo(model.getNombre_ubigeo());
                viewHolder.setId_ubigeo(model.getId_ubigeo());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnProvince.setText(viewHolder.location_name);
                        btnDistrict.setText("");
                        province_code = viewHolder.id_location;
                        departmentDialog.dismiss();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showProvinceLocations() {
        Query query = peruLocations.child("Provinces").child(province_code).orderByChild("nombre_ubigeo");
        FirebaseRecyclerAdapter<PeruLocationsModel,LocationsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PeruLocationsModel, LocationsViewHolder>
                (PeruLocationsModel.class, R.layout.location_item, LocationsViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final LocationsViewHolder viewHolder, PeruLocationsModel model, final int position) {
                viewHolder.setNombre_ubigeo(model.getNombre_ubigeo());
                viewHolder.setId_ubigeo(model.getId_ubigeo());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnProvince.setText(viewHolder.location_name);
                        btnDistrict.setText("");
                        province_code = viewHolder.id_location;
                        departmentDialog.dismiss();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDepartmentDilog() {
        departmentDialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.departments_locations_dialog,null);

        edtSearch = finance_method.findViewById(R.id.edtSearch);
        recyclerView = finance_method.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        showLocations();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchDepartments();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        departmentDialog.setView(finance_method);
        departmentDialog.show();

    }

    private void searchDepartments() {
        Query query = peruLocations.child("Departments").orderByChild("nombre_ubigeo").startAt(edtSearch.getText().toString()).endAt(edtSearch.getText().toString()+"\uf8ff");
        FirebaseRecyclerAdapter<PeruLocationsModel,LocationsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PeruLocationsModel, LocationsViewHolder>
                (PeruLocationsModel.class, R.layout.location_item, LocationsViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final LocationsViewHolder viewHolder, PeruLocationsModel model, int position) {
                viewHolder.setNombre_ubigeo(model.getNombre_ubigeo());
                viewHolder.setId_ubigeo(model.getId_ubigeo());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnDepartment.setText(viewHolder.location_name);
                        btnProvince.setText("");
                        btnDistrict.setText("");
                        province_code = viewHolder.id_location;
                        departmentDialog.dismiss();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showLocations() {
        Query query = peruLocations.child("Departments").orderByChild("nombre_ubigeo");
        FirebaseRecyclerAdapter<PeruLocationsModel,LocationsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PeruLocationsModel, LocationsViewHolder>
                (PeruLocationsModel.class, R.layout.location_item, LocationsViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final LocationsViewHolder viewHolder, PeruLocationsModel model, final int position) {
                viewHolder.setNombre_ubigeo(model.getNombre_ubigeo());
                viewHolder.setId_ubigeo(model.getId_ubigeo());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnDepartment.setText(viewHolder.location_name);
                        btnProvince.setText("");
                        btnDistrict.setText("");
                        province_code = viewHolder.id_location;
                        departmentDialog.dismiss();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showConfirmPaymentDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.add_company_payment_dialog,null);

        add_fee_amount = add_company_fee*my_companies;
        String add_fee_amount_st = decimalFormat.format(add_fee_amount);


        final TextView txtAmount,txtMessage,txtPenBasicAccountAmmount;
        final Button btnPay;

        txtAmount = finance_method.findViewById(R.id.txtAmount);
        btnPay = finance_method.findViewById(R.id.btnPay);
        txtMessage = finance_method.findViewById(R.id.txtMessage);
        txtPenBasicAccountAmmount = finance_method.findViewById(R.id.txtPenBasicAccountAmmount);

        txtAmount.setText("S/"+add_fee_amount_st);
        txtPenBasicAccountAmmount.setText("S/"+basic_account_pen);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Verify if user has money
                if (basic_account_pen_db < add_fee_amount) {
                    txtMessage.setText("No cuentas con dinero suficiente en tu cuenta en soles");
                }
                if (basic_account_pen_db >= add_fee_amount) {

                    double my_pen_account_updated = basic_account_pen_db-add_fee_amount;
                    String my_pen_account_updated_st = decimalFormat.format(my_pen_account_updated);

                    double add_company_fee_state_updated = add_company_fee_state_db+add_fee_amount;
                    String add_company_fee_state_updated_st = decimalFormat.format(add_company_fee_state_updated);

                    btnPay.setText("Registrando Empresa, Cargando...");
                    btnPay.setEnabled(false);
                    btnPay.setBackgroundResource(R.drawable.edit_text_background1);

                    userRef.child(currentUserID).child("basic_account_pen").setValue(my_pen_account_updated_st);
                    oliverBankRef.child("Add Company Fee PEN").setValue(add_company_fee_state_updated_st);

                    dialog.dismiss();
                    storingImageToFirebaseStorage();


                }
            }

        });


        dialog.setView(finance_method);
        dialog.show();
    }

    private void storingImageToFirebaseStorage() {
        loadingBar.setTitle("Registrando empresa");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        StorageReference filePath = postImagesReference.child("Company Images").child(imageUri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    downloadUrl = task.getResult().getDownloadUrl().toString();
                    txtImageVerification.setText("Imágen cargada con éxito");
                    generateQrCode();
                    saveQrCode();
                    sendQrCodeToDatabase();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(AddPersonalBusinessActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendQrCodeToDatabase() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(AddPersonalBusinessActivity.this.getContentResolver(), bitmap, "Title", null);
        uri = Uri.parse(path);

        StorageReference filePath = postImagesReference.child("Company Images").child(uri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    downloadUrlQr = task.getResult().getDownloadUrl().toString();
                    savingPostInformationToDatabase();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(AddPersonalBusinessActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
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
        inputValue = currentUserID+postRandomName;
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
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void savingPostInformationToDatabase() {
        HashMap postMap = new HashMap();
        postMap.put("uid",currentUserID);
        postMap.put("date",saveCurrentDate);
        postMap.put("time",saveCurrentTime);
        postMap.put("company_image",downloadUrl);
        postMap.put("company_name",edtName.getText().toString().toUpperCase());
        postMap.put("company_social_reason","");
        postMap.put("company_ruc",user_dni);
        postMap.put("company_type","");
        postMap.put("company_line",btnBusinessLine.getText().toString());
        postMap.put("company_department",btnDepartment.getText().toString());
        postMap.put("company_province",btnProvince.getText().toString());
        postMap.put("company_district",btnDistrict.getText().toString());
        postMap.put("company_address",edtAddress.getText().toString());
        postMap.put("company_bth_day",edtBthDay.getText().toString());
        postMap.put("company_bth_month",edtBthMonth.getText().toString());
        postMap.put("company_bth_year",edtBthYear.getText().toString());
        postMap.put("company_workers",edtNumberOfWorkers.getText().toString());
        postMap.put("company_current_bank_account_image","");
        postMap.put("company_declaration_image","");
        postMap.put("company_verification","progress");
        postMap.put("current_account_pen","0.00");
        postMap.put("current_account_usd","0.00");
        postMap.put("pen_account_enabled","true");
        postMap.put("usd_account_enabled","true");
        postMap.put("company_condition","Comercio Personal");
        postMap.put("company_name_ruc",edtName.getText().toString()+user_dni);
        postMap.put("qr_code_image",downloadUrlQr);

        postMap.put("credit_line_pen","false");
        postMap.put("credit_line_pen_available","0.00");
        postMap.put("credit_line_pen_tcea","800.00");
        postMap.put("credit_line_pen_total","0.00");
        postMap.put("credit_line_pen_used","0.00");
        postMap.put("credit_line_usd","false");
        postMap.put("credit_line_usd_available","0.00");
        postMap.put("credit_line_usd_tcea","800.00");
        postMap.put("credit_line_usd_total","0.00");
        postMap.put("credit_line_usd_used","0.00");
        postMap.put("credit_risk_param","0.00");
        postMap.put("credit_score","5");
        postMap.put("credit_line_pen_request_state","false");
        postMap.put("credit_line_usd_request_state","false");

        postMap.put("pen_accoount_is_enabled","true");
        postMap.put("usd_accoount_is_enabled","true");

        postRef.child(currentUserID+postRandomName).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    double my_companies_updated = my_companies+1;
                    DecimalFormat decimalFormat2 = new DecimalFormat("0");
                    String my_companies_updated_st = decimalFormat2.format(my_companies_updated);
                    userRef.child(currentUserID).child("my_company_number").setValue(my_companies_updated_st);

                    sendUserToMyCompaniesActivity();
                    Toast.makeText(AddPersonalBusinessActivity.this, "La empresa "+edtName.getText().toString()+" fue añadida con éxito", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
                else
                {
                    Toast.makeText(AddPersonalBusinessActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void sendUserToMyCompaniesActivity() {

        Intent intent = new Intent(AddPersonalBusinessActivity.this, MyCompaniesActivity.class);
        intent.putExtra("company_id",currentUserID+postRandomName);
        startActivity(intent);
        finish();
    }

    private void uploadCompanyImage() {
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
            btnCompanyImage.setImageURI(imageUri);
            txtImageVerification.setText("Imágen cargada con éxito");
        }

    }

    public static class LocationsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        String id_location,location_name;

        public LocationsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNombre_ubigeo(String nombre_ubigeo) {
            TextView textView = mView.findViewById(R.id.txtName);
            textView.setText(nombre_ubigeo);
            location_name = nombre_ubigeo;
        }

        public void setId_ubigeo(String id_ubigeo) {
            id_location = id_ubigeo;
        }
    }
}
