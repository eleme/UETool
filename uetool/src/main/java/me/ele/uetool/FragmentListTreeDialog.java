package me.ele.uetool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.*;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

public class FragmentListTreeDialog extends Dialog implements Provider {

    private ViewGroup containerView;
    private RegionView regionView;

    public FragmentListTreeDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.uet_dialog_fragment_list_tree);

        containerView = findViewById(R.id.container);
        regionView = findViewById(R.id.region);
        CheckBox checkBox = findViewById(R.id.checkbox);

        createTree(checkBox.isChecked());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                createTree(isChecked);
            }
        });

    }

    //  创建 fragment tree
    private void createTree(boolean showPackageName) {
        TreeNode root = TreeNode.root();

        Activity activity = UETool.getInstance().getTargetActivity();
        if (activity instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            createTreeNode(root, fragmentManager, showPackageName);
        }

        containerView.removeAllViews();

        AndroidTreeView tView = new AndroidTreeView(getContext(), root);
        tView.setDefaultAnimation(true);
        tView.setUse2dScroll(true);
        tView.setDefaultContainerStyle(R.style.uet_TreeNodeStyleCustom);
        containerView.addView(tView.getView());

        tView.expandAll();
    }

    //  递归创建 fragment tree node
    private TreeNode createTreeNode(TreeNode rootNode, FragmentManager fragmentManager, boolean showPackageName) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            TreeNode node = new TreeNode(new TreeItem(fragment, showPackageName)).setViewHolder(new TreeItemVH(getContext(), this));
            FragmentManager childManager = fragment.getChildFragmentManager();
            rootNode.addChild(createTreeNode(node, childManager, showPackageName));
        }
        return rootNode;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClickTreeItem(RectF rectF) {
        regionView.drawRegion(rectF);
    }

    public static class TreeItemVH extends TreeNode.BaseNodeViewHolder<TreeItem> {

        private TextView nameView;
        private ImageView arrowView;

        private Provider provider;

        public TreeItemVH(Context context, Provider provider) {
            super(context);
            this.provider = provider;
        }

        @Override
        public View createNodeView(TreeNode node, final TreeItem value) {
            final View view = LayoutInflater.from(context).inflate(R.layout.uet_cell_tree, null, false);

            nameView = view.findViewById(R.id.name);
            arrowView = view.findViewById(R.id.arrow);

            nameView.setText(Html.fromHtml(value.name));

            if (value.rectF != null) {
                nameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (provider != null) {
                            provider.onClickTreeItem(value.rectF);
                        }
                    }
                });
            }

            return view;
        }

        @Override
        public void toggle(boolean active) {
            super.toggle(active);

            arrowView.animate().setDuration(200).rotation(active ? 90 : 0).start();
        }
    }

    public static class TreeItem {

        public String name;
        public RectF rectF;

        public TreeItem(Fragment fragment, boolean showPackageName) {
            initName(fragment, showPackageName);
            initRect(fragment);
        }

        private void initName(Fragment fragment, boolean showPackageName) {
            StringBuilder sb = new StringBuilder();
            sb.append(showPackageName ? fragment.getClass().getName() : fragment.getClass().getSimpleName());
            sb.append("[visible=").append(fragment.isVisible()).append(", hashCode=").append(fragment.hashCode()).append("]");
            name = sb.toString();
            if (fragment.isVisible()) {
                name = "<u>" + name + "</u>";
            }
        }

        private void initRect(Fragment fragment) {
            if (fragment.isVisible()) {
                View view = fragment.getView();
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                rectF = new RectF(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
            }
        }
    }
}
