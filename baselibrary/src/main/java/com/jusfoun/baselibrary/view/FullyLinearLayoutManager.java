package com.jusfoun.baselibrary.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wang on 2016/11/17.
 * scrollview嵌套recyclerviewlinearlayoutmanager
 */

public class FullyLinearLayoutManager extends LinearLayoutManager {
    private int[] mMeasuredDimension = new int[2];
    public FullyLinearLayoutManager(Context context) {
        super(context);
    }

    public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FullyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode= View.MeasureSpec.getMode(widthSpec);
        final int heightMode=View.MeasureSpec.getMode(heightSpec);
        final int widthSize=View.MeasureSpec.getSize(widthSpec);
        final int heightSize=View.MeasureSpec.getSize(heightSpec);

        int width = 0;
        int height = 0;
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);

            if (getOrientation() == HORIZONTAL) {
                width+=mMeasuredDimension[0];
                if (i == 0) {
                    height = mMeasuredDimension[1];
                }
            } else {
                height+=mMeasuredDimension[1];
                if (i == 0) {
                    width = mMeasuredDimension[0];
                }
            }
        }

        switch (widthMode) {
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }
        setMeasuredDimension(width, height);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler,int position,
                                   int widthSpec,int heightSpec,int[] measuredDimension){
        if (position >= getItemCount())
            return;
        try{
            View view=recycler.getViewForPosition(0);//TODO 数组越界
            if (view!=null){
                RecyclerView.LayoutParams p= (RecyclerView.LayoutParams) view.getLayoutParams();
                int childWidthSpec= ViewGroup.getChildMeasureSpec(widthSpec,getPaddingLeft()+getPaddingRight(),p.width);
                int childHeightSpec= ViewGroup.getChildMeasureSpec(widthSpec,getPaddingTop()+getPaddingBottom(),p.width);
                view.measure(childWidthSpec,childHeightSpec);
                measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                recycler.recycleView(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
