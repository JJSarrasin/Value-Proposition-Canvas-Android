package ch.hesso.valueproposition.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ch.hesso.valueproposition.R;

public class ElementsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);
        if (savedInstanceState == null) {
            ElementsFragment elementsFragment = ElementsFragment.newInstance();
            elementsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, elementsFragment).commit();
        }
    }
}
