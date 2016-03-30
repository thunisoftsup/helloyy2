package com.thunisoft.sswy.mobile.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.thunisoft.dzfy.mobile.R;

@SuppressLint("DrawAllocation")
public class CheckableListItemLayout extends LinearLayout implements Checkable {

    View selectorView;
    ViewGroup viewGroup;

    private boolean mChecked;

    public CheckableListItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        selectorView = findViewById(R.id.selector);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setIcon(mChecked);
    }

    private void setIcon(boolean show) {
        if (show) {
            selectorView.setVisibility(View.VISIBLE);
        } else {
            selectorView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }

    @Override
    public boolean performClick() {
        setChecked(true);
        if (viewGroup != null) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = viewGroup.getChildAt(i);
                if (child != null && child instanceof CheckableListItemLayout && child != CheckableListItemLayout.this) {
                    ((CheckableListItemLayout) child).setChecked(false);
                }
            }
        }
        return super.performClick();
    }

    public void setParentViewGroup(ViewGroup parent) {
        viewGroup = parent;
    }

}
