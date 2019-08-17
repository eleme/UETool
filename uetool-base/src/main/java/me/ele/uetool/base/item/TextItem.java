package me.ele.uetool.base.item;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

public class TextItem extends TitleItem {

    private String detail;
    private boolean enableCopy;
    private View.OnClickListener onClickListener;

    public TextItem(String name, String detail) {
        super(name);
        this.detail = detail;
    }

    public TextItem(String name, String detail, boolean enableCopy) {
        super(name);
        this.detail = detail;
        this.enableCopy = enableCopy;
    }

    public TextItem(String name, String detail, View.OnClickListener onClickListener) {
        super(name);
        this.detail = detail;
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