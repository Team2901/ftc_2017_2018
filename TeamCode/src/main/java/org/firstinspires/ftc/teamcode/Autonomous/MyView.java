package org.firstinspires.ftc.teamcode.Autonomous;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import org.firstinspires.ftc.teamcode.R;

/**
 * Created by butterss21317 on 10/14/2017.
 */

class MyView extends View {

    Bitmap myBitmap;
    int minWidth, minHeight;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        myBitmap = BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_launcher);
        minWidth = myBitmap.getWidth();
        minHeight = myBitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(myBitmap, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(minWidth, minHeight);
    }

}