package ch.hesso.valueproposition.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import ch.hesso.valueproposition.utils.Constants;

public class IdeaFragment extends Fragment {
    private EditText contentEditText;
    private int elementTypeId;
    private int currentCanvasId;
    private int currentIdeaId;

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
            currentCanvasId = args.getInt(Constants.EXTRA_CANVAS_ID);
            currentIdeaId = args.getInt(Constants.EXTRA_IDEA_ID);
            elementTypeId = args.getInt(Constants.EXTRA_ELEMENT_TYPE_ID);

            if (currentIdeaId != 0) {
                //TODO:CG Charger Idea de la DB + afficher dans le champ

                contentEditText.setText("IDEA X");
                contentEditText.setSelection(contentEditText.getText().length());
                getActivity().setTitle(R.string.idea_title_edit);
            }
            else
                getActivity().setTitle(R.string.idea_title_new);
        }

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
            case R.id.action_add_question:
                //TODO
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.idea_delete_confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO:CG: Effacer de la BD
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    }).create().show();
                return true;
            case R.id.action_save:
                saveForm();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveForm() {
        String content = contentEditText.getText().toString();

        //TODO:CG (Create/Update content)

        getActivity().finish();
    }
}