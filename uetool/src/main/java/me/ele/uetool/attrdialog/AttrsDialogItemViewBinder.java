package me.ele.uetool.attrdialog;

import android.support.v7.widget.RecyclerView;

import me.ele.uetool.AttrsDialog;
import me.ele.uetool.base.ItemViewBinder;
import me.ele.uetool.base.item.Item;

/**
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-07-08 23:37
 */
public abstract class AttrsDialogItemViewBinder<T extends Item, VH extends AttrsDialog.Adapter.BaseViewHolder<T>> implements ItemViewBinder<T, VH> {

    protected AttrsDialog.AttrDialogCallback getAttrDialogCallback(RecyclerView.Adapter adapter) {
        AttrsDialog.AttrDialogCallback callback = null;
        if (adapter instanceof AttrsDialog.Adapter) {
            callback = ((AttrsDialog.Adapter) adapter).getAttrDialogCallback();
        }
        return callback;
    }
}
