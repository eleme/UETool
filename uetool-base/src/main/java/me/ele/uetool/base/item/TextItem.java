package me.ele.uetool.base.item;

import android.text.TextUtils;

public class TextItem extends TitleItem {

    private String detail;
    private boolean enableCopy;

    public TextItem(String name, String detail) {
        super(name);
        this.detail = detail;
    }

    public TextItem(String name, String detail, boolean enableCopy) {
        super(name);
        this.detail = detail;
        this.enableCopy = enableCopy;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isEnableCopy() {
        return enableCopy;
    }

    @Override
    public boolean isValid() {
        if (TextUtils.isEmpty(detail)) {
            return false;
        }
        return true;
    }
}