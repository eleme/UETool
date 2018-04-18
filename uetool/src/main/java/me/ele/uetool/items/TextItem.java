package me.ele.uetool.items;

public class TextItem implements Item {

  private String name;
  private String detail;
  private boolean enableCopy;

  public TextItem(String name, String detail) {
    this.name = name;
    this.detail = detail;
  }

  public TextItem(String name, String detail, boolean enableCopy) {
    this.name = name;
    this.detail = detail;
    this.enableCopy = enableCopy;
  }

  public String getName() {
    return name;
  }

  public String getDetail() {
    return detail;
  }

  public boolean isEnableCopy() {
    return enableCopy;
  }
}