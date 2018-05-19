package me.ele.uetool.base;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;

public class Element {

    private View view;
    private Rect originRect = new Rect();
    private Rect rect = new Rect();
    private int[] location = new int[2];
    private Element parentElement;

    public Element(View view) {
        this.view = view;
        reset();
        originRect.set(rect.left, rect.top, rect.right, rect.bottom);
    }

    public View getView() {
        return view;
    }

    public Rect getRect() {
        return rect;
    }

    public Rect getOriginRect() {
        return originRect;
    }

    public void reset() {
        view.getLocationOnScreen(location);
        int width = view.getWidth();
        int height = view.getHeight();

        int left = location[0];
        int right = left + width;
        int top = location[1];
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            top -= DimenUtil.getStatusBarHeight();
        }
        int bottom = top + height;

        rect.set(left, top, right, bottom);
    }

    public Element getParentElement() {
        if (parentElement == null) {
            Object parentView = view.getParent();
            if (parentView instanceof View) {
                parentElement = new Element((View) parentView);
            }
        }
        return parentElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        return view != null ? view.equals(element.view) : element.view == null;

    }

    @Override
    public int hashCode() {
        return view != null ? view.hashCode() : 0;
    }
}
