package me.ele.uetool.function;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;
import me.ele.uetool.Element;
import me.ele.uetool.Util;

public class RelativePositionLayout extends CollectViewsLayout {

  private final int LINE_SPACE = Util.dip2px(getContext(), 2);
  private final int ENDPOINT_LINE_HALF_WIDTH = Util.dip2px(getContext(), 2.5f);
  private final int SCREEN_WIDTH = Util.getScreenWidth(getContext());
  private final int SCREEN_HEIGHT = Util.getScreenHeight(getContext());

  private Paint areaPaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(Color.RED);
      setStyle(Style.STROKE);
      setStrokeWidth(Util.dip2px(getContext(), 1));
    }
  };

  private Paint dashLinePaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x90FF0000);
      setStyle(Style.STROKE);
      setPathEffect(new DashPathEffect(
          new float[] { Util.dip2px(getContext(), 4), Util.dip2px(getContext(), 8) }, 0));
    }
  };

  private Paint relativeLinePaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x90000000);
      setStrokeWidth(Util.dip2px(getContext(), 1));
      setStyle(Style.STROKE);
    }
  };

  private Paint textPaint = new Paint() {
    {
      setAntiAlias(true);
      setTextSize(Util.sp2px(getContext(), 10));
      setColor(Color.RED);
      setStrokeWidth(Util.dip2px(getContext(), 1));
    }
  };

  private Element[] relativeElements = new Element[2];
  private int searchCount = 0;

  public RelativePositionLayout(Context context) {
    this(context, null);
  }

  public RelativePositionLayout(Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RelativePositionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setLayerType(LAYER_TYPE_SOFTWARE, null);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_UP:

        final Element element = getTargetElement(event.getX(), event.getY());
        if (element != null) {
          if (element == relativeElements[(searchCount + 1) % 2]) {
            relativeElements[(searchCount + 1) % 2] = null;
          } else {
            int index = searchCount % 2;
            relativeElements[index] = element;
          }
          searchCount++;
          invalidate();
        } else {
          Toast.makeText(getContext(),
              "该坐标(" + event.getX() + " , " + event.getY() + ")未找到控件，请重新选择控件", Toast.LENGTH_SHORT)
              .show();
        }

        break;
    }
    return true;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    boolean doubleNotNull = true;
    for (Element element : relativeElements) {
      if (element != null) {
        Rect rect = element.getRect();
        canvas.drawLine(0, rect.top, SCREEN_WIDTH, rect.top, dashLinePaint);
        canvas.drawLine(0, rect.bottom, SCREEN_WIDTH, rect.bottom, dashLinePaint);
        canvas.drawLine(rect.left, 0, rect.left, SCREEN_HEIGHT, dashLinePaint);
        canvas.drawLine(rect.right, 0, rect.right, SCREEN_HEIGHT, dashLinePaint);
        canvas.drawRect(rect, areaPaint);
      } else {
        doubleNotNull = false;
      }
    }

    if (doubleNotNull) {
      Rect firstRect = relativeElements[searchCount % 2].getRect();
      Rect secondRect = relativeElements[(searchCount - 1) % 2].getRect();

      if (secondRect.top > firstRect.bottom) {
        int x = secondRect.left + secondRect.width() / 2;
        canvas.drawLine(x, firstRect.bottom + LINE_SPACE, x, secondRect.top - LINE_SPACE,
            relativeLinePaint);
        canvas.drawLine(x - ENDPOINT_LINE_HALF_WIDTH, secondRect.top - LINE_SPACE,
            x + ENDPOINT_LINE_HALF_WIDTH, secondRect.top - LINE_SPACE,
            relativeLinePaint);
        canvas.drawLine(x - ENDPOINT_LINE_HALF_WIDTH, firstRect.bottom + LINE_SPACE,
            x + ENDPOINT_LINE_HALF_WIDTH, firstRect.bottom + LINE_SPACE,
            relativeLinePaint);
        String text = Util.px2dip(getContext(), secondRect.top - firstRect.bottom) + "dp";
        canvas.drawText(text, x + LINE_SPACE,
            firstRect.bottom + (secondRect.top - firstRect.bottom) / 2, textPaint);
      }

      if (firstRect.top > secondRect.bottom) {
        int x = secondRect.left + secondRect.width() / 2;
        canvas.drawLine(x, secondRect.bottom + LINE_SPACE, x, firstRect.top - LINE_SPACE,
            relativeLinePaint);
        canvas.drawLine(x - ENDPOINT_LINE_HALF_WIDTH, secondRect.bottom + LINE_SPACE,
            x + ENDPOINT_LINE_HALF_WIDTH, secondRect.bottom + LINE_SPACE, relativeLinePaint);
        canvas.drawLine(x - ENDPOINT_LINE_HALF_WIDTH, firstRect.top - LINE_SPACE,
            x + ENDPOINT_LINE_HALF_WIDTH, firstRect.top - LINE_SPACE, relativeLinePaint);
        String text = Util.px2dip(getContext(), firstRect.top - secondRect.bottom) + "dp";
        canvas.drawText(text, x + LINE_SPACE,
            firstRect.top + (secondRect.bottom - firstRect.top) / 2, textPaint);
      }

      if (secondRect.left > firstRect.right) {
        int y = secondRect.top + secondRect.height() / 2;
        canvas.drawLine(secondRect.left - LINE_SPACE, y, firstRect.right + LINE_SPACE, y,
            relativeLinePaint);
        canvas.drawLine(secondRect.left - LINE_SPACE, y - ENDPOINT_LINE_HALF_WIDTH,
            secondRect.left - LINE_SPACE, y + ENDPOINT_LINE_HALF_WIDTH, relativeLinePaint);
        canvas.drawLine(firstRect.right + LINE_SPACE, y - ENDPOINT_LINE_HALF_WIDTH,
            firstRect.right + LINE_SPACE, y + ENDPOINT_LINE_HALF_WIDTH, relativeLinePaint);
        String text = Util.px2dip(getContext(), secondRect.left - firstRect.right) + "dp";
        canvas.drawText(text, firstRect.right + (secondRect.left - firstRect.right) / 2
            - getTextWidth(text, textPaint) / 2, y - 2 * LINE_SPACE, textPaint);
      }

      if (firstRect.left > secondRect.right) {
        int y = secondRect.top + secondRect.height() / 2;
        canvas.drawLine(secondRect.right + LINE_SPACE, y, firstRect.left - LINE_SPACE, y,
            relativeLinePaint);
        canvas.drawLine(secondRect.right + LINE_SPACE, y - ENDPOINT_LINE_HALF_WIDTH,
            secondRect.right + LINE_SPACE, y + ENDPOINT_LINE_HALF_WIDTH,
            relativeLinePaint);
        canvas.drawLine(firstRect.left - LINE_SPACE, y - ENDPOINT_LINE_HALF_WIDTH,
            firstRect.left - LINE_SPACE, y + ENDPOINT_LINE_HALF_WIDTH,
            relativeLinePaint);
        String text = Util.px2dip(getContext(), firstRect.left - secondRect.right) + "dp";
        canvas.drawText(text, secondRect.right + (firstRect.left - secondRect.right) / 2
            - getTextWidth(text, textPaint) / 2, y - 2 * LINE_SPACE, textPaint);
      }

      drawNestedAreaLine(canvas, firstRect, secondRect);
      drawNestedAreaLine(canvas, secondRect, firstRect);
    }
  }

  private void drawNestedAreaLine(Canvas canvas, Rect firstRect, Rect secondRect) {
    if (secondRect.left >= firstRect.left
        && secondRect.right <= firstRect.right
        && secondRect.top >= firstRect.top
        && secondRect.bottom <= firstRect.bottom) {

      canvas.drawLine(secondRect.left - LINE_SPACE, secondRect.top + secondRect.height() / 2,
          firstRect.left + LINE_SPACE, secondRect.top + secondRect.height() / 2,
          relativeLinePaint);
      String textLeft = Util.px2dip(getContext(), secondRect.left - firstRect.left) + "dp";
      canvas.drawText(textLeft, firstRect.left + (secondRect.left - firstRect.left) / 2
              - getTextWidth(textLeft, textPaint) / 2,
          secondRect.top + secondRect.height() / 2 - 2 * LINE_SPACE, textPaint);

      canvas.drawLine(secondRect.right + LINE_SPACE, secondRect.top + secondRect.height() / 2,
          firstRect.right - LINE_SPACE, secondRect.top + secondRect.height() / 2,
          relativeLinePaint);
      String textRight = Util.px2dip(getContext(), firstRect.right - secondRect.right) + "dp";
      canvas.drawText(textRight, secondRect.right + (firstRect.right - secondRect.right) / 2
              - getTextWidth(textRight, textPaint) / 2,
          secondRect.top + secondRect.height() / 2 - 2 * LINE_SPACE, textPaint);

      canvas.drawLine(secondRect.left + secondRect.width() / 2, secondRect.top - LINE_SPACE,
          secondRect.left + secondRect.width() / 2, firstRect.top + LINE_SPACE,
          relativeLinePaint);
      String textTop = Util.px2dip(getContext(), secondRect.top - firstRect.top) + "dp";
      canvas.drawText(textTop, secondRect.left + secondRect.width() / 2 + LINE_SPACE,
          firstRect.top + (secondRect.top - firstRect.top) / 2
              + getTextHeight(textTop, textPaint) / 2, textPaint);

      canvas.drawLine(secondRect.left + secondRect.width() / 2, secondRect.bottom + LINE_SPACE,
          secondRect.left + secondRect.width() / 2, firstRect.bottom - LINE_SPACE,
          relativeLinePaint);
      String textBottom = Util.px2dip(getContext(), firstRect.bottom - secondRect.bottom) + "dp";
      canvas.drawText(textBottom, secondRect.left + secondRect.width() / 2 + LINE_SPACE,
          secondRect.bottom + (firstRect.bottom - secondRect.bottom) / 2
              + getTextHeight(textBottom, textPaint) / 2, textPaint);
    }
  }
}
