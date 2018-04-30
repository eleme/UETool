package me.ele.uetool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.ele.uetool.base.IAttrs;
import me.ele.uetool.suspend.UETMenu;

public class UETool {

  private static volatile UETool instance;
  private Application application;
  private Set<String> filterClasses = new HashSet<>();
  private Activity targetActivity;
  private Activity currentTopActivity;
  private UETMenu uetMenu;
  private List<IAttrs> attrsList = new ArrayList<>();

  private UETool() {
    application = getApplicationContext();
    application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
      @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentTopActivity = activity;
      }

      @Override public void onActivityResumed(Activity activity) {
        currentTopActivity = activity;
      }

      @Override public void onActivityDestroyed(Activity activity) {
        if (currentTopActivity != null && currentTopActivity.equals(activity)) {
          currentTopActivity = null;
        }
      }
    });
    for (String attrsClassName : Arrays.asList("me.ele.uetool.fresco.UETFresco")) {
      try {
        attrsList.add((IAttrs) Class.forName(attrsClassName).newInstance());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static UETool getInstance() {
    if (instance == null) {
      synchronized (UETool.class) {
        if (instance == null) {
          instance = new UETool();
        }
      }
    }
    return instance;
  }

  public static void putFilterClassName(String className) {
    getInstance().put(className);
  }

  public static boolean showUETMenu() {
    return getInstance().showMenu();
  }

  public static boolean showUETMenu(int y) {
    return getInstance().showMenu(y);
  }

  public static int dismissUETMenu() {
    return getInstance().dismissMenu();
  }

  static Application getApplication() {
    return getInstance().application;
  }

  private void put(String className) {
    filterClasses.add(className);
  }

  private boolean showMenu() {
    return showMenu(10);
  }

  private boolean showMenu(int y) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (!Settings.canDrawOverlays(getApplication())) {
        requestPermission(getApplication());
        Toast.makeText(application, "After grant this permission, re-enable UETool",
            Toast.LENGTH_LONG).show();
        return false;
      }
    }
    if (uetMenu == null) {
      uetMenu = new UETMenu(getApplication(), y);
    }
    uetMenu.show();
    return true;
  }

  private int dismissMenu() {
    if (uetMenu != null) {
      int y = uetMenu.dismiss();
      uetMenu = null;
      return y;
    }
    return -1;
  }

  public Set<String> getFilterClasses() {
    return filterClasses;
  }

  public Activity getTargetActivity() {
    return targetActivity;
  }

  public void setTargetActivity(Activity targetActivity) {
    this.targetActivity = targetActivity;
  }

  public Activity getCurrentTopActivity() {
    return currentTopActivity;
  }

  public List<IAttrs> getAttrsList() {
    return attrsList;
  }

  public void release() {
    targetActivity = null;
  }

  @TargetApi(Build.VERSION_CODES.M) private void requestPermission(Context context) {
    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:" + context.getPackageName()));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  private Application getApplicationContext() {
    try {
      final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
      final Method method = activityThreadClass.getMethod("currentApplication");
      return (Application) method.invoke(null, (Object[]) null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static class ActivityLifecycleCallbacksAdapter
      implements Application.ActivityLifecycleCallbacks {
    public ActivityLifecycleCallbacksAdapter() {
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }
  }
}
