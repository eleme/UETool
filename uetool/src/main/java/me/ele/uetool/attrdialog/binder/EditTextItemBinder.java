package me.ele.uetool.attrdialog.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.ele.uetool.AttrsDialog;
import me.ele.uetool.attrdialog.AttrsDialogItemViewBinder;
import me.ele.uetool.base.item.EditTextItem;

/**
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-07-08 23:46
 */
public class EditTextItemBinder extends AttrsDialogItemViewBinder<EditTextItem, AttrsDialog.Adapter.EditTextViewHolder<EditTextItem>> {
    @NonNull
    @Override
    public AttrsDialog.Adapter.EditTextViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, RecyclerView.Adapter adapter) {
        return AttrsDialog.Adapter.EditTextViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AttrsDialog.Adapter.EditTextViewHolder holder, @NonNull EditTextItem item) {
        holder.bindView(item);
    }
}
