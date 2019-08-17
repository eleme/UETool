package me.ele.uetool;

import me.ele.uetool.base.ItemViewBinder;
import me.ele.uetool.base.item.Item;

public class UETool {

    public static void putFilterClass(Class clazz) {
    }

    public static void putFilterClass(String className) {
    }

    public static void putAttrsProviderClass(Class clazz) {
    }

    public static void putAttrsProviderClass(String className) {
    }

    public static boolean showUETMenu() {
        return false;
    }

    public static boolean showUETMenu(int y) {
        return false;
    }

    public static int dismissUETMenu() {
        return -1;
    }

    public static <T extends Item> void registerAttrDialogItemViewBinder(Class<T> clazz, ItemViewBinder<T, ?> binder) {
    }
}
