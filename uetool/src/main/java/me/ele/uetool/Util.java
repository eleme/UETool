package me.ele.uetool;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.FadeDrawable;
import com.facebook.drawee.drawable.ScaleTypeDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
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

  public static String getResId(View view) {
    try {
      int id = view.getId();
      if (id == NO_ID) {
        return "";
      } else {
        return "0x" + id;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String intToHexColor(int color) {
    return "#" + Integer.toHexString(color).toUpperCase();
  }

  public static Object getBackground(View view) {
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
    } else {
      return getDrawableBitmap(drawable);
    }
    return null;
  }

  public static String getImageURI(DraweeView draweeView) {
    PipelineDraweeControllerBuilder builder = getFrescoControllerBuilder(draweeView);
    if (builder != null) {
      return builder.getImageRequest().getSourceUri().toString();
    }
    return "";
  }

  public static String isSupportAnimation(DraweeView draweeView) {
    PipelineDraweeControllerBuilder builder = getFrescoControllerBuilder(draweeView);
    if (builder != null) {
      return String.valueOf(builder.getAutoPlayAnimations()).toUpperCase();
    }
    return "";
  }

  public static Bitmap getPlaceHolderBitmap(DraweeView draweeView) {
    GenericDraweeHierarchy hierarchy = (GenericDraweeHierarchy) draweeView.getHierarchy();
    if (hierarchy.hasPlaceholderImage()) {
      try {
        Field mFadeDrawableField = hierarchy.getClass().getDeclaredField("mFadeDrawable");
        mFadeDrawableField.setAccessible(true);
        FadeDrawable fadeDrawable = (FadeDrawable) mFadeDrawableField.get(hierarchy);
        Field mLayersField = fadeDrawable.getClass().getDeclaredField("mLayers");
        mLayersField.setAccessible(true);
        Drawable[] layers = (Drawable[]) mLayersField.get(fadeDrawable);
        // PLACEHOLDER_IMAGE_INDEX == 1
        Drawable drawable = layers[1];
        if (drawable instanceof BitmapDrawable) {
          return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ScaleTypeDrawable) {
          return ((BitmapDrawable) drawable.getCurrent()).getBitmap();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  private static PipelineDraweeControllerBuilder getFrescoControllerBuilder(DraweeView draweeView) {
    try {
      PipelineDraweeController controller = (PipelineDraweeController) draweeView.getController();
      Field mDataSourceSupplierFiled =
          PipelineDraweeController.class.getDeclaredField("mDataSourceSupplier");
      mDataSourceSupplierFiled.setAccessible(true);
      Supplier supplier = (Supplier) mDataSourceSupplierFiled.get(controller);
      Field mAutoField =
          Class.forName("com.facebook.drawee.controller.AbstractDraweeControllerBuilder$2")
              .getDeclaredField("this$0");
      mAutoField.setAccessible(true);
      PipelineDraweeControllerBuilder builder =
          (PipelineDraweeControllerBuilder) mAutoField.get(supplier);
      return builder;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Bitmap[] getTextViewDrawableBitmap(TextView textView) {
    Bitmap[] bitmaps = new Bitmap[2];
    try {
      Field mDrawablesField = TextView.class.getDeclaredField("mDrawables");
      mDrawablesField.setAccessible(true);
      Field mDrawableLeftInitialFiled = Class.forName("android.widget.TextView$Drawables")
          .getDeclaredField("mDrawableLeftInitial");
      mDrawableLeftInitialFiled.setAccessible(true);
      bitmaps[0] = ((BitmapDrawable) mDrawableLeftInitialFiled.get(
          mDrawablesField.get(textView))).getBitmap();

      Field mDrawableRightInitialFiled = Class.forName("android.widget.TextView$Drawables")
          .getDeclaredField("mDrawableRightInitial");
      mDrawableRightInitialFiled.setAccessible(true);
      bitmaps[1] = ((BitmapDrawable) mDrawableRightInitialFiled.get(
          mDrawablesField.get(textView))).getBitmap();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bitmaps;
  }

  public static Bitmap getImageViewBitmap(ImageView imageView) {
    return getDrawableBitmap(imageView.getDrawable());
  }

  private static Bitmap getDrawableBitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else if (drawable instanceof NinePatchDrawable) {
      try {
        Field mNinePatchFiled = NinePatchDrawable.class.getDeclaredField("mNinePatch");
        mNinePatchFiled.setAccessible(true);
        NinePatch ninePatch = (NinePatch) mNinePatchFiled.get(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          return ninePatch.getBitmap();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (drawable instanceof ClipDrawable) {
      try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          return ((BitmapDrawable) ((ClipDrawable) drawable).getDrawable()).getBitmap();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (drawable instanceof StateListDrawable) {
      try {
        return ((BitmapDrawable) drawable.getCurrent()).getBitmap();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static void clipText(Context context, String clipText) {
    ClipData clipData = ClipData.newPlainText("", clipText);
    ((ClipboardManager) (context.getSystemService(Context.CLIPBOARD_SERVICE))).setPrimaryClip(
        clipData);
    Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
  }
}
