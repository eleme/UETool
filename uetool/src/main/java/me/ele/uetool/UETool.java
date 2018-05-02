package me.ele.uetool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UETool {

  private static volatile UETool instance;
  private Application application = getApplicationContext();
  private Set<String> filterClassesSet = new HashSet<>();
  private Set<String> attrsProviderSet = new LinkedHashSet<>();
  private Activity targetActivity;
  private UETMenu uetMenu;

  private UETool() {
    for (String attrsClassName : Arrays.asList(UETCore.class.getName(),
        "me.ele.uetool.fresco.UETFresco")) {
      attrsProviderSet.add(attrsClassName);
    }
  }

  static UETool getInstance() {
    if (instance == null) {
      synchronized (UETool.class) {
        if (instance == null) {
          instance = new UETool();
        }
      }
    }
    return instance;
  }

  public static void putFilterClass(Class clazz) {
    putFilterClass(clazz.getName());
  }

  public static void putFilterClass(String className) {
    getInstance().putFilterClassName(className);
  }

  public static void putAttrsProviderClass(Class clazz) {
    putAttrsProviderClass(clazz.getName());
  }

  public static void putAttrsProviderClass(String className) {
    getInstance().putAttrsProviderClassName(className);
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

  private void putFilterClassName(String className) {
    filterClassesSet.add(className);
  }

  private void putAttrsProviderClassName(String className) {
    attrsProviderSet.add(className);
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
    return filterClassesSet;
  }

  public Activity getTargetActivity() {
    return targetActivity;
  }

  public void setTargetActivity(Activity targetActivity) {
    this.targetActivity = targetActivity;
  }

  public Set<String> getAttrsProvider() {
    return attrsProviderSet;
  }

  void release() {
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
      Class activityThreadClass = Class.forName("android.app.ActivityThread");
      Method method = activityThreadClass.getMethod("currentApplication");
      return (Application) method.invoke(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public Activity getCurrentActivity() {
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
