package me.ele.uetool.function;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import me.ele.uetool.Element;
import me.ele.uetool.UETool;
import me.ele.uetool.Util;

public class CollectViewsLayout extends View {

  private final int ENDPOINT_LINE_HALF_WIDTH = Util.dip2px(getContext(), 2.5f);
  private final int TEXT_BG_FILLING_SPACE = Util.dip2px(getContext(), 2);
  private final int TEXT_LINE_DISTANCE = Util.dip2px(getContext(), 5);
  protected final int SCREEN_WIDTH = Util.getScreenWidth(getContext());
  protected final int SCREEN_HEIGHT = Util.getScreenHeight(getContext());

  protected List<Element> elements = new ArrayList<>();
  protected Element childElement, parentElement;
  protected Paint textPaint = new Paint() {
    {
      setAntiAlias(true);
      setTextSize(Util.sp2px(getContext(), 10));
      setColor(Color.RED);
      setStrokeWidth(Util.dip2px(getContext(), 1));
    }
  };

  private Paint textBgPaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(Color.WHITE);
      setStrokeJoin(Join.ROUND);
    }
  };

  public CollectViewsLayout(Context context) {
    super(context);
  }

  public CollectViewsLayout(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public CollectViewsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    traverse(UETool.getInstance().getTargetActivity().findViewById(android.R.id.content));
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    elements.clear();
    childElement = null;
    parentElement = null;
  }

  private void traverse(View view) {
    if (UETool.getInstance().getFilterClasses().contains(view.getClass().getName())) return;
    if (view.getVisibility() != View.VISIBLE) return;
    if (view.getAlpha() == 0) return;
    if (!view.isEnabled()) return;
    if ("DESABLE_UETOOL".equals(view.getTag())) return;
    elements.add(new Element(view));
    if (view instanceof ViewGroup) {
      ViewGroup parent = (ViewGroup) view;
      for (int i = 0; i < parent.getChildCount(); i++) {
        traverse(parent.getChildAt(i));
      }
    }
  }

  protected Element getTargetElement(float x, float y) {
    for (int i = elements.size() - 1; i >= 0; i--) {
      final Element element = elements.get(i);
      if (element.getRect().contains((int) x, (int) y)) {
        if (element != childElement) {
          childElement = element;
          parentElement = element;
        } else {
          parentElement = new Element((View) (parentElement.getView().getParent()));
        }
        return parentElement;
      }
    }
    return null;
  }

  protected void drawText(Canvas canvas, String text, float x, float y) {
    canvas.drawRect(x - TEXT_BG_FILLING_SPACE, y - getTextHeight(text),
        x + getTextWidth(text) + TEXT_BG_FILLING_SPACE, y + TEXT_BG_FILLING_SPACE,
        textBgPaint);
    canvas.drawText(text, x, y, textPaint);
  }

  private void drawLineWithEndPoint(Canvas canvas, int startX, int startY, int endX,
      int endY) {
    canvas.drawLine(startX, startY, endX, endY, textPaint);
    if (startX == endX) {
      canvas.drawLine(startX - ENDPOINT_LINE_HALF_WIDTH, startY, endX + ENDPOINT_LINE_HALF_WIDTH,
          startY, textPaint);
      canvas.drawLine(startX - ENDPOINT_LINE_HALF_WIDTH, endY, endX + ENDPOINT_LINE_HALF_WIDTH,
          endY, textPaint);
    } else if (startY == endY) {
      canvas.drawLine(startX, startY - ENDPOINT_LINE_HALF_WIDTH, startX,
          endY + ENDPOINT_LINE_HALF_WIDTH, textPaint);
      canvas.drawLine(endX, startY - ENDPOINT_LINE_HALF_WIDTH, endX,
          endY + ENDPOINT_LINE_HALF_WIDTH, textPaint);
    }
  }

  protected void drawLineWithText(Canvas canvas, int startX, int startY, int endX, int endY) {

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
      drawLineWithEndPoint(canvas, startX, startY + getLineEndPointSpace(), endX,
          endY - getLineEndPointSpace());
      String text = Util.px2dip(getContext(), endY - startY) + "dp";
      drawText(canvas, text, startX + TEXT_LINE_DISTANCE,
          startY + (endY - startY) / 2 + getTextHeight(text) / 2);
    } else if (startY == endY) {
      drawLineWithEndPoint(canvas, startX + getLineEndPointSpace(), startY,
          endX - getLineEndPointSpace(), endY);
      String text = Util.px2dip(getContext(), endX - startX) + "dp";
      drawText(canvas, text, startX + (endX - startX) / 2 - getTextWidth(text) / 2,
          startY - TEXT_LINE_DISTANCE);
    }
  }

  protected float getTextHeight(String text) {
    Rect rect = new Rect();
    textPaint.getTextBounds(text, 0, text.length(), rect);
    return rect.height();
  }

  protected float getTextWidth(String text) {
    return textPaint.measureText(text);
  }

  protected int getLineEndPointSpace() {
    return 0;
  }
}
