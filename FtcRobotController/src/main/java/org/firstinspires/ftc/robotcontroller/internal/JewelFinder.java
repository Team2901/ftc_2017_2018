package org.firstinspires.ftc.robotcontroller.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Environment;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;

/**
 * Created by butterss21317 on 11/14/2017.
 */

public class JewelFinder extends TextView implements View.OnTouchListener {
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    public int pWidth;
    public int pHeight;


    public int sampleLeftXPct = 0;
    public int sampleRightXPct = 20;
    public int sampleTopYPct = 0;
    public int sampleBotYPct = 20;



    public JewelFinder(Context context) {
        super(context);
        init(null, 0);
    }

    public JewelFinder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public JewelFinder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setBackgroundColor(0x55FF2500);
        this.setLayoutParams(new FrameLayout.LayoutParams(175,175));
        this.setOnTouchListener(this);

    }

    public void moveBox(final float xpct,final float ypct) {
        pWidth = ((View) this.getParent()).getWidth();
        pHeight = ((View) this.getParent()).getHeight();

        final int pWidth = this.pWidth;
        final int pHeight = this.pHeight;

        this.post(new Runnable() {

            @Override
            public void run() {
                setX(((float) xpct) / 100 * pWidth);
                setY(((float) ypct) / 100 * pHeight);

            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        pWidth = ((View) this.getParent()).getWidth();
        pHeight = ((View) this.getParent()).getHeight();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    float dx, dy;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("", "on  touch");

        float rawX = event.getRawX();
        float rawY = event.getRawY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.setText(String.format("Hi"));
            dx = v.getX() - event.getRawX();
            dy = v.getY() - event.getRawY();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = getXBound(rawX + dx);
            float y = getYBound(rawY + dy);

            sampleLeftXPct = (int) ((x/pWidth)*100);
            sampleRightXPct = (int) (((x + this.getWidth())/pWidth)*100);
            sampleTopYPct = (int) ((y/pHeight)*100);
            sampleBotYPct = (int) (((y + this.getHeight())/pHeight)*100);

            this.setText(String.format("%.1f %.1f", x, y));

            v.animate()
                    .x(x)
                    .y(y)
                    .setDuration(0)
                    .start();
            return true;
        }
        return false;
    }
    public float getXBound(float valueX){

        float x;

        if(valueX > pWidth - this.getWidth())// box x size
            x = pWidth - this.getWidth() ;
        else if (valueX  < 0)
            x = 0 ;
        else
            x  = valueX;

        return x;
    }
    public float getYBound(float valueY){

        float y;

        if(valueY > pHeight - this.getHeight()) //half box y size
            y = pHeight - this.getHeight() ;
        else if(valueY  < 0)
            y = 0 ;
        else
            y  = valueY;

        return y;
    }
}