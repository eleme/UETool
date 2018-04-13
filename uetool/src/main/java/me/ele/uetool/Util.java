package me.ele.uetool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import java.lang.reflect.Field;

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
    try {
      if (id == NO_ID || id == 0) {
        return "";
      } else {
        return resources.getResourceEntryName(id);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String intToHexColor(int color) {
    return "#" + Integer.toHexString(color).toUpperCase();
  }

  public static String getBackground(View view) {
    Drawable drawable = view.getBackground();
    if (drawable instanceof ColorDrawable) {
      return intToHexColor(((ColorDrawable) drawable).getColor());
    } else if (drawable instanceof GradientDrawable) {
      try {
        Field mFillPaintField = GradientDrawable.class.getDeclaredField("mFillPaint");
        mFillPaintField.setAccessible(true);
        Paint mFillPaint = (Paint) mFillPaintField.get(drawable);
        Shader shader = mFillPaint.getShader();
        if (shader instanceof LinearGradient) {
          Field mColorsField = LinearGradient.class.getDeclaredField("mColors");
          mColorsField.setAccessible(true);
          int[] mColors = (int[]) mColorsField.get(shader);
          StringBuilder sb = new StringBuilder();
          for (int i = 0, N = mColors.length; i < N; i++) {
            sb.append(intToHexColor(mColors[i]));
            if (i < N - 1) {
              sb.append(" -> ");
            }
          }
          return sb.toString();
        }
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      return "";
    }
    return "";
  }
}
