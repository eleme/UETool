package me.ele.uetool.function;

import android.content.Context;
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

public class CollectViewsLayout extends View {

  protected List<Element> elements = new ArrayList<>();
  protected Element childElement, parentElement;

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

  protected float getTextHeight(String text, Paint paint) {
    Rect rect = new Rect();
    paint.getTextBounds(text, 0, text.length(), rect);
    return rect.height();
  }

  protected float getTextWidth(String text, Paint paint) {
    return paint.measureText(text);
  }
}
