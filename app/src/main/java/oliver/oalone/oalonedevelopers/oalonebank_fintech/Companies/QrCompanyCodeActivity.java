package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class QrCompanyCodeActivity extends AppCompatActivity {

    CircleImageView imageCompany;
    TextView txtCompanyName,txtCompanyVerification,txtMessage,txtBusinessCondition,txtUserName;
    ImageView qr_code_icon,imageCompanyVerification;
    String my_company_key;
    private FirebaseAuth mAuth;
    private DatabaseReference myCompanyRef, userRef;
    private ProgressDialog loadingBar;
    String company_image,company_name,company_verification,qr_code_image,company_condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_company_code);

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
        txtBusinessCondition = findViewById(R.id.txtBusinessCondition);
        txtUserName = findViewById(R.id.txtUserName);
        my_company_key = getIntent().getExtras().get("my_company_key").toString();

        mAuth = FirebaseAuth.getInstance();
        myCompanyRef = FirebaseDatabase.getInstance().getReference().child("My Companies").child(my_company_key);

        myCompanyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    company_image = dataSnapshot.child("company_image").getValue().toString();
                    company_name = dataSnapshot.child("company_name").getValue().toString();
                    company_verification = dataSnapshot.child("company_verification").getValue().toString();
                    qr_code_image = dataSnapshot.child("qr_code_image").getValue().toString();
                    company_condition = dataSnapshot.child("company_condition").getValue().toString();

                    Picasso.with(QrCompanyCodeActivity.this).load(company_image).fit().centerCrop().into(imageCompany);
                    Picasso.with(QrCompanyCodeActivity.this).load(qr_code_image).fit().into(qr_code_icon);
                    txtCompanyName.setText(company_name);
                    txtMessage.setText("Escanea el Código QR para PAGAR a "+company_name);
                    txtBusinessCondition.setText(company_condition);
                    txtUserName.setText(company_name);

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
