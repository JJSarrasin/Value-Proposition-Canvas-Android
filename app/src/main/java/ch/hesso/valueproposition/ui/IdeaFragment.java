package ch.hesso.valueproposition.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.melnykov.fab.FloatingActionButton;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.db.DbObjects;
import ch.hesso.valueproposition.utils.Constants;

public class IdeaFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText             contentEditText;
    private Uri                  mIdeaUri;
    private String               mCanvasId;
    private Constants.Elements   mElement;
    private SimpleCursorAdapter  mQuestionsAdapter;
    private boolean              isEditMode;
    private FloatingActionButton mFab;

    public static IdeaFragment newInstance() {
        return new IdeaFragment();
    }

    public IdeaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_idea, container, false);

        contentEditText = (EditText) rootView.findViewById(R.id.idea_edittext_content);

        Bundle args = getArguments();

        if (args != null) {
            mElement = (Constants.Elements) args.getSerializable(Constants.EXTRA_ELEMENT_TYPE_ID);
            mCanvasId = args.getString(Constants.EXTRA_CANVAS_ID);
            getLoaderManager().initLoader(0, null, this);
        }

        mIdeaUri = getActivity().getIntent().getData();
        if (mIdeaUri != null) {
            Cursor c = getActivity().getContentResolver().query(mIdeaUri, DbObjects.Ideas.PROJECTION_IDEAS, null, null, null);
            if (c.moveToFirst()) {
                isEditMode = true;
                contentEditText.setText(c.getString(c.getColumnIndex(DbObjects.Ideas.COL_DESC)));
                contentEditText.setSelection(contentEditText.getText().length());
                getActivity().setTitle(R.string.idea_title_edit);
            }
        }

        if (!isEditMode) getActivity().setTitle(R.string.idea_title_new);

        mQuestionsAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[]{DbObjects.Questions.COL_DESC}, new int[]{android.R.id.text1}, 0);
        setListAdapter(mQuestionsAdapter);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        mFab.attachToListView((ListView) rootView.findViewById(android.R.id.list));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());

                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.idea_question_title_new)
                        .setView(input)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String question = input.getText().toString();
                                ContentValues values = new ContentValues(2);
                                values.put(DbObjects.Questions.COL_DESC, question);
                                values.put(DbObjects.Questions.COL_ELEMENT, mElement.ordinal());
                                getActivity().getContentResolver().insert(DbObjects.Questions.CONTENT_URI, values);
                            }
                        }).setNegativeButton(android.R.string.cancel, null).show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_idea, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_delete:
                if (!isEditMode) return true;
                new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.idea_delete_confirmation)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getActivity().getContentResolver().delete(mIdeaUri, null, null);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).create().show();
                return true;
            case R.id.action_save:
                saveIdea();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Uri questionUri = Uri.withAppendedPath(DbObjects.Questions.CONTENT_URI, id + "");
        Cursor c = getActivity().getContentResolver().query(questionUri, DbObjects.Questions.PROJECTION_QUESTIONS, null, null, null);
        c.moveToFirst();

        final EditText input = new EditText(getActivity());
        input.setText(c.getString(c.getColumnIndex(DbObjects.Questions.COL_DESC)));
        input.setSelection(input.getText().length());

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.idea_question_title_edit)
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String question = input.getText().toString();
                        ContentValues values = new ContentValues(1);
                        values.put(DbObjects.Questions.COL_DESC, question);
                        getActivity().getContentResolver().update(questionUri, values, null, null);
                    }
                }).setNegativeButton(R.string.idea_question_edit_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getActivity().getContentResolver().delete(questionUri, null, null);
            }
        }).show();
    }

    private void saveIdea() {
        String content = contentEditText.getText().toString();
        ContentValues values = new ContentValues(2);
        values.put(DbObjects.Ideas.COL_DESC, content);
        values.put(DbObjects.Ideas.COL_ELEMENT, mElement.ordinal());
        values.put(DbObjects.Ideas.COL_CANVAS, mCanvasId);
        if (isEditMode) {
            getActivity().getContentResolver().update(mIdeaUri, values, null, null);
        } else {
            getActivity().getContentResolver().insert(DbObjects.Ideas.CONTENT_URI, values);
        }
        getActivity().finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), DbObjects.Questions.CONTENT_URI, DbObjects.Questions.PROJECTION_QUESTIONS, DbObjects.Questions.COL_ELEMENT + "=?", new String[]{mElement.ordinal() + ""}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mQuestionsAdapter.swapCursor(data);
        mFab.show(); // bugfix for fab disappearing when last list element is deleted
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuestionsAdapter.swapCursor(null);
    }
}