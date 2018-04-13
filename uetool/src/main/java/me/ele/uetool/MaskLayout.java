package me.ele.uetool;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MaskLayout extends View {

  private Paint paint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x30000000);
    }
  };

  private Element element;

  public MaskLayout(Context context) {
    super(context);
  }

  public MaskLayout(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MaskLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (element != null) {
      canvas.drawRect(element.getRect(), paint);
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_UP:

        List<Element> validList = new ArrayList<>();

        int x = (int) event.getX();
        int y = (int) event.getY();

        for (Element element : UETool.getInstance().getElements()) {
          if (element.getRect().contains(x, y)) {
            validList.add(element);
          }
        }

        if (validList.size() > 0) {
          element = validList.get(validList.size() - 1);
          invalidate();
          InfoDialog dialog = new InfoDialog(getContext());
          dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
              invalidate();
            }
          });
          dialog.show(element);
        }

        break;
    }
    return true;
  }
}
