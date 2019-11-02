package example.ivorycirrus.mlkit.barcode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter {
    private ArrayList<Object> list = new ArrayList<>();

    public ItemAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(@Nullable Object object) {
        list.add(object);
    }
}
