package com.angrybirds.game;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {
    protected float x, y;
    protected float size;
    protected int color;

    public GameObject(float x, float y, float size, int color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public abstract void draw(Canvas canvas, Paint paint);
    public abstract void update();
}
