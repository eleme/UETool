package me.ele.uetool;

import android.app.Activity;
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
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.SpannedString;
import android.text.style.ImageSpan;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.ele.uetool.base.Application;

import static android.view.View.NO_ID;

public class Util {

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

    public static String getResourceName(int id) {
        Resources resources = Application.getApplicationContext().getResources();
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
                return "0x" + Integer.toHexString(id);
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

    public static List<Pair<String, Bitmap>> getTextViewBitmap(TextView textView) {
        List<Pair<String, Bitmap>> bitmaps = new ArrayList<>();
        bitmaps.addAll(getTextViewDrawableBitmap(textView));
        bitmaps.addAll(getTextViewImageSpanBitmap(textView));
        return bitmaps;
    }

    private static List<Pair<String, Bitmap>> getTextViewDrawableBitmap(TextView textView) {
        List<Pair<String, Bitmap>> bitmaps = new ArrayList<>();
        try {
            Drawable[] drawables = textView.getCompoundDrawables();
            bitmaps.add(new Pair<>("DrawableLeft", getDrawableBitmap(drawables[0])));
            bitmaps.add(new Pair<>("DrawableTop", getDrawableBitmap(drawables[1])));
            bitmaps.add(new Pair<>("DrawableRight", getDrawableBitmap(drawables[2])));
            bitmaps.add(new Pair<>("DrawableBottom", getDrawableBitmap(drawables[3])));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmaps;
    }

    private static List<Pair<String, Bitmap>> getTextViewImageSpanBitmap(TextView textView) {
        List<Pair<String, Bitmap>> bitmaps = new ArrayList<>();
        try {
            CharSequence text = textView.getText();
            if (text instanceof SpannedString) {
                Field mSpansField = Class.forName("android.text.SpannableStringInternal").getDeclaredField("mSpans");
                mSpansField.setAccessible(true);
                Object[] spans = (Object[]) mSpansField.get(text);
                for (Object span : spans) {
                    if (span instanceof ImageSpan) {
                        bitmaps.add(new Pair<>("SpanBitmap", getDrawableBitmap(((ImageSpan) span).getDrawable())));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmaps;
    }

    public static Bitmap getImageViewBitmap(ImageView imageView) {
        return getDrawableBitmap(imageView.getDrawable());
    }

    private static Bitmap getDrawableBitmap(Drawable drawable) {
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
                Field mCachedBitmapField = Class.forName("android.support.graphics.drawable.VectorDrawableCompat$VectorDrawableCompatState").getDeclaredField("mCachedBitmap");
                mCachedBitmapField.setAccessible(true);
                return (Bitmap) mCachedBitmapField.get(mVectorStateField.get(drawable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImageViewScaleType(ImageView imageView) {
        return imageView.getScaleType().name();
    }

    public static void clipText(String clipText) {
        Context context = Application.getApplicationContext();
        ClipData clipData = ClipData.newPlainText("", clipText);
        ((ClipboardManager) (context.getSystemService(Context.CLIPBOARD_SERVICE))).setPrimaryClip(clipData);
        Toast.makeText(context, "copied", Toast.LENGTH_SHORT).show();
    }

    public static Activity getCurrentActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);
            Field mActivitiesField = activityThreadClass.getDeclaredField("mActivities");
            mActivitiesField.setAccessible(true);
            Map activities = (Map) mActivitiesField.get(currentActivityThread);
            for (Object record : activities.values()) {
                Class recordClass = record.getClass();
                Field pausedField = recordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!(boolean) pausedField.get(record)) {
                    Field activityField = recordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
