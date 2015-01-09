package ch.hesso.valueproposition.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.db.DbObjects;
import ch.hesso.valueproposition.db.DbObjects.Canvas;

public class CanvasFragment extends Fragment {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private boolean isEditMode;
    private Uri mCanvasUri;

    public static CanvasFragment newInstance() {
        return new CanvasFragment();
    }

    public CanvasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_canvas, container, false);

        titleEditText = (EditText) rootView.findViewById(R.id.canvas_edittext_title);
        descriptionEditText = (EditText) rootView.findViewById(R.id.canvas_edittext_description);

        mCanvasUri = getActivity().getIntent().getData();

        if (mCanvasUri != null) {
            Cursor c = getActivity().getContentResolver().query(mCanvasUri, new String[]{DbObjects.Canvas._ID, DbObjects.Canvas.COL_TITLE, DbObjects.Canvas.COL_DESC}, null, null, null);
            if (c.moveToFirst()) {
                isEditMode = true;
                getActivity().setTitle(R.string.canvas_title_edit);
                titleEditText.setText(c.getString(c.getColumnIndex(DbObjects.Canvas.COL_TITLE)));
                descriptionEditText.setText(c.getString(c.getColumnIndex(DbObjects.Canvas.COL_DESC)));
            }
        }

        if (!isEditMode) getActivity().setTitle(R.string.canvas_title_new);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_canvas, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveForm();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveForm() {
        ContentValues values = new ContentValues(2);
        values.put(Canvas.COL_TITLE, titleEditText.getText().toString());
        values.put(Canvas.COL_DESC, descriptionEditText.getText().toString());

        if (isEditMode)
            getActivity().getContentResolver().update(mCanvasUri, values, null, null);
        else {
            mCanvasUri = getActivity().getContentResolver().insert(Canvas.CONTENT_URI, values);
            Intent intent = new Intent(getActivity(), ElementsActivity.class);
            intent.setData(mCanvasUri);
            startActivity(intent);
        }

        getActivity().finish();
    }
}