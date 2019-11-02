package example.ivorycirrus.mlkit.barcode;

import android.content.pm.ActivityInfo;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Camera Preview Activity
 * control preview screen and overlays
 */
public class CameraPreviewActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraView camView;
    private OverlayView overlay;
    public static String barcodeid = "";
    private double overlayScale = -1;

    private interface OnBarcodeListener {
        void onIsbnDetected(FirebaseVisionBarcode barcode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Full Screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Fix orientation : portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set layout
        setContentView(R.layout.activity_camera_preview);

        // Set ui button actions
        findViewById(R.id.btn_finish_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Call API
                OkHttpClient client = new OkHttpClient();

                String url = "https://hackgsu-257800.appspot.com/shop/add-item-by-code/";

                RequestBody formBody = new FormBody.Builder()
                        .add("id", barcodeid)
                        .add("nothing", "")
                        .build();
                formBody = RequestBody.create(MediaType.parse("application/json"), "{\"id\":" + "\"" + barcodeid + "\"}" );

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Content-Type", "application/json")
                        .post(formBody)
                        .build();


                Log.d("Hello", "Built request");
                Log.d("Request", request.toString());

                Toast toast = Toast.makeText(getApplicationContext(),
                        barcodeid,
                        Toast.LENGTH_SHORT);

                toast.show();

                Log.d("Hello", "Going to call");


                client.newCall(request).enqueue(new Callback() {
                    //Response failed
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("tag", "FAILURE");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "FAIL",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        e.printStackTrace();
                    }

                    //Response is successful
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                "In response",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
                        Log.d("alakazam", response.body().string());
                        Log.d("Hello", "In onResponse");
                        String myResponse;
                        if (response.isSuccessful()) {
//                            myResponse = response.body().string();
//                            Toast toast = Toast.makeText(getApplicationContext(),
//                                    myResponse,
//                                    Toast.LENGTH_SHORT);
//                            toast.show();
//                            Log.d("myrespo",myResponse);
//
//                            try {
//                                List<String> list = new ArrayList<>();
//                                JSONArray arr = new JSONArray(myResponse);
//                                item i = new item();
//                                for (int x = 0; x < arr.length(); x++) {
//                                    list.add(arr.getJSONObject(x).get("name") + " - $" + arr.getJSONObject(x).get("price"));
//                                }
//                                Log.d("json", list.toString());
//                        Log.d("json", jsonObject.toString());
//                        Log.d("data", jsonObject.get("id").toString());
//                        JSONObject jsonObject = new JSONObject(myResponse);
//                        JSONArray jsonArray = jsonObject.getJSONArray("snapshot");
//                        jsonObject = jsonArray.getJSONObject(0);
//                        jsonObject = jsonObject.getJSONObject("shortDescription");
//                        jsonArray = jsonObject.getJSONArray("values");
//                        jsonObject = jsonArray.getJSONObject(0);
//                        //jsonObject = jsonObject.getJSONObject("value");
//                        String item = jsonObject.getString("value");
//                        //Food food = new Food(item, "0");
//                        //foodAdapter.add(food);
//                        //food[0] = item;
//                        //ListAdapter foodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, food);
//                                Log.d("rs",myResponse);

//                            } catch (Exception JSONException) {
//                                Log.d("Hello" ,"Unsuccessful with error");
//
//                            }
                        } else {
                            Log.d("Hello", "Unsuccessful");
                        }
                    }
                });

                CameraPreviewActivity.this.finish();
            }
        });

        // Initialize Camera
        mCamera = getCameraInstance();

        // Set-up preview screen
        if(mCamera != null) {
            // Create overlay view
            overlay = new OverlayView(this);

            // Create barcode processor for ISBN
            CustomPreviewCallback camCallback = new CustomPreviewCallback(CameraView.PREVIEW_WIDTH, CameraView.PREVIEW_HEIGHT);
            camCallback.setBarcodeDetectedListener(new OnBarcodeListener() {
                @Override
                public void onIsbnDetected(FirebaseVisionBarcode barcode) {
                    overlay.setOverlay(fitOverlayRect(barcode.getBoundingBox()), barcode.getRawValue());
                    overlay.invalidate();
                }
            });

            // Create camera preview
            camView = new CameraView(this, mCamera);
            camView.setPreviewCallback(camCallback);

            // Add view to UI
            FrameLayout preview = findViewById(R.id.frm_preview);
            preview.addView(camView);
            preview.addView(overlay);
        }
    }

    @Override
    protected void onDestroy() {
        try{
            if(mCamera != null) mCamera.release();
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onDestroy();
    }

    /** Get facing back camera instance */
    public static Camera getCameraInstance()
    {
        int camId = -1;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                camId = i;
                break;
            }
        }

        if(camId == -1) return null;

        Camera c=null;
        try{
            c=Camera.open(camId);
        }catch(Exception e){
            e.printStackTrace();
        }
        return c;
    }

    /** Calculate overlay scale factor */
    private Rect fitOverlayRect(Rect r) {
        if(overlayScale <= 0) {
            Camera.Size prevSize = camView.getPreviewSize();
            overlayScale = (double) overlay.getWidth()/(double)prevSize.height;
        }

        return new Rect((int)(r.left*overlayScale), (int)(r.top*overlayScale), (int)(r.right*overlayScale), (int)(r.bottom*overlayScale));
    }

    /** Post-processor for preview image streams */
    private class CustomPreviewCallback implements Camera.PreviewCallback, OnSuccessListener<List<FirebaseVisionBarcode>>, OnFailureListener {

        public void setBarcodeDetectedListener(OnBarcodeListener mBarcodeDetectedListener) {
            this.mBarcodeDetectedListener = mBarcodeDetectedListener;
        }

        // ML Kit instances
        private FirebaseVisionBarcodeDetectorOptions options;
        private FirebaseVisionBarcodeDetector detector;
        private FirebaseVisionImageMetadata metadata;

        /**
         * Event Listener for post processing
         *
         * We'll set up the detector only for EAN-13 barcode format and ISBN barcode type.
         * This OnBarcodeListener aims of notifying 'ISBN barcode is detected' to other class.
         */
        private OnBarcodeListener mBarcodeDetectedListener = null;

        /** size of input image */
        private int mImageWidth, mImageHeight;

        /**
         * Constructor
         * @param imageWidth preview image width (px)
         * @param imageHeight preview image height (px)
         */
        CustomPreviewCallback(int imageWidth, int imageHeight){
            mImageWidth = imageWidth;
            mImageHeight = imageHeight;

            // set-up detector options for find EAN-13 format (commonly used 1-D barcode)
            options =new FirebaseVisionBarcodeDetectorOptions.Builder()
                    .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_EAN_13)
                    .build();

            detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

            // build detector
            metadata = new FirebaseVisionImageMetadata.Builder()
                    .setFormat(ImageFormat.NV21)
                    .setWidth(mImageWidth)
                    .setHeight(mImageHeight)
                    .setRotation(FirebaseVisionImageMetadata.ROTATION_90)
                    .build();
        }

        /** Start detector if camera preview shows */
        @Override public void onPreviewFrame(byte[] data, Camera camera) {
            try {
                detector.detectInImage(FirebaseVisionImage.fromByteArray(data, metadata))
                        .addOnSuccessListener(this)
                        .addOnFailureListener(this);
            } catch (Exception e) {
                Log.d("CameraView", "parse error");
            }
        }

        /** Barcode is detected successfully */
        @Override public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
            // Task completed successfully
            for (FirebaseVisionBarcode barcode: barcodes) {
                Log.d("Barcode", "value : "+barcode.getRawValue());
                CameraPreviewActivity.barcodeid = barcode.getRawValue();
                int valueType = barcode.getValueType();
                if (valueType == FirebaseVisionBarcode.TYPE_ISBN) {
                    mBarcodeDetectedListener.onIsbnDetected(barcode);
                    return;
                }
            }
        }

        /** Barcode is not recognized */
        @Override
        public void onFailure(@NonNull Exception e) {
            // Task failed with an exception
            Log.i("Barcode", "read fail");
        }
    }
}
