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
        drawRelativeInfo(canvas, x, firstRect.bottom, x, secondRect.top);
      }

      if (firstRect.top > secondRect.bottom) {
        int x = secondRect.left + secondRect.width() / 2;
        drawRelativeInfo(canvas, x, secondRect.bottom, x, firstRect.top);
      }

      if (secondRect.left > firstRect.right) {
        int y = secondRect.top + secondRect.height() / 2;
        drawRelativeInfo(canvas, secondRect.left, y, firstRect.right, y);
      }

      if (firstRect.left > secondRect.right) {
        int y = secondRect.top + secondRect.height() / 2;
        drawRelativeInfo(canvas, secondRect.right, y, firstRect.left, y);
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

      drawRelativeInfo(canvas, secondRect.left,
          secondRect.top + secondRect.height() / 2,
          firstRect.left, secondRect.top + secondRect.height() / 2);

      drawRelativeInfo(canvas, secondRect.right,
          secondRect.top + secondRect.height() / 2,
          firstRect.right, secondRect.top + secondRect.height() / 2);

      drawRelativeInfo(canvas, secondRect.left + secondRect.width() / 2,
          secondRect.top,
          secondRect.left + secondRect.width() / 2, firstRect.top);

      drawRelativeInfo(canvas, secondRect.left + secondRect.width() / 2,
          secondRect.bottom,
          secondRect.left + secondRect.width() / 2, firstRect.bottom);
    }
  }

  private void drawRelativeLine(Canvas canvas, int startX, int startY, int endX,
      int endY) {
    canvas.drawLine(startX, startY, endX, endY, relativeLinePaint);
    if (startX == endX) {
      canvas.drawLine(startX - ENDPOINT_LINE_HALF_WIDTH, startY, endX + ENDPOINT_LINE_HALF_WIDTH,
          startY, relativeLinePaint);
      canvas.drawLine(startX - ENDPOINT_LINE_HALF_WIDTH, endY, endX + ENDPOINT_LINE_HALF_WIDTH,
          endY, relativeLinePaint);
    } else if (startY == endY) {
      canvas.drawLine(startX, startY - ENDPOINT_LINE_HALF_WIDTH, startX,
          endY + ENDPOINT_LINE_HALF_WIDTH, relativeLinePaint);
      canvas.drawLine(endX, startY - ENDPOINT_LINE_HALF_WIDTH, endX,
          endY + ENDPOINT_LINE_HALF_WIDTH, relativeLinePaint);
    }
  }

  private void drawRelativeInfo(Canvas canvas, int startX, int startY, int endX, int endY) {

    if (startX > endX) {
      int tempX = startX;
      startX = endX;
      endX = tempX;
    }
    if (startY > endY) {
      int tempY = startY;
      startY = endY;
      endY = tempY;
    }

    if (startX == endX) {
      drawRelativeLine(canvas, startX, startY + LINE_SPACE, endX, endY - LINE_SPACE);
      String text = Util.px2dip(getContext(), endY - startY) + "dp";
      canvas.drawText(text, startX + LINE_SPACE,
          startY + (endY - startY) / 2 + getTextHeight(text, textPaint) / 2, textPaint);
    } else if (startY == endY) {
      drawRelativeLine(canvas, startX + LINE_SPACE, startY, endX - LINE_SPACE, endY);
      String text = Util.px2dip(getContext(), endX - startX) + "dp";
      canvas.drawText(text, startX + (endX - startX) / 2 - getTextWidth(text, textPaint) / 2,
          startY - 2 * LINE_SPACE, textPaint);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    relativeElements = null;
  }
}
