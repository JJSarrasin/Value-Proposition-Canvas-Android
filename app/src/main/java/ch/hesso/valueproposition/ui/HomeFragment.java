package ch.hesso.valueproposition.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.adapters.CanvasListAdapter;
import ch.hesso.valueproposition.utils.Constants;

public class HomeFragment extends ListFragment {
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //TODO: Map donnée CG
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Map<String, String> map_1 = new HashMap<String, String>();
        map_1.put("TITLE", "Facebook Canvas");
        map_1.put("DESCRIPTION", "This a demonstration to do the empathy map of FB.");
        map_1.put("ID", "1");
        list.add(map_1);
        Map<String, String> map_2 = new HashMap<String, String>();
        map_2.put("TITLE", "Google Canvas");
        map_2.put("DESCRIPTION", "2014.12.12 This a demonstration to do the empathy map of Google.");
        map_2.put("ID", "2");
        list.add(map_2);

        setListAdapter(new CanvasListAdapter(getActivity(), list, R.layout.element_home_card, new String[]{"TITLE", "DESCRIPTION"}, new int[]{R.id.home_card_element_title, R.id.home_card_element_description}));

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, String> selectedItem = (Map<String, String>) l.getItemAtPosition(position);

        Intent intent = new Intent(getActivity(), ElementsActivity.class);
        intent.putExtra(Constants.EXTRA_CANVAS_ID, Integer.parseInt(selectedItem.get("ID")));
        startActivity(intent);
    }
}