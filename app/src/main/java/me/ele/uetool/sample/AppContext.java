package me.ele.uetool.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class AppContext extends Application {

  private static AppContext sContext;
  private Activity currentTopActivity;

  public AppContext() {
    sContext = this;
  }

  public static AppContext getContext() {
    return sContext;
  }

  @Override public void onCreate() {
    super.onCreate();
    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
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

  public Activity getCurrentTopActivity() {
    return currentTopActivity;
  }
}
