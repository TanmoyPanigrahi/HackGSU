package example.ivorycirrus.mlkit.barcode;


import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;


import android.widget.ArrayAdapter;
import android.view.View;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ExploreActivity extends AppCompatActivity {
    private ListView explorelv;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore);

        explorelv = (ListView) findViewById(R.id.explorelist);
        List<String> your_array_list = new ArrayList<String>();

        your_array_list.add("Borden choc milk - 1$ - 20%off");
        your_array_list.add("Reese's Pieces - 2$ - 10%off");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        explorelv.setAdapter(arrayAdapter);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExploreActivity.this.finish();
            }
        });
    }
}
