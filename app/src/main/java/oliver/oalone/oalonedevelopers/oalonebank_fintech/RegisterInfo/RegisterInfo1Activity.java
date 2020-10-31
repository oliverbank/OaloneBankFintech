package oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.AddPersonalBusinessActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.QrCompanyPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.TransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WithdrawalActivity;

public class RegisterInfo1Activity extends AppCompatActivity {

    ImageButton btnImageProfile;
    TextView txtImageVerification;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID,downloadUrl;
    final static int Gallery_Pick = 1;
    final static int Gallery_Pick2 = 2;
    private StorageReference userProfileImageRef;
    private RelativeLayout rootLayout;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_STORAGE_REQUEST_CODE = 200;
    private Uri imageUri;
    Button btnNext;
    private ProgressDialog loadingBar;
    String mCameraFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info1);

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
        btnNext = findViewById(R.id.btnNext);
        rootLayout = findViewById(R.id.rootLayout);
        txtImageVerification = findViewById(R.id.txtImageVerification);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        loadingBar = new ProgressDialog(this);

        btnImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtImageVerification.getText().toString().equals("Imágen cargada con éxito")) {
                    Snackbar.make(rootLayout, "Debes cargar una foto de perfil correctamente", Snackbar.LENGTH_LONG).show();
                    return;

                }
                if (!txtImageVerification.getText().toString().equals("Imágen cargada con éxito")) {
                    Snackbar.make(rootLayout, "Debes cargar una foto de perfil correctamente", Snackbar.LENGTH_LONG).show();
                    return;

                }else {

                    loadingBar.setTitle("Preparando todo...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    StorageReference filePath = userProfileImageRef.child(imageUri.getLastPathSegment()+currentUserID+".jpg");
                    filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                downloadUrl = task.getResult().getDownloadUrl().toString();
                                txtImageVerification.setText("Imágen cargada con éxito");
                                HashMap hashMap = new HashMap();
                                hashMap.put("profileimage",downloadUrl);
                                userRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(RegisterInfo1Activity.this, RegisterInfo2Activity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            loadingBar.dismiss();
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterInfo1Activity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showOptionsDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.picture_options_layout,null);

        ImageButton btnCamera,btnGallery;
        btnCamera = finance_method.findViewById(R.id.btnCamera);
        btnGallery = finance_method.findViewById(R.id.btnGallery);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(RegisterInfo1Activity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(RegisterInfo1Activity.this, "oliver.oalone.oalonedevelopers.oalonebank_fintech.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, Gallery_Pick2);
                    }
                    dialog.dismiss();

                }
            }

        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
                dialog.dismiss();

            }
        });

        dialog.setView(finance_method);
        dialog.show();
    }

    Context inContext =RegisterInfo1Activity.this;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri = data.getData();
            try {
                Bitmap imageBitmap =MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                btnImageProfile.setImageBitmap(imageBitmap);
                // Get the dimensions of the View
                int targetW = btnImageProfile.getWidth();
                int targetH = btnImageProfile.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;

                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                //imageBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);


                btnImageProfile.setImageBitmap(imageBitmap);

                /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), imageBitmap, "Title", null);
                imageUri = imageUri.parse(path);*/
                txtImageVerification.setText("Imágen cargada con éxito");

                Paint rectPaint = new Paint();
                rectPaint.setStrokeWidth(10);
                rectPaint.setColor(Color.WHITE);
                rectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap =Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(tempBitmap);
                canvas.drawBitmap(imageBitmap,0,0,null);

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if (!faceDetector.isOperational()) {
                    //Toast.makeText(inContext, "NO FUNCIONAAA", Toast.LENGTH_SHORT).show();
                }

                Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);

                if (sparseArray.size() < 1) {
                    txtImageVerification.setText("NO SE HAN DETECTADO ROSTROS EN LA FOTO, DEBES CARGAR UNA NUEVA FOTO DONDE MUESTRES TU ROSTRO");
                    txtImageVerification.setTextColor(Color.RED);
                }
                if (sparseArray.size() > 1) {
                    txtImageVerification.setText("SE HA DETECTADO MÁS DE UN ROSTRO EN LA FOTO, DEBES CARGAR UNA NUEVA FOTO DONE MUESTRES SOLAMENTE TU ROSTRO");
                    txtImageVerification.setTextColor(Color.RED);
                }
                if (sparseArray.size() == 1) {
                    txtImageVerification.setText("Imágen cargada con éxito");
                    txtImageVerification.setTextColor(Color.GREEN);
                }

                for (int i = 0; i < sparseArray.size(); i++) {
                    Face face = sparseArray.valueAt(i);
                    float x1 = face.getPosition().x;
                    float y1 = face.getPosition().y;
                    float x2 = x1+face.getWidth();
                    float y2 = y1+face.getHeight();
                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPaint);



                }

                btnImageProfile.setImageBitmap(tempBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == Gallery_Pick2 && resultCode == RESULT_OK&&data!=null) {



            try {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                btnImageProfile.setImageBitmap(imageBitmap);
                // Get the dimensions of the View
                int targetW = btnImageProfile.getWidth();
                int targetH = btnImageProfile.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;

                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                imageBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);


                btnImageProfile.setImageBitmap(imageBitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), imageBitmap, "Title", null);
                imageUri = imageUri.parse(path);
                txtImageVerification.setText("Imágen cargada con éxito");

                Paint rectPaint = new Paint();
                rectPaint.setStrokeWidth(10);
                rectPaint.setColor(Color.WHITE);
                rectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap =Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(tempBitmap);
                canvas.drawBitmap(imageBitmap,0,0,null);

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();
                if (!faceDetector.isOperational()) {
                    Toast.makeText(inContext, "NO FUNCIONAAA", Toast.LENGTH_SHORT).show();
                }

                Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);

                if (sparseArray.size() < 1) {
                    txtImageVerification.setText("NO SE HAN DETECTADO ROSTROS EN LA FOTO, DEBES CARGAR UNA NUEVA FOTO DONDE MUESTRES TU ROSTRO");
                    txtImageVerification.setTextColor(Color.RED);
                }
                if (sparseArray.size() > 1) {
                    txtImageVerification.setText("SE HA DETECTADO MÁS DE UN ROSTRO EN LA FOTO, DEBES CARGAR UNA NUEVA FOTO DONE MUESTRES SOLAMENTE TU ROSTRO");
                    txtImageVerification.setTextColor(Color.RED);
                }
                if (sparseArray.size() == 1) {
                    txtImageVerification.setText("Imágen cargada con éxito");
                    txtImageVerification.setTextColor(Color.GREEN);
                }

                for (int i = 0; i < sparseArray.size(); i++) {
                    Face face = sparseArray.valueAt(i);
                    float x1 = face.getPosition().x;
                    float y1 = face.getPosition().y;
                    float x2 = x1+face.getWidth();
                    float y2 = y1+face.getHeight();
                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,rectPaint);

                }

                btnImageProfile.setImageBitmap(tempBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }





            // Get the dimensions of the View
            /*int targetW = btnImageProfile.getWidth();
            int targetH = btnImageProfile.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            imageBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);


            btnImageProfile.setImageBitmap(imageBitmap);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), imageBitmap, "Title", null);
            imageUri = imageUri.parse(path);
            txtImageVerification.setText("Imágen cargada con éxito");

            Paint rectPaint = new Paint();
            rectPaint.setStrokeWidth(10);
            rectPaint.setColor(Color.WHITE);
            rectPaint.setStyle(Paint.Style.STROKE);

            Bitmap tempBitmap =Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(tempBitmap);
            canvas.drawBitmap(imageBitmap,0,0,null);

            FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .setMode(FaceDetector.FAST_MODE)
                    .build();
            if (!faceDetector.isOperational()) {
                Toast.makeText(inContext, "NO FUNCIONAAA", Toast.LENGTH_SHORT).show();
            }

            Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
            SparseArray<Face> sparseArray = faceDetector.detect(frame);

            if (sparseArray.size() < 1) {
                txtImageVerification.setText("NO SE HAN DETECTADO ROSTROS EN LA FOTO, DEBES CARGAR UNA NUEVA FOTO DONDE MUESTRES TU ROSTRO");
                txtImageVerification.setTextColor(Color.RED);
            }
            if (sparseArray.size() > 1) {
                txtImageVerification.setText("SE HA DETECTADO MÁS DE UN ROSTRO EN LA FOTO, DEBES CARGAR UNA NUEVA FOTO DONE MUESTRES SOLAMENTE TU ROSTRO");
                txtImageVerification.setTextColor(Color.RED);
            }
            if (sparseArray.size() == 1) {
                txtImageVerification.setText("Imágen cargada con éxito");
                txtImageVerification.setTextColor(Color.GREEN);
            }

            for (int i = 0; i < sparseArray.size(); i++) {
                Face face = sparseArray.valueAt(i);
                float x1 = face.getPosition().x;
                float y1 = face.getPosition().y;
                float x2 = x1+face.getWidth();
                float y2 = y1+face.getHeight();
                RectF rectF = new RectF(x1,y1,x2,y2);
                canvas.drawRoundRect(rectF,2,2,rectPaint);

            }

            btnImageProfile.setImageBitmap(tempBitmap);*/

        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
    }
}
