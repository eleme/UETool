package me.ele.uetool.sample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomView extends AppCompatTextView {

    private String moreAttribution;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getMoreAttribution() {
        return moreAttribution;
    }

    public void setMoreAttribution(String moreAttribution) {
        this.moreAttribution = moreAttribution;
    }
}
