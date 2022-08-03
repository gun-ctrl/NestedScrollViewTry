package com.example.nestedscrollviewtry.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class MyNestedScrollChildL extends LinearLayout implements NestedScrollingChild {
    private NestedScrollingChildHelper helper;
    public MyNestedScrollChildL(Context context) {
        super(context);
        init(context);
    }

    public MyNestedScrollChildL(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyNestedScrollChildL(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MyNestedScrollChildL(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        helper = new NestedScrollingChildHelper(this);
        helper.setNestedScrollingEnabled(true);
    }
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        helper.setNestedScrollingEnabled(enabled);
    }

    int realHeight = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        realHeight = 0;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),MeasureSpec.UNSPECIFIED);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            realHeight += view.getMeasuredHeight();
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }
    /**
     * 是否可以嵌套滑动
     * @return
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return helper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return helper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        helper.stopNestedScroll();
    }
    @Override
    public boolean hasNestedScrollingParent() {
        return helper.hasNestedScrollingParent();
    }
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return helper.dispatchNestedScroll(dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed,offsetInWindow);
    }
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return helper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return helper.dispatchNestedFling(velocityX,velocityY,consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return helper.dispatchNestedPreFling(velocityX,velocityY);
    }

    private int mLastTouchX;
    private int mLastTouchY;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int[] consumed = new int[2];
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                mLastTouchY = (int) (event.getRawY()+ .5f);
                int nestedScrollViewAxis = ViewCompat.SCROLL_AXIS_NONE;
                nestedScrollViewAxis|=ViewCompat.SCROLL_AXIS_HORIZONTAL;
                startNestedScroll(nestedScrollViewAxis);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int x = (int)(event.getRawX()+ .5f);
                int y = (int)(event.getRawY()+ .5f);
                int dx = mLastTouchX -x;
                int dy  =mLastTouchY - y;
                mLastTouchX = x;
                mLastTouchY = y;
                if (dispatchNestedPreScroll(dx,dy,consumed,null)){
                    dy -=consumed[1];
                    if (dy==0){
                        return true;
                    }
                }else {
                    scrollBy(0,dy);
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: stopNestedScroll();
            case MotionEvent.ACTION_UP:stopNestedScroll();
        }
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y<0){
            y=0;
        }
        if(y>realHeight){
            y = realHeight;
        }
        if (y!=getScrollY()){
            super.scrollTo(x, y);
        }
    }
}
