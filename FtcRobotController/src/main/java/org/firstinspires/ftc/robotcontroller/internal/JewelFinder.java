package org.firstinspires.ftc.robotcontroller.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by butterss21317 on 11/14/2017.
 */
@SuppressLint("DefaultLocale")
public class JewelFinder extends TextView implements View.OnTouchListener {
    private int pWidth;
    private int pHeight;

    private float dx, dy;

    // Pct is = to box percentage location.
    public int boxLeftXPct = 0;
    public int boxRightXPct = 20;
    public int boxTopYPct = 0;
    public int boxBotYPct = 20;

    public JewelFinder(Context context, int color, String location) {
        this(context, null, color, location);
    }

    public JewelFinder(Context context, AttributeSet attrs, int color, String location) {
        this(context, attrs,  0, color, location );
    }

    public JewelFinder(Context context, AttributeSet attrs, int defStyle, int color, String location) {
        super(context, attrs, defStyle);
        this.setBackgroundColor( color);
        this.setLayoutParams(new FrameLayout.LayoutParams(100,100));
        this.setOnTouchListener(this);
        this.setText(location);
    }
    public void moveTo(List<Integer> config, int width, int height){
        setX(50);
        setY(50);
        setLayoutParams(new FrameLayout.LayoutParams(100,100));
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("", "on  touch");

        float rawX = event.getRawX();
        float rawY = event.getRawY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dx = v.getX() - event.getRawX();
            dy = v.getY() - event.getRawY();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = getXBound(rawX + dx);
            float y = getYBound(rawY + dy);

            boxLeftXPct = (int) ((x/pWidth)*100);
            boxRightXPct = (int) (((x + this.getWidth())/pWidth)*100);
            boxTopYPct = (int) ((y/pHeight)*100);
            boxBotYPct = (int) (((y + this.getHeight())/pHeight)*100);




            v.animate()
                    .x(x)
                    .y(y)
                    .setDuration(0)
                    .start();
            return true;
        }
        return false;
    }

    private float getXBound(float valueX){

        float x;

        if(valueX > pWidth - this.getWidth()) {// box x size
            x = pWidth - this.getWidth();
        } else if (valueX  < 0) {
            x = 0;
        } else {
            x = valueX;
        }
        return x;
    }

    private float getYBound(float valueY){

        float y;

        if(valueY > pHeight - this.getHeight()) //half box y size
            y = pHeight - this.getHeight() ;
        else if(valueY  < 0)
            y = 0 ;
        else
            y  = valueY;

        return y;
    }

    public int getBoxLeftXPct() {
        return boxLeftXPct;
    }

    public int getBoxRightXPct() {
        return boxRightXPct;
    }

    public int getBoxTopYPct() {
        return boxTopYPct;
    }

    public int getBoxBotYPct() {
        return boxBotYPct;
    }

    public List<Integer> getBoxPct()
    {
       List<Integer> configValues = new ArrayList<>();
       configValues.add(boxLeftXPct);
       configValues.add(boxTopYPct);
       configValues.add(boxRightXPct);
       configValues.add(boxBotYPct);
       return configValues;
    }
}