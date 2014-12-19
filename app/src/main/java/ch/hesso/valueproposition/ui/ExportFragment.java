package ch.hesso.valueproposition.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ch.hesso.valueproposition.R;
import ch.hesso.valueproposition.utils.Constants;

public class ExportFragment extends Fragment {
    private int currentCanvasId = 0;
    private List<String> productsServiceList = new ArrayList<>();
    private List<String> gainCreatorsList = new ArrayList<>();
    private List<String> painRelieversList = new ArrayList<>();
    private List<String> customerJobsList = new ArrayList<>();
    private List<String> gainsList = new ArrayList<>();
    private List<String> painsList = new ArrayList<>();

    public static ExportFragment newInstance() {
        return new ExportFragment();
    }

    public ExportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_export, container, false);

        Bundle args = getArguments();
        if (args != null) {
            currentCanvasId = args.getInt(Constants.EXTRA_CANVAS_ID);

            //TODO:CG Charger le nom du canvas et le mettre en titre
            getActivity().setTitle("TODO:TITLE_FROM_DB");

            //TODO:CG Charger les listes pour chaque element de ce canvas et les fournir à YB au lieu des fake data

            // Fill List with fake data
            productsServiceList.add("Idea 1");
            productsServiceList.add("Idea 2");
            productsServiceList.add("Idea 3");
            productsServiceList.add("Idea 4");

            gainCreatorsList.add("Idea 1");
            gainCreatorsList.add("Idea 2");
            gainCreatorsList.add("Idea 3");
            gainCreatorsList.add("Idea 4");

            painRelieversList.add("Idea 1");
            painRelieversList.add("Idea 2");
            painRelieversList.add("Idea 3");
            painRelieversList.add("Idea 4");

            customerJobsList.add("Idea 1");
            customerJobsList.add("Idea 2");
            customerJobsList.add("Idea 3");
            customerJobsList.add("Idea 4");

            gainsList.add("Idea 1");
            gainsList.add("Idea 2");
            gainsList.add("Idea 3");
            gainsList.add("Idea 4");

            painsList.add("Idea 1");
            painsList.add("Idea 2");
            painsList.add("Idea 3");
            painsList.add("Idea 4");

            //TODO: YB Traitement de l'image avec les données ci-dessus
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_export, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {

            //TODO:YB Passer le bitmap au lieu du bitmap vide de 1x1
            Bitmap icon = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            //Bitmap icon = myBitmap;

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try {
                OutputStream outstream = getActivity().getContentResolver().openOutputStream(uri);
                icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }

            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, getString(R.string.export_share_title)));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}