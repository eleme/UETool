package me.ele.uetool;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UETool {

  private static volatile UETool instance;
  private Set<String> filterClasses = new HashSet<>();
  private List<Element> elements = new ArrayList<>();

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
    if (activity.getClass() == TransparentActivity.class) return;
    elements.clear();
    traverse(activity.findViewById(android.R.id.content));
    Intent intent = new Intent(activity, TransparentActivity.class);
    activity.startActivity(intent);
    activity.overridePendingTransition(0, 0);
  }

  private void traverse(View view) {
    if (filterClasses.contains(view.getClass().getName())) return;
    if (view.getVisibility() != View.VISIBLE) return;
    if (view.getAlpha() == 0) return;
    if (!view.isEnabled()) return;
    if ("DESABLE_UETOOL".equals(view.getTag())) return;
    elements.add(new Element(view));
    if (view instanceof ViewGroup) {
      ViewGroup parent = (ViewGroup) view;
      for (int i = 0; i < parent.getChildCount(); i++) {
        traverse(parent.getChildAt(i));
      }
    }
  }

  public List<Element> getElements() {
    return elements;
  }

  public void clear() {
    elements.clear();
  }
}
