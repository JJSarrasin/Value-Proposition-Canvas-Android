package ch.hesso.valueproposition.ui;

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
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.adapters.CanvasListAdapter;
import ch.hesso.valueproposition.db.DbObjects.Canvas;

public class HomeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CanvasListAdapter mCursorAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mCursorAdapter = new CanvasListAdapter(getActivity(), R.layout.element_home_card, null);
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