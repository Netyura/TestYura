package com.angrybirds.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pig extends GameObject {
    private int health = 100;
    private boolean dead = false;

    public Pig(float x, float y, float size) {
        super(x, y, size, Color.rgb(0, 200, 0));
    }

    public void hit() {
        health -= 50;
        if (health <= 0) {
            dead = true;
        }
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public void update() {
        // Pigs don't move on their own
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (dead) return;

        // Draw pig body
        paint.setColor(color);
        canvas.drawCircle(x, y, size, paint);

        // Draw snout
        paint.setColor(Color.rgb(0, 150, 0));
        canvas.drawCircle(x, y + size/3, size/2, paint);

        // Draw nostrils
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x - size/4, y + size/3, size/8, paint);
        canvas.drawCircle(x + size/4, y + size/3, size/8, paint);

        // Draw eyes
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x - size/3, y - size/4, size/3, paint);
        canvas.drawCircle(x + size/3, y - size/4, size/3, paint);

        paint.setColor(Color.BLACK);
        canvas.drawCircle(x - size/3, y - size/4, size/6, paint);
        canvas.drawCircle(x + size/3, y - size/4, size/6, paint);

        // Draw eyebrows (angry look)
        paint.setStrokeWidth(4);
        canvas.drawLine(x - size/2, y - size/2, x - size/6, y - size/3, paint);
        canvas.drawLine(x + size/2, y - size/2, x + size/6, y - size/3, paint);
    }
}
