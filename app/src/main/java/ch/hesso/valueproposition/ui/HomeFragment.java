package ch.hesso.valueproposition.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.db.DbObjects;
import ch.hesso.valueproposition.db.DbObjects.Canvas;

public class HomeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ResourceCursorAdapter mCursorAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //mCursorAdapter = new CanvasListAdapter(getActivity(), R.layout.element_home_card, null);
        mCursorAdapter = new ResourceCursorAdapter(getActivity(), R.layout.element_home_card, null, 0) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView titleTextView = (TextView) view.findViewById(R.id.home_card_element_title);
                titleTextView.setText(cursor.getString(cursor.getColumnIndex(DbObjects.Canvas.COL_TITLE)));

                TextView descTextView = (TextView) view.findViewById(R.id.home_card_element_description);
                descTextView.setText(cursor.getString(cursor.getColumnIndex(DbObjects.Canvas.COL_DESC)));

                final String id = cursor.getString(cursor.getColumnIndex(DbObjects.Canvas._ID));
                ImageButton editBtn = (ImageButton) view.findViewById(R.id.home_card_element_edit);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CanvasActivity.class);
                        intent.setData(Uri.withAppendedPath(DbObjects.Canvas.CONTENT_URI, id));
                        startActivity(intent);
                    }
                });
            }
        };
        setListAdapter(mCursorAdapter);
        getLoaderManager().initLoader(0, null, this);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        fab.attachToListView((ListView) rootView.findViewById(android.R.id.list));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CanvasActivity.class));
            }
        });

        return rootView;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), ElementsActivity.class);
        intent.setData(Uri.withAppendedPath(Canvas.CONTENT_URI, id + ""));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Canvas.CONTENT_URI, Canvas.PROJECTION_CANVAS, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}