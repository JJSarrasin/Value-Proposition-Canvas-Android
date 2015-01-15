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
import android.widget.SimpleCursorAdapter;

import com.melnykov.fab.FloatingActionButton;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.db.DbObjects;
import ch.hesso.valueproposition.utils.Constants;

public class IdeasListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri                 mCanvasUri;
    private Constants.Elements  mElement;
    private SimpleCursorAdapter mCursorAdapter;

    public static IdeasListFragment newInstance() {
        return new IdeasListFragment();
    }

    public IdeasListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ideaslist, container, false);

        Bundle args = getArguments();
        mCanvasUri = getActivity().getIntent().getData();
        if (mCanvasUri != null && args != null) {
            mElement = (Constants.Elements) args.getSerializable(Constants.EXTRA_ELEMENT_TYPE_ID);
            getLoaderManager().initLoader(0, null, this);
            int titleResourceId = getResources().getIdentifier("elements_item_" + (mElement.ordinal() + 1), "string", getActivity().getPackageName());
            getActivity().setTitle(titleResourceId);
        }

        mCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.element_ideaslist, null, new String[]{DbObjects.Ideas.COL_DESC}, new int[]{android.R.id.text1}, 0);
        setListAdapter(mCursorAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        fab.attachToListView((ListView) rootView.findViewById(android.R.id.list));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IdeaActivity.class);
                intent.putExtra(Constants.EXTRA_CANVAS_ID, mCanvasUri.getPathSegments().get(DbObjects.Canvas.CANVAS_ID_PATH_POSITION));
                intent.putExtra(Constants.EXTRA_ELEMENT_TYPE_ID, mElement);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), IdeaActivity.class);
        intent.setData(Uri.withAppendedPath(DbObjects.Ideas.CONTENT_URI, id + ""));
        intent.putExtra(Constants.EXTRA_CANVAS_ID, mCanvasUri.getPathSegments().get(DbObjects.Canvas.CANVAS_ID_PATH_POSITION));
        intent.putExtra(Constants.EXTRA_ELEMENT_TYPE_ID, mElement);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), DbObjects.Ideas.CONTENT_URI, DbObjects.Ideas.PROJECTION_IDEAS, DbObjects.Ideas.COL_CANVAS + "=? AND " + DbObjects.Ideas.COL_ELEMENT + "=?", new String[]{mCanvasUri.getPathSegments().get(DbObjects.Ideas.IDEA_ID_PATH_POSITION), mElement.ordinal() + ""}, null);
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