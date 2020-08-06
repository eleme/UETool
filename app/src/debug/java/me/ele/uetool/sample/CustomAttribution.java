package me.ele.uetool.sample;

import java.util.ArrayList;
import java.util.List;

import me.ele.uetool.base.Element;
import me.ele.uetool.base.IAttrs;
import me.ele.uetool.base.item.Item;
import me.ele.uetool.base.item.TextItem;

public class CustomAttribution implements IAttrs {

    @Override
    public List<Item> getAttrs(Element element) {
        List<Item> items = new ArrayList<>();
        if (element.getView() instanceof CustomView) {
            CustomView view = (CustomView) element.getView();
            items.add(new TextItem("More", view.getMoreAttribution()));
        }
        if (element.getView().getTag(R.id.uetool_xml) != null) {
            items.add(new TextItem("XML", element.getView().getTag(R.id.uetool_xml).toString()));
        }
        if (element.getView().getTag(R.id.uetool_xml_view_stub) != null) {
            items.add(new TextItem("XML_VIEW_STUB", element.getView().getTag(R.id.uetool_xml_view_stub).toString()));
        }
        return items;
    }
}
