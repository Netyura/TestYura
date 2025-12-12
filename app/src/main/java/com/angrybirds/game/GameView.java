package com.angrybirds.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private SurfaceHolder surfaceHolder;
    private Paint paint;

    private int screenWidth;
    private int screenHeight;

    private Slingshot slingshot;
    private List<Bird> birds;
    private List<Pig> pigs;
    private List<Block> blocks;
    private Bird currentBird;

    private boolean isDragging = false;
    private float dragX, dragY;

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();

        initGame();
    }

    private void initGame() {
        birds = new ArrayList<>();
        pigs = new ArrayList<>();
        blocks = new ArrayList<>();

        // Will be initialized properly when surface is created
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;

        // Initialize game objects with screen dimensions
        setupLevel1();
    }

    private void setupLevel1() {
        birds.clear();
        pigs.clear();
        blocks.clear();

        // Create slingshot
        slingshot = new Slingshot(200, screenHeight - 200);

        // Create birds
        birds.add(new Bird(200, screenHeight - 200, 30, Color.RED));
        birds.add(new Bird(150, screenHeight - 200, 30, Color.YELLOW));
        birds.add(new Bird(100, screenHeight - 200, 30, Color.BLUE));

        if (!birds.isEmpty()) {
            currentBird = birds.get(0);
        }

        // Create structure with blocks and pigs
        int startX = screenWidth - 400;
        int groundY = screenHeight - 150;

        // Left pillar
        blocks.add(new Block(startX, groundY - 60, 40, 60, Color.rgb(139, 69, 19)));
        blocks.add(new Block(startX, groundY - 120, 40, 60, Color.rgb(139, 69, 19)));

        // Right pillar
        blocks.add(new Block(startX + 120, groundY - 60, 40, 60, Color.rgb(139, 69, 19)));
        blocks.add(new Block(startX + 120, groundY - 120, 40, 60, Color.rgb(139, 69, 19)));

        // Top horizontal blocks
        blocks.add(new Block(startX + 40, groundY - 150, 80, 30, Color.rgb(160, 82, 45)));

        // Add pigs
        pigs.add(new Pig(startX + 60, groundY - 40, 35));
        pigs.add(new Pig(startX + 60, groundY - 180, 35));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (currentBird != null && !currentBird.isFlying()) {
                    float dx = touchX - currentBird.x;
                    float dy = touchY - currentBird.y;
                    if (dx * dx + dy * dy < 50 * 50) {
                        isDragging = true;
                        dragX = touchX;
                        dragY = touchY;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging && currentBird != null) {
                    dragX = touchX;
                    dragY = touchY;

                    // Limit drag distance
                    float dx = dragX - slingshot.x;
                    float dy = dragY - slingshot.y;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance > 150) {
                        dragX = slingshot.x + dx * 150 / distance;
                        dragY = slingshot.y + dy * 150 / distance;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isDragging && currentBird != null) {
                    // Launch bird
                    float velX = (slingshot.x - dragX) * 0.3f;
                    float velY = (slingshot.y - dragY) * 0.3f;
                    currentBird.launch(velX, velY);

                    isDragging = false;
                    birds.remove(0);
                    currentBird = birds.isEmpty() ? null : birds.get(0);
                }
                break;
        }
        return true;
    }

    private void update() {
        // Update all birds
        for (Bird bird : new ArrayList<>(birds)) {
            if (bird.isFlying()) {
                bird.update();

                // Check collisions with pigs
                for (Pig pig : new ArrayList<>(pigs)) {
                    if (checkCollision(bird, pig)) {
                        pig.hit();
                        if (pig.isDead()) {
                            pigs.remove(pig);
                        }
                    }
                }

                // Check collisions with blocks
                for (Block block : new ArrayList<>(blocks)) {
                    if (checkCollision(bird, block)) {
                        bird.velocityY *= -0.5f;
                        bird.velocityX *= 0.8f;
                        block.hit();
                        if (block.isDestroyed()) {
                            blocks.remove(block);
                        }
                    }
                }
            }
        }

        // Update blocks (apply gravity)
        for (Block block : new ArrayList<>(blocks)) {
            block.update();

            // Check block collisions with pigs
            for (Pig pig : new ArrayList<>(pigs)) {
                if (checkCollision(block, pig)) {
                    pig.hit();
                    if (pig.isDead()) {
                        pigs.remove(pig);
                    }
                }
            }
        }

        // Check for level completion
        if (pigs.isEmpty()) {
            // Level won! Could add level transition here
        }
    }

    private boolean checkCollision(GameObject obj1, GameObject obj2) {
        float dx = obj1.x - obj2.x;
        float dy = obj1.y - obj2.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (obj1.size + obj2.size) / 2;
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();

            // Draw background (sky)
            canvas.drawColor(Color.rgb(135, 206, 235));

            // Draw ground
            paint.setColor(Color.rgb(34, 139, 34));
            canvas.drawRect(0, screenHeight - 150, screenWidth, screenHeight, paint);

            // Draw slingshot
            if (slingshot != null) {
                slingshot.draw(canvas, paint);

                // Draw slingshot bands when dragging
                if (isDragging && currentBird != null) {
                    paint.setColor(Color.rgb(101, 67, 33));
                    paint.setStrokeWidth(8);
                    canvas.drawLine(slingshot.x - 15, slingshot.y, dragX, dragY, paint);
                    canvas.drawLine(slingshot.x + 15, slingshot.y, dragX, dragY, paint);
                }
            }

            // Draw blocks
            for (Block block : blocks) {
                block.draw(canvas, paint);
            }

            // Draw pigs
            for (Pig pig : pigs) {
                pig.draw(canvas, paint);
            }

            // Draw birds
            for (Bird bird : birds) {
                if (isDragging && bird == currentBird) {
                    bird.drawAt(canvas, paint, dragX, dragY);
                } else {
                    bird.draw(canvas, paint);
                }
            }

            // Draw score/info
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("Birds: " + birds.size(), 50, 50, paint);
            canvas.drawText("Pigs: " + pigs.size(), 50, 100, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
