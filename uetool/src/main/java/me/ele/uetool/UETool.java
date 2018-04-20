package me.ele.uetool;

import android.app.Activity;
import android.content.Intent;
import java.util.HashSet;
import java.util.Set;

import static me.ele.uetool.TransparentActivity.Type.TYPE_UNKNOWN;

public class UETool {

  private static volatile UETool instance;
  private Set<String> filterClasses = new HashSet<>();
  private Activity targetActivity;

  private UETool() {

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

  public void put(String className) {
    filterClasses.add(className);
  }

  public void open(final Activity activity) {
    open(activity, TYPE_UNKNOWN);
  }

  public void open(final Activity activity, @TransparentActivity.Type int type) {
    if (activity.getClass() == TransparentActivity.class) return;
    targetActivity = activity;
    Intent intent = new Intent(activity, TransparentActivity.class);
    intent.putExtra(TransparentActivity.EXTRA_TYPE, type);
    activity.startActivity(intent);
    activity.overridePendingTransition(0, 0);
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
