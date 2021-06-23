package com.joeyderuiter.cookieclicker.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.joeyderuiter.cookieclicker.R;

import java.time.Duration;
import java.time.LocalTime;

public class CookieButton extends View {
    private final static String TAG = "CookieButton";

    private LocalTime lastEvent = null;
    private Runnable onClickHandler = null;

    private int diameter    = 0;
    private int maxWidth    = 0;
    private int maxHeight   = 0;

    private final Drawable drawable;

    public CookieButton(Context context) {
        this(context, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public CookieButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        Resources res = getResources();
        drawable = ResourcesCompat.getDrawable(res, R.drawable.cookie, null);

        this.setOnTouchListener((view, event) -> {
            this.invalidate();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int eventX = (int) event.getX();
                int eventY = (int) event.getY();

                if (InCircle(eventX, eventY)) {
                    if (this.onClickHandler != null)
                        this.onClickHandler.run();

                    lastEvent = LocalTime.now();
                    return true;
                }
            }

            return false;
        });
    }
    
    private boolean InCircle(int x, int y) {
        int radius = diameter / 2;

        int offsetLeft  = (maxWidth - diameter) / 2;
        int offsetTop   = (maxHeight - diameter) / 2;

        int centerX = offsetLeft + radius;
        int centerY = offsetTop + radius;

        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) < Math.pow(radius, 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        diameter    = (int) Math.min(w, h);
        maxWidth    = w;
        maxHeight   = h;
    }

    protected void onDraw(Canvas canvas) {
        this.drawCookie();
        drawable.draw(canvas);
    }

    private double calculateScale() {
        double baseScale = 0.8;
        double extraScale = 0.15;

        if (lastEvent == null) return baseScale;
        
        LocalTime currentTime = LocalTime.now();
        long millisBetween = Duration.between(lastEvent, currentTime).toMillis();
        long maxTimeBetween = 250;

        if (millisBetween < maxTimeBetween)
            this.invalidate();

        long clampedMillis = Math.min(millisBetween, maxTimeBetween);
        double scale = (double) clampedMillis / (double) maxTimeBetween;

        return baseScale + (extraScale - extraScale * scale);
    }

    public void registerHandler(Runnable runnable) {
        this.onClickHandler = runnable;
    }

    private void drawCookie() {
        double scale = calculateScale();

        int diameter = (int) (this.diameter * scale);

        int offsetLeft  = (maxWidth - diameter) / 2;
        int offsetTop   = (maxHeight - diameter) / 2;

        drawable.setBounds(
            offsetLeft,
            offsetTop,
            offsetLeft + diameter,
            offsetTop + diameter
        );
    }
}
