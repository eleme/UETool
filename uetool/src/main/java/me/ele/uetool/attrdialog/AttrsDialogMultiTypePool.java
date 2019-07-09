package me.ele.uetool.attrdialog;

import java.util.ArrayList;
import java.util.List;

import me.ele.uetool.base.ItemViewBinder;
import me.ele.uetool.base.item.Item;

/**
 * @author: weishenhong <a href="mailto:weishenhong@bytedance.com">contact me.</a>
 * @date: 2019-07-08 22:50
 */
public class AttrsDialogMultiTypePool {

    private List<Class> classes = new ArrayList<>();
    private List<ItemViewBinder> binders = new ArrayList<>();

    public <T extends Item> void register(
            Class<T> clazz,
            ItemViewBinder<T, ?> binder) {
        if (classes.contains(clazz)) {
            return;
        }
        classes.add(clazz);
        binders.add(binder);
    }


    public ItemViewBinder<?, ?> getItemViewBinder(int index) {
        if (index < 0 || index > binders.size()) {
            throw new RuntimeException("un support view holder type:" + index);
        }
        return binders.get(index);
    }

    public int getItemType(Object item) {
        int index = classes.indexOf(item.getClass());
        if (index == -1) {
            throw new RuntimeException("un support class type:" + item.getClass().getName());
        }
        return index;
    }
}