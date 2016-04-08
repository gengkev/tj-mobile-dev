package io.github.gengkev.exampleapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
    private int mValue;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.MyTextView, 0, 0);
        try {
            mValue = a.getInt(R.styleable.MyTextView_initialValue, 0);
        } finally {
            a.recycle();
        }

        setText(String.valueOf(mValue));
    }

    public void incrementAndUpdate() {
        setText(String.valueOf(++mValue));
    }
}
