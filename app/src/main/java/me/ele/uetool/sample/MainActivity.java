package me.ele.uetool.sample;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import me.ele.uetool.UETool;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final SwitchCompat control = findViewById(R.id.control);
    control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          if (!UETool.showUETMenu()) {
            control.setChecked(false);
          }
        } else {
          UETool.dismissUETMenu();
        }
      }
    });
    control.setChecked(true);

    updateDraweeView();
    updateSpanTextView();
    updateCustomView();
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn1:
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
        break;
      case R.id.btn2:
        new AlertDialog.Builder(this).setTitle("title")
            .setMessage("this is a dialog")
            .create()
            .show();
        break;
    }
  }

  private void updateDraweeView() {
    SimpleDraweeView draweeView = findViewById(R.id.drawee_view);
    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
        .setUri(
            "http://p0.ifengimg.com/pmop/2017/0823/3B8D6E5B199841F33C1FFB62D849C1D89F6BAA2B_size79_w240_h240.gif")
        .setAutoPlayAnimations(true)
        .build();
    draweeView.setController(draweeController);
  }

  private void updateSpanTextView() {
    TextView spanTextView = findViewById(R.id.span);
    SpannableString spannableString = new SpannableString("  海底捞火锅");
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_food_new);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
    spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    spanTextView.setText(spannableString);
  }

  private void updateCustomView() {
    CustomView customView = findViewById(R.id.custom);
    customView.setMoreAttribution("more attribution");
  }
}
