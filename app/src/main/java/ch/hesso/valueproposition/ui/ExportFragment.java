package ch.hesso.valueproposition.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import ch.hesso.valueproposition.db.DbObjects;
import ch.hesso.valueproposition.utils.Constants.Elements;

public class ExportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_CANVAS = 1;
    private static final int LOADER_IDEAS = 2;

    private Menu mMenu;
    private boolean isGenerated;
    private int mLoadedCount = 0;

    private Uri mCanvasUri;
    /**
     * Pour YB : éléments du canvas
     */
    private String mCanvasTitle;
    private String mCanvasDesc;
    private long mCanvasCreatedAt;

    /**
     * Pour YB : Lists contenant la description de l'Idea
     */
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

        mCanvasUri = getActivity().getIntent().getData();
        getLoaderManager().initLoader(LOADER_CANVAS, null, this);
        getLoaderManager().initLoader(LOADER_IDEAS, null, this);

        View rootView = inflater.inflate(R.layout.fragment_export, container, false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_export, menu);
        mMenu = menu;
        menu.findItem(R.id.action_share).setVisible(isGenerated);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;

            case R.id.action_share:

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

    private void generateBitmap() {
        //TODO: YB Création de l'image avec les données dans les lists

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        switch (id) {
            case LOADER_CANVAS:
                loader = new CursorLoader(getActivity(), mCanvasUri, DbObjects.Canvas.PROJECTION_CANVAS, null, null, null);
                break;
            case LOADER_IDEAS:
                loader = new CursorLoader(getActivity(), DbObjects.Ideas.CONTENT_URI, DbObjects.Ideas.PROJECTION_IDEAS, DbObjects.Ideas.COL_CANVAS + "=?", new String[]{mCanvasUri.getLastPathSegment()}, null);
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_CANVAS:
                if (data.moveToFirst()) {
                    mCanvasTitle = data.getString(data.getColumnIndex(DbObjects.Canvas.COL_TITLE));
                    mCanvasDesc = data.getString(data.getColumnIndex(DbObjects.Canvas.COL_DESC));
                    mCanvasCreatedAt = data.getLong(data.getColumnIndex(DbObjects.Canvas.COL_CREATED_AT));
                    getActivity().setTitle(mCanvasTitle);
                }
                mLoadedCount++;
                break;
            case LOADER_IDEAS:
                if (data.moveToFirst()) {
                    do {
                        String description = data.getString(data.getColumnIndex(DbObjects.Ideas.COL_DESC));
                        Elements element = Elements.values()[data.getInt(data.getColumnIndex(DbObjects.Ideas.COL_ELEMENT))];
                        switch (element) {
                            case CUSTOMER_GAINS:
                                gainsList.add(description);
                                break;
                            case CUSTOMER_PAINS:
                                painsList.add(description);
                                break;
                            case CUSTOMERS_JOBS:
                                customerJobsList.add(description);
                                break;
                            case GAIN_CREATORS:
                                gainCreatorsList.add(description);
                                break;
                            case PAIN_RELIEVERS:
                                painRelieversList.add(description);
                                break;
                            case PRODUCTS_SERVICES:
                                productsServiceList.add(description);
                                break;
                        }
                    } while (data.moveToNext());
                }
                mLoadedCount++;
                break;
        }
        if (mLoadedCount > 1) {
            generateBitmap();
            isGenerated = true;
            if (mMenu != null) {
                mMenu.findItem(R.id.action_share).setVisible(isGenerated);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}