package me.ele.uetool.attrdialog.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.ele.uetool.AttrsDialog;
import me.ele.uetool.attrdialog.AttrsDialogItemViewBinder;
import me.ele.uetool.base.item.BitmapItem;

/**
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-07-08 23:46
 */
public class BitmapItemBinder extends AttrsDialogItemViewBinder<BitmapItem, AttrsDialog.Adapter.BitmapInfoViewHolder> {
    @NonNull
    @Override
    public AttrsDialog.Adapter.BitmapInfoViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, RecyclerView.Adapter adapter) {
        return AttrsDialog.Adapter.BitmapInfoViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AttrsDialog.Adapter.BitmapInfoViewHolder holder, @NonNull BitmapItem item) {
        holder.bindView(item);
    }
}
