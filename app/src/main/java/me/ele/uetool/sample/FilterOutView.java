package me.ele.uetool.sample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class FilterOutView extends TextView {
  public FilterOutView(Context context) {
    super(context);
  }

  public FilterOutView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public FilterOutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
