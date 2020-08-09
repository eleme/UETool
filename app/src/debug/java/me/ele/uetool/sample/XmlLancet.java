package me.ele.uetool.sample;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

public class XmlLancet {

    @Proxy("inflate")
    @TargetClass(value = "android.view.LayoutInflater")
    public View inflate(int resourceId, ViewGroup root) {
        View view = (View) Origin.call();
        traverse(view, getResourceName(view, resourceId));
        return view;
    }

    @Proxy("inflate")
    @TargetClass(value = "android.view.LayoutInflater")
    public View inflate(int resourceId, ViewGroup root, boolean attachToRoot) {
        View view = (View) Origin.call();
        traverse(view, getResourceName(view, resourceId));
        return view;
    }

    @Proxy("inflate")
    @TargetClass(value = "android.view.View")
    public static View inflate(Context context, int resourceId, ViewGroup root) {
        View view = (View) Origin.call();
        traverse(view, getResourceName(view, resourceId));
        return view;
    }

    private static void traverse(View view, String name) {
        if (view.getTag(R.id.uetool_xml) == null) {
            view.setTag(R.id.uetool_xml, name);
        }
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                traverse(parent.getChildAt(i), name);
            }
        }
    }

    private static String getResourceName(View view, int resourceId) {
        String resourceName = view.getResources().getResourceName(resourceId) + ".xml";
        String[] splits = resourceName.split("/");
        if (splits.length == 2) {
            return splits[1];
        }
        return resourceName;
    }

}
