package me.ele.uetool.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 来自 52 破解
 * https://www.52pojie.cn/thread-877958-1-1.html
 */
public class ReflectionP {
    @TargetApi(Build.VERSION_CODES.P)
    private static void clearClassLoaderInClass(Class cls) {
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field unsafeInstanceField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeInstanceField.setAccessible(true);
            Object unsafeInstance = unsafeInstanceField.get(null);
            Method objectFieldOffset = unsafeClass.getMethod("objectFieldOffset", Field.class);
            Field classLoaderField = Class.class.getDeclaredField("classLoader");
            classLoaderField.setAccessible(true);
            Method putObject = unsafeClass.getMethod("putObject", Object.class, long.class, Object.class);
            long offset = (long) objectFieldOffset.invoke(unsafeInstance, classLoaderField);
            putObject.invoke(unsafeInstance, cls, offset, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private static void restoreLoaderInClass(Class cls) {
        try {
            Field classLoaderField = Class.class.getDeclaredField("classLoader");
            classLoaderField.setAccessible(true);
            if (cls != null && !cls.isPrimitive() && classLoaderField.get(cls) == null) {
                classLoaderField.set(cls, Thread.currentThread().getContextClassLoader());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void breakAndroidP(Func0 func0) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            clearClassLoaderInClass(func0.getClass());
            func0.call();
            restoreLoaderInClass(func0.getClass());
        } else {
            func0.call();
        }
    }

    public interface Func0 {
        void call();
    }
}
