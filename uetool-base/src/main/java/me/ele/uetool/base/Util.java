package me.ele.uetool.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.TypedValue;
import android.widget.Toast;
import java.lang.reflect.Field;

public class Util {

  public static Bitmap getDrawableBitmap(Drawable drawable) {
    try {
      if (drawable instanceof BitmapDrawable) {
        return ((BitmapDrawable) drawable).getBitmap();
      } else if (drawable instanceof NinePatchDrawable) {
        NinePatch ninePatch = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          Field mNinePatchStateFiled = NinePatchDrawable.class.getDeclaredField("mNinePatchState");
          mNinePatchStateFiled.setAccessible(true);
          Object mNinePatchState = mNinePatchStateFiled.get(drawable);
          Field mNinePatchFiled = mNinePatchState.getClass().getDeclaredField("mNinePatch");
          mNinePatchFiled.setAccessible(true);
          ninePatch = (NinePatch) mNinePatchFiled.get(mNinePatchState);
          return ninePatch.getBitmap();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          Field mNinePatchFiled = NinePatchDrawable.class.getDeclaredField("mNinePatch");
          mNinePatchFiled.setAccessible(true);
          ninePatch = (NinePatch) mNinePatchFiled.get(drawable);
          return ninePatch.getBitmap();
        }
      } else if (drawable instanceof ClipDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          return ((BitmapDrawable) ((ClipDrawable) drawable).getDrawable()).getBitmap();
        }
      } else if (drawable instanceof StateListDrawable) {
        return ((BitmapDrawable) drawable.getCurrent()).getBitmap();
      } else if (drawable instanceof VectorDrawableCompat) {
        Field mVectorStateField = VectorDrawableCompat.class.getDeclaredField("mVectorState");
        mVectorStateField.setAccessible(true);
        Field mCachedBitmapField = Class.forName(
            "android.support.graphics.drawable.VectorDrawableCompat$VectorDrawableCompatState")
            .getDeclaredField("mCachedBitmap");
        mCachedBitmapField.setAccessible(true);
        return (Bitmap) mCachedBitmapField.get(mVectorStateField.get(drawable));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static int px2dip(Context context, float pxValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5F);
  }

  public static int dip2px(Context context, float dpValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5F);
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

  public static int getStatusBarHeight(Context context) {
    Resources resources = context.getResources();
    int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
    return resId > 0 ? resources.getDimensionPixelSize(resId) : 0;
  }

  public static void clipText(Context context, String clipText) {
    ClipData clipData = ClipData.newPlainText("", clipText);
    ((ClipboardManager) (context.getSystemService(Context.CLIPBOARD_SERVICE))).setPrimaryClip(
        clipData);
    Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
  }
}
