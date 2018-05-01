package me.ele.uetool.function;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;
import me.ele.uetool.Element;
import me.ele.uetool.Util;

public class EditAttrLayout extends CollectViewsLayout {

  private final int LINE_BORDER_DISTANCE = Util.dip2px(5);

  private Paint areaPaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x30000000);
    }
  };

  private Element element;
  private ViewAttrDialog dialog;

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
      drawLineWithText(canvas, rect.left, rect.top - LINE_BORDER_DISTANCE, rect.right,
          rect.top - LINE_BORDER_DISTANCE);
      drawLineWithText(canvas, rect.right + LINE_BORDER_DISTANCE, rect.top,
          rect.right + LINE_BORDER_DISTANCE, rect.bottom);
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
          if (dialog == null) {
            dialog = new ViewAttrDialog(getContext());
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
              @Override public void onDismiss(DialogInterface dialog) {
                element.reset();
                invalidate();
              }
            });
          }
          dialog.show(element);
        }

        break;
    }
    return true;
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Toast.makeText(getContext(), "捕捉控件已打开，请点击您感兴趣的控件", Toast.LENGTH_SHORT).show();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    element = null;
    if (dialog != null) {
      dialog.dismiss();
    }
  }
}
