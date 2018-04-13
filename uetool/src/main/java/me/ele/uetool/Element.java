package me.ele.uetool;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import me.ele.uetool.items.EditTextItem;
import me.ele.uetool.items.Item;
import me.ele.uetool.items.SwitchItem;
import me.ele.uetool.items.TextItem;
import me.ele.uetool.items.TitleItem;

public class Element {

  private View view;
  private Rect rect = new Rect();

  public Element(View view) {
    this.view = view;
    reset();
  }

  public View getView() {
    return view;
  }

  public Rect getRect() {
    return rect;
  }

  public void reset() {
    int[] location = new int[2];
    view.getLocationOnScreen(location);
    int width = view.getWidth();
    int height = view.getHeight();

    int left = location[0];
    int right = location[0] + width;
    int top = location[1];
    int bottom = location[1] + height;

    rect.set(left, top, right, bottom);
  }

  public List<Item> getAttrs() {
    List<Item> items = new ArrayList<>();
    items.add(new TitleItem("COMMON"));
    items.add(new TextItem("Class", view.getClass().getName()));
    items.add(new TextItem("Id", "0x" + Integer.toHexString(view.getId())));
    items.add(new TextItem("ResName", Util.getResourceName(view.getResources(), view.getId())));
    items.add(new TextItem("Clickable", Boolean.toString(view.isClickable()).toUpperCase()));
    items.add(new TextItem("Focused", Boolean.toString(view.isFocused()).toUpperCase()));
    if (view instanceof TextView) {
      items.add(new TitleItem("TextView"));
      TextView textView = ((TextView) view);
      items.add(new EditTextItem(this, EditTextItem.Type.TYPE_TEXT, "Text",
          textView.getText().toString()));
      items.add(new EditTextItem(this, EditTextItem.Type.TYPE_TEXT_SIZE, "TextSize（SP）",
          Util.px2sp(view.getContext(), textView.getTextSize()) + ""));
      items.add(new EditTextItem(this, EditTextItem.Type.TYPE_TEXT_COLOR, "TextColor",
          Util.intToHexColor(textView.getCurrentTextColor())));
      items.add(
          new TextItem("TextHint", Util.intToHexColor(textView.getCurrentHintTextColor())));
      items.add(new SwitchItem(this, SwitchItem.Type.TYPE_IS_BOLD, "IsBold",
          textView.getTypeface() != null ? textView.getTypeface().isBold() : false));
    } else if (view instanceof ImageView) {
      items.add(new TitleItem("ImageView"));
    } else {
      items.add(new TitleItem("VIEW"));
    }
    items.add(new TextItem("Width",
        Util.px2dip(view.getContext(), view.getWidth()) + "dp"));
    items.add(new TextItem("Height",
        Util.px2dip(view.getContext(), view.getHeight()) + "dp"));
    items.add(new TextItem("Alpha", view.getAlpha() + ""));
    items.add(new TextItem("Background", Util.getBackground(view)));

    return items;
  }
}
