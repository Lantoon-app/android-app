package com.bazinga.lantoonnew.home.chapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextDrawable extends Drawable {

    private final String text;
    private int color;
    private final Paint paint;

    public TextDrawable(String text, int color) {

        this.text = text;
        this.color = color;

        this.paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(52f);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(12f, 0, 0, Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawText(text, bounds.centerX() - 15f /*just a lazy attempt to centre the text*/ * text.length(), bounds.centerY() + 15f, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public String getText() {
        return text;
    }
}