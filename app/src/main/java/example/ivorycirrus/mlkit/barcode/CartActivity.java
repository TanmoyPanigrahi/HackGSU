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
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import org.json.JSONArray;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CartActivity extends AppCompatActivity{
    private ListView lv;
    List<String> list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        lv = (ListView) findViewById(R.id.cartlist);
        ItemAdapter itemAdapter = new ItemAdapter(this, android.R.layout.simple_list_item_1,list );

        List<String> your_array_list = new ArrayList<String>();

//        your_array_list.add("milk - 1$");
//        your_array_list.add("candy - 1$");
//        your_array_list.add("total - 2$");
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                your_array_list );
//
//        lv.setAdapter(arrayAdapter);

        Log.d("Hello", "Here");

        OkHttpClient client = new OkHttpClient();

        String url = "https://hackgsu-257800.appspot.com/shop/view-cart/";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();

        Log.d("Hello", "Built request");


        client.newCall(request).enqueue(new Callback() {
            //Response failed
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            //Response is successful
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Hello", "In onResponse");
                String myResponse;
                if (response.isSuccessful()) {
                    myResponse = response.body().string();
                    Log.d("myrespo",myResponse);

                    try {
                        final List<String> list = new ArrayList<>();
                        JSONArray arr = new JSONArray(myResponse);

                        for (int x = 0; x < arr.length(); x++) {
                            list.add(arr.getJSONObject(x).get("name") + " - $" + arr.getJSONObject(x).get("price"));
                        }
//                        itemAdapter.notifyDataSetChanged();
//                        lv.setAdapter(itemAdapter);
                        CartActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        CartActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        list );

                                lv.setAdapter(arrayAdapter);
                            }
                        });

//                        lv = (ListView) findViewById(R.id.cartlist);
//                        List<String> your_array_list = new ArrayList<String>();
//
//                        your_array_list.add("milk - 1$");
//                        your_array_list.add("candy - 1$");
//                        your_array_list.add("total - 2$");

//                        ItemAdapter arrayAdapter = new ItemAdapter(
//                                CartActivity.this,
//                                android.R.layout.simple_list_item_1,
//                                list);
//                        arrayAdapter.notifyDataSetChanged();
//                        lv.setAdapter(arrayAdapter);
                        Log.d("json", list.toString());
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
                        Log.d("rs",myResponse);

                    } catch (Exception JSONException) {
                        Log.d("Hello" ,"Unsuccessful with error");

                    }
                } else {
                    Log.d("Hello", "Unsuccessful");
                }
            }
        });

//        lv = (ListView) findViewById(R.id.cartlist);
//
//        List<String> your_array_list = new ArrayList<String>();
//
//        your_array_list.add("milk - 1$");
//        your_array_list.add("candy - 1$");
//        your_array_list.add("total - 2$");
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                your_array_list );
//
//        lv.setAdapter(arrayAdapter);

        findViewById(R.id.addit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.this.finish();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.this.finish();
            }
        });
    }
}
