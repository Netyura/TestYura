package com.angrybirds.game;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bird extends GameObject {
    protected float velocityX = 0;
    protected float velocityY = 0;
    private boolean flying = false;
    private static final float GRAVITY = 0.5f;
    private static final float GROUND_Y = 1920 - 150; // Will be adjusted

    public Bird(float x, float y, float size, int color) {
        super(x, y, size, color);
    }

    public void launch(float velX, float velY) {
        this.velocityX = velX;
        this.velocityY = velY;
        this.flying = true;
    }

    public boolean isFlying() {
        return flying;
    }

    @Override
    public void update() {
        if (flying) {
            // Apply gravity
            velocityY += GRAVITY;

            // Update position
            x += velocityX;
            y += velocityY;

            // Check ground collision
            if (y > GROUND_Y) {
                y = GROUND_Y;
                velocityY = -velocityY * 0.3f; // Bounce
                velocityX *= 0.8f; // Friction

                // Stop if moving slowly
                if (Math.abs(velocityX) < 0.5f && Math.abs(velocityY) < 0.5f) {
                    flying = false;
                    velocityX = 0;
                    velocityY = 0;
                }
            }

            // Stop if out of bounds
            if (x < 0 || x > 3000 || y > GROUND_Y + 100) {
                flying = false;
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        canvas.drawCircle(x, y, size, paint);

        // Draw eyes
        paint.setColor(android.graphics.Color.WHITE);
        canvas.drawCircle(x - size/3, y - size/4, size/4, paint);
        canvas.drawCircle(x + size/3, y - size/4, size/4, paint);

        paint.setColor(android.graphics.Color.BLACK);
        canvas.drawCircle(x - size/3, y - size/4, size/6, paint);
        canvas.drawCircle(x + size/3, y - size/4, size/6, paint);
    }

    public void drawAt(Canvas canvas, Paint paint, float drawX, float drawY) {
        paint.setColor(color);
        canvas.drawCircle(drawX, drawY, size, paint);

        // Draw eyes
        paint.setColor(android.graphics.Color.WHITE);
        canvas.drawCircle(drawX - size/3, drawY - size/4, size/4, paint);
        canvas.drawCircle(drawX + size/3, drawY - size/4, size/4, paint);

        paint.setColor(android.graphics.Color.BLACK);
        canvas.drawCircle(drawX - size/3, drawY - size/4, size/6, paint);
        canvas.drawCircle(drawX + size/3, drawY - size/4, size/6, paint);
    }
}
