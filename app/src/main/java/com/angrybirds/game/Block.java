package com.angrybirds.game;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Block extends GameObject {
    private float width;
    private float height;
    private int health = 100;
    private boolean destroyed = false;
    private float velocityY = 0;
    private static final float GRAVITY = 0.3f;
    private static final float GROUND_Y = 1920 - 150;

    public Block(float x, float y, float width, float height, int color) {
        super(x, y, width, color);
        this.width = width;
        this.height = height;
        this.size = (width + height) / 2;
    }

    public void hit() {
        health -= 30;
        if (health <= 0) {
            destroyed = true;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void update() {
        // Apply gravity if not on ground
        if (y + height < GROUND_Y) {
            velocityY += GRAVITY;
            y += velocityY;

            // Check ground collision
            if (y + height >= GROUND_Y) {
                y = GROUND_Y - height;
                velocityY = 0;
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (destroyed) return;

        paint.setColor(color);
        canvas.drawRect(x, y, x + width, y + height, paint);

        // Draw wood texture (simple lines)
        paint.setColor(android.graphics.Color.rgb(100, 50, 10));
        paint.setStrokeWidth(2);
        for (int i = 0; i < 3; i++) {
            float lineY = y + height * i / 3;
            canvas.drawLine(x, lineY, x + width, lineY, paint);
        }
    }
}
