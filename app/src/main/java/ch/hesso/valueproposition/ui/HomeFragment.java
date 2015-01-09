package ch.hesso.valueproposition.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.melnykov.fab.FloatingActionButton;

import java.util.Map;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.db.DbObjects.Canvas;
import ch.hesso.valueproposition.utils.Constants;

public class HomeFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String[] PROJECTION = {Canvas._ID, Canvas.COL_TITLE, Canvas.COL_DESC, Canvas.COL_CREATED_AT};

    private SimpleCursorAdapter mCursorAdapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.element_home_card, null, PROJECTION, new int[]{R.id.home_card_element_title, R.id.home_card_element_description}, 0);
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
        Map<String, String> selectedItem = (Map<String, String>) l.getItemAtPosition(position);

        Intent intent = new Intent(getActivity(), ElementsActivity.class);
        intent.putExtra(Constants.EXTRA_CANVAS_ID, Integer.parseInt(selectedItem.get("ID")));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Canvas.CONTENT_URI, PROJECTION, null, null, null);
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