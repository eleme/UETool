package me.ele.uetool;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import java.util.HashSet;
import java.util.Set;

import static me.ele.uetool.TransparentActivity.Type.TYPE_UNKNOWN;

public class UETool {

  private static volatile UETool instance;
  private Application application;
  private Set<String> filterClasses = new HashSet<>();
  private Activity targetActivity;
  private Activity currentTopActivity;

  private UETool(Application application) {
    this.application = application;
    this.application.registerActivityLifecycleCallbacks(
        new Application.ActivityLifecycleCallbacks() {
          @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            currentTopActivity = activity;
          }

          @Override public void onActivityStarted(Activity activity) {

          }

          @Override public void onActivityResumed(Activity activity) {
            currentTopActivity = activity;
          }

          @Override public void onActivityPaused(Activity activity) {

          }

          @Override public void onActivityStopped(Activity activity) {

          }

          @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

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

  public void open() {
    open(TYPE_UNKNOWN);
  }

  public void open(@TransparentActivity.Type int type) {
    if (currentTopActivity.getClass() == TransparentActivity.class) {
      currentTopActivity.finish();
      return;
    }
    targetActivity = currentTopActivity;
    Intent intent = new Intent(targetActivity, TransparentActivity.class);
    intent.putExtra(TransparentActivity.EXTRA_TYPE, type);
    targetActivity.startActivity(intent);
    targetActivity.overridePendingTransition(0, 0);
  }

  public Set<String> getFilterClasses() {
    return filterClasses;
  }

  public Activity getTargetActivity() {
    return targetActivity;
  }

  public void release() {
    targetActivity = null;
  }
}
