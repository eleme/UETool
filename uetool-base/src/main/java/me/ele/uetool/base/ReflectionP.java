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

    /**
     * 用Unsafe清除classloader
     * 该方法仅对同一个类生效，所以必须在需要的类中添加使用，不可以跨类调用
     *
     * @param cls 需要清除Loader的类
     */
    @SuppressWarnings({"unchecked", "JavaReflectionMemberAccess"})
    @TargetApi(Build.VERSION_CODES.P)
    private static void clearClassLoaderInClass(Class cls) {
        try {
            //Unsafe类
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            //取Unsafe实例字段
            Field unsafeInstanceField = unsafeClass.getDeclaredField("theUnsafe");
            //放开Unsafe实例字段权限
            unsafeInstanceField.setAccessible(true);
            //取Unsafe实例
            Object unsafeInstance = unsafeInstanceField.get(null);
            //取objectFieldOffset方法取偏移量
            Method objectFieldOffset = unsafeClass.getMethod("objectFieldOffset", Field.class);
            //Class的classLoader字段
            Field classLoaderField = Class.class.getDeclaredField("classLoader");
            //使classLoader可见
            classLoaderField.setAccessible(true);
            //取putObject方法进行置空
            Method putObject = unsafeClass.getMethod("putObject", Object.class, long.class, Object.class);
            //值为8，这里不用硬编码，看什么时候等到classLoader字段被404了再用
            long offset = (long) objectFieldOffset.invoke(unsafeInstance, classLoaderField);

            Log.i("@", "clearClassLoaderInClass: classLoader offset=" + offset);
            //偏移量为8处置空
            putObject.invoke(unsafeInstance, cls, offset, null);
        } catch (Throwable throwable) {
            Log.e("@", "clearClassLoaderInClass: ", throwable);
        }
    }

    /**
     * 恢复classloader:清除loader可能会造成问题
     * (java.lang.NoClassDefFoundError: Class not found using the boot class loader; no stack trace
     * available)
     * 反射完成后恢复loader
     *
     * @param cls 需要恢复Loader的类
     */
    @TargetApi(Build.VERSION_CODES.P)
    private static void restoreLoaderInClass(Class cls) {
        try {
            Field classLoaderField = Class.class.getDeclaredField("classLoader");
            classLoaderField.setAccessible(true);
            //If this object represents a primitive type or void, null is returned.
            if (cls != null && !cls.isPrimitive() && classLoaderField.get(cls) == null) {
                Log.w("@", "restoreLoaderInClass: classloader is null!");
                classLoaderField.set(cls, Thread.currentThread().getContextClassLoader());
            }
        } catch (Exception e) {
            Log.e("@", "restoreLoaderInClass: ", e);
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
