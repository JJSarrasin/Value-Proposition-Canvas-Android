package ch.hesso.valueproposition.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.utils.Constants;

public class IdeasListFragment extends ListFragment {
    private int elementTypeId;
    private int currentCanvasId;

    public static IdeasListFragment newInstance() {
        return new IdeasListFragment();
    }

    public IdeasListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ideaslist, container, false);

        Bundle args = getArguments();
        if (args != null) {
            currentCanvasId = args.getInt(Constants.EXTRA_CANVAS_ID);
            elementTypeId = args.getInt(Constants.EXTRA_ELEMENT_TYPE_ID);

            int titleResourceId = getResources().getIdentifier("elements_item_" + elementTypeId, "string", getActivity().getPackageName());
            getActivity().setTitle(titleResourceId);
        }

        //TODO:CG Récupération des données existantes pour le currentCanvasId/elementTypeId donné
        List<String> list = new ArrayList<>();
        list.add("IDEA 1");
        list.add("IDEA 2");
        list.add("IDEA 3");
        list.add("IDEA 4");
        list.add("IDEA 5");

        setListAdapter(new ArrayAdapter<>(getActivity(), R.layout.element_ideaslist, list));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        fab.attachToListView((ListView) rootView.findViewById(android.R.id.list));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IdeaActivity.class);
                intent.putExtra(Constants.EXTRA_CANVAS_ID, currentCanvasId);
                intent.putExtra(Constants.EXTRA_ELEMENT_TYPE_ID, elementTypeId);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO:CG Gérer la récupération de l'id depuis la position
        //Cursor c = (Cursor) getListAdapter().getItem(position);
        //String url = c.getString(c.getColumnIndex("_id"));

        Intent intent = new Intent(getActivity(), IdeaActivity.class);
        intent.putExtra(Constants.EXTRA_CANVAS_ID, currentCanvasId);
        intent.putExtra(Constants.EXTRA_IDEA_ID, 1); //TODO:CG Remplacer
        intent.putExtra(Constants.EXTRA_ELEMENT_TYPE_ID, elementTypeId);
        startActivity(intent);
    }
}