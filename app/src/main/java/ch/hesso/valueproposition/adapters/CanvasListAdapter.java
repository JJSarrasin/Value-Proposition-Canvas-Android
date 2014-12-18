package ch.hesso.valueproposition.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.ui.CanvasActivity;
import ch.hesso.valueproposition.utils.Constants;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(resource, parent, false);

            ImageButton editButton = (ImageButton) convertView.findViewById(R.id.home_card_element_edit);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CanvasActivity.class);
                    intent.putExtra(Constants.EXTRA_ID, Integer.parseInt(data.get(position).get("ID")));
                    context.startActivity(intent);
                }
            });
        }

        return super.getView(position, convertView, parent);
    }
}