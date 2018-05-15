package me.ele.uetool.base;

import android.content.Context;

import java.lang.reflect.Method;

public class Application {

    private static Context CONTEXT;

    private Application() {
    }

    public static Context getApplicationContext() {
        if (CONTEXT != null) {
            return CONTEXT;
        } else {
            try {
                Class activityThreadClass = Class.forName("android.app.ActivityThread");
                Method method = activityThreadClass.getMethod("currentApplication");
                CONTEXT = (Context) method.invoke(null);
                return CONTEXT;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
