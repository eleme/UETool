package me.ele.uetool.items;

import me.ele.uetool.Element;
import me.ele.uetool.base.Item;
import me.ele.uetool.base.TitleItem;

public class ElementItem extends TitleItem {

  private Element element;

  public ElementItem(String name, Element element) {
    super(name);
    this.element = element;
  }

  public Element getElement() {
    return element;
  }

}
