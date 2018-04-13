package me.ele.uetool.suspend;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import me.ele.uetool.R;
import me.ele.uetool.UETool;

public class UETMenu extends LinearLayout {

  private View vMenu;
  private ViewGroup vSubMenuContainer;

  public UETMenu(final Context context, final CurrentTopActivityProvider provider) {
    super(context);
    inflate(context, R.layout.uet_menu_layout, this);
    vMenu = findViewById(R.id.menu);
    vSubMenuContainer = findViewById(R.id.sub_menu_container);

    vMenu.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        UETool.getInstance().open(provider.provide());
      }
    });
  }

  public interface CurrentTopActivityProvider {
    Activity provide();
  }
}
