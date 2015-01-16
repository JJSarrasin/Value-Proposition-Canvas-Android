package ch.hesso.valueproposition.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
    private String mCanvasTitle;

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
                View rootView = getActivity().findViewById(R.id.export_rootview);

                Bitmap generatedBitmap = generateBitmap();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                try {
                    OutputStream outstream = getActivity().getContentResolver().openOutputStream(uri);
                    generatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
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
                    getActivity().setTitle(mCanvasTitle);
                }
                mLoadedCount++;
                break;
            case LOADER_IDEAS:
                gainsList.clear();
                painsList.clear();
                customerJobsList.clear();
                gainCreatorsList.clear();
                painRelieversList.clear();
                productsServiceList.clear();

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
            FrameLayout rootView = (FrameLayout) getActivity().findViewById(R.id.export_rootview);
            ExportView ev = new ExportView(getActivity(), rootView);
            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            rootView.addView(ev, size.x, size.y);

            isGenerated = true;
            if (mMenu != null) {
                mMenu.findItem(R.id.action_share).setVisible(isGenerated);
            }
        }
    }


    private Bitmap generateBitmap() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float scale = getResources().getDisplayMetrics().density;
        paint.setTextSize((int) (13 * scale));

        View exportLayout = getActivity().getLayoutInflater().inflate(R.layout.layout_export, null);
        exportLayout.setDrawingCacheEnabled(true);
        // Without it the view will have a dimension of 0,0 and the bitmap will be null
        exportLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        exportLayout.layout(0, 0, exportLayout.getMeasuredWidth(), exportLayout.getMeasuredHeight());
        exportLayout.buildDrawingCache(true);
        Bitmap bm = Bitmap.createBitmap(exportLayout.getDrawingCache());
        exportLayout.setDrawingCacheEnabled(false);

        Canvas myCanvas = new Canvas(bm);

        Rect bounds = new Rect();

        int productServiceMaxLength = 16;
        String[] productService = getStringFormatted(productsServiceList, productServiceMaxLength);
        if (productService.length > 0) {
            paint.getTextBounds(productService[0], 0, 1, bounds);
            int lineHeight = bounds.height()+convertDpToPx(4);
            int startHeightPoint = (bm.getHeight() - lineHeight*productService.length) / 2;
            for (int i = 0 ; i < productService.length ; i++) {
                myCanvas.drawText(productService[i], convertDpToPx(23), startHeightPoint + i * lineHeight, paint);
            }
        }

        int gainCreatorsMaxLength = 18;
        String[] gainCreators = getStringFormatted(gainCreatorsList, gainCreatorsMaxLength);
        if (gainCreators.length > 0) {
            paint.getTextBounds(gainCreators[0], 0, 1, bounds);
            int lineHeight = bounds.height()+convertDpToPx(4);
            int startHeightPoint = (bm.getHeight() - lineHeight * productService.length) / 3;
            for (int i = 0; i < gainCreators.length; i++) {
                myCanvas.drawText(gainCreators[i], convertDpToPx(170), startHeightPoint + i * lineHeight, paint);
            }
        }

        int painRelieversMaxLength = 18;
        String[] painRelievers = getStringFormatted(painRelieversList, painRelieversMaxLength);
        if (painRelievers.length > 0) {
            paint.getTextBounds(painRelievers[0], 0, 1, bounds);
            int lineHeight = bounds.height()+convertDpToPx(4);
            int startHeightPoint = bm.getHeight() / 2 + (bm.getHeight() - lineHeight * productService.length) / 5;
            for (int i = 0; i < painRelievers.length; i++) {
                myCanvas.drawText(painRelievers[i], convertDpToPx(170), startHeightPoint + i * lineHeight, paint);
            }
        }

        int gainsMaxLength = 15;
        String[] gains = getStringFormatted(gainsList, gainsMaxLength);
        if (gains.length > 0) {
            paint.getTextBounds(gains[0], 0, 1, bounds);
            int lineHeight = bounds.height()+convertDpToPx(4);
            int startHeightPoint = (bm.getHeight() - lineHeight * productService.length) / 3;
            for (int i = 0; i < gains.length; i++) {
                myCanvas.drawText(gains[i], convertDpToPx(350), startHeightPoint + i * lineHeight, paint);
            }
        }

        int painsMaxLength = 15;
        String[] pains = getStringFormatted(painsList, painsMaxLength);
        if (pains.length > 0) {
            paint.getTextBounds(productService[0], 0, 1, bounds);
            int lineHeight = bounds.height()+convertDpToPx(4);
            int startHeightPoint = bm.getHeight() / 2 + (bm.getHeight() - lineHeight * productService.length) / 5;
            for (int i = 0; i < pains.length; i++) {
                myCanvas.drawText(pains[i], convertDpToPx(350), startHeightPoint + i * lineHeight, paint);
            }
        }

        int customerJobsMaxLength = 15;
        String[] customerJobs = getStringFormatted(customerJobsList, customerJobsMaxLength);
        if (customerJobs.length > 0) {
            paint.getTextBounds(customerJobs[0], 0, 1, bounds);
            int lineHeight = bounds.height()+convertDpToPx(4);
            int startHeightPoint = (bm.getHeight() - lineHeight * productService.length) / 2;
            for (int i = 0; i < customerJobs.length; i++) {
                myCanvas.drawText(customerJobs[i], convertDpToPx(480), startHeightPoint + i * lineHeight, paint);
            }
        }

        return bm;
    }

    private String[] getStringFormatted(List<String> list, int maxLength) {
        ArrayList<String> resultList = new ArrayList<String>();

        for (int i = 0 ; i < list.size() ; i++) {
            int length = list.get(i).length();

            if (length <= maxLength)
                resultList.add(list.get(i));
            else {
                int totalLine = length/maxLength + 1;

                for (int j = 0 ; j < totalLine ; j++) {
                    String stringToAdd = "";

                    if (j == totalLine-1)
                        stringToAdd += list.get(i).substring(j*maxLength, j*maxLength+length%maxLength).trim();
                    else {
                        if (j == totalLine-2 && length%maxLength == 1) { //Handle 1 single character to avoid return for 1 char
                            stringToAdd += list.get(i).substring(j * maxLength).trim();
                            resultList.add(stringToAdd);
                            break;
                        }
                        else
                            stringToAdd += list.get(i).substring(j * maxLength, j * maxLength + maxLength).trim() + "-";
                    }

                    resultList.add(stringToAdd);
                }
            }

            resultList.add("");
        }

        return resultList.toArray(new String[resultList.size()]);
    }

    private int convertDpToPx(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class ExportView extends View {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        View rootView;

        public ExportView(Context context, View rootView) {
            super(context);
            this.rootView = rootView;

            float scale = getResources().getDisplayMetrics().density;
            paint.setTextSize((int) (13 * scale));
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }

        @Override
        public void onDraw(Canvas screenCanvas) {
            screenCanvas.drawBitmap(generateBitmap(), 0, 0, paint);
        }
    }
}