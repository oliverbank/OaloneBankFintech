package oliver.oalone.oalonedevelopers.oalonebank_fintech.WalletFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompaniesCheckoutActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.QrCompanyPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyQrCodeActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class QrPayFragment extends Fragment {

    SurfaceView cameraPreview;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    int RequestCameraPermissionID = 1001;
    String company_id;
    TextView txtResult;
    LinearLayout showMyQrCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr_pay, container, false);

        cameraPreview = view.findViewById(R.id.cameraPreview);
        txtResult = view.findViewById(R.id.txtResult);
        showMyQrCode = view.findViewById(R.id.showMyQrCode);

        barcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getActivity(),barcodeDetector).setAutoFocusEnabled(true).setRequestedFps(40.0f)
                .setRequestedPreviewSize(1200,1200).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0)
                {
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.stop();
                            Vibrator vibrator = (Vibrator)getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            txtResult.setText(qrcodes.valueAt(0).displayValue);
                            Intent intent = new Intent(getActivity(), CompaniesCheckoutActivity.class);
                            intent.putExtra("company_key",txtResult.getText().toString());
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }
            }
        });

        showMyQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyQrCodeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
