package vn.edu.vhu.app_eat.ui.tracking;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import vn.edu.vhu.app_eat.utils.UserManager;

public class InteractivePinMapView extends View {

    private Paint bgPaint;
    private Paint parkPaint;
    private Paint waterPaint;
    private Paint streetPaint;
    private Paint streetBorderPaint;
    private Paint streetTextPaint;
    private Paint landmarkPaint;
    private Paint pinPaint;
    private Paint targetPaint;
    private Paint textPaint;

    // Pin Position (Normalized 0.0 to 1.0)
    private float pinX = UserManager.DEFAULT_PIN_X;
    private float pinY = UserManager.DEFAULT_PIN_Y;
    private boolean isDragging = false;
    private float pinBounceOffset = 0f;

    public interface OnPinMovedListener {
        void onPinMoved(float xPercent, float yPercent, String address);
    }
    private OnPinMovedListener pinMovedListener;

    public InteractivePinMapView(Context context) {
        super(context);
        init();
    }

    public InteractivePinMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InteractivePinMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnPinMovedListener(OnPinMovedListener listener) {
        this.pinMovedListener = listener;
    }

    private void init() {
        pinX = UserManager.getUserPinX(getContext());
        pinY = UserManager.getUserPinY(getContext());

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.parseColor("#F3F3ED"));

        parkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        parkPaint.setColor(Color.parseColor("#D4EED8"));

        waterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        waterPaint.setColor(Color.parseColor("#C3E3FA"));

        streetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        streetPaint.setColor(Color.WHITE);
        streetPaint.setStyle(Paint.Style.STROKE);
        streetPaint.setStrokeCap(Paint.Cap.ROUND);

        streetBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        streetBorderPaint.setColor(Color.parseColor("#E0E2E5"));
        streetBorderPaint.setStyle(Paint.Style.STROKE);
        streetBorderPaint.setStrokeCap(Paint.Cap.ROUND);

        streetTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        streetTextPaint.setColor(Color.parseColor("#80868B"));
        streetTextPaint.setTextSize(26f);
        streetTextPaint.setTextAlign(Paint.Align.CENTER);

        landmarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        pinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        targetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        targetPaint.setStyle(Paint.Style.STROKE);
        targetPaint.setStrokeWidth(3f);
        targetPaint.setPathEffect(new DashPathEffect(new float[]{12f, 10f}, 0));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setPinPosition(float x, float y) {
        this.pinX = Math.max(0.10f, Math.min(0.90f, x));
        this.pinY = Math.max(0.12f, Math.min(0.88f, y));
        triggerBounceAnimation();
        notifyAddressChange();
        invalidate();
    }

    public float getPinX() {
        return pinX;
    }

    public float getPinY() {
        return pinY;
    }

    public String getCurrentAddress() {
        return deduceAddress(pinX, pinY);
    }

    private String deduceAddress(float x, float y) {
        if (x > 0.60f && y > 0.65f) {
            return "123 Nguyễn Huệ, Bến Nghé, Quận 1, TP.HCM";
        } else if (x > 0.65f && y <= 0.45f) {
            return "Vincom Center, 72 Lê Thánh Tôn, Bến Nghé, Quận 1, TP.HCM";
        } else if (x > 0.60f && y > 0.45f && y <= 0.65f) {
            return "Tòa tháp Bitexco, 2 Hải Triều, Bến Nghé, Quận 1, TP.HCM";
        } else if (x <= 0.35f && y > 0.60f) {
            return "Chợ Bến Thành, Lê Lợi, Phường Bến Thành, Quận 1, TP.HCM";
        } else if (x <= 0.35f && y <= 0.40f) {
            return "Dinh Độc Lập, 135 Nam Kỳ Khởi Nghĩa, Quận 1, TP.HCM";
        } else if (x < 0.40f && y > 0.40f && y <= 0.60f) {
            return "Công viên 23/9, Phạm Ngũ Lão, Quận 1, TP.HCM";
        } else if (Math.abs(y - 0.58f) < 0.08f) {
            return "45 Lê Lợi, Phường Bến Nghé, Quận 1, TP.HCM";
        } else if (Math.abs(x - 0.42f) < 0.08f) {
            return "88 Pasteur, Phường Bến Nghé, Quận 1, TP.HCM";
        } else {
            int num = Math.round(x * 120 + y * 90) + 12;
            return "Số " + num + " Phố Trung Tâm, Quận 1, TP.HCM";
        }
    }

    private void notifyAddressChange() {
        if (pinMovedListener != null) {
            pinMovedListener.onPinMoved(pinX, pinY, deduceAddress(pinX, pinY));
        }
    }

    private void triggerBounceAnimation() {
        ValueAnimator anim = ValueAnimator.ofFloat(-24f, 0f);
        anim.setDuration(350);
        anim.setInterpolator(new OvershootInterpolator(2.5f));
        anim.addUpdateListener(animation -> {
            pinBounceOffset = (float) animation.getAnimatedValue();
            invalidate();
        });
        anim.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return super.onTouchEvent(event);

        float px = event.getX() / w;
        float py = event.getY() / h;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDragging = true;
                pinX = Math.max(0.10f, Math.min(0.90f, px));
                pinY = Math.max(0.12f, Math.min(0.88f, py));
                pinBounceOffset = -18f;
                notifyAddressChange();
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    pinX = Math.max(0.10f, Math.min(0.90f, px));
                    pinY = Math.max(0.12f, Math.min(0.88f, py));
                    notifyAddressChange();
                    invalidate();
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                triggerBounceAnimation();
                notifyAddressChange();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;

        // 1. Background
        canvas.drawRect(0, 0, w, h, bgPaint);

        // 2. Water / River
        Path riverPath = new Path();
        riverPath.moveTo(0, h * 0.88f);
        riverPath.quadTo(w * 0.5f, h * 0.82f, w, h * 0.90f);
        riverPath.lineTo(w, h);
        riverPath.lineTo(0, h);
        riverPath.close();
        canvas.drawPath(riverPath, waterPaint);

        // 3. Parks
        RectF park1 = new RectF(w * 0.07f, h * 0.40f, w * 0.35f, h * 0.55f);
        canvas.drawRoundRect(park1, 14f, 14f, parkPaint);

        RectF park2 = new RectF(w * 0.07f, h * 0.65f, w * 0.35f, h * 0.80f);
        canvas.drawRoundRect(park2, 14f, 14f, parkPaint);

        streetTextPaint.setTextSize(22f);
        canvas.drawText("Công viên 23/9", w * 0.21f, h * 0.48f, streetTextPaint);

        // 4. City Street Grid
        drawStreets(canvas, w, h);

        // 5. Street Names & Landmarks
        streetTextPaint.setTextSize(24f);
        canvas.drawText("Đường Pasteur", w * 0.28f, h * 0.17f, streetTextPaint);
        canvas.drawText("Đường Lê Lợi", w * 0.54f, h * 0.53f, streetTextPaint);
        canvas.drawText("Phố Nguyễn Huệ", w * 0.80f, h * 0.45f, streetTextPaint);

        // Landmark Badges
        drawLandmark(canvas, w * 0.80f, h * 0.26f, "Vincom Center", "🏬");
        drawLandmark(canvas, w * 0.72f, h * 0.58f, "Bitexco Tower", "🏢");
        drawLandmark(canvas, w * 0.22f, h * 0.70f, "Chợ Bến Thành", "🛒");

        // 6. Interactive Pin
        float currentX = w * pinX;
        float currentY = h * pinY;
        drawInteractivePin(canvas, currentX, currentY);
    }

    private void drawStreets(Canvas canvas, int w, int h) {
        streetBorderPaint.setStrokeWidth(28f);
        streetPaint.setStrokeWidth(24f);

        float[][] streets = {
                {0, h * 0.22f, w, h * 0.22f},
                {0, h * 0.58f, w, h * 0.58f},
                {w * 0.42f, 0, w * 0.42f, h},
                {w * 0.78f, 0, w * 0.78f, h},
                {0, h * 0.75f, w, h * 0.75f},
                {w * 0.15f, 0, w * 0.15f, h * 0.35f}
        };

        for (float[] s : streets) {
            canvas.drawLine(s[0], s[1], s[2], s[3], streetBorderPaint);
            canvas.drawLine(s[0], s[1], s[2], s[3], streetPaint);
        }
    }

    private void drawLandmark(Canvas canvas, float x, float y, String name, String emoji) {
        landmarkPaint.setColor(Color.parseColor("#42000000"));
        canvas.drawCircle(x, y + 2f, 12f, landmarkPaint);

        landmarkPaint.setColor(Color.parseColor("#607D8B"));
        canvas.drawCircle(x, y, 11f, landmarkPaint);

        landmarkPaint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, 9f, landmarkPaint);

        textPaint.setTextSize(14f);
        textPaint.setColor(Color.parseColor("#37474F"));
        textPaint.setFakeBoldText(true);
        canvas.drawText(emoji + " " + name, x, y + 26f, textPaint);
    }

    private void drawInteractivePin(Canvas canvas, float x, float y) {
        // Target Reticle on Ground
        targetPaint.setColor(Color.parseColor("#EA4335"));
        canvas.drawCircle(x, y, 32f, targetPaint);
        canvas.drawCircle(x, y, 10f, targetPaint);

        // Ground shadow of pin
        pinPaint.setColor(Color.parseColor("#33000000"));
        canvas.drawOval(new RectF(x - 16f, y - 6f, x + 16f, y + 6f), pinPaint);

        // Pin Body (Lifted when dragging/bouncing)
        float pinHeadY = y - 48f + pinBounceOffset;

        // Pin stem/needle
        pinPaint.setColor(Color.parseColor("#B71C1C"));
        Path stem = new Path();
        stem.moveTo(x - 8f, pinHeadY + 12f);
        stem.lineTo(x, y);
        stem.lineTo(x + 8f, pinHeadY + 12f);
        stem.close();
        canvas.drawPath(stem, pinPaint);

        // Pin Head Outer Circle
        pinPaint.setColor(Color.parseColor("#EA4335"));
        canvas.drawCircle(x, pinHeadY, 26f, pinPaint);

        // Pin Head Inner White
        pinPaint.setColor(Color.WHITE);
        canvas.drawCircle(x, pinHeadY, 14f, pinPaint);

        // Pin Icon
        textPaint.setTextSize(22f);
        textPaint.setColor(Color.parseColor("#EA4335"));
        canvas.drawText("📍", x, pinHeadY + 8f, textPaint);

        // Floating "Ghim điểm này" Tooltip Bubble
        String tooltip = isDragging ? "🎯 Đang di chuyển ghim..." : "📍 Điểm giao hàng";
        textPaint.setTextSize(20f);
        textPaint.setFakeBoldText(true);
        float tw = textPaint.measureText(tooltip);
        RectF bubble = new RectF(x - (tw / 2f) - 16f, pinHeadY - 52f, x + (tw / 2f) + 16f, pinHeadY - 18f);

        pinPaint.setColor(Color.parseColor("#202124"));
        canvas.drawRoundRect(bubble, 16f, 16f, pinPaint);

        textPaint.setColor(Color.WHITE);
        canvas.drawText(tooltip, x, pinHeadY - 30f, textPaint);
    }
}