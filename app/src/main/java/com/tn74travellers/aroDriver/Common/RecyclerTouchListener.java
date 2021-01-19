package com.tn74travellers.aroDriver.Common;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener
{
    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position) throws JSONException;
    }

    private GestureDetector mGestureDetector;

    public RecyclerTouchListener(Context context, OnItemClickListener listener)
    {
        mListener       = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
    {
        Log.i("onInterceptTouchEvent  ",": e.getX() : "+e.getX()+", e.getY() : "+e.getY());

        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e))
        {
            try
            {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            catch (JSONException e1)
            {
                e1.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}

