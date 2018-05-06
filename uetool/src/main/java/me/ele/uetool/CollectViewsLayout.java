package me.ele.uetool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.ele.uetool.base.Element;

import static me.ele.uetool.base.DimenUtil.dip2px;
import static me.ele.uetool.base.DimenUtil.getScreenHeight;
import static me.ele.uetool.base.DimenUtil.getScreenWidth;
import static me.ele.uetool.base.DimenUtil.px2dip;
import static me.ele.uetool.base.DimenUtil.sp2px;

public class CollectViewsLayout extends View {

  private final int halfEndPointWidth = dip2px(2.5f);
  private final int textBgFillingSpace = dip2px(2);
  private final int textLineDistance = dip2px(5);
  protected final int screenWidth = getScreenWidth();
  protected final int screenHeight = getScreenHeight();

  protected List<Element> elements = new ArrayList<>();
  protected Element childElement, parentElement;
  protected Paint textPaint = new Paint() {
    {
      setAntiAlias(true);
      setTextSize(sp2px(10));
      setColor(Color.RED);
      setStrokeWidth(dip2px(1));
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
    try {
      Activity targetActivity = UETool.getInstance().getTargetActivity();
      WindowManager windowManager = targetActivity.getWindowManager();
      Field mGlobalField =
          Class.forName("android.view.WindowManagerImpl").getDeclaredField("mGlobal");
      mGlobalField.setAccessible(true);
      Field mRootsField =
          Class.forName("android.view.WindowManagerGlobal").getDeclaredField("mRoots");
      mRootsField.setAccessible(true);
      List viewRootImpls;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        viewRootImpls = (List) mRootsField.get(mGlobalField.get(windowManager));
      } else {
        viewRootImpls = Arrays.asList((Object[]) mRootsField.get(mGlobalField.get(windowManager)));
      }
      for (int i = viewRootImpls.size() - 1; i >= 0; i--) {
        Class clazz = Class.forName("android.view.ViewRootImpl");
        Object object = viewRootImpls.get(i);
        Field mWindowAttributesField = clazz.getDeclaredField("mWindowAttributes");
        mWindowAttributesField.setAccessible(true);
        Field mViewField = clazz.getDeclaredField("mView");
        mViewField.setAccessible(true);
        View decorView = (View) mViewField.get(object);
        WindowManager.LayoutParams layoutParams =
            (WindowManager.LayoutParams) mWindowAttributesField.get(object);
        if (layoutParams.getTitle().toString().contains(targetActivity.getClass().getName())
            || decorView.getContext() == targetActivity) {
          traverse(decorView);
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    elements.clear();
    childElement = null;
    parentElement = null;
  }

  private void traverse(View view) {
    if (UETool.getInstance().getFilterClasses().contains(view.getClass().getName())) return;
    if (view.getAlpha() == 0 || view.getVisibility() != View.VISIBLE) return;
    if (getResources().getString(R.string.uet_disable).equals(view.getTag())) return;
    elements.add(new Element(view));
    if (view instanceof ViewGroup) {
      ViewGroup parent = (ViewGroup) view;
      for (int i = 0; i < parent.getChildCount(); i++) {
        traverse(parent.getChildAt(i));
      }
    }
  }

  protected Element getTargetElement(float x, float y) {
    Element target = null;
    for (int i = elements.size() - 1; i >= 0; i--) {
      final Element element = elements.get(i);
      if (element.getRect().contains((int) x, (int) y)) {
        if (element != childElement) {
          childElement = element;
          parentElement = element;
        } else {
          parentElement = parentElement.getParentElement();
        }
        target = parentElement;
        break;
      }
    }
    if (target == null) {
      Toast.makeText(getContext(),
          getResources().getString(R.string.uet_target_element_not_found, x, y), Toast.LENGTH_SHORT)
          .show();
    }
    return target;
  }

  protected void drawText(Canvas canvas, String text, float x, float y) {
    canvas.drawRect(x - textBgFillingSpace, y - getTextHeight(text),
        x + getTextWidth(text) + textBgFillingSpace, y + textBgFillingSpace,
        textBgPaint);
    canvas.drawText(text, x, y, textPaint);
  }

  private void drawLineWithEndPoint(Canvas canvas, int startX, int startY, int endX, int endY) {
    canvas.drawLine(startX, startY, endX, endY, textPaint);
    if (startX == endX) {
      canvas.drawLine(startX - halfEndPointWidth, startY, endX + halfEndPointWidth,
          startY, textPaint);
      canvas.drawLine(startX - halfEndPointWidth, endY, endX + halfEndPointWidth,
          endY, textPaint);
    } else if (startY == endY) {
      canvas.drawLine(startX, startY - halfEndPointWidth, startX,
          endY + halfEndPointWidth, textPaint);
      canvas.drawLine(endX, startY - halfEndPointWidth, endX,
          endY + halfEndPointWidth, textPaint);
    }
  }

  protected void drawLineWithText(Canvas canvas, int startX, int startY, int endX, int endY) {
    drawLineWithText(canvas, startX, startY, endX, endY, 0);
  }

  protected void drawLineWithText(Canvas canvas, int startX, int startY, int endX, int endY,
      int endPointSpace) {

    if (startX == endX && startY == endY) {
      return;
    }

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
      drawLineWithEndPoint(canvas, startX, startY + endPointSpace, endX, endY - endPointSpace);
      String text = px2dip(endY - startY) + "dp";
      drawText(canvas, text, startX + textLineDistance,
          startY + (endY - startY) / 2 + getTextHeight(text) / 2);
    } else if (startY == endY) {
      drawLineWithEndPoint(canvas, startX + endPointSpace, startY, endX - endPointSpace, endY);
      String text = px2dip(endX - startX) + "dp";
      drawText(canvas, text, startX + (endX - startX) / 2 - getTextWidth(text) / 2,
          startY - textLineDistance);
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
}
