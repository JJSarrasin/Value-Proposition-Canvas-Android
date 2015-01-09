package ch.hesso.valueproposition.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.db.DbObjects;
import ch.hesso.valueproposition.ui.CanvasActivity;

public class CanvasListAdapter extends ResourceCursorAdapter {

    public CanvasListAdapter(Context context, int layout, Cursor cursor) {
        super(context, layout, cursor, ResourceCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView titleTextView = (TextView) view.findViewById(R.id.home_card_element_title);
        titleTextView.setText(cursor.getString(cursor.getColumnIndex(DbObjects.Canvas.COL_TITLE)));

        TextView descTextView = (TextView) view.findViewById(R.id.home_card_element_description);
        descTextView.setText(cursor.getString(cursor.getColumnIndex(DbObjects.Canvas.COL_DESC)));

        ImageButton editBtn = (ImageButton) view.findViewById(R.id.home_card_element_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CanvasActivity.class);
                intent.setData(Uri.withAppendedPath(DbObjects.Canvas.CONTENT_URI, cursor.getString(cursor.getColumnIndex(DbObjects.Canvas._ID))));
                context.startActivity(intent);
            }
        });
    }

}