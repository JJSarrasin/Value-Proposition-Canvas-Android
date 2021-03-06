package ch.hesso.valueproposition.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ch.hesso.valueproposition.R;


public class IdeasListActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);
        if (savedInstanceState == null) {
            IdeasListFragment ideasListFragment = IdeasListFragment.newInstance();
            ideasListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, ideasListFragment).commit();
        }
    }
}
