package me.ele.uetool;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.DraweeView;
import java.util.List;
import me.ele.uetool.items.AddMinusEditItem;
import me.ele.uetool.items.BitmapItem;
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
    List<Item> items = new ItemArrayList<>();
    items.add(new TitleItem("COMMON"));
    items.add(new TextItem("Class", view.getClass().getName()));
    items.add(new TextItem("Id", Util.getResId(view)));
    items.add(new TextItem("ResName", Util.getResourceName(view.getResources(), view.getId())));
    items.add(new TextItem("Clickable", Boolean.toString(view.isClickable()).toUpperCase()));
    items.add(new TextItem("Focused", Boolean.toString(view.isFocused()).toUpperCase()));
    if (view instanceof TextView) {
      items.add(new TitleItem("TextView"));
      TextView textView = ((TextView) view);
      items.add(new EditTextItem(this, EditTextItem.Type.TYPE_TEXT, "Text",
          textView.getText().toString()));
      items.add(new AddMinusEditItem(this, EditTextItem.Type.TYPE_TEXT_SIZE, "TextSize（sp）",
          Util.px2sp(view.getContext(), textView.getTextSize()) + ""));
      items.add(new EditTextItem(this, EditTextItem.Type.TYPE_TEXT_COLOR, "TextColor",
          Util.intToHexColor(textView.getCurrentTextColor())));
      List<Pair<String, Bitmap>> pairs = Util.getTextViewBitmap((TextView) view);
      for (Pair<String, Bitmap> pair : pairs) {
        items.add(new BitmapItem(pair.first, pair.second));
      }
      items.add(new SwitchItem(this, SwitchItem.Type.TYPE_IS_BOLD, "IsBold",
          textView.getTypeface() != null ? textView.getTypeface().isBold() : false));
    } else if (view instanceof DraweeView) {
      items.add(new TitleItem("DraweeView"));
      items.add(new TextItem("ImageURI", Util.getImageURI((DraweeView) view), true));
      items.add(new TextItem("SupportAnimation", Util.isSupportAnimation((DraweeView) view)));
      items.add(new BitmapItem("PlaceHolderImage", Util.getPlaceHolderBitmap((DraweeView) view)));
    } else if (view instanceof ImageView) {
      items.add(new TitleItem("ImageView"));
      items.add(new BitmapItem("Bitmap", Util.getImageViewBitmap((ImageView) view)));
    } else {
      items.add(new TitleItem("VIEW"));
    }
    items.add(new AddMinusEditItem(this, EditTextItem.Type.TYPE_WIDTH, "Width（dp）",
        Util.px2dip(view.getContext(), view.getWidth()) + ""));
    items.add(new AddMinusEditItem(this, EditTextItem.Type.TYPE_HEIGHT, "Height（dp）",
        Util.px2dip(view.getContext(), view.getHeight()) + ""));
    items.add(new TextItem("Alpha", view.getAlpha() + ""));
    Object background = Util.getBackground(view);
    if (background instanceof String) {
      items.add(new TextItem("Background", (String) background));
    } else if (background instanceof Bitmap) {
      items.add(new BitmapItem("Background", (Bitmap) background));
    }
    items.add(new AddMinusEditItem(this, EditTextItem.Type.TYPE_PADDING_LEFT, "PaddingLeft（dp）",
        Util.px2dip(view.getContext(), view.getPaddingLeft()) + ""));
    items.add(new AddMinusEditItem(this, EditTextItem.Type.TYPE_PADDING_RIGHT, "PaddingRight（dp）",
        Util.px2dip(view.getContext(), view.getPaddingRight()) + ""));
    items.add(new AddMinusEditItem(this, EditTextItem.Type.TYPE_PADDING_TOP, "PaddingTop（dp）",
        Util.px2dip(view.getContext(), view.getPaddingTop()) + ""));
    items.add(new AddMinusEditItem(this, EditTextItem.Type.TYPE_PADDING_BOTTOM, "PaddingBottom（dp）",
        Util.px2dip(view.getContext(), view.getPaddingBottom()) + ""));

    return items;
  }
}
