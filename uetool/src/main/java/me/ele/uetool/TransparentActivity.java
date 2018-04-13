package me.ele.uetool;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class TransparentActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Util.setStatusBarColor(getWindow(), Color.TRANSPARENT);
    Util.enableFullscreen(getWindow());
    setContentView(R.layout.uet_activity_transparent);
    Toast.makeText(this, "捕捉控件已打开，请点击您感兴趣的控件", Toast.LENGTH_SHORT).show();
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    UETool.getInstance().clear();
  }
}
