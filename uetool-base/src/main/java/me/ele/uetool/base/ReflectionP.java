package me.ele.uetool.base;

import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 来自 weishu FreeReflection
 * https://github.com/tiann/FreeReflection
 */
public class ReflectionP {

    private static final String TAG = "Reflection";

    private static Object sVmRuntime;
    private static Method setHiddenApiExemptions;

    static {
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            sVmRuntime = getRuntime.invoke(null);
        } catch (Throwable e) {
            Log.e(TAG, "reflect bootstrap failed:", e);
        }
    }

    public static <T> T breakAndroidP(Func<T> func) {
        T result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            exemptAll();
            result = func.call();
        } else {
            result = func.call();
        }
        return result;
    }

    /**
     * make specific methods exempted from hidden API check.
     *
     * @param methods the method signature prefix, such as "Ldalvik/system", "Landroid" or even "L"
     * @return true if success
     */
    private static boolean exempt(String... methods) {
        if (sVmRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }

        try {
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{methods});
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Make all hidden API exempted.
     *
     * @return true if success.
     */
    private static boolean exemptAll() {
        return exempt(new String[]{"L"});
    }

    public interface Func<T> {
        T call();
    }
}
