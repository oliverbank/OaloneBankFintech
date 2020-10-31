package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DocumentVerificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID;
    private ProgressDialog loadingBar;
    final static int Gallery_Pick = 0;
    final static int Gallery_Pick1 = 1;
    private Uri imageUri,imageUri1;
    ImageButton dniImage1,dniImage2;
    private StorageReference postImagesReference;
    String image_verification1,image_verification2;
    Button btnSent;
    RelativeLayout rootLayout;
    private String saveCurrentDate, saveCurrentTime, postRandomName,downloadUrl,downloadUrl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_verification);

        postImagesReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        dniImage1 = findViewById(R.id.dniImage1);
        dniImage2 = findViewById(R.id.dniImage2);
        btnSent = findViewById(R.id.btnSent);
        rootLayout = findViewById(R.id.rootLayout);
        loadingBar = new ProgressDialog(this);
        
        dniImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery1();
            }
        });
        dniImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery2();
            }
        });
        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!image_verification1.equals("true"))
                {
                    Snackbar.make(rootLayout, "Debes subir la imágen delantera de tu DNI", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!image_verification2.equals("true"))
                {
                    Snackbar.make(rootLayout, "Debes subir la imágen trasera de tu DNI", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!image_verification2.equals("true"))
                {
                    Snackbar.make(rootLayout, "Debes subir la imágen trasera de tu DNI", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    loadingBar.setMessage("Cargando imágenes...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);
                    btnSent.setEnabled(false);
                    storingImageToFirebaseStorage();
                }
            }
        });

    }

    private void storingImageToFirebaseStorage() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;
        StorageReference filePath = postImagesReference.child("DNI Verification").child(imageUri.getLastPathSegment()+postRandomName+"1.jpg");
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    downloadUrl = task.getResult().getDownloadUrl().toString();

                    StorageReference filePath1 = postImagesReference.child("DNI Verification").child(imageUri1.getLastPathSegment()+postRandomName+"2.jpg");
                    filePath1.putFile(imageUri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                downloadUrl1 = task.getResult().getDownloadUrl().toString();
                                savingPostInformationToDatabase();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(DocumentVerificationActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(DocumentVerificationActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void savingPostInformationToDatabase() {

        userRef.child(currentUserID).child("dni_image1").setValue(downloadUrl);
        userRef.child(currentUserID).child("dni_image2").setValue(downloadUrl1);
        userRef.child(currentUserID).child("document_verification").setValue("in_progress");
        Intent intent = new Intent(DocumentVerificationActivity.this, MyAccountActivity.class);
        startActivity(intent);
        finish();
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
            dniImage1.setImageURI(imageUri);
            Picasso.with(this).load(imageUri).fit().centerInside().into(dniImage1);
            image_verification1 = "true";
        }
        if (requestCode==Gallery_Pick1&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri1 = data.getData();
            dniImage2.setImageURI(imageUri1);
            Picasso.with(this).load(imageUri1).fit().centerInside().into(dniImage2);
            image_verification2 = "true";
        }

    }
}
