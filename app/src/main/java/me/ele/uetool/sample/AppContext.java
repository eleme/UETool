package me.ele.uetool.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import me.ele.uetool.UETool;

public class AppContext extends Application {

  @Override public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
    Fresco.initialize(this);

    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

      private int visibleActivityCount;

      @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

      }

      @Override public void onActivityStarted(Activity activity) {
        visibleActivityCount++;
        if (visibleActivityCount == 1) {

        }
      }

      @Override public void onActivityResumed(Activity activity) {

      }

      @Override public void onActivityPaused(Activity activity) {

      }

      @Override public void onActivityStopped(Activity activity) {
        visibleActivityCount--;
        if (visibleActivityCount == 0) {
          UETool.dismissUETMenu();
        }
      }

      @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

      }

      @Override public void onActivityDestroyed(Activity activity) {

      }
    });
  }
}
