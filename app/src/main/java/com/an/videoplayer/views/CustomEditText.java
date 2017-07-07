package com.an.videoplayer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.an.videoplayer.R;


public class CustomEditText extends android.widget.EditText {

    public CustomEditText(Context context) {
        super(context);
        setTypeFace(1);
    }

    public CustomEditText(Context context, AttributeSet attrs) {

        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomEditText);
        final int fontValue = a.getInt(R.styleable.CustomEditText_font, 0);
        a.recycle();
        setTypeFace(fontValue);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomEditText);
        final int fontValue = a.getInt(R.styleable.CustomEditText_font, 0);
        a.recycle();
        setTypeFace(fontValue);
    }

    public void setTypeFace(int fontValue) {
        Typeface myTypeFace;

        if (fontValue == 1) {
            myTypeFace = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/gt_medium.otf");
            this.setTypeface(myTypeFace);
        }
    }
}