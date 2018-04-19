package me.ele.uetool.suspend;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import me.ele.uetool.R;
import me.ele.uetool.UETool;

public class UETMenu extends LinearLayout {

  private View vMenu;
  private ViewGroup vSubMenuContainer;
  private ValueAnimator animator;
  private Interpolator defaultInterpolator = new AccelerateDecelerateInterpolator();
  private List<UETSubMenu.SubMenu> subMenus = new ArrayList<>();

  public UETMenu(final Context context, final CurrentTopActivityProvider provider) {
    super(context);
    inflate(context, R.layout.uet_menu_layout, this);
    setGravity(Gravity.CENTER_VERTICAL);
    vMenu = findViewById(R.id.menu);
    vSubMenuContainer = findViewById(R.id.sub_menu_container);

    subMenus.add(new UETSubMenu.SubMenu("捕捉控件", R.drawable.uet_edit_attr, new OnClickListener() {
      @Override public void onClick(View v) {
        UETool.getInstance().open(provider.provide());
      }
    }));
    subMenus.add(new UETSubMenu.SubMenu("网格栅栏", R.drawable.uet_show_gridding,
        new OnClickListener() {
          @Override public void onClick(View v) {
            Toast.makeText(getContext(), "敬请期待", Toast.LENGTH_SHORT).show();
          }
        }));

    for (UETSubMenu.SubMenu subMenu : subMenus) {
      UETSubMenu uetSubMenu = new UETSubMenu(getContext());
      uetSubMenu.update(subMenu);
      vSubMenuContainer.addView(uetSubMenu);
    }

    vMenu.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        startAnim();
      }
    });
  }

  private void startAnim() {
    ensureAnim();
    TimeInterpolator interpolator;
    if (vSubMenuContainer.getTranslationX() > -vSubMenuContainer.getWidth()) {
      interpolator = new ReverseInterpolator(defaultInterpolator);
    } else {
      interpolator = defaultInterpolator;
    }
    animator.setInterpolator(interpolator);
    animator.start();
  }

  private void ensureAnim() {
    if (animator == null) {
      animator = ValueAnimator.ofInt(-vSubMenuContainer.getWidth(), 0);
      animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          vSubMenuContainer.setTranslationX((int) animation.getAnimatedValue());
        }
      });
      animator.setDuration(400);
    }
  }

  public View getMenuView() {
    return vMenu;
  }

  public interface CurrentTopActivityProvider {
    Activity provide();
  }

  private static class ReverseInterpolator implements TimeInterpolator {

    private TimeInterpolator mWrappedInterpolator;

    ReverseInterpolator(TimeInterpolator interpolator) {
      mWrappedInterpolator = interpolator;
    }

    @Override public float getInterpolation(float input) {
      return mWrappedInterpolator.getInterpolation(Math.abs(input - 1f));
    }
  }
}
