package me.ele.uetool;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import me.ele.uetool.base.Element;

import static me.ele.uetool.base.DimenUtil.dip2px;
import static me.ele.uetool.base.DimenUtil.px2dip;

public class EditAttrLayout extends CollectViewsLayout {

    private final int moveUnit = dip2px(1);
    private final int lineBorderDistance = dip2px(5);

    private Paint areaPaint = new Paint() {
        {
            setAntiAlias(true);
            setColor(0x30000000);
        }
    };

    private Element targetElement;
    private AttrsDialog dialog;
    private IMode mode = new ShowMode();
    private float lastX, lastY;
    private OnDragListener onDragListener;

    public EditAttrLayout(Context context) {
        super(context);
    }

    public EditAttrLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditAttrLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (targetElement != null) {
            canvas.drawRect(targetElement.getRect(), areaPaint);
            mode.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mode.triggerActionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mode.triggerActionMove(event);
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        targetElement = null;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    class MoveMode implements IMode {

        @Override
        public void onDraw(Canvas canvas) {
            Rect rect = targetElement.getRect();
            Rect originRect = targetElement.getOriginRect();
            canvas.drawRect(originRect, dashLinePaint);
            Element parentElement = targetElement.getParentElement();
            if (parentElement != null) {
                Rect parentRect = parentElement.getRect();
                int x = rect.left + rect.width() / 2;
                int y = rect.top + rect.height() / 2;
                drawLineWithText(canvas, rect.left, y, parentRect.left, y, dip2px(2));
                drawLineWithText(canvas, x, rect.top, x, parentRect.top, dip2px(2));
                drawLineWithText(canvas, rect.right, y, parentRect.right, y, dip2px(2));
                drawLineWithText(canvas, x, rect.bottom, x, parentRect.bottom, dip2px(2));
            }
            if (onDragListener != null) {
                onDragListener.showOffset("Offset:\n" + "x -> " + px2dip(rect.left - originRect.left, true) + " y -> " + px2dip(rect.top - originRect.top, true));
            }
        }

        @Override
        public void triggerActionMove(MotionEvent event) {
            if (targetElement != null) {
                boolean changed = false;
                View view = targetElement.getView();
                float diffX = event.getX() - lastX;
                if (Math.abs(diffX) >= moveUnit) {
                    view.setTranslationX(view.getTranslationX() + diffX);
                    lastX = event.getX();
                    changed = true;
                }
                float diffY = event.getY() - lastY;
                if (Math.abs(diffY) >= moveUnit) {
                    view.setTranslationY(view.getTranslationY() + diffY);
                    lastY = event.getY();
                    changed = true;
                }
                if (changed) {
                    targetElement.reset();
                    invalidate();
                }
            }
        }

        @Override
        public void triggerActionUp(MotionEvent event) {

        }
    }

    class ShowMode implements IMode {

        @Override
        public void onDraw(Canvas canvas) {
            Rect rect = targetElement.getRect();
            drawLineWithText(canvas, rect.left, rect.top - lineBorderDistance, rect.right, rect.top - lineBorderDistance);
            drawLineWithText(canvas, rect.right + lineBorderDistance, rect.top, rect.right + lineBorderDistance, rect.bottom);
        }

        @Override
        public void triggerActionMove(MotionEvent event) {

        }

        @Override
        public void triggerActionUp(final MotionEvent event) {
            final Element element = getTargetElement(event.getX(), event.getY());
            if (element != null) {
                targetElement = element;
                invalidate();
                if (dialog == null) {
                    dialog = new AttrsDialog(getContext());
                    dialog.setAttrDialogCallback(new AttrsDialog.AttrDialogCallback() {
                        @Override
                        public void enableMove() {
                            mode = new MoveMode();
                            dialog.dismiss();
                        }

                        @Override
                        public void showValidViews(int position, boolean isChecked) {
                            int positionStart = position + 1;
                            if (isChecked) {
                                dialog.notifyValidViewItemInserted(positionStart, getTargetElements(lastX, lastY), targetElement);
                            } else {
                                dialog.notifyItemRangeRemoved(positionStart);
                            }
                        }

                        @Override
                        public void selectView(Element element) {
                            targetElement = element;
                            dialog.dismiss();
                            dialog.show(targetElement);
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (targetElement != null) {
                                targetElement.reset();
                                invalidate();
                            }
                        }
                    });
                }
                dialog.show(targetElement);
            }
        }
    }

    public interface IMode {
        void onDraw(Canvas canvas);

        void triggerActionMove(MotionEvent event);

        void triggerActionUp(MotionEvent event);
    }

    public interface OnDragListener {
        void showOffset(String offsetContent);
    }
}
