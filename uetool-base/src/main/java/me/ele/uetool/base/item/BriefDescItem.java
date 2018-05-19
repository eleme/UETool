package me.ele.uetool.base.item;

import me.ele.uetool.base.Element;

public class BriefDescItem extends ElementItem {

    private boolean isSelected;

    public BriefDescItem(Element element) {
        this(element, false);
    }

    public BriefDescItem(Element element, boolean isSelected) {
        super("", element);
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
