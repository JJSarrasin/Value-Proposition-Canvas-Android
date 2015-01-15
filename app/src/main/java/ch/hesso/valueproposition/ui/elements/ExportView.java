package ch.hesso.valueproposition.ui.elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import ch.hesso.valueproposition.R;

class ExportView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;

    public ExportView(Context context) {
        super(context);
        init(context);
    }

    public ExportView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public ExportView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        float scale = getResources().getDisplayMetrics().density;

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize((int) (14 * scale));

        this.setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View exportLayout = inflater.inflate(R.layout.layout_export, null);
        exportLayout.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(exportLayout.getDrawingCache());
        exportLayout.setDrawingCacheEnabled(false);

        canvas.setBitmap(bm);

        //Draw data
        Rect bounds = new Rect();

        paint.getTextBounds("toto", 0, "toto".length(), bounds);
        canvas.drawText("toto", 2, 2, paint);

       /* String productService = getStringFormatted(productsServiceList);
        paint.getTextBounds(productService, 0, productService.length(), bounds);
        canvas.drawText(productService, 2, (bm.getHeight() + bounds.height())/2, paint);

        String gainCreators = getStringFormatted(gainCreatorsList);
        paint.getTextBounds(gainCreators, 0, gainCreators.length(), bounds);
        canvas.drawText(gainCreators, 10, (bm.getHeight() + bounds.height())/3, paint);

        String painRelievers = getStringFormatted(painRelieversList);
        paint.getTextBounds(painRelievers, 0, painRelievers.length(), bounds);
        canvas.drawText(painRelievers, 10, 20, paint);

        String gains = getStringFormatted(gainsList);
        paint.getTextBounds(gains, 0, gains.length(), bounds);
        canvas.drawText(gains, 30, (bm.getHeight() + bounds.height())/3, paint);

        String pains = getStringFormatted(painsList);
        paint.getTextBounds(pains, 0, pains.length(), bounds);
        canvas.drawText(pains, 30, 20, paint);

        String customerJobs = getStringFormatted(customerJobsList);
        paint.getTextBounds(customerJobs, 0, customerJobs.length(), bounds);
        canvas.drawText(customerJobs, 40, (bm.getHeight() + bounds.height()) / 2, paint);*/
    }

    private String getStringFormatted(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < list.size() ; i++) {
            sb.append(list.get(i));
            sb.append("\n");
        }
        return sb.toString();
    }
}