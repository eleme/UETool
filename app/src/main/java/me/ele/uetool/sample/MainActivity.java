package me.ele.uetool.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.reflect.Field;
import me.ele.uetool.suspend.UETMenu;
import me.wangyuwei.uetool.sample.R;

public class MainActivity extends AppCompatActivity {

  private WindowManager windowManager;
  private UETMenu uetMenu;
  private SimpleDraweeView draweeView;
  private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
  private int touchSlop;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    draweeView = findViewById(R.id.drawee_view);

    touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

    windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    addMenu();

    updateDraweeView();

    findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
      }
    });
  }

  @TargetApi(Build.VERSION_CODES.M) private void requestPermission(Context context) {
    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:" + context.getPackageName()));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private WindowManager.LayoutParams getLayoutParams() {
    params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
    params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    } else {
      params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    }
    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    params.format = PixelFormat.TRANSLUCENT;
    params.gravity = Gravity.TOP | Gravity.LEFT;
    params.x = 10;
    params.y = 10;
    return params;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    removeMenu();
  }

  private void addMenu() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!Settings.canDrawOverlays(this)) {
        requestPermission(this);
        return;
      }
    }
    uetMenu = new UETMenu(this, new UETMenu.CurrentTopActivityProvider() {
      @Override public Activity provide() {
        return AppContext.getContext().getCurrentTopActivity();
      }
    });
    uetMenu.getMenuView().setOnTouchListener(new View.OnTouchListener() {
      private float downX, downY;
      private float lastY;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            downX = event.getRawX();
            downY = event.getRawY();
            lastY = downY;
            break;
          case MotionEvent.ACTION_MOVE:
            params.y += event.getRawY() - lastY;
            windowManager.updateViewLayout(uetMenu, params);
            lastY = event.getRawY();
            break;
          case MotionEvent.ACTION_UP:
            if (Math.abs(event.getRawX() - downX) < touchSlop
                && Math.abs(event.getRawY() - downY) < touchSlop) {
              try {
                Field field = View.class.getDeclaredField("mListenerInfo");
                field.setAccessible(true);
                Object object = field.get(uetMenu.getMenuView());
                field = object.getClass().getDeclaredField("mOnClickListener");
                field.setAccessible(true);
                object = field.get(object);
                if (object != null && object instanceof View.OnClickListener) {
                  ((View.OnClickListener) object).onClick(uetMenu.getMenuView());
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            break;
        }
        return true;
      }
    });
    windowManager.addView(uetMenu, getLayoutParams());
  }

  private void removeMenu() {
    if (uetMenu != null) {
      windowManager.removeView(uetMenu);
    }
  }

  private void updateDraweeView() {
    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
        .setUri(
            "http://p0.ifengimg.com/pmop/2017/0823/3B8D6E5B199841F33C1FFB62D849C1D89F6BAA2B_size79_w240_h240.gif")
        .setAutoPlayAnimations(true)
        .build();
    draweeView.getHierarchy().setFailureImage(R.mipmap.ic_launcher);
    draweeView.getHierarchy().setPlaceholderImage(R.mipmap.ic_placeholder);

    draweeView.setController(draweeController);
  }
}
