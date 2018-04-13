package me.ele.uetool;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import static android.view.View.NO_ID;

public class Util {

  public static int getStatusBarHeight(Context context) {
    Resources resources = context.getResources();
    int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
    return resId > 0 ? resources.getDimensionPixelSize(resId) : 0;
  }

  public static void enableFullscreen(@NonNull Window window) {
    if (Build.VERSION.SDK_INT >= 21) {
      addSystemUiFlag(window, 1280);
    }
  }

  private static void addSystemUiFlag(Window window, int flag) {
    View view = window.getDecorView();
    if (view != null) {
      view.setSystemUiVisibility(view.getSystemUiVisibility() | flag);
    }
  }

  public static void setStatusBarColor(@NonNull Window window, int color) {
    if (Build.VERSION.SDK_INT >= 21) {
      window.setStatusBarColor(color);
    }
  }

  public static int px2dip(Context context, float pxValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5F);
  }

  public static int sp2px(Context context, float sp) {
    return (int) TypedValue.applyDimension(2, sp, context.getResources().getDisplayMetrics());
  }

  public static int px2sp(Context context, float pxValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  public static int getScreenWidth(Context context) {
    return context.getResources().getDisplayMetrics().widthPixels;
  }

  public static int getScreenHeight(Context context) {
    return context.getResources().getDisplayMetrics().heightPixels;
  }

  public static String getResourceName(Resources resources, int id) {
    if (id == NO_ID) {
      return "";
    } else {
      return resources.getResourceEntryName(id);
    }
  }
}
