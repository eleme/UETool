package me.ele.uetool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.ele.uetool.function.EditAttrLayout;
import me.ele.uetool.function.GriddingLayout;
import me.ele.uetool.function.RelativePositionLayout;

import static me.ele.uetool.TransparentActivity.Type.TYPE_EDIT_ATTR;
import static me.ele.uetool.TransparentActivity.Type.TYPE_RELATIVE_POSITION;
import static me.ele.uetool.TransparentActivity.Type.TYPE_SHOW_GRIDDING;
import static me.ele.uetool.TransparentActivity.Type.TYPE_UNKNOWN;

public class TransparentActivity extends AppCompatActivity {

  public static final String EXTRA_TYPE = "extra_type";

  private Activity bindActivity = UETool.getInstance().getTargetActivity();
  private ViewGroup vContainer;
  private int type;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Util.setStatusBarColor(getWindow(), Color.TRANSPARENT);
    Util.enableFullscreen(getWindow());
    setContentView(R.layout.uet_activity_transparent);

    vContainer = findViewById(R.id.container);

    final String targetActivityInfo =
        getString(R.string.uet_name) + " / " + bindActivity.getClass().getName();

    final TextView board =
        (TextView) LayoutInflater.from(this).inflate(R.layout.uet_info_board, vContainer, false);
    board.setText(targetActivityInfo);
    vContainer.addView(board);

    type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_UNKNOWN);

    switch (type) {
      case TYPE_EDIT_ATTR:
        EditAttrLayout editAttrLayout = new EditAttrLayout(this);
        editAttrLayout.setOnDragListener(new EditAttrLayout.OnDragListener() {
          @Override public void showOffset(String offsetContent) {
            board.setText(offsetContent + "\n" + targetActivityInfo);
          }
        });
        vContainer.addView(editAttrLayout);
        break;
      case TYPE_RELATIVE_POSITION:
        vContainer.addView(new RelativePositionLayout(this));
        break;
      case TYPE_SHOW_GRIDDING:
        vContainer.addView(new GriddingLayout(this));
        break;
      default:
        Toast.makeText(this, getString(R.string.uet_coming_soon), Toast.LENGTH_SHORT).show();
        finish();
        break;
    }
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    if (type == TYPE_SHOW_GRIDDING) {
      bindActivity.dispatchTouchEvent(ev);
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    bindActivity = null;
    UETool.getInstance().release();
  }

  @Override protected void onStop() {
    super.onStop();
    finish();
  }

  @IntDef({
      TYPE_UNKNOWN,
      TYPE_EDIT_ATTR,
      TYPE_SHOW_GRIDDING,
      TYPE_RELATIVE_POSITION,
  })
  @Retention(RetentionPolicy.SOURCE) public @interface Type {
    int TYPE_UNKNOWN = -1;
    int TYPE_EDIT_ATTR = 1;
    int TYPE_SHOW_GRIDDING = 2;
    int TYPE_RELATIVE_POSITION = 3;
  }
}
