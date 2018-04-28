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
import java.util.HashSet;
import java.util.Set;
import me.ele.uetool.suspend.UETMenu;

public class UETool {

  private static volatile UETool instance;
  private Application application;
  private Set<String> filterClasses = new HashSet<>();
  private Activity targetActivity;
  private Activity currentTopActivity;
  private UETMenu uetMenu;

  private UETool(Application application) {
    this.application = application;
    this.application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
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
  }

  public static void init(Application application) {
    if (instance == null) {
      synchronized (UETool.class) {
        if (instance == null) {
          instance = new UETool(application);
        }
      }
    }
  }

  public static UETool getInstance() {
    if (instance == null) {
      throw new RuntimeException("Please init UETool!");
    }
    return instance;
  }

  public static Application getApplication() {
    return getInstance().application;
  }

  public void put(String className) {
    filterClasses.add(className);
  }

  public boolean showMenu() {
    return showMenu(10);
  }

  public boolean showMenu(int y) {
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

  public int dismissMenu() {
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

  public void release() {
    targetActivity = null;
  }

  @TargetApi(Build.VERSION_CODES.M) private void requestPermission(Context context) {
    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:" + context.getPackageName()));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
