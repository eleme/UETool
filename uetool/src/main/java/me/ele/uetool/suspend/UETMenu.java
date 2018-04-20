package me.ele.uetool.suspend;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import java.util.ArrayList;
import java.util.List;
import me.ele.uetool.R;
import me.ele.uetool.TransparentActivity;
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
        UETool.getInstance().open(provider.provide(), TransparentActivity.Type.TYPE_EDIT_ATTR);
      }
    }));
    subMenus.add(new UETSubMenu.SubMenu("网格栅栏", R.drawable.uet_show_gridding,
        new OnClickListener() {
          @Override public void onClick(View v) {
            UETool.getInstance()
                .open(provider.provide(), TransparentActivity.Type.TYPE_SHOW_GRDDING);
          }
        }));
    subMenus.add(new UETSubMenu.SubMenu("相对位置", R.drawable.uet_relative_position,
        new OnClickListener() {
          @Override public void onClick(View v) {
            UETool.getInstance().open(provider.provide());
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
    final boolean isOpen = vSubMenuContainer.getTranslationX() <= -vSubMenuContainer.getWidth();
    animator.setInterpolator(
        isOpen ? defaultInterpolator : new ReverseInterpolator(defaultInterpolator));
    animator.removeAllListeners();
    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        vSubMenuContainer.setVisibility(VISIBLE);
      }

      @Override public void onAnimationEnd(Animator animation) {
        if (!isOpen) {
          vSubMenuContainer.setVisibility(GONE);
        }
      }
    });
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
