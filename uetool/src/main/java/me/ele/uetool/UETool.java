package me.ele.uetool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import me.ele.uetool.attrdialog.AttrsDialogMultiTypePool;
import me.ele.uetool.attrdialog.binder.AddMinusEditTextItemBinder;
import me.ele.uetool.attrdialog.binder.BitmapItemBinder;
import me.ele.uetool.attrdialog.binder.BriefDescItemBinder;
import me.ele.uetool.attrdialog.binder.EditTextItemBinder;
import me.ele.uetool.attrdialog.binder.SwitchItemBinder;
import me.ele.uetool.attrdialog.binder.TextItemBinder;
import me.ele.uetool.attrdialog.binder.TitleItemBinder;
import me.ele.uetool.base.Application;
import me.ele.uetool.base.ItemViewBinder;
import me.ele.uetool.base.item.AddMinusEditItem;
import me.ele.uetool.base.item.BitmapItem;
import me.ele.uetool.base.item.BriefDescItem;
import me.ele.uetool.base.item.EditTextItem;
import me.ele.uetool.base.item.Item;
import me.ele.uetool.base.item.SwitchItem;
import me.ele.uetool.base.item.TextItem;
import me.ele.uetool.base.item.TitleItem;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class UETool {

    private static volatile UETool instance;
    private Set<String> filterClassesSet = new HashSet<>();
    private Set<String> attrsProviderSet = new LinkedHashSet<String>() {
        {
            add(UETCore.class.getName());
            add("me.ele.uetool.fresco.UETFresco");
        }
    };
    private Activity targetActivity;
    private UETMenu uetMenu;
    private AttrsDialogMultiTypePool attrsDialogMultiTypePool = new AttrsDialogMultiTypePool();

    private UETool() {
        initAttrsDialogMultiTypePool();
    }

    static UETool getInstance() {
        if (instance == null) {
            synchronized (UETool.class) {
                if (instance == null) {
                    instance = new UETool();
                }
            }
        }
        return instance;
    }

    public static void putFilterClass(Class clazz) {
        putFilterClass(clazz.getName());
    }

    public static void putFilterClass(String className) {
        getInstance().putFilterClassName(className);
    }

    public static <T extends Item> void registerAttrDialogItemViewBinder(Class<T> clazz, ItemViewBinder<T, ?> binder) {
        getInstance().attrsDialogMultiTypePool.register(clazz, binder);
    }

    public static void putAttrsProviderClass(Class clazz) {
        putAttrsProviderClass(clazz.getName());
    }

    public static void putAttrsProviderClass(String className) {
        getInstance().putAttrsProviderClassName(className);
    }

    public static boolean showUETMenu() {
        return getInstance().showMenu();
    }

    public static boolean showUETMenu(int y) {
        return getInstance().showMenu(y);
    }

    public static int dismissUETMenu() {
        return getInstance().dismissMenu();
    }

    private void putFilterClassName(String className) {
        filterClassesSet.add(className);
    }

    private void putAttrsProviderClassName(String className) {
        attrsProviderSet.add(className);
    }

    private boolean showMenu() {
        return showMenu(10);
    }

    private boolean showMenu(int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(Application.getApplicationContext())) {
                requestPermission(Application.getApplicationContext());
                Toast.makeText(Application.getApplicationContext(), "After grant this permission, re-enable UETool", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (uetMenu == null) {
            uetMenu = new UETMenu(Application.getApplicationContext(), y);
        }
        if (!uetMenu.isShown()) {
            uetMenu.show();
            return true;
        }
        return false;
    }

    private int dismissMenu() {
        if (uetMenu != null) {
            int y = uetMenu.dismiss();
            uetMenu = null;
            return y;
        }
        return -1;
    }

    public Set<String> getFilterClasses() {
        return filterClassesSet;
    }

    public Activity getTargetActivity() {
        return targetActivity;
    }


    public void setTargetActivity(Activity targetActivity) {
        this.targetActivity = targetActivity;
    }

    public AttrsDialogMultiTypePool getAttrsDialogMultiTypePool() {
        return attrsDialogMultiTypePool;
    }

    public Set<String> getAttrsProvider() {
        return attrsProviderSet;
    }

    void release() {
        targetActivity = null;
    }

    private void initAttrsDialogMultiTypePool() {
        attrsDialogMultiTypePool.register(AddMinusEditItem.class, new AddMinusEditTextItemBinder());
        attrsDialogMultiTypePool.register(BitmapItem.class, new BitmapItemBinder());
        attrsDialogMultiTypePool.register(BriefDescItem.class, new BriefDescItemBinder());
        attrsDialogMultiTypePool.register(EditTextItem.class, new EditTextItemBinder());
        attrsDialogMultiTypePool.register(SwitchItem.class, new SwitchItemBinder());
        attrsDialogMultiTypePool.register(TextItem.class, new TextItemBinder());
        attrsDialogMultiTypePool.register(TitleItem.class, new TitleItemBinder());
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
