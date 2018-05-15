package me.ele.uetool.base.item;

import android.graphics.Bitmap;

public class BitmapItem extends Item {

    private String name;
    private Bitmap bitmap;

    public BitmapItem(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public boolean isValid() {
        if (bitmap == null) {
            return false;
        }
        return true;
    }
}
