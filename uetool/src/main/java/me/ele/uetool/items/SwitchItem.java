package me.ele.uetool.items;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.ele.uetool.Element;

import static me.ele.uetool.items.SwitchItem.Type.TYPE_IS_BOLD;

public class SwitchItem extends ElementItem {

  @Type private int type;
  private boolean isChecked;

  public SwitchItem(String name, Element element, @Type int type, boolean isChecked) {
    super(name, element);
    this.type = type;
    this.isChecked = isChecked;
  }

  public boolean isChecked() {
    return isChecked;
  }

  public int getType() {
    return type;
  }

  @IntDef({
      TYPE_IS_BOLD,
  })
  @Retention(RetentionPolicy.SOURCE) public @interface Type {
    int TYPE_IS_BOLD = 1;
  }
}
