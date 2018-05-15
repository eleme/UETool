package me.ele.uetool.base.item;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.ele.uetool.base.Element;

import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_HEIGHT;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_PADDING_BOTTOM;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_PADDING_LEFT;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_PADDING_RIGHT;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_PADDING_TOP;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_TEXT;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_TEXT_COLOR;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_TEXT_SIZE;
import static me.ele.uetool.base.item.EditTextItem.Type.TYPE_WIDTH;

public class EditTextItem extends ElementItem {

    private @Type
    int type;
    private String detail;

    public EditTextItem(String name, Element element, @Type int type, String detail) {
        super(name, element);
        this.type = type;
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public int getType() {
        return type;
    }

    @IntDef({
            TYPE_TEXT,
            TYPE_TEXT_SIZE,
            TYPE_TEXT_COLOR,
            TYPE_WIDTH,
            TYPE_HEIGHT,
            TYPE_PADDING_LEFT,
            TYPE_PADDING_RIGHT,
            TYPE_PADDING_TOP,
            TYPE_PADDING_BOTTOM,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int TYPE_TEXT = 1;
        int TYPE_TEXT_SIZE = 2;
        int TYPE_TEXT_COLOR = 3;
        int TYPE_WIDTH = 4;
        int TYPE_HEIGHT = 5;
        int TYPE_PADDING_LEFT = 6;
        int TYPE_PADDING_RIGHT = 7;
        int TYPE_PADDING_TOP = 8;
        int TYPE_PADDING_BOTTOM = 9;
    }
}
