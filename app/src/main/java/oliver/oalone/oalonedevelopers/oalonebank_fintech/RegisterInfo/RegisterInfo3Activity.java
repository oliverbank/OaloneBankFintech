package oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Locations.PeruLocationsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class RegisterInfo3Activity extends AppCompatActivity {

    EditText edtEmail,edtAddress;
    Button btnDepartment,btnDistrict,btnProvince,btnNext;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,peruLocations;
    String currentUserID, province_code;
    private RelativeLayout rootLayout;
    private ProgressDialog loadingBar;
    RecyclerView recyclerView;
    EditText edtSearch;
    AlertDialog departmentDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info3);

        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        btnDepartment = findViewById(R.id.btnDepartment);
        btnProvince = findViewById(R.id.btnProvince);
        btnDistrict = findViewById(R.id.btnDistrict);
        rootLayout = findViewById(R.id.rootLayout);
        btnNext = findViewById(R.id.btnNext);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        peruLocations= FirebaseDatabase.getInstance().getReference().child("Peru Locations");


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (TextUtils.isEmpty(edtAddress.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu dirección exacta", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAddress.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu dirección exacta", Snackbar.LENGTH_LONG).show();
                    return;
                }else {
                    loadingBar.setTitle("Preparando todo...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    HashMap userMap = new HashMap();
                    userMap.put("email",edtEmail.getText().toString());
                    userMap.put("email_marketing","true");
                    userMap.put("department",btnDepartment.getText().toString());
                    userMap.put("district",btnDistrict.getText().toString());
                    userMap.put("address",edtAddress.getText().toString());
                    userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(RegisterInfo3Activity.this, RegisterInfo4Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loadingBar.dismiss();
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterInfo3Activity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
                }

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
