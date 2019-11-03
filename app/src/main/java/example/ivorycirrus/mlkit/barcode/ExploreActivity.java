package example.ivorycirrus.mlkit.barcode;


import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ListView;


import android.widget.ArrayAdapter;
import android.view.View;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExploreActivity extends AppCompatActivity {
    private ListView explorelv;
    public String barcodeid = CameraPreviewActivity.getBarcodeid();

    Map<String, String> map = new HashMap<>();
    List<Item> list = new ArrayList<>();

    private List<String> getRelated(String id) {
        id = barcodeid;
        // add to list
        list.add(new Item("9783161484100", "Bose QC-35", "headphones", 350.00));
        list.add(new Item("9791234567896", "Beats Solo 3", "headphones", 200.00));
        list.add(new Item("9781640841215", "CB3 Hush Headphones", "headphones", 50.00));
        list.add(new Item("9780733426094", "Starbucks Coffee", "coffee", 10.00));
        list.add(new Item("9781782808084", "Dunkin Donuts", "coffee", 7.50));
        list.add(new Item("9781934293065", "Nescafe", "coffee", 5.00));
        list.add(new Item("9783883094557", "Gucci Jacket", "jacket", 750.00));
        list.add(new Item("9782981558206", "North Face Jacket", "jacket", 150.00));
        list.add(new Item("9781941065303", "Levi's Jacket", "jacket", 50.00));
        list.add(new Item("9781565924796", "Dorm Chair", "chair", 10.00));
        list.add(new Item("9788175257665", "Aeros", "chair", 300.00));


        List<String> retList = new ArrayList<>();
        map.put("9783161484100", "headphones");
        map.put("9791234567896", "headphones");
        map.put("9781640841215", "headphones");
        map.put("978073342609", "headphones");

        map.put("9781782808084", "coffee");
        map.put("9781934293065", "coffee");
        map.put("9783883094557", "jacket");

        map.put("9782981558206", "jacket");
        map.put("9781941065303", "jacket");
        map.put("9788175257665", "chair");
        map.put("978156924796", "chair");

        String category = map.get(barcodeid);
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        List<String> ids = new ArrayList<>();
        for (String curr : map.keySet()) {
            if (map.get(curr).equals(category)) {
                Log.d("data given", category);
                Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
                ids.add(curr);
            }
        }
        for (String curr : ids) {
            for (Item item : list) {
                Log.d("checking", curr + " - " + item.id);
                if (curr.equals(item.id)) {
                    Log.d("match", "match");
                    retList.add(item.name + " - $" + item.price);
                }
            }

        }
        Log.d("pikachu", retList.toString());
        return retList;

    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);



        //API
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
//
//
//        client.newCall(request).enqueue(new Callback() {
//            //Response failed
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                Log.d("tag", "FAILURE");
//                Toast toast = Toast.makeText(getApplicationContext(),
//                        "FAIL",
//                        Toast.LENGTH_SHORT);
//                toast.show();
//                e.printStackTrace();
//            }
//
//            //Response is successful
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
////                        Toast toast = Toast.makeText(getApplicationContext(),
////                                "In response",
////                                Toast.LENGTH_SHORT);
////                        toast.show();
//                Log.d("alakazam", response.body().string());
//                Log.d("Hello", "In onResponse");
//                String myResponse;
//                if (response.isSuccessful()) {
////                            myResponse = response.body().string();
////                            Toast toast = Toast.makeText(getApplicationContext(),
////                                    myResponse,
////                                    Toast.LENGTH_SHORT);
////                            toast.show();
////                            Log.d("myrespo",myResponse);
////
////                            try {
////                                List<String> list = new ArrayList<>();
////                                JSONArray arr = new JSONArray(myResponse);
////                                item i = new item();
////                                for (int x = 0; x < arr.length(); x++) {
////                                    list.add(arr.getJSONObject(x).get("name") + " - $" + arr.getJSONObject(x).get("price"));
////                                }
////                                Log.d("json", list.toString());
////                        Log.d("json", jsonObject.toString());
////                        Log.d("data", jsonObject.get("id").toString());
////                        JSONObject jsonObject = new JSONObject(myResponse);
////                        JSONArray jsonArray = jsonObject.getJSONArray("snapshot");
////                        jsonObject = jsonArray.getJSONObject(0);
////                        jsonObject = jsonObject.getJSONObject("shortDescription");
////                        jsonArray = jsonObject.getJSONArray("values");
////                        jsonObject = jsonArray.getJSONObject(0);
////                        //jsonObject = jsonObject.getJSONObject("value");
////                        String item = jsonObject.getString("value");
////                        //Food food = new Food(item, "0");
////                        //foodAdapter.add(food);
////                        //food[0] = item;
////                        //ListAdapter foodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, food);
////                                Log.d("rs",myResponse);
//
////                            } catch (Exception JSONException) {
////                                Log.d("Hello" ,"Unsuccessful with error");
////
////                            }
//                } else {
//                    Log.d("Hello", "Unsuccessful");
//                }
//            }
//        });

        explorelv = (ListView) findViewById(R.id.explorelist);
//        List<String> your_array_list = new ArrayList<String>();
//
//        your_array_list.add("Borden choc milk - 1$ - 20%off");
//        your_array_list.add("Reese's Pieces - 2$ - 10%off");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                getRelated(barcodeid) );

        explorelv.setAdapter(arrayAdapter);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExploreActivity.this.finish();
            }
        });
    }
}
