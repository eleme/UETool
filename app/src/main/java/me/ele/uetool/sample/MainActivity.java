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
import android.view.WindowManager;
import android.widget.FrameLayout;
import me.ele.uetool.suspend.UETMenu;
import me.wangyuwei.uetool.sample.R;

public class MainActivity extends AppCompatActivity {

  private WindowManager windowManager;
  private UETMenu uetMenu;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    uetMenu = new UETMenu(this, new UETMenu.CurrentTopActivityProvider() {
      @Override public Activity provide() {
        return MainActivity.this;
      }
    });
    addMenu();
  }

  @TargetApi(Build.VERSION_CODES.M) private void requestPermission(Context context) {
    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:" + context.getPackageName()));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private WindowManager.LayoutParams getLayoutParams() {
    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
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
    windowManager.addView(uetMenu, getLayoutParams());
  }

  private void removeMenu() {
    windowManager.removeView(uetMenu);
  }
}
