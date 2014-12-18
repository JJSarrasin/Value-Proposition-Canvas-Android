package ch.hesso.valueproposition.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.adapters.CanvasListAdapter;

public class HomeFragment extends ListFragment {
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

        setListAdapter(new CanvasListAdapter(getActivity(), list, R.layout.home_card_element, new String[]{"TITLE", "DESCRIPTION"}, new int[]{R.id.home_card_element_title, R.id.home_card_element_description}));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        fab.attachToListView((ListView) rootView.findViewById(android.R.id.list));

        return rootView;
    }
}