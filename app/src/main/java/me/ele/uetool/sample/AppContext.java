package me.ele.uetool.sample;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import me.ele.uetool.UETool;

public class AppContext extends Application {

  private static AppContext sContext;

  public AppContext() {
    sContext = this;
  }

  public static AppContext getContext() {
    return sContext;
  }

  @Override public void onCreate() {
    super.onCreate();
    Fresco.initialize(this);
    UETool.init(this);
  }
}
