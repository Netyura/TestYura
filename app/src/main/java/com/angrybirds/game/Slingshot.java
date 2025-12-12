package com.angrybirds.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Slingshot {
    public float x, y;
    private static final float HEIGHT = 120;
    private static final float WIDTH = 40;

    public Slingshot(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas, Paint paint) {
        // Draw base
        paint.setColor(Color.rgb(101, 67, 33));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x - WIDTH/2, y, x + WIDTH/2, y + 20, paint);

        // Draw left arm
        paint.setStrokeWidth(12);
        canvas.drawLine(x - 15, y, x - 15, y - HEIGHT, paint);

        // Draw right arm
        canvas.drawLine(x + 15, y, x + 15, y - HEIGHT, paint);

        // Draw connector
        paint.setColor(Color.rgb(80, 50, 20));
        canvas.drawLine(x - 15, y - HEIGHT, x + 15, y - HEIGHT, paint);
    }
}
