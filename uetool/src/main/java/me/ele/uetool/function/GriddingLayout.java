package me.ele.uetool.function;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static me.ele.uetool.base.DimenUtil.dip2px;
import static me.ele.uetool.base.DimenUtil.getScreenHeight;
import static me.ele.uetool.base.DimenUtil.getScreenWidth;

public class GriddingLayout extends View {

  private final int LINE_SPACE = dip2px(5);
  private final int SCREEN_WIDTH = getScreenWidth();
  private final int SCREEN_HEIGHT = getScreenHeight();

  private Paint paint = new Paint() {
    {
      setAntiAlias(true);
      setColor(0x30000000);
      setStrokeWidth(1);
    }
  };

  public GriddingLayout(Context context) {
    super(context);
  }

  public GriddingLayout(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public GriddingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int startX = 0;
    while (startX < SCREEN_WIDTH) {
      canvas.drawLine(startX, 0, startX, SCREEN_HEIGHT, paint);
      startX = startX + LINE_SPACE;
    }

    int startY = 0;
    while (startY < SCREEN_HEIGHT) {
      canvas.drawLine(0, startY, SCREEN_WIDTH, startY, paint);
      startY = startY + LINE_SPACE;
    }
  }
}
