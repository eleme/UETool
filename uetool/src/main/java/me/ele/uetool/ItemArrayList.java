package me.ele.uetool;

import java.util.ArrayList;
import me.ele.uetool.items.Item;

public class ItemArrayList<T extends Item> extends ArrayList<T> {

  @Override public boolean add(T t) {
    if (!t.isValid()) {
      return false;
    }
    return super.add(t);
  }
}
