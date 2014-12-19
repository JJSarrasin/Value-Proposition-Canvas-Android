package ch.hesso.valueproposition.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.utils.Constants;

public class ElementsFragment extends Fragment {
    private int currentCanvasId;

    public static ElementsFragment newInstance() {
        return new ElementsFragment();
    }

    public ElementsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_elements, container, false);

        Bundle args = getArguments();
        if (args != null) {
            currentCanvasId = args.getInt(Constants.EXTRA_CANVAS_ID);

            //TODO:CG Charger le nom du canvas et le mettre en titre
            getActivity().setTitle("TODO:TITLE_FROM_DB");
        }

        rootView.findViewById(R.id.elements_imagebutton_productsservices).setOnClickListener(onItemClickListener);
        rootView.findViewById(R.id.elements_imagebutton_gaincreators).setOnClickListener(onItemClickListener);
        rootView.findViewById(R.id.elements_imagebutton_painrelievers).setOnClickListener(onItemClickListener);
        rootView.findViewById(R.id.elements_imagebutton_customerjobs).setOnClickListener(onItemClickListener);
        rootView.findViewById(R.id.elements_imagebutton_gains).setOnClickListener(onItemClickListener);
        rootView.findViewById(R.id.elements_imagebutton_pains).setOnClickListener(onItemClickListener);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_elements, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export:
                Intent intent = new Intent(getActivity(), ExportActivity.class);
                intent.putExtra(Constants.EXTRA_CANVAS_ID, currentCanvasId);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.elements_delete_confirmation)
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
        }

        return super.onOptionsItemSelected(item);
    }

    android.view.View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), IdeasListActivity.class);
            intent.putExtra(Constants.EXTRA_CANVAS_ID, currentCanvasId);
            intent.putExtra(Constants.EXTRA_ELEMENT_TYPE_ID, Integer.parseInt(v.getTag().toString()));
            startActivity(intent);
        }
    };
}