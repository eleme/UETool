package me.ele.uetool.sample;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import me.ele.uetool.base.DimenUtil;

public class CustomDialog extends Dialog {

  public CustomDialog(@NonNull Context context) {
    super(context);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_layout);
  }

  @Override public void show() {
    super.show();
    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    layoutParams.setTitle("???");
    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    getWindow().getDecorView().setPadding(DimenUtil.dip2px(30), 0, DimenUtil.dip2px(30), 0);
    getWindow().setAttributes(layoutParams);
  }
}
