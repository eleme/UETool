package me.ele.uetool;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static me.ele.uetool.base.DimenUtil.dip2px;

public class UETSubMenu extends LinearLayout {

    private final int padding = dip2px(5);

    private ImageView vImage;
    private TextView vTitle;

    public UETSubMenu(Context context) {
        this(context, null);
    }

    public UETSubMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UETSubMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.uet_sub_menu_layout, this);
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        setPadding(padding, 0, padding, 0);
        setTranslationY(dip2px(2));
        vImage = findViewById(R.id.image);
        vTitle = findViewById(R.id.title);
    }

    public void update(SubMenu subMenu) {
        vImage.setImageResource(subMenu.getImageRes());
        vTitle.setText(subMenu.getTitle());
        setOnClickListener(subMenu.getOnClickListener());
    }

    public static class SubMenu {
        private String title;
        private int imageRes;
        private OnClickListener onClickListener;

        public SubMenu(String title, int imageRes, OnClickListener onClickListener) {
            this.title = title;
            this.imageRes = imageRes;
            this.onClickListener = onClickListener;
        }

        public String getTitle() {
            return title;
        }

        public int getImageRes() {
            return imageRes;
        }

        public OnClickListener getOnClickListener() {
            return onClickListener;
        }
    }
}
