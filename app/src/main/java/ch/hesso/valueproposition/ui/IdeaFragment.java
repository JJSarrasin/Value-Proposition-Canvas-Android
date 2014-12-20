package ch.hesso.valueproposition.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.utils.Constants;

public class IdeaFragment extends ListFragment {
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

        //TODO:CG Récupération des questions pour le elementTypeId donné (nice to have: limité au canvas)
        List<String> questionList = new ArrayList<>();
        questionList.add("Question 1");
        questionList.add("Question 2");
        questionList.add("Question 3");
        questionList.add("Question 4");
        questionList.add("Question 5");

        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, questionList));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        fab.attachToListView((ListView) rootView.findViewById(android.R.id.list));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());

                new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.idea_question_title_new)
                    .setView(input)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String question = input.getText().toString();
                            //TODO:CG Ajouter question dans BD
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
            case R.id.action_delete:
                new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.idea_delete_confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO:CG: Effacer de la BD
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
        /*
        //TODO:CG Gérer la récupération de l'id depuis la position
        //Cursor c = (Cursor) getListAdapter().getItem(position);
        //String url = c.getString(c.getColumnIndex("_id"));
        */
        final int questionId = 1;
        final EditText input = new EditText(getActivity());
        input.setText("toto"); //TODO:CG Remplacer
        input.setSelection(input.getText().length());

        new AlertDialog.Builder(getActivity())
            .setTitle(R.string.idea_question_title_edit)
            .setView(input)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String question = input.getText().toString();
                    //TODO:CG Màj question d'après son questionId et le string question
                }
            }).setNegativeButton(R.string.idea_question_edit_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //TODO:CG Supprimer question d'après son questionId
                }
        }).show();
    }

    private void saveIdea() {
        String content = contentEditText.getText().toString();

        //TODO:CG (Create/Update content)

        getActivity().finish();
    }
}