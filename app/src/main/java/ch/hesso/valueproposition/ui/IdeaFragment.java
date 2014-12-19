package ch.hesso.valueproposition.ui;

import android.content.Intent;
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
    private EditText titleEditText;
    private EditText descriptionEditText;
    private int currentCanvasId = 0;

    public static IdeaFragment newInstance() {
        return new IdeaFragment();
    }

    public IdeaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_canvas, container, false);


        titleEditText = (EditText) rootView.findViewById(R.id.canvas_edittext_title);
        descriptionEditText = (EditText) rootView.findViewById(R.id.canvas_edittext_description);

        Bundle args = getArguments();
        if (args != null) {
            currentCanvasId = args.getInt(Constants.EXTRA_CANVAS_ID);

            if (currentCanvasId != 0) {
                //TODO:CG Charger from DB + afficher dans les champs

                titleEditText.setText("TITLE");
                descriptionEditText.setText("DESCRIPTION");
                getActivity().setTitle(R.string.canvas_title_edit);
            }
        }
        else
            getActivity().setTitle(R.string.canvas_title_new);

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
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        //TODO:CG (Save title + description)

        if (currentCanvasId == 0) {
            Intent intent = new Intent(getActivity(), ElementsActivity.class);
            //TODO:CG Transmettre l'id de l'objet à l'activité suivante dans le cas d'une création
            intent.putExtra(Constants.EXTRA_CANVAS_ID, 12345);
            startActivity(intent);
        }

        getActivity().finish();
    }
}