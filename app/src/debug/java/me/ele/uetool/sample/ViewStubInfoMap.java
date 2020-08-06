package me.ele.uetool.sample;

import android.view.View;
import android.view.ViewStub;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewStubInfoMap {

    private static Map<Integer, ViewStubInfo> map = new LinkedHashMap<Integer, ViewStubInfo>() {
        @Override protected boolean removeEldestEntry(final Entry<Integer, ViewStubInfo> eldest) {
            return size() > 50;
        }
    };

    public static void put(ViewStub viewStub, String xmlResourceName, String viewStubXmlResourceName) {
        final int newInflatedId = generateUniqueKey();
        final ViewStubInfo info = ViewStubInfo.create(viewStub.getInflatedId(), xmlResourceName, viewStubXmlResourceName);
        map.put(newInflatedId, info);
        viewStub.setInflatedId(newInflatedId);
    }

    public static ViewStubInfo get(int id) {
        return map.get(id);
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private static int generateUniqueKey() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) {
                newValue = 1; // Roll over to 1, not 0.
            }
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static class ViewStubInfo {

        private int originInflateId;

        private String viewStubInflatedXmlResourceName;

        private String viewStubXmlResourceName;

        private ViewStubInfo() {

        }

        public static ViewStubInfo create(int inflateId, String viewStubInflatedXmlResourceName,
                String viewStubXmlResourceName) {
            final ViewStubInfo viewStubInfo = new ViewStubInfo();
            viewStubInfo.originInflateId = inflateId;
            viewStubInfo.viewStubInflatedXmlResourceName = viewStubInflatedXmlResourceName;
            viewStubInfo.viewStubXmlResourceName = viewStubXmlResourceName;
            return viewStubInfo;
        }

        public boolean isOriginInflateIdValid() {
            return originInflateId != View.NO_ID;
        }

        public int getOriginInflateId() {
            return originInflateId;
        }

        public String getViewStubInflatedXmlResourceName() {
            return viewStubInflatedXmlResourceName;
        }

        public String getViewStubXmlResourceName() {
            return viewStubXmlResourceName;
        }
    }
}
