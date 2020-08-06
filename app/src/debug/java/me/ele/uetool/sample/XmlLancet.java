package me.ele.uetool.sample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import me.ele.lancet.base.Origin;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;
import me.ele.uetool.sample.ViewStubInfoMap.ViewStubInfo;

public class XmlLancet {

    @Proxy("inflate")
    @TargetClass(value = "android.view.LayoutInflater")
    public View inflate(int resourceId, ViewGroup root) {
        View view = (View) Origin.call();
        traverse(view, getResourceName(view, resourceId), null);
        return view;
    }

    @Proxy("inflate")
    @TargetClass(value = "android.view.LayoutInflater")
    public View inflate(int resourceId, ViewGroup root, boolean attachToRoot) {
        View view = (View) Origin.call();
        traverse(view, getResourceName(view, resourceId), null);
        return view;
    }

    @Proxy("inflate")
    @TargetClass(value = "android.view.View")
    public static View inflate(Context context, int resourceId, ViewGroup root) {
        View view = (View) Origin.call();
        traverse(view, getResourceName(view, resourceId), null);
        return view;
    }

    @Proxy("inflate")
    @TargetClass(value = "android.view.ViewStub")
    public View inflate() {
        View view = (View) Origin.call();
        final ViewStubInfo viewStubInfo = ViewStubInfoMap.get(view.getId());
        traverse(view, viewStubInfo.getViewStubInflatedXmlResourceName(), viewStubInfo.getViewStubXmlResourceName());
        // set origin id
        if (viewStubInfo.isOriginInflateIdValid()) {
            view.setId(viewStubInfo.getOriginInflateId());
        }
        return view;
    }

    private static void traverse(View view, String xmlName, String viewStubXmlName) {
        if (view instanceof ViewStub) {
            final String viewStubXmlResourceName = getResourceName(view, ((ViewStub) view).getLayoutResource());
            ViewStubInfoMap.put((ViewStub) view, xmlName, viewStubXmlResourceName);
        } else {
            if (view.getTag(R.id.uetool_xml) == null) {
                view.setTag(R.id.uetool_xml, xmlName);
            }
            if (view.getTag(R.id.uetool_xml_view_stub) == null && viewStubXmlName != null) {
                view.setTag(R.id.uetool_xml_view_stub, viewStubXmlName);
            }
            if (view instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) view;
                for (int i = 0; i < parent.getChildCount(); i++) {
                    traverse(parent.getChildAt(i), xmlName, viewStubXmlName);
                }
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
