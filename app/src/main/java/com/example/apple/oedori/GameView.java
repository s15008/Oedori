package com.example.apple.oedori;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by apple on 2016/09/21.
 */

public class GameView extends View {
    private Bitmap bitmapOedori;
    private Bitmap bitmapOedoriGaman;
    private Bitmap bitmapOedoriOe;
    private int oedoriStatus;
    private final int STATUS_GAMAN = 0;
    private final int STATUS_OE = 1;

    public GameView(Context context) {
        super(context);

        bitmapOedori = BitmapFactory.decodeResource(getResources(), R.drawable.oedori);
        int bitmapWidth = bitmapOedori.getWidth();
        bitmapOedoriGaman = Bitmap.createBitmap(bitmapOedori, 0, 0, bitmapWidth / 2, bitmapOedori.getHeight());
        bitmapOedoriOe = Bitmap.createBitmap(bitmapOedori, bitmapWidth / 2, 0, bitmapWidth / 2, bitmapOedori.getHeight());
        oedoriStatus = STATUS_GAMAN;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int bitmapWidth = bitmapOedoriGaman.getWidth();
        int bitmapHeight = bitmapOedoriGaman.getHeight();
        if (oedoriStatus == STATUS_GAMAN) {
            canvas.drawBitmap(bitmapOedoriGaman, canvasWidth / 2  - bitmapWidth / 2, canvasHeight / 2 - bitmapHeight / 2, null);
        }
        else {
            canvas.drawBitmap(bitmapOedoriOe, canvasWidth / 2  - bitmapWidth / 2, canvasHeight / 2 - bitmapHeight / 2, null);
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("GAMEVIEW", "gameView.onTouchEvent " + oedoriStatus);
        oedoriStatus = (oedoriStatus + 1) % 2;
        Log.d("GAMEVIEW", "gameView.onTouchEvent " + oedoriStatus);
        return super.onTouchEvent(event);
    }
}
