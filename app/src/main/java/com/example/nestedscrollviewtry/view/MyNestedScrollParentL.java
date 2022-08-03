package com.example.nestedscrollviewtry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

public class MyNestedScrollParentL extends LinearLayout implements NestedScrollingParent {
    private NestedScrollingParentHelper helper;
    public MyNestedScrollParentL(Context context) {
        super(context);
        init(context);
    }

    public MyNestedScrollParentL(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyNestedScrollParentL(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MyNestedScrollParentL(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        helper = new NestedScrollingParentHelper(this);
    }
    int relHeight = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        relHeight = 0;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),MeasureSpec.UNSPECIFIED);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            relHeight+=view.getMeasuredHeight();
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    /**
     * @param child
     * @param target
     * @param nestedScrollAxes
     * @return 一般这里只返回true,如果返回false，parent将不做任何处理
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        helper.onNestedScrollAccepted(child,target,axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        helper.onStopNestedScroll(child);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        View view = getChildAt(0);
        int topHeight = view.getMeasuredHeight();
        int relDy = 0;
        boolean show = showImg(dy);
        boolean hide = hideImg(dy);
        if (show||hide){
            if (dy>topHeight){
                relDy = topHeight;
            }else{
                relDy = dy;
            }
            consumed[1] = relDy;
            scrollBy(0,relDy);
        }
    }

    private boolean showImg(int dy){
        View view = getChildAt(0);
        /**
         * canScrollVertically(-1)内部传的<0则表示向下滑动，如果滑到顶部不能再滑则返回true
         */
        if (dy<0&&getScrollY()>0&&!view.canScrollVertically(-1)){
            return true;
        }
        return false;
    }
    private boolean hideImg(int dy){
        View view = getChildAt(0);
        if(dy>0&&getScrollY()<view.getHeight()){
            return true;
        }
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {
        View view = getChildAt(0);
        if (y<0){
            y=0;
        }
        if (y>view.getHeight()){
            y = view.getHeight();
        }
        if (y!=getScrollY()){
            super.scrollTo(x, y);
        }
    }
}
