package me.ele.uetool.attrdialog.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.ele.uetool.AttrsDialog;
import me.ele.uetool.attrdialog.AttrsDialogItemViewBinder;
import me.ele.uetool.base.item.BriefDescItem;

/**
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-07-08 23:46
 */
public class BriefDescItemBinder extends AttrsDialogItemViewBinder<BriefDescItem, AttrsDialog.Adapter.BriefDescViewHolder> {
    @NonNull
    @Override
    public AttrsDialog.Adapter.BriefDescViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, RecyclerView.Adapter adapter) {

        return AttrsDialog.Adapter.BriefDescViewHolder.newInstance(parent, getAttrDialogCallback(adapter));
    }

    @Override
    public void onBindViewHolder(@NonNull AttrsDialog.Adapter.BriefDescViewHolder holder, @NonNull BriefDescItem item) {
        holder.bindView(item);
    }
}
