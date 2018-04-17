package me.ele.uetool.items;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.ele.uetool.Element;

import static me.ele.uetool.items.EditTextItem.Type.TYPE_HEIGHT;
import static me.ele.uetool.items.EditTextItem.Type.TYPE_WIDTH;
import static me.ele.uetool.items.EditTextItem.Type.TYPE_TEXT;
import static me.ele.uetool.items.EditTextItem.Type.TYPE_TEXT_COLOR;
import static me.ele.uetool.items.EditTextItem.Type.TYPE_TEXT_SIZE;

public class EditTextItem extends ElementItem {

  private @Type int type;
  private String name;
  private String detail;

  public EditTextItem(Element element, @Type int type, String name, String detail) {
    super(element);
    this.type = type;
    this.name = name;
    this.detail = detail;
  }

  public String getName() {
    return name;
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
  })
  @Retention(RetentionPolicy.SOURCE) public @interface Type {
    int TYPE_TEXT = 1;
    int TYPE_TEXT_SIZE = 2;
    int TYPE_TEXT_COLOR = 3;
    int TYPE_WIDTH = 4;
    int TYPE_HEIGHT = 5;
  }
}
