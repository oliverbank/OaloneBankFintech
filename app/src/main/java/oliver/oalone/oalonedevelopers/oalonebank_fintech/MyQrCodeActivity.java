package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.QrCompanyCodeActivity;

public class MyQrCodeActivity extends AppCompatActivity {

    CircleImageView imageCompany;
    TextView txtCompanyName,txtCompanyVerification,txtMessage,txtUserName;
    ImageView qr_code_icon,imageCompanyVerification;
    String my_company_key,currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference myCompanyRef, userRef;
    private ProgressDialog loadingBar;
    String company_image,company_name,company_verification,qr_code_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);

        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        qr_code_icon = findViewById(R.id.qrImage);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);
        txtMessage = findViewById(R.id.txtMessage);
        txtUserName = findViewById(R.id.txtUserName);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        myCompanyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        myCompanyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    company_image = dataSnapshot.child("profileimage").getValue().toString();
                    company_name = dataSnapshot.child("username").getValue().toString();
                    company_verification = dataSnapshot.child("user_verification").getValue().toString();
                    qr_code_image = dataSnapshot.child("qr_code_image").getValue().toString();

                    Picasso.with(MyQrCodeActivity.this).load(company_image).fit().centerCrop().into(imageCompany);
                    Picasso.with(MyQrCodeActivity.this).load(qr_code_image).fit().centerCrop().into(qr_code_icon);
                    txtCompanyName.setText(company_name);
                    txtUserName.setText(company_name);
                    txtMessage.setText("Escanea el Código QR para PAGAR a "+company_name);

                    if (company_verification.equals("true"))
                    {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                        txtCompanyVerification.setText("Verificación: Verificado");
                    }
                    if (company_verification.equals("false"))
                    {
                        imageCompanyVerification.setImageResource(R.drawable.error_icon);
                        txtCompanyVerification.setText("Verificación: Denegado");
                    }
                    if (company_verification.equals("progress"))
                    {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                        txtCompanyVerification.setText("Verificación: En Proceso");
                    }
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
