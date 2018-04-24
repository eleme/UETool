package me.ele.uetool.function;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import me.ele.uetool.Element;
import me.ele.uetool.Util;

public class EditAttrLayout extends CollectViewsLayout {

  private final int SIDE_LINE_SPACE = Util.dip2px(getContext(), 3);
  private final int SIDE_TEXT_SPACE = Util.dip2px(getContext(), 6);

  private Paint areaPaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x30000000);
    }
  };

  private Paint sidesPaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x90000000);
      setTextSize(Util.sp2px(getContext(), 10));
      setStrokeWidth(Util.dip2px(getContext(), 1));
    }
  };

  private Element element;

  public EditAttrLayout(Context context) {
    super(context);
  }

  public EditAttrLayout(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public EditAttrLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (element != null) {
      Rect rect = element.getRect();
      canvas.drawLine(rect.left, rect.top - SIDE_LINE_SPACE, rect.right, rect.top - SIDE_LINE_SPACE,
          sidesPaint);
      int width = element.getView().getWidth();
      String widthText = Util.px2dip(getContext(), width) + "dp";
      canvas.drawText(widthText, rect.left + width / 2 - getTextWidth(widthText, sidesPaint) / 2,
          rect.top - SIDE_TEXT_SPACE, sidesPaint);
      canvas.drawLine(rect.right + SIDE_LINE_SPACE, rect.top, rect.right + SIDE_LINE_SPACE,
          rect.bottom, sidesPaint);
      int height = element.getView().getHeight();
      String heightText = Util.px2dip(getContext(), height) + "dp";
      canvas.drawText(heightText, rect.right + SIDE_TEXT_SPACE,
          rect.top + height / 2 + getTextHeight(heightText, sidesPaint) / 2, sidesPaint);
      canvas.drawRect(rect, areaPaint);
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_UP:

        final Element element = getTargetElement(event.getX(), event.getY());
        if (element != null) {
          this.element = element;
          invalidate();
          ViewAttrDialog dialog = new ViewAttrDialog(getContext());
          dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
              element.reset();
              invalidate();
            }
          });
          dialog.show(element);
        }

        break;
    }
    return true;
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    element = null;
  }
}
