package oliver.oalone.oalonedevelopers.oalonebank_fintech.RegistrationData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

import static android.app.Activity.RESULT_OK;


public class RegisterData1Fragment extends Fragment {

    ImageView profileImage,profileImageState;
    LinearLayout btnCamera,btnGallery;
    String currentPhotoPath,image_verification;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    String currentUserID,downloadUrl,profileimage;
    StorageReference userProfileImageRef;
    Uri imageUri;
    TextView txtImageVerification;
    final static int Gallery_Pick = 2;
    ProgressDialog loadingBar;
    Button btnContinue;
    RelativeLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_data1, container, false);

        profileImage = view.findViewById(R.id.profileImage);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnGallery = view.findViewById(R.id.btnGallery);
        txtImageVerification = view.findViewById(R.id.txtImageVerification);
        profileImageState = view.findViewById(R.id.profileImageState);
        rootLayout = view.findViewById(R.id.rootLayout);
        btnContinue = view.findViewById(R.id.btnContinue);
        loadingBar = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        image_verification = "false";

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("profileimage")) {
                    profileimage = dataSnapshot.child("profileimage").getValue().toString();
                    Picasso.with(getActivity()).load(profileimage).fit().into(profileImage);
                    image_verification = "true";
                    txtImageVerification.setText("Esta foto es adecuada para tu perfil");
                    txtImageVerification.setTextColor(Color.GREEN);
                    profileImageState.setImageResource(R.drawable.transaction_completed);
                    loadingBar.dismiss();
                } else {
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image_verification.equals("false")) {
                    Snackbar.make(rootLayout, "Debes cargar una foto de perfil correctamente", Snackbar.LENGTH_LONG).show();
                    return;

                }
                else if (image_verification.equals("false")) {
                    Snackbar.make(rootLayout, "Debes cargar una foto de perfil correctamente", Snackbar.LENGTH_LONG).show();
                    return;

                }else if (image_verification.equals("true")) {
                    Intent intent = new Intent(getActivity(), RegistrationDataActivity.class);
                    intent.putExtra("FRAGMENT_ID", 1);
                    startActivity(intent);
                }
            }
        });



        return view;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = profileImage.getWidth();
        int targetH = profileImage.getHeight();

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

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        profileImage.setImageBitmap(bitmap);

        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Title", null);
        imageUri = imageUri.parse(path);

        Paint rectPaint = new Paint();
        rectPaint.setStrokeWidth(10);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap =Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap,0,0,null);

        FaceDetector faceDetector = new FaceDetector.Builder(getActivity().getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if (!faceDetector.isOperational()) {
            //Toast.makeText(inContext, "NO FUNCIONAAA", Toast.LENGTH_SHORT).show();
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);

        if (sparseArray.size() < 1) {
            txtImageVerification.setText("En esta foto no se ha detectado tu rostro.");
            txtImageVerification.setTextColor(Color.RED);
            profileImageState.setImageResource(R.drawable.error_icon);
        }
        if (sparseArray.size() > 1) {
            txtImageVerification.setText("Esta foto contiene dos o más rostros.");
            txtImageVerification.setTextColor(Color.RED);
            profileImageState.setImageResource(R.drawable.error_icon);
        }
        if (sparseArray.size() == 1) {
            txtImageVerification.setText("Esta foto es adecuada para tu perfil");
            txtImageVerification.setTextColor(Color.GREEN);
            profileImageState.setImageResource(R.drawable.transaction_completed);
            savePhotoOnDatabase();
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
    }

    private void savePhotoOnDatabase() {

        loadingBar.setTitle("Subiendo tu Foto de Perfil...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        StorageReference filePath = userProfileImageRef.child(imageUri.getLastPathSegment()+currentUserID+".jpg");
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    downloadUrl = task.getResult().getDownloadUrl().toString();
                    userRef.child("profileimage").setValue(downloadUrl);
                    image_verification = "true";
                    loadingBar.dismiss();
                }
            }
        });

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "oliver.oalone.oalonedevelopers.oalonebank_fintech.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic();
            setPic();

            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);*/
        }
        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            try {
                Bitmap imageBitmap =MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImage.setImageBitmap(imageBitmap);
                // Get the dimensions of the View
                int targetW = profileImage.getWidth();
                int targetH = profileImage.getHeight();

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


                profileImage.setImageBitmap(imageBitmap);

                /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), imageBitmap, "Title", null);
                imageUri = imageUri.parse(path);*/
                //txtImageVerification.setText("Imágen cargada con éxito");

                Paint rectPaint = new Paint();
                rectPaint.setStrokeWidth(10);
                rectPaint.setColor(Color.WHITE);
                rectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap =Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(tempBitmap);
                canvas.drawBitmap(imageBitmap,0,0,null);

                FaceDetector faceDetector = new FaceDetector.Builder(getActivity().getApplicationContext())
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
                    txtImageVerification.setText("En esta foto no se ha detectado tu rostro.");
                    txtImageVerification.setTextColor(Color.RED);
                    profileImageState.setImageResource(R.drawable.error_icon);
                }
                if (sparseArray.size() > 1) {
                    txtImageVerification.setText("Esta foto contiene dos o más rostros.");
                    txtImageVerification.setTextColor(Color.RED);
                    profileImageState.setImageResource(R.drawable.error_icon);
                }
                if (sparseArray.size() == 1) {
                    txtImageVerification.setText("Esta foto es adecuada para tu perfil");
                    txtImageVerification.setTextColor(Color.GREEN);
                    profileImageState.setImageResource(R.drawable.transaction_completed);
                    savePhotoOnDatabase();
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

                profileImage.setImageBitmap(tempBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}