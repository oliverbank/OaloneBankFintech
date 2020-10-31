package oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class RegisterInfo4Activity extends AppCompatActivity {

    Button btnOccupation,btnAcademicDegree;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID;
    private RelativeLayout rootLayout;
    Button btnNext;
    private ProgressDialog loadingBar;

    ArrayList<String> ocupations =new ArrayList<>();
    SpinnerDialog spinnerOcupation;

    ArrayList<String> academicDegrees =new ArrayList<>();
    SpinnerDialog spinnerAcademicDegrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info4);

        btnOccupation = findViewById(R.id.btnOccupation);
        btnAcademicDegree = findViewById(R.id.btnAcademicDegree);
        rootLayout = findViewById(R.id.rootLayout);
        btnNext = findViewById(R.id.btnNext);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        loadingBar = new ProgressDialog(this);

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

        spinnerOcupation = new SpinnerDialog(RegisterInfo4Activity.this,ocupations,"Selecciona tu Ocupación");
        spinnerOcupation.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnOccupation.setText(item);
                Toast.makeText(RegisterInfo4Activity.this, "Seleccionado: "+item, Toast.LENGTH_SHORT).show();
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

        spinnerAcademicDegrees = new SpinnerDialog(RegisterInfo4Activity.this,academicDegrees, "Selecciona tu Grado Académico");
        spinnerAcademicDegrees.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                btnAcademicDegree.setText(item2);
                Toast.makeText(RegisterInfo4Activity.this, "Seleccionado: "+item2, Toast.LENGTH_SHORT).show();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(btnOccupation.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar tu ocupación", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnAcademicDegree.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar tu grado académico", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnAcademicDegree.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar tu grado académico", Snackbar.LENGTH_LONG).show();
                    return;
                }else {
                    loadingBar.setTitle("Preparando todo...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    HashMap userMap = new HashMap();
                    userMap.put("occupation",btnOccupation.getText().toString());
                    userMap.put("academic_degree",btnAcademicDegree.getText().toString());
                    userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(RegisterInfo4Activity.this, RegisterInfo5Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loadingBar.dismiss();
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterInfo4Activity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
                }
            }
        });
    }
}
