package ch.hesso.valueproposition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class CanvasListAdapter extends SimpleAdapter {
    private final Context context;
    private LayoutInflater	mInflater;
    private List<Map<String, String>> data;
    private int resource;

    public CanvasListAdapter(Context context, List<Map<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.resource = resource;
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(resource, parent, false);

        return super.getView(position, convertView, parent);
    }
}