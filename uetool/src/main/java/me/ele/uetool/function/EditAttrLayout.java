package me.ele.uetool.function;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.ele.uetool.CollectViewsLayout;
import me.ele.uetool.R;
import me.ele.uetool.ViewAttrDialog;
import me.ele.uetool.base.Element;

import static me.ele.uetool.base.DimenUtil.dip2px;
import static me.ele.uetool.function.EditAttrLayout.Mode.MOVE;
import static me.ele.uetool.function.EditAttrLayout.Mode.SHOW;

public class EditAttrLayout extends CollectViewsLayout {

  private final int MOVE_UNIT = dip2px(1);
  private final int LINE_BORDER_DISTANCE = dip2px(5);

  private Paint areaPaint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x30000000);
    }
  };

  private @Mode int mode = SHOW;
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
      canvas.drawRect(rect, areaPaint);
      if (mode == SHOW) {
        drawLineWithText(canvas, rect.left, rect.top - LINE_BORDER_DISTANCE, rect.right,
            rect.top - LINE_BORDER_DISTANCE);
        drawLineWithText(canvas, rect.right + LINE_BORDER_DISTANCE, rect.top,
            rect.right + LINE_BORDER_DISTANCE, rect.bottom);
      } else if (mode == MOVE) {
        Element parentElement = element.getParentElement();
        if (parentElement != null) {
          Rect parentRect = parentElement.getRect();
          int x = rect.left + rect.width() / 2;
          int y = rect.top + rect.height() / 2;
          drawLineWithText(canvas, rect.left, y, parentRect.left, y);
          drawLineWithText(canvas, x, rect.top, x, parentRect.top);
          drawLineWithText(canvas, rect.right, y, parentRect.right, y);
          drawLineWithText(canvas, x, rect.bottom, x, parentRect.bottom);
        }
      }
    }
  }

  private float lastX, lastY;

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        lastX = event.getX();
        lastY = event.getY();
        break;
      case MotionEvent.ACTION_UP:

        if (mode == SHOW) {
          final Element element = getTargetElement(event.getX(), event.getY());
          if (element != null) {
            this.element = element;
            invalidate();
            if (dialog == null) {
              dialog = new ViewAttrDialog(getContext());
              dialog.setAttrDialogCallback(new ViewAttrDialog.AttrDialogCallback() {
                @Override public void enableMove() {
                  mode = MOVE;
                  dialog.dismiss();
                }
              });
              dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override public void onDismiss(DialogInterface dialog) {
                  element.reset();
                  invalidate();
                }
              });
            }
            dialog.show(element);
          }
        }

        break;
      case MotionEvent.ACTION_MOVE:

        if (mode == MOVE && element != null) {
          boolean changed = false;
          View view = element.getView();
          float diffX = event.getX() - lastX;
          if (Math.abs(diffX) >= MOVE_UNIT) {
            view.setTranslationX(view.getTranslationX() + diffX);
            lastX = event.getX();
            changed = true;
          }
          float diffY = event.getY() - lastY;
          if (Math.abs(diffY) >= MOVE_UNIT) {
            view.setTranslationY(view.getTranslationY() + diffY);
            lastY = event.getY();
            changed = true;
          }
          if (changed) {
            element.reset();
            invalidate();
          }
        }

        break;
    }
    return true;
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Toast.makeText(getContext(), getResources().getString(R.string.uet_enable_edit_attr),
        Toast.LENGTH_SHORT).show();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    element = null;
    if (dialog != null) {
      dialog.dismiss();
    }
  }

  @IntDef({
      SHOW,
      MOVE,
  })
  @Retention(RetentionPolicy.SOURCE) public @interface Mode {
    int SHOW = 1;
    int MOVE = 2;
  }
}
