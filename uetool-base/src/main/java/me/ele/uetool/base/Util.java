package me.ele.uetool.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import java.lang.reflect.Field;

public class Util {

  public static Bitmap getDrawableBitmap(Drawable drawable) {
    try {
      if (drawable instanceof BitmapDrawable) {
        return ((BitmapDrawable) drawable).getBitmap();
      } else if (drawable instanceof NinePatchDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          Field mNinePatchFiled = NinePatchDrawable.class.getDeclaredField("mNinePatch");
          mNinePatchFiled.setAccessible(true);
          NinePatch ninePatch = (NinePatch) mNinePatchFiled.get(drawable);
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
}
