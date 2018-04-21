package me.ele.uetool.items;

import me.ele.uetool.Element;

public class ElementItem extends Item {

  private Element element;

  public ElementItem(Element element) {
    this.element = element;
  }

  public Element getElement() {
    return element;
  }

}
