package me.ele.uetool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import me.ele.uetool.base.DimenUtil;

public class RegionView extends View {

    private RectF rectF;
    private Paint paint = new Paint() {
        {
            setAntiAlias(true);
            setColor(Color.YELLOW);
            setStrokeWidth(DimenUtil.dip2px(2));
            setStyle(Style.STROKE);
        }
    };

    public RegionView(Context context) {
        this(context, null);
    }

    public RegionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RegionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawRegion(RectF rectF) {
        this.rectF = rectF;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF != null) {
            canvas.drawRect(rectF, paint);
        }
    }
}
