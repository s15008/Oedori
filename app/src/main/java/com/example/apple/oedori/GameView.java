package com.example.apple.oedori;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by apple on 2016/09/21.
 */

public class GameView extends View {
    // オエー鳥描画
    private Bitmap bitmapOedori;
    private Bitmap bitmapOedoriGaman;
    private Bitmap bitmapOedoriOe;
    private int oedoriStatus;
    private final int STATUS_GAMAN = 0;
    private final int STATUS_OE = 1;
    private int oedoriAnimCounter;
    private int oedoriAnimRange;
    private final int ANIMERANGE_DEFAULT = 0;
    private final int ANIMERANGE_SMALL = 2;
    private final int ANIMERANGE_MIDDLE = 8;
    private final int ANIMERANGE_LARGE = 16;
    private final int ANIMERANGE_MAXHEART = 42;

    // オエーゲージ
    private int oeGage;
    private int oeSpeed;
    private int oeGageMax;
    private int oeCounter;

    public GameView(Context context) {
        super(context);

        // オエー鳥描画
        bitmapOedori = BitmapFactory.decodeResource(getResources(), R.drawable.oedori);
        int bitmapWidth = bitmapOedori.getWidth();
        bitmapOedoriGaman = Bitmap.createBitmap(bitmapOedori, 0, 0, bitmapWidth / 2, bitmapOedori.getHeight());
        bitmapOedoriOe = Bitmap.createBitmap(bitmapOedori, bitmapWidth / 2, 0, bitmapWidth / 2, bitmapOedori.getHeight());
        oedoriStatus = STATUS_GAMAN;
        oedoriAnimCounter = 0;
        oedoriAnimRange = ANIMERANGE_DEFAULT;

        // オエーゲージ
        oeGage = 0;
        oeSpeed = 10;
        oeGageMax = 1000;
        oeCounter = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {

        // オエー時ディレイ処理
        if (oedoriStatus == STATUS_OE) {
            try {
                Thread.sleep(800);
            }
            catch (InterruptedException e) {
            }
            oedoriStatus = STATUS_GAMAN;
        }

        // オエーゲージ更新処理
        oeGage += oeSpeed;
        if (oeGage > oeGageMax) {
            // オエーゲージマックス突破処理
            Log.d("GAMEVIEW", "gameView.draw oeGageMax " + oeGage);
            oedoriStatus = STATUS_OE;
            oeGage = 0;
            oeCounter++;
            oedoriAnimCounter = 0;
        }
        else {
            // 我慢時の処理
            oedoriAnimCounter++;

            int oePercent = (int)(((float)oeGage / (float)oeGageMax) * 100);
            if (oePercent < 10) {
                oedoriAnimRange = ANIMERANGE_DEFAULT;
            }
            else if (oePercent < 40) {
                oedoriAnimRange = ANIMERANGE_SMALL;
            }
            else if (oePercent < 60) {
                oedoriAnimRange = ANIMERANGE_MIDDLE;
            }
            else if (oePercent < 80) {
                oedoriAnimRange = ANIMERANGE_LARGE;
            }
            else {
                oedoriAnimRange = ANIMERANGE_MAXHEART;
            }
            Log.d("GAMEVIEW", oePercent + "% " + oedoriAnimRange);
        }

        // オエー鳥描画
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        int bitmapWidth = bitmapOedoriGaman.getWidth();
        int bitmapHeight = bitmapOedoriGaman.getHeight();
        if (oedoriStatus == STATUS_GAMAN) {
            int oedoriDrawX = canvasWidth / 2  - bitmapWidth / 2;
            int oedoriDrawY = canvasHeight / 2 - bitmapHeight / 2;
            if (oedoriAnimCounter % 2 == 0) {
                canvas.drawBitmap(bitmapOedoriGaman, oedoriDrawX - oedoriAnimRange, oedoriDrawY, null);
            }
            else {
                canvas.drawBitmap(bitmapOedoriGaman, oedoriDrawX + oedoriAnimRange, oedoriDrawY, null);
            }
        }
        else {
            canvas.drawBitmap(bitmapOedoriOe, canvasWidth / 2  - bitmapWidth / 2, canvasHeight / 2 - bitmapHeight / 2, null);
        }

        // オエーゲージ描画
        Paint gagePaint = new Paint();
        gagePaint.setColor(Color.RED);
        float gageWidth = ((float)oeGage / (float)oeGageMax) * canvasWidth;
        canvas.drawRect(0, 0, gageWidth, 10, gagePaint);

        // オエー回数描画
        Paint countPaint = new Paint();
        countPaint.setTextSize(100);
        canvas.drawText(oeCounter + "オエー", canvasWidth/2, canvasHeight/4, countPaint);

        invalidate();

        // むりやり遅延処理 マルチスレッドでやるべき
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (oeGage >= oeSpeed * 2) {
            oeGage -= oeSpeed * 2;
        }

        return super.onTouchEvent(event);
    }
}
