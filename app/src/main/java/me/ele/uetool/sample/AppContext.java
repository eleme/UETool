package me.ele.uetool.sample;

import android.app.Activity;
import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import me.ele.uetool.ActivityLifecycleCallbacksAdapter;
import me.ele.uetool.UETool;

public class AppContext extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Fresco.initialize(this);
    UETool.init(this);
    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {

      private int mVisibleActivityCount;

      @Override public void onActivityStarted(Activity activity) {
        mVisibleActivityCount++;
        if (mVisibleActivityCount == 1) {

        }
      }

      @Override public void onActivityStopped(Activity activity) {
        mVisibleActivityCount--;
        if (mVisibleActivityCount == 0) {
          UETool.getInstance().dismissMenu();
        }
      }
    });
  }
}
