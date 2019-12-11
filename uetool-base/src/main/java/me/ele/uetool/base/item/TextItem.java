package me.ele.uetool.base.item;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

public class TextItem extends TitleItem {

    private String detail;
    private boolean enableCopy;
    private View.OnClickListener onClickListener;

    public TextItem(String name, String detail) {
        this(name, detail, false, null);
    }

    public TextItem(String name, String detail, boolean enableCopy) {
        this(name, detail, enableCopy, null);
    }

    public TextItem(String name, String detail, View.OnClickListener onClickListener) {
        this(name, detail, false, onClickListener);
    }

    public TextItem(String name, String detail, boolean enableCopy, View.OnClickListener onClickListener) {
        super(name);
        this.detail = detail;
        this.enableCopy = enableCopy;
        this.onClickListener = onClickListener;
    }

    public String getDetail() {
        return detail;
    }

    //  是否可复制文案
    public boolean isEnableCopy() {
        return enableCopy;
    }

    @Nullable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public boolean isValid() {
        if (TextUtils.isEmpty(detail)) {
            return false;
        }
        return true;
    }
}