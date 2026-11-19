package vn.edu.vhu.app_eat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BarcodeView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String barcodeText = "MZ892416020261118";

    // Fixed bar width pattern simulating Code 128
    private static final int[] PATTERN = {
            2, 1, 3, 1, 1, 2, 4, 1, 2, 3, 1, 2, 1, 4, 2, 1, 3, 2, 1, 1,
            2, 2, 3, 1, 4, 1, 1, 2, 3, 1, 2, 1, 1, 3, 2, 4, 1, 2, 1, 3,
            3, 1, 2, 2, 1, 4, 1, 2, 2, 3, 1, 1, 2, 4, 1, 2, 3, 1, 2, 1,
            1, 3, 2, 1, 4, 2, 1, 1, 3, 2, 1, 4, 2, 1, 3, 1, 2, 2, 1, 4
    };

    public BarcodeView(Context context) {
        super(context);
        init();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarcodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setColor(0xFF1E293B);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setBarcodeText(String text) {
        this.barcodeText = text;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        int totalUnits = 0;
        for (int p : PATTERN) totalUnits += p;

        float unitWidth = (float) (w - 40) / (float) totalUnits;
        float currentX = 20;

        for (int i = 0; i < PATTERN.length; i++) {
            float barW = PATTERN[i] * unitWidth;
            if (i % 2 == 0) {
                // Draw black bar
                canvas.drawRect(currentX, 0, currentX + barW, h, paint);
            }
            currentX += barW;
        }
    }
}
